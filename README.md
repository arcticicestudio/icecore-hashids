IceCore - Hashids
=================

![Hashids logo][hashids-logo] ![Hashids text banner][hashids-text-banner]

Lightweight module library as part of the [IceCore](https://bitbucket.org/arcticicestudio/icecore) engine to generate short, unique, non-sequential and decodable Hashids from positive unsigned (long) integer numbers.

This is a implementation of the [Hashids](http://hashids.org) project.

It was designed for websites to use in URL shortening, tracking stuff, or making pages private or at least unguessable.
Can be used to create e.g obfuscated database ids, invitation codes, store shard numbers and much more.

It converts numbers like `347` into strings like "yr8", or array of numbers like `[27, 986]` into "3kTMd".
You can also decode those ids back.
This is useful in bundling several parameters into one or simply using them as short UIDs.

This algorithm tries to satisfy the following requirements:
  1. Hashids must be unique and decodable
  2. Hashids should be able to contain more than one integer to use them in complex or clustered systems
  3. Ability to specify minimum hash length
  4. Hashids should not contain basic English curse words since they are meant to appear in public places like a URL

**NOTE**: All (long) integers must be greater than or equal to zero.

Usage Guide
-----------
This is a basic guide to show the common usage of the icecore-hashids API.
The API documentation can be found in the JavaDoc.

The class `Hashids` is the entrypoint to the [icecore-hashids][bitbucket-icecore-hashids] API.

1. [Encoding a number](#encoding-a-number)
2. [Decoding](#decoding)
3. [Encoding several numbers](#encoding-several-numbers)
4. [Decoding a hash of several numbers](#decoding-a-hash-of-several-numbers)
5. [Encoding with a specified minimum hash length](#encoding-with-a-specified-minimum-hash-length)
6. [Specify a custom hash alphabet](#specify-a-custom-hash-alphabet)
7. [Specify custom separators](#specify-custom-separators)
8. [Randomness](#randomness)
  8.1 [Repeating numbers](#repeating-numbers)
9. [Bad or offensive hashes](#bad-or-offensive-hashes)

#### Encoding a number
You can pass a unique salt value to create a individual hash:
```java
Hashids hashids = new Hashids("salt");
String hash = hashids.encodeToString(12345L);
```
`hash` will be `X4j1`.

Via the `Builder`:
```java
Hashids hashids = new Hashids.Builder()
  .salt("salt")
  .build();
String hash = hashids.encodeToString(12345L);
```

Supported number types are `long` and `int`.

#### Decoding
**NOTE**: Make sure to use the same salt during decoding!**
```java
Hashids hashids = new Hashids("salt");
long[] numbers = hashids.decodeLongNumbers("X4j1");
```
`numbers` will be `[ 12345 ]`.

Via the `Builder`:
```java
Hashids hashids = new Hashids.Builder()
  .salt("salt")
  .build();
long[] numbers = hashids.decodeLongNumbers("X4j1");
```

Decoding will not work if salt is changed:
```java
Hashids hashids = new Hashids("salt and pepper");
long[] numbers = hashids.decodeLongNumbers("X4j1");
```
**This will throw a `IllegalArgumentException.class` exception!**

#### Encoding several numbers
```java
Hashids hashids = new Hashids("salt");
String hash = hashids.encodeToString(683L, 94108L, 123L, 5L);
```
`hash` will be `1eMToyKzsRAfO`.

#### Decoding a hash of several numbers
```java
Hashids hashids = new Hashids("salt");
long[] numbers = hashids.decodeLongNumbers("1eMToyKzsRAfO");
```
`numbers` will be `[ 683, 94108, 123, 5 ]`.

#### Encoding with a specified minimum hash length
You can set the minimum hash length by passing it to the constructor or the `Builder` method `minHashLength(int)`.
The default value is `0` to generate hashes with the shortest possible length.
```java
Hashids hashids = new Hashids("salt", 8);
String hash = hashids.encodeToString(1L);
```
`hash` will be `zxkXG8ZW`.

Via the `Builder`:
```java
Hashids hashids = new Hashids.Builder()
  .salt("salt")
  .minHashLength(8)
  .build();
String hash = hashids.encodeToString(1L);
```

#### Specify a custom hash alphabet
You can also set a custom alphabet by passing it to the constructor or the `Builder` method `alphabet(String)`.
```java
Hashids hashids = new Hashids("salt", "0123456789abcdef");
String hash = hashids.encodeToString(1234567L);
```
`hash` will be `e884ade`.

Via the `Builder`:
```java
Hashids hashids = new Hashids.Builder()
  .salt("salt")
  .alphabet("0123456789abcdef")
  .build();
String hash = hashids.encodeToString(1234567L);
```

#### Specify custom separators
You can set custom separators by passing it to the constructor or the `Builder` method `separators(String)`.
```java
Hashids hashids = new Hashids("salt", 0, Hashids.DEFAULT_ALPHABET, "aeiosuyzAEIOSUYZ0179");
String hash = hashids.encodeToString(12345L);
```
`hash` will be `VFKk`.

Via the `Builder`:
```java
Hashids hashids = new Hashids.Builder()
  .salt("salt")
  .separators("aeiosuyzAEIOSUYZ0179")
  .build();
String hash = hashids.encodeToString(12345L);
```

Separators try to prevent accidentally generated bad or offensive hashes.
Read the section [9. Bad or offensive hashes](#bad-or-offensive-hashes) for more information.

#### Randomness
The primary purpose of hashids is to obfuscate ids.
This algorithm does try to make these hashes unguessable and unpredictable.
**Note that it is not meant to be used for security purposes or compression!**

##### Repeating numbers
```java
Hashids hashids = new Hashids("salt");
String hash = hashids.encodeToString(5L, 5L, 5L, 5L);
```
`hash` is going to be `YBF7FKFz`.

You don't see any repeating patterns that might show there's 4 identical numbers in the hash:
Same with incremented numbers:
```java
Hashids hashids = new Hashids("salt");
String hash = hashids.encodeToString(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);
```
`hash` will be `rjiJulUECaFAS1TBhzcX`.

You can compare this result by encoding every number on its own:
```java
Hashids hashids = new Hashids("this is my salt");
String hash1 = hashids.encodeToString(1L); /* XG */
String hash2 = hashids.encodeToString(2L); /* dv */
String hash3 = hashids.encodeToString(3L); /* w2 */
String hash4 = hashids.encodeToString(4L); /* nO */
String hash5 = hashids.encodeToString(5L); /* q2 */
//...
```

#### Bad or offensive hashes
This module was written with the intent of placing these hashes in visible places like the URL.
It is possible that the created hash ends up accidentally being a bad or offensice word.

Therefore, this algorithm tries to avoid generating most common english curse words with the default alphabet.
This is done by never placing the following letters next to each other:
`c, C, s, S, f, F, h, H, u, U, i, I, t, T`

### Version
[`0.0.0`](https://bitbucket.org/arcticicestudio/icecore-hashids/downloads)  

For older versions check out the private [Bitbucket Git Repository](https://bitbucket.org/arcticicestudio/icecore-hashids).

### Changelog
[`0.0.0`](CHANGELOG.md)

### Development
#### Workflow
This project follows the [gitflow](http://nvie.com/posts/a-successful-git-branching-model) branching model.

#### Specifications
This project follows the [Arctic Versioning Specification (ArcVer)](https://github.com/arcticicestudio/arcver).

### Dependencies
#### Development
  - [`junit@4.12.0`](http://junit.org)
  - [`hashids-java@1.0.0`](https://github.com/jiecao-fm/hashids-java)
  - [`hamcrest-core@1.3.0`](http://hamcrest.org)

**Engines**
  - [`ant@1.9.6`](http://ant.apache.org)
  - [`git@2.8.*`](https://git-scm.com)
  - [`odin@0.1.0`](~/yggdrasil/Odin)

**Skeletons**
  - [`glacier-apache-ant@0.11.0`](https://github.com/arcticicestudio/glacier-apache-ant)
  - [`glacier-git@0.23.0`](https://github.com/arcticicestudio/glacier-git)

### Contribution
Please report issues/bugs, suggestions for improvements and feature requests to the [issuetracker](https://bitbucket.org/arcticicestudio/icecore-hashids/issues) or via [email](mailto:bugs@arcticicestudio.com)

### Author
The [IceCore](https://bitbucket.org/arcticicestudio/icecore) engine project and [IceCore - Hashids](https://bitbucket.org/arcticicestudio/icecore-hashids) module is developed and authored by [Arctic Ice Studio](http://arcticicestudio.com).

### Copyright
<a href="mailto:development@arcticicestudio.com"><img src="http://www.arcticicestudio.com/assets/content/image/ais-logo.png" width=48 height=48 alt="Arctic Ice Studio Logo"/> Copyright &copy; 2016 Arctic Ice Studio</a>

---

### References
  - [IceCore](https://bitbucket.org/arcticicestudio/icecore-hashids)
  - [IceCore - Core](https://bitbucket.org/arcticicestudio/icecore-core)
  - [Hashids](http://hashids.org)
  - [ArcVer](https://github.com/arcticicestudio/arcver)
  - [gitflow](http://nvie.com/posts/a-successful-git-branching-model)
  - [Glacier - Apache Ant](https://github.com/arcticicestudio/glacier-apache-ant)
  - [Glacier - Git](https://github.com/arcticicestudio/glacier-git)

[hashids-text-banner]: https://camo.githubusercontent.com/1c71719d94fca1132cd4570f7e54f9aedfb27ba0/687474703a2f2f686173686964732e6f72672f7075626c69632f696d672f686173686964732d6c6f676f2d6e6f726d616c2e706e67
[hashids-logo]: https://avatars1.githubusercontent.com/u/8481000
[bitbucket-icecore-hashids]: https://bitbucket.org/arcticicestudio/icecore-hashids
