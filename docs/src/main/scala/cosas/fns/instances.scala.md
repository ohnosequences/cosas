
```scala
package ohnosequences.cosas.fns

import ohnosequences.cosas._

trait AnyFn1 extends Any with AnyDepFn1 {

  def f: In1 => Out
}
// std functions as depfns
case class Fn1[A,B](val f: A => B) extends AnyVal with AnyFn1 with DepFn1[A,B]

case object Fn1 {

  implicit def defaultApp[A,B](df: Fn1[A,B]): AnyApp1At[DepFn1[A,B], A] { type Y = B } =
    AppFn1(df.f)

  case class AppFn1[A,B](val f: A => B) extends AnyVal with AnyApp1At[DepFn1[A,B],A] {

    type Y = B

    def apply(x: A): B = f(x)
  }
}

trait AnyFn2 extends Any with AnyDepFn2 {

  def f: (In1,In2) => Out
}
case class Fn2[A, B, C](val f: (A,B) => C) extends AnyVal with AnyFn2 with DepFn2[A, B, C]

case object Fn2 {

  implicit def defaultApp[A, B, C](df: Fn2[A, B, C]):
    AnyApp2At[Fn2[A, B, C], A, B] { type Y = C } = App2 { (a: A, b: B) => df.f(a, b) }
}

case object identity extends DepFn1[Any, Any] {

  implicit def default[X]: AnyApp1At[identity.type,X] { type Y = X } = identity at { x: X => x }
}

class as[X, Y >: X] extends DepFn1[X,Y]

case object as {

  def apply[X,Y >: X]: as[X,Y] = new as[X,Y]

  implicit def default[A, B >: A]: AnyApp1At[as[A,B],A] {type Y = B } = App1 { a: A => a }
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