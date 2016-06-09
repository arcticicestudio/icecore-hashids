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
modified  2016-06-09 20:20 UTC+0200       +
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

import org.junit.Test;

import static org.junit.Assert.*;

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
  public void oneNumber() {
    Hashids hashids = new Hashids("salt");
    long number = 12_345L;
    String expected = "X4j1";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(number));

    assertEquals(hashids.encodeToString(number), expected);
    assertEquals(decoded.length, 1);
    assertEquals(decoded[0], number);
  }

  @Test
  public void oneLargeNumber() {
    Hashids hashids = new Hashids("salt");
    long number = 9_007_199_254_740_991L;
    String expected = "wQpRRqRX24R";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(number));

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

  @Test(expected = IllegalArgumentException.class)
  public void oneLargeNumberNotSupported() throws Exception {
    long number = 9007199254740993L;
    Hashids a = new Hashids("salt");
    a.encode(number);
  }

  @Test
  public void oneNumberWithCustomMinimumHashLength() {
    Hashids hashids = new Hashids("salt", 8);
    long number = 12_345L;
    String expected = "xkX4j1kZ";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(number));

    assertEquals(hashids.encodeToString(number), expected);
    assertEquals(decoded.length, 1);
    assertEquals(decoded[0], number);
  }

  @Test
  public void oneNumberWithoutSeparators() {
    Hashids hashids = new Hashids("salt", 0, Hashids.DEFAULT_ALPHABET, null);
    long number = 12_345L;
    String hash = hashids.encodeToString(number);
    long[] decoded = hashids.decodeLongNumbers(hash);

    assertEquals(1, decoded.length);
    assertEquals(number, decoded[0]);
  }

  @Test
  public void oneNumberWithCustomSeparators() {
    Hashids hashids = new Hashids("salt", 0, Hashids.DEFAULT_ALPHABET, "abcdefgABCDEFG1234567");
    long number = 12_345L;
    String hash = hashids.encodeToString(number);
    long[] decoded = hashids.decodeLongNumbers(hash);

    assertEquals(1, decoded.length);
    assertEquals(number, decoded[0]);
  }

  @Test
  public void oneNumberWithLargeCustomMinimumHashLength() {
    Hashids hashids = new Hashids("salt", 1000);
    long number = 12_345;
    String expected = "lPaNoKpAjg2O9bdQ5K6l9Yvj0o7MWXwZPNyZ90jxQp7WYlA5gJ4LvwEMGnwDNBZjpxQY692dXoPOMyrVrgXDGE0BnaeZdM" +
      "YO1WxRlevqJK0BloZNbYG29dP1paDDw7gpRqBzjy5OedVZlEJnNjPQ4LXZye5REY0qA2wW1pOnJNjvMbLKg6rexwWDVyl74ZW74pVdEYQBa6zv" +
      "15eDbwlVqpzoDZ14AwlxbGL0RyE9P250JQ4egxYbwaoz17vXZENyrgxzn6V2pW5eA9vMPRdBoa7w5Vo9GMge1nPqQYJxlEZVyrxYKONAepM0lw" +
      "L42g7PdgK6V0xvdQlao1ANOG92LpYONBLEyYvqeZwnDA6WMaGb7jaQOVlPerdbJ7B1LzDyY59b4AJOeo6l9RpgGWVnzZqjNVDpraMYz2bQxdKv" +
      "y5PEwe7OP7WZDzeQANv9o6ga0jG2dDYj4xlKLZpa5qBbzMyGvrNp9nl4bLoy7E2dPB6AGYqzxkX4j1kZWaevwVKNR01OrMX5DgjJQAdPXJ9W16" +
      "OwQeR2go07VEnb1MqKxJ4YLr5XEVlwpnRyBqOlZG9Rg1AnN4o6XLBJ0jWP2yrvYX7QMB0a5LdxKwED1g0KMRpWnXv26oZx4GEqAwNdVg9PR25z" +
      "rJQjxKoXl104pzZeqEb7J5Pyw4BXWjRMnrD5RqWnoa1EGzX6JvDQbjBZ9NB6Av2bjd4LRy0WOXzrKDp0DlXKJZL7YNjOwQqbE1a4GpMLnj6RKy" +
      "PDrWVB9lOGdqAQenj5aYBM7KWdNr6JXOgv2M9XNL0oAxJ2RqrGOngPjKyq2oPpEzdA5XORGQZa91Y0BlNxz96Bba7oVDnMgdrJvGKaMYKo9bvQ" +
      "6r04xXL1GP2AWOWR7x46rMLgjXyQnVAEw5zL76Aboq49vK2pQz5jNJywPlGg1KAV0aLWzJ5bRevE74qBPoy6Xzbqre1RnKa2NdVODOVnBaDERz" +
      "AgJGx41qe2LrpdWGE4QbxLJez6";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(number));

    assertEquals(expected, hashids.encodeToString(number));
    assertEquals(1, decoded.length);
    assertEquals(number, decoded[0]);
  }

  @Test
  public void oneNumberWithCustomUpperCaseAndNumbersAlphabet() {
    Hashids hashids = new Hashids("salt", "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789");
    long number = 12_345L;
    String expected = "KQL1R";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(number));

    assertEquals(expected, hashids.encodeToString(number));
    assertEquals(decoded.length, 1);
    assertEquals(decoded[0], number);
  }

  @Test
  public void oneNumberWithCustomLowerCaseAndNumbersAlphabet() {
    Hashids hashids = new Hashids("salt", "abcdefghijklmnpqrstuvwxyz123456789");
    long number = 12_345L;
    String expected = "kzkvx";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(number));

    assertEquals(expected, hashids.encodeToString(number));
    assertEquals(1, decoded.length);
    assertEquals(number, decoded[0]);
  }

  @Test
  public void severalNumbers() {
    Hashids hashids = new Hashids("salt");
    long[] numbers = {683L, 94_108L, 123L, 5L};
    String expected = "1eMToyKzsRAfO";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(numbers));

    assertEquals(hashids.encodeToString(numbers), expected);
    assertEquals(decoded.length, numbers.length);
    assertArrayEquals(decoded, numbers);
  }

  @Test
  public void severalNumbersRandomness() {
    Hashids hashids = new Hashids("salt");
    long[] numbers = {5L, 5L, 5L, 5L};
    String expected = "YBF7FKFz";
    long[] decoded = hashids.decodeLongNumbers(expected);

    assertEquals(expected, hashids.encodeToString(numbers));
    assertEquals(numbers.length, decoded.length);
    assertArrayEquals(numbers, decoded);
  }

  @Test
  public void severalIncrementingNumbersRandomness() {
    Hashids hashids = new Hashids("salt");
    long[] numbers = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L};
    String expected = "rjiJulUECaFAS1TBhzcX";
    long[] decoded = hashids.decodeLongNumbers(expected);

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

  @Test
  public void hexString() {
    Hashids hashids = new Hashids("salt");
    String hex = "75bcd15";
    String hash = hashids.encodeHex(hex);
    String returnedHex = hashids.decodeHex(hash);

    assertNotNull(hash);
    assertNotEquals(0, hash.length());
    assertEquals(hex, returnedHex);
  }

  @Test
  public void hexStringWithCustomMinimumHashLength() {
    Hashids hashids = new Hashids("salt", 8);
    String hex = "75bcd15";
    String hash = hashids.encodeHex(hex);
    String returnedHex = hashids.decodeHex(hash);

    assertNotNull(hash);
    assertNotEquals(0, hash.length());
    assertEquals(hex, returnedHex);
    assertEquals(8, hash.length());
  }

  @Test
  public void longHexString() {
    Hashids hashids = new Hashids("salt");
    String hex = "f000000000000000000000000000000000000000000000000000000000000000000000000000000000000f";
    String hash = hashids.encodeHex(hex);
    String returnedHex = hashids.decodeHex(hash);

    assertNotNull(hash);
    assertNotEquals(0, hash.length());
    assertEquals(hex, returnedHex);
  }

  @Test(expected = IllegalArgumentException.class)
  public void negativeNumber() {
    Hashids hashids = new Hashids("salt");
    long number = -1L;
    hashids.encodeToString(number);
  }

  @Test
  public void hashidsBuilder() {
    Hashids hashids = new Hashids.Builder()
      .salt("salt")
      .minHashLength(16)
      .alphabet(Hashids.DEFAULT_ALPHABET)
      .separators(Hashids.DEFAULT_SEPARATORS)
      .build();
    long number = 12_345L;
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(number));

    assertEquals(1, decoded.length);
    assertEquals(number, decoded[0]);
  }
}
