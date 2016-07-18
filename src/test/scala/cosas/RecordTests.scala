package ohnosequences.cosas.tests

import ohnosequences.cosas._, types._, klists._, records._
import recordTestsContext._

case object recordTestsContext {

  case object id    extends Type[Int]("id")
  case object name  extends Type[String]("name")
  case object email extends Type[String]("email")
  case object color extends Type[String]("color")

  case object simpleUser extends RecordType(id × (name × |[AnyType]))
  case object normalUser extends RecordType(id × (name × (email × (color × |[AnyType]))))

  case class FastaSeq(val id: String) extends Type[(String,Seq[String])](label = s"${id}")

  def parametricRecord(fs: FastaSeq): RecordType[FastaSeq × (color.type × |[AnyType])] =
    new RecordType(fs × (color × |[AnyType]))

  val volatileRec = new RecordType(email × (color × |[AnyType]))

  val volatileRecEntry = volatileRec (
    email("oh@buh.com") ::
    color("blue") ::
    *[AnyDenotation]
  )

  // creating a record instance is easy and neat:
  val simpleUserEntry = simpleUser (
    id(123)     ::
    name("foo") ::
    *[AnyDenotation]
  )

  val normalUserEntry = normalUser := (
    id(123)               ::
    name("foo")           ::
    email("foo@bar.qux")  ::
    color("orange")       ::
    *[AnyDenotation]
  )
}

class RecordTypeTests extends org.scalatest.FunSuite {

  test("should fail when some properties are missing") {

    assertTypeError("""
    val wrongAttrSet = simpleUser(
      id(123) ::
      *[AnyDenotation]
    )
    """)

    assertTypeError("""
    val wrongAttrSet = normalUser(
      id(123)     ::
      name("foo") ::
      *[AnyDenotation]
    )
    """)
  }

  test("labels and typeLabel") {

    assert { simpleUser.label === typeLabel(simpleUser) }
  }

  test("can access fields and field values") {

    assert { (simpleUserEntry get id) =~= id(123) }
    assert { (simpleUserEntry getV id) === 123 }

    assert { (simpleUserEntry get name) =~= name("foo") }
    assert { (simpleUserEntry getV name) === "foo" }

    assert { (normalUserEntry get email) =~= email("foo@bar.qux") }
    assert { (normalUserEntry getV email) === "foo@bar.qux" }
  }

  test("can access fields from vals and volatile vals") {

    assert { (volatileRecEntry get color) =~= color("blue") }
    assert { (volatileRecEntry get email) =~= email("oh@buh.com") }

    val fs = FastaSeq("hola")
    val fsD = fs := ( (">asdf", List("ATCTGCTC")) )
    val zz = parametricRecord(fs) := (
      fsD                 ::
      (color := "green")  :: *[AnyDenotation]
    )

    val zz2 = parametricRecord(fs) := (
      fsD                 ::
      (color := "green")  :: *[AnyDenotation]
    )

    assert { zz =~= zz2 }
    assert { (zz get fs) =~= fsD }
    assert { (typeLabel(fs) === "hola") }
  }

  test("can update fields") {

    assert {
      (normalUserEntry update color("albero")) =~=
        (normalUser(
          (normalUserEntry get id)    ::
          (normalUserEntry get name)  ::
          (normalUserEntry get email) ::
          color("albero")             ::
          *[AnyDenotation]
        )
      )
    }

    assert {
      // NOTE can update in any order
      (normalUserEntry update name("bar") :: id(321) :: *[AnyDenotation]) =~= (
        normalUser(
          id(321)                     ::
          name("bar")                 ::
          (normalUserEntry get email) ::
          (normalUserEntry get color) ::
          *[AnyDenotation]
        )
      )
    }
  }

  test("can transform a klist of values as a record") {

    // assertResult(normalUserEntry) {
    //   (
    //     name("foo")           ::
    //     color("orange")       ::
    //     email("foo@bar.qux")  ::
    //     id(123)               ::
    //     *[AnyDenotation]
    //   ) as normalUser
    // }

    // TODO reorder not fixed yet
    // assertResult(normalUserEntry) {
    //   normalUser from (
    //     name("foo")           ::
    //     email("foo@bar.qux")  ::
    //     color("orange")       ::
    //     id(123)               ::
    //     *[AnyDenotation]
    //   )
    // }

    case object buh extends RecordType(id × |[AnyType])

    val z = buh := id(2) :: *[AnyDenotation { type Tpe <: AnyType }]
    // assertResult(z) {
    //
    //   buh.from(
    //     id(2)     ::
    //     *[AnyDenotation { type Tpe <: AnyType }]
    //   )(Reorder.nonEmpty(pickByType.foundInHead, Reorder.empty))//(Reorder.nonEmpty(pickByType.foundInHead, Reorder.nonEmpty(pickByType.foundInHead, Reorder.empty)))
    // }
  }

  test("product type interop") {

    assert { (simpleUser := simpleUserEntry.toProduct.value) =~= simpleUserEntry }
  }


  ignore("can check if record has properties") {

    // implicitly[simpleUser.SetOfTypes HasProperties (id.type :: name.type :: KNil[AnyDenotation])]
    // implicitly[simpleUser.SetOfTypes HasProperties (name.type :: id.type :: KNil)]
    // implicitly[simpleUser.SetOfTypes HasProperties (name.type :: KNil)]
    // implicitly[simpleUser.SetOfTypes HasProperties (id.type :: KNil)]
    //
    // implicitly[simpleUser.SetOfTypes HasProperty name.type]
    // implicitly[simpleUser.SetOfTypes HasProperty id.type]
    //
    // assertTypeError { """implicitly[simpleUser.SetOfTypes HasProperties (email.type :: id.type :: KNil)]""" }
    // assertTypeError { """implicitly[simpleUser.SetOfTypes HasProperties (email.type :: name.type :: color.type :: KNil)]""" }
  }

  case object propertyConverters {

    val idVParser: String => Option[Int] = str => {
      import scala.util.control.Exception._
      catching(classOf[NumberFormatException]) opt str.toInt
    }

    implicit def idParser: DenotationParser[id.type, Int, String]  = new DenotationParser(id, id.label)(idVParser)

    implicit val idSerializer: DenotationSerializer[id.type, Int, String] = new DenotationSerializer(id, id.label)( { x: Int => Some(x.toString) } )
  }

  ignore("can parse records from Maps") {

    import propertyConverters._

    assert { idParser("hola", "2") === Left(WrongKey(id, "hola", id.label)) }

    val simpleUserEntryMap =  Map(
      "id" -> "29681",
      "name" -> "Antonio"
    )
    val wrongKeyMap = Map(
      "idd" -> "29681",
      "name" -> "Antonio"
    )

    val notIntValueMap = Map(
      "name" -> "Antonio",
      "id" -> "twenty-two"
    )

    val mapWithOtherStuff = simpleUserEntryMap + ("other" -> "stuff")

    assert {
      (simpleUser.parse(simpleUserEntryMap)(ParseDenotations.nonEmpty(idParser, ParseDenotations.nonEmpty(AnyDenotationParser.genericParser, ParseDenotations.emptyParam)))) ===
      Right(simpleUser := id(29681) :: name("Antonio") :: *[AnyDenotation { type Tpe <: AnyType}])
    }
    // TODO fix parsing
    // assert {
    //   simpleUser.parse(wrongKeyMap) ===
    //   Left(KeyNotFound(id.label, wrongKeyMap))
    // }
    // assert {
    //   simpleUser.parse(notIntValueMap) ===
    //   Left(ErrorParsing(ErrorParsingValue(id)("twenty-two")))
    // }
    // assert {
    //   simpleUser.parse(mapWithOtherStuff) ===
    //   Right(simpleUser := id(29681) :: name("Antonio") :: *[AnyDenotation { type Tpe <: AnyType}])
    // }
  }

  test("can serialize records to Maps") {

    import propertyConverters._

    val simpleUserEntryMap =  Map(
      "id" -> "29681",
      "name" -> "Antonio"
    )
    assert {
      Right(simpleUserEntryMap) ===
      (simpleUser := id(29681) :: name("Antonio") :: KNil[AnyDenotation]).serialize
    }

    val unrelatedMap = Map(
      "lala" -> "hola!",
      "ohno" -> "pigeons"
    )

    val mapWithKey = unrelatedMap + ("id" -> "1321")

    assert {
      Right(simpleUserEntryMap ++ unrelatedMap) ===
        (simpleUser := id(29681) :: name("Antonio") :: KNil[AnyDenotation]).serializeUsing(unrelatedMap)
    }

    assert {
      Left(KeyPresent(id.label, mapWithKey)) ===
        (simpleUser := id(29681) :: name("Antonio") :: *[AnyDenotation]).serializeUsing(mapWithKey)
    }
  }
}
