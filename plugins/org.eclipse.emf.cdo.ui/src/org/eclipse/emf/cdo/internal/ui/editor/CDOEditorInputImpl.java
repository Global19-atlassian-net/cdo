/*
 * Copyright (c) 2020 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.internal.ui.editor;

import org.eclipse.emf.cdo.internal.ui.AbstractCDOEditorInput;
import org.eclipse.emf.cdo.view.CDOView;

/**
 * @author Eike Stepper
 */
public class CDOEditorInputImpl extends AbstractCDOEditorInput
{
  private CDOView view;

  private boolean viewOwned;

  public CDOEditorInputImpl(CDOView view, String resourcePath)
  {
    this(view, resourcePath, false);
  }

  public CDOEditorInputImpl(CDOView view, String resourcePath, boolean viewOwned)
  {
    super(resourcePath, null);
    this.view = view;
    this.viewOwned = viewOwned;
  }

  @Override
  public CDOView getView()
  {
    return view;
  }

  @Override
  public boolean isViewOwned()
  {
    return viewOwned;
  }
}
