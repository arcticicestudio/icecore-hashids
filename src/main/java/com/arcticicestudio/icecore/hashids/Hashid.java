/*
+++++++++++++++++++++++++++++++++++++++++++
title     Hashid                          +
project   icecore-hashids                 +
file      Hashid.java                     +
version                                   +
author    Arctic Ice Studio               +
email     development@arcticicestudio.com +
website   http://arcticicestudio.com      +
copyright Copyright (C) 2016              +
created   2016-06-05 17:45 UTC+0200       +
modified  2016-06-05 17:46 UTC+0200       +
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

/**
 * Represents a hashid which holds all numbers and the encoded string.
 *
 * <p>
 *   This class is immutable.
 * </p>
 *
 * @author Arctic Ice Studio &lt;development@arcticicestudio.com&gt;
 * @see <a href="http://hashids.org">Hashids</a>
 * @since 0.1.0
 */
public final class Hashid {

  private final long[] numbers;
  private final String hash;

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
}
