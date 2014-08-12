/*
## Lookup in TypeSet by type

This is similar to the shapeless `Selector`, but it can find first occurence of an element, 
that has a type, which is a sub-type of the given one:

```scala
trait Foo
object Bar extends Foo

(1 :~: "abc" :~: 'a' :~: Bar :~: 42).lookup[Foo] == Bar
```

------
*/

package ohnosequences.typesets


trait Lookup[S <: TypeSet, E] extends Fn2[S,E] { 

  type SetOut <: TypeSet // type of S without E
  type Out <: E
  // not really, why not put this in ops?
  def apply(s: S): Out
}

object Lookup extends Lookup_2 {

  implicit def foundInHead[
    E, 
    H <: E ,
    T <: TypeSet
  ]: Lookup[  H :~: T, E] = new Lookup[H :~: T, E] {
      
    type SetOut = T
    type Out = E
    def apply(s: H :~: T): Out = s.head
  }
}

trait Lookup_2 {

  implicit def foundInTail[
    H,
    T <: TypeSet,
    E
  ](implicit 
    e: E ∈ T, 
    l: Lookup[T, E]
  ): Lookup[H :~: T, E] = new Lookup[H :~: T, E] {
      type SetOut = H :~: l.SetOut
      type Out = l.Out
      def apply(s: H :~: T): Out = l(s.tail)
    }
}

