/*
 * Copyright (c) 2010-2012, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.internal.cdo.session;

import org.eclipse.net4j.util.security.IPasswordCredentials;
import org.eclipse.net4j.util.security.IPasswordCredentialsProvider;
import org.eclipse.net4j.util.security.SecurityUtil;

/**
 * @author Eike Stepper
 */
@Deprecated
public class CDOAuthenticatorImpl implements org.eclipse.emf.cdo.common.protocol.CDOAuthenticator
{
  private String encryptionAlgorithmName = SecurityUtil.PBE_WITH_MD5_AND_DES;

  private byte[] encryptionSaltBytes = SecurityUtil.DEFAULT_SALT;

  private int encryptionIterationCount = SecurityUtil.DEFAULT_ITERATION_COUNT;

  private IPasswordCredentialsProvider credentialsProvider;

  public CDOAuthenticatorImpl()
  {
  }

  public String getEncryptionAlgorithmName()
  {
    return encryptionAlgorithmName;
  }

  public void setEncryptionAlgorithmName(String encryptionAlgorithmName)
  {
    this.encryptionAlgorithmName = encryptionAlgorithmName;
  }

  public byte[] getEncryptionSaltBytes()
  {
    return encryptionSaltBytes;
  }

  public void setEncryptionSaltBytes(byte[] encryptionSaltBytes)
  {
    this.encryptionSaltBytes = encryptionSaltBytes;
  }

  public int getEncryptionIterationCount()
  {
    return encryptionIterationCount;
  }

  public void setEncryptionIterationCount(int encryptionIterationCount)
  {
    this.encryptionIterationCount = encryptionIterationCount;
  }

  public IPasswordCredentialsProvider getCredentialsProvider()
  {
    return credentialsProvider;
  }

  public void setCredentialsProvider(IPasswordCredentialsProvider credentialsProvider)
  {
    this.credentialsProvider = credentialsProvider;
  }

  public org.eclipse.emf.cdo.spi.common.CDOAuthenticationResult authenticate(byte[] randomToken)
  {
    if (credentialsProvider == null)
    {
      throw new IllegalStateException("No credentials provider configured"); //$NON-NLS-1$
    }

    IPasswordCredentials credentials = credentialsProvider.getCredentials();
    if (credentials != null)
    {
      String userID = credentials.getUserID();
      byte[] cryptedToken = encryptToken(credentials.getPassword(), randomToken);
      return new org.eclipse.emf.cdo.spi.common.CDOAuthenticationResult(userID, cryptedToken);
    }

    return null;
  }

  protected byte[] encryptToken(char[] password, byte[] token)
  {
    try
    {
      return SecurityUtil.encrypt(token, password, encryptionAlgorithmName, encryptionSaltBytes, encryptionIterationCount);
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new SecurityException(ex);
    }
  }
}
