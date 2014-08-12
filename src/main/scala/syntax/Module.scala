package ohnosequences.typesets.syntax

/*
  You'd normally use objects in the end
*/
trait Module {

  trait Syntax {

    /*
      the underlying type
    */
    type Carrier
  }

  trait Ops {

    type MySyntax <: Syntax
    val syntax: MySyntax
    import syntax._

    type C <: Carrier
    val c: C
  }


}