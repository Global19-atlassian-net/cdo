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
package org.eclipse.emf.cdo.internal.server.protocol;

import org.eclipse.emf.cdo.common.CDOProtocolConstants;
import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.emf.cdo.internal.server.bundle.OM;
import org.eclipse.emf.cdo.spi.common.InternalCDORevision;

import org.eclipse.net4j.util.io.ExtendedDataInputStream;
import org.eclipse.net4j.util.om.trace.ContextTracer;

import java.io.IOException;

/**
 * @author Eike Stepper
 */
public class LoadRevisionByTimeIndication extends LoadRevisionIndication
{
  private static final ContextTracer PROTOCOL = new ContextTracer(OM.DEBUG_PROTOCOL, LoadRevisionByTimeIndication.class);

  private long timeStamp;

  public LoadRevisionByTimeIndication()
  {
  }

  @Override
  protected short getSignalID()
  {
    return CDOProtocolConstants.SIGNAL_LOAD_REVISION_BY_TIME;
  }

  @Override
  protected void indicating(ExtendedDataInputStream in) throws IOException
  {
    super.indicating(in);
    timeStamp = in.readLong();
    if (PROTOCOL.isEnabled()) PROTOCOL.format("Read timeStamp: {0}", timeStamp);
  }

  @Override
  protected InternalCDORevision getRevision(CDOID cdoID)
  {
    return getRevisionManager().getRevisionByTime(cdoID, referenceChunk, timeStamp);
  }
}