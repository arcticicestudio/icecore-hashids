<p align="center"><img src="https://cdn.rawgit.com/arcticicestudio/icecore-hashids/develop/src/main/assets/icecore-hashids-logo-banner.svg"/></p>

<p align="center"><img src="https://cdn.travis-ci.org/images/favicon-c566132d45ab1a9bcae64d8d90e4378a.svg" width=24 height=24/> <a href="https://travis-ci.org/arcticicestudio/icecore-hashids"><img src="https://img.shields.io/travis/arcticicestudio/icecore-hashids/develop.svg?style=flat-square"/></a> <img src="https://circleci.com/favicon.ico" width=24 height=24/> <a href="https://circleci.com/gh/arcticicestudio/icecore-hashids"><img src="https://img.shields.io/circleci/project/github/arcticicestudio/icecore-hashids/develop.svg?style=flat-square"/></a> <img src="https://assets-cdn.github.com/favicon.ico" width=24 height=24/> <a href="https://github.com/arcticicestudio/icecore-hashids/releases/latest"><img src="https://img.shields.io/github/release/arcticicestudio/icecore-hashids.svg?style=flat-square"/></a> <img src="https://d234q63orb21db.cloudfront.net/685e381330164f79197bc0e7f75035c6f1b9d7d0/media/images/favicon.png" width=24 height=24/> <a href="https://codecov.io/gh/arcticicestudio/icecore-hashids"><img src="https://img.shields.io/codecov/c/github/arcticicestudio/icecore-hashids/develop.svg?style=flat-square"/></a></p>

<p align="center"><img src="http://central.sonatype.org/favicon.ico" width=24 height=24/> <a href="http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.arcticicestudio%22%20AND%20a%3A%22icecore-hashids%22"><img src="https://img.shields.io/maven-central/v/com.arcticicestudio/icecore-hashids.svg?style=flat-square"/></a> <img src="https://oss.sonatype.org/favicon.ico"/> <a href="https://oss.sonatype.org/content/repositories/snapshots/com/arcticicestudio/icecore-hashids"><img src="https://img.shields.io/badge/snapshot-_---blue.svg?style=flat-square"/></a> <img src="https://bintray.com/favicon.ico" width=24 height=24/> <a href="https://bintray.com/arcticicestudio/IceCore/icecore-hashids/_latestVersion"><img src="https://api.bintray.com/packages/arcticicestudio/IceCore/icecore-hashids/images/download.svg"></a> <a href="https://oss.jfrog.org/webapp/#/artifacts/browse/tree/General/oss-snapshot-local/com/arcticicestudio/icecore-hashids"><img src="https://img.shields.io/badge/artifactory-_---green.svg?style=flat-square"/></a> <img src="https://jitpack.io/favicon.ico"/> <a href="https://jitpack.io/#arcticicestudio/icecore-hashids"><img src="https://jitpack.io/v/arcticicestudio/icecore-hashids.svg?style=flat-square"></a></p>

<p align="center">A lightweight generator for short, unique, non-sequential and decodable Hashids from positive unsigned (long) integer numbers.</p>

<p align="center">Implementation of the <a href="http://hashids.org">Hashids</a> algorithm.</p>

---

[Hashids][hashids] are obfuscated unique hashes of positive unsigned numbers, but in contrast to cryptographic one-way hash algorithms they are can be decoded again. The algorithm can be used to either generate a hash from a single number or bundling several numbers into one to be stored as simple short UIDs. This design allows to use them for many use-cases like

* URL shortening
* database ID protection
* shard numbers storage
* invitation-, authorization- and gift codes
* complex- or clustered system parameters

Numbers like `347` are converted into strings like `yr8`, or an array of numbers like `[27, 986]` into `3kTMd`.

## Features

The **algorithm** provides the following features:

* Generation of short, unique, case-sensitive and non-sequential [decodable][api-guide-decoding] hashes of natural numbers
* Additional entropy through [salt][api-guide-config-salt] usage
* Configurable [minimum hash length][api-guide-config-min-hash-length] and [alphabet][api-guide-config-alphabet]
* Combining of [several numbers to one hash][api-guide-encoding]
* Deterministic hash computation given the same input and parametrization/[instance configuration][api-instances]
* [Prevention of curse words][api-curse-word-prevention] through separator characters

In addition, the **library** provides features to

* pass `0x` or `0X` [prefixed hexadecimal numbers][api-guide-config-feature-hex-prefix] to the public API methods
* [handle exceptions][api-guide-config-feature-exception-handling] instead of returning empty values when invalid parameters are passed to any public API method
* [disable the maximum number size limit][api-guide-config-feature-no-max-number-size] which ensures the interoperability with the [algorithm reference implementation][hashids-js] and allows the usage of the Java `Long` [maximum value][long-max-value]

> Please note that most features will break the interoperability with the [algorithm reference implementation][hashids-js]!

[api-curse-word-prevention]: api/curse-word-prevention.md
[api-guide-config-alphabet]: api/guide/configuration/index.md#determine-a-custom-alphabet
[api-guide-config-feature-exception-handling]: api/guide/configuration/features.md#exception-handling
[api-guide-config-feature-hex-prefix]: api/guide/configuration/features.md#allow-hexadecimal-number-prefixes
[api-guide-config-feature-no-max-number-size]: api/guide/configuration/features.md#no-number-size-limit
[api-guide-config-min-hash-length]: api/guide/configuration/index.md#defining-a-minimum-hash-length
[api-guide-config-salt]: api/guide/configuration/index.md#using-a-salt
[api-guide-decoding]: api/guide/decoding.md
[api-guide-encoding]: api/guide/encoding.md
[api-instances]: api/instances.md
[hashids]: http://hashids.org
[hashids-js]: https://github.com/ivanakimov/hashids.js
[long-max-value]: https://docs.oracle.com/javase/8/docs/api/java/lang/Long.html#MAX_VALUE
