package ohnosequences.cosas.klists

import ohnosequences.cosas.fns._

/* Mnemonics:
   - F for Function
   - Z for Zero
   - L for List
*/

/* foldl :: (b -> a -> b) -> b -> [a] -> b
   foldl f z []     = z
   foldl f z (x:xs) = foldl f (f z x) xs
*/
class FoldLeft[F <: AnyDepFn2] extends DepFn3[
  // func, zero, list:
  F, F#Out, AnyKList.Of[F#In2],
  F#Out
]

case object FoldLeft {

  implicit def forStdFunction[
    A, B, L <: AnyKList.Of[A]
  ]: AnyApp3At[FoldLeft[Fn2[B, A, B]],
      Fn2[B, A, B], B, L
    ] { type Y = B } =
    App3 { (f: Fn2[B, A, B], z: B, l: L) =>

      // TODO: remove this before release
      // println { "using foldLeft from std List" }

      l.asList.foldLeft(z)(f.f)
    }

  implicit def empty[
    A <: F#In2, Z <: F#Out,
    F <: AnyDepFn2
  ]: AnyApp3At[
      FoldLeft[F],
      F, Z, KNil[A]
    ] { type Y = Z } =
    App3 { (_, z: Z, _) => z }

  implicit def cons[
    F <: AnyDepFn2 {
      type In1 >: Z
      type In2 >: H
      type Out >: FOut
    },
    // NOTE: a funny fact: it doesn't work with T <: AnyKList.Of[F#In2]
    H <: T#Bound, T <: AnyKList { type Bound <: F#In2 },
    Z <: F#Out,
    FOut
  ](implicit
    appF: AnyApp2At[F, Z, H] { type Y = FOut },
    rec: AnyApp3At[FoldLeft[F], F, FOut, T]
  ): AnyApp3At[FoldLeft[F],
      F, Z, H :: T
    ] { type Y = rec.Y } =
    App3 { (f: F, z: Z, xs: H :: T) =>

      rec(f, appF(z, xs.head), xs.tail)
    }
}



/* foldr :: (a -> b -> b) -> b -> [a] -> b
   foldr f z []     = z
   foldr f z (x:xs) = f x (foldr f z xs)
*/
class FoldRight[F <: AnyDepFn2] extends DepFn3[
  // func, zero, list:
  F, F#Out, AnyKList.Of[F#In1],
  F#Out
]

case object FoldRight {

  implicit def forStdFunction[
    A, B, L <: AnyKList.Of[A]
  ]: AnyApp3At[FoldRight[Fn2[A, B, B]],
      Fn2[A, B, B], B, L
    ] { type Y = B } =
    App3 { (f: Fn2[A, B, B], z: B, l: L) =>

      // TODO: remove this before release
      // println { "using foldRight from std List" }

      l.asList.foldRight(z)(f.f)
    }

  implicit def empty[
    A <: F#In1, Z <: F#Out,
    F <: AnyDepFn2
  ]: AnyApp3At[
      FoldRight[F],
      F, Z, KNil[A]
    ] { type Y = Z } =
    App3 { (_, z: Z, _) => z }

  implicit def cons[
    F <: AnyDepFn2 {
      type In1 >: H
      type In2 >: FoldOut
      type Out >: FOut
    },
    Z <: F#Out,
    H <: T#Bound, T <: AnyKList { type Bound <: F#In1 }, // .Of[F#In1]
    FoldOut, FOut
  ](implicit
    appFold: AnyApp3At[ FoldRight[F],
      F, Z, T
    ] { type Y = FoldOut },
    appF: AnyApp2At[
      F, H, FoldOut
    ] { type Y = FOut }
  ): AnyApp3At[ FoldRight[F],
      F, Z, H :: T
    ] { type Y = FOut } =
    App3 { (f: F, z: Z, xs: H :: T) =>

      appF(xs.head, appFold(f, z, xs.tail))
    }

}
