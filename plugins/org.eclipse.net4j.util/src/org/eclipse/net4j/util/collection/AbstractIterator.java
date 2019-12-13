/*
 * Copyright (c) 2011, 2012, 2014, 2016, 2019 Eike Stepper (Loehne, Germany) and others.
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
import java.util.NoSuchElementException;

/**
 * An abstract base class for custom iterators that only requires to implement a single {@link #computeNextElement()} method.
 *
 * @author Eike Stepper
 * @since 3.2
 */
public abstract class AbstractIterator<T> implements Iterator<T>
{
  /**
   * The token to be used in {@link #computeNextElement()} to indicate the end of the iteration.
   */
  protected static final Object END_OF_DATA = new Object();

  private boolean nextComputed;

  private T next;

  public AbstractIterator()
  {
  }

  @Override
  public final boolean hasNext()
  {
    if (nextComputed)
    {
      return true;
    }

    Object object = computeNextElement();
    nextComputed = true;

    if (object == END_OF_DATA)
    {
      return false;
    }

    @SuppressWarnings("unchecked")
    T cast = (T)object;
    next = cast;
    return true;
  }

  @Override
  public final T next()
  {
    if (!hasNext())
    {
      throw new NoSuchElementException();
    }

    nextComputed = false;
    return next;
  }

  @Override
  public void remove()
  {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the next iteration element, or {@link #END_OF_DATA} if the end of the iteration has been reached.
   */
  protected abstract Object computeNextElement();

  /**
   * @since 3.4
   */
  @SuppressWarnings("unchecked")
  public static <T> ListIterator<T> empty()
  {
    return (ListIterator<T>)EmptyIterator.INSTANCE;
  }

  /**
   * @author Eike Stepper
   */
  static final class EmptyIterator implements ListIterator<Object>, CloseableIterator<Object>
  {
    static final ListIterator<Object> INSTANCE = new EmptyIterator();

    @Override
    public boolean hasNext()
    {
      return false;
    }

    @Override
    public Object next()
    {
      throw new NoSuchElementException();
    }

    @Override
    public boolean hasPrevious()
    {
      return false;
    }

    @Override
    public Object previous()
    {
      throw new NoSuchElementException();
    }

    @Override
    public int nextIndex()
    {
      throw new NoSuchElementException();
    }

    @Override
    public int previousIndex()
    {
      throw new NoSuchElementException();
    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public void set(Object e)
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public void add(Object e)
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public void close()
    {
      // Do nothing.
    }

    @Override
    public boolean isClosed()
    {
      return true;
    }
  }
}
