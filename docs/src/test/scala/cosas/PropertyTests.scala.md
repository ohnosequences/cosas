
```scala
package ohnosequences.cosas.test

import ohnosequences.cosas._, types._, properties._, typeSets._

object exampleProperties {

  case object key       extends Property[String]("key")
  case object name      extends Property[String]("name")
  case object age       extends Property[Int]("age")
  case object valueless extends Property("valueless")
}

class PropertyTests extends org.scalatest.FunSuite {

  import exampleProperties._

  test("can create fields") {

    val k: ValueOf[key.type] = key := "2aE5Cgo7Gv62"

    val n: ValueOf[name.type] = name := "Rigoberto Smith"

    val a = age := 13123
    val a0: ValueOf[age.type] = age := (13123: Integer)

    assertTypeError("""val z = key(34343)""")
    assertTypeError("""val uhoh = age(true)""")
  }

  test("valueless properties lead to nothing") {

    val itIs = implicitly[valueless.Raw =:= Nothing]
  }
}

```




[test/scala/cosas/asserts.scala]: asserts.scala.md
[test/scala/cosas/DenotationTests.scala]: DenotationTests.scala.md
[test/scala/cosas/SubsetTypesTests.scala]: SubsetTypesTests.scala.md
[test/scala/cosas/EqualityTests.scala]: EqualityTests.scala.md
[test/scala/cosas/PropertyTests.scala]: PropertyTests.scala.md
[test/scala/cosas/RecordTests.scala]: RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: TypeSetTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: TypeUnionTests.scala.md
[main/scala/cosas/typeUnions.scala]: ../../../main/scala/cosas/typeUnions.scala.md
[main/scala/cosas/properties.scala]: ../../../main/scala/cosas/properties.scala.md
[main/scala/cosas/records.scala]: ../../../main/scala/cosas/records.scala.md
[main/scala/cosas/fns.scala]: ../../../main/scala/cosas/fns.scala.md
[main/scala/cosas/types.scala]: ../../../main/scala/cosas/types.scala.md
[main/scala/cosas/typeSets.scala]: ../../../main/scala/cosas/typeSets.scala.md
[main/scala/cosas/ops/records/Conversions.scala]: ../../../main/scala/cosas/ops/records/Conversions.scala.md
[main/scala/cosas/ops/records/Update.scala]: ../../../main/scala/cosas/ops/records/Update.scala.md
[main/scala/cosas/ops/records/Transform.scala]: ../../../main/scala/cosas/ops/records/Transform.scala.md
[main/scala/cosas/ops/records/Get.scala]: ../../../main/scala/cosas/ops/records/Get.scala.md
[main/scala/cosas/ops/typeSets/Conversions.scala]: ../../../main/scala/cosas/ops/typeSets/Conversions.scala.md
[main/scala/cosas/ops/typeSets/Filter.scala]: ../../../main/scala/cosas/ops/typeSets/Filter.scala.md
[main/scala/cosas/ops/typeSets/Subtract.scala]: ../../../main/scala/cosas/ops/typeSets/Subtract.scala.md
[main/scala/cosas/ops/typeSets/Mappers.scala]: ../../../main/scala/cosas/ops/typeSets/Mappers.scala.md
[main/scala/cosas/ops/typeSets/Union.scala]: ../../../main/scala/cosas/ops/typeSets/Union.scala.md
[main/scala/cosas/ops/typeSets/Reorder.scala]: ../../../main/scala/cosas/ops/typeSets/Reorder.scala.md
[main/scala/cosas/ops/typeSets/Take.scala]: ../../../main/scala/cosas/ops/typeSets/Take.scala.md
[main/scala/cosas/ops/typeSets/Representations.scala]: ../../../main/scala/cosas/ops/typeSets/Representations.scala.md
[main/scala/cosas/ops/typeSets/Pop.scala]: ../../../main/scala/cosas/ops/typeSets/Pop.scala.md
[main/scala/cosas/ops/typeSets/Replace.scala]: ../../../main/scala/cosas/ops/typeSets/Replace.scala.md
[main/scala/cosas/equality.scala]: ../../../main/scala/cosas/equality.scala.md