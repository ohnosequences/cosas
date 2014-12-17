package ohnosequences.cosas.tests

import shapeless.test.illTyped
import ohnosequences.cosas._, AnyWrap._, ValueOf._, AnySubsetType._

object WrapTestsContext {

  case object Color extends Wrap[String] { val label = "Color" }

  final class WrappedList[E] extends Wrap[List[E]] {

    lazy val label = "WrappedList"
  }
  class NEList[E] extends SubsetType[WrappedList[E]] {

    lazy val label = "NEList"
    def predicate(l: List[E]): Boolean = ! l.isEmpty

    def apply(e: E): ValueOf[NEList[E]] = new ValueOf[NEList[E]](e :: Nil)
  }

  object NEList {

    implicit def toOps[E](v: ValueOf[NEList[E]]): NEListOps[E] = new NEListOps(v.value)
  }

  def NEListOf[E]: NEList[E] = new NEList()

  class NEListOps[E](val l: List[E]) extends AnyVal with ValueOfSubsetTypeOps[WrappedList[E], NEList[E]] {

    def ::(x: E): ValueOf[NEList[E]] = unsafeValueOf[NEList[E]](x :: l)
  }
}

class WrapTests extends org.scalatest.FunSuite {
  import WrapTestsContext._

  test("creating values") {

    val azul = Color("blue")
    val verde = valueOf[Color.type]("green")
    val amarillo = Color withValue "yellow"

    assert{ azul.value == "blue" }
    assert{ verde.value == "green" }
    assert{ amarillo.value == "yellow" }
  }
}

class DenotationTests extends org.scalatest.FunSuite {

  object UserType extends Type("User")
  type User = UserType.type
  val User: User = UserType

  object Friend extends Type("Friend")
  type Friend = Friend.type

  case class user(id: String, name: String, age: Int)

  test("create denotations") {

    import Denotes._

    /* the right-associative syntax */
    val uh: user :%: User = user(id = "adqwr32141", name = "Salustiano", age = 143) :%: User
    val z = User denoteWith 2423423
  }

  test("type-safe equals") {

    // TODO: right imports here
    import org.scalactic.TypeCheckedTripleEquals._
    import scalaz._, Scalaz._


    val paco = "Paco"
    val u1 = paco :%: User
    val u1Again = paco :%: User
    val u2 = paco :%: Friend

    // TODO: needs integration with ScalaTest stuff.
    // Things are safe if you import the above (I don't know why)
    // illTyped {"""

    //   u1 === u2
    // """}

    assert {

      u1 === u1Again
    }

    assert {

      u1 === u1
    }


  }

  test("naive nonempty lists") {

    import WrapTestsContext._

    // val buh: Option[ValueOf[NEList[String]]] = NEListOf[String]("there's something!" :: Nil)

    // val oh = NEListOf[Int](12 :: 232 :: Nil)

    val nelint = NEListOf(232)

    val u1 = 23 :: nelint
  }
}
