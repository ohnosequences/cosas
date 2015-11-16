package ohnosequences.cosas.klists

import ohnosequences.cosas.fns._

/* Mnemonics:
   - L for List
   - Z for Zero
   - F for Function
*/

/* foldl :: (b -> a -> b) -> b -> [a] -> b
   foldl f z []     = z
   foldl f z (x:xs) = foldl f (f z x) xs
*/
class FoldLeft[F <: AnyDepFn2] extends DepFn3[
  // list, zero, func:
  AnyKList.Of[F#In2], F#Out, F,
  F#Out
]

case object FoldLeft {

  implicit def forStdFunction[
    A, B, L <: AnyKList.Of[A]
  ]: AnyApp3At[FoldLeft[Fn2[B, A, B]],
      L, B, Fn2[B, A, B]
    ] { type Y = B } =
    App3 { (l: L, z: B, f: Fn2[B, A, B]) =>

      // TODO: remove this before release
      println { "using foldLeft from std List" }

      l.asList.foldLeft(z)(f.f)
    }

  implicit def empty[
    A <: F#In2, Z <: F#Out,
    F <: AnyDepFn2
  ]: AnyApp3At[
      FoldLeft[F],
      KNil[A], Z, F
    ] { type Y = Z } = App3 {
      (n: KNil[A], z: Z, f: F) => z
    }

  implicit def cons[
    F <: AnyDepFn2 {
      type In1 >: Z
      type In2 >: H
      type Out >: FOut
    },
    // NOTE: a funny fact: it doesn't work with T <: AnyKList.Of[F#In2]
    H <: T#Bound, T <: AnyKList { type Bound <: F#In2},
    Z <: F#Out,
    FOut
  ](implicit
    appF: AnyApp2At[F, Z, H] { type Y = FOut },
    rec: AnyApp3At[FoldLeft[F], T, FOut, F]
  ): AnyApp3At[FoldLeft[F],
      H :: T, Z, F
    ] { type Y = rec.Y } =
    App3 { (xs: H :: T, z: Z, f: F) =>

      rec(xs.tail, appF(z, xs.head), f)
    }
}



/* foldr :: (a -> b -> b) -> b -> [a] -> b
   foldr f z []     = z
   foldr f z (x:xs) = f x (foldr f z xs)
*/
class FoldRight[L <: AnyKList, Z <: F#Out, F <: AnyDepFn2]
  extends DepFn3[
    L, Z, F,
    F#Out
  ]

case object FoldRight {

  implicit def forStdFunction[
    A, B, L <: AnyKList.Of[A]
  ]: AnyApp3At[
      FoldRight[L, B, Fn2[A, B, B]],
               L, B, Fn2[A, B, B]
    ] { type Y = B } =
    App3 { (l: L, z: B, f: Fn2[A, B, B]) =>

      // TODO: remove this before release
      println { "using foldRight from std List" }

      l.asList.foldRight(z)(f.f)
    }

  implicit def empty[
    F <: AnyDepFn2 { type In1 >: A; type Out >: Z },
    Z, A
  ]: AnyApp3At[
      FoldRight[KNil[A], Z, F],
                KNil[A], Z, F
    ] { type Y = Z } = App3 {

      (n: KNil[A], z: Z, f: F) => z
    }

  implicit def cons[
    F <: AnyDepFn2 { type In1 >: H; type In2 >: FoldOut; type Out >: FOut },
    Z <: F#Out,
    H <: T#Bound, T <: AnyKList,
    FoldOut, FOut
  ](implicit
    foldRight: AnyApp3At[
      FoldRight[T, Z, F],
      T, Z, F
    ] { type Y = FoldOut },
    appF: AnyApp2At[F, H, FoldOut] { type Y = FOut }
  ): AnyApp3At[
      FoldRight[H :: T, Z, F],
                H :: T, Z, F
    ] { type Y = FOut } =
    App3 { (xs: H :: T, z: Z, f: F) =>

      appF(xs.head, foldRight(xs.tail, z, f))
    }
}
