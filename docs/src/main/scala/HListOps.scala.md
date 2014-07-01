
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
      + [HListOps.scala][main/scala/HListOps.scala]
      + [LookupInSet.scala][main/scala/LookupInSet.scala]
      + [MapFoldSets.scala][main/scala/MapFoldSets.scala]
      + [package.scala][main/scala/package.scala]
      + [SetMapper.scala][main/scala/SetMapper.scala]
      + [SubtractSets.scala][main/scala/SubtractSets.scala]
      + [TypeSet.scala][main/scala/TypeSet.scala]
      + [TypeUnion.scala][main/scala/TypeUnion.scala]
      + [UnionSets.scala][main/scala/UnionSets.scala]
  + test
    + scala
      + [TypeSetTests.scala][test/scala/TypeSetTests.scala]

[main/scala/HListOps.scala]: HListOps.scala.md
[main/scala/LookupInSet.scala]: LookupInSet.scala.md
[main/scala/MapFoldSets.scala]: MapFoldSets.scala.md
[main/scala/package.scala]: package.scala.md
[main/scala/SetMapper.scala]: SetMapper.scala.md
[main/scala/SubtractSets.scala]: SubtractSets.scala.md
[main/scala/TypeSet.scala]: TypeSet.scala.md
[main/scala/TypeUnion.scala]: TypeUnion.scala.md
[main/scala/UnionSets.scala]: UnionSets.scala.md
[test/scala/TypeSetTests.scala]: ../../test/scala/TypeSetTests.scala.md