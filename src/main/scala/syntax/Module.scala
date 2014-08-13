package ohnosequences.typesets.syntax

/*
  You'd normally use objects in the end
*/
trait Module {

  trait Types {

    /*
      the underlying type
    */
    type Carrier
  }

  trait Syntax {

    type MyTypes <: Types
    val types: MyTypes
    import types._

    type C <: Carrier
    val c: C

    // here you do 
  }


}