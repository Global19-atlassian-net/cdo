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
package org.eclipse.net4j.util.security;

import org.eclipse.net4j.util.factory.Factory;

/**
 * @author Eike Stepper
 * @since 3.3
 */
public abstract class AuthenticatorFactory extends Factory
{
  public static final String PRODUCT_GROUP = "org.eclipse.net4j.authenticators"; //$NON-NLS-1$

  public AuthenticatorFactory(String type)
  {
    super(PRODUCT_GROUP, type);
  }
}
