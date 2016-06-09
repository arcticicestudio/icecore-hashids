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
  public void oneLongNumberWithoutSeparators() {
    Hashids hashids = new Hashids("salt", 0, Hashids.DEFAULT_ALPHABET, null);
    long number = 12_345L;
    String hash = hashids.encodeToString(number);
    long[] decoded = hashids.decodeLongNumbers(hash);

    assertEquals(1, decoded.length);
    assertEquals(number, decoded[0]);
  }

  @Test
  public void oneLongNumberWithCustomSeparators() {
    Hashids hashids = new Hashids("salt", 0, Hashids.DEFAULT_ALPHABET, "abcdefgABCDEFG1234567");
    long number = 12_345L;
    String hash = hashids.encodeToString(number);
    long[] decoded = hashids.decodeLongNumbers(hash);

    assertEquals(1, decoded.length);
    assertEquals(number, decoded[0]);
  }

  @Test
  public void oneLongNumberWithLargeCustomMinimumHashLength() {
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
  public void oneLongNumberWithCustomUpperCaseAndNumbersAlphabet() {
    Hashids hashids = new Hashids("salt", "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789");
    long number = 12_345L;
    String expected = "KQL1R";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(number));

    assertEquals(expected, hashids.encodeToString(number));
    assertEquals(decoded.length, 1);
    assertEquals(decoded[0], number);
  }

  @Test
  public void oneLongNumberWithCustomLowerCaseAndNumbersAlphabet() {
    Hashids hashids = new Hashids("salt", "abcdefghijklmnpqrstuvwxyz123456789");
    long number = 12_345L;
    String expected = "kzkvx";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(number));

    assertEquals(expected, hashids.encodeToString(number));
    assertEquals(1, decoded.length);
    assertEquals(number, decoded[0]);
  }

  @Test
  public void oneLongNumberWithCustomMinimumHashLengthAndCustomUpperCaseAndNumbersAlphabet() {
    Hashids hashids = new Hashids("salt", 8, "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789");
    long number = 12_345L;
    String expected = "5MKQL1RM";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(number));

    assertEquals(expected, hashids.encodeToString(number));
    assertEquals(decoded.length, 1);
    assertEquals(decoded[0], number);
  }

  @Test
  public void oneLongNumberWithCustomMinimumHashLengthAndCustomLowerCaseAndNumbersAlphabet() {
    Hashids hashids = new Hashids("salt", 8, "abcdefghijklmnpqrstuvwxyz123456789");
    long number = 12_345L;
    String expected = "gnkzkvxn";
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
  public void oneIntegerNumberWithoutSeparators() {
    Hashids hashids = new Hashids("salt", 0, Hashids.DEFAULT_ALPHABET, null);
    int number = 12_345;
    String hash = hashids.encodeToString(number);
    int[] decoded = hashids.decodeIntegerNumbers(hash);

    assertEquals(1, decoded.length);
    assertEquals(number, decoded[0]);
  }

  @Test
  public void oneIntegerNumberWithCustomSeparators() {
    Hashids hashids = new Hashids("salt", 0, Hashids.DEFAULT_ALPHABET, "abcdefgABCDEFG1234567");
    int number = 12_345;
    String hash = hashids.encodeToString(number);
    int[] decoded = hashids.decodeIntegerNumbers(hash);

    assertEquals(1, decoded.length);
    assertEquals(number, decoded[0]);
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
  public void oneIntegerNumberWithLargeCustomMinimumHashLength() {
    Hashids hashids = new Hashids("salt", 1000);
    int number = 12_345;
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
    int[] decoded = hashids.decodeIntegerNumbers(hashids.encodeToString(number));

    assertEquals(expected, hashids.encodeToString(number));
    assertEquals(1, decoded.length);
    assertEquals(number, decoded[0]);
  }

  @Test
  public void oneIntegerNumberWithCustomUpperCaseAndNumbersAlphabet() {
    Hashids hashids = new Hashids("salt", "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789");
    int number = 12_345;
    String expected = "KQL1R";
    int[] decoded = hashids.decodeIntegerNumbers(hashids.encodeToString(number));

    assertEquals(expected, hashids.encodeToString(number));
    assertEquals(decoded.length, 1);
    assertEquals(decoded[0], number);
  }

  @Test
  public void oneIntegerNumberWithCustomLowerCaseAndNumbersAlphabet() {
    Hashids hashids = new Hashids("salt", "abcdefghijklmnpqrstuvwxyz123456789");
    int number = 12_345;
    String expected = "kzkvx";
    int[] decoded = hashids.decodeIntegerNumbers(hashids.encodeToString(number));

    assertEquals(expected, hashids.encodeToString(number));
    assertEquals(decoded.length, 1);
    assertEquals(decoded[0], number);
  }

  @Test
  public void oneIntegerNumberWithCustomMinimumHashLengthAndCustomUpperCaseAndNumbersAlphabet() {
    Hashids hashids = new Hashids("salt", 8, "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789");
    int number = 12_345;
    String expected = "5MKQL1RM";
    int[] decoded = hashids.decodeIntegerNumbers(hashids.encodeToString(number));

    assertEquals(expected, hashids.encodeToString(number));
    assertEquals(decoded.length, 1);
    assertEquals(decoded[0], number);
  }

  @Test
  public void oneIntegerNumberWithCustomMinimumHashLengthAndCustomLowerCaseAndNumbersAlphabet() {
    Hashids hashids = new Hashids("salt", 8, "abcdefghijklmnpqrstuvwxyz123456789");
    int number = 12_345;
    String expected = "gnkzkvxn";
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
  public void severalLongNumbersWithoutSeparators() {
    Hashids hashids = new Hashids("salt", 0, Hashids.DEFAULT_ALPHABET, null);
    long[] number = {1L, 2L, 3L, 4L, 5L};
    String hash = hashids.encodeToString(number);
    long[] decoded = hashids.decodeLongNumbers(hash);

    assertEquals(5, decoded.length);
    assertArrayEquals(number, decoded);
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
  public void severalLongNumberWithLargeCustomMinimumHashLength() {
    Hashids hashids = new Hashids("salt", 1000);
    long[] numbers = {683L, 94_108L, 123L, 5L};
    String expected =
      "OK1VzBwj7aW6rJnwzX1yQeL0pNZ45dBwLreag6JP7RqjnEx09Nd41yD5L9EGXMBV0b6W4Rq7w2NDejWVO9oB4vaNAd1YbxGnwwre7ORAKD09W" +
      "JXj4MBny15Jdr524W7ZgDlbPzQNY6OwLrzjDypL6JQbqP9aBnXgwAORYOEA1leMjJnpwNDQLyG90vwq9zEMgDaOo57KYbNnAdGLg6DKbVxGzM" +
        "AQBE2vwdYplMr9bGRlgv2KoO45jw1NJLAEezGDWP0J95M71yZpVKnqbe9YbyE6gBwODQJnd5WvXK71nAEX4MpeBgv5dGQYwRDLWpNRGd97J" +
        "OeLBW6q5PlnojA9yNqnPGjRWzvKe4bV5YQ2aXwZArzoK9G4avRLyYe5q1BGzKNqDL0Y7d41OZ6oVlgr9dZQvV5RnpoNLPWwzDj6MgYrv9Aa" +
        "PMylJ4EBdb0O25QNzQ9zJyEgMr07apGlXBjDPA6NjK1RJQGEL0zgaAV5bq7P231eMToyKzsRAfO3Y9leBp6dWyZO4woxvrXDnMbVR4exL1Y" +
        "Wdvq5wZn2ONKopGjgLZoD7Ye1VnRWqX6xwKGxab4OKlyEq0A9271JrXBeEWb5XwaARBQy2xJvjnPepMQJjWMNxOlbd0gpEPVD672nlAd7DO" +
        "opZLr6XBJxE0w1gMg0Dy4ZrYEvKa2Qbzx1XwMVo2KalOj7ybV06qxPr9zNZJxpjrV4aPMqAlLZ0zGo21RNvY6LQxgXrlj2BROdNwoa4Ade0" +
        "6PW7QVpEaYzxXBDqnZye5R1a0qPOy49XojNZnr7WJpR2BQlrZL0VxJ6ejXWPy417ovV6WzdP42KaZX5xrqgbBdKEoN1VxYRe7ZMvl42GW50" +
        "MEaBvX1RqVA9K0yjxpnGeoavVZxpqEP6gbl2zQNdYoGLgRlr7ZXypLzPE6q02K5QJMJAelnYOKxzrgZPodvQa1jp2yWOoDpXzAMQKYBVbZl" +
        "5GvoqjMRgD72PYlbG9EVvOxAK6AaxZEde";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(numbers));

    assertEquals(expected, hashids.encodeToString(numbers));
    assertEquals(decoded.length, numbers.length);
    assertArrayEquals(decoded, numbers);
  }

  @Test
  public void severalLongNumbersWithCustomUpperCaseAndNumbersAlphabet() {
    Hashids hashids = new Hashids("salt", "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789");
    long[] numbers = {683L, 94_108L, 123L, 5L};
    String expected = "3L4DTV52JC4KAJ";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(numbers));

    assertEquals(expected, hashids.encodeToString(numbers));
    assertEquals(decoded.length, numbers.length);
    assertArrayEquals(decoded, numbers);
  }

  @Test
  public void severalLongNumbersWithCustomLowerCaseAndNumbersAlphabet() {
    Hashids hashids = new Hashids("salt", "abcdefghijklmnpqrstuvwxyz123456789");
    long[] numbers = {683L, 94_108L, 123L, 5L};
    String expected = "38wdt3wjqf65t2";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(numbers));

    assertEquals(expected, hashids.encodeToString(numbers));
    assertEquals(decoded.length, numbers.length);
    assertArrayEquals(decoded, numbers);
  }

  @Test
  public void severalLongNumbersWithCustomMinimumHashLengthAndCustomUpperCaseAndNumbersAlphabet() {
    Hashids hashids = new Hashids("salt", 8, "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789");
    long[] numbers = {683L, 94_108L, 123L, 5L};
    String expected = "3L4DTV52JC4KAJ";
    long[] decoded = hashids.decodeLongNumbers(hashids.encodeToString(numbers));

    assertEquals(expected, hashids.encodeToString(numbers));
    assertEquals(decoded.length, numbers.length);
    assertArrayEquals(decoded, numbers);
  }

  @Test
  public void severalLongNumbersWithCustomMinimumHashLengthAndCustomLowerCaseAndNumbersAlphabet() {
    Hashids hashids = new Hashids("salt", 8, "abcdefghijklmnpqrstuvwxyz123456789");
    long[] numbers = {683L, 94_108L, 123L, 5L};
    String expected = "38wdt3wjqf65t2";
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
  public void severalIntegerNumbersWithoutSeparators() {
    Hashids hashids = new Hashids("salt", 0, Hashids.DEFAULT_ALPHABET, null);
    int[] number = {1, 2, 3, 4, 5};
    String hash = hashids.encodeToString(number);
    int[] decoded = hashids.decodeIntegerNumbers(hash);

    assertEquals(5, decoded.length);
    assertArrayEquals(number, decoded);
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
  public void severalIntegerNumbersWithLargeCustomMinimumHashLength() {
    Hashids hashids = new Hashids("salt", 1000);
    int[] numbers = {683, 94_108, 123, 5};
    String expected =
      "OK1VzBwj7aW6rJnwzX1yQeL0pNZ45dBwLreag6JP7RqjnEx09Nd41yD5L9EGXMBV0b6W4Rq7w2NDejWVO9oB4vaNAd1YbxGnwwre7ORAKD09W" +
        "JXj4MBny15Jdr524W7ZgDlbPzQNY6OwLrzjDypL6JQbqP9aBnXgwAORYOEA1leMjJnpwNDQLyG90vwq9zEMgDaOo57KYbNnAdGLg6DKbVxGzM" +
        "AQBE2vwdYplMr9bGRlgv2KoO45jw1NJLAEezGDWP0J95M71yZpVKnqbe9YbyE6gBwODQJnd5WvXK71nAEX4MpeBgv5dGQYwRDLWpNRGd97J" +
        "OeLBW6q5PlnojA9yNqnPGjRWzvKe4bV5YQ2aXwZArzoK9G4avRLyYe5q1BGzKNqDL0Y7d41OZ6oVlgr9dZQvV5RnpoNLPWwzDj6MgYrv9Aa" +
        "PMylJ4EBdb0O25QNzQ9zJyEgMr07apGlXBjDPA6NjK1RJQGEL0zgaAV5bq7P231eMToyKzsRAfO3Y9leBp6dWyZO4woxvrXDnMbVR4exL1Y" +
        "Wdvq5wZn2ONKopGjgLZoD7Ye1VnRWqX6xwKGxab4OKlyEq0A9271JrXBeEWb5XwaARBQy2xJvjnPepMQJjWMNxOlbd0gpEPVD672nlAd7DO" +
        "opZLr6XBJxE0w1gMg0Dy4ZrYEvKa2Qbzx1XwMVo2KalOj7ybV06qxPr9zNZJxpjrV4aPMqAlLZ0zGo21RNvY6LQxgXrlj2BROdNwoa4Ade0" +
        "6PW7QVpEaYzxXBDqnZye5R1a0qPOy49XojNZnr7WJpR2BQlrZL0VxJ6ejXWPy417ovV6WzdP42KaZX5xrqgbBdKEoN1VxYRe7ZMvl42GW50" +
        "MEaBvX1RqVA9K0yjxpnGeoavVZxpqEP6gbl2zQNdYoGLgRlr7ZXypLzPE6q02K5QJMJAelnYOKxzrgZPodvQa1jp2yWOoDpXzAMQKYBVbZl" +
        "5GvoqjMRgD72PYlbG9EVvOxAK6AaxZEde";
    int[] decoded = hashids.decodeIntegerNumbers(hashids.encodeToString(numbers));

    assertEquals(expected, hashids.encodeToString(numbers));
    assertEquals(decoded.length, numbers.length);
    assertArrayEquals(decoded, numbers);
  }

  @Test
  public void severalIntegerNumbersWithCustomUpperCaseAndNumbersAlphabet() {
    Hashids hashids = new Hashids("salt", "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789");
    int[] numbers = {683, 94_108, 123, 5};
    String expected = "3L4DTV52JC4KAJ";
    int[] decoded = hashids.decodeIntegerNumbers(hashids.encodeToString(numbers));

    assertEquals(expected, hashids.encodeToString(numbers));
    assertEquals(decoded.length, numbers.length);
    assertArrayEquals(decoded, numbers);
  }

  @Test
  public void severalIntegerNumbersWithCustomLowerCaseAndNumbersAlphabet() {
    Hashids hashids = new Hashids("salt", "abcdefghijklmnpqrstuvwxyz123456789");
    int[] numbers = {683, 94_108, 123, 5};
    String expected = "38wdt3wjqf65t2";
    int[] decoded = hashids.decodeIntegerNumbers(hashids.encodeToString(numbers));

    assertEquals(expected, hashids.encodeToString(numbers));
    assertEquals(decoded.length, numbers.length);
    assertArrayEquals(decoded, numbers);
  }

  @Test
  public void severalIntegerNumbersWithCustomMinimumHashLengthAndCustomUpperCaseAndNumbersAlphabet() {
    Hashids hashids = new Hashids("salt", 8, "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789");
    int[] numbers = {683, 94_108, 123, 5};
    String expected = "3L4DTV52JC4KAJ";
    int[] decoded = hashids.decodeIntegerNumbers(hashids.encodeToString(numbers));

    assertEquals(expected, hashids.encodeToString(numbers));
    assertEquals(decoded.length, numbers.length);
    assertArrayEquals(decoded, numbers);
  }

  @Test
  public void severalIntegerNumbersWithCustomMinimumHashLengthAndCustomLowerCaseAndNumbersAlphabet() {
    Hashids hashids = new Hashids("salt", 8, "abcdefghijklmnpqrstuvwxyz123456789");
    int[] numbers = {683, 94_108, 123, 5};
    String expected = "38wdt3wjqf65t2";
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

  @Test
  public void hexStringLowerCase() {
    Hashids hashids = new Hashids("salt");
    String hex = "75bcd15";
    String hash = hashids.encodeHex(hex);
    String returnedHex = hashids.decodeHex(hash);

    assertNotNull(hash);
    assertNotEquals(0, hash.length());
    assertEquals(hex, returnedHex);
  }

  @Test
  public void hexStringLowerCaseWithCustomMinimumHashLength() {
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
  public void hexStringUpperCase() {
    Hashids hashids = new Hashids("salt");
    String hex = "75BCD15";
    String hash = hashids.encodeHex(hex);
    String returnedHex = hashids.decodeHex(hash);

    assertNotNull(hash);
    assertNotEquals(0, hash.length());
    assertEquals(hex, returnedHex);
  }

  @Test
  public void hexStringUpperCaseWithCustomMinimumHashLength() {
    Hashids hashids = new Hashids("salt", 8);
    String hex = "75BCD15";
    String hash = hashids.encodeHex(hex);
    String returnedHex = hashids.decodeHex(hash);

    assertNotNull(hash);
    assertNotEquals(0, hash.length());
    assertEquals(hex, returnedHex);
    assertEquals(8, hash.length());
  }

  @Test
  public void longHexStringLowerCase() {
    Hashids hashids = new Hashids("salt");
    String hex = "f000000000000000000000000000000000000000000000000000000000000000000000000000000000000f";
    String hash = hashids.encodeHex(hex);
    String returnedHex = hashids.decodeHex(hash);

    assertNotNull(hash);
    assertNotEquals(0, hash.length());
    assertEquals(hex, returnedHex);
  }

  @Test
  public void longHexStringUpperCase() {
    Hashids hashids = new Hashids("salt");
    String hex = "F000000000000000000000000000000000000000000000000000000000000000000000000000000000000F";
    String hash = hashids.encodeHex(hex);
    String returnedHex = hashids.decodeHex(hash);

    assertNotNull(hash);
    assertNotEquals(0, hash.length());
    assertEquals(hex, returnedHex);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotSupportOneNegativeLongNumber() {
    Hashids hashids = new Hashids("salt");
    long number = -1L;
    hashids.encodeToString(number);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotSupportSeveralNegativeLongNumbers() {
    Hashids hashids = new Hashids("salt");
    long[] numbers = {-1L, -2L, -3L};
    hashids.encodeToString(numbers);
  }
}
