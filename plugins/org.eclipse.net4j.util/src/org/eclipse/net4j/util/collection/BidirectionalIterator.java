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
import java.util.ListIterator;

/**
 * @author Eike Stepper
 * @since 3.3
 */
public class BidirectionalIterator<T> implements Iterator<T>
{
  private final ListIterator<T> delegate;

  private boolean backward;

  public BidirectionalIterator(ListIterator<T> delegate, boolean backward)
  {
    this.delegate = delegate;
    this.backward = backward;
  }

  public BidirectionalIterator(ListIterator<T> delegate)
  {
    this(delegate, false);
  }

  public ListIterator<T> getDelegate()
  {
    return delegate;
  }

  public boolean isBackward()
  {
    return backward;
  }

  public void setBackward(boolean backward)
  {
    this.backward = backward;
  }

  public boolean hasNext()
  {
    return backward ? delegate.hasPrevious() : delegate.hasNext();
  }

  public T next()
  {
    return backward ? delegate.previous() : delegate.next();
  }

  public void remove()
  {
    delegate.remove();
  }
}
