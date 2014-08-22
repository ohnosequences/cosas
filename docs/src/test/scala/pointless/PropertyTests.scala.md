
```scala
package ohnosequences.pointless.test

import ohnosequences.pointless._, taggedType._, property._

object exampleProperties {
  
  case object key extends Property[String]
  case object name extends Property[String]
  case object age extends Property[Integer]
}

class uhoh extends org.scalatest.FunSuite {
    
  import exampleProperties._
  import shapeless.test.illTyped

  test("create property instances") {

    val k = key is "2aE5Cgo7Gv62"

    val n = name is "Rigoberto Smith"

    val a = age is 13123
    val a0 = age =>> (13123: Integer)

    illTyped (
    """
      val z = key is 34343
    """
    )

    illTyped (
    """
      val uhoh = age is true
    """
    )
  }
}

```


------

### Index

+ src
  + test
    + scala
      + pointless
        + [PropertyTests.scala][test/scala/pointless/PropertyTests.scala]
        + [RecordTests.scala][test/scala/pointless/RecordTests.scala]
        + [TypeSetTests.scala][test/scala/pointless/TypeSetTests.scala]
  + main
    + scala
      + pointless
        + [TaggedType.scala][main/scala/pointless/TaggedType.scala]
        + [Record.scala][main/scala/pointless/Record.scala]
        + ops
          + typeSet
            + [Lookup.scala][main/scala/pointless/ops/typeSet/Lookup.scala]
            + [Reorder.scala][main/scala/pointless/ops/typeSet/Reorder.scala]
            + [Conversions.scala][main/scala/pointless/ops/typeSet/Conversions.scala]
            + [Subtract.scala][main/scala/pointless/ops/typeSet/Subtract.scala]
            + [Pop.scala][main/scala/pointless/ops/typeSet/Pop.scala]
            + [Representations.scala][main/scala/pointless/ops/typeSet/Representations.scala]
            + [Replace.scala][main/scala/pointless/ops/typeSet/Replace.scala]
            + [Take.scala][main/scala/pointless/ops/typeSet/Take.scala]
            + [Union.scala][main/scala/pointless/ops/typeSet/Union.scala]
            + [Mappers.scala][main/scala/pointless/ops/typeSet/Mappers.scala]
          + record
            + [Update.scala][main/scala/pointless/ops/record/Update.scala]
            + [Conversions.scala][main/scala/pointless/ops/record/Conversions.scala]
            + [Get.scala][main/scala/pointless/ops/record/Get.scala]
        + [Denotation.scala][main/scala/pointless/Denotation.scala]
        + [TypeUnion.scala][main/scala/pointless/TypeUnion.scala]
        + [Fn.scala][main/scala/pointless/Fn.scala]
        + [Property.scala][main/scala/pointless/Property.scala]
        + [TypeSet.scala][main/scala/pointless/TypeSet.scala]

[test/scala/pointless/PropertyTests.scala]: PropertyTests.scala.md
[test/scala/pointless/RecordTests.scala]: RecordTests.scala.md
[test/scala/pointless/TypeSetTests.scala]: TypeSetTests.scala.md
[main/scala/pointless/TaggedType.scala]: ../../../main/scala/pointless/TaggedType.scala.md
[main/scala/pointless/Record.scala]: ../../../main/scala/pointless/Record.scala.md
[main/scala/pointless/ops/typeSet/Lookup.scala]: ../../../main/scala/pointless/ops/typeSet/Lookup.scala.md
[main/scala/pointless/ops/typeSet/Reorder.scala]: ../../../main/scala/pointless/ops/typeSet/Reorder.scala.md
[main/scala/pointless/ops/typeSet/Conversions.scala]: ../../../main/scala/pointless/ops/typeSet/Conversions.scala.md
[main/scala/pointless/ops/typeSet/Subtract.scala]: ../../../main/scala/pointless/ops/typeSet/Subtract.scala.md
[main/scala/pointless/ops/typeSet/Pop.scala]: ../../../main/scala/pointless/ops/typeSet/Pop.scala.md
[main/scala/pointless/ops/typeSet/Representations.scala]: ../../../main/scala/pointless/ops/typeSet/Representations.scala.md
[main/scala/pointless/ops/typeSet/Replace.scala]: ../../../main/scala/pointless/ops/typeSet/Replace.scala.md
[main/scala/pointless/ops/typeSet/Take.scala]: ../../../main/scala/pointless/ops/typeSet/Take.scala.md
[main/scala/pointless/ops/typeSet/Union.scala]: ../../../main/scala/pointless/ops/typeSet/Union.scala.md
[main/scala/pointless/ops/typeSet/Mappers.scala]: ../../../main/scala/pointless/ops/typeSet/Mappers.scala.md
[main/scala/pointless/ops/record/Update.scala]: ../../../main/scala/pointless/ops/record/Update.scala.md
[main/scala/pointless/ops/record/Conversions.scala]: ../../../main/scala/pointless/ops/record/Conversions.scala.md
[main/scala/pointless/ops/record/Get.scala]: ../../../main/scala/pointless/ops/record/Get.scala.md
[main/scala/pointless/Denotation.scala]: ../../../main/scala/pointless/Denotation.scala.md
[main/scala/pointless/TypeUnion.scala]: ../../../main/scala/pointless/TypeUnion.scala.md
[main/scala/pointless/Fn.scala]: ../../../main/scala/pointless/Fn.scala.md
[main/scala/pointless/Property.scala]: ../../../main/scala/pointless/Property.scala.md
[main/scala/pointless/TypeSet.scala]: ../../../main/scala/pointless/TypeSet.scala.md