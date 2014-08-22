## Conversions to HList and List

```scala
package ohnosequences.pointless.ops.typeSet

import ohnosequences.pointless._, AnyFn._, typeSet._
import shapeless._

case object id extends Poly1 { implicit def default[T] = at[T]((t:T) => t) }


@annotation.implicitNotFound(msg = "Can't convert ${S} to an HList")
trait ToHList[S <: AnyTypeSet] extends Fn1[S] with WithCodomain[HList]

object ToHList {

  def apply[S <: AnyTypeSet](implicit toHList: ToHList[S]): ToHList[S] with out[toHList.Out] = toHList

  implicit def any[S <: AnyTypeSet](implicit 
      mapper: id.type MapToHList S
    ):  ToHList[S] with out[mapper.Out] =
    new ToHList[S] {
      type Out = mapper.Out
      def apply(s: S): Out = mapper(s)
    }

}


@annotation.implicitNotFound(msg = "Can't convert ${L} to a TypeSet (maybe element types are not distinct)")
trait FromHList[L <: HList] extends Fn1[L] with WithCodomain[AnyTypeSet]

object FromHList {

  def apply[L <: HList](implicit fromHList: FromHList[L]): FromHList[L] with out[fromHList.Out] = fromHList

  implicit def hnil[N <: HNil]: FromHList[N] with out[∅] = 
    new FromHList[N] {
      type Out = ∅
      def apply(l: N): Out = ∅
    }
  
  implicit def cons[H, T <: HList, OutT <: AnyTypeSet]
    (implicit 
      rest: FromHList[T] with out[OutT],
      check: H ∉ OutT
    ):  FromHList[H :: T] with out[H :~: OutT] = 
    new FromHList[H :: T] {
      type Out = H :~: OutT
      def apply(l: H :: T): Out = l.head :~: rest(l.tail)
    }

}


@annotation.implicitNotFound(msg = "Can't convert ${S} to a List")
trait ToList[S <: AnyTypeSet] extends Fn1[S] with WrappedIn[List]

object ToList {

  def apply[S <: AnyTypeSet](implicit toList: ToList[S]): ToList[S] with out[toList.Out] = toList

  // NOTE: approach with using MapToList doesn't work...
  
  // case object id_ extends Poly1 { implicit def default[T <: X, X] = at[T]((t: T) => (t: X)) }
  // 
  // implicit def any[S <: AnyTypeSet](implicit 
  //     mapper: id_.type MapToList S
  //   ):  ToList[S] with o[mapper.O] =
  //   new ToList[S] {
  //     type O = mapper.O
  //     def apply(s: S): Out = mapper(s)
  //   }

  implicit def empty[X]: ToList[∅] with o[X] = 
    new ToList[∅] {
      type O = X
      def apply(s: ∅): Out = Nil
    }
  
  implicit def one[X, H <: X]: ToList[H :~: ∅] with o[X] =
    new ToList[H :~: ∅] {
      type O = X
      def apply(s: H :~: ∅): Out = List[X](s.head)
    }

  implicit def cons2[X, H1 <: X, H2 <: X, T <: AnyTypeSet]
    (implicit 
      lt: ToList[H2 :~: T] with o[X]
    ):  ToList[H1 :~: H2 :~: T] with o[X] = 
    new ToList[H1 :~: H2 :~: T] {
      type O = X
      def apply(s: H1 :~: H2 :~: T): Out = s.head :: lt(s.tail.head :~: s.tail.tail)
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