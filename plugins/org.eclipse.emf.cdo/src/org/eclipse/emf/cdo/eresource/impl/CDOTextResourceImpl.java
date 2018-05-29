/*
 * Copyright (c) 2011, 2012, 2015, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.eresource.impl;

import org.eclipse.emf.cdo.common.lob.CDOClob;
import org.eclipse.emf.cdo.eresource.CDOTextResource;
import org.eclipse.emf.cdo.eresource.EresourcePackage;

import org.eclipse.emf.ecore.EClass;

import java.io.Reader;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>CDO Text Resource</b></em>'.
 *
 * @since 4.1
 * @noextend This class is not intended to be subclassed by clients. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.emf.cdo.eresource.impl.CDOTextResourceImpl#getContents <em>Contents</em>}</li>
 *   <li>{@link org.eclipse.emf.cdo.eresource.impl.CDOTextResourceImpl#getEncoding <em>Encoding</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CDOTextResourceImpl extends CDOFileResourceImpl<Reader> implements CDOTextResource
{
  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  protected CDOTextResourceImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return EresourcePackage.Literals.CDO_TEXT_RESOURCE;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public CDOClob getContents()
  {
    return (CDOClob)eGet(EresourcePackage.Literals.CDO_TEXT_RESOURCE__CONTENTS, true);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public void setContents(CDOClob newContents)
  {
    eSet(EresourcePackage.Literals.CDO_TEXT_RESOURCE__CONTENTS, newContents);
  }

  /**
   * <!-- begin-user-doc -->
   * @since 4.2
   * <!-- end-user-doc -->
   * @generated
   */
  public String getEncoding()
  {
    return (String)eGet(EresourcePackage.Literals.CDO_TEXT_RESOURCE__ENCODING, true);
  }

  /**
   * <!-- begin-user-doc -->
   * @since 4.2
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEncoding(String newEncoding)
  {
    eSet(EresourcePackage.Literals.CDO_TEXT_RESOURCE__ENCODING, newEncoding);
  }

} // CDOTextResourceImpl
