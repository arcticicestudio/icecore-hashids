# Instances

Default configured `Hashids` instances can be created by using the `Hashids.Builder` default instance

```java
Hashids hashids = new Hashids.Builder().build();
```

or the default constructor

```java
Hashids hashids = new Hashids();
```

### Configuration

The `Hashids.Builder` instance provides methods to configure the algorithm features like the [entropy salt][guide-config-salt], the [minimum hash length][guide-config-min-hash-length] and the [customizable alphabet][guide-config-alphabet].

#### Default Values

Below are the default values of the instance configurations which can also be found in the [JavaDoc][javadoc-gh-pages].

* `Hashids.Builder#salt(String)` - By default the value of the salt is empty which will generate hashes without entropy
* `Hashids.Builder#minLength(int)` By default the minimum hash length is `0` which will generate hashes with the required length only depending on the given number(s)
* `Hashids.Builder#alphabet(String)` - The default alphabet is `abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890` which is defined in the `Hashids#DEFAULT_ALPHABET` constant

### Features

IceCore Hashids includes opt-in instance features to enable language specific advantages like

* [exception handling][guide-feature-exception-handling]
* disabling [algorithm reference implementation][hashids-js] interoperability limitations like the [maximum number size][guide-feature-no-max-number-size]
* allowing to [prefix hexadecimal numbers][guide-feature-allow-hexadecimal-prefix] with `0x` or `0X`

All public API instance features are represented by the `HashidsFeature` class located in the `com.arcticicestudio.icecore.hashids` package.

[guide-config-alphabet]: guide/configuration/index.md#determine-a-custom-alphabet
[guide-config-min-hash-length]: guide/configuration/index.md#defining-a-minimum-hash-length
[guide-config-salt]: guide/configuration/index.md#using-a-salt
[guide-feature-allow-hexadecimal-prefix]: guide/configuration/features.md#allow-exadecimal-number-prefixes
[guide-feature-exception-handling]: guide/configuration/features.md#exception-handling
[guide-feature-no-max-number-size]: guide/configuration/features.md#no-number-size-limit
[hashids-js]: https://github.com/ivanakimov/hashids.js
[javadoc-gh-pages]: https://arcticicestudio.github.io/icecore-hashids/javadoc
