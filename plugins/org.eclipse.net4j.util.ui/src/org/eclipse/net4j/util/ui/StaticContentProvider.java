/*
 * Copyright (c) 2007, 2009, 2011, 2012, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.util.ui;

import java.util.Collection;

/**
 * @author Eike Stepper
 */
public class StaticContentProvider extends StructuredContentProvider<Object>
{
  private Object[] elements;

  public StaticContentProvider(Object[] elements)
  {
    this.elements = elements;
  }

  public StaticContentProvider(Collection<?> elements)
  {
    this(elements.toArray());
  }

  public StaticContentProvider(Class<Object> enumClass)
  {
    this(createElements(enumClass));
  }

  public Object[] getElements(Object inputElement)
  {
    return elements;
  }

  private static Object[] createElements(Class<Object> enumClass)
  {
    Object[] enumConstants = enumClass.getEnumConstants();
    if (enumConstants == null)
    {
      throw new IllegalArgumentException("Illegal enum: " + enumClass); //$NON-NLS-1$
    }

    return enumConstants;
  }
}
