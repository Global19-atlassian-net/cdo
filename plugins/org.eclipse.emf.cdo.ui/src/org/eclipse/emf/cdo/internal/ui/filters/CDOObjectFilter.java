/*
 * Copyright (c) 2008, 2009, 2011, 2012 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.internal.ui.filters;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * @author Victor Roldan Betancort
 */
public abstract class CDOObjectFilter extends ViewerFilter
{
  private StructuredViewer viewer;

  private String pattern = ""; //$NON-NLS-1$

  public CDOObjectFilter(StructuredViewer viewer)
  {
    this.viewer = viewer;
    viewer.addFilter(this);
  }

  public String getPattern()
  {
    return pattern;
  }

  public void setPattern(String pattern)
  {
    parsePattern(pattern);
    this.pattern = pattern;
    viewer.refresh();
  }

  protected abstract void parsePattern(String pattern);

  public abstract String getDescription();

  public abstract String getTitle();
}
