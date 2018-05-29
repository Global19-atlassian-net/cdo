/*
 * Copyright (c) 2011, 2012, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Martin Fluegge - initial API and implementation
 */
package org.eclipse.emf.cdo.dawn.graphiti.editors;

import org.eclipse.emf.cdo.dawn.ui.DawnEditorInput;
import org.eclipse.emf.cdo.dawn.ui.IDawnEditorInput;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.view.CDOView;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.graphiti.ui.editor.DiagramEditorInput;

/**
 * @author Martin Fluegge
 */
public class DawnGraphitiEditorInput extends DiagramEditorInput implements IDawnEditorInput
{
  private DawnEditorInput input;

  public DawnGraphitiEditorInput(URI uri, String providerId)
  {
    super(uri, providerId);
    input = new DawnEditorInput(uri);
  }

  public DawnGraphitiEditorInput(URI uri, String providerId, Resource resource)
  {
    this(uri, providerId);
    input.setResource((CDOResource)resource);
  }

  public CDOView getView()
  {
    return input.getView();
  }

  public boolean isViewOwned()
  {
    return input.isViewOwned();
  }

  public String getResourcePath()
  {
    return input.getResourcePath();
  }

  /**
   * This id is needed to provide the correct IElementFactory to the framework and allow to create a
   * DawnGraphitiEditorInput from the persisted state on restoring an editor. See extension point
   * org.eclipse.ui.elementFactories.
   */
  @Override
  public String getFactoryId()
  {
    return DawnGraphitiDiagramEditorInputFactory.class.getName();
  }
}
