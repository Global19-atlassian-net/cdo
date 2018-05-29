/*
 * Copyright (c) 2007-2013, 2015, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.util.io;

import java.io.Closeable;
import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Eike Stepper
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface ExtendedDataOutput extends DataOutput
{
  /**
   * @since 3.7
   */
  public void writeVarInt(int v) throws IOException;

  /**
   * @since 3.7
   */
  public void writeVarLong(long v) throws IOException;

  public void writeByteArray(byte[] b) throws IOException;

  public void writeObject(Object object) throws IOException;

  public void writeString(String str) throws IOException;

  /**
   * @since 3.0
   */
  public void writeEnum(Enum<?> literal) throws IOException;

  /**
   * @since 3.4
   */
  public void writeException(Throwable t) throws IOException;

  /**
   * @author Eike Stepper
   * @since 2.0
   */
  public static class Delegating implements ExtendedDataOutput, Closeable
  {
    private ExtendedDataOutput delegate;

    public Delegating(ExtendedDataOutput delegate)
    {
      this.delegate = delegate;
    }

    public ExtendedDataOutput getDelegate()
    {
      return delegate;
    }

    public void write(byte[] b, int off, int len) throws IOException
    {
      delegate.write(b, off, len);
    }

    public void write(byte[] b) throws IOException
    {
      delegate.write(b);
    }

    public void write(int b) throws IOException
    {
      delegate.write(b);
    }

    public void writeBoolean(boolean v) throws IOException
    {
      delegate.writeBoolean(v);
    }

    public void writeByte(int v) throws IOException
    {
      delegate.writeByte(v);
    }

    public void writeByteArray(byte[] b) throws IOException
    {
      delegate.writeByteArray(b);
    }

    public void writeBytes(String s) throws IOException
    {
      delegate.writeBytes(s);
    }

    public void writeChar(int v) throws IOException
    {
      delegate.writeChar(v);
    }

    public void writeChars(String s) throws IOException
    {
      delegate.writeChars(s);
    }

    public void writeDouble(double v) throws IOException
    {
      delegate.writeDouble(v);
    }

    public void writeFloat(float v) throws IOException
    {
      delegate.writeFloat(v);
    }

    public void writeInt(int v) throws IOException
    {
      delegate.writeInt(v);
    }

    public void writeLong(long v) throws IOException
    {
      delegate.writeLong(v);
    }

    public void writeObject(Object object) throws IOException
    {
      delegate.writeObject(object);
    }

    public void writeShort(int v) throws IOException
    {
      delegate.writeShort(v);
    }

    public void writeString(String str) throws IOException
    {
      delegate.writeString(str);
    }

    /**
     * @since 3.0
     */
    public void writeEnum(Enum<?> literal) throws IOException
    {
      delegate.writeEnum(literal);
    }

    /**
     * @since 3.4
     */
    public void writeException(Throwable t) throws IOException
    {
      delegate.writeException(t);
    }

    public void writeUTF(String str) throws IOException
    {
      delegate.writeUTF(str);
    }

    /**
     * @since 3.7
     */
    public void writeVarInt(int v) throws IOException
    {
      delegate.writeVarInt(v);
    }

    /**
     * @since 3.7
     */
    public void writeVarLong(long v) throws IOException
    {
      delegate.writeVarLong(v);
    }

    /**
     * @since 3.6
     */
    public void close() throws IOException
    {
      if (delegate instanceof Closeable)
      {
        ((Closeable)delegate).close();
      }
    }
  }

  /**
   * @author Eike Stepper
   * @since 2.0
   */
  public static class Stream extends OutputStream
  {
    private ExtendedDataOutput delegate;

    public Stream(ExtendedDataOutput delegate)
    {
      this.delegate = delegate;
    }

    public ExtendedDataOutput getDelegate()
    {
      return delegate;
    }

    @Override
    public void write(int b) throws IOException
    {
      delegate.write(b);
    }

    @Override
    public void close() throws IOException
    {
      if (delegate instanceof Closeable)
      {
        ((Closeable)delegate).close();
      }

      super.close();
    }
  }
}
