/*
+++++++++++++++++++++++++++++++++++++++++++
title     Hashids Public API              +
project   icecore-hashids                 +
file      Hashids.java                    +
version                                   +
author    Arctic Ice Studio               +
email     development@arcticicestudio.com +
website   http://arcticicestudio.com      +
copyright Copyright (C) 2016              +
created   2016-06-05 19:58 UTC+0200       +
modified  2016-06-05 19:59 UTC+0200       +
+++++++++++++++++++++++++++++++++++++++++++

[Description]
Generates short, unique, non-sequential and decodable hashids from positive unsigned (long) integer numbers.
Serves as the entry point to the "IceCore - Hashids" public API.

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
 * Generates short, unique, non-sequential and decodable hashids from positive unsigned (long) integer numbers.
 *
 * <p>
 *   Serves as the entry point to the
 *   <a href="https://bitbucket.org/arcticicestudio/icecore-hashids">IceCore - Hashids</a> public API.
 * </p>
 *
 * <p>
 *   The Hashids's {@code salt} is used as a secret to generate unique strings using a given {@code alphabet}.
 *    Generated strings can have a {@code minHashLength}.
 * </p>
 *
 * <p>
 *   If used to obfuscates identities, make sure to not expose your {@code salt}, {@code alphabet} nor
 * {@code separators} to a client, client-side is not safe.
 * </p>
 *
 * <p>
 *   Only positive numbers are supported.
 *   All methods in this class will throw an {@link IllegalArgumentException} if a negative number is given.
 *   To use negative numbers prepend a hyphen character {@code -} to the hash string.
 *   <strong>
 *     Note that this method is limited to single number hashes only and breaks the official Hashids definition!
 *   </strong>
 *   Example:
 *   <pre>
 *     Hashids hashids = new Hashids("salt");
 *     long number = -1234567890;
 *     String enc = (Math.abs(number) != number ? "-" : "") + hashids.encodeToString(Math.abs(number));
 *     long dec = enc.startsWith("-") ? hashids.decodeLongs(enc.substring(1))[0] : hashids.decodeLongs(enc)[0];
 * </pre>
 * </p>
 *
 * <p>
 *   {@code Hashids} instances are thread-safe.
 * </p>
 *
 * @author Arctic Ice Studio &lt;development@arcticicestudio.com&gt;
 * @see <a href="https://bitbucket.org/arcticicestudio/icecore-hashids">IceCore - Hashids</a>
 * @see <a href="http://hashids.org">Hashids</a>
 * @since 0.1.0
 */
public final class Hashids {

  /**
   * Holds the default alphabet.
   *
   * <p>
   *   Value: {@code abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890}
   * </p>
   */
  public static final String DEFAULT_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

  /**
   * Holds the default separators.
   *
   * <p>
   *   Used to prevent the generation of strings that contain bad, offensive or rude words.
   * </p>
   *
   * <p>
   *   Value: {@code cfhistuCFHISTU}
   * </p>
   */
  public static final String DEFAULT_SEPARATORS = "cfhistuCFHISTU";

  /**
   * Holds the maximum number value.
   *
   * <p>
   *   This limit is mandatory in order to ensure interoperability.
   * </p>
   *
   * <p>
   *   JavaScript equivalents used in <a href="https://github.com/ivanakimov/hashids.js">hashids.js</a>:
   *   <ul>
   *     <li>{@code 9_007_199_254_740_991}</li>
   *     <li>{@code 2^53-1}</li>
   *     <li>{@code Number.MAX_VALUE-1}</li>
   *   </ul>
   * </p>
   * <p>
   *   Value: {@code 9_007_199_254_740_992L - 1}
   * </p>
   */
  public static final long MAX_NUMBER_VALUE = 9_007_199_254_740_992L - 1;

  private static final int GUARD_DIV = 12;
  private static final int MIN_ALPHABET_LENGTH = 16;
  private static final double SEP_DIV = 3.5;

  private final String alphabet;
  private final String guards;
  private final int minHashLength;
  private final String salt;
  private final String separators;

  public Hashids(String salt) {
    this(salt, 0);
  }

  public Hashids(String salt, int minHashLength) {
    this(salt, minHashLength, DEFAULT_ALPHABET);
  }

  public Hashids(String salt, String alphabet) {
    this(salt, 0, alphabet);
  }

  public Hashids(String salt, int minHashLength, String alphabet) {
    this(salt, minHashLength, alphabet, DEFAULT_SEPARATORS);
  }

  public Hashids(String salt, int minHashLength, String alphabet, String separators) {
    if (alphabet == null) {
      throw new IllegalArgumentException("alphabet was null");
    }
    if (alphabet.length() == 0) {
      throw new IllegalArgumentException("alphabet was empty");
    }

    this.salt = salt == null ? "" : salt;
    this.minHashLength = minHashLength < 0 ? 0 : minHashLength;

    String uniqueAlphabet = "";
    for (int idx = 0; idx < alphabet.length(); idx++) {
      if (!uniqueAlphabet.contains("" + alphabet.charAt(idx))) {
        uniqueAlphabet += "" + alphabet.charAt(idx);
      }
    }
    alphabet = uniqueAlphabet;

    if (alphabet.length() < MIN_ALPHABET_LENGTH) {
      throw new IllegalArgumentException(
        "Alphabet must contain at least " + MIN_ALPHABET_LENGTH + " unique characters"
      );
    }

    if (alphabet.contains(" ")) {
      throw new IllegalArgumentException("Alphabet cannot contains spaces");
    }

    /*
     * Separators should contain only characters present in alphabet.
     * Alphabet should not contain separators.
     */
    String seps = separators == null ? "" : separators;
    for (int sepIdx = 0; sepIdx < seps.length(); sepIdx++) {
      int alphaIdx = alphabet.indexOf(seps.charAt(sepIdx));
      if (alphaIdx == -1) {
        seps = seps.substring(0, sepIdx) + " " + seps.substring(sepIdx + 1);
      } else {
        alphabet = alphabet.substring(0, alphaIdx) + " " + alphabet.substring(alphaIdx + 1);
      }
    }

    alphabet = alphabet.replaceAll("\\s+", "");
    seps = seps.replaceAll("\\s+", "");
    seps = consistentShuffle(seps, this.salt);


    if (seps == null || seps.length() == 0 || (alphabet.length() / seps.length()) > SEP_DIV) {
      int sepsLen = (int) Math.ceil(alphabet.length() / SEP_DIV);
      if (sepsLen == 1) {
        sepsLen++;
      }
      if (sepsLen > seps.length()) {
        int diff = sepsLen - seps.length();
        seps += alphabet.substring(0, diff);
        alphabet = alphabet.substring(diff);
      } else {
        seps = seps.substring(0, sepsLen);
      }
    }

    alphabet = consistentShuffle(alphabet, this.salt);
    int guardCount = (int) Math.ceil(alphabet.length() / GUARD_DIV);

    if (alphabet.length() < 3) {
      guards = seps.substring(0, guardCount);
      seps = seps.substring(guardCount);
    } else {
      guards = alphabet.substring(0, guardCount);
      alphabet = alphabet.substring(guardCount);
    }

    this.alphabet = alphabet;
    this.separators = seps;
  }

  private Hashid doEncode(long... numbers) {
    int numberHashInt = 0;
    for (int idx = 0; idx < numbers.length; idx++) {
      if (numbers[idx] < 0 || numbers[idx] > MAX_NUMBER_VALUE) {
        throw new IllegalArgumentException("Number out of range");
      }
      numberHashInt += numbers[idx] % (idx + 100);
    }
    String decodeAlphabet = alphabet;
    final char lottery = decodeAlphabet.toCharArray()[numberHashInt % decodeAlphabet.length()];

    String result = lottery + "";

    String buffer;
    int sepsIdx, guardIdx;
    for (int idx = 0; idx < numbers.length; idx++) {
      long num = numbers[idx];
      buffer = lottery + salt + decodeAlphabet;

      decodeAlphabet = consistentShuffle(decodeAlphabet, buffer.substring(0, decodeAlphabet.length()));
      final String last = hash(num, decodeAlphabet);

      result += last;

      if (idx + 1 < numbers.length) {
        num %= ((int) last.toCharArray()[0] + idx);
        sepsIdx = (int) (num % separators.length());
        result += separators.toCharArray()[sepsIdx];
      }
    }

    if (result.length() < minHashLength) {
      guardIdx = (numberHashInt + (int) (result.toCharArray()[0])) % guards.length();
      char guard = guards.toCharArray()[guardIdx];

      result = guard + result;

      if (result.length() < minHashLength) {
        guardIdx = (numberHashInt + (int) (result.toCharArray()[2])) % guards.length();
        guard = guards.toCharArray()[guardIdx];

        result += guard;
      }
    }

    final int halfLen = decodeAlphabet.length() / 2;
    while (result.length() < minHashLength) {
      decodeAlphabet = consistentShuffle(decodeAlphabet, decodeAlphabet);
      result = decodeAlphabet.substring(halfLen) + result + decodeAlphabet.substring(0, halfLen);
      final int excess = result.length() - minHashLength;
      if (excess > 0) {
        int startPos = excess / 2;
        result = result.substring(startPos, startPos + minHashLength);
      }
    }

    return new Hashid(numbers, result);
  }

  private String consistentShuffle(String alphabet, String salt) {
    if (salt.length() <= 0) {
      return alphabet;
    }
    final char[] saltChars = salt.toCharArray();
    int ascVal, j;
    char tmp;
    for (int idx = alphabet.length() - 1, v = 0, p = 0; idx > 0; idx--, v++) {
      v %= salt.length();
      ascVal = (int) saltChars[v];
      p += ascVal;
      j = (ascVal + v + p) % idx;

      tmp = alphabet.charAt(j);
      alphabet = alphabet.substring(0, j) + alphabet.charAt(idx) + alphabet.substring(j + 1);
      alphabet = alphabet.substring(0, idx) + tmp + alphabet.substring(idx + 1);
    }
    return alphabet;
  }

  private String hash(long input, String alphabet) {
    String hash = "";
    final int alphabetLen = alphabet.length();
    final char[] alphabetChars = alphabet.toCharArray();
    do {
      hash = alphabetChars[(int) (input % alphabetLen)] + hash;
      input /= alphabetLen;
    }
    while (input > 0);
    return hash;
  }

  private Long unhash(String input, String alphabet) {
    long number = 0;
    long pos;
    final char[] inputChars = input.toCharArray();
    for (int idx = 0; idx < input.length(); idx++) {
      pos = alphabet.indexOf(inputChars[idx]);
      number += pos * Math.pow(alphabet.length(), input.length() - idx - 1);
    }
    return number;
  }
}
