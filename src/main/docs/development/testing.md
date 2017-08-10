# Testing

> Please make sure to complete the [requirement](requirements.md) steps first in order to run the tests!

Continuous integration builds are running at [Travis CI][travis-ci] and [Circle CI][circle-ci]. The code coverage results are processed via [Codecov][codecov].

To start all test types at once run

```sh
mvn clean test
```

## Unit Tests

The unit tests are implemented using [JUnit 4][junit] and can either be run via Maven

```sh
mvn clean test -Dtest=HashidsTest
```

or by using the provided *Unit Tests* [run/debug configuration][intellij-run-config] from within IntelliJ IDEA.

![][scrot-run-unit-tests]

The unit tests can also be run in debug mode by using the bug icon next to the run configuration name or from the menu via *Run* > *Debug* > *Unit Tests*.

The [test runner tab][intellij-test-runner-tab] will automatically toggle to show the test results.

![][scrot-window-unit-tests]

## Interoperability Tests

The interoperability tests ensure that the library provides the same results as the reference algorithm implementation [*hashids.js*][hashids-js] by using

* the Maven plugin [frontend-maven-plugin][maven-plugin-frontend] to locally download [NodeJS][nodejs] and install the required *devDependencies* via [*npm*][npm]
* the Java 8 JavaScript engine [Nashorn][nashorn] to execute the downloaded *hashids.js* script

and then comparing the results of both libraries.

The tests can either be run via Maven

```sh
mvn clean test -P node -Dtest=InteropHashidsTest
```

or by using the provided *Interop Tests* [run/debug configuration][intellij-run-config] from within IntelliJ IDEA.

![][scrot-run-interop-tests]

The tests can also be run in debug mode by using the bug icon next to the run configuration name or from the menu via *Run* > *Debug* > *Interop Tests*.

The [test runner tab][intellij-test-runner-tab] will automatically toggle to show the test results.

![][scrot-window-interop-tests]

[circle-ci]: https://circleci.com/bb/arcticicestudio/icecore-hashids
[codecov]: https://codecov.io/gh/arcticicestudio/icecore-hashids
[hashids-js]: https://github.com/ivanakimov/hashids.js
[intellij-test-runner-tab]: https://www.jetbrains.com/help/idea/test-runner-tab.html
[intellij-run-config]: https://www.jetbrains.com/help/pycharm/run-debug-configurations.html
[junit]: http://junit.org/junit4
[maven-plugin-frontend]: https://github.com/eirslett/frontend-maven-plugin
[nashorn]: https://docs.oracle.com/javase/8/docs/technotes/guides/scripting/nashorn
[nodejs]: https://nodejs.org
[npm]: https://www.npmjs.com
[scrot-run-interop-tests]: ../assets/scrot-docs-testing-config-interop-tests.png
[scrot-run-unit-tests]: ../assets/scrot-docs-testing-config-unit-tests.png
[scrot-window-interop-tests]: ../assets/scrot-docs-testing-window-interop-tests.png
[scrot-window-unit-tests]: ../assets/scrot-docs-testing-window-unit-tests.png
[travis-ci]: https://travis-ci.org/arcticicestudio/icecore-hashids
