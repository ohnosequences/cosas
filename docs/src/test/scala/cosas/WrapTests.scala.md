
```scala
package ohnosequences.cosas.tests

import shapeless.test.illTyped
import ohnosequences.cosas._, AnyWrap._, ValueOf._

object WrapTestsContext {

  case object Color extends Wrap[String]
}

class WrapTests extends org.scalatest.FunSuite {
  import WrapTestsContext._

  test("creating values") {

    val azul = Color("blue")
    val verde = valueOf[Color.type]("green")
    val amarillo = Color withValue "yellow"

    assert{ azul.raw == "blue" }
    assert{ verde.raw == "green" }
    assert{ amarillo.raw == "yellow" }
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