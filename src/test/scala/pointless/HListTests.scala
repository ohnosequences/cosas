package ohnosequences.pointless.test

import ohnosequences.pointless._, AnyHList._

object HListTestsContext {

  trait A {

    def oh: Boolean
  }

  class A0 extends A { 

    def oh = true
  }
  class A1 extends A {

    def oh = false
  }

}

class HListTests extends org.scalatest.FunSuite {

  import HListTestsContext._
  import AnyHList._

  test("build HLists") {

    val sib = "12312" :@: 121312 :@: true :@: HNil[Any]

    val s = "12312" :@: HNil[Any]

    val sh = s.HEAD
    val st = s.TAIL

    assert ( sh === "12312" )
    assert ( st === HNil[Any] )

    // sib

    val sibh = sib.HEAD
    val sibt = sib.TAIL
    // val sibth = sibt.HEAD
    
    // val nested = 12321 :@@: true :@@: HNil[Any]

  }

  test("KList stuff") {

    val oh = new A0 :@: new A1 :@: new A0 :@: HNil[A]

    // val ohth = oh.TAIL.HEAD

    // val ohno: AnyHList.Of[A] = new A0 :: 232 :: new A0 :: HNil

  }
}