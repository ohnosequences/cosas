
```scala
package ohnosequences.cosas.fns

import ohnosequences.cosas._, klists._

case object syntax {

  case class DepFn1Syntax[DF <: AnyDepFn1](val df: DF) extends AnyVal {

    final def at[I <: DF#In1, O <: DF#Out](f: I => O): App1[DF,I,O] =
      App1(f)

    final def ∘[F <: AnyDepFn1 { type Out <: DF#In1 }](f: F): Composition[F,DF] =
      new Composition[F,DF]
  }

  case class PredicateSyntax[P <: AnyPredicate](val p: P) extends AnyVal {

    // for constructing evidences
    def isTrueOn[X <: P#In1]:  P isTrueOn X  = App1 { _: X => True }
    def isFalseOn[X <: P#In1]: P isFalseOn X = App1 { _: X => False }
  }

  case class DepFn2Syntax[DF <: AnyDepFn2](val df: DF) extends AnyVal {

    final def at[X1 <: DF#In1, X2 <: DF#In2, Y <: DF#Out](f: (X1,X2) => Y): App2[DF,X1,X2,Y] =
      App2(f)

    def foldLeft[
      L <: AnyKList.Of[DF#In2],
      Z <: DF#Out,
      O <: DF#Out
    ](z: Z)(l: L)(implicit
      foldl: AnyApp3At[FoldLeft[DF], DF, Z, L] { type Y = O }
    ): O = foldl(df, z, l)

    def foldRight[
      L <: AnyKList.Of[DF#In1],
      Z <: DF#Out,
      O <: DF#Out
    ](z: Z)(l: L)(implicit
      foldr: AnyApp3At[FoldRight[DF], DF, Z, L] { type Y = O }
    ): O = foldr(df, z, l)

  }

  case class DepFn3Syntax[DF <: AnyDepFn3](val df: DF) extends AnyVal {

    final def at[
      X1 <: DF#In1, X2 <: DF#In2, X3 <: DF#In3,
      Y <: DF#Out
    ]
    (f: (X1,X2,X3) => Y): App3[DF,X1,X2,X3,Y] =
      App3(f)
  }

  case class DepFn1ApplyAt[DF <: AnyDepFn1, A <: DF#In1](val df: DF) extends AnyVal {

    final def apply[Y0 <: DF#Out](x1: A)(implicit app: AnyApp1At[DF,A] { type Y = Y0 }): Y0 =
      app(x1)
  }

  case class DepFn2ApplyAt[DF <: AnyDepFn2, A <: DF#In1, B <: DF#In2](val df: DF) extends AnyVal {

    final def apply[Y0 <: DF#Out](x1: A, x2: B)(implicit app: AnyApp2At[DF,A,B] { type Y = Y0 }): Y0 = app(x1,x2)
  }

  case class DepFn3ApplyAt[DF <: AnyDepFn3, A <: DF#In1, B <: DF#In2, C <: DF#In3](val df: DF) extends AnyVal {

    final def apply[Y0 <: DF#Out](x1: A, x2: B, x3: C)(implicit app: AnyApp3At[DF,A,B,C] { type Y = Y0 }): Y0 =
      app(x1,x2,x3)
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
[main/scala/cosas/fns/predicates.scala]: predicates.scala.md
[main/scala/cosas/fns/instances.scala]: instances.scala.md
[main/scala/cosas/fns/package.scala]: package.scala.md
[main/scala/cosas/fns/syntax.scala]: syntax.scala.md
[main/scala/cosas/fns/functions.scala]: functions.scala.md
[main/scala/cosas/subtyping.scala]: ../subtyping.scala.md
[main/scala/cosas/witness.scala]: ../witness.scala.md
[main/scala/cosas/equality.scala]: ../equality.scala.md
[main/scala/cosas/Nat.scala]: ../Nat.scala.md
[main/scala/cosas/Bool.scala]: ../Bool.scala.md