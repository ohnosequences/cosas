// package ohnosequences.typesets.items

// import ohnosequences.typesets._
// import shapeless.FieldOf
// import sampleFields._

// object sampleFields {

//   object intField1 extends FieldOf[Int]
//   object intField2 extends FieldOf[Int]
//   object stringField1 extends FieldOf[String]
//   object stringField2 extends FieldOf[String]
//   object boolField1 extends FieldOf[Boolean]
//   object boolField2 extends FieldOf[Boolean]
//   object doubleField1 extends FieldOf[Double]
//   object doubleField2 extends FieldOf[Double]

//   def typed[T](t : => T) {}
// }

// class ItemTestsTypeSetSyntax extends org.scalatest.FunSuite {

//   import experiment.TypeSetSyntax._

//   test("no duplicated fields") {

//     // val notOK =
//     //   (intField1    ->>    23) :~:
//     //   (stringField1 ->> "foo") :~:
//     //   (boolField1   ->>  true) :~:
//     //   (intField1    ->>    43) :~:
//     //   (doubleField1 ->>   2.0) :~:
//     //   ∅
//   }

//   test("get values") {


//     val r1 =
//       (intField1    ->>    23) :~:
//       (stringField1 ->> "foo") :~:
//       (boolField1   ->>  true) :~:
//       (doubleField1 ->>   2.0) :~:
//       ∅
    
//     val v1 = r1.get(intField1)
//     typed[Int](v1)
//     assert(23 === v1)

//     val v2 = r1.get(stringField1)
//     typed[String](v2)
//     assert("foo" === v2)
//   }
// }

// class FunnyHListItemsTests extends org.scalatest.FunSuite {

//   import shapeless.{::, HNil}
//   import experiment.HListSyntax._

//    def typed[T](t : => T) {}

//   test("no duplicated fields") {

//     // val notOK =
//     //   (intField1    ->>    23) :~:
//     //   (stringField1 ->> "foo") :~:
//     //   (boolField1   ->>  true) :~:
//     //   (intField1    ->>    43) :~:
//     //   (doubleField1 ->>   2.0) :~:
//     //   ∅


//   }

//   test("get values") {
//     val r1 =
//       (intField1    ->>    23) ::
//       (stringField1 ->> "foo") ::
//       (boolField1   ->>  true) ::
//       (doubleField1 ->>   2.0) ::
//       HNil
    
//     val v1 = r1.get(intField1)
//     typed[Int](v1)
//     assert(23 === v1)

//   }

// }


