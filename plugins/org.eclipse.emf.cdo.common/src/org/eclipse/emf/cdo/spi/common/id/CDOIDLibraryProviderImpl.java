/**
 * Copyright (c) 2004 - 2009 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.spi.common.id;

import org.eclipse.emf.cdo.common.id.CDOIDLibraryDescriptor;
import org.eclipse.emf.cdo.common.id.CDOIDLibraryProvider;
import org.eclipse.emf.cdo.internal.common.messages.Messages;

import org.eclipse.net4j.util.io.ExtendedDataInput;
import org.eclipse.net4j.util.io.IOUtil;
import org.eclipse.net4j.util.om.OMBundle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 * @since 2.0
 */
public class CDOIDLibraryProviderImpl implements CDOIDLibraryProvider
{
  private Map<String, OMBundle> bundles = new HashMap<String, OMBundle>();

  public CDOIDLibraryProviderImpl()
  {
  }

  public void addLibrary(String libraryName, OMBundle bundle)
  {
    bundles.put(libraryName, bundle);
  }

  public String[] getLibraryNames()
  {
    Set<String> libraryNames = bundles.keySet();
    return libraryNames.toArray(new String[libraryNames.size()]);
  }

  public CDOIDLibraryDescriptor createDescriptor(String factoryName)
  {
    return new Descriptor(factoryName);
  }

  public InputStream getContents(String libraryName) throws IOException
  {
    File library = getLibrary(libraryName);
    return IOUtil.openInputStream(library);
  }

  public int getSize(String libraryName)
  {
    File library = getLibrary(libraryName);
    return (int)library.length();
  }

  private File getLibrary(String libraryName)
  {
    OMBundle bundle = bundles.get(libraryName);
    if (bundle == null)
    {
      throw new IllegalStateException(MessageFormat.format(
          Messages.getString("CDOIDLibraryProviderImpl.0"), libraryName)); //$NON-NLS-1$
    }

    URL url = bundle.getBaseURL();
    File file = new File(url.getFile());
    if (file.exists() && file.isDirectory())
    {
      file = new File(file, libraryName);
    }

    if (file.exists() && file.isFile() && file.getName().endsWith(".jar")) //$NON-NLS-1$
    {
      return file;
    }

    throw new IllegalStateException(MessageFormat.format(
        Messages.getString("CDOIDLibraryProviderImpl.2"), file.getAbsolutePath())); //$NON-NLS-1$
  }

  /**
   * @author Eike Stepper
   */
  private final class Descriptor extends CDOIDLibraryDescriptorImpl
  {
    public Descriptor(String factoryName)
    {
      super(factoryName, CDOIDLibraryProviderImpl.this.getLibraryNames());
    }

    public Descriptor(ExtendedDataInput in) throws IOException
    {
      super(in);
    }
  }
}
