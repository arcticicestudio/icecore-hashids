/*
+++++++++++++++++++++++++++++++++++++++++++
title     Hashid                          +
project   icecore-hashids                 +
file      Hashid.java                     +
version   0.2.0                           +
author    Arctic Ice Studio               +
email     development@arcticicestudio.com +
website   http://arcticicestudio.com      +
copyright Copyright (C) 2016              +
created   2016-06-05 17:45 UTC+0200       +
modified  2016-06-05 18:09 UTC+0200       +
+++++++++++++++++++++++++++++++++++++++++++

[Description]
Represents a hashid which holds all numbers and the encoded string.

[Copyright]
Copyright (C) 2016 Arctic Ice Studio <development@arcticicestudio.com>

[References]
Hashids
  (http://hashids.org)
Java 8 API Documentation
  (https://docs.oracle.com/javase/8/docs/api/)
Arctic Versioning Specification (ArcVer)
  (http://specs.arcticicestudio.com/arcver)
*/
package com.arcticicestudio.icecore.hashids;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a hashid which holds all numbers and the encoded string.
 * <p>
 *   <strong>This class is immutable.</strong>
 * </p>
 *
 * @author Arctic Ice Studio &lt;development@arcticicestudio.com&gt;
 * @see <a href="http://hashids.org">Hashids</a>
 * @since 0.1.0
 */
public final class Hashid {

  private final long[] numbers;
  private final String hash;

  public static final Hashid EMPTY = new Hashid(new long[0], "");

  public Hashid(long[] longs, String hash) {
    this.numbers = longs;
    this.hash = hash;
  }

  /**
   * All numbers of this hashid.
   *
   * @return a new array with all numbers of this hashid
   */
  public long[] numbers() {
    long[] values = new long[numbers.length];
    System.arraycopy(numbers, 0, values, 0, numbers.length);
    return values;
  }

  /**
   * Returns the hash string of this hashid
   *
   * @return the hash string of this hashid
   */
  @Override
  public String toString() {
    return hash;
  }

  @Override
  public int hashCode() {
    int hashCode = 7;
    hashCode = 97 * hashCode + Arrays.hashCode(this.numbers);
    hashCode = 97 * hashCode + Objects.hashCode(this.hash);
    return hashCode;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Hashid other = (Hashid) obj;
    return Arrays.equals(this.numbers, other.numbers) && Objects.equals(this.hash, other.hash);
  }
}
