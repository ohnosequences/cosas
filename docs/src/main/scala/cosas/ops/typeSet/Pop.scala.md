
## Popping an element from a set

Returns the element of the set of the given type and the rest of the set


```scala
package ohnosequences.cosas.ops.typeSet

import ohnosequences.cosas._, AnyFn._, AnyTypeSet._

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


------

### Index

+ src
  + test
    + scala
      + cosas
        + [PropertyTests.scala][test/scala/cosas/PropertyTests.scala]
        + [TypeUnionTests.scala][test/scala/cosas/TypeUnionTests.scala]
        + [ScalazEquality.scala][test/scala/cosas/ScalazEquality.scala]
        + [WrapTests.scala][test/scala/cosas/WrapTests.scala]
        + [RecordTests.scala][test/scala/cosas/RecordTests.scala]
        + [TypeSetTests.scala][test/scala/cosas/TypeSetTests.scala]
  + main
    + scala
      + cosas
        + [PropertiesHolder.scala][main/scala/cosas/PropertiesHolder.scala]
        + [Record.scala][main/scala/cosas/Record.scala]
        + ops
          + typeSet
            + [Check.scala][main/scala/cosas/ops/typeSet/Check.scala]
            + [Reorder.scala][main/scala/cosas/ops/typeSet/Reorder.scala]
            + [Conversions.scala][main/scala/cosas/ops/typeSet/Conversions.scala]
            + [AggregateProperties.scala][main/scala/cosas/ops/typeSet/AggregateProperties.scala]
            + [Subtract.scala][main/scala/cosas/ops/typeSet/Subtract.scala]
            + [Pop.scala][main/scala/cosas/ops/typeSet/Pop.scala]
            + [Representations.scala][main/scala/cosas/ops/typeSet/Representations.scala]
            + [Replace.scala][main/scala/cosas/ops/typeSet/Replace.scala]
            + [Take.scala][main/scala/cosas/ops/typeSet/Take.scala]
            + [Union.scala][main/scala/cosas/ops/typeSet/Union.scala]
            + [Mappers.scala][main/scala/cosas/ops/typeSet/Mappers.scala]
          + record
            + [Update.scala][main/scala/cosas/ops/record/Update.scala]
            + [Conversions.scala][main/scala/cosas/ops/record/Conversions.scala]
            + [Get.scala][main/scala/cosas/ops/record/Get.scala]
        + [TypeUnion.scala][main/scala/cosas/TypeUnion.scala]
        + [Fn.scala][main/scala/cosas/Fn.scala]
        + [Types.scala][main/scala/cosas/Types.scala]
        + csv
          + [csv.scala][main/scala/cosas/csv/csv.scala]
        + [Property.scala][main/scala/cosas/Property.scala]
        + [TypeSet.scala][main/scala/cosas/TypeSet.scala]

[test/scala/cosas/PropertyTests.scala]: ../../../../../test/scala/cosas/PropertyTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: ../../../../../test/scala/cosas/TypeUnionTests.scala.md
[test/scala/cosas/ScalazEquality.scala]: ../../../../../test/scala/cosas/ScalazEquality.scala.md
[test/scala/cosas/WrapTests.scala]: ../../../../../test/scala/cosas/WrapTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: ../../../../../test/scala/cosas/TypeSetTests.scala.md
[main/scala/cosas/PropertiesHolder.scala]: ../../PropertiesHolder.scala.md
[main/scala/cosas/Record.scala]: ../../Record.scala.md
[main/scala/cosas/ops/typeSet/Check.scala]: Check.scala.md
[main/scala/cosas/ops/typeSet/Reorder.scala]: Reorder.scala.md
[main/scala/cosas/ops/typeSet/Conversions.scala]: Conversions.scala.md
[main/scala/cosas/ops/typeSet/AggregateProperties.scala]: AggregateProperties.scala.md
[main/scala/cosas/ops/typeSet/Subtract.scala]: Subtract.scala.md
[main/scala/cosas/ops/typeSet/Pop.scala]: Pop.scala.md
[main/scala/cosas/ops/typeSet/Representations.scala]: Representations.scala.md
[main/scala/cosas/ops/typeSet/Replace.scala]: Replace.scala.md
[main/scala/cosas/ops/typeSet/Take.scala]: Take.scala.md
[main/scala/cosas/ops/typeSet/Union.scala]: Union.scala.md
[main/scala/cosas/ops/typeSet/Mappers.scala]: Mappers.scala.md
[main/scala/cosas/ops/record/Update.scala]: ../record/Update.scala.md
[main/scala/cosas/ops/record/Conversions.scala]: ../record/Conversions.scala.md
[main/scala/cosas/ops/record/Get.scala]: ../record/Get.scala.md
[main/scala/cosas/TypeUnion.scala]: ../../TypeUnion.scala.md
[main/scala/cosas/Fn.scala]: ../../Fn.scala.md
[main/scala/cosas/Types.scala]: ../../Types.scala.md
[main/scala/cosas/csv/csv.scala]: ../../csv/csv.scala.md
[main/scala/cosas/Property.scala]: ../../Property.scala.md
[main/scala/cosas/TypeSet.scala]: ../../TypeSet.scala.md