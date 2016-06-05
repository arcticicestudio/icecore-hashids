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
public final class Hashids {}
