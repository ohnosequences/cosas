
```scala
package ohnosequences.typesets.tests

import shapeless._
import shapeless.test.illTyped
import ohnosequences.typesets._

class TypeSetTests extends org.scalatest.FunSuite {

  test("empty set") {
    implicitly[in[?]#isnot[Any]]
    // or with nicer syntax:
    implicitly[Any ? ?]

    assert(set('a') === ('a' :~: ?))
  }

  test("bounding") {
    implicitly[boundedBy[Nothing]#is[?]]

    trait foo
    case object boo extends foo
    case object buh extends foo

    val foos = boo :~: buh :~: ?
    implicitly[boundedBy[foo]#is[foos.type]]

    val vals = 1 :~: 'a' :~: true :~: ?
    implicitly[boundedBy[AnyVal]#is[vals.type]]
  }

  test("subset") {
    val s = set(1)
    implicitly[? ? ?]
    implicitly[? ? s.type]
    implicitly[s.type ? s.type]

    val a = 100500 :~: 'a' :~: ?
    val b = 'b' :~: 1 :~: true :~: ?
    implicitly[a.type ? b.type]

    implicitly[(Int :~: Char :~: ?) ? (Char :~: Int :~: ?)]
    implicitly[(Int :~: Char :~: ?) ? (Char :~: Int :~: ?)]
    implicitly[(Int :~: Char :~: ?) ~ (Char :~: Int :~: ?)]
  }

  test("contains/lookup") {
    val s = 1 :~: 'a' :~: "foo" :~: ?
    type st = s.type

    implicitly[Int ? st]
    assert(s.lookup[Int] === 1)

    implicitly[Char ? st]
    assert(s.lookup[Char] === 'a')

    implicitly[String ? st]
    assert(s.lookup[String] === "foo")

    trait truth;
    trait happiness;
    implicitly[    truth ? st]
    implicitly[happiness ? st]

    // Neither of these two things work:
    // implicitly[Nothing ? st]
    // implicitly[Nothing ? st]
  }

  test("pop") {
    val s = 1 :~: 'a' :~: "foo" :~: ?

    assert(s.pop[Int] === (1, 'a' :~: "foo" :~: ?))
    assert(s.pop[Char] === ('a', 1 :~: "foo" :~: ?))
    assert(s.pop[String] === ("foo", 1 :~: 'a' :~: ?))
  }

  test("projection") {
    val s = 1 :~: 'a' :~: "foo" :~: ?

    type pt = Char :~: Int :~: ?

    implicitly[Choose[?, ?]]
    implicitly[Choose[Int :~: ?, Int :~: ?]]
    implicitly[Choose[Int :~: Char :~: String :~: ?, Char :~: Int :~: ?]]
    implicitly[Choose[Int :~: Char :~: String :~: ?, pt]]
    assert(s.project[pt] === 'a' :~: 1 :~: ?)
    assert(s.project[Int :~: Char :~: String :~: ?] === s)
  }

  test("reordering") {
    val s = 1 :~: 'a' :~: "foo" :~: ?

    assert(?.reorder[?] === ?)
    assert(s.reorder[Char :~: Int :~: String :~: ?] === 'a' :~: 1 :~: "foo" :~: ?)

    val p = "bar" :~: 2 :~: 'b' :~: ?
    assert(s ~> p === "foo" :~: 1 :~: 'a' :~: ?)
  }

  test("replace") {
    val s = 1 :~: 'a' :~: "foo" :~: ?

    assert(?.replace(?) === ?)
    assert(s.replace(2 :~: ?) === 2 :~: 'a' :~: "foo" :~: ?)
    assert(s.replace("bar" :~: ?) === 1 :~: 'a' :~: "bar" :~: ?)
  }

  test("subtraction") {
    val s = 1 :~: 'a' :~: "foo" :~: ?

    assert(? \ ? === ?)
    assert(? \ s === ?)
    assert(s \ ? === s)
    assert(s \ s === ?)

    case object bar
    val q = bar :~: true :~: 2 :~: bar.toString :~: ?

    assert(s \ q === set('a'))
    assert(q \ s === bar :~: true :~: ?)
  }

  test("union") {
    val s = 1 :~: 'a' :~: "foo" :~: ?

    case object bar
    val q = bar :~: true :~: 2 :~: bar.toString :~: ?

    assert((? ? ?) === ?)
    assert((? ? q) === q)
    assert((s ? ?) === s)

    val sq = s ? q
    val qs = q ? s
    implicitly[sq.type ~ qs.type]
    assert(sq === 'a' :~: bar :~: true :~: 2 :~: "bar" :~: ?)
    assert(qs === bar :~: 'a' :~: true :~: 2 :~: "bar" :~: ?)
  }

  test("hlist ops") {
    assert(?.toHList === HNil)
    assert((1 :~: 'a' :~: "foo" :~: ?).toHList === (1 :: 'a' :: "foo" :: HNil))
  }

  test("to list") {
    assert(?.toList === Nil)
    assert((1 :~: 'a' :~: "foo" :~: ?).toListWith[Any] === List[Any](1, 'a', "foo"))

    trait foo
    case object boo extends foo
    case object buh extends foo

    val s = boo :~: buh :~: ?
    assert(s.toList === List[foo](boo, buh))
  }

  test("mapper") {
    import poly._

    object id extends Poly1 { implicit def default[T] = at[T](t => t) }
    object toStr extends (Any -> String)(_.toString)
    object rev extends Poly1 { 
      implicit val str = at[String](t => t.reverse)
      implicit def list[T] = at[List[T]](t => t.reverse)
      implicit def default[T] = at[T](t => t)
    }

    assert(?.map(toStr) === ?)

    val s = 1 :~: 'a' :~: "foo" :~: List(1,2,3) :~: ?
    assert(s.map(id) === s)
    assert(s.map(rev) === 1 :~: 'a' :~: "oof" :~: List(3,2,1) :~: ?)

    // This case should fail, because toStr in not "type-injective"
    illTyped("implicitly[SetMapper[toStr.type, s.type]]")
    illTyped("s.map(toStr)")

    assert(s.mapHList(toStr) === "1" :: "a" :: "foo" :: "List(1, 2, 3)" :: HNil)
    assert(s.mapList(toStr) === List("1", "a", "foo", "List(1, 2, 3)"))
    // assert(s.mapHList(toStr).toList === s.mapList(toStr))
  }

}

```


------

### Index

+ src
  + main
    + scala
      + items
        + [items.scala][main/scala/items/items.scala]
      + ops
        + [Choose.scala][main/scala/ops/Choose.scala]
        + [Lookup.scala][main/scala/ops/Lookup.scala]
        + [Map.scala][main/scala/ops/Map.scala]
        + [MapFold.scala][main/scala/ops/MapFold.scala]
        + [Pop.scala][main/scala/ops/Pop.scala]
        + [Reorder.scala][main/scala/ops/Reorder.scala]
        + [Replace.scala][main/scala/ops/Replace.scala]
        + [Subtract.scala][main/scala/ops/Subtract.scala]
        + [ToList.scala][main/scala/ops/ToList.scala]
        + [Union.scala][main/scala/ops/Union.scala]
      + [package.scala][main/scala/package.scala]
      + pointless
        + impl
      + [Property.scala][main/scala/Property.scala]
      + [Record.scala][main/scala/Record.scala]
      + [Representable.scala][main/scala/Representable.scala]
      + [TypeSet.scala][main/scala/TypeSet.scala]
      + [TypeUnion.scala][main/scala/TypeUnion.scala]
  + test
    + scala
      + items
        + [itemsTests.scala][test/scala/items/itemsTests.scala]
      + [RecordTests.scala][test/scala/RecordTests.scala]
      + [TypeSetTests.scala][test/scala/TypeSetTests.scala]

[main/scala/items/items.scala]: ../../main/scala/items/items.scala.md
[main/scala/ops/Choose.scala]: ../../main/scala/ops/Choose.scala.md
[main/scala/ops/Lookup.scala]: ../../main/scala/ops/Lookup.scala.md
[main/scala/ops/Map.scala]: ../../main/scala/ops/Map.scala.md
[main/scala/ops/MapFold.scala]: ../../main/scala/ops/MapFold.scala.md
[main/scala/ops/Pop.scala]: ../../main/scala/ops/Pop.scala.md
[main/scala/ops/Reorder.scala]: ../../main/scala/ops/Reorder.scala.md
[main/scala/ops/Replace.scala]: ../../main/scala/ops/Replace.scala.md
[main/scala/ops/Subtract.scala]: ../../main/scala/ops/Subtract.scala.md
[main/scala/ops/ToList.scala]: ../../main/scala/ops/ToList.scala.md
[main/scala/ops/Union.scala]: ../../main/scala/ops/Union.scala.md
[main/scala/package.scala]: ../../main/scala/package.scala.md
[main/scala/Property.scala]: ../../main/scala/Property.scala.md
[main/scala/Record.scala]: ../../main/scala/Record.scala.md
[main/scala/Representable.scala]: ../../main/scala/Representable.scala.md
[main/scala/TypeSet.scala]: ../../main/scala/TypeSet.scala.md
[main/scala/TypeUnion.scala]: ../../main/scala/TypeUnion.scala.md
[test/scala/items/itemsTests.scala]: items/itemsTests.scala.md
[test/scala/RecordTests.scala]: RecordTests.scala.md
[test/scala/TypeSetTests.scala]: TypeSetTests.scala.md