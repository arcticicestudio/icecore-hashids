package com.arcticicestudio.icecore.hashids;

import java.util.EnumSet;

/**
 * Enumeration that defines optional features for the {@link Hashids.Builder}.
 *
 * <p><strong>Please note that almost every feature will break the interoperability with the origin algorithm implementation if enabled!</strong>
 *
 * @author Arctic Ice Studio &lt;development@arcticicestudio.com&gt;
 * @since 0.4.0
 */
public enum HashidsFeature {
  /**
   * Enables support for the {@code 0x} or {@code 0X} hexadecimal format prefixes of parameter values passed to the
   * {@link Hashids#encodeHex(String) encodeHex(String)}and {@link Hashids#decodeHex(String) decodeHex(String)} methods.
   *
   * <p><strong>Please note that this will break the interoperability with the origin algorithm implementation!</strong>
   *
   * @see <a href="https://en.wikipedia.org/wiki/Hexadecimal#Written_representation">Hexadecimal - Written representation</a>
   */
  ALLOW_HEXADECIMAL_NUMBER_PREFIX,

  /**
   * Enables the handling of exceptions instead of returning empty values when invalid parameters are passed to any public API method.
   *
   * <p><strong>Please note that this will break the interoperability with the origin algorithm implementation!</strong>
   */
  EXCEPTION_HANDLING,

  /**
   * Disables the {@link Hashids#MAX_INTEROP_NUMBER_SIZE maximum number size limit} which ensures the interoperability with the origin algorithm implementation
   * <a href="https://github.com/ivanakimov/hashids.js">hashids.js</a> and allows the usage of the {@link Long#MAX_VALUE Java Long maxiumum value}.
   *
   * <p><strong>Please note that this will break the interoperability with the origin algorithm implementation!</strong>
   */
  NO_MAX_INTEROP_NUMBER_SIZE;

  /**
   * Enables all features.
   */
  public static final EnumSet<HashidsFeature> ALL = EnumSet.allOf(HashidsFeature.class);
}
