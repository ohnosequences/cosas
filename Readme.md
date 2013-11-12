## Type-sets

This library defines an abstraction of a _type set_, which is something similar to a heterogeneous
list, like `HList` from shapeless, with the difference that it can store only things of different
types. Also it provides some basic operations on these sets and one of them is a _fast_ check that a
type is in the set (in compile time) without traversing the set.

### Usage

To add a dependency on this library to your sbt project, add these lines to your `build.sbt` file:

```scala
resolvers += "Era7 maven releases" at "http://releases.era7.com.s3.amazonaws.com"

libraryDependencies += "ohnosequences" %% "type-sets" % "0.1.0"
```
