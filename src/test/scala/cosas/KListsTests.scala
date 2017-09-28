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

    val z = cons.foldRight(*[Int])(1 :: *[Int])(
      FoldRight.cons[
        cons.type,
        *[Int],
        Int, *[Int],
        *[Int], Int :: *[Int]
      ](
        FoldRight.empty,
        cons.default
      )
    )

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

  test("KList any") {

    assert { any(isString)("hola" :: *[Any]) === (()) }
    assert { any(isString)(2 :: true :: "hola" :: *[Any]) === (()) }
    assert { any(isString)(*[Any]) === (()) }

    assertTypeError { """ any(isString)(true :: 2 :: *[Any]) """ }
  }

  test("KList all") {

    assert { all(isString)("hola" :: "scalac" :: *[Any]) === (()) }
    assert { all(isString)(*[Any]) === (()) }

    assertTypeError { """ all(isString)("hola" :: 2 :: *[Any]) """ }
  }

  test("can filter KLists") {

    assert { isAnyVal('x') === (()) }
    assert { trueOnLists(List("hola")) === (()) }

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
