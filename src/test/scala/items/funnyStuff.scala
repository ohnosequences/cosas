package lalala

class FunnyHListItemsTests extends org.scalatest.FunSuite {

  import shapeless._
  import shapeless.record._
  import shapeless.syntax.singleton._
  import ohnosequences.typesets.items.experiment.HListSyntax._

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
        (intField1    ->>    23) ::
        (stringField1 ->> "foo") ::
        (boolField1   ->>  true) ::
        (doubleField1 ->>   2.0) ::
        HNil
      
      val v1 = r1.get(intField1)
      typed[Int](v1)
      assert(23 === v1)

    }

}

class FunnyItemTests extends org.scalatest.FunSuite {

    import ohnosequences.typesets._
    import ohnosequences.typesets.items.experiment._
    import ohnosequences.typesets.items.experiment.TypeSetSyntax._
    import shapeless._
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