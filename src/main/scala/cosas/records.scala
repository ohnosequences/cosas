package ohnosequences.cosas

import types._, typeSets._, properties._
import ops.typeSets.{ReorderTo, CheckForAll}

case object records {

  /*
    ## Records

    Records wrap a typeset of properties, constructing along the way the typeset of their `ValueOf`s: `Values`.
  */
  trait AnyFields {

    type Properties <: AnyTypeSet // of AnyProperty's
    val properties: Properties

    type Values <: AnyTypeSet
  }

  // TODO aliases for matching etc
  type □  = FNil.type
  val □ : □ = FNil
  case object FNil extends AnyFields {

    type Properties = ∅
    val properties = ∅

    type Values = ∅
  }

  // TODO review this symbol; I'm fine with any other
  case class :&:[P <: AnyProperty, T <: AnyFields]
  (val head: P, val tail: T)(implicit val headIsNew: P ∉ T#Properties) extends AnyFields {

    type Properties = P :~: T#Properties
    val properties: Properties = head :~: (tail.properties: T#Properties)

    type Values = ValueOf[P] :~: T#Values
  }

  case object AnyFields {

    /* Refiners */
    type withBound[B <: AnyProperty] = AnyFields { type Properties <: AnyTypeSet.Of[B] }

    type withProperties[Ps <: AnyTypeSet.Of[AnyProperty]] = AnyFields { type Properties = Ps }
    type withValues[Vs <: AnyTypeSet] = AnyFields { type Values = Vs }

    type size[R <: AnyFields] = typeSets.size[R#Properties]

    implicit def getFieldsOps[R <: AnyFields](record: R): FieldsOps[R] =
      FieldsOps(record)
  }

  case class FieldsOps[R <: AnyFields](val fields: R) extends AnyVal {

    def :&:[P <: AnyProperty](p: P)(implicit check: P ∉ R#Properties): (P :&: R) = records.:&:(p,fields)
  }
  /*
    ## Records

    Records are `AnyType`s wrapping a `Fields` from which they take their `Raw` type: entries for that set of fields.
  */
  trait AnyRecord extends AnyType {

    type Fields <: AnyFields
    val fields: Fields
    type Raw = Fields#Values
  }

  class Record[R <: AnyFields](val fields: R) extends AnyRecord {

    type Fields = R

    lazy val label = toString
  }

  case object AnyRecord {

    type withRecord[R <: AnyFields] = AnyRecord { type Fields = R }
    type withFields[E <: AnyTypeSet] = AnyRecord { type Raw = E }

    implicit def getRecordOps[RT <: AnyRecord](recType: RT): RecordOps[RT] =
      RecordOps(recType)

    implicit def getRecordEntryOps[RT <: AnyRecord](entry: ValueOf[RT]): RecordEntryOps[RT] =
      RecordEntryOps(entry.value)
  }

  @annotation.implicitNotFound(msg = "Cannot prove that ${R} has property ${P}")
  sealed class HasProperty[R <: AnyFields, P <: AnyProperty]

  case object HasProperty {

    implicit def pIsInProperties[R <: AnyFields, P <: AnyProperty]
      (implicit in: P ∈ R#Properties):
          (R HasProperty P) =
      new (R HasProperty P)
  }

  @annotation.implicitNotFound(msg = "Cannot prove that ${R} has properties ${Ps}")
  sealed class HasProperties[R <: AnyFields, Ps <: AnyTypeSet.Of[AnyProperty]]

  object HasProperties {

    trait PropertyIsIn[R <: AnyFields] extends TypePredicate[AnyProperty] {
      type Condition[P <: AnyProperty] = R HasProperty P
    }

    implicit def recordHasPs[R <: AnyFields, Ps <: AnyTypeSet.Of[AnyProperty]]
      (implicit check: CheckForAll[Ps, PropertyIsIn[R]]):
          (R HasProperties Ps) =
      new (R HasProperties Ps)
  }


  /*
    ### Record ops

    An `apply` method for building denotations of this record type, overloaded so that the fields can be provided in any order.
  */
  import ops.records.ParseFieldsFrom
  case class RecordOps[RT <: AnyRecord](val recType: RT) extends AnyVal {

    def apply(recEntry: RT#Raw): ValueOf[RT] = recType := recEntry

    /* Same as apply, but you can pass fields in any order */
    def apply[Vs <: AnyTypeSet](values: Vs)(implicit
        reorder: Vs ReorderTo RT#Raw
      ): ValueOf[RT] = recType := reorder(values)

    def parseFrom[V](map: Map[String,V])(implicit
      parseFieldsFrom: RT#Fields ParseFieldsFrom V
    ): Either[AnyPropertyParsingError, ValueOf[RT]] =
      parseFieldsFrom(map) match {

        case Left(err)  => Left(err)
        case Right(v)   => Right(new ValueOf[RT](v))
      }
  }

  import ops.records._
  /*
    ### Record entry ops

    Operations on `ValueOf`s a record type. As usual with value classes, parameter is of the wrapped type, with the implicits providing them only for value class instances.
  */
  case class RecordEntryOps[RT <: AnyRecord](val entryRaw: RT#Raw) extends AnyVal {

    def get[P <: AnyProperty](p: P)(implicit
      get: RT Get P
    ): ValueOf[P] = p := get(entryRaw)

    def update[P <: AnyProperty](field: ValueOf[P])(implicit
      check: (ValueOf[P] :~: ∅) ⊂ RT#Raw,
      update: RT Update (ValueOf[P] :~: ∅)
    ): ValueOf[RT] = update(entryRaw, field :~: ∅)

    def update[Ps <: AnyTypeSet](fields: Ps)(implicit
      update: RT Update Ps
    ): ValueOf[RT] = update(entryRaw, fields)

    def as[Other <: AnyRecord, Rest <: AnyTypeSet](other: Other, rest: Rest)(implicit
      transform: Transform[RT, Other, Rest]
    ): ValueOf[Other] = transform(entryRaw, other, rest)

    def as[Other <: AnyRecord { type Raw = RT#Raw }](otherEntry: ValueOf[Other]): ValueOf[RT] =
      new ValueOf[RT](otherEntry.value)
  }
}
