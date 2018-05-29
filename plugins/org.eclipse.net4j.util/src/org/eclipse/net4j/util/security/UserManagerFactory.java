/*
 * Copyright (c) 2008, 2009, 2011, 2012 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.util.security;

import org.eclipse.net4j.util.factory.Factory;

/**
 * @author Eike Stepper
 */
public abstract class UserManagerFactory extends Factory
{
  public static final String PRODUCT_GROUP = "org.eclipse.net4j.userManagers"; //$NON-NLS-1$

  public UserManagerFactory(String type)
  {
    super(PRODUCT_GROUP, type);
  }
}
