IceCore - Hashids
=================

![Hashids logo][hashids-logo] ![Hashids text banner][hashids-text-banner]

## 0.2.0 (2016-06-11)
### Features
#### API
  - Implemented public API Builder methods:

| Class | Constructor | Description |
| ----- | ----------- | ----------- |
| `com.arcticicestudio.icecore.hashids.Hashids` | <u>`+ getVersion(String):Builder`</u> | Returns the [ArcVer][arcver-github]- and [SemVer][semver] compatible version. |

### Improvements
#### API
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

## 0.1.0 (2016-06-10) - Public API
### Features
#### API
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

#### Documentation
  - Added a usage guide for the API.
    This includes all currently implemented public API methods and general notes to the library algorithm.

### Tests
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
