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
package org.eclipse.emf.cdo.genmodel.impl;

import org.eclipse.emf.codegen.ecore.genmodel.GenAnnotation;
import org.eclipse.emf.ecore.EcoreFactory;

/**
 * @author Eike Stepper
 *
 */
public class CDOGenModelFactoryImpl extends org.eclipse.emf.codegen.ecore.genmodel.impl.GenModelFactoryImpl
{
  private static final boolean ENABLED = "org.eclipse.emf.cdo.ecore.impl.CDOEcoreFactoryImpl".equals(EcoreFactory.eINSTANCE.getClass().getName());

  public CDOGenModelFactoryImpl()
  {
  }

  @Override
  public GenAnnotation createGenAnnotation()
  {
    if (ENABLED)
    {
      return new org.eclipse.emf.cdo.genmodel.impl.GenAnnotationImpl();
    }

    return super.createGenAnnotation();
  }
}
