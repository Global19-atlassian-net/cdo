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
package org.eclipse.net4j.buddies.common;

import org.eclipse.net4j.signal.SignalProtocol;
import org.eclipse.net4j.util.event.INotifier;

import org.eclipse.core.runtime.IAdaptable;

/**
 * @author Eike Stepper
 */
public interface ISession extends INotifier, IAdaptable
{
  /**
   * @since 2.0
   */
  public SignalProtocol getProtocol();

  public IBuddy getSelf();

  public void close();
}
