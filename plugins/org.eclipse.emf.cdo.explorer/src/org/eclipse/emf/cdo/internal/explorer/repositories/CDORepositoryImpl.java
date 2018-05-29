/*
 * Copyright (c) 2015, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.internal.explorer.repositories;

import org.eclipse.emf.cdo.common.CDOCommonSession.Options.PassiveUpdateMode;
import org.eclipse.emf.cdo.common.branch.CDOBranch;
import org.eclipse.emf.cdo.common.branch.CDOBranchChangedEvent;
import org.eclipse.emf.cdo.common.branch.CDOBranchChangedEvent.ChangeKind;
import org.eclipse.emf.cdo.common.branch.CDOBranchCreationContext;
import org.eclipse.emf.cdo.common.branch.CDOBranchManager;
import org.eclipse.emf.cdo.common.branch.CDOBranchPoint;
import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.emf.cdo.common.id.CDOIDGenerator;
import org.eclipse.emf.cdo.common.id.CDOIDUtil;
import org.eclipse.emf.cdo.common.revision.CDORevision;
import org.eclipse.emf.cdo.common.revision.CDORevisionHandler;
import org.eclipse.emf.cdo.explorer.CDOExplorerManager.ElementsChangedEvent;
import org.eclipse.emf.cdo.explorer.checkouts.CDOCheckout;
import org.eclipse.emf.cdo.explorer.repositories.CDORepository;
import org.eclipse.emf.cdo.explorer.repositories.CDORepositoryElement;
import org.eclipse.emf.cdo.internal.explorer.AbstractElement;
import org.eclipse.emf.cdo.internal.explorer.bundle.OM;
import org.eclipse.emf.cdo.net4j.CDONet4jSessionConfiguration;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.session.CDOSessionConfiguration;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.util.CDOUtil;
import org.eclipse.emf.cdo.view.CDOView;

import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.util.StringUtil;
import org.eclipse.net4j.util.UUIDGenerator;
import org.eclipse.net4j.util.container.ContainerEvent;
import org.eclipse.net4j.util.container.IContainerEvent;
import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.container.IPluginContainer;
import org.eclipse.net4j.util.event.IEvent;
import org.eclipse.net4j.util.event.IListener;
import org.eclipse.net4j.util.lifecycle.ILifecycle;
import org.eclipse.net4j.util.lifecycle.LifecycleEventAdapter;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.security.IPasswordCredentials;
import org.eclipse.net4j.util.security.PasswordCredentials;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.spi.cdo.InternalCDOSession;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.provider.IProviderHints;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Eike Stepper
 */
public abstract class CDORepositoryImpl extends AbstractElement implements CDORepository
{
  public static final String PROP_NAME = "name";

  public static final String PROP_REALM = "realm";

  public static final String REPOSITORY_KEY = CDORepository.class.getName();

  private static final boolean READABLE_IDS = OMPlatform.INSTANCE.isProperty("cdo.explorer.readableIDs");

  private static final boolean SET_USER_NAME = OMPlatform.INSTANCE.isProperty("cdo.explorer.setUserName");

  private final Set<CDOCheckout> checkouts = new HashSet<CDOCheckout>();

  private final Set<CDOCheckout> openCheckouts = new HashSet<CDOCheckout>();

  private final IListener branchManagerListener = new IListener()
  {
    public void notifyEvent(IEvent event)
    {
      if (event instanceof CDOBranchChangedEvent)
      {
        CDOBranchChangedEvent e = (CDOBranchChangedEvent)event;
        if (e.getChangeKind() == ChangeKind.RENAMED)
        {
          CDORepositoryManagerImpl manager = getManager();
          if (manager != null)
          {
            manager.fireElementChangedEvent(ElementsChangedEvent.StructuralImpact.NONE, e.getBranch());
          }
        }
      }
    }
  };

  private final IListener mainBranchListener = new IListener()
  {
    public void notifyEvent(IEvent event)
    {
      if (event instanceof IContainerEvent)
      {
        @SuppressWarnings("unchecked")
        IContainerEvent<CDOBranch> e = (IContainerEvent<CDOBranch>)event;

        fireEvent(new ContainerEvent<CDOBranch>(CDORepositoryImpl.this, Arrays.asList(e.getDeltas())));
      }
    }
  };

  private final IListener sessionReleaser = new LifecycleEventAdapter()
  {
    @Override
    protected void onDeactivated(ILifecycle lifecycle)
    {
      releaseSession();
    }
  };

  private String name;

  private VersioningMode versioningMode;

  private IDGeneration idGeneration;

  private String realm;

  private State state = State.Disconnected;

  private boolean explicitlyConnected;

  private int sessionRefCount;

  private CDOSession session;

  public static final String PROP_VERSIONING_MODE = "versioningMode";

  public static final String PROP_ID_GENERATION = "idGeneration";

  public CDORepositoryImpl()
  {
  }

  public IManagedContainer getContainer()
  {
    return IPluginContainer.INSTANCE;
  }

  @Override
  public CDORepositoryManagerImpl getManager()
  {
    return OM.getRepositoryManager();
  }

  public final String getName()
  {
    return name;
  }

  public final VersioningMode getVersioningMode()
  {
    return versioningMode;
  }

  public final IDGeneration getIDGeneration()
  {
    return idGeneration;
  }

  public IPasswordCredentials getCredentials()
  {
    return getCredentials(null);
  }

  public IPasswordCredentials getCredentials(String realm)
  {
    try
    {
      ISecurePreferences securePreferences = getSecurePreferences();
      if (securePreferences != null)
      {
        String path = getSecurePath(securePreferences);
        if (securePreferences.nodeExists(path))
        {
          ISecurePreferences node = securePreferences.node(path);
          String userID = node.get("username", null);

          if (!StringUtil.isEmpty(userID))
          {
            String password = node.get("password", null);
            return new PasswordCredentials(userID, password);
          }
        }
      }
    }
    catch (Exception ex)
    {
      OM.LOG.error(ex);
    }

    return null;
  }

  public void setCredentials(IPasswordCredentials credentials)
  {
    try
    {
      ISecurePreferences securePreferences = getSecurePreferences();
      if (securePreferences != null)
      {
        String path = getSecurePath(securePreferences);
        ISecurePreferences node = securePreferences.node(path);

        node.put("uri", getURI(), false);
        node.put("username", credentials.getUserID(), false);
        node.put("password", new String(credentials.getPassword()), true);
        node.flush();
      }
    }
    catch (Exception ex)
    {
      OM.LOG.error(ex);
    }
  }

  public boolean isInteractive()
  {
    return false;
  }

  public final State getState()
  {
    return state;
  }

  public final boolean isConnected()
  {
    return session != null;
  }

  public final void connect()
  {
    explicitlyConnected = true;
    doConnect();
  }

  protected void doConnect()
  {
    boolean connected = false;

    synchronized (this)
    {
      if (!isConnected())
      {
        try
        {
          state = State.Connecting;

          session = openSession();
          session.properties().put(REPOSITORY_KEY, this);
          session.addListener(new LifecycleEventAdapter()
          {
            @Override
            protected void onDeactivated(ILifecycle lifecycle)
            {
              disconnect();
            }
          });

          CDOBranchManager branchManager = session.getBranchManager();
          branchManager.addListener(branchManagerListener);

          CDOBranch mainBranch = branchManager.getMainBranch();
          mainBranch.addListener(mainBranchListener);

          state = State.Connected;
        }
        catch (RuntimeException ex)
        {
          state = State.Disconnected;
          throw ex;
        }
        catch (Error ex)
        {
          state = State.Disconnected;
          throw ex;
        }

        connected = true;
      }
    }

    if (connected)
    {
      CDORepositoryManagerImpl manager = getManager();
      if (manager != null)
      {
        manager.fireRepositoryConnectionEvent(this, session, true);
      }
    }
  }

  public final void disconnect()
  {
    explicitlyConnected = false;
    doDisconnect(false);
  }

  protected void doDisconnect(boolean force)
  {
    if (!force)
    {
      if (explicitlyConnected || sessionRefCount != 0)
      {
        return;
      }
    }

    boolean disconnected = false;
    CDOSession oldSession = null;

    synchronized (this)
    {
      if (isConnected())
      {
        state = State.Disconnecting;
        oldSession = session;

        try
        {
          closeSession();
        }
        finally
        {
          session = null;
          state = State.Disconnected;
        }

        disconnected = true;
      }
    }

    if (disconnected)
    {
      CDORepositoryManagerImpl manager = getManager();
      if (manager != null)
      {
        manager.fireRepositoryConnectionEvent(this, oldSession, false);
      }
    }
  }

  public final void disconnectIfUnused()
  {
    synchronized (checkouts)
    {
      if (openCheckouts.isEmpty())
      {
        doDisconnect(false);
      }
    }
  }

  public final CDOSession getSession()
  {
    return session;
  }

  public CDOSession acquireSession()
  {
    ++sessionRefCount;
    doConnect();

    return session;
  }

  public void releaseSession()
  {
    --sessionRefCount;
    doDisconnect(false);
  }

  public CDOTransaction openTransaction(CDOBranchPoint target, ResourceSet resourceSet)
  {
    CDOSession session = acquireSession();
    CDOTransaction transaction = session.openTransaction(target, resourceSet);
    transaction.addListener(sessionReleaser);
    return transaction;
  }

  public CDOTransaction openTransaction(String durableLockingID, ResourceSet resourceSet)
  {
    CDOSession session = acquireSession();
    CDOTransaction transaction = session.openTransaction(durableLockingID, resourceSet);
    transaction.addListener(sessionReleaser);
    return transaction;
  }

  public CDOView openView(CDOBranchPoint target, ResourceSet resourceSet)
  {
    CDOSession session = acquireSession();
    CDOView view = session.openView(target, resourceSet);
    view.addListener(sessionReleaser);
    return view;
  }

  public CDOView openView(String durableLockingID, ResourceSet resourceSet)
  {
    CDOSession session = acquireSession();
    CDOView view = session.openView(durableLockingID, resourceSet);
    view.addListener(sessionReleaser);
    return view;
  }

  @Override
  public void delete(boolean deleteContents)
  {
    disconnect();

    CDORepositoryManagerImpl manager = getManager();
    if (manager != null)
    {
      manager.removeElement(this);
    }

    super.delete(deleteContents);
  }

  public final CDOCheckout[] getCheckouts()
  {
    synchronized (checkouts)
    {
      return checkouts.toArray(new CDOCheckout[checkouts.size()]);
    }
  }

  public final void addCheckout(CDOCheckout checkout)
  {
    synchronized (checkouts)
    {
      checkouts.add(checkout);
    }
  }

  public final void removeCheckout(CDOCheckout checkout)
  {
    synchronized (checkouts)
    {
      checkouts.remove(checkout);
    }
  }

  public final CDOSession openCheckout(CDOCheckout checkout)
  {
    connect();

    synchronized (checkouts)
    {
      openCheckouts.add(checkout);
    }

    return session;
  }

  public final void closeCheckout(CDOCheckout checkout)
  {
    synchronized (checkouts)
    {
      openCheckouts.remove(checkout);
    }
  }

  public final boolean isEmpty()
  {
    if (isConnected())
    {
      return session.getBranchManager().getMainBranch().isEmpty();
    }

    return false;
  }

  public final CDOBranch[] getElements()
  {
    if (isConnected())
    {
      return session.getBranchManager().getMainBranch().getBranches();
    }

    return new CDOBranch[0];
  }

  @Override
  @SuppressWarnings({ "rawtypes" })
  public Object getAdapter(Class adapter)
  {
    if (isConnected())
    {
      if (adapter == CDOSession.class)
      {
        return session;
      }

      if (adapter == CDOBranchCreationContext.class)
      {
        if (session.getRepositoryInfo().isSupportingBranches())
        {
          return new CDOBranchCreationContext()
          {
            public CDOBranchPoint getBase()
            {
              return session.getBranchManager().getMainBranch().getHead();
            }
          };
        }
      }
    }

    if (adapter == CDORepositoryElement.class)
    {
      return new CDORepositoryElement()
      {
        public CDORepository getRepository()
        {
          return CDORepositoryImpl.this;
        }

        public int getBranchID()
        {
          return CDOBranch.MAIN_BRANCH_ID;
        }

        public long getTimeStamp()
        {
          return CDOBranchPoint.UNSPECIFIED_DATE;
        }

        public CDOID getObjectID()
        {
          return null;
        }
      };
    }

    return super.getAdapter(adapter);
  }

  @Override
  public String toString()
  {
    return getLabel();
  }

  @Override
  protected void init(File folder, String type, Properties properties)
  {
    super.init(folder, type, properties);
    name = properties.getProperty(PROP_NAME);

    String versioningModeProperty = properties.getProperty(PROP_VERSIONING_MODE);
    versioningMode = versioningModeProperty == null ? null : VersioningMode.valueOf(versioningModeProperty);

    String idGenerationProperty = properties.getProperty(PROP_ID_GENERATION);
    idGeneration = idGenerationProperty == null ? null : IDGeneration.valueOf(idGenerationProperty);

    realm = properties.getProperty(PROP_REALM);
  }

  @Override
  protected void collectProperties(Properties properties)
  {
    super.collectProperties(properties);
    properties.setProperty(PROP_NAME, name);

    if (versioningMode != null)
    {
      properties.setProperty(PROP_VERSIONING_MODE, versioningMode.toString());
    }

    if (idGeneration != null)
    {
      properties.setProperty(PROP_ID_GENERATION, idGeneration.toString());
    }

    if (realm != null)
    {
      properties.setProperty(PROP_REALM, realm);
    }
  }

  protected IConnector getConnector()
  {
    IManagedContainer container = getContainer();
    return Net4jUtil.getConnector(container, getConnectorType(), getConnectorDescription());
  }

  protected CDOSessionConfiguration createSessionConfiguration()
  {
    IConnector connector = getConnector();

    CDONet4jSessionConfiguration config = CDONet4jUtil.createNet4jSessionConfiguration();
    config.setConnector(connector);
    config.setRepositoryName(name);

    if (READABLE_IDS)
    {
      config.setIDGenerator(new CDOIDGenerator()
      {
        private final UUIDGenerator decoder = new UUIDGenerator();

        private Map<EClass, AtomicInteger> counters;

        private int typeCounter;

        public CDOID generateCDOID(EObject object)
        {
          if (counters == null)
          {
            counters = new HashMap<EClass, AtomicInteger>();
            CDOView view = CDOUtil.getView(object);
            view.getSession().getRevisionManager().handleRevisions(null, null, false, CDOBranchPoint.UNSPECIFIED_DATE, false, new CDORevisionHandler()
            {
              public boolean handleRevision(CDORevision revision)
              {
                EClass eClass = revision.getEClass();
                AtomicInteger counter = getCounter(eClass);

                String id = revision.getID().toString();
                id = id.substring(0, id.length() - "A".length());
                id = id.substring(id.lastIndexOf('_') + 1);

                int counterValue = Integer.parseInt(id);
                if (counterValue > counter.get())
                {
                  counter.set(counterValue);
                }

                return true;
              }
            });
          }

          EClass eClass = object.eClass();

          String type = eClass.getName();
          if (type.length() > 16)
          {
            String suffix = "_" + (++typeCounter);
            type = type.substring(0, 16 - suffix.length()) + suffix;
            System.out.println(eClass.getName() + " --> " + type);
          }

          type = "_" + type;

          AtomicInteger counter = getCounter(eClass);

          String str = "_" + counter.incrementAndGet();
          String id = type + "____________________________________".substring(0, 22 - type.length() - str.length()) + str + "A";

          if ("_CDOResource_________5A".equals(id))
          {
            System.out.println();
          }

          byte[] value = decoder.decode(id);

          String encoded = decoder.encode(value);
          if (!encoded.equals(id))
          {
            throw new IllegalStateException();
          }

          return CDOIDUtil.createUUID(value);
        }

        private AtomicInteger getCounter(EClass eClass)
        {
          AtomicInteger counter = counters.get(eClass);
          if (counter == null)
          {
            counter = new AtomicInteger();
            counters.put(eClass, counter);
          }
          return counter;
        }

        public void reset()
        {
        }
      });
    }

    return config;
  }

  protected CDOSession openSession()
  {
    CDOSessionConfiguration sessionConfiguration = createSessionConfiguration();
    sessionConfiguration.setPassiveUpdateEnabled(true);
    sessionConfiguration.setPassiveUpdateMode(PassiveUpdateMode.CHANGES);
    sessionConfiguration.setCredentialsProvider(this);

    CDOSession session = sessionConfiguration.openSession();
    session.options().setGeneratedPackageEmulationEnabled(true);

    if (SET_USER_NAME && StringUtil.isEmpty(session.getUserID()))
    {
      String userName = System.getProperty("user.name");
      if (!StringUtil.isEmpty(userName))
      {
        ((InternalCDOSession)session).setUserID(userName);
      }
    }

    return session;
  }

  protected void closeSession()
  {
    session.close();
  }

  private String getSecurePath(ISecurePreferences securePreferences)
  {
    String stateLocation = OM.getStateLocation().replace('/', '\\');
    String id = getID().replace('/', '_');

    return "CDO/" + stateLocation + "/" + id;
  }

  private static ISecurePreferences getSecurePreferences() throws IOException
  {
    Map<Object, Object> options = new HashMap<Object, Object>();
    options.put(IProviderHints.PROMPT_USER, Boolean.FALSE);

    return SecurePreferencesFactory.open(null, options);
  }
}
