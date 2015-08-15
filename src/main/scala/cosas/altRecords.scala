package ohnosequences.cosas

import types._, typeSets._, properties._

case object altRecords {

  trait AnyRecord {

    type Properties <: AnyTypeSet // of AnyProperty's
    val properties: Properties

    type Values <: AnyTypeSet
  }

  case object AnyRecord {

    /* Refiners */
    type withProperties[Ps <: AnyTypeSet.Of[AnyProperty]] = AnyRecord { type Properties = Ps }
    type withValues[Vs <: AnyTypeSet] = AnyRecord { type Values = Vs }
  }


  case object EmptyRecord extends AnyRecord {

    type Properties = ∅
    val properties = ∅

    type Values = ∅
  }

  case class :@:[P <: AnyProperty, T <: AnyRecord]
  (val head: P, val tail: T)(implicit val headIsNew: P ∉ T#Properties) extends AnyRecord {

    type Properties = P :~: T#Properties
    val properties: Properties = head :~: (tail.properties: T#Properties)

    type Values = ValueOf[P] :~: T#Values
  }

  trait AnyRecordType extends AnyType {

    type Record <: AnyRecord
    val record: Record
    type Raw <: Record#Values
  }
  class RecordType[R <: AnyRecord](val record: R) extends AnyRecordType {

    type Record = R
    type Raw = Record#Values

    val label = "buh!"
  }

  implicit def getRecordOps[R <: AnyRecord](record: R): RecordOps[R] =
    RecordOps(record)
  case class RecordOps[R <: AnyRecord](val record: R) extends AnyVal {

    def :@:[P <: AnyProperty](p: P)(implicit check: P ∉ R#Properties): (P :@: R) = new :@:(p,record)
  }

  import ops.typeSets.ReorderTo

  implicit def getRecordTypeOps[RT <: AnyRecordType](recType: RT): RecordTypeOps[RT] =
    RecordTypeOps(recType)
  case class RecordTypeOps[RT <: AnyRecordType](val recType: RT) extends AnyVal {

    def apply(recEntry: RT#Raw): ValueOf[RT] = recType := recEntry

    /* Same as apply, but you can pass fields in any order */
    def apply[Vs <: AnyTypeSet](values: Vs)(implicit
        reorder: Vs ReorderTo RT#Raw
      ): ValueOf[RT] = recType := reorder(values)
  }

  implicit def getRecordEntryOps[RT <: AnyRecordType](entry: ValueOf[RT]): RecordEntryOps[RT] =
    RecordEntryOps(entry.value)
  case class RecordEntryOps[RT <: AnyRecordType](val entryRaw: RT#Raw) extends AnyVal {

    def get[P <: AnyProperty](p: P)
      (implicit get: RT Get P): ValueOf[P] = p := get(entryRaw)

    def update[P <: AnyProperty](field: ValueOf[P])
      (implicit check: (ValueOf[P] :~: ∅) ⊂ RT#Raw,
                update: RT Update (ValueOf[P] :~: ∅)
      ): ValueOf[RT] = update(entryRaw, field :~: ∅)

    def update[Ps <: AnyTypeSet](fields: Ps)
      (implicit update: RT Update Ps): ValueOf[RT] = update(entryRaw, fields)

    def as[Other <: AnyRecordType, Rest <: AnyTypeSet](other: Other, rest: Rest)
      (implicit transform: Transform[RT, Other, Rest]): ValueOf[Other] = transform(entryRaw, other, rest)

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

  object Transform {

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
