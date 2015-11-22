
```scala
package ohnosequences.cosas

import ohnosequences.cosas._, fns._, typeUnions._

trait AnyNat { n =>

  type Next <: AnyNat
  val next: Next

  type StrictlySmaller <: AnyTypeUnion

  implicit val me = this
}

case object zero extends AnyNat {

  type Next = Successor[zero.type]
  lazy val next: Next = Successor(zero)

  type StrictlySmaller = empty
}
trait AnyNonZeroNat extends AnyNat { nz =>

  type Next <: AnyNonZeroNat
  type Pred <: AnyNat
  val pred: Pred

  type StrictlySmaller = Pred#StrictlySmaller or Pred
}

case class Successor[N <: AnyNat](val pred: N) extends AnyNonZeroNat {

  type Next = Successor[Successor[N]]
  lazy val next: Next = Successor(this)
  type Pred = N
}

// TODO build nat rec depfn0

case object sum extends DepFn2[AnyNat, AnyNat, AnyNat] {

  implicit def zeroPlusAnything[N <: AnyNat]
  : AnyApp2At[sum.type, N, _0] { type Y = N } =
    App2 { (n: N, o: _0) => n }

  implicit def rec[X <: AnyNat, Y <: AnyNat, Z <: AnyNat](implicit
    sumN: AnyApp2At[sum.type, Successor[X], Y] { type Y = Z }
  )
  : AnyApp2At[sum.type, X, Successor[Y]] { type Y = Z }=
    App2 { (n: X, o: Successor[Y]) => sumN( Successor(n), o.pred ) }
}

```




[test/scala/cosas/asserts.scala]: ../../../test/scala/cosas/asserts.scala.md
[test/scala/cosas/DenotationTests.scala]: ../../../test/scala/cosas/DenotationTests.scala.md
[test/scala/cosas/EqualityTests.scala]: ../../../test/scala/cosas/EqualityTests.scala.md
[test/scala/cosas/DependentFunctionsTests.scala]: ../../../test/scala/cosas/DependentFunctionsTests.scala.md
[test/scala/cosas/KListsTests.scala]: ../../../test/scala/cosas/KListsTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/NatTests.scala]: ../../../test/scala/cosas/NatTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: ../../../test/scala/cosas/TypeUnionTests.scala.md
[main/scala/cosas/package.scala]: package.scala.md
[main/scala/cosas/types/package.scala]: types/package.scala.md
[main/scala/cosas/types/types.scala]: types/types.scala.md
[main/scala/cosas/types/parsing.scala]: types/parsing.scala.md
[main/scala/cosas/types/productTypes.scala]: types/productTypes.scala.md
[main/scala/cosas/types/syntax.scala]: types/syntax.scala.md
[main/scala/cosas/types/project.scala]: types/project.scala.md
[main/scala/cosas/types/denotations.scala]: types/denotations.scala.md
[main/scala/cosas/types/functionTypes.scala]: types/functionTypes.scala.md
[main/scala/cosas/types/serialization.scala]: types/serialization.scala.md
[main/scala/cosas/klists/replace.scala]: klists/replace.scala.md
[main/scala/cosas/klists/cons.scala]: klists/cons.scala.md
[main/scala/cosas/klists/klists.scala]: klists/klists.scala.md
[main/scala/cosas/klists/take.scala]: klists/take.scala.md
[main/scala/cosas/klists/package.scala]: klists/package.scala.md
[main/scala/cosas/klists/takeFirst.scala]: klists/takeFirst.scala.md
[main/scala/cosas/klists/toList.scala]: klists/toList.scala.md
[main/scala/cosas/klists/filter.scala]: klists/filter.scala.md
[main/scala/cosas/klists/pick.scala]: klists/pick.scala.md
[main/scala/cosas/klists/drop.scala]: klists/drop.scala.md
[main/scala/cosas/klists/map.scala]: klists/map.scala.md
[main/scala/cosas/klists/at.scala]: klists/at.scala.md
[main/scala/cosas/klists/syntax.scala]: klists/syntax.scala.md
[main/scala/cosas/klists/fold.scala]: klists/fold.scala.md
[main/scala/cosas/klists/noDuplicates.scala]: klists/noDuplicates.scala.md
[main/scala/cosas/klists/slice.scala]: klists/slice.scala.md
[main/scala/cosas/klists/find.scala]: klists/find.scala.md
[main/scala/cosas/records/package.scala]: records/package.scala.md
[main/scala/cosas/records/recordTypes.scala]: records/recordTypes.scala.md
[main/scala/cosas/records/syntax.scala]: records/syntax.scala.md
[main/scala/cosas/records/reorder.scala]: records/reorder.scala.md
[main/scala/cosas/typeUnions/typeUnions.scala]: typeUnions/typeUnions.scala.md
[main/scala/cosas/typeUnions/package.scala]: typeUnions/package.scala.md
[main/scala/cosas/fns/predicates.scala]: fns/predicates.scala.md
[main/scala/cosas/fns/instances.scala]: fns/instances.scala.md
[main/scala/cosas/fns/package.scala]: fns/package.scala.md
[main/scala/cosas/fns/syntax.scala]: fns/syntax.scala.md
[main/scala/cosas/fns/functions.scala]: fns/functions.scala.md
[main/scala/cosas/subtyping.scala]: subtyping.scala.md
[main/scala/cosas/witness.scala]: witness.scala.md
[main/scala/cosas/equality.scala]: equality.scala.md
[main/scala/cosas/Nat.scala]: Nat.scala.md
[main/scala/cosas/Bool.scala]: Bool.scala.md