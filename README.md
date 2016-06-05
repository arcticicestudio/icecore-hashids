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
