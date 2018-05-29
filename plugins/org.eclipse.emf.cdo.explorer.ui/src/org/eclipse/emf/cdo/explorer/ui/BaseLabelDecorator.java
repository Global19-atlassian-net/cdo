/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.explorer.ui;

import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * @author Eike Stepper
 */
public abstract class BaseLabelDecorator implements ILabelDecorator
{
  public BaseLabelDecorator()
  {
  }

  public void dispose()
  {
  }

  public Image decorateImage(Image image, Object element)
  {
    return null;
  }

  public String decorateText(String text, Object element)
  {
    return null;
  }

  public boolean isLabelProperty(Object element, String property)
  {
    return false;
  }

  public void addListener(ILabelProviderListener listener)
  {
    // Ignore listeners, DecoratorManager handles them.
  }

  public void removeListener(ILabelProviderListener listener)
  {
    // Ignore listeners, DecoratorManager handles them.
  }
}
