/*
 * Copyright (c) 2004 - 2012 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Martin Fluegge - initial API and implementation
 */
package org.eclipse.emf.cdo.dawn.graphiti.util;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gef.EditPart;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Fluegge
 */
public class DawnGraphitiUtil
{
  /**
   * This method tries to find an editpart for a given pictogram element. It recursivly searches all children for the
   * given editpart if the model matches to pictogramElement.
   */
  public static EditPart getEditpart(PictogramElement pictogramElement, EditPart part)
  {
    for (Object object : part.getChildren())
    {
      EditPart child = (EditPart)object;
      if (child.getModel().equals(pictogramElement))
      {
        return child;
      }

      EditPart childEditPart = getEditpart(pictogramElement, child);
      if (childEditPart != null)
      {
        return childEditPart;
      }
    }
    return null;
  }

  public static List<PictogramElement> getPictgramElements(Diagram diagram, EObject element)
  {
    PictogramElement pictgramElement = getPictgramElement(element);

    if (element instanceof PictogramElement)
    {
      return Collections.singletonList((PictogramElement)element);
    }

    if (pictgramElement != null)
    {
      return Collections.singletonList(pictgramElement);
    }

    return Graphiti.getLinkService().getPictogramElements(diagram, element);
  }

  /**
   * Tries to retriev the pictogram element from a given element. If the element itself is a PictogramElement, the
   * element will be returned. Otherwise all eContainers will be checked until a PictogramElement is found.
   */
  public static PictogramElement getPictgramElement(EObject element)
  {
    if (element == null)
    {
      return null;
    }

    if (element instanceof PictogramElement)
    {
      return (PictogramElement)element;
    }

    EObject eContainer = element.eContainer();

    if (eContainer instanceof PictogramElement)
    {
      return (PictogramElement)eContainer;
    }
    return getPictgramElement(eContainer);
  }
}