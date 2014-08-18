
## Conversions to HList and List


```scala
package ohnosequences.typesets

import shapeless._

trait ToHList[S <: TypeSet] extends DepFn1[S] { type Out <: HList }

object ToHList {
  def apply[S <: TypeSet](implicit toHList: ToHList[S]): Aux[S, toHList.Out] = toHList

  type Aux[S <: TypeSet, L <: HList] = ToHList[S] { type Out = L }

  implicit def emptyToHList: Aux[?, HNil] = 
    new ToHList[?] { type Out = HNil
      def apply(s: ?): Out = HNil
    }
  
  implicit def consToHList[H, T <: TypeSet, LT <: HList]
      (implicit lt : Aux[T, LT]): Aux[H :~: T, H :: LT] = 
    new ToHList[H :~: T] { type Out = H :: LT
      def apply(s: H :~: T): Out = s.head :: lt(s.tail)
    }
}

// TODO: FromHList

trait ToList[S <: TypeSet] extends DepFn1[S] {
  type O
  type Out = List[O]
}

// object ToList {
//   def apply[S <: TypeSet](implicit toList: ToList[S]): ToList[S] = toList

//   type Aux[S <: TypeSet, O <: List[_]] = ToList[S] { type Out = O }

//   implicit def emptyToList[O]: Aux[?, List[O]] = 
//     new ToList[?] {
//       type Out = List[O]
//       def apply(s: ?): Out = Nil
//     }
  
//   implicit def consToList[H, T <: TypeSet]
//     (implicit lt: Aux[T, List[T#Bound#get]]): Aux[H :~: T, List[T#Bound#or[H]#get]] =
//       new ToList[H :~: T] {
//         type O = T#Bound#or[H]#get
//         type Out = List[O]
//         def apply(s: H :~: T): Out = 
//           // List[O](s.head) ++ lt(s.tail)
//           // s.head :: lt(s.tail)
//           s.head.asInstanceOf[O] :: lt(s.tail).asInstanceOf[Out]
//       }
// }

object ToList {
  def apply[S <: TypeSet](implicit toList: ToList[S]): Aux[S, toList.O] = toList

  type Aux[S <: TypeSet, O_] = ToList[S] { type O = O_ }

  implicit def emptyToList[O_]: Aux[?, O_] = 
    new ToList[?] { type O = O_
      def apply(s: ?): Out = Nil
    }
  
  implicit def oneToList[OH, H <: OH]: Aux[H :~: ?, OH] =
    new ToList[H :~: ?] { type O = OH
      def apply(s: H :~: ?): Out = List[OH](s.head)
    }

  implicit def cons2ToList[OT, H1 <: OT, H2 <: OT, T <: TypeSet]
      (implicit 
        lt: Aux[H2 :~: T, OT]): Aux[H1 :~: H2 :~: T, OT] = 
    new ToList[H1 :~: H2 :~: T] { type O = OT
      def apply(s: H1 :~: H2 :~: T): Out = s.head :: lt(s.tail.head :~: s.tail.tail)
    }
}

```


------

### Index

+ src
  + main
    + scala
      + items
        + [items.scala][main/scala/items/items.scala]
      + ops
        + [Choose.scala][main/scala/ops/Choose.scala]
        + [Lookup.scala][main/scala/ops/Lookup.scala]
        + [Map.scala][main/scala/ops/Map.scala]
        + [MapFold.scala][main/scala/ops/MapFold.scala]
        + [Pop.scala][main/scala/ops/Pop.scala]
        + [Reorder.scala][main/scala/ops/Reorder.scala]
        + [Replace.scala][main/scala/ops/Replace.scala]
        + [Subtract.scala][main/scala/ops/Subtract.scala]
        + [ToList.scala][main/scala/ops/ToList.scala]
        + [Union.scala][main/scala/ops/Union.scala]
      + [package.scala][main/scala/package.scala]
      + pointless
        + impl
      + [Property.scala][main/scala/Property.scala]
      + [Record.scala][main/scala/Record.scala]
      + [Representable.scala][main/scala/Representable.scala]
      + [TypeSet.scala][main/scala/TypeSet.scala]
      + [TypeUnion.scala][main/scala/TypeUnion.scala]
  + test
    + scala
      + items
        + [itemsTests.scala][test/scala/items/itemsTests.scala]
      + [RecordTests.scala][test/scala/RecordTests.scala]
      + [TypeSetTests.scala][test/scala/TypeSetTests.scala]

[main/scala/items/items.scala]: ../items/items.scala.md
[main/scala/ops/Choose.scala]: Choose.scala.md
[main/scala/ops/Lookup.scala]: Lookup.scala.md
[main/scala/ops/Map.scala]: Map.scala.md
[main/scala/ops/MapFold.scala]: MapFold.scala.md
[main/scala/ops/Pop.scala]: Pop.scala.md
[main/scala/ops/Reorder.scala]: Reorder.scala.md
[main/scala/ops/Replace.scala]: Replace.scala.md
[main/scala/ops/Subtract.scala]: Subtract.scala.md
[main/scala/ops/ToList.scala]: ToList.scala.md
[main/scala/ops/Union.scala]: Union.scala.md
[main/scala/package.scala]: ../package.scala.md
[main/scala/Property.scala]: ../Property.scala.md
[main/scala/Record.scala]: ../Record.scala.md
[main/scala/Representable.scala]: ../Representable.scala.md
[main/scala/TypeSet.scala]: ../TypeSet.scala.md
[main/scala/TypeUnion.scala]: ../TypeUnion.scala.md
[test/scala/items/itemsTests.scala]: ../../../test/scala/items/itemsTests.scala.md
[test/scala/RecordTests.scala]: ../../../test/scala/RecordTests.scala.md
[test/scala/TypeSetTests.scala]: ../../../test/scala/TypeSetTests.scala.md