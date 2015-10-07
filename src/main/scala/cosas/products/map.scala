package ohnosequences.cosas.products

import ohnosequences.cosas._, fns._

// case object MapKList extends DepFn2[
//   AnyDepFn1,
//   AnyKList,
//   AnyKList
// ]
// {
//
//   implicit def empty[F <: In1]: App2[MapKList.type, F, KNil[F#In1], KNil[F#Out]] =
//     MapKList at { (f: F, e: KNil[F#In1]) => KNil[F#Out] }
//
//   implicit def cons[
//     F <: In1 { type Out >: OutH },
//     H <: F#In1, T <: KList[F#In1],
//     OutH, OutT <: KList[F#Out]
//   ](implicit
//     evF: App1[F, H, OutH],
//     evThis: App2[MapKList.type, F, T, OutT]
//   )
//   : App2[MapKList.type, F, H :: T, OutH :: OutT] =
//     MapKList at { (f: F, ht: H :: T) => KCons(f(ht.head), MapKList(f,ht.tail)) }
// }
