package com.arcticicestudio.icecore.hashids;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generates short, unique, non-sequential and decodable hashids from positive unsigned (long) integer numbers.
 * <p>
 *   Serves as the entry point to the
 *   <a href="https://github.com/arcticicestudio/icecore-hashids">IceCore Hashids</a> public API.
 * </p>
 * <p>
 *   The Hashids's {@code salt} is used as a secret to generate unique strings using a given {@code alphabet}.
 *    Generated strings can have a {@code minHashLength}.
 * </p>
 * <p>
 *   If used to obfuscate identities make sure to not expose your {@code salt} or {@code alphabet}.
 * </p>
 * <p>
 *   Only positive numbers are supported.
 *   All methods in this class will throw an {@link IllegalArgumentException} if a negative number is given.
 *   To use negative numbers prepend a hyphen character to the hash string.
 *   <strong>
 *     Note that this method is limited to single number hashes only and breaks the official Hashids definition!
 *   </strong>
 *   Example:
 *   <pre>
 *     Hashids hashids = new Hashids("salt");
 *     long number = -1234567890;
 *     String enc = (Math.abs(number) != number ? "-" : "") + hashids.encode(Math.abs(number));
 *     long dec = enc.startsWith("-") ? hashids.decode(enc.substring(1))[0] : hashids.decode(enc)[0];
 *   </pre>
 *
 * <p>
 *   <strong>{@code Hashids} instances are thread-safe.</strong>
 * </p>
 *
 * @author Arctic Ice Studio &lt;development@arcticicestudio.com&gt;
 * @see <a href="https://github.com/arcticicestudio/icecore-hashids">IceCore Hashids</a>
 * @see <a href="http://hashids.org">Hashids</a>
 * @version 0.3.0
 */
public final class Hashids {

  /**
   * Holds the default alphabet.
   */
  public static final String DEFAULT_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

  /**
   * The default separators.
   * <p>
   *   Used to prevent the generation of strings that contain bad, offensive and rude words.
   * </p>
   */
  private static final String DEFAULT_SEPARATORS = "cfhistuCFHISTU";

  /**
   * Holds the maximum number value.
   * <p>
   *   This limit is mandatory in order to ensure interoperability.
   * </p>
   * <p>
   *   JavaScript equivalents used in <a href="https://github.com/ivanakimov/hashids.js">hashids.js</a>:
   *   <ul>
   *     <li>{@code 9_007_199_254_740_991}</li>
   *     <li>{@code 2^53-1}</li>
   *     <li>{@code Number.MAX_VALUE-1}</li>
   *   </ul>
   */
  public static final long MAX_NUMBER_VALUE = 9_007_199_254_740_992L - 1;

  /**
   * The version of the public API.
   *
   * @since 0.3.0
   */
  public static final String VERSION = "0.3.0";

  private static final int GUARD_DIV = 12;
  private static final int MIN_ALPHABET_LENGTH = 16;
  private static final double SEP_DIV = 3.5;
  private static final Pattern PATTERN_ENCODE_HEX = Pattern.compile("^[0-9a-fA-F]+$");
  private static final Pattern PATTERN_ALPHABET_REPLACE = Pattern.compile("\\s+");

  private final String alphabet;
  private final String guards;
  private final int minHashLength;
  private final String salt;

  /**
   * Constructs a new instance without a salt, no minimum hash length and the {@link #DEFAULT_ALPHABET default alphabet}.
   */
  public Hashids() {
    this("", 0, DEFAULT_ALPHABET);
  }

  /**
   * Constructs a new instance with the given salt, the minimum hash length and alphabet.
   *
   * @param salt the salt to be used as entropy
   * @param minLength the minimum hash length
   * @param alphabet the alphabet to be used for the hash generation
   */
  private Hashids(String salt, int minLength, String alphabet) {
    if (alphabet == null) {
      throw new IllegalArgumentException("alphabet was null");
    }
    if (alphabet.length() == 0) {
      throw new IllegalArgumentException("alphabet was empty");
    }

    this.salt = salt == null ? "" : salt;
    this.minHashLength = minLength < 0 ? 0 : minLength;

    StringBuilder uniqueAlphabet = new StringBuilder();
    for (int idx = 0; idx < alphabet.length(); idx++) {
      if (uniqueAlphabet.indexOf(String.valueOf(alphabet.charAt(idx))) == -1) {
        uniqueAlphabet.append(alphabet.charAt(idx));
      }
    }

    if (uniqueAlphabet.length() < MIN_ALPHABET_LENGTH) {
      throw new IllegalArgumentException(
        "Alphabet must contain at least " + MIN_ALPHABET_LENGTH + " unique characters"
      );
    }

    if (uniqueAlphabet.toString().contains(" ")) {
      throw new IllegalArgumentException("Alphabet cannot contains spaces");
    }

    // Alphabet should not contain separators.
    StringBuilder seps = new StringBuilder(DEFAULT_SEPARATORS);
    for (int sepIdx = 0; sepIdx < seps.length(); sepIdx++) {
      int alphaIdx = uniqueAlphabet.indexOf(String.valueOf(seps.charAt(sepIdx)));
      if (alphaIdx == -1) {
        seps.replace(sepIdx, sepIdx + 1, " ");
      } else {
        uniqueAlphabet.replace(alphaIdx, alphaIdx + 1, " ");
      }
    }

    uniqueAlphabet.replace(0, uniqueAlphabet.length(), PATTERN_ALPHABET_REPLACE.matcher(uniqueAlphabet).replaceAll(""));
    seps.replace(0, seps.length(), PATTERN_ALPHABET_REPLACE.matcher(seps).replaceAll(""));
    seps.replace(0, seps.length(), consistentShuffle(seps.toString(), this.salt));

    if (isEmpty(seps.toString()) || ((float)uniqueAlphabet.length() / seps.length()) > SEP_DIV) {
      int sepsLen = (int) Math.ceil(uniqueAlphabet.length() / SEP_DIV);
      if (sepsLen == 1) {
        sepsLen++;
      }
      if (sepsLen > seps.length()) {
        int diff = sepsLen - seps.length();
        seps.append(uniqueAlphabet.substring(0, diff));
        uniqueAlphabet.replace(0, uniqueAlphabet.length(), uniqueAlphabet.substring(diff));
      } else {
        seps.replace(0, seps.length(), seps.substring(0, sepsLen));
      }
    }

    uniqueAlphabet.replace(0, uniqueAlphabet.length(), consistentShuffle(uniqueAlphabet.toString(), this.salt));
    int guardCount = (int) Math.ceil((double)uniqueAlphabet.length() / GUARD_DIV);

    if (uniqueAlphabet.length() < 3) {
      guards = seps.substring(0, guardCount);
      seps.replace(0, seps.length(), seps.substring(guardCount));
    } else {
      guards = uniqueAlphabet.substring(0, guardCount);
      uniqueAlphabet.replace(0, uniqueAlphabet.length(), uniqueAlphabet.substring(guardCount));
    }

    this.alphabet = uniqueAlphabet.toString();
  }

  /**
   * Immutable {@link Hashids} instance builder.
   */
  public static final class Builder {

    private final String salt;
    private final int minLength;
    private final String alphabet;

    /**
     * Constructs a new instance without a salt, no minimum hash length and the {@link #DEFAULT_ALPHABET default alphabet}.
     */
    public Builder() {
      this.salt = "";
      this.alphabet = DEFAULT_ALPHABET;
      this.minLength = 0;
    }

    /**
     * Constructs a new instance with the given salt, the minimum hash length and alphabet.
     *
     * @param salt the salt to be used as entropy
     * @param minLength the minimum hash length
     * @param alphabet the alphabet to be used for the hash generation
     */
    private Builder(String salt, int minLength, String alphabet) {
      this.salt = salt;
      this.minLength = minLength;
      this.alphabet = alphabet;
    }

    /**
     * Sets the salt to be used as entropy.
     *
     * @param salt the salt to be used as entropy
     * @return a new builder instance with the given salt
     */
    public Builder salt(String salt) {
      return new Builder(salt, minLength, alphabet);
    }

    /**
     * Sets the minimum hash length.
     *
     * @param minLength the minimum hash length
     * @return a new builder instance with the given minimum hash length
     */
    public Builder minHashLength(int minLength) {
      return new Builder(salt, minLength, alphabet);
    }

    /**
     * Sets the alphabet to be used for the hash generation.
     *
     * @param alphabet the alphabet to be used for the hash generation
     * @return a new builder instance with the given alphabet
     */
    public Builder alphabet(String alphabet) {
      return new Builder(salt, minLength, alphabet);
    }

    /**
     * Builds a new {@link Hashids} instance.
     *
     * @return a new configured instance
     */
    public Hashids build() {
      return new Hashids(salt, minLength, alphabet);
    }
  }

  /**
   * Encode number(s).
   *
   * @param numbers the number(s) to encode
   * @return the hash
   */
  public String encode(long... numbers) {
    if (numbers.length == 0) {
      return "";
    }
    return doEncode(numbers);
  }

  /**
   * Encode an hexadecimal string to string.
   *
   * @param hex the hexadecimal string to encode
   * @return the encoded string
   */
  public String encodeHex(String hex) {
    if (!PATTERN_ENCODE_HEX.matcher(hex).matches()) {
      throw new IllegalArgumentException(String.format("%s is not a hex string", hex));
    }
    Matcher matcher = Pattern.compile("[\\w\\W]{1,12}").matcher(hex);
    List<Long> matched = new ArrayList<>();
    while (matcher.find()) {
      matched.add(Long.parseLong("1" + matcher.group(), 16));
    }
    return doEncode(toArray(matched));
  }

  /**
   * Decode an encoded string.
   *
   * @param hash the encoded string
   * @return the decoded number(s)
   */
  public long[] decode(String hash) {
    if (isEmpty(hash)) {
      return new long[0];
    }
    return doDecode(hash, alphabet);
  }

  /**
   * Decode an string to hexadecimal numbers.
   *
   * @param hash the encoded string
   * @return the decoded hexadecimal numbers string
   */
  public String decodeHex(String hash) {
    StringBuilder sb = new StringBuilder();
    long[] numbers = decode(hash);
    for (long number : numbers) {
      sb.append(Long.toHexString(number).substring(1));
    }
    return sb.toString();
  }

  private String doEncode(long... numbers) {
    int numberHashInt = 0;
    for (int idx = 0; idx < numbers.length; idx++) {
      if (numbers[idx] < 0 || numbers[idx] > MAX_NUMBER_VALUE) {
        throw new IllegalArgumentException("Number out of range");
      }
      numberHashInt += numbers[idx] % (idx + 100);
    }
    String decodeAlphabet = alphabet;
    final char lottery = decodeAlphabet.charAt(numberHashInt % decodeAlphabet.length());
    StringBuilder result = new StringBuilder(String.valueOf(lottery));

    String buffer;
    int sepsIdx, guardIdx;
    for (int idx = 0; idx < numbers.length; idx++) {
      long num = numbers[idx];
      buffer = lottery + salt + decodeAlphabet;

      decodeAlphabet = consistentShuffle(decodeAlphabet, buffer.substring(0, decodeAlphabet.length()));
      final String last = hash(num, decodeAlphabet);
      result.append(last);

      if (idx + 1 < numbers.length) {
        num %= ((int) last.charAt(0) + idx);
        sepsIdx = (int) (num % DEFAULT_SEPARATORS.length());
        result.append(DEFAULT_SEPARATORS.charAt(sepsIdx));
      }
    }

    if (result.length() < minHashLength) {
      guardIdx = (numberHashInt + (int) (result.charAt(0))) % guards.length();
      char guard = guards.charAt(guardIdx);
      result.insert(0, guard);

      if (result.length() < minHashLength) {
        guardIdx = (numberHashInt + (int) (result.charAt(2))) % guards.length();
        guard = guards.charAt(guardIdx);
        result.append(guard);
      }
    }

    final int halfLen = decodeAlphabet.length() / 2;
    while (result.length() < minHashLength) {
      decodeAlphabet = consistentShuffle(decodeAlphabet, decodeAlphabet);
      result.insert(0, decodeAlphabet.substring(halfLen)).append(decodeAlphabet.substring(0, halfLen));
      final int excess = result.length() - minHashLength;
      if (excess > 0) {
        int startPos = excess / 2;
        result.replace(0, result.length(), result.substring(startPos, startPos + minHashLength));
      }
    }
    return result.toString();
  }

  private long[] doDecode(String hash, String alphabet) {
    final List<Long> result = new ArrayList<>();
    int idx = 0;
    String[] hashArray = hash.replaceAll("[" + guards + "]", " ").split(" ");
    if (hashArray.length == 3 || hashArray.length == 2) {
      idx = 1;
    }
    String hashBreakdown = hashArray[idx];

    if (!hashBreakdown.isEmpty()) {
      final char lottery = hashBreakdown.charAt(0);
      hashBreakdown = hashBreakdown.substring(1);
      hashBreakdown = Pattern.compile("[" + DEFAULT_SEPARATORS + "]").matcher(hashBreakdown).replaceAll(" ");
      hashArray = hashBreakdown.split(" ");

      String buffer;
      for (String subHash : hashArray) {
        buffer = lottery + salt + alphabet;
        alphabet = consistentShuffle(alphabet, buffer.substring(0, alphabet.length()));
        result.add(unhash(subHash, alphabet));
      }
    }
    return toArray(result);
  }

  private static String consistentShuffle(String alphabet, String salt) {
    if (salt.length() <= 0) {
      return alphabet;
    }
    final char[] saltChars = salt.toCharArray();
    int ascVal, j;
    char [] tmpArr = alphabet.toCharArray();
    for (int idx = tmpArr.length - 1, v = 0, p = 0; idx > 0; idx--, v++) {
      v %= salt.length();
      ascVal = (int) saltChars[v];
      p += ascVal;
      j = (ascVal + v + p) % idx;

      char tmp = tmpArr[j];
      tmpArr[j] = tmpArr[idx];
      tmpArr[idx] = tmp;
    }
    return new String(tmpArr);
  }

  private static String hash(long input, String alphabet) {
    StringBuilder hash = new StringBuilder();
    final int alphabetLen = alphabet.length();
    final char[] alphabetChars = alphabet.toCharArray();
    do {
      hash.insert(0, alphabetChars[(int) (input % alphabetLen)]);
      input /= alphabetLen;
    }
    while (input > 0);
    return hash.toString();
  }

  private static Long unhash(String input, String alphabet) {
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
   * @return {@code true} if the string is {@code null} or empty, {@code false} otherwise
   */
  private boolean isEmpty(String value) {
    return value == null || value.length() == 0;
  }

  /**
   * Returns the ArcVer / SemVer version of the public API.
   *
   * @return the ArcVer/SemVer version string
   * @see <a href="https://github.com/arcticicestudio/arcver">ArcVer</a>
   * @see <a href="http://semver.org">SemVer</a>
   * @since 0.2.0
   */
  public static String getVersion() {
    return VERSION;
  }
}
