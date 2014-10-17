package ohnosequences.cosas.tests

class TypeUnionTests extends org.scalatest.FunSuite {


  import ohnosequences.cosas._, AnyTypeUnion._

  test("check arities") {

    type SBS = either[String]#or[Boolean]#or[String]

    type Three = arity[SBS]

    import shapeless._, Nat._
    
    implicitly[ Three =:= _3 ]
  }
}