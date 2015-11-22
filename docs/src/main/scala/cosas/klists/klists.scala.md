
```scala
package ohnosequences.cosas.klists

import ohnosequences.cosas._, typeUnions._, fns._

sealed trait AnyKList extends Any {

  type Bound

  type Length <: AnyNat

  type Types <: AnyTypeUnion
  // NOTE: should be Types#union, but we can't set it here; scalac bugs
  type Union >: Types#union <: Types#union
}

case object KList {

  def apply[F <: AnyDepFn1](f: F): mapKList[F, F#Out] = new mapKList[F, F#Out]
}

sealed trait AnyEmptyKList extends Any with AnyKList {

  type Types = TypeUnion.empty
  type Union = Types#union

  type Length = _0
}

case class KNilOf[+A]() extends AnyEmptyKList {

  type Bound = A @uv
}

sealed trait AnyNonEmptyKList extends Any with AnyKList {

  type Head <: Bound
  def  head: Head

  type Tail <: AnyKList
  def  tail: Tail

  type Bound >: Tail#Bound <: Tail#Bound // NOTE again this is for forcing type inference

  type Types = Tail#Types#or[Head]
  type Union = Types#union

  type Length = Successor[Tail#Length]
}

case object AnyNonEmptyKList {

  type Of[+B] = AnyNonEmptyKList { type Bound <: B }
  type withBound[B] = AnyNonEmptyKList { type Bound = B }
}

case class KCons[+H <: T#Bound, +T <: AnyKList](val head: H, val tail: T) extends AnyNonEmptyKList {

  type Bound = T#Bound @uv
  type Head = H @uv
  type Tail = T @uv
}

case object AnyKList {

  type Of[+B] = AnyKList { type Bound <: B }
  type withBound[B] = AnyKList { type Bound = B }

  implicit def klistSyntax[L <: AnyKList](l: L)
  : syntax.KListSyntax[L] =
    syntax.KListSyntax[L](l)
}

// TODO should be a depfn
trait IsKCons[L <: AnyKList, H <: L#Bound, T <: AnyKList] {

  def h(l: L): H
  def t(l: L): T
}

case object IsKCons {

  implicit def default[
    H0 <: T0#Bound,
    T0 <: AnyKList
  ]
  : IsKCons[KCons[H0,T0], H0, T0] =
    new IsKCons[KCons[H0,T0], H0, T0] {

    def h(l: KCons[H0,T0]): H0 = l.head
    def t(l: KCons[H0,T0]): T0 = l.tail
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