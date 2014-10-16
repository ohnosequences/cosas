
```scala
package ohnosequences.cosas.test

import ohnosequences.cosas._, AnyWrap._, AnyProperty._, AnyTypeSet._

object exampleProperties {
  
  case object key extends Property[String]
  case object name extends Property[String]
  case object age extends Property[Integer]
  case object valueless extends Property
}

class uhoh extends org.scalatest.FunSuite {
    
  import exampleProperties._
  import shapeless.test.illTyped

  test("create property instances") {

    val k = key("2aE5Cgo7Gv62")

    val n = name("Rigoberto Smith")

    val a = age(13123)
    val a0: ValueOf[age.type] = age(13123: Integer)

    illTyped (
    """
      val z = key(34343)
    """
    )

    illTyped (
    """
      val uhoh = age(true)
    """
    )
  }

  test("valueless properties lead to nothing") {

    implicitly[RawOf[valueless.type] =:= Nothing]: Unit
  }

  test("having properties") {
    import AnyPropertiesHolder._

    object foo
    implicit val foo_name = foo has name
    implicit val foo_age = foo has age

    implicitly[foo.type HasProperty name.type]
    implicitly[foo.type HasProperty age.type]

    implicitly[foo.type HasProperties ∅]
    implicitly[foo.type HasProperties (name.type :~: ∅)]
    implicitly[foo.type HasProperties (age.type :~: name.type :~: ∅)]

    implicit val foo_key = foo has key

    implicitly[foo.type HasProperty key.type]
    implicitly[foo.type HasProperties (key.type :~: ∅)]
    implicitly[foo.type HasProperties (age.type :~: key.type :~: ∅)]
    implicitly[foo.type HasProperties (age.type :~: key.type :~: name.type :~: ∅)]
  }

}

```


------

### Index

+ src
  + test
    + scala
      + cosas
        + [PropertyTests.scala][test/scala/cosas/PropertyTests.scala]
        + [WrapTests.scala][test/scala/cosas/WrapTests.scala]
        + [RecordTests.scala][test/scala/cosas/RecordTests.scala]
        + [TypeSetTests.scala][test/scala/cosas/TypeSetTests.scala]
  + main
    + scala
      + cosas
        + [Wrap.scala][main/scala/cosas/Wrap.scala]
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
        + [Denotation.scala][main/scala/cosas/Denotation.scala]
        + [TypeUnion.scala][main/scala/cosas/TypeUnion.scala]
        + [Fn.scala][main/scala/cosas/Fn.scala]
        + [Property.scala][main/scala/cosas/Property.scala]
        + [TypeSet.scala][main/scala/cosas/TypeSet.scala]

[test/scala/cosas/PropertyTests.scala]: PropertyTests.scala.md
[test/scala/cosas/WrapTests.scala]: WrapTests.scala.md
[test/scala/cosas/RecordTests.scala]: RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: TypeSetTests.scala.md
[main/scala/cosas/Wrap.scala]: ../../../main/scala/cosas/Wrap.scala.md
[main/scala/cosas/PropertiesHolder.scala]: ../../../main/scala/cosas/PropertiesHolder.scala.md
[main/scala/cosas/Record.scala]: ../../../main/scala/cosas/Record.scala.md
[main/scala/cosas/ops/typeSet/Check.scala]: ../../../main/scala/cosas/ops/typeSet/Check.scala.md
[main/scala/cosas/ops/typeSet/Reorder.scala]: ../../../main/scala/cosas/ops/typeSet/Reorder.scala.md
[main/scala/cosas/ops/typeSet/Conversions.scala]: ../../../main/scala/cosas/ops/typeSet/Conversions.scala.md
[main/scala/cosas/ops/typeSet/AggregateProperties.scala]: ../../../main/scala/cosas/ops/typeSet/AggregateProperties.scala.md
[main/scala/cosas/ops/typeSet/Subtract.scala]: ../../../main/scala/cosas/ops/typeSet/Subtract.scala.md
[main/scala/cosas/ops/typeSet/Pop.scala]: ../../../main/scala/cosas/ops/typeSet/Pop.scala.md
[main/scala/cosas/ops/typeSet/Representations.scala]: ../../../main/scala/cosas/ops/typeSet/Representations.scala.md
[main/scala/cosas/ops/typeSet/Replace.scala]: ../../../main/scala/cosas/ops/typeSet/Replace.scala.md
[main/scala/cosas/ops/typeSet/Take.scala]: ../../../main/scala/cosas/ops/typeSet/Take.scala.md
[main/scala/cosas/ops/typeSet/Union.scala]: ../../../main/scala/cosas/ops/typeSet/Union.scala.md
[main/scala/cosas/ops/typeSet/Mappers.scala]: ../../../main/scala/cosas/ops/typeSet/Mappers.scala.md
[main/scala/cosas/ops/record/Update.scala]: ../../../main/scala/cosas/ops/record/Update.scala.md
[main/scala/cosas/ops/record/Conversions.scala]: ../../../main/scala/cosas/ops/record/Conversions.scala.md
[main/scala/cosas/ops/record/Get.scala]: ../../../main/scala/cosas/ops/record/Get.scala.md
[main/scala/cosas/Denotation.scala]: ../../../main/scala/cosas/Denotation.scala.md
[main/scala/cosas/TypeUnion.scala]: ../../../main/scala/cosas/TypeUnion.scala.md
[main/scala/cosas/Fn.scala]: ../../../main/scala/cosas/Fn.scala.md
[main/scala/cosas/Property.scala]: ../../../main/scala/cosas/Property.scala.md
[main/scala/cosas/TypeSet.scala]: ../../../main/scala/cosas/TypeSet.scala.md