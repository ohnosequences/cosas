
```scala
package ohnosequences.pointless


trait AnyFn {

  type Out
}

object AnyFn {
  
  type out[T] = AnyFn { type Out = T }

  trait Constant[Z] extends AnyFn { 

    type Out = Z 
  }

  trait WithCodomain[Z] extends AnyFn {

    type Out <: Z
  }

  trait Wrapped extends AnyFn {

    type O
  }

  type o[T] = Wrapped { type O = T }

  trait WrappedIn[F[_]] extends Wrapped{

    type Out = F[O]
  }
  
  trait ConstantWrappedIn[O0,F[_]] extends WrappedIn[F] {

    type O = O0
  }

  trait AnyFn0 extends AnyFn {

    def apply(): Out
  }

  trait AnyFn1 extends AnyFn {

    type In1

    def apply(in1: In1): Out
  }

  trait Fn1[A] extends AnyFn1 {

    type In1 = A
  }

  trait AnyFn2 extends AnyFn {

    type In1; type In2

    def apply(in1: In1, in2: In2): Out
  }

  trait Fn2[A, B] extends AnyFn2 {

    type In1 = A; type In2 = B
  }

  trait AnyFn3 extends AnyFn {

    type In1; type In2; type In3

    def apply(in1: In1, in2: In2, in3: In3): Out
  }

  trait Fn3[A, B, C] extends AnyFn3 {

    type In1 = A; type In2 = B; type In3 = C
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

[test/scala/pointless/PropertyTests.scala]: ../../../test/scala/pointless/PropertyTests.scala.md
[test/scala/pointless/RecordTests.scala]: ../../../test/scala/pointless/RecordTests.scala.md
[test/scala/pointless/TypeSetTests.scala]: ../../../test/scala/pointless/TypeSetTests.scala.md
[main/scala/pointless/TaggedType.scala]: TaggedType.scala.md
[main/scala/pointless/Record.scala]: Record.scala.md
[main/scala/pointless/ops/typeSet/Lookup.scala]: ops/typeSet/Lookup.scala.md
[main/scala/pointless/ops/typeSet/Reorder.scala]: ops/typeSet/Reorder.scala.md
[main/scala/pointless/ops/typeSet/Conversions.scala]: ops/typeSet/Conversions.scala.md
[main/scala/pointless/ops/typeSet/Subtract.scala]: ops/typeSet/Subtract.scala.md
[main/scala/pointless/ops/typeSet/Pop.scala]: ops/typeSet/Pop.scala.md
[main/scala/pointless/ops/typeSet/Representations.scala]: ops/typeSet/Representations.scala.md
[main/scala/pointless/ops/typeSet/Replace.scala]: ops/typeSet/Replace.scala.md
[main/scala/pointless/ops/typeSet/Take.scala]: ops/typeSet/Take.scala.md
[main/scala/pointless/ops/typeSet/Union.scala]: ops/typeSet/Union.scala.md
[main/scala/pointless/ops/typeSet/Mappers.scala]: ops/typeSet/Mappers.scala.md
[main/scala/pointless/ops/record/Update.scala]: ops/record/Update.scala.md
[main/scala/pointless/ops/record/Conversions.scala]: ops/record/Conversions.scala.md
[main/scala/pointless/ops/record/Get.scala]: ops/record/Get.scala.md
[main/scala/pointless/Denotation.scala]: Denotation.scala.md
[main/scala/pointless/TypeUnion.scala]: TypeUnion.scala.md
[main/scala/pointless/Fn.scala]: Fn.scala.md
[main/scala/pointless/Property.scala]: Property.scala.md
[main/scala/pointless/TypeSet.scala]: TypeSet.scala.md