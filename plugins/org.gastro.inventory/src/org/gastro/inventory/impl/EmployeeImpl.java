/*
 * Copyright (c) 2009, 2011, 2012, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *
 *  Initial Publication:
 *    Eclipse Magazin - http://www.eclipse-magazin.de
 */
package org.gastro.inventory.impl;

import org.eclipse.emf.internal.cdo.CDOObjectImpl;

import org.eclipse.emf.ecore.EClass;

import org.gastro.inventory.Department;
import org.gastro.inventory.Employee;
import org.gastro.inventory.InventoryPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Employee</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.gastro.inventory.impl.EmployeeImpl#getDepartment <em>Department</em>}</li>
 * <li>{@link org.gastro.inventory.impl.EmployeeImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EmployeeImpl extends CDOObjectImpl implements Employee
{
  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   *
   * @generated
   */
  protected EmployeeImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   *
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return InventoryPackage.Literals.EMPLOYEE;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   *
   * @generated
   */
  @Override
  protected int eStaticFeatureCount()
  {
    return 0;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   *
   * @generated
   */
  public Department getDepartment()
  {
    return (Department)eGet(InventoryPackage.Literals.EMPLOYEE__DEPARTMENT, true);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   *
   * @generated
   */
  public void setDepartment(Department newDepartment)
  {
    eSet(InventoryPackage.Literals.EMPLOYEE__DEPARTMENT, newDepartment);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   *
   * @generated
   */
  public String getName()
  {
    return (String)eGet(InventoryPackage.Literals.EMPLOYEE__NAME, true);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   *
   * @generated
   */
  public void setName(String newName)
  {
    eSet(InventoryPackage.Literals.EMPLOYEE__NAME, newName);
  }

} // EmployeeImpl
