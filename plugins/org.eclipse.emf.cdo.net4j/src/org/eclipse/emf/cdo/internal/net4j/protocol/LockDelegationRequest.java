/**
 * Copyright (c) 2004 - 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Caspar De Groot - initial API and implementation
 */
package org.eclipse.emf.cdo.internal.net4j.protocol;

import org.eclipse.emf.cdo.common.branch.CDOBranch;
import org.eclipse.emf.cdo.common.protocol.CDODataOutput;
import org.eclipse.emf.cdo.common.protocol.CDOProtocolConstants;
import org.eclipse.emf.cdo.common.revision.CDORevisionKey;

import org.eclipse.net4j.util.concurrent.IRWLockManager.LockType;

import java.io.IOException;
import java.util.List;

/**
 * @author Caspar De Groot
 */
public class LockDelegationRequest extends LockObjectsRequest
{
  private String lockAreaID;

  public LockDelegationRequest(CDOClientProtocol protocol, String lockAreaID, List<CDORevisionKey> revisionKeys,
      CDOBranch viewedBranch, LockType lockType, long timeout)
  {
    super(protocol, CDOProtocolConstants.SIGNAL_LOCK_DELEGATION, revisionKeys, 0, viewedBranch, lockType, timeout);
    this.lockAreaID = lockAreaID;
  }

  @Override
  protected void requesting(CDODataOutput out) throws IOException
  {
    out.writeString(lockAreaID);
    super.requesting(out);
  }
}