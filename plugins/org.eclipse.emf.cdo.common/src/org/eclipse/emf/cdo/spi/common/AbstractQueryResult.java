/*
 * Copyright (c) 2009-2012 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Simon McDuff - initial API and implementation
 *    Eike Stepper - maintenance
 */
package org.eclipse.emf.cdo.spi.common;

import org.eclipse.emf.cdo.common.CDOCommonView;
import org.eclipse.emf.cdo.common.util.BlockingCloseableIterator;
import org.eclipse.emf.cdo.common.util.CDOQueryInfo;
import org.eclipse.emf.cdo.common.util.CDOQueryQueue;

import org.eclipse.net4j.util.WrappedException;

/**
 * If the meaning of this type isn't clear, there really should be more of a description here...
 *
 * @author Simon McDuff
 * @since 2.0
 */
public class AbstractQueryResult<T> implements BlockingCloseableIterator<T>
{
  private int queryID;

  private CDOCommonView view;

  private CDOQueryInfo queryInfo;

  private CDOQueryQueue<Object> linkQueue = new CDOQueryQueue<Object>();

  private BlockingCloseableIterator<Object> queueItr = linkQueue.iterator();

  /**
   * @since 3.0
   */
  public AbstractQueryResult(CDOCommonView view, CDOQueryInfo queryInfo, int queryID)
  {
    this.queryID = queryID;
    this.view = view;
    this.queryInfo = queryInfo;
  }

  /**
   * @since 3.0
   */
  public CDOQueryInfo getQueryInfo()
  {
    return queryInfo;
  }

  public CDOQueryQueue<Object> getQueue()
  {
    return linkQueue;
  }

  public CDOCommonView getView()
  {
    return view;
  }

  public int getQueryID()
  {
    return queryID;
  }

  public void setQueryID(int queryID)
  {
    this.queryID = queryID;
  }

  @SuppressWarnings("unchecked")
  public T peek()
  {
    return (T)queueItr.peek();
  }

  public boolean hasNext()
  {
    return queueItr.hasNext();
  }

  @SuppressWarnings("unchecked")
  public T next()
  {
    Object element = queueItr.next();

    if (element instanceof Throwable)
    {
      if (element instanceof Error)
      {
        throw (Error)element;
      }

      throw WrappedException.wrap((Exception)element);
    }

    return (T)element;
  }

  public void remove()
  {
    throw new UnsupportedOperationException();
  }

  public void close()
  {
    queueItr.close();
  }

  public boolean isClosed()
  {
    return queueItr.isClosed();
  }
}
