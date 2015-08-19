
## Popping an element from a set

Returns the element of the set of the given type and the rest of the set


```scala
package ohnosequences.cosas.ops.typeSets

import ohnosequences.cosas._, fns._, typeSets._

@annotation.implicitNotFound(msg = "Can't pop an element of type ${E} from the set ${S}")
trait Pop[S <: AnyTypeSet, E] extends Fn1[S] {
  type SOut <: AnyTypeSet
  type Out = (E, SOut)
}

trait PopSOut[S <: AnyTypeSet, E, SO <: AnyTypeSet] extends Pop[S, E] { type SOut = SO }

object Pop extends Pop_2 {
  def apply[S <: AnyTypeSet, E](implicit pop: Pop[S, E]): Pop[S, E] = pop

  implicit def foundInHead[E, H <: E, T <: AnyTypeSet]: 
        PopSOut[H :~: T, E, T] =
    new PopSOut[H :~: T, E, T] { 

      def apply(s: H :~: T): Out = (s.head, s.tail)
    }
}

trait Pop_2 {
  implicit def foundInTail[H, T <: AnyTypeSet, E, TO <: AnyTypeSet]
    (implicit 
      e: E ∈ T, 
      l: PopSOut[T, E, TO]
    ):  PopSOut[H :~: T, E, H :~: TO] =
    new PopSOut[H :~: T, E, H :~: TO] { 

      def apply(s: H :~: T): Out = {
        
        val (e, t) = l(s.tail)
        (e, s.head :~: t)
      }
    }
}
```

This just returns the first part of pop (the element)

```scala
@annotation.implicitNotFound(msg = "Can't lookup an element of type ${E} from the set ${S}")
trait Lookup[S <: AnyTypeSet, E] extends Fn1[S] with Out[E]

object Lookup {
  implicit def popToLookup[S <: AnyTypeSet, E]
    (implicit p: S Pop E): 
        Lookup[S, E] = 
    new Lookup[S, E] { def apply(s: S): Out = p(s)._1 }
}
```

This just returns the second part of pop (the set without the element)

```scala
@annotation.implicitNotFound(msg = "Can't delete an element of type ${E} from the set ${S}")
trait Delete[S <: AnyTypeSet, E] extends Fn1[S] with OutBound[AnyTypeSet]

object Delete {
  implicit def popToDelete[S <: AnyTypeSet, E, SO <: AnyTypeSet]
    (implicit p: PopSOut[S, E, SO]): 
        Delete[S, E] with Out[SO] = 
    new Delete[S, E] with Out[SO] { def apply(s: S): Out = p(s)._2 }
}

```




[test/scala/cosas/asserts.scala]: ../../../../../test/scala/cosas/asserts.scala.md
[test/scala/cosas/DenotationTests.scala]: ../../../../../test/scala/cosas/DenotationTests.scala.md
[test/scala/cosas/SubsetTypesTests.scala]: ../../../../../test/scala/cosas/SubsetTypesTests.scala.md
[test/scala/cosas/EqualityTests.scala]: ../../../../../test/scala/cosas/EqualityTests.scala.md
[test/scala/cosas/PropertyTests.scala]: ../../../../../test/scala/cosas/PropertyTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: ../../../../../test/scala/cosas/TypeSetTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: ../../../../../test/scala/cosas/TypeUnionTests.scala.md
[main/scala/cosas/typeUnions.scala]: ../../typeUnions.scala.md
[main/scala/cosas/properties.scala]: ../../properties.scala.md
[main/scala/cosas/records.scala]: ../../records.scala.md
[main/scala/cosas/fns.scala]: ../../fns.scala.md
[main/scala/cosas/types.scala]: ../../types.scala.md
[main/scala/cosas/typeSets.scala]: ../../typeSets.scala.md
[main/scala/cosas/ops/typeSets/Conversions.scala]: Conversions.scala.md
[main/scala/cosas/ops/typeSets/Filter.scala]: Filter.scala.md
[main/scala/cosas/ops/typeSets/Subtract.scala]: Subtract.scala.md
[main/scala/cosas/ops/typeSets/Mappers.scala]: Mappers.scala.md
[main/scala/cosas/ops/typeSets/Union.scala]: Union.scala.md
[main/scala/cosas/ops/typeSets/Reorder.scala]: Reorder.scala.md
[main/scala/cosas/ops/typeSets/Take.scala]: Take.scala.md
[main/scala/cosas/ops/typeSets/Representations.scala]: Representations.scala.md
[main/scala/cosas/ops/typeSets/Pop.scala]: Pop.scala.md
[main/scala/cosas/ops/typeSets/Replace.scala]: Replace.scala.md
[main/scala/cosas/equality.scala]: ../../equality.scala.md