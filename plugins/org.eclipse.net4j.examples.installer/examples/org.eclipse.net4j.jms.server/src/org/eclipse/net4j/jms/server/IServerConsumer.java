/*
 * Copyright (c) 2008, 2011, 2012, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.jms.server;

import org.eclipse.net4j.protocol.IProtocol;

/**
 * @author Eike Stepper
 * @since 2.0
 */
public interface IServerConsumer
{
  public long getID();

  public IDestination getDestination();

  public String getMessageSelector();

  public ISession getSession();

  public IProtocol<?> getProtocol();

  public boolean isNoLocal();

  public boolean isDurable();
}
