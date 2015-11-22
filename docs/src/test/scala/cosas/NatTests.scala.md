
```scala
package ohnosequences.cosas.tests

import ohnosequences.cosas._

final class NatTests extends org.scalatest.FunSuite {

  test("aliases for numbers") {

    assert { Successor(_0) === _1 }
    assert { _1.pred === _0 }
  }

  test("can sum nat vals and types") {

    assert { sum(_1, _0) === sum(_0, _1) }
    assert { sum(_2, _1) === sum(_1, _2) }

    assert { _3 === sum(_2, _1) }
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