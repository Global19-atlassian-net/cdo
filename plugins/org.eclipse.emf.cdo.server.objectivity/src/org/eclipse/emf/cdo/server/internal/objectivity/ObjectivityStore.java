/**
 * Copyright (c) 2004 - 2010 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Simon McDuff - initial API and implementation
 *    Ibrahim Sallam - code refactoring for CDO 3.0
 */
package org.eclipse.emf.cdo.server.internal.objectivity;

import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.emf.cdo.common.id.CDOIDUtil;
import org.eclipse.emf.cdo.common.revision.CDORevision;
import org.eclipse.emf.cdo.server.ISession;
import org.eclipse.emf.cdo.server.IStoreAccessor;
import org.eclipse.emf.cdo.server.ITransaction;
import org.eclipse.emf.cdo.server.IView;
import org.eclipse.emf.cdo.server.internal.objectivity.bundle.OM;
import org.eclipse.emf.cdo.server.internal.objectivity.clustering.ObjyPlacementManager;
import org.eclipse.emf.cdo.server.internal.objectivity.db.ObjyCommitInfoHandler;
import org.eclipse.emf.cdo.server.internal.objectivity.db.ObjyConnection;
import org.eclipse.emf.cdo.server.internal.objectivity.db.ObjyPackageHandler;
import org.eclipse.emf.cdo.server.internal.objectivity.db.ObjyPropertyMapHandler;
import org.eclipse.emf.cdo.server.internal.objectivity.db.ObjySchema;
import org.eclipse.emf.cdo.server.internal.objectivity.db.ObjyScope;
import org.eclipse.emf.cdo.server.internal.objectivity.db.ObjySession;
import org.eclipse.emf.cdo.server.internal.objectivity.schema.ObjyStoreInfo;
import org.eclipse.emf.cdo.server.internal.objectivity.utils.ObjyDb;
import org.eclipse.emf.cdo.server.objectivity.IObjectivityStore;
import org.eclipse.emf.cdo.server.objectivity.IObjectivityStoreConfig;
import org.eclipse.emf.cdo.spi.server.LongIDStore;
import org.eclipse.emf.cdo.spi.server.Store;
import org.eclipse.emf.cdo.spi.server.StoreAccessorPool;

import org.eclipse.net4j.util.ReflectUtil.ExcludeFromDump;
import org.eclipse.net4j.util.om.trace.ContextTracer;

import com.objy.db.app.Connection;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ObjectivityStore extends Store implements IObjectivityStore
{

  public static final String TYPE = "objectivity"; //$NON-NLS-1$

  private static final ContextTracer TRACER_DEBUG = new ContextTracer(OM.DEBUG, ObjectivityStore.class);

  protected ConcurrentLinkedQueue<ObjectivityStoreAccessor> writers = new ConcurrentLinkedQueue<ObjectivityStoreAccessor>();

  @ExcludeFromDump
  private transient StoreAccessorPool readerPool = new StoreAccessorPool(this, null);

  @ExcludeFromDump
  private transient StoreAccessorPool writerPool = new StoreAccessorPool(this, null);

  private ObjyConnection objyConnection = null;

  private IObjectivityStoreConfig storeConfig = null;

  private boolean firstTime = false;

  private int nActivate = 0;

  private boolean requiredToSupportAudits;

  private boolean requiredToSupportBranches;

  private ObjyCommitInfoHandler objyCommitInfoHandler = null;

  private ObjyPropertyMapHandler objyPropertyMapHandler = null;

  private ObjyPackageHandler objyPackageHandler = null;

  private boolean storeInitialized = false;

  private long creationTime = CDORevision.UNSPECIFIED_DATE;

  // private boolean resetData = false;

  public ObjectivityStore(IObjectivityStoreConfig config)
  {
    // super(TYPE, set(ChangeFormat.REVISION, ChangeFormat.DELTA), set(
    // RevisionTemporality.NONE, RevisionTemporality.AUDITING),
    // set(RevisionParallelism.NONE));
    // setRevisionTemporality(RevisionTemporality.AUDITING);
    super(TYPE, LongIDStore.OBJECT_ID_TYPES, set(ChangeFormat.REVISION, ChangeFormat.DELTA), set(
        RevisionTemporality.NONE, RevisionTemporality.AUDITING), set(RevisionParallelism.NONE,
        RevisionParallelism.BRANCHING));
    storeConfig = config;
  }

  private void initStore()
  {
    // the caller already used the StoreConfig to open the connection
    // to the FD so, get the current here.
    objyConnection = ObjyConnection.INSTANCE;
    objyConnection.setSessionMinCacheSize(storeConfig.getSessionMinCacheSize());
    objyConnection.setSessionMaxCacheSize(storeConfig.getSessionMaxCacheSize());

    // -----------------------------------------------------------------------
    // Initialize schema as needed, and also any other config information

    // connection to the FD.
    objyConnection.connect(storeConfig.getFdName());
    Connection.current().setUserClassLoader(this.getClass().getClassLoader());

    objyConnection.registerClass("org.eclipse.emf.cdo.server.internal.objectivity.schema.ObjyStoreInfo"); //$NON-NLS-1$
    objyConnection.registerClass("org.eclipse.emf.cdo.server.internal.objectivity.schema.ObjyPackageInfo"); //$NON-NLS-1$
    objyConnection.registerClass("org.eclipse.emf.cdo.server.internal.objectivity.schema.ObjyPackageUnit"); //$NON-NLS-1$
    objyConnection.registerClass("org.eclipse.emf.cdo.server.internal.objectivity.schema.ObjyCommitInfo"); //$NON-NLS-1$
    objyConnection.registerClass("org.eclipse.emf.cdo.server.internal.objectivity.schema.ObjyProperty"); //$NON-NLS-1$

    ObjySession objySession = objyConnection.getWriteSessionFromPool("Main"); //$NON-NLS-1$
    objySession.setRecoveryAutomatic(true);
    objySession.begin();

    ObjySchema.createBaseSchema();

    try
    {
      String repositoryName = getRepository().getName();
      // check if we initialized the store for the first time.
      {
        // we have one-to-one mapping between a store and a repository. In Objectivity, we
        // can still use one federation to store multiple repository, each will have its
        // own ObjyStoreInfo object in the default container.
        ObjyScope objyScope = new ObjyScope(repositoryName, ObjyDb.DEFAULT_CONT_NAME);
        ObjyStoreInfo objyStoreInfo = null;
        try
        {
          objyStoreInfo = (ObjyStoreInfo)objyScope.lookupObject(ObjyDb.OBJYSTOREINFO_NAME);
          creationTime = objyStoreInfo.getCreationTime();
        }
        catch (Exception ex)
        {
          // create the ObjyStoreInfo.
          objyStoreInfo = new ObjyStoreInfo(System.currentTimeMillis(), "...");
          objyScope.getContainerObj().cluster(objyStoreInfo);
          objyScope.nameObj(ObjyDb.OBJYSTOREINFO_NAME, objyStoreInfo);

          // flag as first time.
          firstTime = true;
        }
        creationTime = objyStoreInfo.getCreationTime();
      }

      // This is used for the package storage, it could be lazily done though!!! (verify)
      // ObjyScope.insureScopeExist(objySession, ObjyDb.CONFIGDB_NAME, ObjyDb.PACKAGESTORE_CONT_NAME);

      // make sure we have the root resource created.
      // ObjyDb.getOrCreateResourceList();
      objyCommitInfoHandler = new ObjyCommitInfoHandler(repositoryName);
      objyPropertyMapHandler = new ObjyPropertyMapHandler(repositoryName);
      objyPackageHandler = new ObjyPackageHandler(repositoryName);

      objySession.commit();

      storeInitialized = true;
    }
    catch (RuntimeException ex)
    {
      ex.printStackTrace();
      objySession.abort();
    }
    finally
    {
      objyConnection.returnSessionToPool(objySession);
    }
  }

  @Override
  protected IStoreAccessor createReader(ISession session)
  {
    // System.out
    // .println(">>>>IS:<<<< ObjectivityStore.createRead() - " + (session == null ? "null" : session.toString()));
    return new ObjectivityStoreAccessor(this, session);
  }

  @Override
  protected IStoreAccessor createWriter(ITransaction transaction)
  {
    // if (transaction == null)
    // {
    // System.out.println(">>>>IS:<<<< ObjectivityStore.createWriter() - transaction: null");
    // }
    // else
    // {
    // System.out.println(">>>>IS:<<<< ObjectivityStore.createWriter() - "
    // + (transaction.getSession() == null ? "null" : transaction.getSession().toString()));
    // }
    return new ObjectivityStoreAccessor(this, transaction);
  }

  @Override
  protected StoreAccessorPool getReaderPool(ISession session, boolean forReleasing)
  {
    return readerPool;
  }

  @Override
  protected StoreAccessorPool getWriterPool(IView view, boolean forReleasing)
  {
    return writerPool;
  }

  @Override
  public CDOID createObjectID(String val)
  {
    Long id = Long.valueOf(val);
    return CDOIDUtil.createLong(id);
  }

  public long getCreationTime()
  {
    return creationTime;
  }

  public boolean isFirstTime()
  {
    return firstTime;
  }

  public ObjyConnection getConnection()
  {
    return objyConnection;

  }

  public boolean isRequiredToSupportAudits()
  {
    return requiredToSupportAudits;
  }

  public boolean isRequiredToSupportBranches()
  {
    return requiredToSupportBranches;
  }

  @Override
  protected void doBeforeActivate()
  {
    requiredToSupportAudits = getRepository().isSupportingAudits();
    requiredToSupportBranches = getRepository().isSupportingBranches();
  }

  @Override
  protected void doActivate() throws Exception
  {
    super.doActivate();

    // lazy initialization of the store.
    if (!storeInitialized)
    {
      initStore();
    }

    nActivate++;

    if (TRACER_DEBUG.isEnabled())
    {
      TRACER_DEBUG.trace("doActivate - count: " + nActivate);
    }

    ObjySession objySession = objyConnection.getWriteSessionFromPool("Main");
    objySession.setRecoveryAutomatic(true);
    objySession.begin();

    try
    {
      if (!objySession.getFD().hasDB(getRepository().getName()))
      {
        // Create the repo DB.
        ObjyScope.insureScopeExist(objySession, getRepository().getName(), ObjyDb.DEFAULT_CONT_NAME);
        // ...do other initialisation of the repository here.
        // Note that in the current implementation we don't delete DBs by default, only delete
        // the containers (see ObjectivityStoreConfig.resetFD()) so any initialization done here
        // might not be repeated.
      }

      objySession.commit();
    }
    catch (RuntimeException ex)
    {
      objySession.abort();
    }
    finally
    {
      objyConnection.returnSessionToPool(objySession);
    }
  }

  @Override
  protected void doDeactivate() throws Exception
  {
    try
    {
      ObjySchema.resetCache();
      // System.out.println(" -------- doDeactivate() ObjectivityStore ----------");
    }
    finally
    {
      ObjyConnection.INSTANCE.disconnect();
    }

    // readerPool.dispose();
    // writerPool.dispose();
    super.doDeactivate();

  }

  public Map<String, String> getPropertyValues(Set<String> names)
  {
    ObjySession objySession = objyConnection.getReadSessionFromPool("Main");
    objySession.begin();
    Map<String, String> properties = objyPropertyMapHandler.getPropertyValues(names);
    objySession.commit();
    return properties;
  }

  public void setPropertyValues(Map<String, String> properties)
  {
    ObjySession objySession = objyConnection.getWriteSessionFromPool("Main");
    objySession.begin();
    objyPropertyMapHandler.setPropertyValues(properties);
    objySession.commit();
  }

  public void removePropertyValues(Set<String> names)
  {
    ObjySession objySession = objyConnection.getWriteSessionFromPool("Main");
    objySession.begin();
    objyPropertyMapHandler.removePropertyValues(names);
    objySession.commit();
  }

  public ObjyCommitInfoHandler getCommitInfoHandler()
  {
    return objyCommitInfoHandler;
  }

  public ObjyPackageHandler getPackageHandler()
  {
    return objyPackageHandler;
  }

  public ObjyPlacementManager getGlobalPlacementManager()
  {
    return ObjyConnection.INSTANCE.getDefaultPlacementManager();
  }

}
