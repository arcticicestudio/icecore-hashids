<p align="center"><img src="https://cdn.rawgit.com/arcticicestudio/icecore-hashids/develop/src/main/assets/icecore-hashids-logo-banner.svg"/></p>

<p align="center"><img src="https://assets-cdn.github.com/favicon.ico" width=24 height=24/> <a href="https://github.com/arcticicestudio/icecore-hashids/releases/latest"><img src="https://img.shields.io/github/release/arcticicestudio/icecore-hashids.svg?style=flat-square"/></a> <img src="http://central.sonatype.org/favicon.ico" width=24 height=24/> <a href="http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.arcticicestudio%22%20AND%20a%3A%22icecore-hashids%22"><img src="https://img.shields.io/maven-central/v/com.arcticicestudio/icecore-hashids.svg?style=flat-square"/></a> <img src="https://oss.sonatype.org/favicon.ico"/> <a href="https://oss.sonatype.org/content/repositories/snapshots/com/arcticicestudio/icecore-hashids"><img src="https://img.shields.io/badge/snapshot-_---blue.svg?style=flat-square"/></a> <img src="https://bintray.com/favicon.ico" width=24 height=24/> <a href="https://bintray.com/arcticicestudio/IceCore/icecore-hashids/_latestVersion"><img src="https://api.bintray.com/packages/arcticicestudio/IceCore/icecore-hashids/images/download.svg"></a> <a href="https://oss.jfrog.org/webapp/#/artifacts/browse/tree/General/oss-snapshot-local/com/arcticicestudio/icecore-hashids"><img src="https://img.shields.io/badge/artifactory-_---43A047.svg?style=flat-square"/></a> <img src="https://jitpack.io/favicon.ico"/> <a href="https://jitpack.io/#arcticicestudio/icecore-hashids"><img src="https://jitpack.io/v/arcticicestudio/icecore-hashids.svg?style=flat-square"></a></p>

<p align="center"><img src="https://cdn.travis-ci.org/images/favicon-c566132d45ab1a9bcae64d8d90e4378a.svg" width=24 height=24/> <a href="https://travis-ci.org/arcticicestudio/icecore-hashids"><img src="https://img.shields.io/travis/arcticicestudio/icecore-hashids/develop.svg?style=flat-square"/></a> <img src="https://circleci.com/favicon.ico" width=24 height=24/> <a href="https://circleci.com/gh/arcticicestudio/icecore-hashids"><img src="https://img.shields.io/circleci/project/github/arcticicestudio/icecore-hashids/develop.svg?style=flat-square"/></a> <img src="https://d234q63orb21db.cloudfront.net/685e381330164f79197bc0e7f75035c6f1b9d7d0/media/images/favicon.png" width=24 height=24/> <a href="https://codecov.io/gh/arcticicestudio/icecore-hashids"><img src="https://img.shields.io/codecov/c/github/arcticicestudio/icecore-hashids/develop.svg?style=flat-square"/></a></p>

<p align="center"><a href="https://arcticicestudio.github.io/icecore-hashids"><img src="https://img.shields.io/badge/docs-0.4.0-81A1C1.svg?style=flat-square"/></a> <a href="https://arcticicestudio.github.io/icecore-hashids/javadoc"><img src="https://img.shields.io/badge/JavaDoc-0.4.0-81A1C1.svg?style=flat-square"/></a> <a href="https://github.com/arcticicestudio/icecore-hashids/blob/v0.4.0/CHANGELOG.md"><img src="https://img.shields.io/badge/Changelog-0.4.0-81A1C1.svg?style=flat-square"/></a></p>

---

<p align="center">A lightweight generator for short, unique, non-sequential and decodable Hashids from non-negative numbers.</p>

<p align="center">Implementation of the <a href="http://hashids.org">Hashids</a> algorithm.</p>

[Hashids][hashids] are obfuscated unique hashes of non-negative (long) integer numbers, but in contrast to cryptographic one-way hash algorithms they are can be decoded again. The algorithm can be used to either generate a hash from a single number or bundling several numbers into one to be stored as simple short UIDs. This design allows to use them for many use-cases like

* URL shortening
* database ID protection
* shard numbers storage
* invitation-, authorization- and gift codes
* complex- or clustered system parameters

Numbers like `347` are converted into strings like `yr8`, or an array of numbers like `[27, 986]` into `3kTMd`.

## Features

The **algorithm** provides the following features:

* Generation of short, unique, case-sensitive and non-sequential [decodable][docs-api-guide-decoding] hashes of natural numbers
* Additional entropy through [salt][docs-api-guide-config-salt] usage
* Configurable [minimum hash length][docs-api-guide-config-min-hash-length] and [alphabet][docs-api-guide-config-alphabet]
* Combining of [several numbers to one hash][docs-api-guide-encoding]
* Deterministic hash computation given the same input and parametrization/[instance configuration][docs-api-instances]
* [Prevention of curse words][docs-api-curse-word-prevention] through separator characters

In addition, the **library** provides features to

* pass `0x` or `0X` [prefixed hexadecimal numbers][docs-api-guide-config-feature-hex-prefix] to the public API methods
* [handle exceptions][docs-api-guide-config-feature-exception-handling] instead of returning empty values when invalid parameters are passed to any public API method
* [disable the maximum number size limit][docs-api-guide-config-feature-no-max-number-size] which ensures the interoperability with the [algorithm reference implementation][hashids-js] and allows the usage of the Java `Long` [maximum value][long-max-value]

> Please note that most features will break the interoperability with the [algorithm reference implementation][hashids-js]!


## Getting Started

The [project documentation][docs] contains chapters to learn about the [installation][docs-getting-started-installation] and [requirements][docs-getting-started-requirements], get an [overview][docs-api] of the API and [learn how to use it][docs-api-guide], and [build][docs-dev-building] the project and [running the tests][docs-dev-testing].

## Contributing

Read the [contributing guide][docs-dev-contributing] to learn about the development process and how to propose [enhancement suggestions][docs-dev-contributing-enhancements] and [report bugs][docs-dev-contributing-bug-reports], how to [submit pull requests][docs-dev-contributing-pr] and the project's [styleguides][docs-dev-contributing-styleguides], [branch organization][docs-dev-contributing-branch-org] and [versioning][docs-dev-contributing-versioning] model.

The guide also includes information about [minimal, complete, and verifiable examples][docs-dev-contributing-mcve] and other ways to contribute to the project like [improving existing issues][docs-dev-contributing-other-improve-issues] and [giving feedback on issues and pull requests][docs-dev-contributing-other-feedback].

---

<p align="center">Copyright &copy; 2016-present Arctic Ice Studio</p>

<p align="center"><a href="https://github.com/arcticicestudio/icecore-hashids/blob/develop/LICENSE.md"><img src="https://img.shields.io/badge/License-MIT-5E81AC.svg?style=flat-square"/></a></p>

[docs]: https://arcticicestudio.github.io/icecore-hashids
[docs-api]: https://arcticicestudio.github.io/icecore-hashids/api
[docs-api-curse-word-prevention]: https://arcticicestudio.github.io/icecore-hashids/api/curse-word-prevention.html
[docs-api-guide]: https://arcticicestudio.github.io/icecore-hashids/api/guide
[docs-api-guide-config-alphabet]: https://arcticicestudio.github.io/icecore-hashids/api/guide/configuration/#determine-a-custom-alphabet
[docs-api-guide-config-feature-exception-handling]: https://arcticicestudio.github.io/icecore-hashids/api/guide/configuration/features.html#exception-handling
[docs-api-guide-config-feature-hex-prefix]: https://arcticicestudio.github.io/icecore-hashids/api/guide/configuration/features.html#allow-hexadecimal-number-prefixes
[docs-api-guide-config-feature-no-max-number-size]: https://arcticicestudio.github.io/icecore-hashids/api/guide/configuration/features.html#no-number-size-limit
[docs-api-guide-config-min-hash-length]: https://arcticicestudio.github.io/icecore-hashids/api/guide/configuration/#defining-a-minimum-hash-length
[docs-api-guide-config-salt]: https://arcticicestudio.github.io/icecore-hashids/api/guide/configuration/#using-a-salt
[docs-api-guide-decoding]: https://arcticicestudio.github.io/icecore-hashids/api/guide/decoding.html
[docs-api-guide-encoding]: https://arcticicestudio.github.io/icecore-hashids/api/guide/encoding.html
[docs-api-instances]: https://arcticicestudio.github.io/icecore-hashids/api/instances.html
[docs-dev-building]: https://arcticicestudio.github.io/icecore-hashids/development/building.html
[docs-dev-contributing]: https://arcticicestudio.github.io/icecore-hashids/development/contributing.html
[docs-dev-contributing-branch-org]: https://arcticicestudio.github.io/icecore-hashids/development/contributing.html#branch-organization
[docs-dev-contributing-bug-reports]: https://arcticicestudio.github.io/icecore-hashids/development/contributing.html#bug-reports
[docs-dev-contributing-enhancements]: https://arcticicestudio.github.io/icecore-hashids/development/contributing.html#enhancement-suggestions
[docs-dev-contributing-mcve]: https://arcticicestudio.github.io/icecore-hashids/development/contributing.html#mcve
[docs-dev-contributing-other-feedback]: https://arcticicestudio.github.io/icecore-hashids/development/contributing.html#give-feedback-on-issues-and-pull-requests
[docs-dev-contributing-other-improve-issues]: https://arcticicestudio.github.io/icecore-hashids/development/contributing.html#improve-issues
[docs-dev-contributing-pr]: https://arcticicestudio.github.io/icecore-hashids/development/contributing.html#pull-requests
[docs-dev-contributing-styleguides]: https://arcticicestudio.github.io/icecore-hashids/development/contributing.html#styleguides
[docs-dev-contributing-versioning]: https://arcticicestudio.github.io/icecore-hashids/development/contributing.html#versioning
[docs-dev-testing]: https://arcticicestudio.github.io/icecore-hashids/development/testing.html
[docs-getting-started-installation]: https://arcticicestudio.github.io/icecore-hashids/getting-started/installation.html
[docs-getting-started-requirements]: https://arcticicestudio.github.io/icecore-hashids/getting-started/requirements.html
[hashids]: http://hashids.org
[hashids-js]: https://github.com/ivanakimov/hashids.js
[long-max-value]: https://docs.oracle.com/javase/8/docs/api/java/lang/Long.html#MAX_VALUE
