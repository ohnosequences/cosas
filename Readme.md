## cosas

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
