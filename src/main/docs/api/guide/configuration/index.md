# Configuration

Instances are configured by using the `Hashids.Builder` which can be simply imported statically as described in the [imports][api-overview-imports] chapter.

All configurations are compatible and can be combined.

* [`salt(String)`](#using-a-salt) - Sets the salt to be used as entropy
* [`minLength(int)`](#defining-a-minimum-hash-length) - Sets the minimum hash length
* [`alphabet(String)`](#determine-a-custom-alphabet) - Sets the alphabet to be used for the hash generation
* [`features(HashidsFeature...)`][guide-config-features] - Enables the given instance feature

## Using A Salt

A salt adds additional entropy to the hash generation and can be used to increase the protection against unintentional hash decoding by third parties.

```java
final Hashids hashids = new Hashids.Builder()
  .salt("salt and pepper")
  .build();

final String hash = hashids.encode(1234567L); // Result: "BbVrQ"
```

## Defining A Minimum Hash Length

This configuration allows to determine the *minimum* length of the generated hash. By default the length is set to `0` which will generate hashes with the required length only depending on the given number(s).

```java
final Hashids hashids = new Hashids.Builder()
  .minHashLength(8)
  .build();

final String hash = hashids.encode(42L); // Result: "WPe9xdLy"
```

## Determine A Custom Alphabet

The alphabet for the hash generation can be customized but **must contain at least 16 unique characters**.

```java
final Hashids hashids = new Hashids.Builder()
  .alphabet("abcdefgh12345678")
  .build();

final String hash = hashids.encode(1234567L); // Result: "edd6185"
```

[api-overview-imports]: ../../imports.md
[guide-config-features]: features.md
