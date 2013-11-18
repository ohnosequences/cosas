### Index

+ src
  + main
    + scala
      + [LookupInSet.scala](../../main/scala/LookupInSet.md)
      + [MapFoldSets.scala](../../main/scala/MapFoldSets.md)
      + [package.scala](../../main/scala/package.md)
      + [SubtractSets.scala](../../main/scala/SubtractSets.md)
      + [TypeSet.scala](../../main/scala/TypeSet.md)
      + [TypeUnion.scala](../../main/scala/TypeUnion.md)
      + [UnionSets.scala](../../main/scala/UnionSets.md)
  + test
    + scala
      + [TypeSetTests.scala](TypeSetTests.md)

------


```scala
package ohnosequences.typesets.tests

import shapeless.test.illTyped
import ohnosequences.typesets._

class TypeSetTests extends org.scalatest.FunSuite {

  test("empty set") {
    implicitly[in[∅]#isnot[Any]]
    // or with nicer syntax:
    implicitly[Any ∉ ∅]

    assert(set('a') === ('a' :~: ∅))
    assert(1 :~: 'a' === 1 :~: 'a' :~: ∅)
  }

  test("bounding") {
    implicitly[boundedBy[Nothing]#is[∅]]

    trait foo
    case object boo extends foo
    case object buh extends foo

    val foos = boo :~: buh
    implicitly[boundedBy[foo]#is[foos.type]]

    val vals = 1 :~: 'a' :~: true
    implicitly[boundedBy[AnyVal]#is[vals.type]]
  }

  test("subset") {
    val s = set(1)
    implicitly[∅ ⊂ ∅]
    implicitly[∅ ⊂ s.type]
    implicitly[s.type ⊂ s.type]

    val a = 100500 :~: 'a'
    val b = 'b' :~: 1 :~: true
    implicitly[a.type ⊂ b.type]

    implicitly[(Int :~: Char :~: ∅) ⊂ (Char :~: Int :~: ∅)]
  }

  test("contains/lookup") {
    val s = 1 :~: 'a' :~: "foo"
    type st = s.type

    implicitly[Int ∈ st]
    assert(s.lookup[Int] === 1)

    implicitly[Char ∈ st]
    assert(s.lookup[Char] === 'a')

    implicitly[String ∈ st]
    assert(s.lookup[String] === "foo")

    trait happiness; implicitly[happiness ∉ st]
    trait     truth; implicitly[    truth ∉ st]

    // Neither of these two things work:
    // implicitly[Nothing ∈ st]
    // implicitly[Nothing ∉ st]
  }

  test("subtraction") {
    val s = 1 :~: 'a' :~: "foo"

    assert(∅ \ ∅ === ∅)
    assert(∅ \ s === ∅)
    assert(s \ ∅ === s)
    assert(s \ s === ∅)

    case object bar
    val q = bar :~: true :~: 2 :~: bar.toString

    assert(s \ q === set('a'))
    assert(q \ s === bar :~: true)
  }

  test("union") {
    val s = 1 :~: 'a' :~: "foo"

    case object bar
    val q = bar :~: true :~: 2 :~: bar.toString

    assert((∅ U ∅) === ∅)
    assert((∅ U q) === q)
    assert((s U ∅) === s)

    val sq = s U q
    val qs = q U s
    implicitly[sq.type ~ qs.type]
    assert(sq === 'a' :~: bar :~: true :~: 2 :~: "bar")
    assert(qs === bar :~: 'a' :~: true :~: 2 :~: "bar")
  }

}

```

