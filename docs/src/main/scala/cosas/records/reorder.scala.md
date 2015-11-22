
```scala
package ohnosequences.cosas.types

import ohnosequences.cosas._, types._, klists._, fns._

class Reorder[Ts <: AnyProductType, Vs <: AnyKList { type Bound = AnyDenotation }] extends DepFn1[
  Vs,
  Ts#Raw
]

case object Reorder {

  implicit def empty[S <: AnyKList { type Bound = AnyDenotation }]
  : AnyApp1At[Reorder[unit, S], S] { type Y = *[AnyDenotation] } =
    App1 { s: S => *[AnyDenotation] }

  implicit def nonEmpty[
    TailToTake <: AnyProductType { type Raw >: STailToTake },
    From <: AnyKList { type Bound = AnyDenotation }, Rest <: AnyKList { type Bound = AnyDenotation },
    HeadToTake <: TailToTake#Types#Bound { type Raw >: V}, V,
    STailToTake <: AnyKList { type Bound = AnyDenotation }
  ](implicit
    pick: AnyApp1At[pickByType[HeadToTake], From] { type Y = ((HeadToTake := V), Rest) },
    take: AnyApp1At[Reorder[TailToTake, Rest], Rest] { type Y = STailToTake }
  )
  : AnyApp1At[Reorder[HeadToTake :×: TailToTake, From], From] { type Y = (HeadToTake := V) :: STailToTake } =
    App1 { s: From => { val (h, t) = pick(s); h :: take(t) } }
}

class pickByType[T <: AnyType] extends DepFn1[
  AnyKList { type Bound = AnyDenotation},
  (AnyDenotation { type Tpe = T }, AnyKList { type Bound = AnyDenotation})
]

case object pickByType extends pickInTailReally {
  implicit def foundInHead[
    H <: AnyType { type Raw >: V }, V,
    Ds <: AnyKList { type Bound = AnyDenotation }
  ]
  : AnyApp1At[
      pickByType[H],
      (H := V) :: Ds
    ] { type Y = (H := V, Ds) } =
    App1 { x: (H := V) :: Ds => (x.head, x.tail)  }
}

trait pickInTailReally {

implicit def foundInTail[
  H <: AnyType { type Raw >: V }, V,
  Ds <: AnyKList { type Bound = AnyDenotation },
  Rest <: AnyKList { type Bound = AnyDenotation },
  P <: AnyType { type Raw >: W }, W
]
(implicit
  pick: AnyApp1At[pickByType[P], Ds] { type Y = (P := W, Rest) }
)
: AnyApp1At[
  pickByType[P],
  (H := V) :: Ds
] {type Y = (P := W, (H := V) :: Rest) } =
  App1 { x: (H := V) :: Ds => (pick(x.tail)._1, x.head :: pick(x.tail)._2 ) }
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
[main/scala/cosas/klists/replace.scala]: ../klists/replace.scala.md
[main/scala/cosas/klists/cons.scala]: ../klists/cons.scala.md
[main/scala/cosas/klists/klists.scala]: ../klists/klists.scala.md
[main/scala/cosas/klists/take.scala]: ../klists/take.scala.md
[main/scala/cosas/klists/package.scala]: ../klists/package.scala.md
[main/scala/cosas/klists/takeFirst.scala]: ../klists/takeFirst.scala.md
[main/scala/cosas/klists/toList.scala]: ../klists/toList.scala.md
[main/scala/cosas/klists/filter.scala]: ../klists/filter.scala.md
[main/scala/cosas/klists/pick.scala]: ../klists/pick.scala.md
[main/scala/cosas/klists/drop.scala]: ../klists/drop.scala.md
[main/scala/cosas/klists/map.scala]: ../klists/map.scala.md
[main/scala/cosas/klists/at.scala]: ../klists/at.scala.md
[main/scala/cosas/klists/syntax.scala]: ../klists/syntax.scala.md
[main/scala/cosas/klists/fold.scala]: ../klists/fold.scala.md
[main/scala/cosas/klists/noDuplicates.scala]: ../klists/noDuplicates.scala.md
[main/scala/cosas/klists/slice.scala]: ../klists/slice.scala.md
[main/scala/cosas/klists/find.scala]: ../klists/find.scala.md
[main/scala/cosas/records/package.scala]: package.scala.md
[main/scala/cosas/records/recordTypes.scala]: recordTypes.scala.md
[main/scala/cosas/records/syntax.scala]: syntax.scala.md
[main/scala/cosas/records/reorder.scala]: reorder.scala.md
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