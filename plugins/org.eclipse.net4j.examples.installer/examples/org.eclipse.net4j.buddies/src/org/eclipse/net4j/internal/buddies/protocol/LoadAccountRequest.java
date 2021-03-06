/*
 * Copyright (c) 2007, 2008, 2011, 2012, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.internal.buddies.protocol;

import org.eclipse.net4j.buddies.common.IAccount;
import org.eclipse.net4j.buddies.internal.common.protocol.ProtocolConstants;
import org.eclipse.net4j.buddies.internal.common.protocol.ProtocolUtil;
import org.eclipse.net4j.signal.RequestWithConfirmation;
import org.eclipse.net4j.util.io.ExtendedDataInputStream;
import org.eclipse.net4j.util.io.ExtendedDataOutputStream;

/**
 * @author Eike Stepper
 */
public class LoadAccountRequest extends RequestWithConfirmation<IAccount>
{
  private String userID;

  public LoadAccountRequest(BuddiesClientProtocol protocol, String userID)
  {
    super(protocol, ProtocolConstants.SIGNAL_LOAD_ACCOUNT);
    this.userID = userID;
  }

  @Override
  protected void requesting(ExtendedDataOutputStream out) throws Exception
  {
    out.writeString(userID);
  }

  @Override
  protected IAccount confirming(ExtendedDataInputStream in) throws Exception
  {
    return ProtocolUtil.readAccount(in);
  }
}
