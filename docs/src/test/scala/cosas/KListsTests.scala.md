
```scala
package ohnosequences.cosas.tests

import ohnosequences.cosas._, klists._, fns._
import KListTestsContext._

case object KListTestsContext {

  trait A { val boo: Boolean }

  case object A0 extends A { val boo = true }
  case object A1 extends A { val boo = false }

}

class KListTests extends org.scalatest.FunSuite {

  test("HLists are KLists with bound Any") {

    val s = "12312" :: *[Any]

    val sh = s.head
    val st = s.tail

    assert ( sh === "12312" )
    assert ( st === *[Any] )


    val sib = "12312" :: 121312 :: true :: *[Any]

    val sibh = sib.head
    val sibt = sib.tail

    assert ( sib.head === "12312" )
  }

  test("can use bounds abstractly") {

    trait Foo
    trait Bar extends Foo { val x: String }
    trait Argh extends Bar { val z: Int }

    def hola[L <: AnyNonEmptyKList.Of[Bar]](l: L): String = l.head.x
    def id[X <: AnyNonEmptyKList.Of[Argh]](x: X): AnyNonEmptyKList.Of[Bar] = x
    def idHola[X <: AnyNonEmptyKList.Of[Argh]](x: X): String = hola(id(x))
    def as[B0 <: B, B, L <: AnyKList.Of[B0]](l: L): AnyKList.Of[B] = l

  }

  test("KLists are covariant in bound and values") {

    val oh = A0 :: A1 :: A0 :: *[A]
    val oh_any: AnyKList.Of[Any] = oh

    val uh: AnyKList.Of[A0.type] = A0 :: A0 :: A0 :: *[A0.type]
    val uh_A: AnyKList.Of[A] = uh

    assert{ oh.head.boo === true }
    assert{ oh.tail.head.boo === false }

    def foo[L <: AnyNonEmptyKList.Of[A]](l: L): Boolean = l.head.boo

    assert{ foo(oh) === true }
  }

  test("can convert KLists to lists of their bound") {

    assert {
      (A0 :: A1 :: A0 :: *[A]).toList === (A0 :: A1 :: A0 :: Nil)
    }

    assert {
      ("hola" :: "scalac" :: *[String]).toList === ("hola" :: "scalac" :: Nil)
    }

    assert {
      ("hola" :: "scalac" :: *[String]).asList === ("hola" :: "scalac" :: Nil)
    }

    @scala.annotation.tailrec
    def buildKList_rec(size: Int, l: AnyKList.withBound[Int]): AnyKList.withBound[Int] = size match {

      case 0 => l
      case x: Int => buildKList_rec(x-1, x :: l)
    }

    def buildKList(size: Int): AnyKList.withBound[Int] = buildKList_rec(size, *[Int])

    // no SO
    val z = buildKList(1000000).asList
  }

  test("can convert KLists to lists of a supertype of their bound") {

    assert {
      *[A].toListOf[A] === Nil
    }

    assert {
      (A0 :: *[A0.type]).toListOf[A] === List[A](A0)
    }

    assert {
      ("hola" :: "scalac" :: *[String]).toListOf[Any] === List[Any]("hola","scalac")
    }

    assert {
      (("hola" :: "scalac" :: *[String]).asList : List[Any]) === List[Any]("hola","scalac")
    }

  }

  test("KList cons/uncons") {

    val z = true :: "hola" :: 2 :: *[Any]

    // TODO better syntax for this
    assert{ cons(z.uncons._1, z.uncons._2) === z }
  }

  test("can access elements by index") {

    val sbi = "hola" :: true :: 2 :: *[Any]
    assert {
      ("hola" :: "scalac" :: "que tal" :: *[String] at _0 ) === "hola"
    }

    assert {
      sbi === (sbi at _0) :: (sbi at _1) :: (sbi at _2) :: *[Any]
    }
  }

  test("can take and drop from a KList") {

    type SBI = String :: Boolean :: Int :: *[Any]
    val sbi: SBI = "hola" :: true :: 2 :: *[Any]

    assert { (sbi take _1) === ("hola" :: *[Any]) }
    assert { (sbi take _2) === ("hola" :: true :: *[Any]) }
    assert { (sbi take _3) === sbi }

    assert { (sbi drop _0) === sbi }
    assert { (sbi drop _1) ===  true :: 2 :: *[Any] }
    assert { (sbi drop _2) ===  2 :: *[Any] }
    assert { (sbi drop _3) ===  *[Any] }
  }

  test("can take segments from a KList") {

    val z = "a" :: 'b' :: true :: 2 :: "cd" :: false :: *[Any]

    type SBI = String :: Boolean :: Int :: *[Any]
    val sbi: SBI = "hola" :: true :: 2 :: *[Any]

    assert { (z drop _2 take _3) ===  true :: 2 :: "cd" :: *[Any] }
    assert { (z drop _2 take _3) ===  z.slice(_2, _3) }

    // empty segments will be optimized
    assert { sbi.slice(_2, _2) === *[Any] }
    assert { sbi.slice(_0, _0) === *[Any] }

    def stupidSpan[N <: AnyNat, L <: AnyKList](l: L, n: N): KNil[L#Bound] = l.slice(n, _0)

    assert { stupidSpan(sbi, _4) === *[Any] }
  }

  test("can pick elements by type") {

    assert { (true :: "que tal" :: *[Any]).find[Boolean] === true }
    assert { (true :: "que tal" :: *[Any]).find[String] === "que tal" }
    assert { (true :: "que tal" :: "scalac" :: *[Any]).find[String] === "que tal" }

    assert { (true :: "que tal" :: "scalac" :: *[Any] at _2) === "scalac" }

    assertResult( ( "que tal", true :: "scalac" :: *[Any]) ) {
      (true :: "que tal" :: "scalac" :: *[Any] pick /[String])
    }

    trait A
    trait B extends A
    case object B1 extends B; type B1 = B1.type
    case object B2 extends B; type B2 = B2.type

    val ab1b2b = (new A {}) :: (B1: B1) :: (B2: B2) :: (new B {}) :: *[A]

    // NOTE most specific type
    val b1: B1 = ab1b2b.pickS(/[B])._1
    assert { (ab1b2b pickS /[B])._1 === B1 }

    val b1a = (B1: B1) :: *[A]
    val b1_bis: B1 = b1a.findS(/[B])
  }

  test("KList split/splitS") {

    // WIP
    assertResult((
      true :: 'b' :: List(2,3) :: *[Any],
      "hola",
      32 :: 'a' :: *[Any]
    )) {
      (true :: 'b' :: List(2,3) :: "hola" :: 32 :: 'a' :: *[Any]) split /[String]
    }

    trait A
    trait B extends A
    case object B1 extends B; type B1 = B1.type
    case object B2 extends B; type B2 = B2.type
    val a: A = new A {}
    val b: B = new B {}

    val ab1b2b = a :: (B1: B1) :: (B2: B2) :: b :: *[A]

    assertResult((
      a :: *[A],
      B1,
      (B2: B2) :: b :: *[A]
    )) {
      ab1b2b splitS /[B]
    }
  }

  test("can replace segments of Klists") {

    val z = "a" :: 'b' :: true :: 2 :: "cd" :: false :: *[Any]
    val s = false :: 1 :: "argh" :: *[Any]

    assert { (z replaceFirst *[Any]) === z }
    assert { (z replaceFirst s) === ("argh" :: 'b' :: false :: 1 :: "cd" :: false :: *[Any]) }
    assert { (z replaceFirst "hola" :: *[Any]) === "hola" :: z.tail }
  }

  test("can take a sublist by type") {

      val z = "a" :: 'b' :: true :: 2 :: "cd" :: false :: *[Any]

      assert { (z takeFirst /[Boolean :: Int :: *[Any]]) === true :: 2 :: *[Any] }
  }

  test("can map over KLists") {

    case object f extends DepFn1[Any,String] {

      implicit def atAny[X <: Any]: AnyApp1At[f.type,X] { type Y = String } = f at { a: X => a.toString }
    }

    val z: *[Any] = *[Any]
    val zz: Boolean :: *[Any] = true :: *[Any]
    val zzz: Int :: Boolean :: *[Any] = 2 :: true :: *[Any]

    val uh: Int :: Boolean :: *[AnyVal] = 2 :: true :: *[AnyVal]
    val tostr: Fn1[Any,String] = Fn1 { x: Any => x.toString }

    assert { (uh  map tostr) === "2" :: "true" :: *[String] }

    assert { zzz.map(identity) === zzz }

    assert {
      (zzz map f) === "2" :: "true" :: *[String]
    }

    assert {
      ("hola" :: "scalac" :: *[Any]).map(f) === "hola" :: "scalac" :: *[String]
    }

    assert {
      ("hola" :: "scalac" :: *[String]).map(as[String,Any]) === "hola" :: "scalac" :: *[Any]
    }
  }

  case object sum extends DepFn2[Int,Int,Int] {

    implicit lazy val default: App2[sum.type, Int,Int,Int] =
      sum at { (a: Int, b: Int) => (a + b): Int }
  }

  test("can foldLeft over KLists") {

    val flEmpty = new FoldLeft[sum.type]
    val flAgain = new FoldLeft[sum.type]

    assertResult(0) { flEmpty(sum, 0, *[Int]) }
    assertResult(2) { flAgain(sum, 0, 2 :: *[Int]) }

    assertResult(6) {
      sum.foldLeft(0)(3 :: 2 :: 1 :: *[Int])
    }

    val z = snoc.foldLeft(*[Int])(1 :: *[Int])

    assertResult(1 :: 2 :: 3 :: 4 :: 5 :: 6 :: *[Int]) {
      snoc.foldLeft(4 :: 5 :: 6 :: *[Int])(3 :: 2 :: 1 :: *[Int])
    }

    // snoc.foldLeft(Nil) == reverse
    assertResult(true :: 'b' :: "hola" :: 1 :: *[Any]) {
      snoc.foldLeft(*[Any])(1 :: "hola" :: 'b' :: true :: *[Any])
    }

    val l = 1 :: "hola" :: 'b' :: true :: *[Any]
    val f: (String, Any) => String = { (str, a) => s"${a.toString} :: ${str}" }

    assert {
      Fn2(f).foldLeft("")(l) ===
      l.asList.foldLeft("")(f) // std
    }
  }

  test("can foldRight over KLists") {

    val flEmpty = new FoldRight[sum.type]
    val flAgain = new FoldRight[sum.type]

    assertResult(0) { flEmpty(sum, 0, *[Int]) }
    assertResult(2) { flAgain(sum, 0, 2 :: *[Int]) }

    assertResult(6) {
      sum.foldRight(0)(3 :: 2 :: 1 :: *[Int])
    }

    val z = cons.foldRight(*[Int])(1 :: *[Int])(FoldRight.cons[
      cons.type,
      *[Int],
      Int, *[Int],
      *[Int], Int :: *[Int]
    ])

    assertResult(1 :: 2 :: 3 :: 4 :: 5 :: 6 :: *[Int]) {
      cons.foldRight(4 :: 5 :: 6 :: *[Int])(1 :: 2 :: 3 :: *[Int])
    }

    assertResult(1 :: "hola" :: 'b' :: true :: *[Any]) {
      cons.foldRight(*[Any])(1 :: "hola" :: 'b' :: true :: *[Any])
    }


    val l = 1 :: "hola" :: 'b' :: true :: *[Any]
    val f: (Any, String) => String = { (a, str) => s"${a.toString} :: ${str}" }

    assert {
      Fn2(f).foldRight("")(l) ===
      l.asList.foldRight("")(f) // std
    }
  }

  test("can concatenate KLists") {
    // NOTE: (l ++ m) is just a syntax for cons.foldRight(m)(l)

    val hola: Boolean :: String :: String :: *[Any] =
      (true :: "que tal" :: *[Any]) ++ ("hola" :: *[Any])

    assertResult(true :: "que tal" :: "hola" :: 2 :: *[Any]) {
      (true :: "que tal" :: *[Any]) ++ ("hola" :: 2 :: *[Any])
    }

    // with less specific type
    assertResult(1 :: 2 :: 3 :: 4 :: 5 :: 6 :: *[Any]) {
      (1 :: 2 :: 3 :: *[Int]) ++ (4 :: 5 :: 6 :: *[Any])
    }

    assertResult(1 :: 2 :: 3 :: 4 :: 5 :: 6 :: *[Int]) {
      (1 :: 2 :: 3 :: *[Int]) ++ (4 :: 5 :: 6 :: *[Int])
    }

  }

  test("can reverse KLists") {
    // NOTE: l.reverse is just a syntax for snoc.foldLeft(KNil)(l)

    val hola: String :: String :: Boolean :: *[Any] =
      (true :: "que tal" :: "hola" :: *[Any]).reverse

    assertResult(2 :: *[Int]) {
      (2 :: *[Int]).reverse
    }

    assertResult(1 :: 2 :: 3 :: 4 :: *[Int]) {
      (4 :: 3 :: 2 :: 1 :: *[Int]).reverse
    }

  }

  test("can filter KLists") {

    case object isAnyVal extends PredicateOver[Any] {

      implicit def yes[X <: AnyVal]: isAnyVal.type isTrueOn X =
        isAnyVal.isTrueOn[X]
    }

    case object isInt extends PredicateOver[Any] {

      implicit lazy val itis: isInt.type isTrueOn Int =
        isInt.isTrueOn[Int]
    }

    case object isString extends PredicateOver[Any] {

      implicit lazy val itis: isString.type isTrueOn String =
        isString.isTrueOn[String]
    }

    case object trueOnLists extends DepFn1[Any,Unit] {

      implicit def buh[X]: AnyApp1At[trueOnLists.type, List[X]] { type Y = Unit } =
        trueOnLists at { x: List[X] => () }
    }

    assert { isAnyVal('x') === () }
    assert { trueOnLists(List("hola")) === () }

    assert {
      ( List(2) :: 2 :: List("hola") :: "hola" :: *[Any] filter trueOnLists.asPredicate ) === (List(2) :: List("hola") :: *[Any])
    }

    assertResult('b' :: true :: 2 :: 'a' :: *[Any]) {
      ('b' :: true :: "hola" :: 2 :: 'a' :: *[Any]).filter(isAnyVal)
    }

    assertResult('b' :: true :: "hola" :: 2 :: 'a' :: *[Any]) {
      ('b' :: true :: "hola" :: 2 :: 'a' :: *[Any]).filter(isAnyVal ∨ isString)
    }

    // this is to check that ∨ is not ambiguous:
    assertResult('b' :: true :: 2 :: 'a' :: *[Any]) {
      ('b' :: true :: "hola" :: 2 :: 'a' :: *[Any]).filter(isInt ∨ isAnyVal)
    }

    assertResult(2 :: *[Any]) {
      ('b' :: Set("a") :: true :: "hola" :: List(1,2,3) :: 2 :: 'a' :: *[Any]).filter(isAnyVal ∧ isInt)
    }

    // for completeness on predicate tests
    val z1 = trueOnLists.asPredicate(List("hola"))
    val z2 = (isAnyVal ∧ isInt)(2)
    val z3 = (isInt ∨ isAnyVal)(2)
    val z4 = (isAnyVal ∨ isInt)(true)
  }
}

```




[test/scala/cosas/DenotationTests.scala]: DenotationTests.scala.md
[test/scala/cosas/EqualityTests.scala]: EqualityTests.scala.md
[test/scala/cosas/DependentFunctionsTests.scala]: DependentFunctionsTests.scala.md
[test/scala/cosas/KListsTests.scala]: KListsTests.scala.md
[test/scala/cosas/RecordTests.scala]: RecordTests.scala.md
[test/scala/cosas/NatTests.scala]: NatTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: TypeUnionTests.scala.md
[main/scala/cosas/package.scala]: ../../../main/scala/cosas/package.scala.md
[main/scala/cosas/types/package.scala]: ../../../main/scala/cosas/types/package.scala.md
[main/scala/cosas/types/types.scala]: ../../../main/scala/cosas/types/types.scala.md
[main/scala/cosas/types/parsing.scala]: ../../../main/scala/cosas/types/parsing.scala.md
[main/scala/cosas/types/productTypes.scala]: ../../../main/scala/cosas/types/productTypes.scala.md
[main/scala/cosas/types/syntax.scala]: ../../../main/scala/cosas/types/syntax.scala.md
[main/scala/cosas/types/project.scala]: ../../../main/scala/cosas/types/project.scala.md
[main/scala/cosas/types/denotations.scala]: ../../../main/scala/cosas/types/denotations.scala.md
[main/scala/cosas/types/functionTypes.scala]: ../../../main/scala/cosas/types/functionTypes.scala.md
[main/scala/cosas/types/serialization.scala]: ../../../main/scala/cosas/types/serialization.scala.md
[main/scala/cosas/klists/replace.scala]: ../../../main/scala/cosas/klists/replace.scala.md
[main/scala/cosas/klists/cons.scala]: ../../../main/scala/cosas/klists/cons.scala.md
[main/scala/cosas/klists/klists.scala]: ../../../main/scala/cosas/klists/klists.scala.md
[main/scala/cosas/klists/take.scala]: ../../../main/scala/cosas/klists/take.scala.md
[main/scala/cosas/klists/package.scala]: ../../../main/scala/cosas/klists/package.scala.md
[main/scala/cosas/klists/takeFirst.scala]: ../../../main/scala/cosas/klists/takeFirst.scala.md
[main/scala/cosas/klists/toList.scala]: ../../../main/scala/cosas/klists/toList.scala.md
[main/scala/cosas/klists/filter.scala]: ../../../main/scala/cosas/klists/filter.scala.md
[main/scala/cosas/klists/pick.scala]: ../../../main/scala/cosas/klists/pick.scala.md
[main/scala/cosas/klists/drop.scala]: ../../../main/scala/cosas/klists/drop.scala.md
[main/scala/cosas/klists/map.scala]: ../../../main/scala/cosas/klists/map.scala.md
[main/scala/cosas/klists/at.scala]: ../../../main/scala/cosas/klists/at.scala.md
[main/scala/cosas/klists/syntax.scala]: ../../../main/scala/cosas/klists/syntax.scala.md
[main/scala/cosas/klists/fold.scala]: ../../../main/scala/cosas/klists/fold.scala.md
[main/scala/cosas/klists/noDuplicates.scala]: ../../../main/scala/cosas/klists/noDuplicates.scala.md
[main/scala/cosas/klists/slice.scala]: ../../../main/scala/cosas/klists/slice.scala.md
[main/scala/cosas/klists/find.scala]: ../../../main/scala/cosas/klists/find.scala.md
[main/scala/cosas/records/package.scala]: ../../../main/scala/cosas/records/package.scala.md
[main/scala/cosas/records/recordTypes.scala]: ../../../main/scala/cosas/records/recordTypes.scala.md
[main/scala/cosas/records/syntax.scala]: ../../../main/scala/cosas/records/syntax.scala.md
[main/scala/cosas/records/reorder.scala]: ../../../main/scala/cosas/records/reorder.scala.md
[main/scala/cosas/typeUnions/typeUnions.scala]: ../../../main/scala/cosas/typeUnions/typeUnions.scala.md
[main/scala/cosas/typeUnions/package.scala]: ../../../main/scala/cosas/typeUnions/package.scala.md
[main/scala/cosas/fns/predicates.scala]: ../../../main/scala/cosas/fns/predicates.scala.md
[main/scala/cosas/fns/instances.scala]: ../../../main/scala/cosas/fns/instances.scala.md
[main/scala/cosas/fns/package.scala]: ../../../main/scala/cosas/fns/package.scala.md
[main/scala/cosas/fns/syntax.scala]: ../../../main/scala/cosas/fns/syntax.scala.md
[main/scala/cosas/fns/functions.scala]: ../../../main/scala/cosas/fns/functions.scala.md
[main/scala/cosas/subtyping.scala]: ../../../main/scala/cosas/subtyping.scala.md
[main/scala/cosas/witness.scala]: ../../../main/scala/cosas/witness.scala.md
[main/scala/cosas/equality.scala]: ../../../main/scala/cosas/equality.scala.md
[main/scala/cosas/Nat.scala]: ../../../main/scala/cosas/Nat.scala.md
[main/scala/cosas/Bool.scala]: ../../../main/scala/cosas/Bool.scala.md