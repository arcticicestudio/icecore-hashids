/*
 * Copyright (c) 2016-present Arctic Ice Studio <development@arcticicestudio.com>
 * Copyright (c) 2016-present Sven Greb <code@svengreb.de>
 *
 * Project:    IceCore Hashids
 * Repository: https://github.com/arcticicestudio/icecore-hashids
 * License:    MIT
 */

package com.arcticicestudio.icecore.hashids;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Sets up the reference implementation script and provides it as {@link HashidsJs} representation class.
 *
 * @author Arctic Ice Studio &lt;development@arcticicestudio.com&gt;
 * @see <a href="https://github.com/ivanakimov/hashids.js">hashids.js</a>
 * @version 0.4.0
 */
abstract class AbstractHashidsTest {

  final Hashids algorithm;
  final HashidsJs jsAlgorithm;
  final long[] input;

  AbstractHashidsTest(final Supplier<Hashids> algorithm, final Supplier<HashidsJs> jsAlgorithm, final long[] input) {
    this.algorithm = algorithm.get();
    this.jsAlgorithm = jsAlgorithm.get();
    this.input = input;
  }

  static HashidsJs getHashidsJs() {
    return getHashidsJs("");
  }

  static HashidsJs getHashidsJs(final String salt) {
    return getHashidsJs(salt, 0);
  }

  static HashidsJs getHashidsJs(final String salt, final int minLength) {
    return getHashidsJs(salt, minLength, "");
  }

  static HashidsJs getHashidsJs(final String salt, final int minLength, final String alphabet) {
    try {
      final ScriptEngine nashorn = new ScriptEngineManager().getEngineByName("nashorn");
      try (FileInputStream hashidsScript = new FileInputStream(new File("node_modules/hashids/dist/hashids.js"))) {
        nashorn.eval(new InputStreamReader(hashidsScript));
      }

      // Create the instantiation script with the constructor parameters
      final StringBuilder script = new StringBuilder("var hashids = new Hashids(");

      if (salt != null && salt.length() > 0) {
        script.append("'").append(salt).append("',");
      } else {
        script.append("undefined,");
      }
      script.append(minLength).append(",");
      if (alphabet != null && alphabet.length() > 0) {
        script.append("'").append(alphabet).append("'");
      } else {
        script.append("undefined");
      }
      script.append(");");

      // Evaluate the script and return the algorithm bridge
      nashorn.eval(script.toString());
      return new HashidsJs(nashorn);
    } catch (final Exception e) {
      throw new RuntimeException("unable to initialize hashids.js algorithm", e);
    }
  }

  /**
   * Represents the reference implementation.
   */
  static final class HashidsJs {

    private final ScriptEngine engine;

    private HashidsJs(final ScriptEngine engine) {
      this.engine = engine;
    }

    String encode(long[] numbers) {
      final String params = LongStream.of(numbers)
        .mapToObj(String::valueOf)
        .collect(Collectors.joining(","));
      return eval("hashids.encode(" + params + ");");
    }

    long[] decode(String hash) {
      return eval("hashids.decode('" + hash + "');");
    }

    @SuppressWarnings("unchecked")
    private <T> T eval(final String script) {
      try {
        return (T) engine.eval(script);
      } catch (final Exception e) {
        throw new AssertionError("unexpected exception during hashid.js algorithm execution evaluation", e);
      }
    }
  }
}
