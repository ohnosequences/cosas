# Scala singletons and existentials

## tagging

- if `R <: Singleton with Representable` use `R#Rep, R#Raw`
- if `R <: Representable` use `TaggedWith[R], RawOf[R]`

## syntax, ops and friends

First you declare all your types with stuff that they might have. This is in the normal pkg, so to say.

### syntax

Now, what goes in `syntax`? for each type `AnyFoo`, there should be a trait with `Foo <: AnyFoo` inside called `SyntaxForAnyFoo`. Something like

``` scala
trait SyntaxForAnyFoo {

  type Foo <: AnyFoo
  val foo: Foo
  // methods follow
}
```

You can even do something like (it does not matter actually, just unneeded genericity)

``` scala
trait AnySyntax {

  type BaseType = AnyType
  type Raw <: BaseType
  val raw: Raw
}

object AnySyntaxFor {

  type withBaseType[X] = AnySyntax { type BaseType = X }
}

trait Syntax[X] extends AnySyntax {

  type BaseType = X
}

object Syntax {}
```

Then this should have only abstract methods, with typeclasses for any other syntax required through implicit params of those methods. In practice, these methods have dependent return types, and the traits of the corresponding arguments should be defined in the same way, as syntax, but _without defining any `apply()` method or anything similar_. They should just contain

1. the context on which they depend
2. the return type/s

An example can help. For computing the union of two typesets, we need to compute at compile time the resulting type; and for doing that we need as input (or context) the types of the two typesets that you want to join. Then, in the typesets syntax trait we have

``` scala
// S = Raw here
def ∪[Q <: TypeSet](q: Q)(implicit union: S ∪ Q): union.Out
```

Then, `S ∪ Q` is defined as

``` scala
@annotation.implicitNotFound(msg = "Cannot compute the union of ${S} and ${Q}")
trait Union[S <: TypeSet, Q <: TypeSet] {

  type Out <: TypeSet
}
```

Now we can somewhere else compute this union in whichever way we see fit. I'm not so sure about the best way of requiring them in scope; the standard way is to provide the implicits through the companion object; I hate this [weird set of rules](https://stackoverflow.com/questions/5598085/where-does-scala-look-for-implicits?lq=1) for where `scalac` looks for implicits, but I think that the companion object of a subtype should work.

### ops

I think `impl` would be a better name, but hey who knows. Let's assume an example syntax specification, like `Syntax[AnyTypeSet]`. Now an ops implementation for it should provide

1. a final impl of the syntax trait
2. implicits providing instances of the aforementioned impl