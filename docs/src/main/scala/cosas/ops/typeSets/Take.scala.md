## Taking a subset

```scala
package ohnosequences.cosas.ops.typeSets

import ohnosequences.cosas._, fns._, typeSets._

@annotation.implicitNotFound(msg = "Cannot take subset ${Q} from ${S}")
trait Take[S <: AnyTypeSet, Q <: AnyTypeSet] extends Fn1[S] with Out[Q]

object Take {

  def apply[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit take: Take[S, Q]): Take[S, Q] = take

  implicit def empty[S <: AnyTypeSet]: 
        Take[S, ∅] = 
    new Take[S, ∅] { def apply(s: S): ∅ = ∅ }

  implicit def cons[S <: AnyTypeSet, S_ <: AnyTypeSet, H, T <: AnyTypeSet]
    (implicit 
      pop: PopSOut[S, H, S_],
      rest: Take[S_, T]
    ):  Take[S, H :~: T] =
    new Take[S, H :~: T] { 

      def apply(s: S): Out = {
        val (h, t) = pop(s)
        h :~: rest(t)
      }
    }
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
[main/scala/cosas/ops/records/Update.scala]: ../records/Update.scala.md
[main/scala/cosas/ops/records/Transform.scala]: ../records/Transform.scala.md
[main/scala/cosas/ops/records/Get.scala]: ../records/Get.scala.md
[main/scala/cosas/ops/typeSets/SerializeDenotations.scala]: SerializeDenotations.scala.md
[main/scala/cosas/ops/typeSets/ParseDenotations.scala]: ParseDenotations.scala.md
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