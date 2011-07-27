/**
 * Copyright (c) 2004 - 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Simon McDuff - initial API and implementation
 *    Eike Stepper - maintenance
 *    Caspar De Groot - write options 
 */
package org.eclipse.emf.cdo.tests;

import org.eclipse.emf.cdo.CDOLock;
import org.eclipse.emf.cdo.CDOObject;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.server.IView;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.spi.server.InternalRepository;
import org.eclipse.emf.cdo.tests.model1.Category;
import org.eclipse.emf.cdo.tests.model1.Company;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.util.CDOUtil;
import org.eclipse.emf.cdo.util.CommitException;
import org.eclipse.emf.cdo.util.LockTimeoutException;
import org.eclipse.emf.cdo.util.StaleRevisionLockException;

import org.eclipse.net4j.util.concurrent.IRWLockManager.LockType;
import org.eclipse.net4j.util.concurrent.RWOLockManager;
import org.eclipse.net4j.util.concurrent.TimeoutRuntimeException;
import org.eclipse.net4j.util.io.IOUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon McDuff
 */
public class LockingManagerTest extends AbstractLockingTest
{
  public void testWriteOptions() throws Exception
  {
    final RWOLockManager<Integer, Integer> lockingManager = new RWOLockManager<Integer, Integer>();

    Set<Integer> keys = new HashSet<Integer>();
    keys.add(1);
    lockingManager.lock(LockType.OPTION, 1, keys, 100);

    // (R=Read, W=Write, WO=WriteOption)
    // Scenario 1: 1 has WO, 2 requests W -> fail
    keys.clear();
    keys.add(1);

    try
    {
      lockingManager.lock(LockType.WRITE, 2, keys, 100); // Must fail
      fail("Should have thrown an exception");
    }
    catch (TimeoutRuntimeException e)
    {
    }

    // Scenario 2: 1 has WO, 2 requests R -> succeed
    try
    {
      lockingManager.lock(LockType.READ, 2, keys, 100); // Must succeed
    }
    catch (TimeoutRuntimeException e)
    {
      fail("Should not have thrown an exception");
    }

    // Scenario 3: 1 has WO, 2 has R, 1 requests W -> fail
    try
    {
      lockingManager.lock(LockType.WRITE, 1, keys, 100); // Must fail
      fail("Should have thrown an exception");
    }
    catch (TimeoutRuntimeException e)
    {
    }

    // Scenario 4: 1 has WO, 2 has R, 2 requests WO -> fail
    try
    {
      lockingManager.lock(LockType.OPTION, 2, keys, 100); // Must fail
      fail("Should have thrown an exception");
    }
    catch (TimeoutRuntimeException e)
    {
    }

    // Scenario 5: 1 has WO, 2 has nothing, 2 requests WO -> fail
    lockingManager.unlock(LockType.READ, 2, keys);
    try
    {
      lockingManager.lock(LockType.OPTION, 2, keys, 100); // Must fail
      fail("Should have thrown an exception");
    }
    catch (TimeoutRuntimeException e)
    {
    }

    // Scenario 6: 1 has W, 2 has nothing, 2 requests WO -> fail
    lockingManager.unlock(LockType.OPTION, 1, keys);
    lockingManager.lock(LockType.WRITE, 1, keys, 100);
    try
    {
      lockingManager.lock(LockType.OPTION, 2, keys, 100); // Must fail
      fail("Should have thrown an exception");
    }
    catch (TimeoutRuntimeException e)
    {
    }

    // Scenario 7: 1 has W, 1 request WO -> succeed
    try
    {
      lockingManager.lock(LockType.OPTION, 1, keys, 100); // Must succeed
    }
    catch (TimeoutRuntimeException e)
    {
      fail("Should not have thrown an exception");
    }

    // Scenario 8: 1 has W, 2 has R, 1 request WO -> succeed
    lockingManager.unlock(LockType.OPTION, 1, keys);
    lockingManager.lock(LockType.READ, 1, keys, 100);
    try
    {
      lockingManager.lock(LockType.OPTION, 1, keys, 100); // Must succeed
    }
    catch (TimeoutRuntimeException e)
    {
      fail("Should not have thrown an exception");
    }
  }

  public void testBasicUpgradeFromReadToWriteLock() throws Exception
  {
    final RWOLockManager<Integer, Integer> lockingManager = new RWOLockManager<Integer, Integer>();

    Runnable step1 = new Runnable()
    {
      public void run()
      {
        Set<Integer> keys = new HashSet<Integer>();
        keys.add(1);
        try
        {
          lockingManager.lock(LockType.WRITE, 1, keys, 50000);
        }
        catch (InterruptedException ex)
        {
          fail("Should not have exception");
        }
      }
    };

    ExecutorService executors = Executors.newFixedThreadPool(10);
    Set<Integer> keys = new HashSet<Integer>();
    keys.add(1);
    keys.add(2);
    keys.add(3);
    keys.add(4);

    msg("Context 1 have readlock 1,2,3,4");
    lockingManager.lock(LockType.READ, 1, keys, 1000);
    assertEquals(true, lockingManager.hasLock(LockType.READ, 1, 1));
    assertEquals(true, lockingManager.hasLock(LockType.READ, 1, 2));
    assertEquals(true, lockingManager.hasLock(LockType.READ, 1, 3));
    assertEquals(true, lockingManager.hasLock(LockType.READ, 1, 4));

    keys.clear();
    keys.add(1);
    keys.add(2);
    keys.add(3);

    msg("Context 2 have readlock 1,2,3");
    lockingManager.lock(LockType.READ, 2, keys, 1000);
    assertEquals(true, lockingManager.hasLock(LockType.READ, 2, 1));
    assertEquals(true, lockingManager.hasLock(LockType.READ, 2, 2));
    assertEquals(true, lockingManager.hasLock(LockType.READ, 2, 3));
    assertEquals(true, lockingManager.hasLockByOthers(LockType.READ, 2, 1));
    assertEquals(true, lockingManager.hasLockByOthers(LockType.READ, 1, 1));

    keys.clear();
    keys.add(4);

    msg("Context 1 have readlock 1,2,3,4 and writeLock 4");
    lockingManager.lock(LockType.WRITE, 1, keys, 1000);
    assertEquals(true, lockingManager.hasLock(LockType.READ, 1, 4));
    assertEquals(true, lockingManager.hasLock(LockType.WRITE, 1, 4));

    keys.clear();
    keys.add(1);

    try
    {
      lockingManager.lock(LockType.WRITE, 1, keys, 1000);
      fail("Should not have exception");
    }
    catch (RuntimeException expected)
    {
    }

    executors.execute(step1);
    executors.execute(step1);

    sleep(1000);

    keys.clear();
    keys.add(1);
    keys.add(2);
    keys.add(3);
    lockingManager.unlock(LockType.READ, 2, keys);

    new PollingTimeOuter()
    {
      @Override
      protected boolean successful()
      {
        return lockingManager.hasLock(LockType.WRITE, 1, 1);
      }
    }.assertNoTimeOut();
  }

  public void testBasicWrongUnlock() throws Exception
  {
    final RWOLockManager<Integer, Integer> lockingManager = new RWOLockManager<Integer, Integer>();
    Set<Integer> keys = new HashSet<Integer>();
    keys.add(1);
    lockingManager.lock(LockType.READ, 1, keys, 10000);
    lockingManager.unlock(LockType.READ, 1, keys);

    try
    {
      lockingManager.unlock(LockType.READ, 1, keys);
      fail("Should have an exception");
    }
    catch (IllegalMonitorStateException exception)
    {
    }
  }

  public void testReadTimeout() throws Exception
  {
    Company company = getModel1Factory().createCompany();

    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();

    writeLock(company);

    CDOTransaction transaction2 = session.openTransaction();
    Company company2 = transaction2.getObject(company);

    long start = System.currentTimeMillis();
    assertWriteLock(false, company2);
    assertEquals(true, System.currentTimeMillis() - start < 300);

    start = System.currentTimeMillis();
    assertEquals(false, CDOUtil.getCDOObject(company2).cdoWriteLock().tryLock(2, TimeUnit.SECONDS));
    assertEquals(true, System.currentTimeMillis() - start >= 2000);
  }

  public void testReadLockByOthers() throws Exception
  {
    Company company = getModel1Factory().createCompany();

    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();

    readLock(company);

    CDOTransaction transaction2 = session.openTransaction();
    Company company2 = (Company)transaction2.getResource(getResourcePath("/res1")).getContents().get(0);

    CDOObject cdoCompany2 = CDOUtil.getCDOObject(company2);
    assertEquals(false, cdoCompany2.cdoWriteLock().isLockedByOthers());
    assertEquals(true, cdoCompany2.cdoReadLock().isLockedByOthers());
  }

  public void testDetachedObjects() throws Exception
  {
    Company company = getModel1Factory().createCompany();

    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();

    CDOTransaction transaction2 = session.openTransaction();
    Company company2 = (Company)transaction2.getResource(getResourcePath("/res1")).getContents().get(0);
    res.getContents().remove(0);

    transaction.commit();

    try
    {
      readLock(company2);
      fail("IllegalArgumentException expected");
    }
    catch (IllegalArgumentException expected)
    {
    }

    assertReadLock(false, company2);
    assertEquals(false, CDOUtil.getCDOObject(company2).cdoReadLock().isLockedByOthers());
  }

  public void testWriteLockByOthers() throws Exception
  {
    Company company = getModel1Factory().createCompany();

    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();

    writeLock(company);

    CDOTransaction transaction2 = session.openTransaction();
    Company company2 = (Company)transaction2.getResource(getResourcePath("/res1")).getContents().get(0);

    CDOObject cdoCompany2 = CDOUtil.getCDOObject(company2);
    assertEquals(true, cdoCompany2.cdoWriteLock().isLockedByOthers());
    assertEquals(false, cdoCompany2.cdoReadLock().isLockedByOthers());
  }

  public void testWriteLock() throws Exception
  {
    Company company = getModel1Factory().createCompany();

    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();

    CDOTransaction transaction2 = session.openTransaction();
    Company company2 = (Company)transaction2.getResource(getResourcePath("/res1")).getContents().get(0);

    CDOObject cdoCompany = CDOUtil.getCDOObject(company);
    CDOObject cdoCompany2 = CDOUtil.getCDOObject(company2);

    transaction.lockObjects(Collections.singletonList(cdoCompany), LockType.WRITE, CDOLock.WAIT);

    try
    {
      transaction2.lockObjects(Collections.singletonList(cdoCompany2), LockType.WRITE, 1000);
      fail("Should have thrown an exception");
    }
    catch (LockTimeoutException ex)
    {
    }

    company2.setCity("Ottawa");

    try
    {
      transaction2.commit();
      fail("CommitException expected");
    }
    catch (CommitException exception)
    {
    }
  }

  public void testWriteLockViaObject() throws Exception
  {
    Company company = getModel1Factory().createCompany();

    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();

    CDOTransaction transaction2 = session.openTransaction();
    Company company2 = (Company)transaction2.getResource(getResourcePath("/res1")).getContents().get(0);

    writeLock(company);
    assertWriteLock(false, company2);

    company2.setCity("Ottawa");

    try
    {
      transaction2.commit();
      fail("CommitException expected");
    }
    catch (CommitException expected)
    {
    }
  }

  public void testWriteLockFromDifferenceTransaction() throws Exception
  {
    Company company = getModel1Factory().createCompany();

    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();

    CDOTransaction transaction2 = session.openTransaction();
    Company company2 = (Company)transaction2.getResource(getResourcePath("/res1")).getContents().get(0);

    CDOObject cdoCompany = CDOUtil.getCDOObject(company);
    CDOObject cdoCompany2 = CDOUtil.getCDOObject(company2);

    transaction.lockObjects(Collections.singletonList(cdoCompany), LockType.WRITE, DEFAULT_TIMEOUT);

    try
    {
      transaction2.lockObjects(Collections.singletonList(cdoCompany2), LockType.WRITE, 1000);
      fail("Should have an exception");
    }
    catch (LockTimeoutException ex)
    {
    }
  }

  public void testWriteLockMultiple() throws Exception
  {
    Company company = getModel1Factory().createCompany();
    List<CDOObject> objects = new ArrayList<CDOObject>();

    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);

    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();

    transaction.lockObjects(objects, LockType.WRITE, DEFAULT_TIMEOUT);

    assertWriteLock(false, company);
    assertReadLock(false, company);
    for (CDOObject object : objects)
    {
      assertWriteLock(true, object);
      assertReadLock(false, object);
    }
  }

  public void testWriteUnlockMultiple() throws Exception
  {
    Company company = getModel1Factory().createCompany();
    List<CDOObject> objects = new ArrayList<CDOObject>();

    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);

    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();

    transaction.lockObjects(objects, LockType.WRITE, DEFAULT_TIMEOUT);

    assertWriteLock(false, company);
    assertReadLock(false, company);
    for (CDOObject object : objects)
    {
      assertWriteLock(true, object);
      assertReadLock(false, object);
    }

    transaction.unlockObjects(objects, LockType.WRITE);

    assertWriteLock(false, company);
    assertReadLock(false, company);
    for (CDOObject object : objects)
    {
      assertWriteLock(false, object);
      assertReadLock(false, object);
    }
  }

  public void testReadLockMultiple() throws Exception
  {
    Company company = getModel1Factory().createCompany();
    List<CDOObject> objects = new ArrayList<CDOObject>();

    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);

    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();

    transaction.lockObjects(objects, LockType.READ, DEFAULT_TIMEOUT);

    assertReadLock(false, company);
    assertWriteLock(false, company);
    for (CDOObject object : objects)
    {
      assertReadLock(true, object);
      assertWriteLock(false, object);
    }
  }

  public void testReadUnlockMultiple() throws Exception
  {
    Company company = getModel1Factory().createCompany();
    List<CDOObject> objects = new ArrayList<CDOObject>();

    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);
    addCategory(company, objects);

    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();

    transaction.lockObjects(objects, LockType.READ, DEFAULT_TIMEOUT);

    assertReadLock(false, company);
    assertWriteLock(false, company);
    for (CDOObject object : objects)
    {
      assertReadLock(true, object);
      assertWriteLock(false, object);
    }

    transaction.unlockObjects(objects, LockType.READ);

    assertWriteLock(false, company);
    assertReadLock(false, company);
    for (CDOObject object : objects)
    {
      assertWriteLock(false, object);
      assertReadLock(false, object);
    }
  }

  public void testReadLockAndCommitFromDifferentTransaction() throws Exception
  {
    Company company = getModel1Factory().createCompany();

    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();

    CDOTransaction transaction2 = session.openTransaction();
    Company company2 = (Company)transaction2.getResource(getResourcePath("/res1")).getContents().get(0);
    CDOObject cdoCompany = CDOUtil.getCDOObject(company);

    transaction.lockObjects(Collections.singletonList(cdoCompany), LockType.READ, DEFAULT_TIMEOUT);
    company2.setCity("Ottawa");

    try
    {
      transaction2.commit();
      fail("CommitException expected");
    }
    catch (CommitException expected)
    {
    }
  }

  public void testWriteLockAndCommitFromDifferentTransaction() throws Exception
  {
    Company company = getModel1Factory().createCompany();

    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();

    CDOTransaction transaction2 = session.openTransaction();
    Company company2 = (Company)transaction2.getResource(getResourcePath("/res1")).getContents().get(0);
    CDOObject cdoCompany = CDOUtil.getCDOObject(company);

    transaction.lockObjects(Collections.singletonList(cdoCompany), LockType.WRITE, DEFAULT_TIMEOUT);
    company2.setCity("Ottawa");

    try
    {
      transaction2.commit();
      fail("CommitException expected");
    }
    catch (CommitException expected)
    {
      IOUtil.print(expected);
    }
  }

  public void testReadLockAndCommitSameTransaction() throws Exception
  {
    Company company = getModel1Factory().createCompany();

    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();

    readLock(company);

    company.setCity("Ottawa");
    transaction.commit();
  }

  public void testWriteLockAndCommitSameTransaction() throws Exception
  {
    Company company = getModel1Factory().createCompany();

    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();

    writeLock(company);

    company.setCity("Ottawa");
    assertWriteLock(true, company);
    assertReadLock(false, company);

    transaction.commit();

    assertWriteLock(false, company);
    assertReadLock(false, company);
  }

  public void testWriteLockAndRollback() throws Exception
  {
    Company company = getModel1Factory().createCompany();

    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();

    writeLock(company);
    company.setCity("Ottawa");

    transaction.rollback();
    assertWriteLock(false, company);
  }

  public void testLockUnlock() throws Exception
  {
    Company company = getModel1Factory().createCompany();

    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();
    CDOObject cdoCompany = CDOUtil.getCDOObject(company);

    readLock(company);
    assertReadLock(true, company);
    assertWriteLock(false, company);

    writeLock(company);
    assertReadLock(true, company);
    assertWriteLock(true, company);

    readLock(company);
    assertReadLock(true, company);
    assertWriteLock(true, company);

    writeLock(company);
    assertReadLock(true, company);
    assertWriteLock(true, company);

    readUnlock(cdoCompany);
    assertReadLock(true, company);
    assertWriteLock(true, company);

    readUnlock(cdoCompany);
    assertReadLock(false, company);
    assertWriteLock(true, company);

    writeUnlock(cdoCompany);
    assertReadLock(false, company);
    assertWriteLock(true, company);

    writeUnlock(cdoCompany);
    assertReadLock(false, company);
    assertWriteLock(false, company);

    /********************/

    readLock(company);
    assertReadLock(true, company);
    assertWriteLock(false, company);

    writeLock(company);
    assertReadLock(true, company);
    assertWriteLock(true, company);

    readLock(company);
    assertReadLock(true, company);
    assertWriteLock(true, company);

    writeLock(company);
    assertReadLock(true, company);
    assertWriteLock(true, company);

    writeUnlock(cdoCompany);
    assertReadLock(true, company);
    assertWriteLock(true, company);

    writeUnlock(cdoCompany);
    assertReadLock(true, company);
    assertWriteLock(false, company);

    readUnlock(cdoCompany);
    assertReadLock(true, company);
    assertWriteLock(false, company);

    readUnlock(cdoCompany);
    assertReadLock(false, company);
    assertWriteLock(false, company);
  }

  public void testTransactionClose() throws Exception
  {
    Company company = getModel1Factory().createCompany();

    InternalRepository repo = getRepository();
    CDOSession session = openSession();

    CDOTransaction transaction = session.openTransaction();
    IView view = repo.getSessionManager().getSession(session.getSessionID()).getView(transaction.getViewID());
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();

    CDOObject cdoCompany = CDOUtil.getCDOObject(company);
    readLock(company);
    transaction.close();
    assertEquals(false, repo.getLockManager().hasLock(LockType.READ, view, cdoCompany.cdoID()));
  }

  public void testSessionClose() throws Exception
  {
    Company company = getModel1Factory().createCompany();

    InternalRepository repo = getRepository();
    CDOSession session = openSession();

    CDOTransaction transaction = session.openTransaction();
    IView view = repo.getSessionManager().getSession(session.getSessionID()).getView(transaction.getViewID());
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();

    CDOObject cdoCompany = CDOUtil.getCDOObject(company);
    readLock(company);
    session.close();

    sleep(100);
    assertEquals(false, repo.getLockManager().hasLock(LockType.READ, view, cdoCompany.cdoID()));
  }

  public void testBugzilla_270345() throws Exception
  {
    Company company1 = getModel1Factory().createCompany();
    Company company2 = getModel1Factory().createCompany();

    CDOSession session = openSession();
    CDOTransaction transaction1 = session.openTransaction();
    CDOTransaction transaction2 = session.openTransaction();
    CDOResource res = transaction1.getOrCreateResource(getResourcePath("/res1"));
    res.getContents().add(company1);
    res.getContents().add(company2);
    transaction1.commit();

    writeLock(company1);
    assertWriteLock(true, company1);

    Company companyFrom2 = (Company)CDOUtil.getEObject(transaction2.getObject(company2));
    companyFrom2.setCity("sss");
    transaction2.commit();
    assertWriteLock(true, company1);
  }

  public void testAutoReleaseLockFalse_commit() throws Exception
  {
    Company company = getModel1Factory().createCompany();

    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();
    transaction.options().setAutoReleaseLocksEnabled(false);

    writeLock(company);
    readLock(company);

    msg("Test with read/write lock");
    assertWriteLock(true, company);
    assertReadLock(true, company);

    company.setCity("Ottawa");
    transaction.commit();
    assertWriteLock(true, company);
    assertReadLock(true, company);

    msg("Clean locks");
    transaction.unlockObjects(null, null);

    msg("Test with read lock");
    readLock(company);
    assertReadLock(true, company);

    company.setCity("Toronto");
    transaction.commit();
    assertReadLock(true, company);

    transaction.options().setAutoReleaseLocksEnabled(true);
    transaction.commit();
    assertReadLock(false, company);
  }

  public void testAutoReleaseLockFalse_rollback() throws Exception
  {
    Company company = getModel1Factory().createCompany();

    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();
    transaction.options().setAutoReleaseLocksEnabled(false);

    writeLock(company);
    readLock(company);

    msg("Test with read/write lock");
    assertWriteLock(true, company);
    assertReadLock(true, company);

    company.setCity("Ottawa");
    transaction.rollback();
    assertWriteLock(true, company);
    assertReadLock(true, company);

    msg("Clean locks");
    transaction.unlockObjects(null, null);

    msg("Test with read lock");
    readLock(company);
    assertReadLock(true, company);

    company.setCity("Toronto");
    transaction.rollback();
    assertReadLock(true, company);

    transaction.options().setAutoReleaseLocksEnabled(true);
    transaction.rollback();
    assertReadLock(false, company);
  }

  public void testWriteLockPerformance() throws Exception
  {
    final int ITERATION = 100;
    Company company = getModel1Factory().createCompany();

    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));
    res.getContents().add(company);
    transaction.commit();

    long start = System.currentTimeMillis();

    // 335-418 locks/sec
    for (int i = 0; i < ITERATION; i++)
    {
      writeLock(company);
    }

    msg("Lock " + ITERATION / ((double)(System.currentTimeMillis() - start) / 1000) + " objects/sec");
  }

  public void testReadLockStaleRevision() throws Exception
  {
    lockStaleRevision(LockType.READ);
  }

  public void testWriteLockStaleRevision() throws Exception
  {
    lockStaleRevision(LockType.WRITE);
  }

  private void lockStaleRevision(LockType type) throws Exception
  {
    CDOSession session = openSession();
    session.options().setPassiveUpdateEnabled(false);
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.createResource(getResourcePath("/res1"));

    Company company = getModel1Factory().createCompany();
    company.setName("AAA");
    res.getContents().add(company);
    transaction.commit();

    updateInOtherSession();

    try
    {
      if (type == LockType.WRITE)
      {
        writeLock(company);
      }
      else if (type == LockType.READ)
      {
        readLock(company);
      }

      fail("StaleRevisionLockException expected");
    }
    catch (StaleRevisionLockException expected)
    {
    }

    session.close();
  }

  private void updateInOtherSession() throws CommitException
  {
    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource res = transaction.getResource(getResourcePath("/res1"));

    Company company = (Company)res.getContents().get(0);
    company.setName("BBB");
    transaction.commit();

    session.close();
  }

  private void addCategory(Company company, List<CDOObject> objects)
  {
    Category category = getModel1Factory().createCategory();
    company.getCategories().add(category);
    objects.add(CDOUtil.getCDOObject(category));
  }
}