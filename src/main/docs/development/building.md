# Building

> Please make sure to complete the [requirement](requirements.md) steps first in order to build the project!

Continuous integration builds are running at [Travis CI][travis-ci] and [Circle CI][circle-ci].

## From Source

`icecore-hashids` can be build by running
```sh
mvn clean compile
```

To install `icecore-hashids` into your local repository without GPG signing run
```sh
mvn clean install
```

Signed artifacts can be installed by using the `sign-gpg` profile and providing the `gpg.keyname` property:
```sh
mvn clean install -Dgpg.keyname=<GPG_KEY_ID>
```

All output will be placed in the `target` directory.

## Documentations

In order to build the documentation install the currently tested and supported minimum version [NodeJS 6.5][nodejs] or higher. It comes prebundled with the package manager `npm` which can be used from the CLI.

This documentation can be build by runnning
```sh
npm run docs:build
```
from within the project root to bootstrap the build toolchain and install all dependencies.

The output will be placed in the `target/docs` directory.

To start the local hot reload server with browser live reload, using the default port `4000`, run
```sh
npm run docs:serve
```

[circle-ci]: https://circleci.com/bb/arcticicestudio/icecore-hashids
[nodejs]: https://nodejs.org/en/download/current
[travis-ci]: https://travis-ci.org/arcticicestudio/icecore-hashids
