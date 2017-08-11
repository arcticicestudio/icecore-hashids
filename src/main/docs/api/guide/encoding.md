# Encoding

It is recommended to always use a [salt][salt] before encoding numbers.

> All numbers **must be greater than or equal to zero**!

If the [exception handling][feature-exception-handling] feature is enabled both public API encode methods will throw a `IllegalArgumentException` when no numbers are passed, any number is negative or greater than the maximum number size, unless the feature to allow numbers greater than the [maximum interoperability number size][feature-size-limit] is not enabled. When the passed argument is `null` a `NullPointerException` will be thrown.

## Natural Numbers

The public API method `encode(long...)` can be passed **one or more numbers**.

```java
final Hashids hashids = new Hashids.Builder()
  .salt("salt and pepper")
  .build();

final String singleNumberHash = hashids.encode(123L); // Result: "Blk"
final String multipleNumberHash = hashids.encode(42L, 5L, 17L); // Result: "9dTLhR"
```

## Hexadecimal Numbers

Hexadecimal numbers can be encoded by using the public API method `encodeHex(String)`.

If the [exception handling][feature-exception-handling] feature is enabled the method will throw a `IllegalArgumentException` when the passed hexadecimal number format is not valid.

```java
final Hashids hashids = new Hashids.Builder()
  .salt("salt and pepper")
  .build();

final String hash = hashids.encodeHex("75bcd15"); // Result: "j2g9K4y"
```

If the feature to [allow hexadecimal number prefixes][feature-hex-prefix] is enabled the passed value can optionally be prefixed with `0x` or `0X`.

```java
final Hashids hashids = new Hashids.Builder()
  .salt("salt and pepper")
  .features(HashidsFeature.ALLOW_HEXADECIMAL_NUMBER_PREFIX)
  .build();

// Both will be "j2g9K4y"
final String hashHexPrefixLowercase = hashids.encodeHex("0x75bcd15");
final String hashHexPrefixUppercase = hashids.encodeHex("0X75bcd15");
```

[feature-exception-handling]: configuration/features.md#allow-hexadecimal-number-prefixes
[feature-hex-prefix]: configuration/features.md#allow-hexadecimal-number-prefixes
[feature-size-limit]: configuration/features.md#no-number-size-limit
[salt]: configuration/index.md#using-a-salt
