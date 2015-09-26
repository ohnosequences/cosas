package ohnosequences.cosas.tests

import ohnosequences.cosas._, typeUnions._


class TypeUnionTests extends org.scalatest.FunSuite {

  test("check bounds") {

    type S      = either[String]
    type SB     = either[String] or Boolean
    type SB2    = either[String] or Boolean
    type SBI    = either[String] or Boolean or Int
    trait Bar
    type BarBIS = either[String] or Int or Boolean or Bar
    type Uh     = either[Byte] or Int or Boolean or String

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

    implicitly[just[Byte] <:!< SBI#union]
    implicitly[just[Byte] <:< Uh#union]
    implicitly[just[String] <:< SBI#union]

    implicitly[just[Bar] <:< BarBIS#union]
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
    type DCA = either[Dog] or Cat or Animal
    implicitly[Dog isOneOf DCA]
    implicitly[Cat isOneOf DCA]
    implicitly[UglyCat isOneOf DCA]
    implicitly[pipo.type isOneOf DCA]
    implicitly[uglyCat.type isOneOf DCA]

    type DC = either[Dog] or Cat
    implicitly[Dog isOneOf DC]
    implicitly[Cat isOneOf DC]
    implicitly[UglyCat isOneOf DC]
    implicitly[pipo.type isOneOf DC]
    implicitly[uglyCat.type isOneOf DC]
    // not here
    implicitly[animal.type isNotOneOf DC]

    type DUC = either[Dog] or UglyCat
    implicitly[Dog isOneOf DUC]
    implicitly[UglyCat isOneOf DUC]
    implicitly[pipo.type isOneOf DUC]
    implicitly[uglyCat.type isOneOf DUC]
    // not here
    implicitly[Cat isNotOneOf DUC]
    implicitly[animal.type isNotOneOf DUC]

    type ISDUC = either[String] or Int or Dog or UglyCat
    type DUCIS = either[Dog] or UglyCat or String or Int
    implicitly[Dog isOneOf ISDUC]
    implicitly[UglyCat isOneOf ISDUC]
    implicitly[pipo.type isOneOf ISDUC]
    implicitly[uglyCat.type isOneOf ISDUC]

    implicitly[Cat isNotOneOf ISDUC]
    implicitly[animal.type isNotOneOf ISDUC]
    implicitly[Cat isNotOneOf DUCIS]
    implicitly[animal.type isNotOneOf DUCIS]
    implicitly[Byte isNotOneOf ISDUC]
  }
}
