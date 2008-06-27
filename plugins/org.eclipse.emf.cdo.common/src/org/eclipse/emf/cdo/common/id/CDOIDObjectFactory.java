/***************************************************************************
 * Copyright (c) 2004 - 2008 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Simon McDuff - https://bugs.eclipse.org/226778
 **************************************************************************/
package org.eclipse.emf.cdo.common.id;

import org.eclipse.net4j.util.io.ExtendedDataInput;

/**
 * @author Eike Stepper
 */
public interface CDOIDObjectFactory
{
  /**
   * Returns a new instance of CDOIDObject. The implementor of this method may use (read from) the given data input to
   * determine which class to instantiate. The data input must not be used to fill the state of the new instance, this
   * has to be done in the implementation of the {@link CDOID#read(ExtendedDataInput) read()} method of the new
   * instance.
   */
  public CDOIDObject createCDOIDObject(ExtendedDataInput in);
  
  /**
   * Returns a new instance of CDOIDObject. This implementation is different from createCDOIDObject(ExtendedDataInput
   * in) since the implementor of this method needs to construct completely CDOIDObject from the String.
   */
  public CDOIDObject createCDOIDObject(String in);

}
