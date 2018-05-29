/*
 * Copyright (c) 2010-2012, 2015, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.tests.model5;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Gen List Of Int Array</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.emf.cdo.tests.model5.GenListOfIntArray#getElements <em>Elements</em>}</li>
 * </ul>
 *
 * @see org.eclipse.emf.cdo.tests.model5.Model5Package#getGenListOfIntArray()
 * @model
 * @generated
 */
public interface GenListOfIntArray extends EObject
{
  /**
   * Returns the value of the '<em><b>Elements</b></em>' attribute list.
   * The list contents are of type {@link int}<code>[]</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Elements</em>' attribute list isn't clear, there really should be more of a description
   * here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Elements</em>' attribute list.
   * @see org.eclipse.emf.cdo.tests.model5.Model5Package#getGenListOfIntArray_Elements()
   * @model dataType="org.eclipse.emf.cdo.tests.model5.IntArray"
   * @generated
   */
  EList<int[]> getElements();

} // GenListOfIntArray
