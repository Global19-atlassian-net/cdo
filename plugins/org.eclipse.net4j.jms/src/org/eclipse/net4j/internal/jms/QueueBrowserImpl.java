/*
 * Copyright (c) 2007, 2011, 2012, 2015, 2019 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.internal.jms;

import javax.jms.Queue;
import javax.jms.QueueBrowser;

import java.util.Enumeration;

public class QueueBrowserImpl implements QueueBrowser
{
  private Queue queue;

  private String messageSelector;

  public QueueBrowserImpl(Queue queue, String messageSelector)
  {
    this.queue = queue;
    this.messageSelector = messageSelector;
  }

  public QueueBrowserImpl(Queue queue)
  {
    this(queue, null);
  }

  @Override
  public Queue getQueue()
  {
    return queue;
  }

  @Override
  public String getMessageSelector()
  {
    return messageSelector;
  }

  @Override
  public void close()
  {
    throw new NotYetImplementedException();
  }

  @Override
  public Enumeration<?> getEnumeration()
  {
    throw new NotYetImplementedException();
  }
}
