package ohnosequences.cosas

case object records {

  import types._, typeSets._, properties._
  import ops.typeSets.{ReorderTo, CheckForAll}

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

    implicit def getRecordOps[RT <: AnyRecord](recType: RT): RecordOps[RT] =
      RecordOps(recType)

    implicit def getRecordEntryOps[RT <: AnyRecord](entry: ValueOf[RT]): RecordEntryOps[RT] =
      RecordEntryOps(entry.value)
  }


  /*
    ### Record ops

    An `apply` method for building denotations of this record type, overloaded so that the properties can be provided in any order.
  */
  import ops.records._

  case class RecordOps[RT <: AnyRecord](val recType: RT) extends AnyVal {

    def apply(recEntry: RT#Raw): ValueOf[RT] = recType := recEntry

    /* Same as apply, but you can pass properties in any order */
    def apply[Vs <: AnyTypeSet](values: Vs)(implicit
        reorder: Vs ReorderTo RT#Raw
      ): ValueOf[RT] = recType := reorder(values)

    def parseFrom[V](map: Map[String,V])(implicit
      parse: RT#PropertySet ParsePropertiesFrom V
    ): Either[AnyPropertyParsingError, ValueOf[RT]] =
      parse(map) match {

        case Left(err)  => Left(err)
        case Right(v)   => Right(new ValueOf[RT](v))
      }
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
