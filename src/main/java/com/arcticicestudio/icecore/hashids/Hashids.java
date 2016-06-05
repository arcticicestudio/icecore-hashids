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
 *    Generated strings can have a {@code minimumLength}.
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

  private final String salt;

  public Hashids(String salt) {
    this(salt, 0);
  }
}
