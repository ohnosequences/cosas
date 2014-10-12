// package ohnosequences.pointless.tests

// import shapeless.test.{typed, illTyped}
// import ohnosequences.pointless._
// import AnyWrap._, AnyProperty._, AnyTypeSet._, AnyRecord._, AnyTypeUnion._
// import ops.typeSet._

// object RecordTestsContext {

//   case object id extends Property[Integer]
//   case object name extends Property[String]
//   case object notProperty

//   case object simpleUser extends Record(id :~: name :~: ∅[AnyProperty])

//   // more properties:
//   case object email extends Property[String]
//   case object color extends Property[String]

//   case object normalUser extends Record(id :~: name :~: email :~: color :~: ∅[AnyProperty])

//   val vProps = email :~: color :~: ∅[AnyProperty]
//   // nothing works with this
//   val vRecord = new Record(email :~: color :~: ∅[AnyProperty])

//   val vEmail = "oh@buh.com"

//   val vRecordEntry = vRecord(
//     (email(vEmail)) :~:
//     (color("blue")) :~:
//     ∅[AnyWrappedValue]
//   )

//   // val hasRecordWithId = new HasRecordWithId(id, normalUser)

//   // creating a record instance is easy and neat:
//   val simpleUserEntry = simpleUser fields (
//     (id(123)) :~: 
//     (name("foo")) :~: 
//     ∅[AnyWrappedValue]
//   )

//   // this way the order of properties does not matter
//   val normalUserEntry = normalUser fields (
//     (name("foo")) :~: 
//     (color("orange")) :~:
//     (id(123)) :~: 
//     (email("foo@bar.qux")) :~:
//     ∅[AnyWrappedValue]
//   )

// }

// class RecordTests extends org.scalatest.FunSuite {

//   import RecordTestsContext._

//   test("record property bound works") {

//     illTyped("""

//       val uhoh = Record(id :~: name :~: notProperty :~: ∅)
//     """)
//   }

//   test("recognizing record value types") {

//     implicitly[∅[AnyWrappedValue] areValuesOf ∅[AnyWrap]]

//     implicitly [
//       (ValueOf[id.type] :~: ValueOf[name.type] :~: ∅[AnyWrappedValue]) areValuesOf 
//       (id.type :~: name.type :~: ∅[AnyWrap])
//     ]

//     implicitly [ 
//       RawOf[simpleUser.type] =:= 
//       (ValueOf[id.type] :~: ValueOf[name.type] :~: ∅[AnyWrappedValue])
//     ]

//     implicitly [ 
//       // check the Values alias
//       simpleUser.Raw =:= 
//       (ValueOf[id.type] :~: ValueOf[name.type] :~: ∅[AnyWrappedValue])
//     ]

//     implicitly [
//       simpleUser.valuesOfProperties.Out =:= 
//       (ValueOf[id.type] :~: ValueOf[name.type] :~: ∅[AnyWrappedValue])
//     ]
//   }

//   test("can provide properties in different order") {

//     implicitly [ 
//       // the declared property order
//       simpleUser.Raw =:= 
//       (ValueOf[id.type] :~: ValueOf[name.type] :~: ∅[AnyWrappedValue])
//     ]

//     // they get reordered
//     val simpleUserV: ValueOf[simpleUser.type] = simpleUser fields {
//       (name("Antonio")) :~:
//       (id(29681)) :~: 
//       ∅[AnyWrappedValue]
//     }

//     val sameSimpleUserV: ValueOf[simpleUser.type] = simpleUser fields {
//       (id(29681)) :~:
//       (name("Antonio")) :~: 
//       ∅[AnyWrappedValue]
//     }

//     assert {
//       simpleUserV == sameSimpleUserV
//     }
//   }

//   test("should fail when some properties are missing") {
//     // you have to set _all_ properties
//     assertTypeError("""
//     val wrongAttrSet = simpleUser(id(123) :~: ∅)
//     """)

//     // but you still have to present all properties:
//     assertTypeError("""
//     val wrongAttrSet = normalUser fields (
//       id(123) :~:
//       name("foo") :~: 
//       ∅
//     )
//     """)
//   }

//   test("can access property values") {

//     assert{ (simpleUserEntry get id) == id(123) }
//     assert{ (simpleUserEntry get name) == name("foo") }
//   }

//   test("can access property values from vals and volatile vals") {

//     assert{ (vRecordEntry get email) == email("oh@buh.com") }
//   }

//   test("can see a record entry as another") {

//     val hey: ValueOf[simpleUser.type] = normalUserEntry as simpleUser
//   }

//   test("update fields") {

//     assert(
//       (normalUserEntry update (color("albero"))) ==
//       (normalUser fields (
//         (normalUserEntry get name) :~: 
//         (normalUserEntry get id) :~: 
//         (normalUserEntry get email) :~:
//         (color("albero")) :~:
//         ∅
//       ))
//     )

//     assert(
//       (normalUserEntry update ((name("bar")) :~: (id(321)) :~: ∅)) ==
//       (normalUser fields (
//         (name("bar")) :~: 
//         (color("orange")) :~:
//         (id(321)) :~: 
//         (email("foo@bar.qux")) :~:
//         ∅
//       ))
//     )

//   }

//   test("having properties") {

//     implicitly[simpleUser.type HasProperties (id.type :~: name.type :~: ∅[AnyProperty])]
//     implicitly[simpleUser.type HasProperties (name.type :~: id.type :~: ∅[AnyProperty])]
//     implicitly[simpleUser.type HasProperties (name.type :~: ∅[AnyProperty])]
//     implicitly[simpleUser.type HasProperties (id.type :~: ∅[AnyProperty])]

//     implicitly[simpleUser.type HasProperty name.type]
//     implicitly[simpleUser.type HasProperty id.type]

//     // adding some moar properties
//     implicit val useremail = simpleUser has email
//     implicit val usercolor = simpleUser has color

//     implicitly[simpleUser.type HasProperties (email.type :~: id.type :~: ∅[AnyProperty])]
//     implicitly[simpleUser.type HasProperties (email.type :~: name.type :~: color.type :~: ∅[AnyProperty])]
//   }

//   test("parsing") {
//     // Map parser get's values from map by key, which is the property label
//     object MapParser {
//       implicit def caseInteger[P <: AnyProperty with AnyWrap.withRaw[Integer]](p: P, m: Map[String, String]):
//         (ValueOf[P], Map[String, String]) = (p(m(p.label).toInt), m)

//       implicit def caseString[P <: AnyProperty with AnyWrap.withRaw[String]](p: P, m: Map[String, String]):
//         (ValueOf[P], Map[String, String]) = (p(m(p.label).toString), m)
//     }

//     assertResult(normalUserEntry) {
//       import MapParser._

//       normalUser parseFrom Map(
//         "name" -> "foo",
//         "color" -> "orange",
//         "id" -> "123",
//         "email" -> "foo@bar.qux"
//       )
//     }

//     // List parser just takes the values sequentially, so the order must correspond the order of properties
//     object ListParser {
//       implicit def caseInteger[P <: AnyProperty with AnyWrap.withRaw[Integer]](p: P, l: List[String]):
//         (ValueOf[P], List[String]) = (p(l.head.toInt), l.tail)

//       implicit def caseString[P <: AnyProperty with AnyWrap.withRaw[String]](p: P, l: List[String]):
//         (ValueOf[P], List[String]) = (p(l.head.toString), l.tail)
//     }

//     assertResult(normalUserEntry) {
//       import ListParser._

//       normalUser parseFrom List(
//         "123",
//         "foo",
//         "foo@bar.qux",
//         "orange"
//       )
//     }

//   }

//   test("serialize") {
//     // Map //
//     implicit def anyMapMonoid[X, Y]: Monoid[Map[X, Y]] = new Monoid[Map[X, Y]] {
//       def zero: M = Map[X, Y]()
//       def append(a: M, b: M): M = a ++ b
//     }

//     implicit def serializeProperty[P <: AnyProperty](t: ValueOf[P])
//       (implicit getP: ValueOf[P] => P): Map[String, String] = Map(getP(t).label -> t.toString)

//     assert(
//       normalUserEntry.serializeTo[Map[String, String]] == 
//       Map(
//         "name" -> "foo",
//         "color" -> "orange",
//         "id" -> "123",
//         "email" -> "foo@bar.qux"
//       )
//     )

//     // List //
//     implicit def anyListMonoid[X]: Monoid[List[X]] = new Monoid[List[X]] {
//       def zero: M = List[X]()
//       def append(a: M, b: M): M = a ++ b
//     }

//     implicit def propertyIntToStr[P <: AnyProperty](t: ValueOf[P])
//       (implicit getP: ValueOf[P] => P): List[String] = List(getP(t).label + " -> " + t.toString)

//     // implicit def toStr[P](p: P): List[String] = List(p.toString)

//     assert(
//       normalUserEntry.serializeTo[List[String]] ==
//       List("id -> 123", "name -> foo", "email -> foo@bar.qux", "color -> orange")
//     )

//   }

// }
