package ohnosequences.typesets.items

import ohnosequences.typesets._
import shapeless._

class ItemTests extends org.scalatest.FunSuite{
  import shapeless.record._
  import shapeless.syntax.singleton._
  import items._

  def typed[T](t : => T) {}

  object intField1 extends FieldOf[Int]
  object intField2 extends FieldOf[Int]
  object stringField1 extends FieldOf[String]
  object stringField2 extends FieldOf[String]
  object boolField1 extends FieldOf[Boolean]
  object boolField2 extends FieldOf[Boolean]
  object doubleField1 extends FieldOf[Double]
  object doubleField2 extends FieldOf[Double]

  test("no duplicated fields") {

    // val notOK =
    //   (intField1    ->>    23) :~:
    //   (stringField1 ->> "foo") :~:
    //   (boolField1   ->>  true) :~:
    //   (intField1    ->>    43) :~:
    //   (doubleField1 ->>   2.0) :~:
    //   ∅


  }

  test("get values") {
    val r1 =
      (intField1    ->>    23) :~:
      (stringField1 ->> "foo") :~:
      (boolField1   ->>  true) :~:
      (doubleField1 ->>   2.0) :~:
      ∅
    
    val v1 = r1.get(intField1)
    typed[Int](v1)
    assert(23 === v1)

    val v2 = r1.get(stringField1)
    typed[String](v2)
    assert("foo" === v2)
  }

  
}

class FunnyItemTests extends org.scalatest.FunSuite {


    import experiment._
    import experiment.TypeSetSyntax._
    import shapeless.record._
    import shapeless.syntax.singleton._

    def typed[T](t : => T) {}

    object intField1 extends FieldOf[Int]
    object intField2 extends FieldOf[Int]
    object stringField1 extends FieldOf[String]
    object stringField2 extends FieldOf[String]
    object boolField1 extends FieldOf[Boolean]
    object boolField2 extends FieldOf[Boolean]
    object doubleField1 extends FieldOf[Double]
    object doubleField2 extends FieldOf[Double]

    test("no duplicated fields") {

      // val notOK =
      //   (intField1    ->>    23) :~:
      //   (stringField1 ->> "foo") :~:
      //   (boolField1   ->>  true) :~:
      //   (intField1    ->>    43) :~:
      //   (doubleField1 ->>   2.0) :~:
      //   ∅


    }

    test("get values") {
      val r1 =
        (intField1    ->>    23) :~:
        (stringField1 ->> "foo") :~:
        (boolField1   ->>  true) :~:
        (doubleField1 ->>   2.0) :~:
        ∅
      
      val v1 = r1.get(intField1)
      typed[Int](v1)
      assert(23 === v1)

    }
}