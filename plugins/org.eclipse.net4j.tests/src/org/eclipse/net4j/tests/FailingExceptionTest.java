/***************************************************************************
 * Copyright (c) 2004 - 2007 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 **************************************************************************/
package org.eclipse.net4j.tests;

import org.eclipse.net4j.IChannel;
import org.eclipse.net4j.signal.FailOverStrategy;
import org.eclipse.net4j.signal.IFailOverStrategy;
import org.eclipse.net4j.tests.signal.Request1;
import org.eclipse.net4j.tests.signal.Request3;
import org.eclipse.net4j.tests.signal.TestSignalClientProtocolFactory;
import org.eclipse.net4j.tests.signal.TestSignalServerProtocolFactory;
import org.eclipse.net4j.util.container.IManagedContainer;

/**
 * @author Eike Stepper
 */
public class FailingExceptionTest extends AbstractTransportTest
{
  @Override
  protected IManagedContainer createContainer()
  {
    IManagedContainer container = super.createContainer();
    container.registerFactory(new TestSignalServerProtocolFactory());
    container.registerFactory(new TestSignalClientProtocolFactory());
    return container;
  }

  public void testFailingBefore() throws Exception
  {
    startTransport();
    IChannel channel = getConnector().openChannel(TestSignalClientProtocolFactory.TYPE, null);

    // Simulate a disconnect from the server.
    getAcceptor().deactivate();

    int data = 0x0a;
    IFailOverStrategy failOverStrategy = new FailOverStrategy();

    // Exception HERE
    Request1 request = new Request1(channel, data);

    int result = failOverStrategy.send(request);
    assertEquals(data, result);
  }

  public void testFailingDuring() throws Exception
  {
    startTransport();
    IChannel channel = getConnector().openChannel(TestSignalClientProtocolFactory.TYPE, null);

    int data = 0x0a;
    IFailOverStrategy failOverStrategy = new FailOverStrategy();

    // Exception HERE
    Request1 request = new Request1(channel, data);

    // Simulate a disconnect from the server.
    getAcceptor().deactivate();

    int result = failOverStrategy.send(request);
    assertEquals(data, result);
  }

  public void testFailingDuring2() throws Exception
  {
    startTransport();
    IChannel channel = getConnector().openChannel(TestSignalClientProtocolFactory.TYPE, null);

    int data = 0x0a;
    IFailOverStrategy failOverStrategy = new FailOverStrategy();

    // Exception HERE
    Request3 request = new Request3(channel, data);

    int result = failOverStrategy.send(request, 1000);
    assertEquals(data, result);
  }
}
