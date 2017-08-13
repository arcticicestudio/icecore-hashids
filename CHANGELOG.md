<p align="center"><img src="https://cdn.rawgit.com/arcticicestudio/icecore-hashids/develop/src/main/assets/icecore-hashids-logo-banner.svg"/></p>

<p align="center"><img src="https://assets-cdn.github.com/favicon.ico" width=24 height=24/> <a href="https://github.com/arcticicestudio/icecore-hashids/releases/latest"><img src="https://img.shields.io/github/release/arcticicestudio/icecore-hashids.svg"/></a> <a href="https://arcticicestudio.github.io/icecore-hashids"><img src="https://img.shields.io/badge/docs-0.3.0-81A1C1.svg?style=flat-square"/></a> <a href="https://arcticicestudio.github.io/icecore-hashids/javadoc"><img src="https://img.shields.io/badge/JavaDoc-0.3.0-81A1C1.svg?style=flat-square"/></a> <img src="http://central.sonatype.org/favicon.ico" width=24 height=24/> <a href="http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.arcticicestudio%22%20AND%20a%3A%22icecore-hashids%22"><img src="https://img.shields.io/maven-central/v/com.arcticicestudio/icecore-hashids.svg"/></a> <img src="https://bintray.com/favicon.ico" width=24 height=24/> <a href='https://bintray.com/arcticicestudio/IceCore/icecore-hashids/_latestVersion'><img src='https://api.bintray.com/packages/arcticicestudio/IceCore/icecore-hashids/images/download.svg'></a> <img src="https://jitpack.io/favicon.ico"/> <a href="https://jitpack.io/v/arcticicestudio/icecore-hashids.svg"><img src="https://jitpack.io/v/arcticicestudio/icecore-hashids.svg?style=flat-square"></a></p>

---

# 0.3.0
![Release Date: 2017-07-17](https://img.shields.io/badge/Release_Date-2017--07--17-88C0D0.svg?style=flat-square) [![Project Board](https://img.shields.io/badge/Project_Board-0.3.0-88C0D0.svg?style=flat-square)](https://github.com/arcticicestudio/icecore-hashids/projects/4) [![Milestone](https://img.shields.io/badge/Milestone-0.3.0-88C0D0.svg?style=flat-square)](https://github.com/arcticicestudio/icecore-hashids/milestone/3) [![docs](https://img.shields.io/badge/docs-0.3.0-81A1C1.svg?style=flat-square)][docs] [![JavaDoc](https://img.shields.io/badge/JavaDoc-0.3.0-81A1C1.svg?style=flat-square)][javadoc]

## Features
### API

❯ Implemented a non-parameter constructor as an equivalent to the default `Hashids.Builder` instance. The builder supports the creation of an Hashids instance without parameters for custom configurations, but there was no equivalent constructor for this case. (#1)

## Improvements
### API
#### Performance (Internal)

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

# 0.2.0
![Release Date: 2016-06-11](https://img.shields.io/badge/Release_Date-2016--06--11-88C0D0.svg?style=flat-square) [![Project Board](https://img.shields.io/badge/Project_Board-0.2.0-88C0D0.svg?style=flat-square)](https://github.com/arcticicestudio/icecore-hashids/projects/3) [![Milestone](https://img.shields.io/badge/Milestone-0.2.0-88C0D0.svg?style=flat-square)](https://github.com/arcticicestudio/icecore-hashids/milestone/2)

## Features
### API

❯ Implemented the public API method `getVersion(String)` to get the version.

## Improvements
### API

❯ Now using `StringBuilder` for non-constant `String` attributes to avoid unnecessary `String` primitive-wrapping

❯ No more boxing of primitive for `String` conversion only. Make use of the static JDK method `+ String.valueOf(char):String` to avoid waste of memory and cycles by not using a primitive-wrapper with concatenating empty string `""` to a primitive. This change fulfills the [Eclipse SonarQube][sonarqube-eclipse] rule [`squid:S2131`][sonarqube-eclipse-rule-squid-s2131]:

> Primitives should not be boxed just for `String` conversion

❯ Changed private methods without access to instance data to be `static`. Clarifies that these methods will not modify the state of the object. This change fulfills the [Eclipse SonarQube][sonarqube-eclipse] rule `squid:S2325`:

> Private methods that don't access instance data should be static

❯ Pre-compile regex `Pattern` pattern. Improves the performance (less memory and CPU cycles) by using pre-compiled regex pattern instead of creating instances for each `Pattern` call.

# 0.1.0
![Release Date: 2016-06-10](https://img.shields.io/badge/Release_Date-2016--06--10-88C0D0.svg?style=flat-square) [![Project Board](https://img.shields.io/badge/Project_Board-0.1.0-88C0D0.svg?style=flat-square)](https://github.com/arcticicestudio/icecore-hashids/projects/2) [![Milestone](https://img.shields.io/badge/Milestone-0.1.0-88C0D0.svg?style=flat-square)](https://github.com/arcticicestudio/icecore-hashids/milestone/1)

## Features
### API

❯ Implemented the public API. The `Hashids.Builder` class configures `Hashids` default instances. To create instances with custom configurations either the parameterized constructors can be used or by using the instance builder methods

* `salt(String)`
* `alphabet(String)`
* `minHashLength(int)`
* `build(int)`

To encode numbers and decode hashes the following public API methods have been implemented:

* `encode(long...) : Hashid` - Encode number(s)
* `encodeToString(long...):String` - Encode number(s) to string. |
* `encodeToString(int...):String` | Encode number(s) to string. |
* `encodeHex(String):String` | Encode an hexadecimal string to string. |
* `decode(String):Hashid` | Decode an encoded string. |
* `decodeLongNumbers(String):long[]` | Decode an encoded string to long numbers. |
* `decodeIntegerNumbers(String):int[]` | Decode an encoded string to integer numbers. |
* `decodeHex(String):String` | Decode an string to hexadecimal numbers. |

### Documentation

❯ Added a usage guide for the API. This includes all currently implemented public API methods and general notes to the library algorithm.

## Tests

❯ Implemented unit tests for the public- and internal API.

## Project Initialization

Date: *2016-06-04*

[docs]: https://arcticicestudio.github.io/icecore-hashids
[javadoc]: https://arcticicestudio.github.io/icecore-hashids/javadoc
[sonarqube-eclipse]: https://dev.eclipse.org/sonar
[sonarqube-eclipse-rule-squid-s2131]: https://dev.eclipse.org/sonar/rules/show/squid:S2131
[sonarqube-eclipse-rule-squid-s2325]: https://dev.eclipse.org/sonar/rules/show/squid:S232
