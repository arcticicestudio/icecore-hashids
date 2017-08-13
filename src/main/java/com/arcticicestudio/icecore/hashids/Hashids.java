package com.arcticicestudio.icecore.hashids;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toSet;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * A lightweight generator for short, unique, case-sensitive and non-sequential decodable hashes from positive unsigned (long) integer numbers.
 *
 * <p>Implementation of the <a href="http://hashids.org">Hashids</a> algorithm:
 * <ul>
 *   <li>Generation of short, unique, case-sensitive and non-sequential decodable hashes of natural numbers</li>
 *   <li>Additional entropy through salt usage</li>
 *   <li>Configurable minimum hash size</li>
 *   <li>Combining of several numbers to one hash</li>
 *   <li>Deterministic hash computation given the same input and parametrization/configuration</li>
 * </ul>
 *
 * <p>An instance with the default interoperable configurations is available through the {@link Hashids#Hashids() default constructor}.
 *
 * <p>Configured instances can be created via {@link Hashids.Builder} to set a {@link Hashids.Builder#salt salt}, define a
 * ·{@link Hashids.Builder#minLength minimum hash length} or use a {@link Hashids.Builder#alphabet custom alphabet}. Optional {@link HashidsFeature features}
 * can be enabled via the {@link Hashids.Builder#features features(HashidsFeature)} method. <strong>Please note that most features will break the
 * interoperability with the origin algorithm implementation!</strong>
 *
 * <p><strong>Instances of this class are thread-safe.</strong>
 *
 * @author Arctic Ice Studio &lt;development@arcticicestudio.com&gt;
 * @see <a href="https://github.com/arcticicestudio/icecore-hashids">IceCore Hashids GitHub Repository</a>
 * @version 0.3.0
 */
public final class Hashids {

  private static final String VERSION = "0.3.0";
  private static final String VERSION_INTEROP = "1.0.0";

  private static final int LOTTERY_MOD = 100;
  private static final double GUARD_THRESHOLD = 12;
  private static final double SEPARATOR_THRESHOLD = 3.5;
  private static final Pattern HEX_VALUES_PATTERN = Pattern.compile("[\\w\\W]{1,12}");
  private static final Pattern HEX_FORMAT_PATTERN = Pattern.compile("^[0-9a-fA-F]+$");

  /**
   * The maximum number size to ensure interoperability with the origin algorithm implementation <a href="https://github.com/ivanakimov/hashids.js">hashids
   * .js</a>.
   *
   * <p>This limit is based on the
   * JavaScript <a href="https://developer.mozilla.org/en/docs/Web/JavaScript/Reference/Global_Objects/Number/MAX_SAFE_INTEGER">Number.MAX_SAFE_INTEGER</a>
   * constant which represents the maximum safe integer.
   *
   *<p>The limit can be canceled by enabling the {@link HashidsFeature#NO_MAX_INTEROP_NUMBER_SIZE NO_MAX_INTEROP_NUMBER_SIZE} feature. <strong>Please
   * note that this will break the interoperability with the origin algorithm implementation!</strong>
   *
   * @see <a href="https://www.ecma-international.org/ecma-262/7.0/#sec-number.max_safe_integer">ECMAScript 2016 Language Specification</a>
   */
  public static final long MAX_INTEROP_NUMBER_SIZE = (long) Math.pow(2, 53) - 1;

  /**
   * The minimum length of the hash generation alphabet.
   */
  private static final int MIN_ALPHABET_LENGTH = 16;

  /**
   * The default alphabet for the hash generation.
   *
   * <p>A customized alphabet can be set via the instance builder {@link Hashids.Builder#alphabet alphabet} method.
   */
  public static final char[] DEFAULT_ALPHABET = {
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
    'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
    'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
    '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
  };

  /**
   * The default separators to prevent bad, offensive and rude words in generated hashes.
   */
  private static final char[] DEFAULT_SEPARATORS = {'c', 'f', 'h', 'i', 's', 't', 'u', 'C', 'F', 'H', 'I', 'S', 'T', 'U'};

  private final char[] alphabet;
  private final char[] separators;
  private final char[] salt;
  private final char[] guards;
  private final int minLength;
  private final Set<Character> separatorsSet;

  /**
   * A set of all enabled {@link HashidsFeature features}.
   *
   * @since 0.4.0
   */
  private final EnumSet<HashidsFeature> features;

  /**
   * Constructs a new instance without a salt, no minimum hash length, the {@link #DEFAULT_ALPHABET default alphabet} and no enabled
   * {@link HashidsFeature features}.
   */
  public Hashids() {
    this(new char[0], 0, DEFAULT_ALPHABET, EnumSet.noneOf(HashidsFeature.class));
  }

  /**
   * Constructs a new instance with the given configuration and enabled {@link HashidsFeature features}.
   *
   * @param salt the salt to be used as entropy
   * @param minLength the minimum hash length
   * @param alphabet the alphabet to be used for the hash generation
   * @param features the set of enabled Hashids features
   */
  private Hashids(final char[] salt, final int minLength, final char[] alphabet, final EnumSet<HashidsFeature> features) {
    this.minLength = minLength;
    this.salt = Arrays.copyOf(salt, salt.length);
    this.features = features;
    char[] tmpSeparators = shuffle(filterSeparators(DEFAULT_SEPARATORS, alphabet), this.salt);
    char[] tmpAlphabet = validateAndFilterAlphabet(alphabet, tmpSeparators);

    // Check the separator threshold
    if (tmpSeparators.length == 0 || (tmpAlphabet.length / tmpSeparators.length) > SEPARATOR_THRESHOLD) {
      final int minSeparatorsSize = (int) Math.ceil(tmpAlphabet.length / SEPARATOR_THRESHOLD);
      // Check the minimum size of the separators
      if (minSeparatorsSize > tmpSeparators.length) {
        // Fill the separators from the alphabet
        final int missingSeparators = minSeparatorsSize - tmpSeparators.length;
        tmpSeparators = Arrays.copyOf(tmpSeparators, tmpSeparators.length + missingSeparators);
        System.arraycopy(tmpAlphabet, 0, tmpSeparators, tmpSeparators.length - missingSeparators, missingSeparators);
        tmpAlphabet = Arrays.copyOfRange(tmpAlphabet, missingSeparators, tmpAlphabet.length);
      }
    }

    // Shuffle the current alphabet
    shuffle(tmpAlphabet, this.salt);

    // Check the guards
    this.guards = new char[(int) Math.ceil(tmpAlphabet.length / GUARD_THRESHOLD)];
    if (alphabet.length < 3) {
      System.arraycopy(tmpSeparators, 0, guards, 0, guards.length);
      this.separators = Arrays.copyOfRange(tmpSeparators, guards.length, tmpSeparators.length);
      this.alphabet = tmpAlphabet;
    } else {
      System.arraycopy(tmpAlphabet, 0, guards, 0, guards.length);
      this.separators = tmpSeparators;
      this.alphabet = Arrays.copyOfRange(tmpAlphabet, guards.length, tmpAlphabet.length);
    }

    // Populate the separators set
    this.separatorsSet = IntStream.range(0, separators.length)
      .mapToObj(idx -> separators[idx])
      .collect(toSet());
  }

  /**
   * An immutable {@link Hashids} instance builder with optional {@link HashidsFeature features}.
   */
  public static final class Builder {

    private char[] salt;
    private int minLength;
    private char[] alphabet;

    private EnumSet<HashidsFeature> features;

    /**
     * Constructs a new instance without a salt, no minimum hash length, the {@link #DEFAULT_ALPHABET default alphabet} and no enabled
     * {@link HashidsFeature features}.
     */
    public Builder() {
      this.salt = new char[0];
      this.alphabet = DEFAULT_ALPHABET;
      this.minLength = 0;
      this.features = EnumSet.noneOf(HashidsFeature.class);
    }

    /**
     * Sets the salt to be used as entropy.
     *
     * <p>By default the salt is empty.
     *
     * @param salt the salt to be used as entropy
     * @return a new builder instance with the given salt
     */
    public Builder salt(final String salt) {
      this.salt = salt.toCharArray();
      return this;
    }

    /**
     * Sets the minimum hash length.
     *
     * <p>The default value is {@code 0}.
     *
     * @param minLength the minimum hash length
     * @return a new builder instance with the given minimum hash length
     */
    public Builder minLength(final int minLength) {
      this.minLength = minLength;
      return this;
    }

    /**
     * Sets the alphabet to be used for the hash generation.
     *
     * <p>The default value is the {@link #DEFAULT_ALPHABET default alphabet}.
     *
     * @param alphabet the alphabet to be used for the hash generation
     * @return a new builder instance with the given alphabet
     */
    public Builder alphabet(final String alphabet) {
      this.alphabet = alphabet.toCharArray();
      return this;
    }

    /**
     * Enables the given instance {@link HashidsFeature features}.
     *
     * <p>By default no features are enabled.
     *
     * @param features the features to be enabled for this instance
     * @return a new builder instance with the enabled features
     * @since 0.4.0
     */
    public Builder features(final HashidsFeature... features) {
      this.features.addAll(Arrays.asList(features));
      return this;
    }

    /**
     * Builds a new configured {@link Hashids} instance.
     *
     * @return a new configured instance
     */
    public Hashids build() {
      return new Hashids(salt, minLength, alphabet, features);
    }
  }

  /**
   * Encodes the given positive numbers based on this instance configuration.
   *
   * <p>The given numbers size must not be larger than the {@link #MAX_INTEROP_NUMBER_SIZE maximum interoperability size} unless the
   * {@link HashidsFeature#NO_MAX_INTEROP_NUMBER_SIZE NO_MAX_INTEROP_NUMBER_SIZE} feature is enabled. <strong>Please
   * note that this will break the interoperability with the origin algorithm implementation!</strong>
   *
   * @param numbers the positive numbers to be encoded
   * @return the resultant hash of the encoding of the numbers, empty otherwise
   * @throws IllegalArgumentException if the {@link HashidsFeature#EXCEPTION_HANDLING EXCEPTION_HANDLING} feature is enabled and the total length of
   * numbers is zero, any numbers size is invalid or larger than the {@link #MAX_INTEROP_NUMBER_SIZE maximum interoperability size}
   * @throws NullPointerException if the {@link HashidsFeature#EXCEPTION_HANDLING EXCEPTION_HANDLING} feature is enabled and the given numbers are {@code null}
   */
  public String encode(final long... numbers) {
    if (numbers == null) {
      if (features.contains(HashidsFeature.EXCEPTION_HANDLING)) {
        throw new NullPointerException("numbers must not be null!");
      }
      return "";
    }

    if (numbers.length == 0) {
      if (features.contains(HashidsFeature.EXCEPTION_HANDLING)) {
        throw new IllegalArgumentException("length of numbers must be greater than or equal to one!");
      }
      return "";
    }

    for (long number : numbers) {
      if (number < 0) {
        if (features.contains(HashidsFeature.EXCEPTION_HANDLING)) {
          throw new IllegalArgumentException("number must not be less than zero: " + number);
        }
        return "";
      }

      if (number > MAX_INTEROP_NUMBER_SIZE && !features.contains(HashidsFeature.NO_MAX_INTEROP_NUMBER_SIZE)) {
        if (features.contains(HashidsFeature.EXCEPTION_HANDLING)) {
          throw new IllegalArgumentException("number must not exceed the maximum number size: " + number + " > " + MAX_INTEROP_NUMBER_SIZE);
        }
        return "";
      }
    }

    final char[] currentAlphabet = Arrays.copyOf(alphabet, alphabet.length);

    // Determine the lottery number
    final long lotteryId = LongStream.range(0, numbers.length)
      .reduce(0, (state, idx) -> state + numbers[(int) idx] % (idx + LOTTERY_MOD));
    final char lottery = currentAlphabet[(int) (lotteryId % currentAlphabet.length)];

    // Encode each number
    final StringBuilder global = new StringBuilder();
    IntStream.range(0, numbers.length)
      .forEach(idx -> {
        deriveNewAlphabet(currentAlphabet, salt, lottery);
        final int initialLength = global.length();
        transform(numbers[idx], currentAlphabet, global, initialLength);
        // Append the separator
        if (idx + 1 < numbers.length) {
          long n = numbers[idx] % (global.charAt(initialLength) + idx);
          global.append(separators[(int) (n % separators.length)]);
        }
      });

    // Prepend the lottery
    global.insert(0, lottery);

    // Add the guards if there is any space left
    if (minLength > global.length()) {
      int guardIdx = (int) ((lotteryId + lottery) % guards.length);
      global.insert(0, guards[guardIdx]);
      if (minLength > global.length()) {
        guardIdx = (int) ((lotteryId + global.charAt(2)) % guards.length);
        global.append(guards[guardIdx]);
      }
    }

    // Add the necessary padding
    int paddingLeft = minLength - global.length();
    while (paddingLeft > 0) {
      shuffle(currentAlphabet, Arrays.copyOf(currentAlphabet, currentAlphabet.length));

      final int alphabetHalfSize = currentAlphabet.length / 2;
      final int initialSize = global.length();
      if (paddingLeft > currentAlphabet.length) {
        int offset = alphabetHalfSize + (currentAlphabet.length % 2 == 0 ? 0 : 1);

        global.insert(0, currentAlphabet, alphabetHalfSize, offset);
        global.insert(offset + initialSize, currentAlphabet, 0, alphabetHalfSize);

        paddingLeft -= currentAlphabet.length;
      } else {
        // Calculate the excess
        final int excess = currentAlphabet.length + global.length() - minLength;
        final int secondHalfStartOffset = alphabetHalfSize + Math.floorDiv(excess, 2);
        final int secondHalfLength = currentAlphabet.length - secondHalfStartOffset;
        final int firstHalfLength = paddingLeft - secondHalfLength;

        global.insert(0, currentAlphabet, secondHalfStartOffset, secondHalfLength);
        global.insert(secondHalfLength + initialSize, currentAlphabet, 0, firstHalfLength);

        paddingLeft = 0;
      }
    }

    return global.toString();
  }

  /**
   * Encodes the given numbers in hexadecimal format based on this instance configuration.
   *
   * <p>The given hexadecimal numbers must not be prefixed ({@code 0x} or {@code 0X}) unless the
   * {@link HashidsFeature#ALLOW_HEXADECIMAL_NUMBER_PREFIX ALLOW_HEXADECIMAL_NUMBER_PREFIX} feature is enabled. <strong>Please
   * note that this will break the interoperability with the origin algorithm implementation!</strong>
   *
   * @param hexNumbers the numbers in hexadecimal format to be encoded
   * @return the resultant hash of the encoding of the hexadecimal numbers, empty otherwise
   * @throws IllegalArgumentException if the {@link HashidsFeature#EXCEPTION_HANDLING EXCEPTION_HANDLING} feature is enabled and any of the numbers is invalid
   * @throws NullPointerException if the {@link HashidsFeature#EXCEPTION_HANDLING EXCEPTION_HANDLING} feature is enabled and the given numbers are {@code null}
   */
  public String encodeHex(final String hexNumbers) {
    if (hexNumbers == null) {
      if (features.contains(HashidsFeature.EXCEPTION_HANDLING)) {
        throw new NullPointerException("hexNumbers must not be null!");
      }
      return "";
    }

    final String hex;
    if (hexNumbers.startsWith("0x") || hexNumbers.startsWith("0X")) {
      if (features.contains(HashidsFeature.ALLOW_HEXADECIMAL_NUMBER_PREFIX)) {
        hex = hexNumbers.substring(2);
      } else {
        if (features.contains(HashidsFeature.EXCEPTION_HANDLING)) {
          throw new IllegalArgumentException("numbers must not contain a hexadecimal prefix: " + hexNumbers.substring(0, 2));
        }
        return "";
      }
    } else {
      hex = hexNumbers;
    }

    if (!HEX_FORMAT_PATTERN.matcher(hex).matches()) {
      if (features.contains(HashidsFeature.EXCEPTION_HANDLING)) {
        throw new IllegalArgumentException("hexNumbers must be a valid hexadecimal number!");
      }
      return "";
    }

    // Resolve the associated long value and encode it
    LongStream values = LongStream.empty();
    final Matcher matcher = HEX_VALUES_PATTERN.matcher(hex);
    while (matcher.find()) {
      final long value = new BigInteger("1" + matcher.group(), 16).longValue();
      values = LongStream.concat(values, LongStream.of(value));
    }

    return encode(values.toArray());
  }

  /**
   * Decodes the given hash into its numeric representation based on this instance configuration.
   *
   * @param hash the hash to be decoded
   * @return an array of long values with each numeric number present in the hash, empty otherwise
   * @throws IllegalArgumentException if the {@link HashidsFeature#EXCEPTION_HANDLING EXCEPTION_HANDLING} feature is enabled and the hash is invalid
   * @throws NullPointerException if the {@link HashidsFeature#EXCEPTION_HANDLING EXCEPTION_HANDLING} feature is enabled and the given hash is {@code null}
   */
  public long[] decode(final String hash) {
    if (hash == null) {
      if (features.contains(HashidsFeature.EXCEPTION_HANDLING)) {
        throw new NullPointerException("hash must not be null!");
      }
      return new long[0];
    }

    // Validate that the hash only consists of valid characters
    final Set<Character> validInputChars = new HashSet<>(alphabet.length + guards.length + separators.length);
    Stream.of(alphabet, guards, separators)
      .forEach(chars -> IntStream.range(0, chars.length)
        .mapToObj(idx -> chars[idx])
        .forEach(validInputChars::add));
    if (!IntStream.range(0, hash.length()).allMatch(idx -> validInputChars.contains(hash.charAt(idx)))) {
      if (features.contains(HashidsFeature.EXCEPTION_HANDLING)) {
        throw new IllegalArgumentException("invalid hash: " + hash);
      }
      return new long[0];
    }

    // Create a set of the guards and count the total amount
    final Set<Character> guardsSet = IntStream.range(0, guards.length)
      .mapToObj(idx -> guards[idx])
      .collect(toSet());
    final int[] guardsIdx = IntStream.range(0, hash.length())
      .filter(idx -> guardsSet.contains(hash.charAt(idx)))
      .toArray();

    // Calculate the start- and end index based on the guards count
    final int startIdx;
    final int endIdx;
    if (guardsIdx.length > 0) {
      startIdx = guardsIdx[0] + 1;
      endIdx = guardsIdx.length > 1 ? guardsIdx[1] : hash.length();
    } else {
      startIdx = 0;
      endIdx = hash.length();
    }

    LongStream decoded = LongStream.empty();
    if (hash.length() > 0) {
      final char lottery = hash.charAt(startIdx);

      // Create the initial accumulation string
      final int length = hash.length() - guardsIdx.length - 1;
      StringBuilder block = new StringBuilder(length);

      // Create the base salt
      final char[] decodeSalt = new char[alphabet.length];
      decodeSalt[0] = lottery;
      final int saltLength = salt.length >= alphabet.length ? alphabet.length - 1 : salt.length;
      System.arraycopy(salt, 0, decodeSalt, 1, saltLength);
      final int saltLeft = alphabet.length - saltLength - 1;

      final char[] currentAlphabet = Arrays.copyOf(alphabet, alphabet.length);

      for (int i = startIdx + 1; i < endIdx; i++) {
        if (!separatorsSet.contains(hash.charAt(i))) {
          block.append(hash.charAt(i));
          if (i < endIdx - 1) {
            continue;
          }
        }

        if (block.length() > 0) {
          // Create the salt
          if (saltLeft > 0) {
            System.arraycopy(currentAlphabet, 0, decodeSalt, alphabet.length - saltLeft, saltLeft);
          }

          // Prepend the decoded value and create a new block
          shuffle(currentAlphabet, decodeSalt);
          final long number = transform(block.toString().toCharArray(), currentAlphabet);
          decoded = LongStream.concat(decoded, LongStream.of(number));
          block = new StringBuilder(length);
        }
      }
    }

    final long[] decodedValue = decoded.toArray();
    if (!Objects.equals(hash, encode(decodedValue))) {
      if (features.contains(HashidsFeature.EXCEPTION_HANDLING)) {
        throw new IllegalArgumentException("invalid hash: " + hash);
      }
      return new long[0];
    }

    return decodedValue;
  }

  /**
   * Decodes the given hash into its hexadecimal representation based on this instance configuration.
   *
   * @param hash the hash to be decoded
   * @return the hexadecimal representation of the hash values with each numeric number present in the hash, empty otherwise
   * @throws NullPointerException if the {@link HashidsFeature#EXCEPTION_HANDLING EXCEPTION_HANDLING} feature is enabled and the given hash is {@code null}
   */
  public String decodeHex(final String hash) {
    if (hash == null) {
      if (features.contains(HashidsFeature.EXCEPTION_HANDLING)) {
        throw new NullPointerException("hash must not be null!");
      }
      return "";
    }

    final StringBuilder sb = new StringBuilder();
    Arrays.stream(decode(hash))
      .mapToObj(Long::toHexString)
      .forEach(hex -> sb.append(hex, 1, hex.length()));
    return sb.toString();
  }

  /**
   * Decodes the given valid hash into its single numeric representation based on this instance configuration.
   *
   * <p>Simplifies the use-case where the amount of resulting numbers is known before to handle the return value as single value instead of an array.
   * <strong>The given hash must resolve into a one number only</strong>, otherwise see the {@link #decode(String) decode} method.
   *
   * @param hash the valid hash to be decoded
   * @return the decoded number if the given hash is valid, empty otherwise
   * @throws IllegalArgumentException if the {@link HashidsFeature#EXCEPTION_HANDLING EXCEPTION_HANDLING} feature is enabled and the hash is invalid
   * @throws NullPointerException if the {@link HashidsFeature#EXCEPTION_HANDLING EXCEPTION_HANDLING} feature is enabled and the given hash is {@code null}
   * @since 0.4.0
   */
  public Optional<Long> decodeOne(final String hash) {
    if (hash == null) {
      if (features.contains(HashidsFeature.EXCEPTION_HANDLING)) {
        throw new NullPointerException("hash must not be null!");
      }
      return Optional.empty();
    }

    long[] decoded = decode(hash);
    return decoded.length == 1 ? Optional.of(decoded[0]) : Optional.empty();
  }

  /**
   * Returns the version of the public API.
   *
   * @return the version of the public API
   * @see <a href="https://github.com/arcticicestudio/arcver">Arctic Versioning Specification</a>
   * @see <a href="http://semver.org">Semantic Versioning Specification</a>
   * @since 0.2.0
   */
  public static String getVersion() {
    return VERSION;
  }

  /**
   * Returns the version of the original algorithm implementation <a href="https://github.com/ivanakimov/hashids.js">hashids
   * .js</a> with which the public API is interoperable.
   *
   * @return the version of the original algorithm implementation with which the public API is interoperable
   * @see <a href="http://hashids.org/#compatibility">Hashids Compatibility</a>
   * @since 0.4.0
   */
  public static String getInteropVersion() {
    return VERSION_INTEROP;
  }

  @Override
  public boolean equals(final Object otherObject) {
    if (null == otherObject) {
      return false;
    }
    if (this.getClass() != otherObject.getClass()) {
      return false;
    }
    final Hashids otherHashids = (Hashids) otherObject;
    return Arrays.equals(salt, otherHashids.salt)
      && Objects.equals(minLength, otherHashids.minLength)
      && Arrays.equals(alphabet, otherHashids.alphabet)
      && Objects.equals(features, otherHashids.features);
  }

  @Override
  public String toString() {
    return "Hashids{"
      + "salt=" + Arrays.toString(salt)
      + ", minLength=" + minLength
      + ", alphabet=" + Arrays.toString(alphabet)
      + ", features=" + features
      + '}';
  }

  /**
   * Derives a new alphabet using the given salt and lottery character.
   *
   * @param alphabet the current alphabet
   * @param salt the salt to be used as entropy
   * @param lottery the lottery character
   * @return a new derived alphabet
   * @since 0.4.0
   */
  private char[] deriveNewAlphabet(final char[] alphabet, final char[] salt, final char lottery) {
    final char[] newSalt = new char[alphabet.length];
    newSalt[0] = lottery;
    int spaceLeft = newSalt.length - 1;
    int offset = 1;

    if (salt.length > 0 && spaceLeft > 0) {
      int length = salt.length > spaceLeft ? spaceLeft : salt.length;
      System.arraycopy(salt, 0, newSalt, offset, length);
      spaceLeft -= length;
      offset += length;
    }
    if (spaceLeft > 0) {
      System.arraycopy(alphabet, 0, newSalt, offset, spaceLeft);
    }

    return shuffle(alphabet, newSalt);
  }

  /**
   * Filters the given alphabet after the separators.
   *
   * @param separators the separators to be filtered
   * @param alphabet the alphabet to be filtered
   * @return the filtered alphabet
   * @since 0.4.0
   */
  private char[] filterSeparators(final char[] separators, final char[] alphabet) {
    final Set<Character> valid = IntStream.range(0, alphabet.length)
      .mapToObj(idx -> alphabet[idx])
      .collect(toSet());

    return IntStream.range(0, separators.length)
      .mapToObj(idx -> (separators[idx]))
      .filter(valid::contains)
      .map(c -> Character.toString(c))
      .collect(joining())
      .toCharArray();
  }

  /**
   * Shuffles the alphabet with the given salt.
   *
   * @param alphabet the alphabet to be shuffled
   * @param salt the salt with which the alphabet is shuffled
   * @return the shuffled alphabet
   */
  private char[] shuffle(final char[] alphabet, final char[] salt) {
    for (int idx = alphabet.length - 1, mod = 0, idxChar = 0, idxMatch, num; salt.length > 0 && idx > 0; idx--, mod++) {
      mod %= salt.length;
      idxChar += num = salt[mod];
      idxMatch = (num + mod + idxChar) % idx;
      final char tmp = alphabet[idxMatch];
      alphabet[idxMatch] = alphabet[idx];
      alphabet[idx] = tmp;
    }
    return alphabet;
  }

  /**
   * Transforms the hash into the encoded hash state using the given alphabet.
   *
   * @param number the number to be transformed into the encoded hash state
   * @param alphabet the alphabet to be used for the transformation
   * @param sb the string builder to prepend the transformed number
   * @param start the start index for the given alphabet
   * @return the given string builder with the prepended transformed number
   */
  private StringBuilder transform(final long number, final char[] alphabet, final StringBuilder sb, final int start) {
    long input = number;
    do {
      // Prepend the matched character and trim the input
      sb.insert(start, alphabet[(int) (input % alphabet.length)]);
      input = input / alphabet.length;
    } while (input > 0);

    return sb;
  }

  /**
   * Transforms the hash into the decoded number state using the given alphabet.
   *
   * @param hash the hash to be transformed into the decoded number state
   * @param alphabet the alphabet to be used for the transformation
   * @return the transformed hash in the decoded number state
   */
  private long transform(final char[] hash, final char[] alphabet) {
    long number = 0;

    final Map<Character, Integer> alphabetMapping = IntStream.range(0, alphabet.length)
      .mapToObj(idx -> new Object[]{alphabet[idx], idx})
      .collect(groupingBy(arr -> (Character) arr[0], mapping(arr -> (Integer) arr[1], reducing(null, (a, b) -> a == null ? b : a))));

    for (int idx = 0; idx < hash.length; ++idx) {
      number += alphabetMapping.get(hash[idx]) * (long) Math.pow(alphabet.length, hash.length - idx - 1);
    }

    return number;
  }

  /**
   * Validates and filters the given alphabet.
   *
   * @param alphabet The alphabet to be validated and filtered
   * @param separators the separators to be filtered
   * @return the filtered alphabet
   * @throws IllegalArgumentException if the given alphabet does not contain the {@link #MIN_ALPHABET_LENGTH minimum length of unique characters} or contains
   * spaces.
   * @since 0.4.0
   */
  private char[] validateAndFilterAlphabet(final char[] alphabet, final char[] separators) {
    if (alphabet.length < MIN_ALPHABET_LENGTH) {
      throw new IllegalArgumentException("alphabet must contain at least " + MIN_ALPHABET_LENGTH + " unique characters: " + alphabet.length);
    }

    final Set<Character> validated = new LinkedHashSet<>(alphabet.length);
    final Set<Character> invalid = IntStream.range(0, separators.length)
      .mapToObj(idx -> separators[idx])
      .collect(toSet());

    // Add the validated characters
    IntStream.range(0, alphabet.length)
      .forEach(idx -> {
        if (alphabet[idx] == ' ') {
          throw new IllegalArgumentException("alphabet must not contain spaces: index " + idx);
        }
        final Character c = alphabet[idx];
        if (!invalid.contains(c)) {
          validated.add(c);
        }
      });

    // Create a new alphabet from the validated characters
    final char[] uniqueAlphabet = new char[validated.size()];
    int idx = 0;
    for (char c : validated) {
      uniqueAlphabet[idx++] = c;
    }
    return uniqueAlphabet;
  }
}
