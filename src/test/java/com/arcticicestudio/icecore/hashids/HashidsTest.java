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
  public void oneLargeLongNumber() {
    Hashids hashids = new Hashids("salt");
    long number = 9_007_199_254_740_991L;
    String expected = "wQpRRqRX24R";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(number));

    assertEquals(hashids.encodeToString(number), expected);
    assertEquals(decoded.length, 1);
    assertEquals(decoded[0], number);
  }

  @Test(expected = IllegalArgumentException.class)
  public void oneLargeLongNumberNotSupported() throws Exception {
    long number = 9007199254740993L;
    Hashids a = new Hashids("salt");
    a.encode(number);
  }

  @Test
  public void oneLongNumberWithCustomMinimumHashLength() {
    Hashids hashids = new Hashids("salt", 8);
    long number = 12_345L;
    String expected = "xkX4j1kZ";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(number));

    assertEquals(hashids.encodeToString(number), expected);
    assertEquals(decoded.length, 1);
    assertEquals(decoded[0], number);
  }

  @Test
  public void oneLongNumberWithCustomAlphabet() {
    Hashids hashids = new Hashids("salt", "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789");
    long number = 12_345L;
    String expected = "KQL1R";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(number));

    assertEquals(expected, hashids.encodeToString(number));
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
  public void oneLargeIntegerNumber() {
    Hashids hashids = new Hashids("salt");
    int number = 2147483647;
    String expected = "wqVYY1X";
    int[] decoded = hashids.decodeIntegerNumbers(hashids.encodeToString(number));

    assertEquals(hashids.encodeToString(number), expected);
    assertEquals(decoded.length, 1);
    assertEquals(decoded[0], number);
  }

  @Test
  public void oneIntegerNumberWithCustomMinimumHashLength() {
    Hashids hashids = new Hashids("salt", 8);
    int number = 12345;
    String expected = "xkX4j1kZ";
    int[] decoded = hashids.decodeIntegerNumbers(hashids.encodeToString(number));

    assertEquals(hashids.encodeToString(number), expected);
    assertEquals(decoded.length, 1);
    assertEquals(decoded[0], number);
  }

  @Test
  public void oneIntegerNumberWithCustomAlphabet() {
    Hashids hashids = new Hashids("salt", "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789");
    int number = 12_345;
    String expected = "KQL1R";
    int[] decoded = hashids.decodeIntegerNumbers(hashids.encodeToString(number));

    assertEquals(expected, hashids.encodeToString(number));
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

  @Test
  public void severalLongNumberWithCustomMinimumHashLength() {
    Hashids hashids = new Hashids("salt", 8);
    long[] numbers = {683L, 94_108L, 123L, 5L};
    String expected = "1eMToyKzsRAfO";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(numbers));

    assertEquals(expected, hashids.encodeToString(numbers));
    assertEquals(decoded.length, numbers.length);
    assertArrayEquals(decoded, numbers);
  }

  @Test
  public void severalLongNumbersWithCustomAlphabet() {
    Hashids hashids = new Hashids("salt", "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789");
    long[] numbers = {683L, 94_108L, 123L, 5L};
    String expected = "3L4DTV52JC4KAJ";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(numbers));

    assertEquals(expected, hashids.encodeToString(numbers));
    assertEquals(decoded.length, numbers.length);
    assertArrayEquals(decoded, numbers);
  }

  @Test
  public void severalIntegerNumbers() {
    Hashids hashids = new Hashids("salt");
    int[] numbers = {683, 94_108, 123, 5};
    String expected = "1eMToyKzsRAfO";
    int[] decoded = hashids.decodeIntegerNumbers(hashids.encodeToString(numbers));

    assertEquals(hashids.encodeToString(numbers), expected);
    assertEquals(decoded.length, numbers.length);
    assertArrayEquals(decoded, numbers);
  }

  @Test
  public void severalIntegerNumbersWithCustomMinimumHashLength() {
    Hashids hashids = new Hashids("salt", 8);
    int[] numbers = {683, 94_108, 123, 5};
    String expected = "1eMToyKzsRAfO";
    int[] decoded = hashids.decodeIntegerNumbers(hashids.encodeToString(numbers));

    assertEquals(expected, hashids.encodeToString(numbers));
    assertEquals(decoded.length, numbers.length);
    assertArrayEquals(decoded, numbers);
  }

  @Test
  public void severalIntegerNumbersWithCustomAlphabet() {
    Hashids hashids = new Hashids("salt", "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789");
    int[] numbers = {683, 94_108, 123, 5};
    String expected = "3L4DTV52JC4KAJ";
    int[] decoded = hashids.decodeIntegerNumbers(hashids.encodeToString(numbers));

    assertEquals(expected, hashids.encodeToString(numbers));
    assertEquals(decoded.length, numbers.length);
    assertArrayEquals(decoded, numbers);
  }

  @Test
  public void longNumbersRandomness() {
    Hashids hashids = new Hashids("salt");
    long[] numbers = {5L, 5L, 5L, 5L};
    String expected = "YBF7FKFz";
    long[] decoded = hashids.decodeLongNumbers(expected);

    assertEquals(expected, hashids.encodeToString(numbers));
    assertEquals(numbers.length, decoded.length);
    assertArrayEquals(numbers, decoded);
  }

  @Test
  public void integerNumbersRandomness() {
    Hashids hashids = new Hashids("salt");
    int[] numbers = {5, 5, 5, 5};
    String expected = "YBF7FKFz";
    int[] decoded = hashids.decodeIntegerNumbers(expected);

    assertEquals(expected, hashids.encodeToString(numbers));
    assertEquals(numbers.length, decoded.length);
    assertArrayEquals(numbers, decoded);
  }

  @Test
  public void longIncrementingNumbersRandomness() {
    Hashids hashids = new Hashids("salt");
    long[] numbers = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L};
    String expected = "rjiJulUECaFAS1TBhzcX";
    long[] decoded = hashids.decodeLongNumbers(expected);

    assertEquals(expected, hashids.encodeToString(numbers));
    assertEquals(numbers.length, decoded.length);
    assertArrayEquals(numbers, decoded);
  }

  @Test
  public void integerIncrementingNumbersRandomness() {
    Hashids hashids = new Hashids("salt");
    int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    String expected = "rjiJulUECaFAS1TBhzcX";
    int[] decoded = hashids.decodeIntegerNumbers(expected);

    assertEquals(expected, hashids.encodeToString(numbers));
    assertEquals(numbers.length, decoded.length);
    assertArrayEquals(numbers, decoded);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidSalt() {
    Hashids hashidsA = new Hashids("salt and pepper");
    Hashids hashidsB = new Hashids("salt");
    long number = 12_345L;
    hashidsA.decodeLongNumbers(hashidsB.encodeToString(number));
  }
}
