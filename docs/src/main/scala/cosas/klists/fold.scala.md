
```scala
package ohnosequences.cosas.klists

import ohnosequences.cosas.fns._
```

Mnemonics:
- F for Function
- Z for Zero
- L for List

foldl :: (b -> a -> b) -> b -> [a] -> b
foldl f z []     = z
foldl f z (x:xs) = foldl f (f z x) xs


```scala
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
      println { "using foldLeft from std List" }

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
```

foldr :: (a -> b -> b) -> b -> [a] -> b
foldr f z []     = z
foldr f z (x:xs) = f x (foldr f z xs)


```scala
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
      println { "using foldRight from std List" }

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
    appF: AnyApp2At[F, H, FoldOut] { type Y = FOut }
  ): AnyApp3At[ FoldRight[F],
      F, Z, H :: T
    ] { type Y = FOut } =
    App3 { (f: F, z: Z, xs: H :: T) =>

      appF(xs.head, appFold(f, z, xs.tail))
    }

}

```




[test/scala/cosas/asserts.scala]: ../../../../test/scala/cosas/asserts.scala.md
[test/scala/cosas/DenotationTests.scala]: ../../../../test/scala/cosas/DenotationTests.scala.md
[test/scala/cosas/EqualityTests.scala]: ../../../../test/scala/cosas/EqualityTests.scala.md
[test/scala/cosas/DependentFunctionsTests.scala]: ../../../../test/scala/cosas/DependentFunctionsTests.scala.md
[test/scala/cosas/KListsTests.scala]: ../../../../test/scala/cosas/KListsTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/NatTests.scala]: ../../../../test/scala/cosas/NatTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: ../../../../test/scala/cosas/TypeUnionTests.scala.md
[main/scala/cosas/package.scala]: ../package.scala.md
[main/scala/cosas/types/package.scala]: ../types/package.scala.md
[main/scala/cosas/types/types.scala]: ../types/types.scala.md
[main/scala/cosas/types/parsing.scala]: ../types/parsing.scala.md
[main/scala/cosas/types/productTypes.scala]: ../types/productTypes.scala.md
[main/scala/cosas/types/syntax.scala]: ../types/syntax.scala.md
[main/scala/cosas/types/project.scala]: ../types/project.scala.md
[main/scala/cosas/types/denotations.scala]: ../types/denotations.scala.md
[main/scala/cosas/types/functionTypes.scala]: ../types/functionTypes.scala.md
[main/scala/cosas/types/serialization.scala]: ../types/serialization.scala.md
[main/scala/cosas/klists/replace.scala]: replace.scala.md
[main/scala/cosas/klists/cons.scala]: cons.scala.md
[main/scala/cosas/klists/klists.scala]: klists.scala.md
[main/scala/cosas/klists/take.scala]: take.scala.md
[main/scala/cosas/klists/package.scala]: package.scala.md
[main/scala/cosas/klists/takeFirst.scala]: takeFirst.scala.md
[main/scala/cosas/klists/toList.scala]: toList.scala.md
[main/scala/cosas/klists/filter.scala]: filter.scala.md
[main/scala/cosas/klists/pick.scala]: pick.scala.md
[main/scala/cosas/klists/drop.scala]: drop.scala.md
[main/scala/cosas/klists/map.scala]: map.scala.md
[main/scala/cosas/klists/at.scala]: at.scala.md
[main/scala/cosas/klists/syntax.scala]: syntax.scala.md
[main/scala/cosas/klists/fold.scala]: fold.scala.md
[main/scala/cosas/klists/noDuplicates.scala]: noDuplicates.scala.md
[main/scala/cosas/klists/slice.scala]: slice.scala.md
[main/scala/cosas/klists/find.scala]: find.scala.md
[main/scala/cosas/records/package.scala]: ../records/package.scala.md
[main/scala/cosas/records/recordTypes.scala]: ../records/recordTypes.scala.md
[main/scala/cosas/records/syntax.scala]: ../records/syntax.scala.md
[main/scala/cosas/records/reorder.scala]: ../records/reorder.scala.md
[main/scala/cosas/typeUnions/typeUnions.scala]: ../typeUnions/typeUnions.scala.md
[main/scala/cosas/typeUnions/package.scala]: ../typeUnions/package.scala.md
[main/scala/cosas/fns/predicates.scala]: ../fns/predicates.scala.md
[main/scala/cosas/fns/instances.scala]: ../fns/instances.scala.md
[main/scala/cosas/fns/package.scala]: ../fns/package.scala.md
[main/scala/cosas/fns/syntax.scala]: ../fns/syntax.scala.md
[main/scala/cosas/fns/functions.scala]: ../fns/functions.scala.md
[main/scala/cosas/subtyping.scala]: ../subtyping.scala.md
[main/scala/cosas/witness.scala]: ../witness.scala.md
[main/scala/cosas/equality.scala]: ../equality.scala.md
[main/scala/cosas/Nat.scala]: ../Nat.scala.md
[main/scala/cosas/Bool.scala]: ../Bool.scala.md