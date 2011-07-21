/***************************************************************************
 * Copyright (c) 2004 - 2008 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 **************************************************************************/
package org.eclipse.emf.cdo.tests;

import org.eclipse.emf.cdo.CDOSession;
import org.eclipse.emf.cdo.CDOTransaction;
import org.eclipse.emf.cdo.analyzer.CDOFetchRuleManager;
import org.eclipse.emf.cdo.common.analyzer.CDOFetchRule;
import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.tests.model1.Model1Factory;
import org.eclipse.emf.cdo.tests.model1.Supplier;

import org.eclipse.emf.internal.cdo.CDOSessionImpl;
import org.eclipse.emf.internal.cdo.CDOTransactionImpl;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EContentAdapter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class ContentAdapterTest extends AbstractCDOTest
{
  public void testContentAdapterUsingCDOResource() throws Exception
  {
    final boolean notified[] = new boolean[1];
    EContentAdapter contentAdapter = new EContentAdapter()
    {
      @Override
      public void notifyChanged(Notification notification)
      {
        super.notifyChanged(notification);
        notified[0] = true;
      }

      @Override
      protected boolean resolve()
      {
        return false;
      }
    };

    CDOID supplierID = null;

    {
      CDOSession session = openModel1Session();
      CDOTransaction transaction = session.openTransaction();

      CDOResource resource = transaction.createResource("/test2");
      // resource.eAdapters().add(contentAdapter);

      Supplier supplier = Model1Factory.eINSTANCE.createSupplier();
      resource.getContents().add(supplier);
      // notified[0] = false;

      // contentAdapter should receive notification
      supplier.setName("HELLO");

      // assertEquals(true, notified[0]);
      transaction.commit();
      supplierID = supplier.cdoID();

      transaction.close();
      session.close();
    }

    {
      CDOSessionImpl session = (CDOSessionImpl)openModel1Session();

      CDOFetchRuleManagerInfo info = new CDOFetchRuleManagerInfo();
      session.getRevisionManager().setRuleManager(info);

      CDOTransactionImpl transaction = session.openTransaction();
      CDOResource resource = transaction.getResource("/test2");

      // I don't want to fetch my objects!!
      EList<Adapter> adapters = resource.eAdapters();
      adapters.add(contentAdapter);

      // XXX FAILURE HERE!!!
      // By adding an adapter, we shouldn`t fetch objects
      assertEquals(false, info.getFetchedIDs().contains(supplierID));

      Supplier supplier = (Supplier)transaction.getObject(supplierID);
      notified[0] = false;

      // contentAdapter should receive notification
      supplier.setName("HELLO");

      // Should have been notified!!
      assertEquals(true, notified[0]);

      transaction.commit();
      transaction.close();
    }
  }

  /**
   * @author Eike Stepper
   */
  public class CDOFetchRuleManagerInfo implements CDOFetchRuleManager
  {
    private Set<CDOID> fetchedIDs = new HashSet<CDOID>();

    public CDOFetchRuleManagerInfo()
    {
    }

    public CDOID getContext()
    {
      return CDOID.NULL;
    }

    public void clear()
    {
      fetchedIDs.clear();
    }

    public Set<CDOID> getFetchedIDs()
    {
      return fetchedIDs;
    }

    public List<CDOFetchRule> getFetchRules(Collection<CDOID> ids)
    {
      // accumulate changes
      for (CDOID id : ids)
      {
        fetchedIDs.add(id);
      }

      return null;
    }

    public int getLoadRevisionCollectionChunkSize()
    {
      return 0;
    }
  }
}