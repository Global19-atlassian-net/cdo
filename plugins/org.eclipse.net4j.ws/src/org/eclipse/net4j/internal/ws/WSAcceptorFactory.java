/*
 * Copyright (c) 2020 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.internal.ws;

import org.eclipse.net4j.ws.IWSAcceptor;
import org.eclipse.net4j.ws.WSUtil;

import org.eclipse.spi.net4j.AcceptorFactory;

/**
 * @author Eike Stepper
 */
public class WSAcceptorFactory extends AcceptorFactory
{
  public WSAcceptorFactory()
  {
    super(WSUtil.FACTORY_TYPE);
  }

  /**
   * Allows derived classes to override the TYPE identifier
   */
  protected WSAcceptorFactory(String type)
  {
    super(type);
  }

  @Override
  public WSAcceptor create(String description)
  {
    WSAcceptor acceptor = createAcceptor();
    acceptor.setName(description);
    return acceptor;
  }

  protected WSAcceptor createAcceptor()
  {
    return new WSAcceptor();
  }

  @Override
  public String getDescriptionFor(Object object)
  {
    if (object instanceof IWSAcceptor)
    {
      IWSAcceptor acceptor = (IWSAcceptor)object;
      return acceptor.getName();
    }

    return null;
  }
}
