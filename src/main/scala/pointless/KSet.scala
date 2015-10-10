// package ohnosequences.pointless
//
// import AnyFn._, AnyTypeUnion._
// import shapeless.{ HList, Poly1, <:!<, =:!= }
//
// sealed trait AnyKSet {
//
//   type Types <: AnyTypeUnion
//   type Union //<: just[Bound] // should be Types#union, but we can't set it here
//   type Bound
//
//   def toStr: String
//   override def toString = "{" + toStr + "}"
// }
//
// trait NilKSet extends AnyKSet {
//     type Types = TypeUnion.empty
//     type Union = Types#union
//     type Bound = Nothing
//
//     def toStr = ""
// }
//
// object NilKSet extends NilKSet
//
//
// trait NEKSet extends AnyKSet {
//   type Head <: Bound
//   val  head: Head
//
//   type Tail <: AnyKSet.withBound[Bound]
//   val  tail: Tail
//
//   type Types = Tail#Types#or[Head]
//   type Union = Types#union
// }
//
// case class ConsKSet[B, H <: B, T <: AnyKSet.withBound[B]]
//   (val head : H,  val tail : T) extends NEKSet {
//   type Head = H; type Tail = T
//
//   type Bound = B
//
//   def toStr = {
//     val h = head match {
//       case _: String => "\""+head+"\""
//       case _: Char   => "\'"+head+"\'"
//       case _         => head.toString
//     }
//     val t = tail.toStr
//     if (t.isEmpty) h else h+", "+t
//   }
// }
//
// object AnyKSet {
//
//   type TypesOf[S <: AnyKSet] = S#Types
//   type withBound[B] = AnyKSet { type Bound <: B }
//
//   type Of[X] = AnyKSet { type Bound = X; type Union <: just[X] }
//
//   type Kons[B, H, T <: AnyKSet] = NEKSet { type Head = H; type Tail = T; type Bound <: B }
//
//   final type KNil = NilKSet
//   val knil: KNil = NilKSet
//
//   def kone[H](h: H):
//     ConsKSet[H, H, KNil] =
//     ConsKSet[H, H, KNil](h, knil)
//
//   // NOTE: this works, but looses the type of the tail "(
//   def kcons[B, H1 <: B, H2 <: B, T <: AnyKSet.withBound[B]]
//     (h1: H1, h2t: Kons[B, H2, T]):
//     ConsKSet[B, H1, Kons[B, H2, T]] =
//     ConsKSet[B, H1, Kons[B, H2, T]](h1, h2t)
//
//   // implicit def ksetOps[S <: AnyKSet](s: S): KSetOps[S] = new KSetOps[S](s)
//
// }
// import AnyKSet._
//
// // class KSetOps[S <: AnyKSet](s: S) {
//
// //   def :#:[H, O](h: H)
// //     (implicit doCons: DoCons[H, S] { type Out = O }): O = doCons(h, s)
//
// // }
//
// // trait DoCons[H, T <: AnyKSet] extends Fn2[H, T] with OutBound[AnyKSet]
//
// // object DoCons {
//
// //   implicit def one[H]:
// //         DoCons[H, KNil] with Out[ConsKSet[H, H, KNil]] =
// //     new DoCons[H, KNil] with Out[ConsKSet[H, H, KNil]] {
//
// //       def apply(h: H, t: KNil): Out = ConsKSet[H, H, KNil](h, t)
// //     }
//
// //   // implicit def more[B, H1 <: B, H2 <: B, H2T <: NEKSet { type Head = H2; type Bound <: B }]:
// //   implicit def more[B, H1 <: B, H2 <: B, T <: AnyKSet.withBound[B], H2T <: Kons[B, H2, T]]:
// //         DoCons[H1, H2T] with Out[ConsKSet[B, H1, H2T]] =
// //     new DoCons[H1, H2T] with Out[ConsKSet[B, H1, H2T]] {
//
// //       def apply(h1: H1, h2t: H2T): Out = ConsKSet[B, H1, H2T](h1, h2t)
// //     }
//
// // }
