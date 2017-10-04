# cosas

<!-- TODO: uncomment once #121 is resolved
[![Codacy coverage](https://api.codacy.com/project/badge/coverage/0ead09f732564954b54700aa4e0feea4)](https://www.codacy.com/app/ohnosequence/cosas)
-->
[![Build Status](https://travis-ci.org/ohnosequences/cosas.svg?branch=master)](https://travis-ci.org/ohnosequences/cosas)
[![Codacy code quality](https://api.codacy.com/project/badge/grade/0ead09f732564954b54700aa4e0feea4)](https://www.codacy.com/app/ohnosequence/cosas)
[![](https://img.shields.io/github/release/ohnosequences/cosas/all.svg)](https://github.com/ohnosequences/cosas/releases/latest)
[![License](https://img.shields.io/badge/license-AGPLv3-blue.svg)](https://tldrlegal.com/license/gnu-affero-general-public-license-v3-%28agpl-3.0%29)
[![](https://img.shields.io/badge/contact-gitter_chat-dd1054.svg)](https://gitter.im/ohnosequences/cosas)

*Cosas* is a library for doing macros-free type-dependent generic programming in Scala.


## Usage

Add this to `build.sbt`:

```scala
libraryDependencies += "ohnosequences" %% "cosas" % "<version>"
```

(see the latest version on the badge above)


#### Requirements

This library is currently cross-compiled and tested with Scala 2.11.11 and 2.12.3. Using an older version of Scala may cause problems due to the bugs in scalac.


### Examples

Check tests code for the usage examples:
* [`src/test/scala/cosas/`](src/test/scala/cosas/)
