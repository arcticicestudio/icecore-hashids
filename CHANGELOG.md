<p align="center"><img src="https://cdn.rawgit.com/arcticicestudio/icecore-hashids/develop/src/main/assets/icecore-hashids-logo-banner.svg"/></p>

<p align="center"><img src="https://assets-cdn.github.com/favicon.ico" width=24 height=24/> <a href="https://github.com/arcticicestudio/icecore-hashids/releases/latest"><img src="https://img.shields.io/github/release/arcticicestudio/icecore-hashids.svg"/></a> <img src="http://central.sonatype.org/favicon.ico" width=24 height=24/> <a href="http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.arcticicestudio%22%20AND%20a%3A%22icecore-hashids%22"><img src="https://img.shields.io/maven-central/v/com.arcticicestudio/icecore-hashids.svg"/></a> <img src="https://bintray.com/favicon.ico" width=24 height=24/> <a href='https://bintray.com/arcticicestudio/IceCore/icecore-hashids/_latestVersion'><img src='https://api.bintray.com/packages/arcticicestudio/IceCore/icecore-hashids/images/download.svg'></a> <img src="https://jitpack.io/favicon.ico"/> <a href="https://jitpack.io/v/arcticicestudio/icecore-hashids.svg"><img src="https://jitpack.io/v/arcticicestudio/icecore-hashids.svg?style=flat-square"></a></p>

<p align="center"><a href="https://arcticicestudio.github.io/icecore-hashids"><img src="https://img.shields.io/badge/docs-0.4.0-81A1C1.svg?style=flat-square"/></a> <a href="https://arcticicestudio.github.io/icecore-hashids/javadoc"><img src="https://img.shields.io/badge/JavaDoc-0.4.0-81A1C1.svg?style=flat-square"/></a></p>

---

# 0.4.0
![Release Date: 2017-08-13](https://img.shields.io/badge/Release_Date-2017--08--13-88C0D0.svg?style=flat-square) [![Project Board](https://img.shields.io/badge/Project_Board-0.4.0-88C0D0.svg?style=flat-square)](https://github.com/arcticicestudio/icecore-hashids/projects/5) [![Milestone](https://img.shields.io/badge/Milestone-0.4.0-88C0D0.svg?style=flat-square)](https://github.com/arcticicestudio/icecore-hashids/milestone/4) [![docs](https://img.shields.io/badge/docs-0.4.0-81A1C1.svg?style=flat-square)][docs] [![JavaDoc](https://img.shields.io/badge/JavaDoc-0.4.0-81A1C1.svg?style=flat-square)][javadoc]

This version represents a large milestone which is a revise of the whole project to improve the clarity, performance and stability.

> Detailed information about each new API feature- and change can be found in the [project documentation][docs].

## Features
### API

❯ The public- and internal APIs have been [redesigned](https://github.com/arcticicestudio/icecore-hashids/issues/9) and rewritten from scratch regarding optimizations for Java 8, reduction of complexity and the new code style guide conventions.

❯ The new `com.arcticicestudio.icecore.hashids.HashidsFeature` enum contains constants for various [features][docs-features] which can be enabled per instance through the `Hashids.Builder` method `features(HashidFeature) : Builder`.

❯ The public API now also provides the `decodeOne(String) : Optional<Long>` method to simplify the use-case where the amount of resulting numbers is known before to handle the return value as single value instead of an array. This improves the prevention of an `ArrayIndexOutOfBounceException` when users try to access an non-existent array index due to an failed encoding- or decoding.

#### Public API Breaking Changes

❯ The obsolete class `com.arcticicestudio.icecore.hashids.Hashid` has been removed which was previously added to simplify the binding handling of the numbers and their hash value, but users should implement the simple POJO on their own instead of forcing them to use this approach.

The public API methods now returning the primitive values which have been hold by the `Hashid` class. (#9 closed by PR #22, 6d5b21f4)

❯ Due to the removement of the `Hashid` class some public API methods have become unnecessary and have also been removed. (#9 closed by PR #22, 8e21e96a)

* `decodeLongNumbers(String) : long[]` - The removed `Hashid` class has been used as a kind of container to store both the numbers and their associated hash values. This method allowed to directly receive the `Long` numbers instead of the `Hashid` POJO which is now be provided by the `decode(String) : long[]` method.
* `decodeIntegerNumbers(String) : int[]` - The algorithm is designed for `Long` numbers. This method has been implemented to provide an easy conversion interface to use the library with integer IDs which should now be handled by the user.
* `encodeToString(long...) : String` and `encodeToString(int...) : String` - These methods are the counterparts for the decoding logic methods described above. Both have been removed for the same reason and the functionality is now available through the `encode(String) : long[]` method.

❯ Removed all overloaded constructors due to the already provided interfaces through the `Hashids.Builder` class. It provides methods for a accurate instance configuration while an instance with the default interoperable configurations is still available through the default constructor or by using default constructed builder instance. It allows a finer grained instance building since it doesn't depends on parameter ordering. The default constructor is still available for brevity and the functionality of all other removed constructors can now be achieved with the builder. (#9 closed by PR #22, 40231ad1)

❯ The `Hashids.Builder` method `minHashLength(int) : Builder` has been renamed to `minLength(int) : Builder` to adapt to the the builder methods naming style. (#9 closed by PR #22, 8617f4c2)

❯ Separators are not customizable anymore since the curse word protection of the reference algorithm implementation does
not allow to modify the separators. The API provided methods to change those separators causing invalid and inconsistent hash- and numbers values. (#9 closed by PR #22, 1a082681)

#### Internal API Changes

❯ The private main logic methods `doEncode(long...)` and `doDecode(String, String)` have been removed and rewritten from scratch into the associated public API methods. (#9 closed by PR #22)

❯ The private method `consistentShuffle(String, String) : String` has been rewritten from scratch and renamed to `shuffle(char[], char[]) : char[]`. (#9 closed by PR #22, 8617f4c2)

❯ The private methods `hash(long, String)` and `unhash(String, String)` have been rewritten from scratch and renamed to `transform(long, char[], StringBuilder, int)` and `transform(char[], char"])`. (#9 closed by PR #22, 8617f4c2)

❯ The primary algorithm logic, which was mainly implemented in the private methods `doEncode` and `doDecode`, has been rewritten from scratch and modularized into the new methods `deriveNewAlphabet(char[], char[], char) : char[]` and `filterSeparators(char[], char[]) : char[]`. (#9 closed by PR #22, 8617f4c2)

❯ The private support methods `toArray(List<Long>) : long[]` and `isEmpty(String) : boolean` have been removed. (#9 closed by PR #22, 8617f4c2)

### Documentation

❯ The new [project documentation][docs] includes detailed information on how to [get started][docs-get-started], the new [API overview][docs-api] and [API guide][docs-api-guide] and how to [build][docs-dev-build] the project and [run the tests][docs-dev-test]. (#11 closed by PR #23)

❯ Next to the project documentation the new [GitHub Open Source community standards](https://github.com/arcticicestudio/icecore-hashids/issues/12) have been added to complete the project's [community profile][community-profile] and adapt to the [Open Source Guides][open-source-guides] consisting of the [Contributing Guidelines][docs-contrib-guide] and the [Code of Conduct][docs-code-of-conduct]. (#12 closed by PR #20)

❯ The new [GitHub templates](https://github.com/arcticicestudio/icecore-hashids/issues/13) providing support for contributors to [create issues][issue-template] and [submitting pull requests][pr-template]. (#13 closed by PR #19)

❯ To improve the code review process the new [GitHub code owner](https://github.com/arcticicestudio/icecore-hashids/issues/14) feature has been adapted. (#14 closed by PR #15)

### Tests

All unit tests have been rewritten from scratch to match the new API and include as much use-cases as possible to increase the code coverage. The new interoperability tests run against the matching version of the reference implementation [hashids.js][hashids-js]. The script is loaded by using the [maven-frontend-plugin][maven-frontend-plugin] which installs [NodeJS][nodejs] locally and runs [NPM][npm] to install all dependencies defined in the `package.json`. The parameterized `InteropHashidsTest` class compares the results of both
algorithms to ensure interoperability. (#9 closed by PR #22, 8617f4c2)

> Detailed information about the functionality and how to run the unit- and interoperability tests can be found in the [project documentation][docs-dev-test].

## Improvements

❯ The POM has been refactored to

* update all plugin- and dependency versions
* remove the unnecessary comment documentation header
* fix the copyright year for generated JavaDoc
* add the `maven-enforcer-plugin`
* remove unused profiles and configured core plugins
* remove unused test dependencies

(#17 closed by PR #21)

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

[community-profile]: https://github.com/arcticicestudio/icecore-hashids/community
[docs]: https://arcticicestudio.github.io/icecore-hashids
[docs-api]: https://arcticicestudio.github.io/icecore-hashids/api
[docs-api-guide]: https://arcticicestudio.github.io/icecore-hashids/api/guide
[docs-code-of-conduct]: https://arcticicestudio.github.io/icecore-hashids/development/code-of-conduct.html
[docs-contrib-guide]: https://arcticicestudio.github.io/icecore-hashids/development/contributing.html
[docs-dev-build]: https://arcticicestudio.github.io/icecore-hashids/development/building.html
[docs-dev-test]: https://arcticicestudio.github.io/icecore-hashids/development/testing.html
[docs-features]: https://arcticicestudio.github.io/icecore-hashids/#features
[docs-get-started]: https://arcticicestudio.github.io/icecore-hashids/getting-started/requirements.html
[hashids-js]: https://github.com/ivanakimov/hashids.js
[issue-template]: https://github.com/arcticicestudio/icecore-hashids/blob/develop/.github/ISSUE_TEMPLATE.md
[javadoc]: https://arcticicestudio.github.io/icecore-hashids/javadoc
[maven-frontend-plugin]: https://github.com/eirslett/frontend-maven-plugin
[nodejs]: https://nodejs.org
[npm]: https://npmjs.com
[open-source-guides]: https://opensource.guide
[pr-template]: https://github.com/arcticicestudio/icecore-hashids/blob/develop/.github/PULL_REQUEST_TEMPLATE.md
[sonarqube-eclipse]: https://dev.eclipse.org/sonar
[sonarqube-eclipse-rule-squid-s2131]: https://dev.eclipse.org/sonar/rules/show/squid:S2131
[sonarqube-eclipse-rule-squid-s2325]: https://dev.eclipse.org/sonar/rules/show/squid:S232
