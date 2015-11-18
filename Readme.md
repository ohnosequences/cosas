# cosas [![Build Status](https://travis-ci.org/ohnosequences/cosas.svg?branch=master)](https://travis-ci.org/ohnosequences/cosas) [![Codacy code quality](https://api.codacy.com/project/badge/grade/0ead09f732564954b54700aa4e0feea4)](https://www.codacy.com/app/era7/cosas) [![Codacy coverage](https://api.codacy.com/project/badge/coverage/0ead09f732564954b54700aa4e0feea4)](https://www.codacy.com/app/era7/cosas) [![github tag](https://img.shields.io/github/tag/ohnosequences/cosas.svg)](https://github.com/ohnosequences.cosas/tags) [![github release](https://img.shields.io/github/release/ohnosequences.cosas.svg)](https://github.com/ohnosequences.cosas/releases) ![commits since](https://img.shields.io/github/commits-since/ohnosequences/cosas/0.7.1.svg) [![License](https://img.shields.io/badge/license-AGPLv3-blue.svg)](https://tldrlegal.com/license/gnu-affero-general-public-license-v3-%28agpl-3.0%29) [![Gitter](https://img.shields.io/badge/gitter-chat-yellowgreen.svg)](https://gitter.im/ohnosequences/cosas)


<!-- GitHub release:
GitHub commits: 		 -->

* Records
* Type union
* Type sets — something similar to a heterogeneous list, like `HList` from shapeless, with the difference that it can store only things of different types. Also it provides some basic operations on these sets and one of them is a _fast_ check that a type is in the set (in compile time) without traversing the set.


### Usage

To add a dependency on this library to your sbt project, add these lines to your `build.sbt` file:

```scala
resolvers += "Era7 maven releases" at "http://releases.era7.com.s3.amazonaws.com"

libraryDependencies += "ohnosequences" %% "cosas" % "0.6.0" // not released yet
```

The artifact is published only for scala `2.11.2`.


### Documentation

Documentation is generated from the code by [literator](https://github.com/laughedelic/literator)
tool. See the `docs/` folder and tests for examples.

Also, there are [API docs](http://ohnosequences.com/cosas/docs/api/0.5.0)

### contact

- **maintainer** [@eparejatobes](https://github.com/eparejatobes)
