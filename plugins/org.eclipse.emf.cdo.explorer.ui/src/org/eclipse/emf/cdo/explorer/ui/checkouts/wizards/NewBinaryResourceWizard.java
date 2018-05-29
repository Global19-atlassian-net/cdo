/*
 * Copyright (c) 2015, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.explorer.ui.checkouts.wizards;

import org.eclipse.emf.cdo.eresource.CDOResourceNode;
import org.eclipse.emf.cdo.eresource.EresourceFactory;

/**
 * @author Eike Stepper
 */
public class NewBinaryResourceWizard extends NewWizard
{
  public static final String ID = "org.eclipse.emf.cdo.explorer.ui.new.resource.binary";

  public NewBinaryResourceWizard()
  {
    super("binary", "New Binary File");
  }

  @Override
  protected CDOResourceNode createNewResourceNode()
  {
    return EresourceFactory.eINSTANCE.createCDOBinaryResource();
  }
}
