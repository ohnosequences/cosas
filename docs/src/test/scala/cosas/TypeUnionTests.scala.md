
```scala
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

    implicitly[just[String] ≤ Uh#union]
    implicitly[just[Boolean] ≤ Uh#union]
    implicitly[just[Int] ≤ Uh#union]

    implicitly[S#union =:= just[String]]

    implicitly[just[String] ≤ S#union]
    implicitly[just[Boolean] ≤ SB#union]
    implicitly[just[String] ≤ SB#union]
    implicitly[just[Boolean] ≤ SB2#union]
    implicitly[just[String] ≤ SB2#union]

    implicitly[just[String] ≤ SBI#union]
    implicitly[just[Boolean] ≤ SBI#union]
    implicitly[just[Int] ≤ SBI#union]

    implicitly[just[Byte] !< SBI#union]
    implicitly[just[Byte] ≤ Uh#union]
    implicitly[just[String] ≤ SBI#union]

    implicitly[just[Bar] ≤ BarBIS#union]
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

```




[test/scala/cosas/asserts.scala]: asserts.scala.md
[test/scala/cosas/DenotationTests.scala]: DenotationTests.scala.md
[test/scala/cosas/EqualityTests.scala]: EqualityTests.scala.md
[test/scala/cosas/DependentFunctionsTests.scala]: DependentFunctionsTests.scala.md
[test/scala/cosas/KListsTests.scala]: KListsTests.scala.md
[test/scala/cosas/RecordTests.scala]: RecordTests.scala.md
[test/scala/cosas/NatTests.scala]: NatTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: TypeUnionTests.scala.md
[main/scala/cosas/package.scala]: ../../../main/scala/cosas/package.scala.md
[main/scala/cosas/types/package.scala]: ../../../main/scala/cosas/types/package.scala.md
[main/scala/cosas/types/types.scala]: ../../../main/scala/cosas/types/types.scala.md
[main/scala/cosas/types/parsing.scala]: ../../../main/scala/cosas/types/parsing.scala.md
[main/scala/cosas/types/productTypes.scala]: ../../../main/scala/cosas/types/productTypes.scala.md
[main/scala/cosas/types/syntax.scala]: ../../../main/scala/cosas/types/syntax.scala.md
[main/scala/cosas/types/project.scala]: ../../../main/scala/cosas/types/project.scala.md
[main/scala/cosas/types/denotations.scala]: ../../../main/scala/cosas/types/denotations.scala.md
[main/scala/cosas/types/functionTypes.scala]: ../../../main/scala/cosas/types/functionTypes.scala.md
[main/scala/cosas/types/serialization.scala]: ../../../main/scala/cosas/types/serialization.scala.md
[main/scala/cosas/klists/replace.scala]: ../../../main/scala/cosas/klists/replace.scala.md
[main/scala/cosas/klists/cons.scala]: ../../../main/scala/cosas/klists/cons.scala.md
[main/scala/cosas/klists/klists.scala]: ../../../main/scala/cosas/klists/klists.scala.md
[main/scala/cosas/klists/take.scala]: ../../../main/scala/cosas/klists/take.scala.md
[main/scala/cosas/klists/package.scala]: ../../../main/scala/cosas/klists/package.scala.md
[main/scala/cosas/klists/takeFirst.scala]: ../../../main/scala/cosas/klists/takeFirst.scala.md
[main/scala/cosas/klists/toList.scala]: ../../../main/scala/cosas/klists/toList.scala.md
[main/scala/cosas/klists/filter.scala]: ../../../main/scala/cosas/klists/filter.scala.md
[main/scala/cosas/klists/pick.scala]: ../../../main/scala/cosas/klists/pick.scala.md
[main/scala/cosas/klists/drop.scala]: ../../../main/scala/cosas/klists/drop.scala.md
[main/scala/cosas/klists/map.scala]: ../../../main/scala/cosas/klists/map.scala.md
[main/scala/cosas/klists/at.scala]: ../../../main/scala/cosas/klists/at.scala.md
[main/scala/cosas/klists/syntax.scala]: ../../../main/scala/cosas/klists/syntax.scala.md
[main/scala/cosas/klists/fold.scala]: ../../../main/scala/cosas/klists/fold.scala.md
[main/scala/cosas/klists/noDuplicates.scala]: ../../../main/scala/cosas/klists/noDuplicates.scala.md
[main/scala/cosas/klists/slice.scala]: ../../../main/scala/cosas/klists/slice.scala.md
[main/scala/cosas/klists/find.scala]: ../../../main/scala/cosas/klists/find.scala.md
[main/scala/cosas/records/package.scala]: ../../../main/scala/cosas/records/package.scala.md
[main/scala/cosas/records/recordTypes.scala]: ../../../main/scala/cosas/records/recordTypes.scala.md
[main/scala/cosas/records/syntax.scala]: ../../../main/scala/cosas/records/syntax.scala.md
[main/scala/cosas/records/reorder.scala]: ../../../main/scala/cosas/records/reorder.scala.md
[main/scala/cosas/typeUnions/typeUnions.scala]: ../../../main/scala/cosas/typeUnions/typeUnions.scala.md
[main/scala/cosas/typeUnions/package.scala]: ../../../main/scala/cosas/typeUnions/package.scala.md
[main/scala/cosas/fns/predicates.scala]: ../../../main/scala/cosas/fns/predicates.scala.md
[main/scala/cosas/fns/instances.scala]: ../../../main/scala/cosas/fns/instances.scala.md
[main/scala/cosas/fns/package.scala]: ../../../main/scala/cosas/fns/package.scala.md
[main/scala/cosas/fns/syntax.scala]: ../../../main/scala/cosas/fns/syntax.scala.md
[main/scala/cosas/fns/functions.scala]: ../../../main/scala/cosas/fns/functions.scala.md
[main/scala/cosas/subtyping.scala]: ../../../main/scala/cosas/subtyping.scala.md
[main/scala/cosas/witness.scala]: ../../../main/scala/cosas/witness.scala.md
[main/scala/cosas/equality.scala]: ../../../main/scala/cosas/equality.scala.md
[main/scala/cosas/Nat.scala]: ../../../main/scala/cosas/Nat.scala.md
[main/scala/cosas/Bool.scala]: ../../../main/scala/cosas/Bool.scala.md