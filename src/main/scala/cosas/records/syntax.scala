package ohnosequences.cosas.records

import ohnosequences.cosas._, fns._, types._, typeSets._, properties._

case object syntax {

  /*
    ### Record ops

    An `apply` method for building denotations of this record type, overloaded so that the properties can be provided in any order.
  */
  case class RecordSyntax[RT <: AnyRecord](val recType: RT) extends AnyVal {

    def apply(recEntry: RT#Raw): ValueOf[RT] = recType := recEntry

    /* Same as apply, but you can pass properties in any order */
    def apply[Vs <: AnyTypeSet](values: Vs)(implicit
      reorder: App1[reorderTo[RT#Raw], Vs, RT#Raw]
    ): ValueOf[RT] = recType := reorder(values)

    // def parse[
    //   V0,
    //   PD <: ParseDenotations[RT#PropertySet#Raw, V0]
    // ](map: Map[String,V0])(implicit parse: PD): Either[ParseDenotationsError, ValueOf[RT]] =
    //   parse(map).fold[Either[ParseDenotationsError, ValueOf[RT]]](
    //     l => Left(l),
    //     v => Right(new ValueOf[RT](v))
    //   )
  }


  /*
    ### Record entry ops

    Operations on `ValueOf`s a record type. As usual with value classes, parameter is of the wrapped type, with the implicits providing them only for value class instances.
  */
  case class RecordEntrySyntax[RT <: AnyRecord](val entryRaw: RT#Raw) extends AnyVal {

    // def serialize[V](implicit
    //   serialize: RT#PropertySet#Raw SerializeDenotations V
    // ): Either[SerializeDenotationsError, Map[String,V]] = serialize(entryRaw)
    //
    // def serializeUsing[V](map: Map[String,V])(implicit
    //   serialize: RT#PropertySet#Raw SerializeDenotations V
    // ): Either[SerializeDenotationsError, Map[String,V]] = serialize(entryRaw, map)
    //
    def getV[P <: AnyProperty](p: P)(implicit
      get: App1[get[P,RT], ValueOf[RT], ValueOf[P]]
    )
    : P#Raw =
      get(new Denotes[RT#Raw,RT](entryRaw)).value


    def get[P <: AnyProperty](p: P)(implicit
      get: App1[Get[P,RT], ValueOf[RT], ValueOf[P]]
    )
    : ValueOf[P] =
      get(new Denotes[RT#Raw,RT](entryRaw))

    def update[P <: AnyProperty](field: ValueOf[P])(implicit
      check: (ValueOf[P] :~: ∅) ⊂ RT#Raw,
      update: App2[Update[RT], ValueOf[RT], (ValueOf[P] :~: ∅), ValueOf[RT]]
    )
    : ValueOf[RT] =
      update(new Denotes[RT#Raw,RT](entryRaw), field :~: ∅)

    def update[Ps <: AnyTypeSet](properties: Ps)(implicit
      update: App2[Update[RT], ValueOf[RT], Ps, ValueOf[RT]]
    )
    : ValueOf[RT] =
      update(new Denotes[RT#Raw,RT](entryRaw), properties)
    //
    // def as[Other <: AnyRecord, Rest <: AnyTypeSet](other: Other, rest: Rest)(implicit
    //   transform: Transform[RT, Other, Rest]
    // ): ValueOf[Other] = transform(entryRaw, other, rest)
    //
    // def as[Other <: AnyRecord { type Raw = RT#Raw }](otherEntry: ValueOf[Other]): ValueOf[RT] =
    //   new ValueOf[RT](otherEntry.value)
  }
}
