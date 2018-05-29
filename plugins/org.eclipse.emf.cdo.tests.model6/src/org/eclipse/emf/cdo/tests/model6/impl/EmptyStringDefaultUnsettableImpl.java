/*
 * Copyright (c) 2013, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.tests.model6.impl;

import org.eclipse.emf.cdo.tests.model6.EmptyStringDefaultUnsettable;
import org.eclipse.emf.cdo.tests.model6.Model6Package;

import org.eclipse.emf.internal.cdo.CDOObjectImpl;

import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Empty String Default Unsettable</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.emf.cdo.tests.model6.impl.EmptyStringDefaultUnsettableImpl#getAttribute <em>Attribute</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EmptyStringDefaultUnsettableImpl extends CDOObjectImpl implements EmptyStringDefaultUnsettable
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected EmptyStringDefaultUnsettableImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return Model6Package.Literals.EMPTY_STRING_DEFAULT_UNSETTABLE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected int eStaticFeatureCount()
  {
    return 0;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getAttribute()
  {
    return (String)eGet(Model6Package.Literals.EMPTY_STRING_DEFAULT_UNSETTABLE__ATTRIBUTE, true);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAttribute(String newAttribute)
  {
    eSet(Model6Package.Literals.EMPTY_STRING_DEFAULT_UNSETTABLE__ATTRIBUTE, newAttribute);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void unsetAttribute()
  {
    eUnset(Model6Package.Literals.EMPTY_STRING_DEFAULT_UNSETTABLE__ATTRIBUTE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isSetAttribute()
  {
    return eIsSet(Model6Package.Literals.EMPTY_STRING_DEFAULT_UNSETTABLE__ATTRIBUTE);
  }

} // EmptyStringDefaultUnsettableImpl
