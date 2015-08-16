package ohnosequences.cosas

import types._, typeSets._, properties._
import ops.typeSets.ReorderTo

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
    type withProperties[Ps <: AnyTypeSet.Of[AnyProperty]] = AnyFields { type Properties = Ps }
    type withValues[Vs <: AnyTypeSet] = AnyFields { type Values = Vs }

    type size[R <: AnyFields] = typeSets.size[R#Properties]

    implicit def getFieldsOps[R <: AnyFields](record: R): FieldsOps[R] =
      FieldsOps(record)
  }

  case class FieldsOps[R <: AnyFields](val fields: R) extends AnyVal {

    def :&:[P <: AnyProperty](p: P)(implicit check: P ∉ R#Properties): (P :&: R) = records.:&:(p,fields)
  }

  import ops.typeSets.CheckForAll

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

    trait BelongsTo[R <: AnyFields] extends TypePredicate[AnyProperty] {
      type Condition[P <: AnyProperty] = R HasProperty P
    }

    implicit def recordHasPs[R <: AnyFields, Ps <: AnyTypeSet.Of[AnyProperty]]
      (implicit check: CheckForAll[Ps, BelongsTo[R]]):
          (R HasProperties Ps) =
      new (R HasProperties Ps)
  }

  /*
    ## Records

    Records are `AnyType`s wrapping a `Fields` from which they take their `Raw` type: entries for that set of fields.
  */
  trait AnyRecord extends AnyType {

    type Fields <: AnyFields
    val fields: Fields
    type Raw <: Fields#Values
  }

  class Record[R <: AnyFields](val fields: R) extends AnyRecord {

    type Fields = R
    type Raw = Fields#Values

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

  /*
    ### Record ops

    An `apply` method for building denotations of this record type, overloaded so that the fields can be provided in any order.
  */
  case class RecordOps[RT <: AnyRecord](val recType: RT) extends AnyVal {

    def apply(recEntry: RT#Raw): ValueOf[RT] = recType := recEntry

    /* Same as apply, but you can pass fields in any order */
    def apply[Vs <: AnyTypeSet](values: Vs)(implicit
        reorder: Vs ReorderTo RT#Raw
      ): ValueOf[RT] = recType := reorder(values)
  }

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


  // ops
  import fns._, ops.typeSets._

  @annotation.implicitNotFound(msg = "Cannot get property ${P} from record of type ${RT}")
  trait Get[RT <: AnyRecord, P <: AnyProperty]
    extends Fn1[RT#Raw] with Out[P#Raw]

  case object Get {

    implicit def getter[R <: AnyRecord, P <: AnyProperty]
      (implicit
        lookup: R#Raw Lookup ValueOf[P]
      ):  Get[R, P] =
      new Get[R, P] { def apply(recEntry: R#Raw): Out = lookup(recEntry).value }
  }


  @annotation.implicitNotFound(msg = "Cannot update property values ${Ps} from record of type ${RT}")
  trait Update[RT <: AnyRecord, Ps <: AnyTypeSet]
    extends Fn2[RT#Raw, Ps] with Out[ValueOf[RT]]

  case object Update {

    implicit def update[RT <: AnyRecord, Ps <: AnyTypeSet]
      (implicit
        check: Ps ⊂ RT#Raw,
        replace: Replace[RT#Raw, Ps]
      ):  Update[RT, Ps] =
      new Update[RT, Ps] {

        def apply(recRaw: RT#Raw, propReps: Ps): Out = new ValueOf[RT](replace(recRaw, propReps))
      }
  }


  @annotation.implicitNotFound(msg = "Cannot transform record of type ${RT} to ${Other} with values ${Rest}")
  trait Transform[RT <: AnyRecord, Other <: AnyRecord, Rest]
    extends Fn3[RT#Raw, Other, Rest] with Out[ValueOf[Other]]

  case object Transform {

    implicit def transform[
        RT <: AnyRecord,
        Other <: AnyRecord,
        Rest <: AnyTypeSet,
        Uni <: AnyTypeSet,
        Missing <: AnyTypeSet
      ](implicit
        missing: (Other#Raw \ RT#Raw) { type Out = Missing },
        allMissing: Rest ~:~ Missing,
        uni: (RT#Raw ∪ Rest) { type Out = Uni },
        project: Take[Uni, Other#Raw]
      ):  Transform[RT, Other, Rest] =
      new Transform[RT, Other, Rest] {

        def apply(recRaw: RT#Raw, other: Other, rest: Rest): Out =
          other := project(uni(recRaw, rest))
      }

  }

}
