package ohnosequences.cosas.tests

import shapeless.test.illTyped
import ohnosequences.cosas._, AnyType._, AnySubsetType._

object WrapTestsContext {

  case object Color extends Wrap[String]("Color")
  object User extends Type("User")
  object Friend extends Type("Friend")
  case class userInfo(id: String, name: String, age: Int)

  /* The NEList stuff */
  final class WrappedList[E] extends Wrap[List[E]]("WrappedList")

  class NEList[E] extends SubsetType[WrappedList[E]] {

    lazy val label = "NEList"
    def predicate(l: List[E]): Boolean = ! l.isEmpty

    def apply(e: E): ValueOf[NEList[E]] = new ValueOf[NEList[E]](e :: Nil)
  }

  object NEList {

    implicit def toOps[E](v: ValueOf[NEList[E]]): NEListOps[E] = new NEListOps(v.value)
    implicit def toSSTops[E](v: NEList[E]): SubSetTypeOps[WrappedList[E], NEList[E]] = new SubSetTypeOps(v)
  }

  def NEListOf[E]: NEList[E] = new NEList()

  class NEListOps[E](val l: List[E]) extends AnyVal with ValueOfSubsetTypeOps[WrappedList[E], NEList[E]] {

    def ::(x: E): ValueOf[NEList[E]] = unsafeValueOf[NEList[E]](x :: l)
  }
}

class WrapTests extends org.scalatest.FunSuite {

  import WrapTestsContext._

  test("creating values") {

    val azul = Color denoteWith "blue"
    val verde = new ValueOf[Color.type]("green")
    val amarillo = Color denoteWith "yellow"

    assert{ azul.value == "blue" }
    assert{ verde.value == "green" }
    assert{ amarillo.value == "yellow" }
  }
}

class DenotationTests extends org.scalatest.FunSuite with ScalazEquality {
  import WrapTestsContext._

  test("create denotations") {
    import Denotes._

    /* the right-associative syntax */
    val uh: userInfo :%: User.type = userInfo(id = "adqwr32141", name = "Salustiano", age = 143) :%: User
    val z = User denoteWith 2423423
  }

  test("type-safe equals") {

    val paco = "Paco"
    val jose = "Jose"

    val u1 = paco :%: User
    val u1Again = paco :%: User

    val u2 = paco :%: Friend
    val v = jose :%: Friend

    assert { u1 === u1 }
    assert { u1 === u1Again }
    // assert { u2 =/= v } // not there in ScalaTest :-/
    // assert { u1 === u2 }
    assertTypeError("u1 === u2")
    assert{ !( u2 === v ) }
  }

  test("naive nonempty lists") {

    import WrapTestsContext._

    import AnySubsetType._
    // this is Some(...) but we don't know at runtime. What about a macro for this? For literals of course
    val oh = NEListOf[Int](12 :: 232 :: Nil)

    val nelint = NEListOf(232)

    val u1 = 23 :: nelint
  }
}
