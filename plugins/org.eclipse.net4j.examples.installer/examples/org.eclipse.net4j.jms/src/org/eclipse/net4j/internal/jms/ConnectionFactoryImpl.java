/*
 * Copyright (c) 2007, 2011, 2012, 2015, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.internal.jms;

import org.eclipse.net4j.util.container.IManagedContainer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import java.io.Serializable;
import java.rmi.Remote;

public class ConnectionFactoryImpl implements ConnectionFactory, Remote, Serializable
{
  private static final long serialVersionUID = 1L;

  private String connectorType;

  private String connectorDescription;

  private Object transportContainer;

  public ConnectionFactoryImpl(String connectorType, String connectorDescription)
  {
    this.connectorType = connectorType;
    this.connectorDescription = connectorDescription;
  }

  public String getConnectorType()
  {
    return connectorType;
  }

  public String getConnectorDescription()
  {
    return connectorDescription;
  }

  public Object getTransportContainer()
  {
    return transportContainer;
  }

  public void setTransportContainer(Object transportContainer)
  {
    this.transportContainer = transportContainer;
  }

  public Connection createConnection() throws JMSException
  {
    return createConnection(null, null);
  }

  public Connection createConnection(String userName, String password) throws JMSException
  {
    return new ConnectionImpl((IManagedContainer)transportContainer, connectorType, connectorDescription, userName, password);
  }
}
