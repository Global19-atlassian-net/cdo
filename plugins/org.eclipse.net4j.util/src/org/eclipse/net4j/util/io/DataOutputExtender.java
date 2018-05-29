/*
 * Copyright (c) 2007, 2008, 2010-2013, 2015, 2017 Eike Stepper (Loehne, Germany) and others.
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

/**
 * @author Eike Stepper
 */
public class DataOutputExtender implements ExtendedDataOutput, Closeable
{
  private DataOutput output;

  public DataOutputExtender(DataOutput output)
  {
    this.output = output;
  }

  public void write(byte[] b, int off, int len) throws IOException
  {
    output.write(b, off, len);
  }

  public void write(byte[] b) throws IOException
  {
    output.write(b);
  }

  public void write(int b) throws IOException
  {
    output.write(b);
  }

  public void writeBoolean(boolean v) throws IOException
  {
    output.writeBoolean(v);
  }

  public void writeByte(int v) throws IOException
  {
    output.writeByte(v);
  }

  public void writeBytes(String s) throws IOException
  {
    output.writeBytes(s);
  }

  public void writeChar(int v) throws IOException
  {
    output.writeChar(v);
  }

  public void writeChars(String s) throws IOException
  {
    output.writeChars(s);
  }

  public void writeDouble(double v) throws IOException
  {
    output.writeDouble(v);
  }

  public void writeFloat(float v) throws IOException
  {
    output.writeFloat(v);
  }

  public void writeInt(int v) throws IOException
  {
    output.writeInt(v);
  }

  public void writeLong(long v) throws IOException
  {
    output.writeLong(v);
  }

  public void writeShort(int v) throws IOException
  {
    output.writeShort(v);
  }

  public void writeUTF(String str) throws IOException
  {
    output.writeUTF(str);
  }

  /**
   * @since 3.7
   */
  public void writeVarInt(int v) throws IOException
  {
    ExtendedIOUtil.writeVarInt(output, v);
  }

  /**
  * @since 3.7
  */
  public void writeVarLong(long v) throws IOException
  {
    ExtendedIOUtil.writeVarLong(output, v);
  }

  public void writeByteArray(byte[] b) throws IOException
  {
    ExtendedIOUtil.writeByteArray(output, b);
  }

  public void writeObject(Object object) throws IOException
  {
    ExtendedIOUtil.writeObject(output, object);
  }

  public void writeString(String str) throws IOException
  {
    ExtendedIOUtil.writeString(output, str);
  }

  /**
   * @since 3.0
   */
  public void writeEnum(Enum<?> literal) throws IOException
  {
    ExtendedIOUtil.writeEnum(output, literal);
  }

  /**
   * @since 3.4
   */
  public void writeException(Throwable t) throws IOException
  {
    ExtendedIOUtil.writeException(output, t);
  }

  /**
   * @since 3.6
   */
  public void close() throws IOException
  {
    if (output instanceof Closeable)
    {
      ((Closeable)output).close();
    }
  }
}
