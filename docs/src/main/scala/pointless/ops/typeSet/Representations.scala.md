
## Building a set of representations

This is a generic thing for deriving the set of representations 
from a set of taggedType singletons. For example:
```scala
case object id extends Property[Int]
case object name extends Property[String]

implicitly[Represented.By[
  id.type :~: name.type :~: ∅,
  id.Rep  :~: name.Rep  :~: ∅
]]
```

See examples of usage it for record properties in tests


```scala
package ohnosequences.pointless.ops.typeSet

import ohnosequences.pointless._, AnyFn._, taggedType._, typeSet._

@annotation.implicitNotFound(msg = "Can't construct a set of representations for ${S}")
trait Represented[S <: AnyTypeSet] extends AnyFn with WithCodomain[AnyTypeSet]

object Represented {

  implicit val empty: Represented[∅] with out[∅] = new Represented[∅] { type Out = ∅ }

  implicit def cons[H <: AnyTaggedType, T <: AnyTypeSet]
    (implicit t: Represented[T]): Represented[H :~: T] with out[Tagged[H] :~: t.Out] =
      new Represented[H :~: T] { type Out = Tagged[H] :~: t.Out }
}


// @annotation.implicitNotFound(msg = "")
// trait TagsOf[S <: TypeSet] extends DepFn1[S] { type Out <: TypeSet }

// object TagsOf {

//   def apply[S <: TypeSet](implicit keys: TagsOf[S]): Aux[S, keys.Out] = keys

//   type Aux[S <: TypeSet, O <: TypeSet] = TagsOf[S] { type Out = O }

//   implicit val empty: Aux[∅, ∅] =
//     new TagsOf[∅] {
//       type Out = ∅
//       def apply(s: ∅): Out = ∅
//     }

//   implicit def cons[H <: Singleton with Representable, T <: TypeSet]
//     (implicit fromRep: Tagged[H] => H, t: TagsOf[T]): Aux[Tagged[H] :~: T, H :~: t.Out] =
//       new TagsOf[Tagged[H] :~: T] {
//         type Out = H :~: t.Out
//         def apply(s: Tagged[H] :~: T): Out = {

//           val uh: H = fromRep(s.head)
//           uh :~: t(s.tail)
//         }
//       }
// }

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

[test/scala/pointless/PropertyTests.scala]: ../../../../../test/scala/pointless/PropertyTests.scala.md
[test/scala/pointless/RecordTests.scala]: ../../../../../test/scala/pointless/RecordTests.scala.md
[test/scala/pointless/TypeSetTests.scala]: ../../../../../test/scala/pointless/TypeSetTests.scala.md
[main/scala/pointless/TaggedType.scala]: ../../TaggedType.scala.md
[main/scala/pointless/Record.scala]: ../../Record.scala.md
[main/scala/pointless/ops/typeSet/Lookup.scala]: Lookup.scala.md
[main/scala/pointless/ops/typeSet/Reorder.scala]: Reorder.scala.md
[main/scala/pointless/ops/typeSet/Conversions.scala]: Conversions.scala.md
[main/scala/pointless/ops/typeSet/Subtract.scala]: Subtract.scala.md
[main/scala/pointless/ops/typeSet/Pop.scala]: Pop.scala.md
[main/scala/pointless/ops/typeSet/Representations.scala]: Representations.scala.md
[main/scala/pointless/ops/typeSet/Replace.scala]: Replace.scala.md
[main/scala/pointless/ops/typeSet/Take.scala]: Take.scala.md
[main/scala/pointless/ops/typeSet/Union.scala]: Union.scala.md
[main/scala/pointless/ops/typeSet/Mappers.scala]: Mappers.scala.md
[main/scala/pointless/ops/record/Update.scala]: ../record/Update.scala.md
[main/scala/pointless/ops/record/Conversions.scala]: ../record/Conversions.scala.md
[main/scala/pointless/ops/record/Get.scala]: ../record/Get.scala.md
[main/scala/pointless/Denotation.scala]: ../../Denotation.scala.md
[main/scala/pointless/TypeUnion.scala]: ../../TypeUnion.scala.md
[main/scala/pointless/Fn.scala]: ../../Fn.scala.md
[main/scala/pointless/Property.scala]: ../../Property.scala.md
[main/scala/pointless/TypeSet.scala]: ../../TypeSet.scala.md