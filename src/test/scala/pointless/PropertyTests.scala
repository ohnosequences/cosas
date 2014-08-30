package ohnosequences.pointless.test

import ohnosequences.pointless._, AnyTaggedType._, AnyProperty._, AnyTypeSet._
import shapeless._

object exampleProperties {
  
  case object key extends Property[String]
  case object name extends Property[String]
  case object age extends Property[Integer]
  case object valueless extends Property
}

class uhoh extends org.scalatest.FunSuite {
    
  import exampleProperties._
  import shapeless.test.illTyped

  test("create property instances") {

    val k = key is "2aE5Cgo7Gv62"

    val n = name is "Rigoberto Smith"

    val a = age is 13123
    val a0: Tagged[age.type] = age =>> (13123: Integer)

    illTyped (
    """
      val z = key is 34343
    """
    )

    illTyped (
    """
      val uhoh = age is true
    """
    )
  }

  test("valueless properties lead to nothing") {

    implicitly[AnyTaggedType.RawOf[valueless.type] =:= Nothing]: Unit
  }

  test("deserialize") {
    // using record here just for convenience
    object rec extends Record(name :~: age :~: key :~: ∅)

    val recEntry = rec =>> (
      (name is "foo") :~: 
      (age is 12) :~: 
      (key is "s0dl52f23k") :~: 
      ∅
    )

    // Map parser get's values from map by key, which is the property label
    object MapParser extends Poly1 {

      implicit def caseInteger[P <: AnyProperty.ofType[Integer]] = 
        at[(P, Map[String, String])]{ case (p, m) => (p is m(p.label).toInt, m) }

      implicit def caseString[P <: AnyProperty.ofType[String]] = 
        at[(P, Map[String, String])]{ case (p, m) => (p is m(p.label).toString, m) }
    }

    assertResult(recEntry) {
      import MapParser._

      rec.properties parseFrom Map(
        "age" -> "12",
        "name" -> "foo", 
        "key" -> "s0dl52f23k"
      )
    }

    // List parser just takes the values sequentially, so the order must correspond the order of properties
    object ListParser extends Poly1 {

      implicit def caseInteger[P <: AnyProperty.ofType[Integer]] = 
        at[(P, List[String])]{ case (p, l) => (p is l.head.toInt, l.tail) }

      implicit def caseString[P <: AnyProperty.ofType[String]] = 
        at[(P, List[String])]{ case (p, l) => (p is l.head.toString, l.tail) }
    }

    assertResult(recEntry) {
      import ListParser._

      rec.properties parseFrom List(
        "foo",
        "12",
        "s0dl52f23k"
      )
    }

  }

}
