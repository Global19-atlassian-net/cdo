/*
 * Copyright (c) 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.util.container;

import org.eclipse.net4j.util.container.IContainerDelta.Kind;
import org.eclipse.net4j.util.event.EventUtil;
import org.eclipse.net4j.util.event.IEvent;
import org.eclipse.net4j.util.event.IListener;
import org.eclipse.net4j.util.lifecycle.ILifecycle;
import org.eclipse.net4j.util.lifecycle.LifecycleEventAdapter;
import org.eclipse.net4j.util.lifecycle.LifecycleUtil;

/**
 * @author Eike Stepper
 * @since 3.6
 */
public class SelfAttachingContainerListener implements IListener
{
  public SelfAttachingContainerListener()
  {
  }

  public void attach(Object element)
  {
    if (shouldAttach(element))
    {
      EventUtil.addListener(element, this);

      if (shouldDescend(element))
      {
        try
        {
          Object[] children = ContainerUtil.getElements(element);
          if (children != null)
          {
            for (Object child : children)
            {
              try
              {
                attach(child);
              }
              catch (Exception ex)
              {
                handleException(ex);
              }
            }
          }
        }
        catch (Exception ex)
        {
          handleException(ex);
        }
      }
    }
  }

  public void detach(Object element)
  {
    if (shouldAttach(element))
    {
      if (shouldDescend(element))
      {
        try
        {
          Object[] children = ContainerUtil.getElements(element);
          if (children != null)
          {
            for (Object child : children)
            {
              try
              {
                detach(child);
              }
              catch (Exception ex)
              {
                handleException(ex);
              }
            }
          }
        }
        catch (Exception ex)
        {
          handleException(ex);
        }
      }

      EventUtil.removeListener(element, this);
    }
  }

  public void notifyEvent(IEvent event)
  {
    if (event instanceof IContainerEvent<?>)
    {
      notifyContainerEvent((IContainerEvent<?>)event);
    }
    else
    {
      notifyOtherEvent(event);
    }
  }

  protected void notifyContainerEvent(IContainerEvent<?> event)
  {
    for (IContainerDelta<?> delta : event.getDeltas())
    {
      final Object element = delta.getElement();

      if (delta.getKind() == Kind.ADDED)
      {
        if (isWaitForActive() && !isActive(element))
        {
          EventUtil.addListener(element, new LifecycleEventAdapter()
          {
            @Override
            protected void onActivated(ILifecycle lifecycle)
            {
              lifecycle.removeListener(this);
              attach(element);
            }
          });
        }
        else
        {
          attach(element);
        }
      }
      else
      {
        detach(element);
      }
    }
  }

  protected void notifyOtherEvent(IEvent event)
  {
  }

  protected boolean shouldAttach(Object element)
  {
    return true;
  }

  protected boolean shouldDescend(Object element)
  {
    return !(element instanceof DoNotDescend);
  }

  protected boolean isWaitForActive()
  {
    return true;
  }

  protected boolean isActive(Object element)
  {
    return LifecycleUtil.isActive(element);
  }

  protected void handleException(Exception ex)
  {
  }

  /**
   * @author Eike Stepper
   */
  public interface DoNotDescend
  {
  }

  /**
   * @author Eike Stepper
   */
  public static class Delegating extends SelfAttachingContainerListener
  {
    private final IListener delegate;

    private final boolean delegateContainerEvents;

    public Delegating(IListener delegate, boolean delegateContainerEvents)
    {
      this.delegate = delegate;
      this.delegateContainerEvents = delegateContainerEvents;
    }

    public Delegating(IListener delegate)
    {
      this(delegate, false);
    }

    @Override
    protected void notifyContainerEvent(IContainerEvent<?> event)
    {
      super.notifyContainerEvent(event);

      if (delegateContainerEvents)
      {
        delegate.notifyEvent(event);
      }
    }

    @Override
    protected void notifyOtherEvent(IEvent event)
    {
      delegate.notifyEvent(event);
    }
  }
}
