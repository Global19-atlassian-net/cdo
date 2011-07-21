/***************************************************************************
 * Copyright (c) 2004 - 2008 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 **************************************************************************/
package org.eclipse.emf.cdo.server;

import org.eclipse.emf.cdo.common.id.CDOID;

/**
 * @author Eike Stepper
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IResourceManager extends IRepositoryElement
{
  /**
   * Returns the <code>CDOID</code> of the resource with the given path.
   */
  public CDOID getResourceID(String path);

  /**
   * Returns the path of the resource with the given <code>CDOID</code>.
   */
  public String getResourcePath(CDOID id);
}