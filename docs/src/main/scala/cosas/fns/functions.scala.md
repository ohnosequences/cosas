
```scala
package ohnosequences.cosas.fns

import ohnosequences.cosas._
```

Dependent functions aka dependent products

```scala
trait AnyDepFn extends Any {
  type Out
}
trait AnyDepFn0 extends AnyDepFn
trait DepFn0[O] extends AnyDepFn0 {
  type Out = O
}

trait AnyDepFn1 extends Any with AnyDepFn {
  type In1
}
case object AnyDepFn1 {

  implicit def depFn1Syntax[DF <: AnyDepFn1](df: DF): syntax.DepFn1Syntax[DF] =
    syntax.DepFn1Syntax(df)

  implicit def depFn1ApplySyntax[
    DF0 <: AnyDepFn1,
    X10 <: DF0#In1
  ](df: DF0): syntax.DepFn1ApplyAt[DF0,X10] =
    syntax.DepFn1ApplyAt(df)

  implicit def asPredicateSyntax[DF <: AnyDepFn1 { type Out = Unit }](f: DF): syntax.PredicateLikeSyntax[DF] =
    syntax.PredicateLikeSyntax(f)
}

trait DepFn1[I,O] extends Any with AnyDepFn1 {
  type In1 = I
  type Out = O
}

trait AnyDepFn2 extends Any with AnyDepFn {
  type In1
  type In2
}
case object AnyDepFn2 {

  implicit def depFn2Syntax[DF <: AnyDepFn2](df: DF): syntax.DepFn2Syntax[DF] =
    syntax.DepFn2Syntax(df)

  implicit def depFn2ApplySyntax[
    DF0 <: AnyDepFn2,
    A0 <: DF0#In1,
    B0 <: DF0#In2
  ](df: DF0): syntax.DepFn2ApplyAt[DF0,A0,B0] =
    syntax.DepFn2ApplyAt(df)
}
trait DepFn2[I1,I2,O] extends Any with AnyDepFn2 {
  type In1 = I1
  type In2 = I2
  type Out = O
}

trait AnyDepFn3 extends AnyDepFn {
  type In1
  type In2
  type In3
}
case object AnyDepFn3 {

  implicit def depFn3Syntax[DF <: AnyDepFn3](df: DF): syntax.DepFn3Syntax[DF] =
    syntax.DepFn3Syntax(df)

  implicit def depFn3ApplySyntax[
    DF0 <: AnyDepFn3,
    A0 <: DF0#In1, B0 <: DF0#In2, C0 <: DF0#In3
  ](df: DF0): syntax.DepFn3ApplyAt[DF0,A0,B0,C0] =
    syntax.DepFn3ApplyAt(df)
}
trait DepFn3[I1, I2, I3, O] extends AnyDepFn3 {
  type In1 = I1
  type In2 = I2
  type In3 = I3
  type Out = O
}

trait AnyDepFn1Composition extends AnyDepFn1 {

  type First <: AnyDepFn1 { type Out <: Second#In1 }
  type Second <: AnyDepFn1

  type In1 = First#In1
  type Out = Second#Out
}

class Composition[
  F <: AnyDepFn1 { type Out <: S#In1 },
  S <: AnyDepFn1
]
extends AnyDepFn1Composition {

  type First = F
  type Second = S
}
case object AnyDepFn1Composition {

  implicit def appForComposition[
    SF <: AnyDepFn1Composition { type Second <: AnyDepFn1 { type In1 >: M0; type Out >: O } },
    X10 <: SF#First#In1,
    M0,
    O
  ](implicit
    appF: AnyApp1At[SF#First, X10] { type Y = M0 },
    appS: AnyApp1At[SF#Second, M0] { type Y = O }
  )
  : AnyApp1At[SF,X10] { type Y = O } =
    App1 { x1: X10 => appS(appF(x1)) }
}

// Flips the arguments of a DepFn2, see snoc for example
trait AnyFlip extends AnyDepFn2 {

  type FlippedF <: AnyDepFn2

  type In1 = FlippedF#In2
  type In2 = FlippedF#In1

  type Out = FlippedF#Out
}
class Flip[F <: AnyDepFn2] extends AnyFlip {

  type FlippedF = F
}

case object AnyFlip {

  implicit def flip[
    F <: AnyFlip {
      type FlippedF <: AnyDepFn2 { type In1 >: I2; type In2 >: I1; type Out >: O }
    },
    I1, I2, O
  ](implicit
    appF: AnyApp2At[F#FlippedF, I2, I1] { type Y = O }
  ): AnyApp2At[F, I1, I2] { type Y = O } =
  App2 { (in1: I1, in2: I2) => appF(in2, in1) }
}
```

dependent function application machinery. These are to be thought of as the building blocks for terms of a dependent function type.

```scala
trait AnyApp extends Any {

  type DepFn <: AnyDepFn
  type Y <: DepFn#Out
}

trait AnyApp0 extends Any with AnyApp {

  type DepFn <: AnyDepFn0

  def apply: Y
}

trait AnyApp1 extends Any with AnyApp {

  type DepFn <: AnyDepFn1
  type X1 <: DepFn#In1

  def apply(in: X1): Y
}

trait AnyApp1At[DF <: AnyDepFn1,A0 <: DF#In1] extends Any with AnyApp1 {

  type DepFn = DF;
  type X1 = A0
}

trait AnyApp2 extends Any with AnyApp {

  type DepFn <: AnyDepFn2
  type X1 <: DepFn#In1; type X2 <: DepFn#In2

  def apply(in1: X1, in2: X2): Y
}

trait AnyApp2At[DF <: AnyDepFn2,A0 <: DF#In1,B0 <: DF#In2] extends Any with AnyApp2 {

  type DepFn = DF;
  type X1 = A0; type X2 = B0
}

trait AnyApp3 extends Any with AnyApp {

  type DepFn <: AnyDepFn3
  type X1 <: DepFn#In1; type X2 <: DepFn#In2; type X3 <: DepFn#In3

  def apply(in1: X1, in2: X2, in3: X3): Y
}

trait AnyApp3At[DF <: AnyDepFn3,A0 <: DF#In1,B0 <: DF#In2,C0 <: DF#In3] extends Any with AnyApp3 {

  type DepFn = DF;
  type X1 = A0; type X2 = B0; type X3 = C0
}

case class App1[
  DF <: AnyDepFn1,
  I <: DF#In1,
  O <: DF#Out
]
(val does: I => O) extends AnyVal with AnyApp1At[DF,I] {

  type Y = O

  final def apply(in: X1): Y =
    does(in)
}

case class App2[
  DF <: AnyDepFn2,
  I1 <: DF#In1, I2 <: DF#In2,
  O <: DF#Out
]
(val does: (I1,I2) => O) extends AnyVal with AnyApp2At[DF,I1,I2] {

  type Y = O

  final def apply(in1: X1, in2: X2): Y =
    does(in1,in2)
}

case class App3[
  DF <: AnyDepFn3,
  I1 <: DF#In1, I2 <: DF#In2, I3 <: DF#In3,
  O <: DF#Out
]
(val does: (I1,I2,I3) => O) extends AnyVal with AnyApp3At[DF,I1,I2,I3] {

  type Y = O

  final def apply(in1: X1, in2: X2, in3: X3): Y =
    does(in1,in2,in3)
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