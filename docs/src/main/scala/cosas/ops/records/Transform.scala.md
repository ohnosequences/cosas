
```scala
package ohnosequences.cosas.ops.records

import ohnosequences.cosas._, types._, fns._, records._, typeSets._
import ops.typeSets.Take

@annotation.implicitNotFound(msg = """
  Cannot transform record of type
    ${RT}
  to
    ${Other}
  with values
    ${Rest}
  Probably you haven't provided values for all missing properties.
""")
trait Transform[RT <: AnyRecord, Other <: AnyRecord, Rest]
  extends Fn3[RT#Raw, Other, Rest] with Out[ValueOf[Other]]

case object Transform {

  implicit def transform[
      RT <: AnyRecord,
      Other <: AnyRecord,
      Rest <: AnyTypeSet,
      Uni <: AnyTypeSet,
      Missing <: AnyTypeSet
    ](implicit
      missing: (Other#Raw \ RT#Raw) { type Out = Missing },
      allMissing: Rest ~:~ Missing,
      uni: (RT#Raw ∪ Rest) { type Out = Uni },
      project: Take[Uni, Other#Raw]
    ):  Transform[RT, Other, Rest] =
    new Transform[RT, Other, Rest] {

      def apply(recRaw: RT#Raw, other: Other, rest: Rest): Out =
        other := project(uni(recRaw, rest))
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
[main/scala/cosas/ops/records/Conversions.scala]: Conversions.scala.md
[main/scala/cosas/ops/records/Update.scala]: Update.scala.md
[main/scala/cosas/ops/records/Transform.scala]: Transform.scala.md
[main/scala/cosas/ops/records/Get.scala]: Get.scala.md
[main/scala/cosas/ops/typeSets/Conversions.scala]: ../typeSets/Conversions.scala.md
[main/scala/cosas/ops/typeSets/Filter.scala]: ../typeSets/Filter.scala.md
[main/scala/cosas/ops/typeSets/Subtract.scala]: ../typeSets/Subtract.scala.md
[main/scala/cosas/ops/typeSets/Mappers.scala]: ../typeSets/Mappers.scala.md
[main/scala/cosas/ops/typeSets/Union.scala]: ../typeSets/Union.scala.md
[main/scala/cosas/ops/typeSets/Reorder.scala]: ../typeSets/Reorder.scala.md
[main/scala/cosas/ops/typeSets/Take.scala]: ../typeSets/Take.scala.md
[main/scala/cosas/ops/typeSets/Representations.scala]: ../typeSets/Representations.scala.md
[main/scala/cosas/ops/typeSets/Pop.scala]: ../typeSets/Pop.scala.md
[main/scala/cosas/ops/typeSets/Replace.scala]: ../typeSets/Replace.scala.md
[main/scala/cosas/equality.scala]: ../../equality.scala.md