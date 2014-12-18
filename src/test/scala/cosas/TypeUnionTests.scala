package ohnosequences.cosas.tests

class TypeUnionTests extends org.scalatest.FunSuite {


  import ohnosequences.cosas._, AnyTypeUnion._

  test("check arities") {

    type SBS = either[String]#or[Boolean]#or[String]

    type SBS2 = SBS

    type Three = arity[SBS]
    type Three2 = arity[SBS2]


    import shapeless._, Nat._
    
    implicitly[ Three =:= _3 ]
    implicitly[ Three2 =:= _3 ]
    implicitly[ SBS =:= SBS2 ]
  }

  test("check bounds") {

    import TypeUnion._

    type S = either[String]
    type SB = either[String]#or[Boolean]
    type SB2 = either[String] or Boolean
    type SBI = either[String] or Boolean or Int
    trait Bar
    type BarBIS = either[String] or Int or Boolean or Bar
    type Uh = Int :∨: Boolean :∨: String :∨: empty

    implicitly[just[String] <:< Uh#union]
    implicitly[just[Boolean] <:< Uh#union]
    implicitly[just[Int] <:< Uh#union]

    implicitly[S#union =:= just[String]]

    implicitly[just[String] <:< S#union]
    implicitly[just[Boolean] <:< SB#union]
    implicitly[just[String] <:< SB#union]
    implicitly[just[Boolean] <:< SB2#union]
    implicitly[just[String] <:< SB2#union]

    implicitly[just[String] <:< SBI#union]
    implicitly[just[Boolean] <:< SBI#union]
    implicitly[just[Int] <:< SBI#union]

    import shapeless.{ <:!< }
    implicitly[just[Byte] <:!< SBI#union]
    implicitly[just[Byte] <:!< Uh#union]
    implicitly[just[String] <:< SBI#union]

    implicitly[just[Bar] <:< BarBIS#union]
  }
}