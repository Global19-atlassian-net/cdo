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
package org.eclipse.net4j.tests;

import org.eclipse.emf.cdo.tests.bundle.OM;

import org.eclipse.net4j.util.concurrent.ConcurrencyUtil;
import org.eclipse.net4j.util.io.IOUtil;
import org.eclipse.net4j.util.lifecycle.LifecycleUtil;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.om.log.PrintLogHandler;
import org.eclipse.net4j.util.om.trace.PrintTraceHandler;

import junit.framework.TestCase;
import junit.framework.TestResult;

/**
 * @author Eike Stepper
 */
public abstract class AbstractOMTest extends TestCase
{
  private static boolean consoleEnabled;

  protected AbstractOMTest()
  {
  }

  @Override
  public void setUp() throws Exception
  {
    OMPlatform.INSTANCE.addLogHandler(PrintLogHandler.CONSOLE);
    OMPlatform.INSTANCE.addTraceHandler(PrintTraceHandler.CONSOLE);
    OMPlatform.INSTANCE.setDebugging(true);
    enableConsole();

    IOUtil.OUT().println("*******************************************************");
    IOUtil.OUT().println(this);
    IOUtil.OUT().println("*******************************************************");

    super.setUp();
    doSetUp();

    IOUtil.OUT().println();
    IOUtil.OUT().println("------------------------ START ------------------------");
  }

  @Override
  public void tearDown() throws Exception
  {
    IOUtil.OUT().println("------------------------- END -------------------------");
    IOUtil.OUT().println();

    doTearDown();
    super.tearDown();

    IOUtil.OUT().println();
    IOUtil.OUT().println();
  }

  @Override
  public void runBare() throws Throwable
  {
    try
    {
      super.runBare();
    }
    catch (SkipTestException ex)
    {
      OM.LOG.info("Skipped " + this);
    }
    catch (Throwable t)
    {
      t.printStackTrace(IOUtil.OUT());
      throw t;
    }
  }

  @Override
  public void run(TestResult result)
  {
    try
    {
      super.run(result);
    }
    catch (SkipTestException ex)
    {
      OM.LOG.info("Skipped " + this);
    }
    catch (RuntimeException ex)
    {
      ex.printStackTrace(IOUtil.OUT());
      throw ex;
    }
    catch (Error err)
    {
      err.printStackTrace(IOUtil.OUT());
      throw err;
    }
  }

  protected void enableConsole()
  {
    if (!consoleEnabled)
    {
      PrintTraceHandler.CONSOLE.setShortContext(true);
      OMPlatform.INSTANCE.addTraceHandler(PrintTraceHandler.CONSOLE);
      OMPlatform.INSTANCE.addLogHandler(PrintLogHandler.CONSOLE);
      OMPlatform.INSTANCE.setDebugging(true);
      consoleEnabled = true;
    }
  }

  protected void disableConsole()
  {
    if (consoleEnabled)
    {
      consoleEnabled = false;
      OMPlatform.INSTANCE.setDebugging(false);
      OMPlatform.INSTANCE.removeTraceHandler(PrintTraceHandler.CONSOLE);
      OMPlatform.INSTANCE.removeLogHandler(PrintLogHandler.CONSOLE);
    }
  }

  protected void doSetUp() throws Exception
  {
  }

  protected void doTearDown() throws Exception
  {
  }

  protected static void msg(Object m)
  {
    if (consoleEnabled)
    {
      IOUtil.OUT().println("--> " + m);
    }
  }

  protected static void sleep(long millis)
  {
    ConcurrencyUtil.sleep(millis);
  }

  protected static void assertActive(Object object)
  {
    assertEquals(true, LifecycleUtil.isActive(object));
  }

  protected static void assertInactive(Object object)
  {
    assertEquals(false, LifecycleUtil.isActive(object));
  }

  protected static void skipTest(boolean skip)
  {
    if (skip)
    {
      throw new SkipTestException();
    }
  }

  protected static void skipTest()
  {
    skipTest(true);
  }

  /**
   * @author Eike Stepper
   */
  private static final class SkipTestException extends RuntimeException
  {
    private static final long serialVersionUID = 1L;
  }
}
