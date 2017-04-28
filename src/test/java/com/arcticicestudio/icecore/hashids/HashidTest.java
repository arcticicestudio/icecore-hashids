/*
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
title      Hashid Unit Tests                                  +
project    icecore-hashids                                    +
repository https://github.com/arcticicestudio/icecore-hashids +
author     Arctic Ice Studio                                  +
email      development@arcticicestudio.com                    +
copyright  Copyright (C) 2017                                 +
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
*/
package com.arcticicestudio.icecore.hashids;


import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link Hashid} implementation.
 *
 * @author Arctic Ice Studio &lt;development@arcticicestudio.com&gt;
 * @since 0.3.0
 */
public class HashidTest {

  @Test
  public void testEqualsSymmetric() {
    Hashid h1 = new Hashid(new long[] {2, 17, 92}, "MjhWikW");
    Hashid h2 = new Hashid(new long[] {2, 17, 92}, "MjhWikW");

    assertTrue(h1.equals(h2) && h2.equals(h1));
    assertTrue(h1.hashCode() == h2.hashCode());
    assertTrue(h1.equals(h1));
    assertFalse(h1.equals(null));

    Long unequalClassObject = 12L;
    assertFalse(h1.equals(unequalClassObject));
  }
}
