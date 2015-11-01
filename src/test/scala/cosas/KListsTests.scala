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

    val s = "12312" :: KNil[Any]

    val sh = s.head
    val st = s.tail

    assert ( sh === "12312" )
    assert ( st === KNil[Any] )


    val sib = "12312" :: 121312 :: true :: KNil[Any]

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

    val oh = A0 :: A1 :: A0 :: KNil[A]
    val oh_any: KList.Of[Any] = oh

    val uh: KList.Of[A0.type] = A0 :: A0 :: A0 :: KNil[A0.type]
    val uh_A: KList.Of[A] = uh

    assert{ oh.head.boo === true }
    assert{ oh.tail.head.boo === false }

    def foo[L <: NonEmptyKList.Of[A]](l: L): Boolean = l.head.boo

    assert{ foo(oh) === true }
  }


  test("can convert KLists to lists of their bound") {

    assert {
      (A0 :: A1 :: A0 :: KNil[A]).toList === (A0 :: A1 :: A0 :: Nil)
    }

    assert {
      ("hola" :: "scalac" :: KNil[String]).toList === ("hola" :: "scalac" :: Nil)
    }
  }

  test("can convert KLists to lists of a supertype of their bound") {

    assert {
      KNil[A].toListOf[A] === Nil
    }

    assert {
      (A0 :: KNil[A0.type]).toListOf[A] === List[A](A0)
    }

    assert {
      ("hola" :: "scalac" :: KNil[String]).toListOf[Any] === List[Any]("hola","scalac")
    }

  }

  test("can concatenate KLists") {

    val concat = new Concatenate[Boolean :: String :: KNil[Any]]

    assert {

      concat(true :: "que tal" :: KNil[Any], "hola" :: 2 :: KNil[Any]) ===
        (true :: "que tal" :: "hola" :: 2 :: KNil[Any])
    }

    assert {
      (true :: "que tal" :: KNil[Any]) ++ ("hola" :: 2 :: KNil[Any]) ===
        true :: "que tal" :: "hola" :: 2 :: KNil[Any]
    }

    val hola: Boolean :: String :: String :: KNil[Any] =
      (true :: "que tal" :: KNil[Any]) ++ ("hola" :: KNil[Any])
  }

  test("can access elements by index") {

    val sbi = "hola" :: true :: 2 :: KNil[Any]
    assert {
      ("hola" :: "scalac" :: "que tal" :: KNil[String] at _0 ) === "hola"
    }

    assert {
      sbi === (sbi at _0) :: (sbi at _1) :: (sbi at _2) :: KNil[Any]
    }
  }

  test("can take and drop from a KList") {

    type SBI = String :: Boolean :: Int :: KNil[Any]
    val sbi: SBI = "hola" :: true :: 2 :: KNil[Any]

    assert { (sbi take _1) === ("hola" :: KNil[Any]) }
    assert { (sbi take _2) === ("hola" :: true :: KNil[Any]) }
    assert { (sbi take _3) === sbi }

    assert { (sbi drop _0) === sbi }
    assert { (sbi drop _1) ===  true :: 2 :: KNil[Any] }
    assert { (sbi drop _2) ===  2 :: KNil[Any] }
    assert { (sbi drop _3) ===  KNil[Any] }
  }

  test("can take segments from a KList") {

    val z = "a" :: 'b' :: true :: 2 :: "cd" :: false :: KNil[Any]

    assert { (z drop _2 take _3) ===  true :: 2 :: "cd" :: KNil[Any] }
  }

  test("can pick elements by type") {

    assert { (true :: "que tal" :: KNil[Any]).find[Boolean] === true }
    assert { (true :: "que tal" :: KNil[Any]).find[String] === "que tal" }
    assert { (true :: "que tal" :: "scalac" :: KNil[Any]).find[String] === "que tal" }

    assert { (true :: "que tal" :: "scalac" :: KNil[Any] at _2) === "scalac" }

    assert { (true :: "que tal" :: "scalac" :: KNil[Any] pick /[String]) ===
      ( "que tal", true :: "scalac" :: KNil[Any]) }
  }

  test("can map over KLists") {

    case object f extends DepFn1[Any,String] {

      implicit def default[X <: Any]: App1[f.type,X,String] = f at { a: X => a.toString }
    }

    val z: KNil[Any] = KNil[Any]
    val zz: Boolean :: KNil[Any] = true :: KNil[Any]
    val zzz: Int :: Boolean :: KNil[Any] = 2 :: true :: KNil[Any]

    assert {

      KList(identity)(zzz) === zzz
    }
    assert {
      KList(f)(zzz) === "2" :: "true" :: KNil[String]
    }

    assert {

      KList(f)("hola" :: "scalac" :: KNil[Any]) === "hola" :: "scalac" :: KNil[String]
    }

    assert {

      KList(as[String,Any])("hola" :: "scalac" :: KNil[String]) === "hola" :: "scalac" :: KNil[Any]
    }
  }

  case object sum extends DepFn2[Int,Int,Int] {

    implicit val default: App2[sum.type, Int,Int,Int] = App2 { (a: Int, b: Int) => (a + b): Int }
  }

  test("can foldLeft over KLists") {

    val flEmpty = new FoldLeft[KNil[Int], sum.type, Int]
    val flAgain = new FoldLeft[Int :: KNil[Int], sum.type, Int]
    assert {
      flEmpty(KNil[Int],0,sum) === 0
    }

    assert {

      flAgain(2 :: KNil[Int],0,sum) === 2
    }

    assert {

      (3 :: 2 :: 1 :: KNil[Int]).foldLeft(sum)(0) === 6
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
      filter[Any](isPrimitive, 'b' :: true :: "hola" :: 2 :: 'b' :: KNil[Any]) === (true :: 2 :: KNil[Any])
    }
  }
}
