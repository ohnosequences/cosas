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

    type Two = ( Int :~: Char :~: ∅[Any] )#Size

    implicitly [ Two ≃ _2 ]
  }

  test("empty set") {

    implicitly[Any isNotIn ∅[Any]]

    // or with nicer syntax:
    implicitly[Any ∉ ∅[Any]]

    // or Yoda style:
    implicitly[in[∅[Any]]#isNot[Any]]

  }


  test("bounding") {

    // implicitly[boundedBy[Nothing]#is[∅[Any]]]

    trait foo
    case object boo extends foo
    case object buh extends foo

    val foos = boo :~: buh :~: ∅[foo]
    implicitly[boundedBy[foo]#is[foos.type]]

    val vals = 1 :~: 'a' :~: true :~: ∅[AnyVal]
    implicitly[boundedBy[AnyVal]#is[vals.type]]


    // case class foo[S <: AnyTypeSet.TypeSetOf[Int]]()
    implicitly[foos.type ≤ AnyTypeSet.Of[foo]]
    implicitly[(Int :~: ∅[AnyVal]) ≤ AnyTypeSet.Of[AnyVal]]
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
    def checkTypes[S <: AnyTypeSet.BoundedByUnion[AllowedTypes]](s: S): Unit = assert(true)
    // checkTypes(1 :~: false :~: ∅[AnyVal])
    // assertTypeError("""checkTypes(1 :~: 'a' :~: ∅[Any])""")

    // implicitly[(Boolean :~: Int :~: ∅[Any]) isBoundedByUnion AllowedTypes]
    // implicitly[(Boolean :~: Char :~: Int :~: ∅[Any]) isNotBoundedByUnion AllowedTypes]
  }

  // test("filtering set by a type predicate") {
  //
  //   type AllowedTypes = either[Int] or Boolean
  //   val s = 1 :~: false :~: ∅[Any]
  //
  //   case object isAllowed extends TypePredicate[Any] {
  //     type Condition[X] = X isOneOf AllowedTypes
  //   }
  //   // implicitly[CheckForAll[Int :~: ∅[Any], isAllowed]]
  //   // implicitly[CheckForAll[Boolean :~: Int :~: ∅[Any], isAllowed]]
  //   //
  //   // assert{ filter(isAllowed,s) === s }
  //   assert{ filter(isAllowed,"foo" :~: 1 :~: 'a' :~: ∅[Any]) === (1 :~: ∅[Any]) }
  //   //
  //   case class isInSet[S <: AnyTypeSet](val s: S) extends TypePredicate[Any] {
  //     type Condition[X] = X ∈ S
  //   }
  //   // implicitly[CheckForAll[Boolean :~: Int :~: ∅, isInSet[s.type]]]
  //   //
  //   assert{ filter(isInSet(s),s) === s }
  //
  //   val q = "foo" :~: 1 :~: 'a' :~: ∅[Any]
  //   assert{ filter(isInSet(s),q) === (1 :~: ∅[Any]) }
  //   assert{ filter(isInSet(q),s) === (1 :~: ∅[Any]) }
  //   //
  //   // s.checkForAll[isAllowed]
  //   // s.checkForAll[isInSet[s.type]]
  //   //
  //   // val q0 = 'a' :~: true :~: "bar" :~: ∅[Any]
  //   // q0.checkForAny[isInSet[s.type]]
  // }

  // test("subset") {
  //
  //   val s = 1 :~: ∅[Any]
  //
  //   implicitly[∅[Any] ⊂ ∅[Any]]
  //   implicitly[∅[Any] ⊂ s.type]
  //   implicitly[s.type ⊂ s.type]
  //
  //   val a = 100500 :~: 'a' :~: ∅[Any]
  //   val b = 'b' :~: 1 :~: true :~: ∅[Any]
  //   implicitly[a.type ⊂ b.type]
  //
  //   implicitly[(Int :~: Char :~: ∅[Any]) ⊂ (Char :~: Int :~: ∅[Any])]
  //   implicitly[(Int :~: Char :~: ∅[Any]) ~:~ (Char :~: Int :~: ∅[Any])]
  //
  //   def isSubsetOfb[S <: AnyTypeSet.SubsetOf[b.type]]: Boolean = true
  //   assert(isSubsetOfb[Boolean :~: Int :~: ∅[Any]] == true)
  //   assertTypeError("""
  //     val x = isSubsetOfb[Boolean :~: Int :~: String :~: ∅[Any]]
  //   """)
  // }

  test("pop") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]

    assert{ pop[Int](s)    === ((1, 'a' :~: "foo" :~: ∅[Any])) }
    assert{ pop[Char](s)   === (('a', 1 :~: "foo" :~: ∅[Any])) }
    assert{ pop[String](s) === (("foo", 1 :~: 'a' :~: ∅[Any])) }
  }

  test("contains/lookup") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]
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
  //
  // test("projection (take)") {
  //
  //   val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]
  //
  //   type pt = Char :~: Int :~: ∅[Any]
  //
  //   assert { take[pt](s) === 'a' :~: 1 :~: ∅[Any] }
  //   assert { take[Int :~: Char :~: String :~: ∅[Any]](s) === s }
  // }

  // test("replace") {
  //
  //   val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]
  //
  //   assert { replace(∅[Any],∅[Any]) === ∅[Any] }
  //   assert { replace(s, 2 :~: ∅[Any]) === 2 :~: 'a' :~: "foo" :~: ∅[Any] }
  //   assert { replace(s, "bar" :~: ∅[Any]) == 1 :~: 'a' :~: "bar" :~: ∅[Any] }
  // }

  // test("reordering") {
  //
  //   val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]
  //
  //   assert { reorderTo[∅[Any]](∅[Any]) === ∅[Any] }
  //   assert { reorderTo[Char :~: Int :~: String :~: ∅[Any]](s) === 'a' :~: 1 :~: "foo" :~: ∅[Any] }
  // }

  // test("subtraction") {
  //
  //   val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]
  //
  //   assert(subtract(∅[Any],∅[Any]) === ∅[Any])
  //   assert(subtract(∅[Any],s) === ∅[Any])
  //   assert(subtract(s,∅[Any]) === s)
  //   assert(subtract(s,s) === ∅[Any])
  //
  //   val q = bar :~: true :~: 2 :~: "bar" :~: ∅[Any]
  //
  //   assert(subtract(s,q) === 'a' :~: ∅[Any])
  //   assert(subtract(q,s) === bar :~: true :~: ∅[Any])
  // }

  // test("union") {
  //
  //   val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]
  //   val q = bar :~: true :~: 2 :~: bar.toString :~: ∅[Any]
  //
  //   assert { union(∅[Any], ∅[Any]) === ∅[Any] }
  //   assert { union(∅[Any], q) === q }
  //   // assert { union(s, ∅[Any]) === s }
  //
  //   val sq = union(s,q)
  //   val qs = union(q,s)
  //   // implicitly[sq.type ~:~ qs.type]
  //   assert(sq === 'a' :~: bar :~: true :~: 2 :~: "bar" :~: ∅[Any])
  //   assert(qs === bar :~: 'a' :~: true :~: 2 :~: "bar" :~: ∅[Any])
  // }

  // test("mappers") {
  //
  //   assert { mapSet(toStr,∅[Any]) === ∅[Any] }
  //
  //   type S = Int :~: Char :~: String :~: List[Int] :~: ∅[Any]
  //   val s: S = 1 :~: 'a' :~: "foo" :~: List(1,2,3) :~: ∅[Any]
  //   // implicitly[MapSet[id.type, Int :~: Char :~: String :~: List[Int] :~: ∅[Any]]]
  //
  //   assert { mapSet(identity, s) === s }
  //
  //   val x0 = mapSet(identity, 1 :~: ∅[Any])
  //   val x = mapSet(identity, 1 :~: 'a' :~: "oof" :~: List(3,2,1) :~: ∅[Any])
  //   assert { mapSet(rev,s) === 1 :~: 'a' :~: "oof" :~: List(3,2,1) :~: ∅[Any] }
  //
  //   // // This case should fail, because toStr in not "type-injective"
  //   assertTypeError("mapSet(toStr,s)")
  //
  //   // import shapeless._
  //   // assert(mapToHList(toStr,s) == "1" :: "a" :: "foo" :: "List(1, 2, 3)" :: HNil)
  //
  //   assert(mapToListOf[String](toStr,s) === List("1", "a", "foo", "List(1, 2, 3)"))
  //   // assert(s.mapToHList(toStr).toList == s.mapToList(toStr))
  //   //
  //   // assert(∅[Any].mapFold(rev)(0)(_ + _) == 0)
  //   // assertResult("1 :~: a :~: foo :~: List(1, 2, 3) :~: ∅[Any]") {
  //   //   s.mapFold(toStr)("∅[Any]"){ _ + " :~: " + _ }
  //   // }
  //   // // TODO: more tests for map-folding
  // }
  //
  // test("conversions to HList/List") {
  //
  //   import shapeless._
  //
  //   // assert(∅[Any].toHList == HNil)
  //   // assert((1 :~: 'a' :~: "foo" :~: ∅[Any]).toHList == (1 :: 'a' :: "foo" :: HNil))
  //
  //   assert(toListOf[Any](∅[Any]) === Nil)
  //   assert( toListOf[Any](1 :~: 'a' :~: "foo" :~: ∅[Any]) === List[Any](1, 'a', "foo"))
  //
  //   trait foo
  //   object boo extends foo
  //   object buh extends foo
  //   assert(toListOf[foo](boo :~: buh :~: ∅[Any]) === List[foo](boo, buh))
  //
  //   val s = 1 :~: 'a' :~: buh :~: "sdk" :~: boo :~: ∅[Any]
  //   // val buhbuh = pop[foo] from s
  //
  // }

  // test("getting types of a set of denotations") {
  //
  //   object foo extends AnyType { val label = "foo"; type Raw = Any }
  //   object bar extends AnyType { val label = "bar"; type Raw = Any }
  //
  //   val denots = (foo := 1) :~: (bar := "buh") :~: ∅[Any]
  //
  //   assertResult(foo :~: bar :~: ∅[Any]) { mapSet(typeOf,denots) }
  // }

  test("conversion to a Map") {
    // import properties._
    //
    // assert{ ∅[Any].serialize[Int] === Right( Map() ) }
    // assert{ ∅[Any].toMap[AnyType, Int] === Map() }
    //
    // case object key  extends Property[String]("key")
    // case object name extends Property[String]("name")
    //
    // val set = key("foo") :~: name("bob") :~: ∅[Any]
    //
    // assert{ set.serialize[String] === Right( Map("key" -> "foo", "name" -> "bob") ) }
    // assert{ set.toMap[AnyProperty, String] === Map(key -> "foo", name -> "bob") }
  }

}
