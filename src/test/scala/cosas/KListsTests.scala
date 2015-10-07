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
    // assert ( sib.TAIL.TAIL.TAIL == KNil[Any] )

  }

  test("KLists are covariant in bound and values") {

    val oh = A0 :: A1 :: A0 :: KNil[A]
    val oh_any: KList[Any] = oh

    val uh: KList[A0.type] = A0 :: A0 :: A0 :: KNil[A0.type]
    val uh_A: KList[A] = uh

    assert{ oh.head.boo === true }
    assert{ oh.tail.head.boo === false }

    def foo[L <: AnyNEKList { type Bound = A }](l: L): Boolean = l.head.boo

    assert{ foo(oh) === true }
  }

  test("can map over KLists") {

    // case object toStr extends DepFn1[Any,String] {
    //
    //   implicit def default[X]: App1[toStr.type,X,String] = toStr at { a: X => a.toString }
    // }
    //
    // val zz = true :: KNil[Any]
    // val uh = MapKList(toStr,zz)(MapKList.cons(toStr.default, MapKList.empty))

  }

}
