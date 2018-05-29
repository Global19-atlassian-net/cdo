/*
 * Copyright (c) 2012 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.util.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author Eike Stepper
 * @since 3.3
 */
public class PriorityQueueRunner extends QueueRunner2<PriorityQueueRunnable>
{
  public PriorityQueueRunner()
  {
  }

  @Override
  protected BlockingQueue<PriorityQueueRunnable> createQueue()
  {
    return new PriorityBlockingQueue<PriorityQueueRunnable>();
  }
}
