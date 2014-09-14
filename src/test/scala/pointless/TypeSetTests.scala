package ohnosequences.pointless.tests

import shapeless.test.illTyped
import ohnosequences.pointless._, AnyTypeSet._, AnyWrap._, AnyTypeUnion._
import ops.typeSet._

class TypeSetTests extends org.scalatest.FunSuite {

  test("empty set") {

    implicitly[Any isNotIn ∅[Any]]

    // or with nicer syntax:
    implicitly[Any ∉ ∅[Any]]

    // or Yoda style:
    implicitly[in[∅[Any]]#isNot[Any]]

  }


  test("bounding") {

    implicitly[boundedBy[Nothing]#is[∅[Any]]]

    trait foo
    case object boo extends foo
    case object buh extends foo

    val foos = boo :~: buh :~: ∅[foo]
    implicitly[boundedBy[foo]#is[foos.type]]

    val vals = 1 :~: 'a' :~: true :~: ∅[Any]
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
    checkTypes(1 :~: false :~: ∅[Any])
    illTyped("""checkTypes(1 :~: 'a' :~: ∅)""")

    implicitly[(Boolean :~: Int :~: ∅[Any]) isBoundedByUnion AllowedTypes]
    implicitly[(Boolean :~: Char :~: Int :~: ∅[Any]) isNotBoundedByUnion AllowedTypes]
  }

  test("check smth for every element") {

    type AllowedTypes = either[Int]#or[Boolean]
    val s = 1 :~: false :~: ∅[Any]

    trait isAllowed extends TypePredicate[Any] {
      type Condition[X] = X isOneOf AllowedTypes
    }
    implicitly[CheckForAll[Int :~: ∅[Any], isAllowed]]
    implicitly[CheckForAll[Boolean :~: Int :~: ∅[Any], isAllowed]]

    trait isInSet[S <: AnyTypeSet] extends TypePredicate[Any] {
      type Condition[X] = X ∈ S
    }
    implicitly[CheckForAll[Boolean :~: Int :~: ∅[Any], isInSet[s.type]]]

    s.checkForAll[isAllowed]
    s.checkForAll[isInSet[s.type]]

    val q = 'a' :~: true :~: "bar" :~: ∅[Any]
    q.checkForAny[isInSet[s.type]]

  }

  test("subset") {

    val s = 1 :~: ∅[Any]

    implicitly[∅[Any] ⊂ ∅[Any]]
    implicitly[∅[Any] ⊂ s.type]
    implicitly[s.type ⊂ s.type]

    val a = 100500 :~: 'a' :~: ∅[Any]
    val b = 'b' :~: 1 :~: true :~: ∅[Any]
    implicitly[a.type ⊂ b.type]

    implicitly[(Int :~: Char :~: ∅[Any]) ⊂ (Char :~: Int :~: ∅[Any])]
    implicitly[(Int :~: Char :~: ∅[Any]) ~:~ (Char :~: Int :~: ∅[Any])]

    def isSubsetOfb[S <: AnyTypeSet.SubsetOf[b.type]] = true
    assert(isSubsetOfb[Boolean :~: Int :~: ∅[Any]] == true)
    illTyped("""
      val x = isSubsetOfb[Boolean :~: Int :~: String :~: ∅[Any]]
    """)
  }

  test("pop") {
    val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]
    type st = Int :~: Char :~: String :~: ∅[Any]
    val uhouh = 1 :~: ∅[Any]

    assert(s.pop[Int] == (1, 'a' :~: "foo" :~: ∅[Any]))
    assert(s.pop[String] == ("foo", 1 :~: 'a' :~: ∅[Any]))

  }

  test("contains/lookup") {
    val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]
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
    val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]

    type pt = Char :~: Int :~: ∅[Any]

    implicitly[Take[∅[Any], ∅[Any]]]
    implicitly[Take[Int :~: ∅[Any], Int :~: ∅[Any]]]
    implicitly[Take[Int :~: Char :~: String :~: ∅[Any], Char :~: Int :~: ∅[Any]]]
    implicitly[Take[Int :~: Char :~: String :~: ∅[Any], pt]]
    assert(s.take[pt] == 'a' :~: 1 :~: ∅[Any])
    assert(s.take[Int :~: Char :~: String :~: ∅[Any]] == s)
  }

  test("replace") {
    val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]

    assert(∅[Any].replace(∅[Any]) == ∅[Any])
    assert(s.replace(2 :~: ∅[Any]) == 2 :~: 'a' :~: "foo" :~: ∅[Any])
    assert(s.replace("bar" :~: ∅[Any]) == 1 :~: 'a' :~: "bar" :~: ∅[Any])
  }

  test("reordering") {
    val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]

    assert(∅[Any].reorderTo[∅[Any]] == ∅[Any])
    assert(s.reorderTo[Char :~: Int :~: String :~: ∅[Any]] == 'a' :~: 1 :~: "foo" :~: ∅[Any])

    val p = "bar" :~: 2 :~: 'b' :~: ∅[Any]
    assert((s reorderTo p) == "foo" :~: 1 :~: 'a' :~: ∅[Any])
  }

  test("subtraction") {
    val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]

    assert(∅[Any] \ ∅[Any] == ∅[Any])
    assert(∅[Any] \ s == ∅[Any])
    assert(s \ ∅[Any] == s)
    assert(s \ s == ∅[Any])
    assert(((true :~: s) \ s) == true :~: ∅[Any])

    case object bar
    val q = bar :~: true :~: 2 :~: bar.toString :~: ∅[Any]

    assert(s \ q == 'a' :~: ∅[Any])
    assert(q \ s == bar :~: true :~: ∅[Any])
  }

  test("union") {
    val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]

    case object bar
    val q = bar :~: true :~: 2 :~: bar.toString :~: ∅[Any]

    assert((∅[Any] ∪ ∅[Any]) == ∅[Any])
    assert((∅[Any] ∪ q) == q)
    assert((s ∪ ∅[Any]) == s)

    assert((s ∪ s) == s)

    val sq = s ∪ q
    val qs = q ∪ s
    implicitly[sq.type ~:~ qs.type]
    assert(sq == 'a' :~: bar :~: true :~: 2 :~: "bar" :~: ∅[Any])
    assert(qs == bar :~: 'a' :~: true :~: 2 :~: "bar" :~: ∅[Any])
  }

  test("mappers") {

    import shapeless._, poly._

    object id_ extends Poly1 { implicit def default[T] = at[T]((t:T) => t) }
    object toStr extends (Any -> String)(_.toString)
    object rev extends Poly1 { 
      implicit val str = at[String](t => t.reverse)
      implicit def list[T] = at[List[T]](t => t.reverse)
      implicit def default[T] = at[T](t => t)
    }

    assert(∅[Int].map(id_) == ∅[Int])
    assert(∅[Int].map(toStr) == ∅[String])
    implicitly[MapSet[toStr.type, Int :~: ∅[Int]] { type Out = String :~: ∅[String] }]
    implicitly[Case1[toStr.type, Int] { type Result = String }]
    assert((1 :~: ∅[Int]).map(toStr) == ("1" :~: ∅[String]))


    trait foo
    object boo extends foo
    object qoo extends foo

    // val xoo = qoo :~: ∅[foo]
    // assert(xoo.map(id_) == xoo)
    implicitly[MapSet[id_.type, qoo.type :~: ∅[foo]]](MapSet.cons) //[id_.type, Int, Int, ∅[Any], ∅[Any]])

    val s = 1 :~: 'a' :~: "foo" :~: List(1,2,3) :~: ∅[Any]
    implicitly[MapSet[id_.type, Int :~: Char :~: String :~: List[Int] :~: ∅[Any]]](MapSet.cons)
    assert(s.map(id_) == s)
    assert(s.map(rev) == 1 :~: 'a' :~: "oof" :~: List(3,2,1) :~: ∅[Any])

    // This case should fail, because toStr in not "type-injective"
    illTyped("implicitly[MapSet[toStr.type, s.type]]")
    // illTyped("s.map(toStr)")
    s.map(toStr)

    assert(s.mapToHList(toStr) == "1" :: "a" :: "foo" :: "List(1, 2, 3)" :: HNil)
    assert(s.mapToList(toStr) == List("1", "a", "foo", "List(1, 2, 3)"))
    assert(s.mapToHList(toStr).toList == s.mapToList(toStr))

    assert(∅[Any].mapFold(rev)(0)(_ + _) == 0)
    assertResult("1 :~: a :~: foo :~: List(1, 2, 3) :~: ∅[Any]") {
      s.mapFold(toStr)("∅[Any]"){ _ + " :~: " + _ }
    }
    // TODO: more tests for map-folding
  }

  // test("conversions to HList/List") {

  //   import shapeless._

  //   assert(∅[Any].toHList == HNil)
  //   assert((1 :~: 'a' :~: "foo" :~: ∅[Any]).toHList == (1 :: 'a' :: "foo" :: HNil))

  //   assert(∅[Any].toList == Nil)
  //   assert((1 :~: 'a' :~: "foo" :~: ∅[Any]).toListOf[Any] == List[Any](1, 'a', "foo"))

  //   trait foo
  //   object boo extends foo
  //   object buh extends foo
  //   assert((boo :~: buh :~: ∅[Any]).toList == List[foo](boo, buh))

  //   val s = 1 :~: 'a' :~: buh :~: "sdk" :~: boo :~: ∅[Any]
  //   // val buhbuh = pop[foo] from s

  // }

  // test("conversion from HList") {

  //   import shapeless._

  //   assert(HNil.toTypeSet == ∅[Any])

  //   val l = 1 :: 'a' :: "foo" :: HNil
  //   assert(l.toTypeSet == 1 :~: 'a' :~: "foo" :~: ∅[Any])

  //   illTyped("""(1 :: 'x' :: 2 :: "foo" :: HNil).toTypeSet""")

  // }

  // test("parse") {
  //   case object key extends Property[String]
  //   case object name extends Property[String]
  //   case object age extends Property[Integer]

  //   // using record here just for convenience
  //   object rec extends Record(name :~: age :~: key :~: ∅)

  //   val recEntry = rec(
  //     name("foo") :~: 
  //     age(12) :~: 
  //     key("s0dl52f23k") :~: 
  //     ∅
  //   )

  //   // Map parser get's values from map by key, which is the property label
  //   object MapParser {
  //     implicit def caseInteger[P <: AnyProperty.ofType[Integer]](p: P, m: Map[String, String]):
  //       (ValueOf[P], Map[String, String]) = (p(m(p.label).toInt), m)

  //     implicit def caseString[P <: AnyProperty.ofType[String]](p: P, m: Map[String, String]):
  //       (ValueOf[P], Map[String, String]) = (p(m(p.label).toString), m)
  //   }

  //   assertResult(recEntry.raw) {
  //     import MapParser._

  //     rec.properties parseFrom Map(
  //       "age" -> "12",
  //       "name" -> "foo", 
  //       "key" -> "s0dl52f23k"
  //     )
  //   }

  //   // List parser just takes the values sequentially, so the order must correspond the order of properties
  //   object ListParser {
  //     implicit def caseInteger[P <: AnyProperty.ofType[Integer]](p: P, l: List[String]):
  //       (ValueOf[P], List[String]) = (p(l.head.toInt), l.tail)

  //     implicit def caseString[P <: AnyProperty.ofType[String]](p: P, l: List[String]):
  //       (ValueOf[P], List[String]) = (p(l.head.toString), l.tail)
  //   }

  //   assertResult(recEntry.raw) {
  //     import ListParser._

  //     rec.properties parseFrom List(
  //       "foo",
  //       "12",
  //       "s0dl52f23k"
  //     )
  //   }

  // }

  // test("serialize") {
  //   case object name extends Property[String]
  //   case object age  extends Property[Integer]
  //   case object key  extends Property[String]

  //   val s = name("foo") :~: age(12) :~: key("s0dl52f23k") :~: ∅

  //   // Map //
  //   implicit def anyMapMonoid[X, Y]: Monoid[Map[X, Y]] = new Monoid[Map[X, Y]] {
  //     def zero: M = Map[X, Y]()
  //     def append(a: M, b: M): M = a ++ b
  //   }

  //   implicit def serializeProperty[P <: AnyProperty](t: ValueOf[P])
  //     (implicit getP: ValueOf[P] => P): Map[String, String] = Map(getP(t).label -> t.toString)

  //   assert(
  //     s.serializeTo[Map[String, String]] ==
  //     Map("age" -> "12", "name" -> "foo", "key" -> "s0dl52f23k")
  //   )

  //   assert(
  //     ∅.serializeTo[Map[String, String]] == Map()
  //   )

  //   // List //
  //   implicit def anyListMonoid[X]: Monoid[List[X]] = new Monoid[List[X]] {
  //     def zero: M = List[X]()
  //     def append(a: M, b: M): M = a ++ b
  //   }

  //   implicit def propertyToStr[P <: AnyProperty](t: ValueOf[P])
  //     (implicit getP: ValueOf[P] => P): List[String] = List(getP(t).label + " -> " + t.toString)

  //   assert(
  //     s.serializeTo[List[String]] ==
  //     List("name -> foo", "age -> 12", "key -> s0dl52f23k")
  //   )

  //   assert(
  //     ∅.serializeTo[List[String]] == List()
  //   )

  // }

}
