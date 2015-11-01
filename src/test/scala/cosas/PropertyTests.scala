// package ohnosequences.cosas.tests
//
// import ohnosequences.cosas._, types._, properties._, typeSets._
//
// case object exampleProperties {
//
//   case object key       extends Property[String]("key")
//   case object name      extends Property[String]("name")
//   case object age       extends Property[Int]("age")
//   case object valueless extends Property("valueless")
// }
//
// class PropertyTests extends org.scalatest.FunSuite {
//
//   import exampleProperties._
//
//   test("can create fields") {
//
//     val k: ValueOf[key.type] = key := "2aE5Cgo7Gv62"
//
//     val n: ValueOf[name.type] = name := "Rigoberto Smith"
//
//     val a = age := 13123
//     val a0: ValueOf[age.type] = age := (13123: Integer)
//
//     assertTypeError("""val z = key(34343)""")
//     assertTypeError("""val uhoh = age(true)""")
//   }
//
//   test("properties which do not set Raw have Raw = Nothing") {
//
//     val itIs = implicitly[valueless.Raw =:= Nothing]
//   }
// }
