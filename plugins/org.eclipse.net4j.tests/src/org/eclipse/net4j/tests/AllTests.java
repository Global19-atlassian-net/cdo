/*
 * Copyright (c) 2006-2012, 2015, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.tests;

import org.eclipse.net4j.tests.bugzilla.Bugzilla_241463_Test;
import org.eclipse.net4j.tests.bugzilla.Bugzilla_259086_Test;
import org.eclipse.net4j.tests.config.Net4jTestSuite;
import org.eclipse.net4j.tests.config.TestConfig.JVM;
import org.eclipse.net4j.tests.config.TestConfig.TCP;
import org.eclipse.net4j.tests.config.TestConfig.WS;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Eike Stepper
 */
public class AllTests
{
  public static Test suite()
  {
    @SuppressWarnings("unchecked")
    TestSuite suite = new Net4jTestSuite(AllTests.class.getName(), JVM.class, TCP.class/* , SSL.class */, WS.class);
    populateSuite(suite);
    return suite;
  }

  static void populateSuite(TestSuite suite)
  {
    // Normal tests
    // suite.addTestSuite(UUIDGeneratorTest.class);
    // suite.addTestSuite(MultiMapTest.class);
    // suite.addTestSuite(SortedFileMapTest.class);
    // suite.addTestSuite(SynchronizingCorrelatorTest.class);
    // suite.addTestSuite(BufferPoolTest.class);
    // suite.addTestSuite(ExtendedIOTest.class);
    // suite.addTestSuite(StringCompressorTest.class);
    // suite.addTestSuite(SecurityTest.class);
    // suite.addTestSuite(ExecutorWorkSerializerTest.class);
    // suite.addTestSuite(ExpectedIOTest.class);
    // suite.addTestSuite(RollingLogTest.class);
    // suite.addTestSuite(Bugzilla_262875_Test.class);

    // Config tests
    suite.addTestSuite(ChannelTest.class);
    suite.addTestSuite(TCPConnectorTest.class);
    suite.addTestSuite(TransportTest.class);
    suite.addTestSuite(SignalTest.class);
    suite.addTestSuite(SignalMonitorTest.class);
    suite.addTestSuite(ExceptionTest.class);
    suite.addTestSuite(Bugzilla_241463_Test.class);
    suite.addTestSuite(Bugzilla_259086_Test.class);
  }
}
