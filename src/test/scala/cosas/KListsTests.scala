package ohnosequences.cosas.tests

import ohnosequences.cosas._, klists._, fns._

case object KListTestsContext {

  trait A { val boo: Boolean }

  case object A0 extends A { val boo = true }
  case object A1 extends A { val boo = false }

}

class KListTests extends org.scalatest.FunSuite {

  import KListTestsContext._

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

  test("can concatenate KLists") {

    val concat = new Concatenate[Boolean :: String :: *[Any]]

    assert {

      concat(true :: "que tal" :: *[Any], "hola" :: 2 :: *[Any]) ===
        (true :: "que tal" :: "hola" :: 2 :: *[Any])
    }

    assert {
      (true :: "que tal" :: *[Any]) ++ ("hola" :: 2 :: *[Any]) ===
        true :: "que tal" :: "hola" :: 2 :: *[Any]
    }

    val hola: Boolean :: String :: String :: *[Any] =
      (true :: "que tal" :: *[Any]) ++ ("hola" :: *[Any])
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
    assert { (z drop _2 take _3) ===  z.span(_2, _3) }

    // empty segments will be optimized
    assert { sbi.span(_2, _2) === *[Any] }
    assert { sbi.span(_0, _0) === *[Any] }

    def stupidSpan[N <: AnyNat, L <: AnyKList](l: L, n: N): KNil[L#Bound] = l.span(n, _0)

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

    // assert { ab1b2b.takeFirstS(/[B :: *[A]]) === B1 :: *[A] }
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

      implicit def default[X <: Any]: AnyApp1At[f.type,X] { type Y = String } = f at { a: X => a.toString }
    }

    val z: *[Any] = *[Any]
    val zz: Boolean :: *[Any] = true :: *[Any]
    val zzz: Int :: Boolean :: *[Any] = 2 :: true :: *[Any]

    assert {
      (zzz map identity) === zzz
    }
    assert {
      KList(f)(zzz) === "2" :: "true" :: *[String]
    }
    //
    assert {

      KList(f)("hola" :: "scalac" :: *[Any]) === "hola" :: "scalac" :: *[String]
    }

    assert {

      KList(as[String,Any])("hola" :: "scalac" :: *[String]) === "hola" :: "scalac" :: *[Any]
    }
  }

  case object sum extends DepFn2[Int,Int,Int] {

    implicit val default: App2[sum.type, Int,Int,Int] = App2 { (a: Int, b: Int) => (a + b): Int }
  }

  test("can foldLeft over KLists") {

    val flEmpty = new FoldLeft[*[Int], Int, sum.type]
    val flAgain = new FoldLeft[Int :: *[Int], Int, sum.type]

    assert {
      flEmpty(*[Int], 0, sum) === 0
    }

    // assertResult(2) {
    //   flAgain(2 :: *[Int],0,sum)
    // }

    assertResult(6) {
      (3 :: 2 :: 1 :: *[Int]).foldLeft(sum)(0)(FoldLeft.cons[
        Int, Int :: Int :: *[Int],
        Int,
        sum.type,
        Int, Int
      ])
    }

    val z = (1 :: *[Int]).foldLeft(snoc)(*[Int])(FoldLeft.cons[
      Int, *[Int],
      *[Int],
      snoc.type,
      Int :: *[Int], Int :: *[Int]
    ])

    // assertResult(1 :: 2 :: 3 :: 4 :: 5 :: 6 :: *[Int]) {
    //   (1 :: 2 :: 3 :: *[Int]).foldLeft(cons)(4 :: 5 :: 6 :: *[Int])
    // }

    // assertResult(1 :: "hola" :: 'b' :: true :: *[Any]) {
    //   (1 :: "hola" :: 'b' :: true :: *[Any]).foldLeft(cons)(*[Any])
    // }
  }

  test("can foldRight over KLists") {

    val flEmpty = new FoldRight[*[Int], Int, sum.type]
    val flAgain = new FoldRight[Int :: *[Int], Int, sum.type]
    assert {
      flEmpty(*[Int],0,sum) === 0
    }

    assertResult(2) {
      flAgain(2 :: *[Int],0,sum)
    }

    assertResult(6) {
      (3 :: 2 :: 1 :: *[Int]).foldRight(sum)(0)
    }

    val z = (1 :: *[Int]).foldRight(snoc)(*[Int])(FoldRight.cons[
      snoc.type,
      *[Int],
      Int, *[Int],
      *[Int], Int :: *[Int]
      ])

      assert { (1 :: 2 :: 3 :: *[Int]).foldRight(snoc)(4 :: 5 :: 6 :: *[Int]) === (1 :: 2 :: 3 :: 4 :: 5 :: 6 :: *[Int]) }
      assert { (1 :: "hola" :: 'b' :: true :: *[Any]).foldRight(snoc)(*[Any]) === (1 :: "hola" :: 'b' :: true :: *[Any]) }

      val f: (String, Any) => String = { (str,a) => a.toString ++ str }
      val f_flip: (Any,String) => String = { (a,str) => f(str,a) }

      // println { (1 :: "hola" :: 'b' :: true :: *[Any]).foldRight(Fn2(f))("") }
      println { (1 :: "hola" :: 'b' :: true :: *[Any]).asList.foldRight("")(f_flip) }
  }

  test("can filter KLists") {

    case object isPrimitive extends DepFn1[Any, AnyBool] {

      implicit val intPrimitive: AnyApp1At[isPrimitive.type, Int] { type Y = True } =
        this at { x: Int => True }
      implicit val booleanPrimitive: App1[isPrimitive.type, Boolean, True] =
        this at { x: Boolean => True }
    }

    assert {
      filter[Any](isPrimitive, 'b' :: true :: "hola" :: 2 :: 'b' :: *[Any]) === (true :: 2 :: *[Any])
    }
  }
}
