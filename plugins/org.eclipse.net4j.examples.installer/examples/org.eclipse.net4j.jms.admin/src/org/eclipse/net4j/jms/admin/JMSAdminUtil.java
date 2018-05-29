/*
 * Copyright (c) 2007, 2008, 2011, 2012, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.jms.admin;

import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.jms.internal.admin.JMSAdmin;

/**
 * @author Eike Stepper
 */
public final class JMSAdminUtil
{
  private JMSAdminUtil()
  {
  }

  public static IJMSAdmin createAdmin(IConnector connector)
  {
    return new JMSAdmin(connector);
  }
}
