/**
 * Copyright (c) 2004 - 2009 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Simon McDuff - delta support
 */
package org.eclipse.emf.cdo.common.revision;

import org.eclipse.emf.cdo.common.branch.CDOBranch;
import org.eclipse.emf.cdo.common.branch.CDOBranchPoint;
import org.eclipse.emf.cdo.common.revision.delta.CDORevisionDelta;

import org.eclipse.emf.ecore.EClass;

/**
 * @author Eike Stepper
 */
public interface CDORevision extends CDORevisionKey, CDOBranchPoint
{
  /**
   * @since 3.0
   */
  public static final int MAIN_BRANCH_ID = CDOBranch.MAIN_BRANCH_ID;

  public static final int UNCHUNKED = -1;

  /**
   * @since 3.0
   */
  public static final int DEPTH_NONE = 0;

  /**
   * @since 3.0
   */
  public static final int DEPTH_INFINITE = -1;

  /**
   * @since 2.0
   */
  public EClass getEClass();

  public int getVersion();

  public long getRevised();

  /**
   * Returns <code>true</code> exactly if {@link #getTimeStamp()} does not return {@link #UNSPECIFIED_DATE},
   * <code>false</code> otherwise.
   * 
   * @since 3.0
   */
  public boolean isHistorical();

  public boolean isValid(long timeStamp);

  public boolean isTransactional();

  /**
   * @since 2.0
   */
  public boolean isResourceNode();

  /**
   * @since 2.0
   */
  public boolean isResourceFolder();

  public boolean isResource();

  /**
   * @since 2.0
   */
  public CDORevisionData data();

  public CDORevisionDelta compare(CDORevision origin);

  public void merge(CDORevisionDelta delta);

  /**
   * @since 2.0
   */
  public CDORevision copy();
}
