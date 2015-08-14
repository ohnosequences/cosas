package ohnosequences.cosas

object records {

  // deps
  import typeSets._, types._, propertyHolders._, properties._

  import ops.typeSets._

  trait AnyRecord extends AnyType with AnyPropertiesHolder {

    val label: String

    /* Record wraps a set of values of it's properties */
    type Raw <: AnyTypeSet
    // should be provided implicitly:
    implicit val valuesOfProperties: Raw areValuesOf Properties
  }

  class Record[Props <: AnyTypeSet.Of[AnyProperty], Vals <: AnyTypeSet]
    (val properties: Props)
    (implicit
      val valuesOfProperties: Vals areValuesOf Props
    ) extends AnyRecord {

    val label = this.toString

    type Properties = Props
    type Raw = Vals
  }

  type size[R <: AnyRecord] = typeSets.size[R#Raw]

  object AnyRecord {

    /* Refiners */
    type withProperties[Ps <: AnyTypeSet.Of[AnyProperty]] = AnyRecord { type Properties = Ps }
    type withRaw[R <: AnyTypeSet] = AnyRecord { type Raw = R }

    implicit def recordOps[R <: AnyRecord](rec: R):
          RecordOps[R] =
      new RecordOps[R](rec)
  }

  // class RecordOps[R <: AnyRecord](val rec: R) extends WrapOps[R](rec) {
  class RecordOps[R <: AnyRecord](val rec: R) extends AnyVal {

    def apply(v: R#Raw): ValueOf[R] = new ValueOf[R](v)

    /* Same as apply, but you can pass fields in any order */
    def apply[Vs <: AnyTypeSet](values: Vs)(implicit
        reorder: Vs ReorderTo R#Raw
      ): ValueOf[R] = rec := reorder(values)

    def parseFrom[X](x: X)(implicit
      parseSet: (R#Properties ParseFrom X) { type Out = R#Raw }
    ): ValueOf[R] = rec := parseSet(rec.properties, x)

    def parseMap[RV](map: Map[String,RV])(implicit
        parseSet: (R#Properties ParseFrom Map[String,RV]) { type Out = R#Raw }
    ): ValueOf[R] = rec := parseSet(rec.properties, map)

  }

  // NOTE: you'll only get record ops _if_ you have a ValueOf[R]. From that point, you don't need the wrapper at all, just use `rec.value`. This lets you make RecordRawOps a value class itself!
  // see https://stackoverflow.com/questions/14861862/how-do-you-enrich-value-classes-without-overhead/
  implicit def recordRawOps[R <: AnyRecord](rec: ValueOf[R]):
        RecordRawOps[R] =
    new RecordRawOps[R](rec.value)

  class RecordRawOps[R <: AnyRecord](val recRaw: R#Raw) extends AnyVal {
    import ops.records._

    def get[P <: AnyProperty](p: P)
      (implicit get: R Get P): ValueOf[P] = get(recRaw)
      // (implicit lookup: R#Raw Lookup ValueOf[P]): ValueOf[P] = lookup(recRaw.raw)


    def update[P <: AnyProperty](propRep: ValueOf[P])
      (implicit check: (ValueOf[P] :~: ∅) ⊂ R#Raw,
                upd: R Update (ValueOf[P] :~: ∅)
      ): ValueOf[R] = upd(recRaw, propRep :~: ∅)

    def update[Ps <: AnyTypeSet](propReps: Ps)
      (implicit upd: R Update Ps): ValueOf[R] = upd(recRaw, propReps)


    def as[Other <: AnyRecord](other: Other)
      (implicit project: Take[R#Raw, Other#Raw]): ValueOf[Other] = other := project(recRaw)

    def as[Other <: AnyRecord, Rest <: AnyTypeSet](other: Other, rest: Rest)
      (implicit transform: Transform[R, Other, Rest]): ValueOf[Other] = transform(recRaw, other, rest)


    def serializeTo[X](implicit serializer: R#Raw SerializeTo X): X = serializer(recRaw)
  }

}
