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
modified  2016-06-05 22:48 UTC+0200       +
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    if (isEmpty(seps) || ((float)alphabet.length() / seps.length()) > SEP_DIV) {
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
    int guardCount = (int) Math.ceil((double)alphabet.length() / GUARD_DIV);

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

  /**
   * An immutable and reusable {@link Hashids} builder.
   *
   * <p>
   *   Each method returns a new builder instance.
   * </p>
   *
   * <p>
   *   Defaults are
   *   <ul>
   *     <li>no salt</li>
   *     <li>{@link #DEFAULT_ALPHABET} ({@value #DEFAULT_ALPHABET})</li>
   *     <li>no minimum hash length</li>
   *     <li>{@link #DEFAULT_SEPARATORS} ({@value #DEFAULT_SEPARATORS})</li>
   *   </ul>
   * </p>
   */
  public static final class Builder {

    private final String salt;
    private final String alphabet;
    private final String separators;
    private final int minHashLength;

    /**
     * Create a new {@link Hashids} builder.
     */
    public Builder() {
      this.salt = "";
      this.alphabet = DEFAULT_ALPHABET;
      this.separators = DEFAULT_SEPARATORS;
      this.minHashLength = 0;
    }

    private Builder(String salt, String alphabet, String separators, int minHashLength) {
      this.salt = salt;
      this.alphabet = alphabet;
      this.separators = separators;
      this.minHashLength = minHashLength;
    }

    /**
     * Sets the salt string.
     *
     * @param salt The string to use as salt
     * @return The builder instance with the specified salt
     */
    public Builder salt(String salt) {
      return new Builder(salt, alphabet, separators, minHashLength);
    }

    /**
     * Sets the custom alphabet string.
     *
     * @param alphabet The string to use as custom alphabet
     * @return The builder instance with the specified custom alphabet
     */
    public Builder alphabet(String alphabet) {
      return new Builder(salt, alphabet, separators, minHashLength);
    }

    /**
     * Sets the custom separators string.
     *
     * @param separators The string to use as custom alphabet
     * @return The builder instance with the specified custom separators
     */
    public Builder separators(String separators) {
      return new Builder(salt, alphabet, separators, minHashLength);
    }

    /**
     * Sets the minimum hash length.
     *
     * @param minHashLength The minimum length of the hash
     * @return The builder instance with the minimum hash length
     */
    public Builder minHashLength(int minHashLength) {
      return new Builder(salt, alphabet, separators, minHashLength);
    }

    /**
     * Builds the {@link Hashids}.
     *
     * @return The {@link Hashids} instance
     */
    public Hashids build() {
      return new Hashids(salt, minHashLength, alphabet, separators);
    }
  }

  /**
   * Encode number(s).
   *
   * @param numbers The number(s) to encode
   * @return The {@link Hashid} instance with the number(s) and the encoded string
   */
  public Hashid encode(long... numbers) {
    if (numbers.length == 0) {
      return Hashid.EMPTY;
    }
    return doEncode(numbers);
  }

  /**
   * Encode number(s) to string.
   *
   * @param numbers The number(s) to encode
   * @return The encoded string
   */
  public String encodeToString(long... numbers) {
    if (numbers.length == 0) {
      return "";
    }
    return encode(numbers).toString();
  }

  /**
   * Encode number(s) to string.
   *
   * @param numbers The number(s) to encode
   * @return The encoded string
   */
  public String encodeToString(int... numbers) {
    if (numbers.length == 0) {
      return "";
    }
    long[] longs = new long[numbers.length];
    for (int idx = 0; idx < numbers.length; idx++) {
      longs[idx] = numbers[idx];

    }
    return doEncode(longs).toString();
  }

  /**
   * Encode an hexadecimal string to string.
   *
   * @param hex The hexadecimal string to encode
   * @return The encoded string
   */
  public String encodeHex(String hex) {
    if (!hex.matches("^[0-9a-fA-F]+$")) {
      throw new IllegalArgumentException(String.format("%s is not a hex string", hex));
    }
    Matcher matcher = Pattern.compile("[\\w\\W]{1,12}").matcher(hex);
    List<Long> matched = new ArrayList<>();
    while (matcher.find()) {
      matched.add(Long.parseLong("1" + matcher.group(), 16));
    }
    return doEncode(toArray(matched)).toString();
  }

  /**
   * Decode an encoded string.
   *
   * @param hash The encoded string
   * @return The {@link Hashid} instance with the decoded hash and decoded number(s)
   */
  public Hashid decode(String hash) {
    if (isEmpty(hash)) {
      return Hashid.EMPTY;
    }
    return doDecode(hash, alphabet);
  }

  /**
   * Decode an encoded string to long numbers.
   *
   * @param hash The encoded string
   * @return The decoded long numbers
   */
  public long[] decodeLongNumbers(String hash) {
    if (isEmpty(hash)) {
      return new long[0];
    }
    return doDecode(hash, alphabet).numbers();
  }

  /**
   * Decode an encoded string to integer numbers.
   *
   * @param hash The encoded string
   * @return The decoded integer numbers
   *
   * @throws IllegalArgumentException if decoded number is out of integer range, shouldn't you be using longs instead?
   */
  public int[] decodeIntegerNumbers(String hash) {
    if (isEmpty(hash)) {
      return new int[0];
    }
    long[] numbers = doDecode(hash, alphabet).numbers();
    int[] ints = new int[numbers.length];
    for (int idx = 0; idx < numbers.length; idx++) {
      long number = numbers[idx];
      if (number < Integer.MIN_VALUE || number > Integer.MAX_VALUE) {
        throw new IllegalArgumentException("Number out of range");
      }
      ints[idx] = (int) number;
    }
    return ints;
  }

  /**
   * Decode an string to hexadecimal numbers.
   *
   * @param hash The encoded string
   * @return The decoded hexadecimal numbers string
   */
  public String decodeHex(String hash) {
    StringBuilder sb = new StringBuilder();
    long[] numbers = decodeLongNumbers(hash);
    for (long number : numbers) {
      sb.append(Long.toHexString(number).substring(1));
    }
    return sb.toString();
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

  private Hashid doDecode(String hash, String alphabet) {
    int idx = 0;
    String[] hashArray = hash.replaceAll("[" + guards + "]", " ").split(" ");
    if (hashArray.length == 3 || hashArray.length == 2) {
      idx = 1;
    }
    String hashBreakdown = hashArray[idx];

    final char lottery = hashBreakdown.toCharArray()[0];
    hashBreakdown = hashBreakdown.substring(1);
    hashBreakdown = hashBreakdown.replaceAll("[" + separators + "]", " ");
    hashArray = hashBreakdown.split(" ");

    final List<Long> result = new ArrayList<>();

    String buffer;
    for (String subHash : hashArray) {
      buffer = lottery + salt + alphabet;
      alphabet = consistentShuffle(alphabet, buffer.substring(0, alphabet.length()));
      result.add(unhash(subHash, alphabet));
    }
    long[] resultArray = toArray(result);
    if (!doEncode(resultArray).toString().equals(hash)) {
      throw new IllegalArgumentException(String.format("%s is not a valid hashid", hash));
    }
    return new Hashid(resultArray, hash);
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

  private long[] toArray(List<Long> longs) {
    final long[] result = new long[longs.size()];
    int idx = 0;
    for (Long aLong : longs) {
      result[idx++] = aLong;
    }
    return result;
  }

  /**
   * Check if a string is {@code null} or empty.
   *
   * @param value The string to check
   * @return {code true} if the string is {@code null} or empty, {@code false }otherwise
   */
  private boolean isEmpty(String value) {
    return value == null || value.length() == 0;
  }
}
