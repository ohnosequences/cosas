
```scala
package ohnosequences.cosas

package object fns {

  implicit def toDepFn1[A, B](f: A => B): Fn1[A, B] = Fn1(f)
  implicit def toDepFn2[A, B, C](f: (A, B) => C): Fn2[A, B, C] = Fn2(f)

  type isTrueOn[P <: AnyPredicate, X <: P#In1] = AnyApp1At[P, X] { type Y = Unit }
  // type isFalseOn[P <: AnyPredicate, X <: P#In1] = AnyApp1At[P, X] { type Y = False }
  // def ¬[P <: AnyPredicate](p: P): Not[P] = Not(p)
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