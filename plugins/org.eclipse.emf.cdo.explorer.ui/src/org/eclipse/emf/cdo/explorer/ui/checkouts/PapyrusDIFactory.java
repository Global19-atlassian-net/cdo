/*
 * Copyright (c) 2004-2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.explorer.ui.checkouts;

/**
 * @author Eike Stepper
 */
public final class PapyrusDIFactory extends ResourceGroupModifier.Factory
{
  public static final String EXTENSION = "di";

  public PapyrusDIFactory()
  {
    super(EXTENSION);
  }
}