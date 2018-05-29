/*
 * Copyright (c) 2013 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.internal.security;

import org.eclipse.emf.cdo.common.revision.CDORevisionProvider;
import org.eclipse.emf.cdo.view.CDOView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public final class PermissionUtil
{
  private static final ThreadLocal<String> USER = new ThreadLocal<String>();

  private static final ThreadLocal<ViewCreator> VIEW_CREATOR = new ThreadLocal<ViewCreator>();

  private static final ThreadLocal<Map<CDORevisionProvider, CDOView>> VIEWS = new ThreadLocal<Map<CDORevisionProvider, CDOView>>();

  private PermissionUtil()
  {
  }

  public static String getUser()
  {
    return USER.get();
  }

  public static void setUser(String user)
  {
    if (user == null)
    {
      USER.remove();
    }
    else
    {
      USER.set(user);
    }
  }

  private static Map<CDORevisionProvider, CDOView> getViews()
  {
    Map<CDORevisionProvider, CDOView> views = VIEWS.get();
    if (views == null)
    {
      views = new HashMap<CDORevisionProvider, CDOView>();
      VIEWS.set(views);
    }

    return views;
  }

  public static CDOView getView(CDORevisionProvider revisionProvider)
  {
    Map<CDORevisionProvider, CDOView> views = getViews();

    CDOView view = views.get(revisionProvider);
    if (view == null)
    {
      ViewCreator viewCreator = VIEW_CREATOR.get();
      if (viewCreator == null)
      {
        throw new IllegalStateException("No view creator available for " + revisionProvider);
      }

      view = viewCreator.createView(revisionProvider);
      views.put(revisionProvider, view);
    }

    return view;
  }

  public static void initViewCreation(ViewCreator viewCreator)
  {
    VIEW_CREATOR.set(viewCreator);
  }

  public static void doneViewCreation()
  {
    VIEW_CREATOR.remove();
    VIEWS.remove();
  }
}
