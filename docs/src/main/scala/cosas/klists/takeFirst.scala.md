
```scala
package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

// TODO this is not a good name. l pick /[Int :: String :: *[Any]]
class takeFirst[Q <: AnyKList] extends DepFn1[AnyKList, Q]

case object takeFirst {

  implicit def empty[S <: AnyKList { type Bound = X }, X]
  : AnyApp1At[takeFirst[*[X]], S] { type Y = *[X] } =
    App1 { s: S => *[X] }

  implicit def nonEmpty[
    TailToTake <: AnyKList { type Bound >: HeadToTake }, From <: AnyKList, Rest <: AnyKList,
    HeadToTake >: SHeadToTake, SHeadToTake
  ](implicit
    pick: AnyApp1At[pickS[HeadToTake], From] { type Y = (SHeadToTake, Rest) },
    take: AnyApp1At[takeFirst[TailToTake], Rest] { type Y = TailToTake }
  )
  : AnyApp1At[takeFirst[HeadToTake :: TailToTake], From] { type Y = SHeadToTake :: TailToTake } =
    App1 { s: From => { val (h, t) = pick(s); h :: take(t) } }
}

// class takeFirstS[Q <: AnyKList] extends DepFn1[AnyKList, Q]
//
// case object takeFirstS {
//
//   implicit def empty[S <: AnyKList, X0 <: X, X]
//   : AnyApp1At[takeFirstS[*[X]], S] { type Y = *[X0] } =
//     App1 { s: S => *[X0] }
//
//   implicit def nonEmpty[
//     From <: AnyKList, Rest <: AnyKList,
//     SHeadToTake, HeadToTake >: SHeadToTake <: TailToTake#Bound,
//     STailToTake <: TailToTake, TailToTake >: STailToTake <: AnyKList { type Bound >: SHeadToTake }
//   ](implicit
//     pick: AnyApp1At[pickS[HeadToTake], From] { type Y = (SHeadToTake, Rest) },
//     take: AnyApp1At[takeFirstS[TailToTake], Rest] { type Y = STailToTake }
//   )
//   : AnyApp1At[takeFirstS[HeadToTake :: TailToTake], From] { type Y = SHeadToTake :: STailToTake } =
//     App1 { s: From => { val (h, t): (SHeadToTake, Rest) = pick(s); h :: take(t) } }
// }

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