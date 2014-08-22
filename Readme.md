## Pointless

_It's **pointless**, but **useful**_


* Records
* Type union
* Type sets — something similar to a heterogeneous list, like `HList` from shapeless, with the difference that it can store only things of different types. Also it provides some basic operations on these sets and one of them is a _fast_ check that a type is in the set (in compile time) without traversing the set.


### Usage

To add a dependency on this library to your sbt project, add these lines to your `build.sbt` file:

```scala
resolvers += "Era7 maven releases" at "http://releases.era7.com.s3.amazonaws.com"

libraryDependencies += "ohnosequences" %% "type-sets" % "0.4.0"
```

The artifact is published for scala `2.10.4` and `2.11.1`.


### Documentation

Documentation is generated from the code by [literator](https://github.com/laughedelic/literator) 
tool. You can start from [TypeSet](docs/src/main/scala/TypeSet.md) definitions and use index for 
navigation, see also [tests](docs/src/test/scala/TypeSetTests.md) for examples.

Also, there are [API docs](http://ohnosequences.com/type-sets/docs/api/0.4.0)
