/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.internal.util.table;

/**
 * @author Eike Stepper
 */
public final class Coordinate implements Comparable<Coordinate>
{
  public static final Coordinate ZERO = new Coordinate(0, 0);

  public final int col;

  public final int row;

  public Coordinate(int col, int row)
  {
    if (col < 0)
    {
      throw new IllegalArgumentException("Column is negative");
    }

    if (row < 0)
    {
      throw new IllegalArgumentException("Row is negative");
    }

    this.col = col;
    this.row = row;
  }

  public Coordinate offset(int cols, int rows)
  {
    return new Coordinate(col + cols, row + rows);
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + col;
    result = prime * result + row;
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }

    if (obj == null)
    {
      return false;
    }

    if (Coordinate.class != obj.getClass())
    {
      return false;
    }

    Coordinate other = (Coordinate)obj;
    return col == other.col && row == other.row;
  }

  public int compareTo(Coordinate o)
  {
    int result = row - o.row;
    if (result == 0)
    {
      return col - o.col;
    }

    return result;
  }

  @Override
  public String toString()
  {
    return "[" + col + "," + row + "]";
  }
}
