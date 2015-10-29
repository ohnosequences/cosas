package ohnosequences.cosas.records

import ohnosequences.cosas._, fns._, types._, typeSets._, properties._

case object syntax {

  case class RecordSyntax[RT <: AnyRecord](val recType: RT) extends AnyVal {

    def apply(recEntry: RT#Raw): ValueOf[RT] = recType := recEntry

    /* Same as apply, but you can pass properties in any order */
    def apply[Vs <: AnyTypeSet](values: Vs)(implicit
      reorder: App1[reorderTo[RT#Raw], Vs, RT#Raw]
    ): ValueOf[RT] = recType := reorder(values)

    def parse[V0](map: Map[String,V0])(
      implicit parseRaw: App1[parseDenotations[V0,RT#Raw], Map[String,V0], Either[ParseDenotationsError,RT#Raw]]
    )
    : Either[ParseDenotationsError, ValueOf[RT]] =
      parseRaw(map).fold[Either[ParseDenotationsError, ValueOf[RT]]](
        l => Left(l),
        v => Right(new ValueOf[RT](v))
      )
  }


  /*
    ### Record entry ops

    Operations on `ValueOf`s a record type. As usual with value classes, parameter is of the wrapped type, with the implicits providing them only for value class instances.
  */
  case class RecordEntrySyntax[RT <: AnyRecord](val entryRaw: RT#Raw) extends AnyVal {

    // def serialize[V](implicit
    //   serialize: RT#SetOfTypes#Raw SerializeDenotations V
    // ): Either[SerializeDenotationsError, Map[String,V]] = serialize(entryRaw)
    //
    // def serializeUsing[V](map: Map[String,V])(implicit
    //   serialize: RT#SetOfTypes#Raw SerializeDenotations V
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

    def update[P <: AnyProperty,V <: P#Raw](field: P := V)(implicit
      check: (ValueOf[P] :~: ∅[AnyDenotation]) ⊂ RT#Raw,
      update: App2[Update[RT], ValueOf[RT], (ValueOf[P] :~: ∅[AnyDenotation]), ValueOf[RT]]
    )
    : ValueOf[RT] =
      update(new Denotes[RT#Raw,RT](entryRaw), field :~: ∅)

    def update[Ps <: AnyTypeSet](properties: Ps)(implicit
      update: App2[Update[RT], ValueOf[RT], Ps, ValueOf[RT]]
    )
    : ValueOf[RT] =
      update(new Denotes[RT#Raw,RT](entryRaw), properties)

    def as[
      Other <: AnyRecord,
      Rest <: AnyTypeSet
    ]
    (other: Other, rest: Rest)(implicit
      _transform: App3[transform[RT, Other], ValueOf[RT], Other, Rest, ValueOf[Other]]
    )
    : ValueOf[Other] =
      _transform(new (RT := RT#Raw)(entryRaw), other, rest)

    // def as[Other <: AnyRecord { type Raw = RT#Raw }](otherEntry: ValueOf[Other]): ValueOf[RT] =
    //   new ValueOf[RT](otherEntry.value)
  }
}
