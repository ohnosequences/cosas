package ohnosequences.cosas.tests

import shapeless.test.illTyped
import ohnosequences.cosas._, AnyTypeSet._, AnyWrap._, AnyTypeUnion._
import ops.typeSet._

class TypeSetTests extends org.scalatest.FunSuite {

  test("empty set") {

    implicitly[Any isNotIn ∅]

    // or with nicer syntax:
    implicitly[Any ∉ ∅]

    // or Yoda style:
    implicitly[in[∅]#isNot[Any]]

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

    type AllowedTypes = either[Int]#or[Boolean]
    def checkTypes[S <: AnyTypeSet.BoundedByUnion[AllowedTypes]](s: S) = assert(true)
    checkTypes(1 :~: false :~: ∅)
    illTyped("""checkTypes(1 :~: 'a' :~: ∅)""")

    implicitly[(Boolean :~: Int :~: ∅) isBoundedByUnion AllowedTypes]
    implicitly[(Boolean :~: Char :~: Int :~: ∅) isNotBoundedByUnion AllowedTypes]
  }

  test("check smth for every element") {

    type AllowedTypes = either[Int]#or[Boolean]
    val s = 1 :~: false :~: ∅

    trait isAllowed extends TypePredicate[Any] {
      type Condition[X] = X isOneOf AllowedTypes
    }
    implicitly[CheckForAll[Int :~: ∅, isAllowed]]
    implicitly[CheckForAll[Boolean :~: Int :~: ∅, isAllowed]]

    trait isInSet[S <: AnyTypeSet] extends TypePredicate[Any] {
      type Condition[X] = X ∈ S
    }
    implicitly[CheckForAll[Boolean :~: Int :~: ∅, isInSet[s.type]]]

    s.checkForAll[isAllowed]
    s.checkForAll[isInSet[s.type]]

    val q = 'a' :~: true :~: "bar" :~: ∅
    q.checkForAny[isInSet[s.type]]

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
    type st = Int :~: Char :~: String :~: ∅
    val uhouh = 1 :~: ∅
    assert(s.pop[Int] == ((1, 'a' :~: "foo" :~: ∅)))
    // val uh: (Char, Int :~: String :~: ∅) = pop[Char,Char] from s
    // assert(s.pop[Char](
    //        // implicitly[Char ∈ st], 
    //        Pop.foundInTail(Pop.foundInHead)
    //        ) == ('a', 1 :~: "foo" :~: ∅))
    
    // val hhhh: (Char, Int :~: String :~: ∅)  = pop[AnyVal, Char] from s
    assert(s.pop[String] == (("foo", 1 :~: 'a' :~: ∅)))

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

    assert(∅.reorderTo[∅] == ∅)
    assert(s.reorderTo[Char :~: Int :~: String :~: ∅] == 'a' :~: 1 :~: "foo" :~: ∅)

    val p = "bar" :~: 2 :~: 'b' :~: ∅
    assert((s reorderTo p) == "foo" :~: 1 :~: 'a' :~: ∅)
  }

  test("substraction") {
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

    assert((s ∪ s) == s)

    val sq = s ∪ q
    val qs = q ∪ s
    implicitly[sq.type ~:~ qs.type]
    assert(sq == 'a' :~: bar :~: true :~: 2 :~: "bar" :~: ∅)
    assert(qs == bar :~: 'a' :~: true :~: 2 :~: "bar" :~: ∅)
  }

  test("mappers") {

    import shapeless._, poly._

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

    import shapeless._

    assert(∅.toHList == HNil)
    assert((1 :~: 'a' :~: "foo" :~: ∅).toHList == (1 :: 'a' :: "foo" :: HNil))

    assert(∅.toList == Nil)
    assert((1 :~: 'a' :~: "foo" :~: ∅).toListOf[Any] == List[Any](1, 'a', "foo"))

    trait foo
    object boo extends foo
    object buh extends foo
    assert((boo :~: buh :~: ∅).toList == List[foo](boo, buh))

    val s = 1 :~: 'a' :~: buh :~: "sdk" :~: boo :~: ∅
    // val buhbuh = pop[foo] from s

  }

  test("conversion from HList") {

    import shapeless._

    assert(HNil.toTypeSet == ∅)

    val l = 1 :: 'a' :: "foo" :: HNil
    assert(l.toTypeSet == 1 :~: 'a' :~: "foo" :~: ∅)

    illTyped("""(1 :: 'x' :: 2 :: "foo" :: HNil).toTypeSet""")

  }

  test("parse") {
    case object key extends Property[String]("key")
    case object name extends Property[String]("name")
    case object age extends Property[Integer]("age")

    // using record here just for convenience
    object rec extends Record(name :~: age :~: key :~: ∅)

    val recEntry = rec(
      name("foo") :~: 
      age(12) :~: 
      key("s0dl52f23k") :~: 
      ∅
    )

    // Map parser get's values from map by key, which is the property label
    object MapParser {
      implicit def caseInteger[P <: AnyProperty.ofType[Integer]](p: P, m: Map[String, String]):
        (ValueOf[P], Map[String, String]) = (p(m(p.label).toInt), m)

      implicit def caseString[P <: AnyProperty.ofType[String]](p: P, m: Map[String, String]):
        (ValueOf[P], Map[String, String]) = (p(m(p.label).toString), m)
    }

    assertResult(recEntry.value) {
      import MapParser._

      rec.properties parseFrom Map(
        "age" -> "12",
        "name" -> "foo", 
        "key" -> "s0dl52f23k"
      )
    }

    // List parser just takes the values sequentially, so the order must correspond the order of properties
    object ListParser {
      implicit def caseInteger[P <: AnyProperty.ofType[Integer]](p: P, l: List[String]):
        (ValueOf[P], List[String]) = (p(l.head.toInt), l.tail)

      implicit def caseString[P <: AnyProperty.ofType[String]](p: P, l: List[String]):
        (ValueOf[P], List[String]) = (p(l.head.toString), l.tail)
    }

    assertResult(recEntry.value) {
      import ListParser._

      rec.properties parseFrom List(
        "foo",
        "12",
        "s0dl52f23k"
      )
    }

  }

  test("serialize") {
    case object name extends Property[String]("name")
    case object age  extends Property[Integer]("age")
    case object key  extends Property[String]("key")

    val s = name("foo") :~: age(12) :~: key("s0dl52f23k") :~: ∅

    // Map //
    implicit def anyMapMonoid[X, Y]: Monoid[Map[X, Y]] = new Monoid[Map[X, Y]] {
      def zero: M = Map[X, Y]()
      def append(a: M, b: M): M = a ++ b
    }

    implicit def serializeProperty[P <: AnyProperty](t: ValueOf[P])
      (implicit getP: ValueOf[P] => P): Map[String, String] = Map(getP(t).label -> t.toString)

    // assert(
    //   s.serializeTo[Map[String, String]] ==
    //   Map("age" -> "12", "name" -> "foo", "key" -> "s0dl52f23k")
    // )

    // assert(
    //   ∅.serializeTo[Map[String, String]] == Map()
    // )

    // List //
    implicit def anyListMonoid[X]: Monoid[List[X]] = new Monoid[List[X]] {
      def zero: M = List[X]()
      def append(a: M, b: M): M = a ++ b
    }

    implicit def propertyToStr[P <: AnyProperty](t: ValueOf[P])
      (implicit getP: ValueOf[P] => P): List[String] = List(getP(t).label + " -> " + t.toString)

    // assert(
    //   s.serializeTo[List[String]] ==
    //   List("name -> foo", "age -> 12", "key -> s0dl52f23k")
    // )

    assert(
      ∅.serializeTo[List[String]] == List()
    )

  }

}
