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

  test("can map over KLists") {

    case object f extends DepFn1[Any,String] {

      implicit def default[X <: Any]: App1[f.type,X,String] = f at { a: X => a.toString }
    }

    val z: KNil[Any] = KNil[Any]
    val zz: Boolean :: KNil[Any] = true :: KNil[Any]
    val zzz: Int :: Boolean :: KNil[Any] = 2 :: true :: KNil[Any]

    val map: mapKList[identity.type] = mapKList[identity.type]

    val m0 = mapKList[identity.type](identity, KNil[Any])
    val m1 = map.apply(identity, zz)
    val m2 = map(identity, zzz)

    assert {
      mapKList[f.type](f,zzz) === "2" :: "true" :: KNil[String]
    }

    // TODO shouldn't need to add the type
    // val bbb = mapKList(f,zzz)
  }

}
