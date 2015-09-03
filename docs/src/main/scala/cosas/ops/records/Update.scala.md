
```scala
package ohnosequences.cosas.ops.records

import ohnosequences.cosas._, types._, typeSets._, records._, fns._
import ops.typeSets.Replace

@annotation.implicitNotFound(msg = """
  Cannot update property values
    ${Ps}
  from record of type
    ${RT}
""")
trait Update[RT <: AnyRecord, Ps <: AnyTypeSet]
  extends Fn2[RT#Raw, Ps] with Out[ValueOf[RT]]

case object Update {

  implicit def update[RT <: AnyRecord, Ps <: AnyTypeSet]
    (implicit
      check: Ps ⊂ RT#Raw,
      replace: Replace[RT#Raw, Ps]
    ):  Update[RT, Ps] =
    new Update[RT, Ps] {

      def apply(recRaw: RT#Raw, propReps: Ps): Out = new ValueOf[RT](replace(recRaw, propReps))
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
[main/scala/cosas/ops/records/Update.scala]: Update.scala.md
[main/scala/cosas/ops/records/Transform.scala]: Transform.scala.md
[main/scala/cosas/ops/records/Get.scala]: Get.scala.md
[main/scala/cosas/ops/typeSets/SerializeDenotations.scala]: ../typeSets/SerializeDenotations.scala.md
[main/scala/cosas/ops/typeSets/ParseDenotations.scala]: ../typeSets/ParseDenotations.scala.md
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