/**
 * Copyright (c) 2004 - 2008 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *
 * $Id: Model2Factory.java,v 1.5 2008-12-28 17:56:56 estepper Exp $
 */
package org.eclipse.emf.cdo.tests.model2;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each non-abstract class of
 * the model. <!-- end-user-doc -->
 * @see org.eclipse.emf.cdo.tests.model2.Model2Package
 * @generated
 */
public interface Model2Factory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  Model2Factory eINSTANCE = org.eclipse.emf.cdo.tests.model2.impl.Model2FactoryImpl.init();

  /**
   * Returns a new object of class '<em>Special Purchase Order</em>'.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @return a new object of class '<em>Special Purchase Order</em>'.
   * @generated
   */
  SpecialPurchaseOrder createSpecialPurchaseOrder();

  /**
   * Returns a new object of class '<em>Task Container</em>'.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @return a new object of class '<em>Task Container</em>'.
   * @generated
   */
  TaskContainer createTaskContainer();

  /**
   * Returns a new object of class '<em>Task</em>'.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @return a new object of class '<em>Task</em>'.
   * @generated
   */
  Task createTask();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  Model2Package getModel2Package();

} // Model2Factory
