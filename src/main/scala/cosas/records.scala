package ohnosequences.cosas

case object records {

  import types._, typeSets._, properties._
  import ops.typeSets.{ReorderTo, CheckForAll}
  import ops.typeSets.{SerializeDenotations, SerializeDenotationsError}
  import ops.typeSets.{ParseDenotations, ParseDenotationsError}

  /*
    ## Records

    Records are `AnyType`s wrapping a `PropertySet` from which they take their `Raw` type: entries for that set of properties.
  */
  trait AnyRecord extends AnyType {

    type PropertySet <: AnyPropertySet
    val  propertySet: PropertySet

    type Properties = PropertySet#Properties
    val  properties: Properties = propertySet.properties

    type Raw = PropertySet#Raw

    type Size = Properties#Size
  }

  class Record[PS <: AnyPropertySet](val propertySet: PS) extends AnyRecord {

    type PropertySet = PS

    lazy val label = toString
  }

  case object AnyRecord {

    type withProperties[PS <: AnyPropertySet] = AnyRecord { type Properties = PS }
  }


  /*
    ### Record ops

    An `apply` method for building denotations of this record type, overloaded so that the properties can be provided in any order.
  */
  import ops.records._

  implicit def getRecordOps[RT <: AnyRecord](recType: RT): RecordOps[RT] =
    RecordOps(recType)
  case class RecordOps[RT <: AnyRecord](val recType: RT) extends AnyVal {

    def apply(recEntry: RT#Raw): ValueOf[RT] = recType := recEntry

    /* Same as apply, but you can pass properties in any order */
    def apply[Vs <: AnyTypeSet](values: Vs)(implicit
        reorder: Vs ReorderTo RT#Raw
      ): ValueOf[RT] = recType := reorder(values)

    def parse[V0](map: Map[String,V0])(implicit parse: ParseDenotations[RT#Raw, V0]): Either[ParseDenotationsError, ValueOf[RT]] =
      parse(map).fold[Either[ParseDenotationsError, ValueOf[RT]]](
        l => Left(l),
        v => Right(new ValueOf[RT](v))
      )
  }

  /*
    ### Record entry ops

    Operations on `ValueOf`s a record type. As usual with value classes, parameter is of the wrapped type, with the implicits providing them only for value class instances.
  */
  implicit def getRecordEntryOps[RT <: AnyRecord](entry: ValueOf[RT]): RecordEntryOps[RT] =
    RecordEntryOps(entry.value)
  case class RecordEntryOps[RT <: AnyRecord](val entryRaw: RT#Raw) extends AnyVal {

    def serialize[V](implicit
      serialize: RT#PropertySet#Raw SerializeDenotations V
    ): Either[SerializeDenotationsError, Map[String,V]] = serialize(entryRaw)

    def serializeUsing[V](map: Map[String,V])(implicit
      serialize: RT#PropertySet#Raw SerializeDenotations V
    ): Either[SerializeDenotationsError, Map[String,V]] = serialize(entryRaw, map)

    def getV[P <: AnyProperty](p: P)(implicit get: RT Get P): P#Raw =
      get(entryRaw)

    def get[P <: AnyProperty](p: P)(implicit
      get: RT Get P
    ): ValueOf[P] = p := get(entryRaw)

    def update[P <: AnyProperty](field: ValueOf[P])(implicit
      check: (ValueOf[P] :~: ∅) ⊂ RT#Raw,
      update: RT Update (ValueOf[P] :~: ∅)
    ): ValueOf[RT] = update(entryRaw, field :~: ∅)

    def update[Ps <: AnyTypeSet](properties: Ps)(implicit
      update: RT Update Ps
    ): ValueOf[RT] = update(entryRaw, properties)

    def as[Other <: AnyRecord, Rest <: AnyTypeSet](other: Other, rest: Rest)(implicit
      transform: Transform[RT, Other, Rest]
    ): ValueOf[Other] = transform(entryRaw, other, rest)

    def as[Other <: AnyRecord { type Raw = RT#Raw }](otherEntry: ValueOf[Other]): ValueOf[RT] =
      new ValueOf[RT](otherEntry.value)
  }
}
