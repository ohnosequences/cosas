package ohnosequences.cosas.tests

import ohnosequences.cosas._, types._, typeUnions._, typeSets._, fns._
import testFunctions._


class TypeSetSyntaxTests extends org.scalatest.FunSuite {

  test("lookup syntax") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅
    type st = s.type

    assert{ s.lookup[Int] === 1 }
    assert{ s.lookup[Char] === 'a' }
    assert{ s.lookup[String] === "foo" }
  }

  test("take syntax") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅
    type pt = Char :~: Int :~: ∅

    assert { s.take[pt] === 'a' :~: 1 :~: ∅ }
    assert { s.take[Int :~: Char :~: String :~: ∅] === s }
  }

  test("delete syntax") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅

    assert { ( s delete q[Char] )   === pop[Char](s)._2 }
    assert { ( s delete q[String] ) === pop[String](s)._2 }
  }

  test("pop syntax") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅

    assert{ ( s pop q[Int] )    === pop[Int](s)      }
    assert{ ( s pop q[Char] )   === pop[Char](s)     }
    assert{ ( s pop q[String] ) === pop[String](s)   }
  }

  test("replace syntax") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅

    assert { (∅ replace ∅) === ∅ }
    assert { (s replace 2 :~: ∅) === 2 :~: 'a' :~: "foo" :~: ∅ }
    assert { (s replace "bar" :~: ∅) === 1 :~: 'a' :~: "bar" :~: ∅ }
  }

  test("reorder syntax") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅

    assert { s.reorderTo[Char :~: Int :~: String :~: ∅] === 'a' :~: 1 :~: "foo" :~: ∅ }
  }

  test("union syntax") {

    val s = 1 :~: 'a' :~: "foo" :~: ∅
    val q = List(23) :~: true :~: 2 :~: List(23).toString :~: ∅

    assert { (∅ ∪ ∅) === union(∅,∅) }
    assert { (∅ ∪ q) === union(∅,q) }
    assert { (s ∪ ∅) === union(s,∅) }
    assert { (s ∪ s) === union(s,s) }
    assert { (s ∪ q) === union(s,q) }
    assert { (q ∪ s) === union(q,s) }
  }

  test("map syntax") {

    type S = Int :~: Char :~: String :~: List[Int] :~: ∅
    val s: S = 1 :~: 'a' :~: "foo" :~: List(1,2,3) :~: ∅

    assert { ( ∅ map toStr ) === ∅ }
    assert { ( s map identity ) === mapSet(identity, s) }
    assert { (s map rev ) === mapSet(rev,s) }
  }

  test("mapToListOf syntax") {

    type S = Int :~: Char :~: String :~: List[Int] :~: ∅
    val s: S = 1 :~: 'a' :~: "foo" :~: List(1,2,3) :~: ∅

    assert { ( s mapToList toStr ) === mapToListOf[String](toStr,s) }
  }
}
