package ohnosequences.pointless.tests

import shapeless._
import shapeless.test.illTyped
import ohnosequences.pointless._, AnyTypeSet._

class TypeSetTests extends org.scalatest.FunSuite {

  test("empty set") {

    implicitly[Any isNotIn ∅]

    // or with nicer syntax:
    implicitly[Any ∉ ∅]

    // FIXME: Yoda style doesn't work
    // implicitly[in[∅]#isNot[Any]]

  }


  test("bounding") {

    implicitly[boundedBy[Nothing]#is[∅]]

    trait foo
    case object boo extends foo
    case object buh extends foo

    val foos = boo :~: buh :~: ∅
    implicitly[boundedBy[foo]#is[foos.type]]

    val vals = 1 :~: 'a' :~: true :~: ∅
    implicitly[boundedBy[AnyVal]#is[vals.type]]


    // case class foo[S <: AnyTypeSet.TypeSetOf[Int]]()
    implicitly[foos.type <:< AnyTypeSet.Of[foo]]
    // implicitly[(Int :~: ∅) <:< AnyTypeSet.Of[AnyVal]]
    trait goos {
      type F <: AnyTypeSet.Of[foo]
      val  f: F
    }
    object g extends goos {
      type F = foos.type
      val f = foos: F
    }
    case class boos[T <: AnyTypeSet.Of[foo]](t: T) extends goos {
      type F = T
      val f = t
    }
    val b = boos(foos)

  }


  test("subset") {

    val s = 1 :~: ∅

    implicitly[∅ ⊂ ∅]
    implicitly[∅ ⊂ s.type]
    implicitly[s.type ⊂ s.type]

    val a = 100500 :~: 'a' :~: ∅
    val b = 'b' :~: 1 :~: true :~: ∅
    implicitly[a.type ⊂ b.type]

    implicitly[(Int :~: Char :~: ∅) ⊂ (Char :~: Int :~: ∅)]
    implicitly[(Int :~: Char :~: ∅) ~:~ (Char :~: Int :~: ∅)]

    def isSubsetOfb[S <: AnyTypeSet.SubsetOf[b.type]] = true
    assert(isSubsetOfb[Boolean :~: Int :~: ∅] == true)
    illTyped("""
      val x = isSubsetOfb[Boolean :~: Int :~: String :~: ∅]
    """)
  }

  test("pop") {
    val s = 1 :~: 'a' :~: "foo" :~: ∅

    assert(s.pop[Int] == (1, 'a' :~: "foo" :~: ∅))
    assert(s.pop[Char] == ('a', 1 :~: "foo" :~: ∅))
    assert(s.pop[String] == ("foo", 1 :~: 'a' :~: ∅))
  }

  test("contains/lookup") {
    val s = 1 :~: 'a' :~: "foo" :~: ∅
    type st = s.type

    implicitly[Int ∈ st]
    assert(s.lookup[Int] == 1)

    implicitly[Char ∈ st]
    assert(s.lookup[Char] == 'a')

    implicitly[String ∈ st]
    assert(s.lookup[String] == "foo")

    trait truth;
    trait happiness;
    implicitly[    truth ∉ st]
    implicitly[happiness ∉ st]

    implicitly[Nothing ∈ st]
  }

  test("projection (take)") {
    val s = 1 :~: 'a' :~: "foo" :~: ∅

    type pt = Char :~: Int :~: ∅

    implicitly[Take[∅, ∅]]
    implicitly[Take[Int :~: ∅, Int :~: ∅]]
    implicitly[Take[Int :~: Char :~: String :~: ∅, Char :~: Int :~: ∅]]
    implicitly[Take[Int :~: Char :~: String :~: ∅, pt]]
    assert(s.take[pt] == 'a' :~: 1 :~: ∅)
    assert(s.take[Int :~: Char :~: String :~: ∅] == s)
  }

  test("replace") {
    val s = 1 :~: 'a' :~: "foo" :~: ∅

    assert(∅.replace(∅) == ∅)
    assert(s.replace(2 :~: ∅) == 2 :~: 'a' :~: "foo" :~: ∅)
    assert(s.replace("bar" :~: ∅) == 1 :~: 'a' :~: "bar" :~: ∅)
  }

  test("reordering") {
    val s = 1 :~: 'a' :~: "foo" :~: ∅

    assert(∅.as[∅] == ∅)
    assert(s.as[Char :~: Int :~: String :~: ∅] == 'a' :~: 1 :~: "foo" :~: ∅)

    val p = "bar" :~: 2 :~: 'b' :~: ∅
    assert((s as p) == "foo" :~: 1 :~: 'a' :~: ∅)
  }

  test("subtraction") {
    val s = 1 :~: 'a' :~: "foo" :~: ∅

    assert(∅ \ ∅ == ∅)
    assert(∅ \ s == ∅)
    assert(s \ ∅ == s)
    assert(s \ s == ∅)

    case object bar
    val q = bar :~: true :~: 2 :~: bar.toString :~: ∅

    assert(s \ q == 'a' :~: ∅)
    assert(q \ s == bar :~: true :~: ∅)
  }

  test("union") {
    val s = 1 :~: 'a' :~: "foo" :~: ∅

    case object bar
    val q = bar :~: true :~: 2 :~: bar.toString :~: ∅

    assert((∅ ∪ ∅) == ∅)
    assert((∅ ∪ q) == q)
    assert((s ∪ ∅) == s)

    val sq = s ∪ q
    val qs = q ∪ s
    implicitly[sq.type ~:~ qs.type]
    assert(sq == 'a' :~: bar :~: true :~: 2 :~: "bar" :~: ∅)
    assert(qs == bar :~: 'a' :~: true :~: 2 :~: "bar" :~: ∅)
  }

  test("mappers") {

    import poly._

    object id extends Poly1 { implicit def default[T] = at[T]((t:T) => t) }
    object toStr extends (Any -> String)(_.toString)
    object rev extends Poly1 { 
      implicit val str = at[String](t => t.reverse)
      implicit def list[T] = at[List[T]](t => t.reverse)
      implicit def default[T] = at[T](t => t)
    }

    assert(∅.map(toStr) == ∅)

    val s = 1 :~: 'a' :~: "foo" :~: List(1,2,3) :~: ∅
    implicitly[MapSet[id.type, Int :~: Char :~: String :~: List[Int] :~: ∅]]
    assert(s.map(id) == s)
    assert(s.map(rev) == 1 :~: 'a' :~: "oof" :~: List(3,2,1) :~: ∅)

    // This case should fail, because toStr in not "type-injective"
    illTyped("implicitly[MapSet[toStr.type, s.type]]")
    illTyped("s.map(toStr)")

    assert(s.mapToHList(toStr) == "1" :: "a" :: "foo" :: "List(1, 2, 3)" :: HNil)
    assert(s.mapToList(toStr) == List("1", "a", "foo", "List(1, 2, 3)"))
    assert(s.mapToHList(toStr).toList == s.mapToList(toStr))

    assert(∅.mapFold(rev)(0)(_ + _) == 0)
    assertResult("1 :~: a :~: foo :~: List(1, 2, 3) :~: ∅") {
      s.mapFold(toStr)("∅"){ _ + " :~: " + _ }
    }
    // TODO: more tests for map-folding
  }

  test("conversions to HList/List") {

    assert(∅.toHList == HNil)
    assert((1 :~: 'a' :~: "foo" :~: ∅).toHList == (1 :: 'a' :: "foo" :: HNil))

    assert(∅.toList == Nil)
    assert((1 :~: 'a' :~: "foo" :~: ∅).toListOf[Any] == List[Any](1, 'a', "foo"))

    trait foo
    object boo extends foo
    object buh extends foo
    assert((boo :~: buh :~: ∅).toList == List[foo](boo, buh))

  }

  test("conversion from HList") {

    assert(HNil.toTypeSet == ∅)
    assert(fromHList(HNil) == ∅)

    val l = 1 :: 'a' :: "foo" :: HNil
    assert(l.toTypeSet == 1 :~: 'a' :~: "foo" :~: ∅)

    illTyped("""(1 :: 'x' :: 2 :: "foo" :: HNil).toTypeSet""")

  }

}
