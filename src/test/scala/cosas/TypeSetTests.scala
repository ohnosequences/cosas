package ohnosequences.cosas.tests

import ohnosequences.cosas._, types._, typeUnions._, typeSets._, fns._
import testFunctions._

case object testFunctions {

  case object toStr extends DepFn1[Any,String] {

    implicit def default[X]: App1[toStr.type, X, String] =
      this at { _.toString }
  }
  case object rev extends DepFn1[Any,Any] {

    implicit val str: App1[rev.type,String,String] =
      rev at { _.reverse }
    implicit def list[T]: App1[rev.type,List[T],List[T]] =
      rev at { _.reverse }
    implicit def default[T]: App1[rev.type,T,T] =
      rev at { x: T => x }
  }
}

class TypeSetTests extends org.scalatest.FunSuite {

  class Bar
  case object bar extends Bar

  test("set size") {

    import shapeless.Nat._

    type Two = ( Int :~: Char :~: ∅ )#Size

    implicitly [ Two =:= _2 ]
  }

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
    assertTypeError("""checkTypes(1 :~: 'a' :~: ∅)""")

    implicitly[(Boolean :~: Int :~: ∅) isBoundedByUnion AllowedTypes]
    implicitly[(Boolean :~: Char :~: Int :~: ∅) isNotBoundedByUnion AllowedTypes]
  }

  test("filtering set by a type predicate") {

    type AllowedTypes = either[Int] or Boolean
    val s = 1 :~: false :~: ∅

    case object isAllowed extends TypePredicate[Any] {
      type Condition[X] = X isOneOf AllowedTypes
    }
    // implicitly[CheckForAll[Int :~: ∅, isAllowed]]
    // implicitly[CheckForAll[Boolean :~: Int :~: ∅, isAllowed]]
    //
    assert{ filter(isAllowed,s) === s }
    assert{ filter(isAllowed,"foo" :~: 1 :~: 'a' :~: ∅) === (1 :~: ∅) }
    //
    case class isInSet[S <: AnyTypeSet](val s: S) extends TypePredicate[Any] {
      type Condition[X] = X ∈ S
    }
    // implicitly[CheckForAll[Boolean :~: Int :~: ∅, isInSet[s.type]]]
    //
    assert{ filter(isInSet(s),s) === s }

    val q = "foo" :~: 1 :~: 'a' :~: ∅
    assert{ filter(isInSet(s),q) === (1 :~: ∅) }
    assert{ filter(isInSet(q),s) === (1 :~: ∅) }
    //
    // s.checkForAll[isAllowed]
    // s.checkForAll[isInSet[s.type]]
    //
    // val q0 = 'a' :~: true :~: "bar" :~: ∅
    // q0.checkForAny[isInSet[s.type]]
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
    assertTypeError("""
      val x = isSubsetOfb[Boolean :~: Int :~: String :~: ∅]
    """)
  }

  test("pop") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅

    assert{ pop[Int](s)    === ((1, 'a' :~: "foo" :~: ∅)) }
    assert{ pop[Char](s)   === (('a', 1 :~: "foo" :~: ∅)) }
    assert{ pop[String](s) === (("foo", 1 :~: 'a' :~: ∅)) }
  }

  test("contains/lookup") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅
    type st = s.type

    implicitly[Int ∈ st]
    assert{ lookup[Int](s) === 1 }

    implicitly[Char ∈ st]
    assert{ lookup[Char](s) === 'a' }

    implicitly[String ∈ st]
    assert{ lookup[String](s) === "foo" }

    trait truth;
    trait happiness;
    implicitly[    truth ∉ st]
    implicitly[happiness ∉ st]

    implicitly[Nothing ∈ st]
  }

  test("projection (take)") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅

    type pt = Char :~: Int :~: ∅

    // implicitly[Take[∅, ∅]]
    // implicitly[Take[Int :~: ∅, Int :~: ∅]]
    // implicitly[Take[Int :~: Char :~: String :~: ∅, Char :~: Int :~: ∅]]
    // implicitly[Take[Int :~: Char :~: String :~: ∅, pt]]
    assert { take[pt](s) === 'a' :~: 1 :~: ∅ }
    assert { take[Int :~: Char :~: String :~: ∅](s) === s }
  }

  test("replace") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅

    assert { replace(∅,∅) === ∅ }
    assert { replace(s, 2 :~: ∅) === 2 :~: 'a' :~: "foo" :~: ∅ }
    assert { replace(s, "bar" :~: ∅) == 1 :~: 'a' :~: "bar" :~: ∅ }
  }

  test("reordering") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅

    assert { reorderTo[∅](∅) === ∅ }
    assert { reorderTo[Char :~: Int :~: String :~: ∅](s) == 'a' :~: 1 :~: "foo" :~: ∅ }

    val p = "bar" :~: 2 :~: 'b' :~: ∅
    // TODO add syntax, possibly test it somewhere else
    // assert((s reorderTo p) == "foo" :~: 1 :~: 'a' :~: ∅)
  }

  test("substraction") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅

    // assert(∅ \ ∅ == ∅)
    // assert(∅ \ s == ∅)
    // assert(s \ ∅ == s)
    // assert(s \ s == ∅)

    val q = bar :~: true :~: 2 :~: "bar" :~: ∅

    // assert(s \ q == 'a' :~: ∅)
    // assert(q \ s == bar :~: true :~: ∅)
  }

  test("union") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅

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

    // import shapeless._, poly._
    //

    assert { mapSet(toStr,∅) === ∅ }

    type S = Int :~: Char :~: String :~: List[Int] :~: ∅
    val s: S = 1 :~: 'a' :~: "foo" :~: List(1,2,3) :~: ∅
    // implicitly[MapSet[id.type, Int :~: Char :~: String :~: List[Int] :~: ∅]]

    assert { mapSet(id, s) === s }

    val x0 = mapSet(id, 1 :~: ∅)
    val x = mapSet(id, 1 :~: 'a' :~: "oof" :~: List(3,2,1) :~: ∅)
    assert { mapSet(rev,s) === 1 :~: 'a' :~: "oof" :~: List(3,2,1) :~: ∅ }

    // // This case should fail, because toStr in not "type-injective"
    // assertTypeError("implicitly[MapSet[toStr.type, s.type]]")
    assertTypeError("s.map(toStr)")
    //
    // assert(s.mapToHList(toStr) == "1" :: "a" :: "foo" :: "List(1, 2, 3)" :: HNil)
    // val uh = mapToListOf[String](toStr,1 :~: ∅)
    assert(mapToListOf[String](toStr,s) === List("1", "a", "foo", "List(1, 2, 3)"))
    // assert(s.mapToHList(toStr).toList == s.mapToList(toStr))
    //
    // assert(∅.mapFold(rev)(0)(_ + _) == 0)
    // assertResult("1 :~: a :~: foo :~: List(1, 2, 3) :~: ∅") {
    //   s.mapFold(toStr)("∅"){ _ + " :~: " + _ }
    // }
    // // TODO: more tests for map-folding
  }

  test("conversions to HList/List") {

    import shapeless._

    // assert(∅.toHList == HNil)
    // assert((1 :~: 'a' :~: "foo" :~: ∅).toHList == (1 :: 'a' :: "foo" :: HNil))

    assert(toListOf[Any](∅) === Nil)
    assert( toListOf[Any](1 :~: 'a' :~: "foo" :~: ∅) === List[Any](1, 'a', "foo"))

    trait foo
    object boo extends foo
    object buh extends foo
    assert(toListOf[foo](boo :~: buh :~: ∅) === List[foo](boo, buh))

    val s = 1 :~: 'a' :~: buh :~: "sdk" :~: boo :~: ∅
    // val buhbuh = pop[foo] from s

  }

  test("getting types of a set of denotations") {

    object foo extends AnyType { val label = "foo"; type Raw = Any }
    object bar extends AnyType { val label = "bar"; type Raw = Any }

    val denots = (foo := 1) :~: (bar := "buh") :~: ∅

    // val typesOf = implicitly[TypesOf[Denotes[Int, foo.type] :~: Denotes[String, bar.type] :~: ∅]]

    assertResult(foo :~: bar :~: ∅) { mapSet(typeOf,denots) }
  }

  test("conversion to a Map") {
    // import properties._
    //
    // assert{ ∅.serialize[Int] === Right( Map() ) }
    // assert{ ∅.toMap[AnyType, Int] === Map() }
    //
    // case object key  extends Property[String]("key")
    // case object name extends Property[String]("name")
    //
    // val set = key("foo") :~: name("bob") :~: ∅
    //
    // assert{ set.serialize[String] === Right( Map("key" -> "foo", "name" -> "bob") ) }
    // assert{ set.toMap[AnyProperty, String] === Map(key -> "foo", name -> "bob") }
  }

}
