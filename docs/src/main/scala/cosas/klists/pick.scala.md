
```scala
package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

class split[E] extends DepFn1[AnyKList, (AnyKList, E, AnyKList)]

case object split extends splitFoundInTail {

  implicit def foundInHead[
    E <: T#Bound,
    T <: AnyKList
  ]: AnyApp1At[split[E], E :: T] { type Y = (*[T#Bound], E, T) } =
     App1 { (s: E :: T) => (*[T#Bound], s.head, s.tail) }
}

sealed trait splitFoundInTail {

  implicit def foundInTail[
    E <: T#Bound,
    // picking from H :: T
    H <: OL#Bound, T  <: AnyKList { type Bound = OL#Bound },
    OL <: AnyKList,
    OR <: AnyKList  {type Bound = OL#Bound }
  ](implicit
      l: AnyApp1At[split[E], T] { type Y = (OL, E, OR) }
  )
  : AnyApp1At[split[E], H :: T] { type Y = (H :: OL, E, OR) } =
    App1 { (s: H :: T) => val (lo, e, ro) = l(s.tail); (s.head :: lo, e, ro) }
}

class splitS[E] extends DepFn1[AnyKList, (AnyKList, E, AnyKList)]

case object splitS extends splitSFoundInTail {

  implicit def foundInHead[
    E <: T#Bound, H <: E,
    T <: AnyKList { type Bound >: H }
  ]: AnyApp1At[splitS[E], H :: T] { type Y = (*[T#Bound], H, T) } =
     App1 { (s: H :: T) => (*[T#Bound], s.head, s.tail) }
}

sealed trait splitSFoundInTail {

  implicit def foundInTail[
    E >: X <: T#Bound,
    // picking from H :: T
    H <: OL#Bound, T  <: AnyKList { type Bound = OL#Bound },
    OL <: AnyKList,
    X,
    OR <: AnyKList  {type Bound = OL#Bound }
  ](implicit
      l: AnyApp1At[splitS[E], T] { type Y = (OL, X, OR) }
  )
  : AnyApp1At[splitS[E], H :: T] { type Y = (H :: OL, X, OR) } =
    App1 { (s: H :: T) => val (lo, x, ro) = l(s.tail); (s.head :: lo, x, ro) }
}


class pick[E] extends DepFn1[AnyKList, (E, AnyKList)]

case object pick extends pickFoundInTail {

  implicit def foundInHead[
    E,
    T <: AnyKList { type Bound >: E }
  ]: AnyApp1At[pick[E], E :: T] { type Y = (E, T) } =
     App1 { (s: E :: T) => (s.head, s.tail) }
}

sealed trait pickFoundInTail {

  implicit def foundInTail[
    E, H <: TO#Bound,
    T  <: AnyKList { type Bound = TO#Bound },
    TO <: AnyKList
  ](implicit
      l: AnyApp1At[pick[E], T] { type Y = (E, TO) }
  ): AnyApp1At[pick[E], H :: T] { type Y = (E, H :: TO) } =
     App1 { (s: H :: T) => val (e, t) = l(s.tail); (e, s.head :: t) }
}


class pickS[E] extends DepFn1[AnyKList, (E, AnyKList)]

case object pickS extends pickSFoundInTail  {

  implicit def foundInHead[E <: T#Bound, H <: E, T <: AnyKList { type Bound >: H }]
  : AnyApp1At[pickS[E], H :: T] { type Y = (H, T) } =
    App1 { (s: H :: T) => (s.head, s.tail) }
}

trait pickSFoundInTail {

  implicit def foundInTail[
    X, E >: X, H <: TO#Bound,
    T  <: AnyKList { type Bound >: H },
    TO <: AnyKList
  ](implicit
      l: AnyApp1At[pickS[E], T] { type Y = (X, TO) }
  )
  : AnyApp1At[pickS[E], H :: T] { type Y = (X, H :: TO) } =
    App1 { (s: H :: T) => val (e, t) = l(s.tail); (e, s.head :: t) }
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