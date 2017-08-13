# Decoding

> In order to decode a hash the [instance algorithm configuration][algorithm-config] **must be equal to the configuration the hash has been encoded with**, otherwise the returned value will be empty!

If the [exception handling][feature-exception-handling] feature is enabled a `IllegalArgumentException` will be thrown instead when the passed hash is not valid or a `NullPointerException` if the passed argument is `null`.

This example will throw a `IllegalArgumentException` due to instance incompatibility:

```java
final Hashids saltyHashids = new Hashids.Builder()
  .salt("salt and pepper")
  .features(HashidsFeature.EXCEPTION_HANDLING)
  .build();

final Hashids fruityHashids = new Hashids.Builder()
  .salt("coconut and yogurt")
  .features(HashidsFeature.EXCEPTION_HANDLING)
  .build();

final String hash = saltyHashids.encode(123L);
final long[] invalidNumbers = fruityHashids.decode(hash); // IllegalArgumentException
```

## Natural Numbers

To decode a hash use the public API method `decode(String)`.

```java
final Hashids hashids = new Hashids.Builder()
  .salt("salt and pepper")
  .build();

final long[] number = hashids.decode("Blk"); // Result: [123]
final long[] numbers = hashids.decode("9dTLhR"); // Result: [42, 5, 17]
```

## Hexadecimal Numbers

Hexadecimal numbers can be decoded by using the public API method `decodeHex(String)`.

```java
final Hashids hashids = new Hashids.Builder()
  .salt("salt and pepper")
  .build();

final String hexNumber = hashids.decodeHex("j2g9K4y"); // Result: "75bcd15"
```

## Single Number As Optional

The `decodeOne(String)` public API method simplifies the use-case where the amount of resulting numbers is known before to handle the return value as single value instead of an array.

It decodes into a single numeric representation and returns it as `Optional` if the given hash is valid, otherwise `Optional.EMPTY`.

> The given hash **must resolve into a one number only**, otherwise the [`decode(String)`](#natural-numbers) public API method must be used.

```java
final Hashids hashids = new Hashids.Builder()
  .salt("salt and pepper")
  .build();

final String singleNumberHash = hashids.encode(123L);
final String multipleNumberHash = hashids.encode(42L, 5L, 17L);

final Optional<Long> number = hashids.decodeOne(singleNumberHash); // Optional[123]
final Optional<Long> empty = hashids.decodeOne(multipleNumberHash); // Optional.EMPTY
```


[algorithm-config]: configuration/index.md
[feature-exception-handling]: configuration/features.md#exception-handling
[salt]: configuration/index.md#using-a-salt
