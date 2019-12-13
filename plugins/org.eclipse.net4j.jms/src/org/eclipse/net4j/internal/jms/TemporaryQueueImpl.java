/*
 * Copyright (c) 2007-2012, 2019 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.internal.jms;

import javax.jms.TemporaryQueue;

public class TemporaryQueueImpl extends QueueImpl implements TemporaryQueue
{
  private static final long serialVersionUID = 1L;

  private static int counter;

  public TemporaryQueueImpl()
  {
    super("TempQueue" + ++counter); //$NON-NLS-1$
  }

  @Override
  public void delete()
  {
    throw new NotYetImplementedException();
  }
}
