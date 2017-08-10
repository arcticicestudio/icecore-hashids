# Enable Additional Features

> Please note that most features will break the interoperability with the [algorithm reference implementation][hashids-js]!

## Allow Hexadecimal Number Prefixes

When this feature is enabled the public API methods [`encodeHex(String)`][guide-encode-hex] and [`decodeHex(String)`][guide-decode-hex] will allow to pass `0x` or `0X` prefixed hexadecimal numbers.

```java
final Hashids hashids = new Hashids.Builder()
  .features(HashidsFeature.ALLOW_HEXADECIMAL_NUMBER_PREFIX)
  .build();

// Both will be "j2g9K4y"
final String hashHexPrefixLowercase = hashids.encodeHex("0x75bcd15");
final String hashHexPrefixUppercase = hashids.encodeHex("0X75bcd15");
```

## Exception Handling

When this feature is enabled the instance will handle errors by throwing specific exceptions instead of returning empty values when invalid parameters are passed to any public API method. This allows a more accurate analysis and treatment of the error by using language specific advantages.

This example will throw a `IllegalArgumentException` to handle negative numbers:

```java
final Hashids hashids = new Hashids.Builder()
  .features(HashidsFeature.EXCEPTION_HANDLING)
  .build();

final long invalidNumber = -123L;
final String hash = hashids.encode(invalidNumber);
```

## No Number Size Limit

This instance feature disables the maximum number size limit which ensures the interoperability with the [algorithm reference implementation][hashids-js] and allows the usage of the Java `Long` [maximum value][long-max-value].

```java
final Hashids hashids = new Hashids.Builder()
  .features(HashidsFeature.NO_MAX_INTEROP_NUMBER_SIZE)
  .build();

final String hash = hashids.encode(Hashids.MAX_INTEROP_NUMBER_SIZE + 1L);
```

[guide-decode-hex]: ../decoding.md#hexadecimal-numbers
[guide-encode-hex]: ../encoding.md#hexadecimal-numbers
[hashids-js]: https://github.com/ivanakimov/hashids.js
