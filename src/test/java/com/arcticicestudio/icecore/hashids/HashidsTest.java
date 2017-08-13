package com.arcticicestudio.icecore.hashids;

import static com.arcticicestudio.icecore.hashids.HashidsFeature.ALLOW_HEXADECIMAL_NUMBER_PREFIX;
import static com.arcticicestudio.icecore.hashids.HashidsFeature.EXCEPTION_HANDLING;
import static com.arcticicestudio.icecore.hashids.HashidsFeature.NO_MAX_INTEROP_NUMBER_SIZE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import java.util.Optional;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Units tests for the <a href="https://github.com/arcticicestudio/icecore-hashids">IceCore Hashids</a> public API.
 *
 * @author Arctic Ice Studio &lt;development@arcticicestudio.com&gt;
 * @since 0.1.0
 */
public class HashidsTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void defaultInstanceConfigurationEquality() {
    final Hashids hashidsBuilder = new Hashids.Builder().build();
    final Hashids hashids = new Hashids();
    assertThat(hashidsBuilder, equalTo(hashids));
    assertThat(hashidsBuilder.toString(), equalTo(hashids.toString()));
  }

  @Test
  public void invalidAlphabetLength() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("alphabet must contain at least 16 unique characters: 6");
    final Hashids hashids = new Hashids.Builder()
      .alphabet("abc123")
      .build();
  }

  @Test
  public void alphabetWithSpaces() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("alphabet must not contain spaces: index 1");
    final Hashids hashids = new Hashids.Builder()
      .salt("salt")
      .alphabet("a bcdefg1234567890")
      .build();
  }

  @Test
  public void alphabetWithSymbols() {
    Hashids hashids = new Hashids.Builder()
      .alphabet("`~!@#$%^&*()-_=+|';:/?.>,<{[}]")
      .build();
    assertThat(hashids.encode(123456L), equalTo("]]''?"));
  }

  @Test
  public void transcode() {
    Hashids hashids = new Hashids.Builder().build();
    assertThat(hashids.encode(123456L), equalTo("xkNDJ"));
    assertThat(hashids.encode(11L, 222L, 3333L), equalTo("x7SQJh8kQ"));
    assertThat(hashids.decode("xkNDJ"), equalTo(new long[] {123456L}));
    assertThat(hashids.decode("x7SQJh8kQ"), equalTo(new long[] {11L, 222L, 3333L}));
    assertThat(hashids.decodeOne("xkNDJ"), equalTo(Optional.of(123456L)));
  }

  @Test
  public void transcodeWithSalt() {
    Hashids hashids = new Hashids.Builder()
      .salt("salt")
      .build();
    assertThat(hashids.encode(123456L), equalTo("7E1dX"));
    assertThat(hashids.encode(11L, 222L, 3333L), equalTo("7NfvoFvAJ"));
    assertThat(hashids.decode("7E1dX"), equalTo(new long[] {123456L}));
    assertThat(hashids.decode("7NfvoFvAJ"), equalTo(new long[] {11L, 222L, 3333L}));
    assertThat(hashids.decodeOne("7E1dX"), equalTo(Optional.of(123456L)));
  }

  @Test
  public void transcodeWithMinLength() {
    Hashids hashids = new Hashids.Builder()
      .minLength(16)
      .build();
    assertThat(hashids.encode(123456L), equalTo("VoJX7axkNDJeyv4E"));
    assertThat(hashids.encode(11L, 222L, 3333L), equalTo("vDLax7SQJh8kQe6p"));
    assertThat(hashids.decode("VoJX7axkNDJeyv4E"), equalTo(new long[] {123456L}));
    assertThat(hashids.decode("vDLax7SQJh8kQe6p"), equalTo(new long[] {11L, 222L, 3333L}));
    assertThat(hashids.decodeOne("VoJX7axkNDJeyv4E"), equalTo(Optional.of(123456L)));
  }

  @Test
  public void transcodeWithCustomAlphabet() {
    Hashids hashids = new Hashids.Builder()
      .alphabet("abcdefghij1234560")
      .build();
    assertThat(hashids.encode(123456L), equalTo("e60655"));
    assertThat(hashids.encode(11L, 222L, 3333L), equalTo("e43i3j2hg6e4"));
    assertThat(hashids.decode("e60655"), equalTo(new long[] {123456L}));
    assertThat(hashids.decode("e43i3j2hg6e4"), equalTo(new long[] {11L, 222L, 3333L}));
    assertThat(hashids.decodeOne("e60655"), equalTo(Optional.of(123456L)));

    Hashids hashidsSymbols = new Hashids.Builder()
      .alphabet("!#$%&'()*+,-./:;<=>?@[]^_`{|}~")
      .build();
    assertThat(hashidsSymbols.encode(123456L), equalTo("~_--:"));
    assertThat(hashidsSymbols.encode(11L, 222L, 3333L), equalTo("<~$-{$:;="));
    assertThat(hashidsSymbols.decode("~_--:"), equalTo(new long[] {123456L}));
    assertThat(hashidsSymbols.decode("<~$-{$:;="), equalTo(new long[] {11L, 222L, 3333L}));
    assertThat(hashidsSymbols.decodeOne("~_--:"), equalTo(Optional.of(123456L)));
  }

  @Test
  public void transcodeWithSaltAndMinLength() {
    Hashids hashids = new Hashids.Builder()
      .salt("salt")
      .minLength(16)
      .build();
    assertThat(hashids.encode(123456L), equalTo("lgaRz37E1dXkB1XL"));
    assertThat(hashids.encode(11L, 222L, 3333L), equalTo("Wnj37NfvoFvAJmze"));
    assertThat(hashids.decode("lgaRz37E1dXkB1XL"), equalTo(new long[] {123456L}));
    assertThat(hashids.decode("Wnj37NfvoFvAJmze"), equalTo(new long[] {11L, 222L, 3333L}));
    assertThat(hashids.decodeOne("lgaRz37E1dXkB1XL"), equalTo(Optional.of(123456L)));
  }

  @Test
  public void transcodeWithSaltAndCustomAlphabet() {
    Hashids hashids = new Hashids.Builder()
      .salt("salt")
      .alphabet("abcdefghij1234560")
      .build();
    assertThat(hashids.encode(123456L), equalTo("534300"));
    assertThat(hashids.encode(11L, 222L, 3333L), equalTo("5eahg15cbea1"));
    assertThat(hashids.decode("534300"), equalTo(new long[] {123456L}));
    assertThat(hashids.decode("5eahg15cbea1"), equalTo(new long[] {11L, 222L, 3333L}));
    assertThat(hashids.decodeOne("534300"), equalTo(Optional.of(123456L)));
  }

  @Test
  public void transcodeWithMinLengthAndCustomAlphabet() {
    Hashids hashids = new Hashids.Builder()
      .minLength(16)
      .alphabet("abcdefghij1234560")
      .build();
    assertThat(hashids.encode(123456L), equalTo("6125ebe60655adg0"));
    assertThat(hashids.encode(11L, 222L, 3333L), equalTo("e1be43i3j2hg6e4a"));
    assertThat(hashids.decode("6125ebe60655adg0"), equalTo(new long[] {123456L}));
    assertThat(hashids.decode("e1be43i3j2hg6e4a"), equalTo(new long[] {11L, 222L, 3333L}));
    assertThat(hashids.decodeOne("6125ebe60655adg0"), equalTo(Optional.of(123456L)));
  }

  @Test
  public void transcodeWithSaltAndMinLengthAndCustomAlphabet() {
    Hashids hashids = new Hashids.Builder()
      .salt("salt")
      .minLength(16)
      .alphabet("abcdefghij1234560")
      .build();
    assertThat(hashids.encode(123456L), equalTo("61a0425343002gd3"));
    assertThat(hashids.encode(11L, 222L, 3333L), equalTo("4d25eahg15cbea12"));
    assertThat(hashids.decode("61a0425343002gd3"), equalTo(new long[] {123456L}));
    assertThat(hashids.decode("4d25eahg15cbea12"), equalTo(new long[] {11L, 222L, 3333L}));
    assertThat(hashids.decodeOne("61a0425343002gd3"), equalTo(Optional.of(123456L)));
  }

  @Test
  public void transcodeMaximumNumberSize() {
    Hashids hashids = new Hashids.Builder().build();
    assertThat(hashids.encode(Hashids.MAX_INTEROP_NUMBER_SIZE), equalTo("lEW77X7g527"));
    assertThat(hashids.decode("lEW77X7g527"), equalTo(new long[] {Hashids.MAX_INTEROP_NUMBER_SIZE}));
    assertThat(hashids.decodeOne("lEW77X7g527"), equalTo(Optional.of(Hashids.MAX_INTEROP_NUMBER_SIZE)));
  }

  @Test
  public void transcodeNullInput() {
    final Hashids hashids = new Hashids.Builder().build();
    assertThat(hashids.encode((long[]) null), emptyString());
    assertThat(hashids.decode(null), equalTo(new long[0]));
    assertThat(hashids.decodeOne(null), equalTo(Optional.empty()));
  }

  @Test
  public void transcodeHex() {
    final Hashids hashids = new Hashids.Builder().build();
    assertThat(hashids.encodeHex("75bcd15"), equalTo("j2g9K4y"));
    assertThat(hashids.decodeHex("j2g9K4y"), equalTo("75bcd15"));
  }

  @Test
  public void transcodeHexPrefixedInput() {
    final Hashids hashids = new Hashids.Builder().build();
    assertThat(hashids.encodeHex("0x75bcd15"), emptyString());
    assertThat(hashids.encodeHex("0X75bcd15"), emptyString());
    assertThat(hashids.decodeHex("0x75bcd15"), emptyString());
    assertThat(hashids.decodeHex("0X75bcd15"), emptyString());
  }

  @Test
  public void transcodeHexWithAllowedHexadecimalNumberPrefix() {
    final Hashids hashids = new Hashids.Builder()
      .features(ALLOW_HEXADECIMAL_NUMBER_PREFIX)
      .build();
    final String hex = hashids.encodeHex("75bcd15");
    final String hexPrefixLowercase = hashids.encodeHex("0x75bcd15");
    final String hexPrefixUppercase = hashids.encodeHex("0X75bcd15");
    assertThat(hex, equalTo("j2g9K4y"));
    assertThat(hex, equalTo(hexPrefixLowercase));
    assertThat(hex, equalTo(hexPrefixUppercase));
    assertThat(hashids.decodeHex(hex), equalTo("75bcd15"));
  }

  @Test
  public void transcodeHexNullInput() {
    final Hashids hashids = new Hashids.Builder().build();
    assertThat(hashids.encodeHex(null), emptyString());
    assertThat(hashids.decodeHex(null), emptyString());
  }

  @Test
  public void encodeMaximumNumberSizeExceeded() {
    Hashids hashids = new Hashids.Builder().build();
    assertThat(hashids.encode(Hashids.MAX_INTEROP_NUMBER_SIZE + 1L), emptyString());
  }

  @Test
  public void encodeMaximumNumberSizeExceededWithExceptionHandling() {
    Hashids hashids = new Hashids.Builder()
      .features(EXCEPTION_HANDLING)
      .build();
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("number must not exceed the maximum number size: 9007199254740992 > 9007199254740991");
    hashids.encode(Hashids.MAX_INTEROP_NUMBER_SIZE + 1L);
  }

  @Test
  public void encodeWithNoMaximumNumberSizeLimit() {
    Hashids hashids = new Hashids.Builder()
      .features(NO_MAX_INTEROP_NUMBER_SIZE)
      .build();
    assertThat(hashids.encode(Hashids.MAX_INTEROP_NUMBER_SIZE + 1L), not(emptyString()));
  }

  @Test
  public void encodeNegativeInput() {
    final Hashids hashids = new Hashids.Builder().build();
    assertThat(hashids.encode(-123456L), emptyString());
    assertThat(hashids.encode(-10L, -222L, -3333L), emptyString());
  }

  @Test
  public void encodeNegativeInputWithExceptionHandling() {
    final Hashids hashids = new Hashids.Builder()
      .features(EXCEPTION_HANDLING)
      .build();
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("number must not be less than zero: -1");
    hashids.encode(-1L);
  }

  @Test
  public void encodeNullInputWithExceptionHandling() {
    final Hashids hashids = new Hashids.Builder()
      .features(EXCEPTION_HANDLING)
      .build();
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("numbers must not be null!");
    hashids.encode((long[]) null);
  }

  @Test
  public void decodeNullInputWithExceptionHandling() {
    final Hashids hashids = new Hashids.Builder()
      .features(EXCEPTION_HANDLING)
      .build();
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("hash must not be null!");
    hashids.decode(null);
  }

  @Test
  public void decodeOneNullInputWithExceptionHandling() {
    final Hashids hashids = new Hashids.Builder()
      .features(EXCEPTION_HANDLING)
      .build();
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("hash must not be null!");
    hashids.decodeOne(null);
  }

  @Test
  public void encodeNoInput() {
    final Hashids hashids = new Hashids.Builder().build();
    assertThat(hashids.encode(), emptyString());
  }

  @Test
  public void encodeNoInputWithExceptionHandling() {
    final Hashids hashids = new Hashids.Builder()
      .features(EXCEPTION_HANDLING)
      .build();
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("length of numbers must be greater than or equal to one!");
    hashids.encode();
  }

  @Test
  public void encodeHexInvalidHexadecimalFormat() {
    final Hashids hashids = new Hashids.Builder().build();
    assertThat(hashids.encodeHex("z"), emptyString());
  }

  @Test
  public void encodeHexInvalidHexadecimalFormatWithExceptionHandling() {
    final Hashids hashids = new Hashids.Builder()
      .features(EXCEPTION_HANDLING)
      .build();
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("hexNumbers must be a valid hexadecimal number!");
    hashids.encodeHex("z");
  }

  @Test
  public void encodeHexLowercasePrefixedInputWithExceptionHandling() {
    final Hashids hashids = new Hashids.Builder()
      .features(EXCEPTION_HANDLING)
      .build();
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("numbers must not contain a hexadecimal prefix: 0x");
    hashids.encodeHex("0x75bcd15");
  }

  @Test
  public void encodeHexUppercasePrefixedInputWithExceptionHandling() {
    final Hashids hashids = new Hashids.Builder()
      .features(EXCEPTION_HANDLING)
      .build();
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("numbers must not contain a hexadecimal prefix: 0X");
    hashids.encodeHex("0X75bcd15");
  }

  @Test
  public void encodeHexNullInputWithExceptionHandling() {
    final Hashids hashids = new Hashids.Builder()
      .features(EXCEPTION_HANDLING)
      .build();
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("hexNumbers must not be null!");
    hashids.encodeHex(null);
  }

  @Test
  public void decodeHexNullInputWithExceptionHandling() {
    final Hashids hashids = new Hashids.Builder()
      .features(EXCEPTION_HANDLING)
      .build();
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("hash must not be null!");
    hashids.decodeHex(null);
  }

  @Test
  public void decodeInvalidHash() {
    final Hashids hashids = new Hashids.Builder().build();
    assertThat(hashids.decode("yogurt"), equalTo(new long[0]));
    assertThat(hashids.decode("()"), equalTo(new long[0]));
    assertThat(hashids.decode("[]"), equalTo(new long[0]));
    assertThat(hashids.decodeOne("yogurt"), equalTo(Optional.empty()));
    assertThat(hashids.decodeOne("()"), equalTo(Optional.empty()));
    assertThat(hashids.decodeOne("[]"), equalTo(Optional.empty()));
  }

  @Test
  public void decodeInvalidSalt() {
    final Hashids hashidsSalt = new Hashids.Builder()
      .salt("salt")
      .build();
    final Hashids hashidsPepper = new Hashids.Builder()
      .salt("pepper")
      .build();
    String salt = hashidsSalt.encode(123456L);
    String pepper = hashidsPepper.encode(123456L);
    assertThat(salt, not(equalTo(pepper)));
    assertThat(hashidsSalt.decode(pepper), equalTo(new long[0]));
    assertThat(hashidsSalt.decodeOne(pepper), equalTo(Optional.empty()));
    assertThat(hashidsPepper.decode(salt), equalTo(new long[0]));
    assertThat(hashidsPepper.decodeOne(salt), equalTo(Optional.empty()));
  }

  @Test
  public void decodeInvalidHashWithExceptionHandling() {
    final Hashids hashids = new Hashids.Builder()
      .features(EXCEPTION_HANDLING)
      .build();
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("invalid hash: yogurt");
    hashids.decode("yogurt");
  }

  @Test
  public void decodeOneInvalidHashWithExceptionHandling() {
    final Hashids hashids = new Hashids.Builder()
      .features(EXCEPTION_HANDLING)
      .build();
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("invalid hash: yogurt");
    hashids.decodeOne("yogurt");
  }

  @Test
  public void validVersion() {
    assertThat(Hashids.getVersion(), equalTo("0.4.0"));
  }

  @Test
  public void validInteropVersion() {
    assertThat(Hashids.getInteropVersion(), equalTo("1.0.0"));
  }
}
