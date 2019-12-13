/*
 * Copyright (c) 2016, 2019 Eike Stepper (Loehne, Germany) and others.
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
import java.util.NoSuchElementException;

/**
 * @author Eike Stepper
 * @since 3.7
 */
public class DelegatingCloseableIterator<E> implements CloseableIterator<E>
{
  private final Iterator<E> delegate;

  private boolean closed;

  public DelegatingCloseableIterator(Iterator<E> delegate)
  {
    this.delegate = delegate;
  }

  @Override
  public boolean hasNext()
  {
    return !closed && delegate.hasNext();
  }

  @Override
  public E next()
  {
    if (closed)
    {
      throw new NoSuchElementException();
    }

    return delegate.next();
  }

  @Override
  public void remove()
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void close()
  {
    closed = true;
  }

  @Override
  public boolean isClosed()
  {
    return closed;
  }
}
