/*
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
title      Hashid                                             +
project    icecore-hashids                                    +
repository https://github.com/arcticicestudio/icecore-hashids +
author     Arctic Ice Studio                                  +
email      development@arcticicestudio.com                    +
copyright  Copyright (C) 2016                                 +
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
*/
package com.arcticicestudio.icecore.hashids;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a hashid holding all numbers and the encoded string.
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

  /**
   * A empty hashid with no numbers and no hash.
   */
  public static final Hashid EMPTY = new Hashid(new long[0], "");

  /**
   * Constructs a new hashid with the specified numbers and hash.
   *
   * @param longs the numbers of this hashid
   * @param hash the hash of this hashid
   */
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
   * Returns the hash of this hashid.
   *
   * @return the hash of this hashid
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
