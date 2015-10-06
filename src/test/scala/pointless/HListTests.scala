package ohnosequences.pointless.test

import ohnosequences.pointless._, AnyKList._

object HListTestsContext {

  trait A { val boo: Boolean }

  case object A0 extends A { val boo = true }
  case object A1 extends A { val boo = false }

}

class HListTests extends org.scalatest.FunSuite {

  import HListTestsContext._

  test("build HLists") {

    val s = "12312" :@: KNil[Any]

    val sh = s.HEAD
    val st = s.TAIL

    assert ( sh == "12312" )
    assert ( st == KNil[Any] )


    val sib = "12312" :@: 121312 :@: true :@: KNil[Any]

    val sibh = sib.HEAD
    val sibt = sib.TAIL

    assert ( sib.HEAD == "12312" )
    // assert ( sib.TAIL.TAIL.TAIL == KNil[Any] )

  }

  test("KList stuff") {

    val oh = A0 :@: A1 :@: A0 :@: KNil[A]
    val oh_any: KList[Any] = oh

    val uh: KList[A0.type] = A0 :@: A0 :@: A0 :@: KNil[A0.type]
    val uh_A: KList[A] = uh

    assert{ oh.HEAD.boo == true }
    assert{ oh.tail.head.boo == false }

    def foo[L <: NEKList[A]](l: L): Boolean = l.head.boo

    assert{ foo(oh) == true }
  }

}
