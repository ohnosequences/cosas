package ohnosequences.cosas.tests

import ohnosequences.cosas._, products._, fns._

case object HListTestsContext {

  trait A { val boo: Boolean }

  case object A0 extends A { val boo = true }
  case object A1 extends A { val boo = false }

}

class HListTests extends org.scalatest.FunSuite {

  import HListTestsContext._

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

  test("KLists are covariant in bound and values") {

    val oh = A0 :: A1 :: A0 :: KNil[A]
    val oh_any: KList[Any] = oh

    val uh: KList[A0.type] = A0 :: A0 :: A0 :: KNil[A0.type]
    val uh_A: KList[A] = uh

    assert{ oh.head.boo === true }
    assert{ oh.tail.head.boo === false }

    def foo[L <: NEKList[A]](l: L): Boolean = l.head.boo

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

    // val foldLeft = new FoldLeft[String :: String :: KNil[Any], identity.type]
    // val z = foldLeft("hola" :: "scalac" :: KNil[Any], "", identity)
  }

}
