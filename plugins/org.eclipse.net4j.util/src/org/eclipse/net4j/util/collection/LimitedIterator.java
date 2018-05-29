/*
 * Copyright (c) 2012 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.util.collection;

import java.util.Iterator;

/**
 * @author Eike Stepper
 * @since 3.3
 */
public class LimitedIterator<T> extends AbstractFilteredIterator<T>
{
  private final int limit;

  private int count;

  public LimitedIterator(Iterator<T> delegate, int limit)
  {
    super(delegate);
    this.limit = limit;
  }

  public final int getLimit()
  {
    return limit;
  }

  public final int getCount()
  {
    return count;
  }

  @Override
  protected boolean isValid(T element)
  {
    return ++count <= limit;
  }
}
