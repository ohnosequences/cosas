package ohnosequences.cosas.tests

import ohnosequences.cosas._, typeUnions._


class TypeUnionTests extends org.scalatest.FunSuite {


  test("check arities") {

    type SBS = either[String]#or[Boolean]#or[String]

    type SBS2 = SBS

    type Three = arity[SBS]
    type Three2 = arity[SBS2]


    import shapeless.Nat._
    
    implicitly[ Three =:= _3 ]
    implicitly[ Three2 =:= _3 ]
    implicitly[ SBS =:= SBS2 ]
  }

  test("check bounds") {

    type S = either[String]
    type SB = either[String]#or[Boolean]
    type SB2 = either[String] or Boolean
    type SBI = either[String] or Boolean or Int
    trait Bar
    type BarBIS = either[String] or Int or Boolean or Bar
    type Uh = Byte :∨: Int :∨: Boolean :∨: String :∨: either[Nothing]

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
    implicitly[just[Byte] <:< Uh#union]
    implicitly[just[String] <:< SBI#union]

    implicitly[just[Bar] <:< BarBIS#union]

    // intersections
    type J = AnyRef :∨: String :∨: empty
    implicitly[String <:< J#intersection]
    implicitly[Int <:!< J#intersection]

  }

  test("bounds with subtyping") {

    // weird issues
    trait Animal
    val animal = new Animal {}
    trait Cat extends Animal
    trait UglyCat extends Cat
    object pipo extends UglyCat
    val uglyCat = new UglyCat {}
    trait Dog extends Animal

    // everyone fits here
    type DCA = Dog :∨: Cat :∨: Animal :∨: empty
    implicitly[Dog isOneOf DCA]
    implicitly[Cat isOneOf DCA]
    implicitly[UglyCat isOneOf DCA]
    implicitly[pipo.type isOneOf DCA]
    implicitly[uglyCat.type isOneOf DCA]

    type DC = Dog :∨: Cat :∨: empty
    implicitly[Dog isOneOf DC]
    implicitly[Cat isOneOf DC]
    implicitly[UglyCat isOneOf DC]
    implicitly[pipo.type isOneOf DC]
    implicitly[uglyCat.type isOneOf DC]
    // not here
    implicitly[animal.type isNotOneOf DC]

    type DUC = Dog :∨: UglyCat :∨: empty
    implicitly[Dog isOneOf DUC]
    implicitly[UglyCat isOneOf DUC]
    implicitly[pipo.type isOneOf DUC]
    implicitly[uglyCat.type isOneOf DUC]
    // not here
    implicitly[Cat isNotOneOf DUC]
    implicitly[animal.type isNotOneOf DUC]

    // this does not work
    // type ISDUC = String :∨: Int :∨: DUC
    type ISDUC = String :∨: Int :∨: Dog :∨: UglyCat :∨: empty
    type DUCIS = Dog :∨: UglyCat :∨: String :∨: Int :∨: empty
    implicitly[Dog isOneOf ISDUC]
    implicitly[UglyCat isOneOf ISDUC]
    implicitly[pipo.type isOneOf ISDUC]
    implicitly[uglyCat.type isOneOf ISDUC]
    

    // not here; should not work!!
    implicitly[Cat isOneOf ISDUC]
    implicitly[animal.type isOneOf ISDUC]
    implicitly[Cat isOneOf DUCIS]
    implicitly[animal.type isOneOf DUCIS]
    implicitly[Byte isOneOf ISDUC]



  }
}
