
```scala
package ohnosequences.cosas.types

import ohnosequences.cosas._, fns._, klists._

case object syntax {


  final case class TypeSyntax[T <: AnyType](val tpe: T) extends AnyVal {

    final def :=[@specialized V <: T#Raw](v: V): T := V = Denotation[T, V](tpe, v)
    final def apply[@specialized V <: T#Raw](v: V): T := V = Denotation[T, V](tpe, v)

    final def ==>[S <: AnyType](s: S): T ==> S = new (T ==> S)(tpe, s)
  }

  final case class DenotationSyntax[D <: AnyDenotation](val d: D) extends AnyVal {

    def =~=(w: D#Tpe := D#Value): Boolean = d.value == w.value
  }

  final case class AnyProductTypeSyntax[L <: AnyProductType](val l: L) extends AnyVal {

    def :×:[H0 <: L#Types#Bound](h: H0): H0 :×: L = new :×:[H0,L](h,l)
  }

  final case class AnyProductTypeDenotationSyntax[L <: AnyProductType, Vs <: L#Raw](val vs: L := Vs) extends AnyVal {

    def project[T <: AnyType, V <: T#Raw](t: T)(implicit
      p: AnyApp1At[Project[L,T], L := Vs] { type Y = T := V }
    )
    : T := V =
      p( vs )

    def at[D <: AnyDenotation, N <: AnyNat](position: N)(implicit
      getAt: AnyApp1At[Vs at N, Vs] { type Y = D }
    )
    : D =
      getAt(vs.value)

    def getFirst[D <: AnyDenotation { type Tpe = T }, T <: AnyType](tpe: T)(implicit
      get: AnyApp1At[find[D], Vs] { type Y = D }
    )
    : D =
      get(vs.value)
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