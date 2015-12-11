
```scala
package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

class mapKList[F <: AnyDepFn1, Y <: F#Out] extends DepFn2[
  F,
  AnyKList,
  AnyKList { type Bound = Y }
]

case object mapKList extends WithSameType {

  implicit def optimizeFn1[
    A0 >: L#Bound, B, L <: AnyKList
  ]
  : AnyApp2At[mapKList[Fn1[A0,B], B], Fn1[A0,B], L] { type Y = AnyKList.withBound[B] } =
    App2 { (fn: Fn1[A0,B], l: L) => AnyKList.fromList( (l.asList: List[A0]) map fn.f ) }

  implicit def empty[
    F <: AnyDepFn1 { type Out >: B },
    A,
    B
  ]
  : AnyApp2At[mapKList[F,B], F, KNil[A]] { type Y = KNil[B] } =
    App2 { (f: F, e: KNil[A]) => KNil[B] }

  implicit def kconsBis[
    F <: AnyDepFn1 { type Out >: U },
    H <: InT#Bound, InT <: AnyKList { type Bound <: F#In1 },
    U >: O,
    O
  ](implicit
    evF: AnyApp1At[F,H] { type Y = O },
    mapof: AnyApp2At[
      mapKList[F,U],
      F,
      InT
    ]
  )
  : AnyApp2At[mapKList[F,U], F, H :: InT] { type Y = O :: mapof.Y } =
    App2[mapKList[F,U], F, H :: InT, O :: mapof.Y] {
      (f: F, s: H :: InT) => evF(s.head) :: mapof(f,s.tail)
    }
}

trait WithSameType {

  implicit def kcons[
    F <: AnyDepFn1 { type Out >: O },
    H <: InT#Bound, InT <: AnyKList { type Bound <: F#In1 },
    O
  ](implicit
    evF: AnyApp1At[F,H] { type Y = O },
    mapof: AnyApp2At[
      mapKList[F,F#Out],
      F,
      InT
    ]
  )
  : AnyApp2At[mapKList[F,F#Out], F, H :: InT] { type Y = O :: mapof.Y } =
    App2[mapKList[F,F#Out], F, H :: InT, O :: mapof.Y] {
      (f: F, s: H :: InT) => evF(s.head) :: mapof(f,s.tail)
    }
}

```




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