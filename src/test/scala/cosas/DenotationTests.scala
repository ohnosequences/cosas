package ohnosequences.cosas.tests

import ohnosequences.cosas._, types._, AnySubsetType._
import ohnosequences.cosas.tests.asserts._

object DenotationTestsContext {

  case object Color extends Wrap[String]("Color")
  object User extends Type("User")
  type User = User.type
  object Friend extends Type("Friend")
  case class userInfo(id: String, name: String, age: Int)
}

class DenotationTests extends org.scalatest.FunSuite {

  import DenotationTestsContext._

  test("creating values") {

    val azul = Color := "blue"
    val verde = valueOf(Color)("green")
    val amarillo = Color := "yellow"

    val x1 = "yellow" =: Color

    assert(azul.value == "blue")
    assert(verde.value == "green")
    assert(amarillo.value == "yellow")
    assertTaggedEq(amarillo, x1)
  }

  test("create denotations") {

    val z = User := 2423423

    /* the right-associative syntax */
    val uh: userInfo =: User = userInfo(id = "adqwr32141", name = "Salustiano", age = 143) =: User
    /* or with equals-sign style */
    val oh = userInfo(id = "adqwr32141", name = "Salustiano", age = 143) =: User
    /* or in the other order */
    val ah = User := userInfo(id = "adqwr32141", name = "Salustiano", age = 143)
  }

  test("type-safe equals") {

    val paco = "Paco"
    val jose = "Jose"

    val u1 = paco =: User
    val u1Again = paco =: User

    val u2 = paco =: Friend
    val v = jose =: Friend

    assertTaggedEq(u1, u1)
    assertTaggedEq(u1, u1Again)
    // assertTaggedEq { u2, v } // not there in ScalaTest :-/
    // assertTaggedEq { u1, u2 }
    assertTypeError("u1 =~= u2")
    assert{ !(v =~= u2) }
  }

  test("Denotation show") {

    assert{ (User := "hola").show == "(User := hola)" }

    val azul = Color := "blue"

    assert{ azul.show == "(Color := blue)" }
  }
}
