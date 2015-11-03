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

    def hola[L <: NonEmptyKList.Of[Bar]](l: L): String = l.head.x
    def id[X <: NonEmptyKList.Of[Argh]](x: X): NonEmptyKList.Of[Bar] = x
    def idHola[X <: NonEmptyKList.Of[Argh]](x: X): String = hola(id(x))
    def as[B0 <: B, B, L <: KList.Of[B0]](l: L): KList.Of[B] = l

  }

  test("KLists are covariant in bound and values") {

    val oh = A0 :: A1 :: A0 :: *[A]
    val oh_any: KList.Of[Any] = oh

    val uh: KList.Of[A0.type] = A0 :: A0 :: A0 :: *[A0.type]
    val uh_A: KList.Of[A] = uh

    assert{ oh.head.boo === true }
    assert{ oh.tail.head.boo === false }

    def foo[L <: NonEmptyKList.Of[A]](l: L): Boolean = l.head.boo

    assert{ foo(oh) === true }
  }


  test("can convert KLists to lists of their bound") {

    assert {
      (A0 :: A1 :: A0 :: *[A]).toList === (A0 :: A1 :: A0 :: Nil)
    }

    assert {
      ("hola" :: "scalac" :: *[String]).toList === ("hola" :: "scalac" :: Nil)
    }
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

    def stupidSpan[N <: AnyNat, L <: AnyKList](l: L, n: N) = l.span(n, _0)

    assert { stupidSpan(sbi, _4) === *[Any] }
  }

  test("can pick elements by type") {

    assert { (true :: "que tal" :: *[Any]).find[Boolean] === true }
    assert { (true :: "que tal" :: *[Any]).find[String] === "que tal" }
    assert { (true :: "que tal" :: "scalac" :: *[Any]).find[String] === "que tal" }

    assert { (true :: "que tal" :: "scalac" :: *[Any] at _2) === "scalac" }

    assert { (true :: "que tal" :: "scalac" :: *[Any] pick /[String]) ===
      ( "que tal", true :: "scalac" :: *[Any]) }
  }

  test("can map over KLists") {

    case object f extends DepFn1[Any,String] {

      implicit def default[X <: Any]: App1[f.type,X,String] = f at { a: X => a.toString }
    }

    val z: *[Any] = *[Any]
    val zz: Boolean :: *[Any] = true :: *[Any]
    val zzz: Int :: Boolean :: *[Any] = 2 :: true :: *[Any]

    assert {

      KList(identity)(zzz) === zzz
    }
    assert {
      KList(f)(zzz) === "2" :: "true" :: *[String]
    }

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

    val flEmpty = new FoldLeft[*[Int], sum.type, Int]
    val flAgain = new FoldLeft[Int :: *[Int], sum.type, Int]
    assert {
      flEmpty(*[Int],0,sum) === 0
    }

    assert {

      flAgain(2 :: *[Int],0,sum) === 2
    }

    assert {

      (3 :: 2 :: 1 :: *[Int]).foldLeft(sum)(0) === 6
    }
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