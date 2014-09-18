
```scala
package ohnosequences.cosas
```

### Wrapping types

```scala
trait AnyWrap {

  type Raw

  type Me = this.type
  implicit def getWrap[V <: ValueOf[Me]](v: V): Me = this
}

trait Wrap[R] extends AnyWrap { type Raw = R }

object AnyWrap {

  type withRaw[R] = AnyWrap { type Raw = R }
  type RawOf[W <: AnyWrap] = W#Raw

  def valueOf[W <: AnyWrap](r: RawOf[W]): ValueOf[W] = new ValueOf[W](r)

  implicit def typeOps[W <: AnyWrap](t: W): WrapOps[W] = new WrapOps[W](t)

  // NOTE: better to do the conversion explicitly
  // implicit def toRaw[W <: AnyWrap](v: ValueOf[W]): RawOf[W] = v.raw
}
import AnyWrap._

class WrapOps[W <: AnyWrap](t: W) {

  def apply(r: RawOf[W]): ValueOf[W] = new ValueOf[W](r)
  def withValue(r: RawOf[W]): ValueOf[W] = new ValueOf[W](r)
}
```

### Values of wrapped types

```scala
sealed trait AnyWrappedValue extends Any {

  type Wrap <: AnyWrap
  type Value <: Wrap#Raw
}

object AnyWrappedValue {

  type ofWrap[W <: AnyWrap] = AnyWrappedValue { type Wrap = W }
  type WrapOf[V <: AnyWrappedValue] = V#Wrap
  type RawOf[V <: AnyWrappedValue] = AnyWrap.RawOf[WrapOf[V]]
}

trait WrappedValue[W <: AnyWrap, @specialized V <: W#Raw] extends Any with AnyWrappedValue {

  type Wrap = W
  type Value = V
}

final class ValueOf[W <: AnyWrap](val raw: RawOf[W]) extends AnyVal with WrappedValue[W, RawOf[W]] {

  // NOTE: it may be confusing:
  override def toString = raw.toString
}

// object ValueOf {

//   implicit def valueOps[W <: AnyWrap](v: ValueOf[W]): ValueOps[W] = new ValueOps[W](v)
// }

// class ValueOps[W <: AnyWrap](v: ValueOf[W]) {
//   // ... //
// }

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

[test/scala/cosas/PropertyTests.scala]: ../../../test/scala/cosas/PropertyTests.scala.md
[test/scala/cosas/WrapTests.scala]: ../../../test/scala/cosas/WrapTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: ../../../test/scala/cosas/TypeSetTests.scala.md
[main/scala/cosas/Wrap.scala]: Wrap.scala.md
[main/scala/cosas/PropertiesHolder.scala]: PropertiesHolder.scala.md
[main/scala/cosas/Record.scala]: Record.scala.md
[main/scala/cosas/ops/typeSet/Check.scala]: ops/typeSet/Check.scala.md
[main/scala/cosas/ops/typeSet/Reorder.scala]: ops/typeSet/Reorder.scala.md
[main/scala/cosas/ops/typeSet/Conversions.scala]: ops/typeSet/Conversions.scala.md
[main/scala/cosas/ops/typeSet/AggregateProperties.scala]: ops/typeSet/AggregateProperties.scala.md
[main/scala/cosas/ops/typeSet/Subtract.scala]: ops/typeSet/Subtract.scala.md
[main/scala/cosas/ops/typeSet/Pop.scala]: ops/typeSet/Pop.scala.md
[main/scala/cosas/ops/typeSet/Representations.scala]: ops/typeSet/Representations.scala.md
[main/scala/cosas/ops/typeSet/Replace.scala]: ops/typeSet/Replace.scala.md
[main/scala/cosas/ops/typeSet/Take.scala]: ops/typeSet/Take.scala.md
[main/scala/cosas/ops/typeSet/Union.scala]: ops/typeSet/Union.scala.md
[main/scala/cosas/ops/typeSet/Mappers.scala]: ops/typeSet/Mappers.scala.md
[main/scala/cosas/ops/record/Update.scala]: ops/record/Update.scala.md
[main/scala/cosas/ops/record/Conversions.scala]: ops/record/Conversions.scala.md
[main/scala/cosas/ops/record/Get.scala]: ops/record/Get.scala.md
[main/scala/cosas/Denotation.scala]: Denotation.scala.md
[main/scala/cosas/TypeUnion.scala]: TypeUnion.scala.md
[main/scala/cosas/Fn.scala]: Fn.scala.md
[main/scala/cosas/Property.scala]: Property.scala.md
[main/scala/cosas/TypeSet.scala]: TypeSet.scala.md