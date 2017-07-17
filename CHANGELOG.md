<p align="center"><img src="https://cdn.rawgit.com/arcticicestudio/icecore-hashids/develop/src/main/assets/icecore-hashids-logo-banner.svg"/></p>

<p align="center"><img src="https://assets-cdn.github.com/favicon.ico" width=24 height=24/> <a href="https://github.com/arcticicestudio/icecore-hashids/releases/latest"><img src="https://img.shields.io/github/release/arcticicestudio/icecore-hashids.svg"/></a> <img src="http://central.sonatype.org/favicon.ico" width=24 height=24/> <a href="http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.arcticicestudio%22%20AND%20a%3A%22icecore-hashids%22"><img src="https://img.shields.io/maven-central/v/com.arcticicestudio/icecore-hashids.svg"/></a> <img src="https://bintray.com/favicon.ico" width=24 height=24/> <a href='https://bintray.com/arcticicestudio/IceCore/icecore-hashids/_latestVersion'><img src='https://api.bintray.com/packages/arcticicestudio/IceCore/icecore-hashids/images/download.svg'></a> <img src="https://jitpack.io/favicon.ico"/> <a href="https://jitpack.io/v/arcticicestudio/icecore-hashids.svg"><img src="https://jitpack.io/v/arcticicestudio/icecore-hashids.svg?style=flat-square"></a></p>

---

# 0.3.0
<details>
  <summary>Version Details</summary>
  <p>
    Release Date: 2017-07-17<br>
    <a href="https://github.com/arcticicestudio/icecore-hashids/milestone/3">Milestone</a><br>
    <a href="https://github.com/arcticicestudio/icecore-hashids/projects/4">Project Board</a>
  </p>
</details>

## Features
### API
❯ Implemented a non-parameter constructor as an equivalent to the default `Hashids.Builder` instance. The builder supports the creation of an Hashids instance without parameters for custom configurations, but there was no equivalent constructor for this case. (#1)

## Improvements
### API
#### Performance
##### Internal
❯ The private method `consistentShuffle(String,String)` now uses `char` arrays instead of `String` operations. This removes five additional `String` operations/methods and six String concatenations to three simple array assignments. (#7)

❯ Replaced `toCharArray(int)` methods with `charAt(int)` since `Strings` are implemented as array internally so  there is no need to convert to a char array.

❯ Adapted to SonarQube minor rule [squid:S3400](https://sonarcloud.io/coding_rules#q=squid%3AS3400). (#5)

❯ Adapted to SonarQube minor rule [squid:S3400](https://sonarcloud.io/coding_rules#q=squid%3AS1643) to improve the performance. (#6)

### Documentation
❯ Adapted to a new project setup and documentation style. (#2)

### Tests
❯ Slighly increased the code coverage by implementation unit tests for the `Hashid` *equals* symmetric. (#8)

## Bug Fixes
### API
❯ Fixed a `ArrayIndexOutOfBoundsException` when using a invalid decode salt.

### JavaDoc
❯ Fixed JavaDoc lint compilation errors for self-closed tags. (#3)

# 0.2.0 (2016-06-11)
## Features
### API
  - Implemented public API Builder methods:

| Class | Constructor | Description |
| ----- | ----------- | ----------- |
| `com.arcticicestudio.icecore.hashids.Hashids` | <u>`+ getVersion(String):Builder`</u> | Returns the [ArcVer][arcver-github]- and [SemVer][semver] compatible version. |

## Improvements
### API
  - Now using `StringBuilder` class for non-constant `String` attributes to avoid unnecessary `String` primitive-wrapping
  - No more boxing of primitive for `String` conversion only
    Make use of the static JDK method `+ String.valueOf(char):String` to avoid waste of memory and cycles by not using a primitive-wrapper with concatenating empty string `""` to a primitive.
    This change fulfills the [Eclipse SonarQube][sonarqube-eclipse] rule [`squid:S2131`][sonarqube-eclipse-rule-squid-s2131]:
      > Primitives should not be boxed just for `String` conversion

  - Changed private methods without access to instance data to be `static`
    Clarifies that these methods will not modify the state of the object.
    This change fulfills the [Eclipse SonarQube][sonarqube-eclipse] rule `squid:S2325`:
      > Private methods that don't access instance data should be static

  - Pre-compile regex `Pattern` pattern
    Improves the performance (less memory and CPU cycles) by using pre-compiled regex pattern instead of creating instances for each `Pattern` call.

# 0.1.0 (2016-06-10) - Public API
## Features
### API
  - Implemented public API classes:

| Class | Description |
| ----- | ----------- |
| `com.arcticicestudio.icecore.hashids.Hashids` | Generates short, unique, non-sequential and decodable hashids from positive unsigned (long) integer numbers. Serves as the entry point to the "IceCore - JSON" public API. |
| `com.arcticicestudio.icecore.hashids.Hashids.Builder` | An immutable and reusable Hashids builder. Each method returns a new builder instance. |
| `com.arcticicestudio.icecore.hashids.Hashid` | Represents a hashid which holds all numbers and the encoded string. |

  - Implemented public API constructors:

| Class | Constructor | Description |
| ----- | ----------- | ----------- |
| `com.arcticicestudio.icecore.hashids.Hashids` | `+ Hashids(String)` | - |
| `com.arcticicestudio.icecore.hashids.Hashids` | `+ Hashids(String,int)` | - |
| `com.arcticicestudio.icecore.hashids.Hashids` | `+ Hashids(String,int,String)` | - |
| `com.arcticicestudio.icecore.hashids.Hashids` | `+ Hashids(String,int,String,String)` | - |

  - Implemented public API Builder constructors:

| Class | Constructor | Description |
| ----- | ----------- | ----------- |
| `com.arcticicestudio.icecore.hashids.Hashids.Builder` | `+ Builder()` | - |

  - Implemented public API Builder methods:

| Class | Constructor | Description |
| ----- | ----------- | ----------- |
| `com.arcticicestudio.icecore.hashids.Hashids.Builder` | `+ salt(String):Builder` | - |
| `com.arcticicestudio.icecore.hashids.Hashids.Builder` | `+ alphabet(String):Builder` | - |
| `com.arcticicestudio.icecore.hashids.Hashids.Builder` | `+ minHashLength(int):Builder` | - |
| `com.arcticicestudio.icecore.hashids.Hashids.Builder` | `+ build(int):Hashids` | - |

  - Implemented public API methods:

| Class | Method | Description |
| ----- | ------ | ----------- |
| `com.arcticicestudio.icecore.hashids.Hashids` | `+ encode(long...):Hashid` | Encode number(s). |
| `com.arcticicestudio.icecore.hashids.Hashids` | `+ encodeToString(long...):String` | Encode number(s) to string. |
| `com.arcticicestudio.icecore.hashids.Hashids` | `+ encodeToString(int...):String` | Encode number(s) to string. |
| `com.arcticicestudio.icecore.hashids.Hashids` | `+ encodeHex(String):String` | Encode an hexadecimal string to string. |
| `com.arcticicestudio.icecore.hashids.Hashids` | `+ decode(String):Hashid` | Decode an encoded string. |
| `com.arcticicestudio.icecore.hashids.Hashids` | `+ decodeLongNumbers(String):long[]` | Decode an encoded string to long numbers. |
| `com.arcticicestudio.icecore.hashids.Hashids` | `+ decodeIntegerNumbers(String):int[]` | Decode an encoded string to integer numbers. |
| `com.arcticicestudio.icecore.hashids.Hashids` | `+ decodeHex(String):String` | Decode an string to hexadecimal numbers. |

  - Implemented public API static constants:

| Class | Constructor | Value | Description |
| ----- | ----------- | ----- | ----------- |
| `com.arcticicestudio.icecore.hashids.Hashids` | `+ DEFAULT_ALPHABET:String` | `abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890` | Holds the default alphabet. |
| `com.arcticicestudio.icecore.hashids.Hashids` | `+ DEFAULT_SEPARATORS:String` | `´cfhistuCFHISTU` | Holds the default separators. Used to prevent the generation of strings that contain bad, offensive or rude words. |
| `com.arcticicestudio.icecore.hashids.Hashids` | `+ MAX_NUMBER_VALUE:long` | `9_007_199_254_740_992L - 1` | Holds the maximum number value. This limit is mandatory in order to ensure interoperability. |

### Documentation
  - Added a usage guide for the API.
    This includes all currently implemented public API methods and general notes to the library algorithm.

## Tests
  - Implemented public API test classes:

| Class | Description |
| ----- | ----------- |
| `com.arcticicestudio.icecore.hashids.HashidsTest` | Tests the "IceCore - Hashids" public API class `Hashids`. |

## 0.0.0 (2016-06-04) - Project Initialization

[hashids-text-banner]: https://camo.githubusercontent.com/1c71719d94fca1132cd4570f7e54f9aedfb27ba0/687474703a2f2f686173686964732e6f72672f7075626c69632f696d672f686173686964732d6c6f676f2d6e6f726d616c2e706e67
[hashids-logo]: https://avatars1.githubusercontent.com/u/8481000
[sonarqube-eclipse]: https://dev.eclipse.org/sonar
[sonarqube-eclipse-rule-squid-s2131]: https://dev.eclipse.org/sonar/rules/show/squid:S2131
[sonarqube-eclipse-rule-squid-s2325]: https://dev.eclipse.org/sonar/rules/show/squid:S232
[arcver-github]: https://github.com/arcticicestudio/arcver
[semver]: http://semver.org
