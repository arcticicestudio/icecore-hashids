/*
 * Copyright (c) 2016-present Arctic Ice Studio <development@arcticicestudio.com>
 * Copyright (c) 2016-present Sven Greb <code@svengreb.de>
 *
 * Project:    IceCore Hashids
 * Repository: https://github.com/arcticicestudio/icecore-hashids
 * License:    MIT
 */

package com.arcticicestudio.icecore.hashids;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Interoperability tests for the {@link Hashids} algorithm and the reference implementation represented by the {@link HashidsJs} class.
 *
 * @author Arctic Ice Studio &lt;development@arcticicestudio.com&gt;
 * @see <a href="https://github.com/ivanakimov/hashids.js">hashids.js</a>
 * @version 0.4.0
 */
@RunWith(Parameterized.class)
public class InteropHashidsTest extends AbstractHashidsTest {

  /**
   * Generates test data with incremented numbers from 1 to 32.
   *
   * @return the generated test data
   */
  @Parameterized.Parameters(name = "{index}: {0}")
  public static Collection<Object[]> data() {

    // generate a 1-to-32 sized arrays
    final List<long[]> input = new ArrayList<>();
    for (long i = 0; i < 32; i++) {
      if (input.isEmpty()) {
        input.add(new long[]{i});
      } else {
        final long[] prev = input.get(input.size() - 1);
        final long[] curr = Arrays.copyOf(prev, prev.length + 1);
        curr[prev.length] = i;
        input.add(curr);
      }
    }

    // generate a supplier for all algorithm combinations
    final List<Supplier<?>[]> algorithms = new ArrayList<>();
    algorithms.add(new Supplier<?>[]{
      () -> "NoSalt|NoMinLength|NoAlphabet",
      Hashids::new,
      AbstractHashidsTest::getHashidsJs
    });

    algorithms.add(new Supplier<?>[]{
      () -> "Salt|NoMinLength|NoAlphabet",
      () -> new Hashids.Builder().salt("salt and pepper").build(),
      () -> getHashidsJs("salt and pepper")
    });

    algorithms.add(new Supplier<?>[]{
      () -> "NoSalt|NoMinLength|Alphabet",
      () -> new Hashids.Builder().alphabet("abcdef1234567890").build(),
      () -> getHashidsJs("", 0, "abcdef1234567890")
    });

    algorithms.add(new Supplier<?>[]{
      () -> "NoSalt|MinLength|NoAlphabet",
      () -> new Hashids.Builder().minLength(32).build(),
      () -> getHashidsJs("", 32)
    });

    algorithms.add(new Supplier<?>[]{
      () -> "Salt|NoMinLength|Alphabet",
      () -> new Hashids.Builder().salt("salt and pepper").alphabet("abcdef1234567890").build(),
      () -> getHashidsJs("salt and pepper", 0, "abcdef1234567890")
    });

    algorithms.add(new Supplier<?>[]{
      () -> "NoSalt|MinLength|Alphabet",
      () -> new Hashids.Builder().minLength(32).alphabet("abcdef1234567890").build(),
      () -> getHashidsJs("", 32, "abcdef1234567890")
    });

    algorithms.add(new Supplier<?>[]{
      () -> "Salt|MinLength|NoAlphabet",
      () -> new Hashids.Builder().salt("salt and pepper").minLength(32).build(),
      () -> getHashidsJs("salt and pepper", 32)
    });

    algorithms.add(new Supplier<?>[]{
      () -> "Salt|MinLength|Alphabet",
      () -> new Hashids.Builder().salt("salt and pepper").minLength(32).alphabet("abcdef1234567890").build(),
      () -> getHashidsJs("salt and pepper", 32, "abcdef1234567890")
    });

    // generate the input for each hash algorithm
    return algorithms.stream()
      .flatMap(algorithm -> input.stream().map(numbers -> new Object[]{
        // name
        String.format("algorithm(%s) with input(%s)", algorithm[0].get(), Arrays.toString(numbers)),
        // algorithm, jsAlgorithm, input
        algorithm[1], algorithm[2], numbers
      }))
      .collect(Collectors.toList());
  }

  @SuppressWarnings("unused")
  public InteropHashidsTest(final String name, final Supplier<Hashids> hashids, final Supplier<HashidsJs> hashidsJs, final long[] input) {
    super(hashids, hashidsJs, input);
  }

  @Test
  public void runInteropTests() {
    final String encoded = algorithm.encode(input);
    assertThat("encoding mismatch", encoded, equalTo(jsAlgorithm.encode(input)));
    final long[] original = algorithm.decode(encoded);
    assertThat(original, equalTo(input));
  }
}
