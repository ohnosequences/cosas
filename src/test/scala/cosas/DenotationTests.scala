package ohnosequences.cosas.tests

import ohnosequences.cosas._, types._, fns._, klists._
import ohnosequences.cosas.tests.asserts._
import DenotationTestsContext._

case object DenotationTestsContext {

  case object Color extends AnyType { val label = "Color"; type Raw = String }
  type Color = Color.type
  object User extends AnyType {  val label = "User"; type Raw = Any  }
  type User = User.type
  object Friend extends AnyType { val label = "Friend"; type Raw = Any }
  type Friend = Friend.type
  case class userInfo(id: String, name: String, age: Int)

  val FavoriteColor = (User ==> Color)

  val colorAndFriend = Color :×: Friend :×: unit
}

class DenotationTests extends org.scalatest.FunSuite {

  test("type := value") {

    val azul      = Color := "blue"
    val verde     = Color := "green"
    val amarillo  = Color := "yellow"

    val x1 = Color := "yellow"

    assert(azul.value === "blue")
    assert(verde.value === "green")
    assert(amarillo.value === "yellow")
    assertTaggedEq(amarillo, x1)

    val z = User := 2423423
    val uh: User := userInfo = User := userInfo(id = "adqwr32141", name = "Salustiano", age = 143)
    val oh = User := userInfo(id = "adqwr32141", name = "Salustiano", age = 143)
    val ah = User := userInfo(id = "adqwr32141", name = "Salustiano", age = 143)

    assert { uh === oh }
    assert { oh === ah }
  }

  test(" denotation =~= denotation") {

    val paco = "Paco"
    val jose = "Jose"

    val u1 = User := paco
    val u1Again = User := paco

    val u2 = Friend := paco
    val v = Friend := jose

    assertTaggedEq(u1, u1)
    assertTaggedEq(u1, u1Again)
    assert { u1 =~= u1Again }
    assertTypeError("u1 =~= u2")
    assert { !(v =~= u2) }
  }

  test("denotation show") {

    assert{ (User := "hola").show === "(User := hola)" }

    val azul = Color := "blue"

    assert{ azul.show === "(Color := blue)" }
  }

  test("denotationValue") {

    assert { "blue" === denotationValue(User := "blue") }
  }

  test("typeOf") {

    assert { typeOf(Color := "blue") === Color }
    assert { typeOf(User := "LALALA") === typeOf(User := 23) }
  }

  test("typeLabel") {

    val azul = Color := "blue"

    assert { s"(${typeLabel(Color)} := ${azul.value})" === azul.show }

    assert { typeLabel(User ==> Color) === s"${User.label} ==> ${Color.label}" }
  }

  test("Denotation covariant on Value") {

    trait Foo
    class Buh extends Foo
    class Bar extends Buh

    val z: User := Bar = User := new Bar
    val x: User := Foo = z

    case object A extends AnyType { lazy val label = toString; type Raw = Foo }

    val aFoo = A := new Foo {}
    val aBar: A.type := A.Raw = A := new Bar
  }

  test("Denotation with bound Any does not require bounds") {

    trait Boundless extends AnyType { type Raw = Any }

    def buh[Val, B <: Boundless](v: Val): B := Val = new (B := Val)(v)
  }

  test("T ==> S") {

    val f = { x: Any => "blue" }

    val alwaysBlue = FavoriteColor := Fn1(f)
    val alwaysBlueAgain = (User ==> Color) := Fn1(f)

    assertTypeError("""FavoriteColor := Fn1 { x: Int => "hola" }""")
  }

  test("T :×: S") {

    val zz = colorAndFriend := (
      (Color := "blue") ::
      (Friend := true)  ::
      *[AnyDenotation]
    )

    val friend = (zz getFirst Friend)

    assert { (zz getFirst Friend) === (zz project Friend)  }
    // NOTE 0-based indexes
    assert { (zz getFirst Friend) === (zz at _1) }
  }
}
