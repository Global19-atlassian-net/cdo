/*
 * Copyright (c) 2011, 2012, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.dawn.gmf.stylizer;

import org.eclipse.emf.cdo.dawn.appearance.DawnElementStylizer;
import org.eclipse.emf.cdo.dawn.appearance.IDawnElementStylizerFactory;
import org.eclipse.emf.cdo.dawn.gmf.appearance.impl.DawnBasicConnectionEditPartStylizerImpl;
import org.eclipse.emf.cdo.dawn.gmf.appearance.impl.DawnBasicGraphicalEditPartStylizerImpl;
import org.eclipse.emf.cdo.dawn.gmf.appearance.impl.DawnBasicNodeEditPartStylizerImpl;
import org.eclipse.emf.cdo.dawn.gmf.appearance.impl.DawnBasicTextAwareEditPartStylizerImpl;

import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;

/**
 * @author Martin Fluegge
 */
public class DawnGMFElementStylizerFactory implements IDawnElementStylizerFactory
{
  public DawnElementStylizer getElementStylizer(Object object)
  {
    DawnElementStylizer stylizer = null;
    if (object instanceof ConnectionEditPart)
    {
      stylizer = new DawnBasicConnectionEditPartStylizerImpl();
    }
    else if (object instanceof NodeEditPart)
    {
      stylizer = new DawnBasicNodeEditPartStylizerImpl();
    }
    else if (object instanceof DiagramEditPart)
    {
      stylizer = new DawnBasicNodeEditPartStylizerImpl();
    }
    else if (object instanceof ITextAwareEditPart)
    {
      stylizer = new DawnBasicTextAwareEditPartStylizerImpl();
    }
    else
    {
      // In the case that there is no match we use a simple border styled stylizer.
      stylizer = new DawnBasicGraphicalEditPartStylizerImpl();
    }

    return stylizer;
  }
}
