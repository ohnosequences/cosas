package ohnosequences.cosas

import types._, typeSets._, properties._
import ops.typeSets.ReorderTo

case object records {

  /*
    ## Records

    Records wrap a typeset of properties, constructing along the way the typeset of their `ValueOf`s: `Values`.
  */
  trait AnyRecord {

    type Properties <: AnyTypeSet // of AnyProperty's
    val properties: Properties

    type Values <: AnyTypeSet
  }

  // TODO aliases for matching etc
  case object EmptyRecord extends AnyRecord {

    type Properties = ∅
    val properties = ∅

    type Values = ∅
  }

  // TODO review this symbol; I'm fine with any other
  case class :@:[P <: AnyProperty, T <: AnyRecord]
  (val head: P, val tail: T)(implicit val headIsNew: P ∉ T#Properties) extends AnyRecord {

    type Properties = P :~: T#Properties
    val properties: Properties = head :~: (tail.properties: T#Properties)

    type Values = ValueOf[P] :~: T#Values
  }

  case object AnyRecord {

    /* Refiners */
    type withProperties[Ps <: AnyTypeSet.Of[AnyProperty]] = AnyRecord { type Properties = Ps }
    type withValues[Vs <: AnyTypeSet] = AnyRecord { type Values = Vs }

    type size[R <: AnyRecord] = typeSets.size[R#Properties]

    implicit def getRecordOps[R <: AnyRecord](record: R): RecordOps[R] =
      RecordOps(record)
  }

  case class RecordOps[R <: AnyRecord](val record: R) extends AnyVal {

    def :@:[P <: AnyProperty](p: P)(implicit check: P ∉ R#Properties): (P :@: R) = records.:@:(p,record)
  }

  import ops.typeSets.CheckForAll
  
  @annotation.implicitNotFound(msg = "Cannot prove that ${R} has property ${P}")
  sealed class HasProperty[R <: AnyRecord, P <: AnyProperty]

  case object HasProperty {

    implicit def pIsInProperties[R <: AnyRecord, P <: AnyProperty]
      (implicit in: P ∈ R#Properties):
          (R HasProperty P) =
      new (R HasProperty P)
  }

  @annotation.implicitNotFound(msg = "Cannot prove that ${R} has properties ${Ps}")
  sealed class HasProperties[R <: AnyRecord, Ps <: AnyTypeSet.Of[AnyProperty]]

  object HasProperties {

    trait BelongsTo[R <: AnyRecord] extends TypePredicate[AnyProperty] {
      type Condition[P <: AnyProperty] = R HasProperty P
    }

    implicit def recordHasPs[R <: AnyRecord, Ps <: AnyTypeSet.Of[AnyProperty]]
      (implicit check: CheckForAll[Ps, BelongsTo[R]]):
          (R HasProperties Ps) =
      new (R HasProperties Ps)
  }

  /*
    ## Record types

    Record types are `AnyType`s wrapping a `Record` from which they take their `Raw` type: entries for that  record.
  */
  trait AnyRecordType extends AnyType {

    type Record <: AnyRecord
    val record: Record
    type Raw <: Record#Values
  }

  class RecordType[R <: AnyRecord](val record: R) extends AnyRecordType {

    type Record = R
    type Raw = Record#Values

    lazy val label = toString
  }

  case object AnyRecordType {

    type withRecord[R <: AnyRecord] = AnyRecordType { type Record = R }
    type withFields[E <: AnyTypeSet] = AnyRecordType { type Raw = E }

    implicit def getRecordTypeOps[RT <: AnyRecordType](recType: RT): RecordTypeOps[RT] =
      RecordTypeOps(recType)

    implicit def getRecordEntryOps[RT <: AnyRecordType](entry: ValueOf[RT]): RecordEntryOps[RT] =
      RecordEntryOps(entry.value)
  }

  /*
    ### RecordType ops

    An `apply` method for building denotations of this record type, overloaded so that the fields can be provided in any order.
  */
  case class RecordTypeOps[RT <: AnyRecordType](val recType: RT) extends AnyVal {

    def apply(recEntry: RT#Raw): ValueOf[RT] = recType := recEntry

    /* Same as apply, but you can pass fields in any order */
    def apply[Vs <: AnyTypeSet](values: Vs)(implicit
        reorder: Vs ReorderTo RT#Raw
      ): ValueOf[RT] = recType := reorder(values)
  }

  /*
    ### RecordType entry ops

    Operations on `ValueOf`s a record type. As usual with value classes, parameter is of the wrapped type, with the implicits providing them only for value class instances.
  */
  case class RecordEntryOps[RT <: AnyRecordType](val entryRaw: RT#Raw) extends AnyVal {

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

    def as[Other <: AnyRecordType, Rest <: AnyTypeSet](other: Other, rest: Rest)(implicit
      transform: Transform[RT, Other, Rest]
    ): ValueOf[Other] = transform(entryRaw, other, rest)

    def as[Other <: AnyRecordType { type Raw = RT#Raw }](otherEntry: ValueOf[Other]): ValueOf[RT] =
      new ValueOf[RT](otherEntry.value)
  }


  // ops
  import fns._, ops.typeSets._

  @annotation.implicitNotFound(msg = "Cannot get property ${P} from record of type ${RT}")
  trait Get[RT <: AnyRecordType, P <: AnyProperty]
    extends Fn1[RT#Raw] with Out[P#Raw]

  case object Get {

    implicit def getter[R <: AnyRecordType, P <: AnyProperty]
      (implicit
        lookup: R#Raw Lookup ValueOf[P]
      ):  Get[R, P] =
      new Get[R, P] { def apply(recEntry: R#Raw): Out = lookup(recEntry).value }
  }


  @annotation.implicitNotFound(msg = "Cannot update property values ${Ps} from record of type ${RT}")
  trait Update[RT <: AnyRecordType, Ps <: AnyTypeSet]
    extends Fn2[RT#Raw, Ps] with Out[ValueOf[RT]]

  case object Update {

    implicit def update[RT <: AnyRecordType, Ps <: AnyTypeSet]
      (implicit
        check: Ps ⊂ RT#Raw,
        replace: Replace[RT#Raw, Ps]
      ):  Update[RT, Ps] =
      new Update[RT, Ps] {

        def apply(recRaw: RT#Raw, propReps: Ps): Out = new ValueOf[RT](replace(recRaw, propReps))
      }
  }


  @annotation.implicitNotFound(msg = "Cannot transform record of type ${RT} to ${Other} with values ${Rest}")
  trait Transform[RT <: AnyRecordType, Other <: AnyRecordType, Rest]
    extends Fn3[RT#Raw, Other, Rest] with Out[ValueOf[Other]]

  case object Transform {

    implicit def transform[
        RT <: AnyRecordType,
        Other <: AnyRecordType,
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
