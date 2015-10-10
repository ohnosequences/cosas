package ohnosequences.cosas.products

import ohnosequences.cosas._, fns._



trait MapKList[F <: AnyDepFn1, X, Y, InK <: KList[X]] {

  type OutK <: KList[Y]

  def apply(xs: InK): OutK
}

case object MapKList {

  // implicit def mapEmpty[
  //   F <: AnyDepFn1, A, B
  // ]
  // : MapKList[F,A,B,KNil[A]] { type OutK = KNil[B] } =
  //   new MapKList[F,A,B, KNil[A]] {
  //
  //   type OutK = KNil[B]
  //   def apply(xs: KNil[A]): KNil[B] = KNil[B]
  // }
  //
  // implicit def mapKCons[
  //   F <: AnyDepFn1,
  //   H <: F#In1, InT <: KList[F#In1],
  //   O <: F#Out, OutT <: KList[F#Out]
  // ](implicit
  //   evF: App1[F,H,O],
  //   mapTail: MapKList[F, F#In1, F#Out, InT] { type OutK = OutT }
  // ): MapKList[F, F#In1, F#Out, KCons[H, InT, F#In1]] { type OutK = KCons[O,OutT,F#Out] } = new MapKList[F, F#In1, F#Out, KCons[H, InT, F#In1]] {
  //
  //   // type InK = H :: InT
  //   type OutK = KCons[O, OutT, F#Out]
  //   def apply(xs: KCons[H, InT, F#In1]): KCons[O, OutT, F#Out] = KCons[O, OutT, F#Out](evF(xs.head), mapTail(xs.tail))
  // }
}

class MapKListOf[F <: AnyDepFn1 { type Out = Y; type In1 = X }, X,Y] extends DepFn2[AnyDepFn1, KList[X], KList[Y]]

case object MapKListOf {

  implicit def empty[
    F <: AnyDepFn1 {type In1 = A; type Out = B0 }, A, B0
  ] //{ type In1 = W; type Out = X },W,X]
  : AnyApp2At[MapKListOf[F,A,B0],F,KNil[A]] { type Y = KNil[B0] } =
    App2 { (f: F, e: KNil[A]) => KNil[B0] }

  // implicit def emptyAny[
  //   F <: AnyDepFn1
  // ]
  // : App2[MapKListOf[F,Any,Any],F,KNil[Any],KNil[Any]] =
  //   App2 { (f: F, e: KNil[Any]) => KNil[Any] }


  // implicit def one[
  //   F <: AnyDepFn1 { type In1 = A; type Out = B }, A, B >: O,
  //   H <: A, O
  // ](implicit
  //   evF: App1[F,H,O]
  // )
  // : AnyApp2At[MapKListOf[F,A,B], F, KCons[H, KNil[A], A]] { type Y = KCons[O, KNil[B], B] } =
  //   App2[MapKListOf[F,A,B], F, KCons[H, KNil[A], A], KCons[O, KNil[B], B]] {
  //     (f: F, s: KCons[H,KNil[F#In1],F#In1]) => f(s.head) :: KNil[F#Out]
  //   }
  // //
  implicit def kconsAlt[
    F <: AnyDepFn1 { type In1 = A; type Out = B },
    A, B >: O,
    H <: F#In1, InT <: KList[A],
    O, OutT <: KList[B]
  ](implicit
    evF: App1[F,H,O],
    mapof: AnyApp2At[
      MapKListOf[F,A,B],
      F, InT
    ]
  )
  : AnyApp2At[MapKListOf[F,A,B], F, KCons[H,InT,A]] { type Y = KCons[O, mapof.Y, B] } =
    App2 { (f: F, s: KCons[H,InT,A]) => KCons[O, mapof.Y, B](f(s.head), mapof(f,s.tail)) }

  // implicit def kcons[
  //   F <: AnyDepFn1 { type In1 = A; type Out = B },
  //   H1 <: F#In1, H2 <: F#In1, InT <: KList[A],
  //   O1, O2 <: B, OutT <: T,
  //   A,
  //   B >: O1, T >: OutT <: KList[B]
  // ](implicit
  //   evF: App1[F,H1,O1],
  //   mapof: AnyApp2At[
  //     MapKListOf[F,A,B],
  //     F, KCons[H2,InT,A]
  //   ] { type Y = KCons[O2,OutT,B] }
  // )
  // : App2[
  //   MapKListOf[F,A,B],
  //   F, KCons[H1, KCons[H2,InT,A],A],
  //   KCons[O1, KCons[O2,OutT,B],B]
  // ] =
  //   App2 { (f: F, ht: KCons[H1, KCons[H2,InT,A],A]) => {
  //
  //       val h: O1 = evF(ht.head)
  //       val t: KCons[O2,OutT,B] = mapof(f,ht.tail)
  //
  //       KCons[O1, KCons[O2,OutT,B],B](h,t)//.asInstanceOf[KCons[O1, KCons[O2,OutT,B],B]]
  //     }
  //   }




  // implicit def two[
  //   W >: H1, X >: O1,
  //   H1, H2 <: W,
  //   O1, O2 <: X,
  //   F <: AnyDepFn1 { type Out >: O1; type In1 >: H1 }
  // ](implicit
  //   mapof: App2[MapKListOf[W,X],F,KCons[H2, KNil[W], W], KCons[O2, KNil[X], X]],
  //   evf: App1[F,H1,O1]
  // )
  // : App2[MapKListOf[W,X],F,KCons[H1, KCons[H2, KNil[W], W], W],  KCons[O1, KCons[O2, KNil[X], X],X]] =
  //   App2 { (f: F, s: KCons[H1, KCons[H2, KNil[W], W], W]) => (evf(s.head):O1) :: (mapof(f,s.tail)) }
}











// case object MapKList extends DepFn2[
//   AnyDepFn1, //{ type In1 >: A; type Out >: B },
//   AnyKList,
//   AnyKList
// ]
// {
//   implicit def empty[
//     F0 <: AnyDepFn1 { type In1 >: A0; type Out >: B0 },
//     A0, B0
//   ]
//   : App2[MapKList.type, F0, KNil[A0], KNil[B0]] =
//     App2 { (f: F0, e: KNil[A0]) => KNil[B0] }
//
//   implicit def cons[
//     F <: AnyDepFn1 { type In1 >: T#Bound; type Out >: OutT#Bound },
//     H <: T#Bound, T <: AnyKList,
//     OutH <: OutT#Bound, OutT <: AnyKList
//   ](implicit
//     evF: App1[F, H, OutH],
//     evThis: App2[MapKList.type, F, T, OutT]
//   )
//   : App2[MapKList.type, F, H :: T, OutH :: OutT] =
//     App2 { (f: F, ht: H :: T) => (f(ht.head): OutH) :: (evThis(f,ht.tail: T): OutT) }
// }
