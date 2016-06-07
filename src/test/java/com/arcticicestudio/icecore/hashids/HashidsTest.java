/*
+++++++++++++++++++++++++++++++++++++++++++
title     Hashids Public API Test         +
project   icecore-hashids                 +
file      HashidsTest.java                +
version                                   +
author    Arctic Ice Studio               +
email     development@arcticicestudio.com +
website   http://arcticicestudio.com      +
copyright Copyright (C) 2016              +
created   2016-06-06 21:05 UTC+0200       +
modified  2016-06-06 21:06 UTC+0200       +
+++++++++++++++++++++++++++++++++++++++++++

[Description]
Tests the "IceCore - Hashids" public API class "Hashids".

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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests the <a href="https://bitbucket.org/arcticicestudio/icecore-hashids">IceCore - Hashids</a>
 * public API class {@link Hashids}.
 *
 * @author Arctic Ice Studio &lt;development@arcticicestudio.com&gt;
 * @see <a href="https://bitbucket.org/arcticicestudio/icecore-hashids">IceCore - Hashids</a>
 * @see <a href="http://hashids.org">Hashids</a>
 * @since 0.1.0
 */
public class HashidsTest {

  @Test
  public void oneLongNumber() {
    Hashids hashids = new Hashids("salt");
    long number = 12_345L;
    String expected = "X4j1";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(number));

    assertEquals(hashids.encodeToString(number), expected);
    assertEquals(decoded.length, 1);
    assertEquals(decoded[0], number);
  }

  @Test
  public void oneIntegerNumber() {
    Hashids hashids = new Hashids("salt");
    int number = 12_345;
    String expected = "X4j1";
    int[] decoded = hashids.decodeIntegerNumbers(hashids.encodeToString(number));

    assertEquals(hashids.encodeToString(number), expected);
    assertEquals(decoded.length, 1);
    assertEquals(decoded[0], number);
  }

  @Test
  public void severalLongNumbers() {
    Hashids hashids = new Hashids("salt");
    long[] numbers = {683L, 94_108L, 123L, 5L};
    String expected = "1eMToyKzsRAfO";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(numbers));

    assertEquals(hashids.encodeToString(numbers), expected);
    assertEquals(decoded.length, numbers.length);
    assertArrayEquals(decoded, numbers);
  }
}
