package ohnosequences.cosas.tests

import ohnosequences.cosas._, types._, typeUnions._, typeSets._, fns._
import testFunctions._


class TypeSetSyntaxTests extends org.scalatest.FunSuite {

  test("lookup syntax") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]
    type st = s.type

    assert{ s.lookup[Int] === 1 }
    assert{ s.lookup[Char] === 'a' }
    assert{ s.lookup[String] === "foo" }
  }

  test("take syntax") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]
    type pt = Char :~: Int :~: ∅[Any]

    assert { s.take[pt] === 'a' :~: 1 :~: ∅[Any] }
    assert { s.take[Int :~: Char :~: String :~: ∅[Any]] === s }
  }

  test("delete syntax") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]

    assert { ( s delete ofType[Char] )   === pop[Char](s)._2 }
    assert { ( s delete ofType[String] ) === pop[String](s)._2 }
  }

  test("pop syntax") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]

    assert{ ( s pop ofType[Int] )    === pop[Int](s)      }
    assert{ ( s pop ofType[Char] )   === pop[Char](s)     }
    assert{ ( s pop ofType[String] ) === pop[String](s)   }
  }

  test("replace syntax") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]

    assert { (∅[Any] replace ∅[Any]) === ∅[Any] }
    assert { (s replace 2 :~: ∅[Any]) === 2 :~: 'a' :~: "foo" :~: ∅[Any] }
    assert { (s replace "bar" :~: ∅[Any]) === 1 :~: 'a' :~: "bar" :~: ∅[Any] }
  }

  test("reorder syntax") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]

    assert { s.reorderTo[Char :~: Int :~: String :~: ∅[Any]] === 'a' :~: 1 :~: "foo" :~: ∅[Any] }
  }

  test("union syntax") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅[Any]
    val q = List(23) :~: true :~: 2 :~: List(23).toString :~: ∅[Any]

    assert { (∅[Any] ∪ ∅[Any]) === union(∅[Any],∅[Any]) }
    assert { (∅[Any] ∪ q) === union(∅[Any],q) }
    assert { (s ∪ ∅[Any]) === union(s,∅[Any]) }
    assert { (s ∪ s) === union(s,s) }
    assert { (s ∪ q) === union(s,q) }
    assert { (q ∪ s) === union(q,s) }
  }

  test("map syntax") {

    type S = Int :~: Char :~: String :~: List[Int] :~: ∅[Any]
    val s: S = 1 :~: 'a' :~: "foo" :~: List(1,2,3) :~: ∅[Any]

    assert { ( ∅[Any] map toStr ) === ∅[Any] }
    assert { ( s map identity ) === mapSet(identity, s) }
    assert { (s map rev ) === mapSet(rev,s) }
  }

  test("mapToListOf syntax") {

    type S = Int :~: Char :~: String :~: List[Int] :~: ∅[Any]
    val s: S = 1 :~: 'a' :~: "foo" :~: List(1,2,3) :~: ∅[Any]

    assert { ( s mapToList toStr ) === mapToListOf[String](toStr,s) }
  }
}
