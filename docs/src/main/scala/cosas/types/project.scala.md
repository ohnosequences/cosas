
```scala
package ohnosequences.cosas.types

import ohnosequences.cosas._, fns._, klists._

class Project[Ts <: AnyProductType, T <: AnyType] extends DepFn1[
  AnyDenotation { type Tpe = Ts },
  AnyDenotation { type Tpe = T }
]

case object Project extends ProjectInTail {

  implicit def foundInHead[
    H <: Ts#Types#Bound { type Raw >: V }, V,
    Ts <: AnyProductType { type Raw >: Ds }, Ds <: AnyKList { type Bound = AnyDenotation }
  ]
  : AnyApp1At[
      Project[H :×: Ts, H],
      (H :×: Ts) := ((H := V) :: Ds)
    ] { type Y = H := V } =
    App1 { x: (H :×: Ts) := ((H := V) :: Ds) => x.value.head  }
}

trait ProjectInTail {

  implicit def foundInTail[
    H <: Ts#Types#Bound { type Raw >: V }, V,
    Ts <: AnyProductType { type Raw >: Ds }, Ds <: AnyKList { type Bound = AnyDenotation },
    P <: AnyType { type Raw >: W }, W
  ]
  (implicit
    proj: AnyApp1At[Project[Ts,P], Ts := Ds] { type Y = P := W }
  )
  : AnyApp1At[
    Project[H :×: Ts, P],
    (H :×: Ts) := ((H := V) :: Ds)
  ] { type Y = P := W } = 
    App1 { x: (H :×: Ts) := ((H := V) :: Ds) => proj( new (Ts := Ds)(x.value.tail)) }
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
[main/scala/cosas/types/package.scala]: package.scala.md
[main/scala/cosas/types/types.scala]: types.scala.md
[main/scala/cosas/types/parsing.scala]: parsing.scala.md
[main/scala/cosas/types/productTypes.scala]: productTypes.scala.md
[main/scala/cosas/types/syntax.scala]: syntax.scala.md
[main/scala/cosas/types/project.scala]: project.scala.md
[main/scala/cosas/types/denotations.scala]: denotations.scala.md
[main/scala/cosas/types/functionTypes.scala]: functionTypes.scala.md
[main/scala/cosas/types/serialization.scala]: serialization.scala.md
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