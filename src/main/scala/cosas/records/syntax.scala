package ohnosequences.cosas.records

import ohnosequences.cosas._, types._, typeSets._, properties._
import ohnosequences.cosas.typeSets.ReorderTo

case object syntax {

  /*
    ### Record ops

    An `apply` method for building denotations of this record type, overloaded so that the properties can be provided in any order.
  */
  case class RecordSyntax[RT <: AnyRecord](val recType: RT) extends AnyVal {

    def apply(recEntry: RT#Raw): ValueOf[RT] = recType := recEntry

    /* Same as apply, but you can pass properties in any order */
    def apply[Vs <: AnyTypeSet](values: Vs)(implicit
        reorder: Vs ReorderTo RT#Raw
      ): ValueOf[RT] = recType := reorder(values)

    def parse[
      V0,
      PD <: ParseDenotations[RT#PropertySet#Raw, V0]
    ](map: Map[String,V0])(implicit parse: PD): Either[ParseDenotationsError, ValueOf[RT]] =
      parse(map).fold[Either[ParseDenotationsError, ValueOf[RT]]](
        l => Left(l),
        v => Right(new ValueOf[RT](v))
      )
  }


  /*
    ### Record entry ops

    Operations on `ValueOf`s a record type. As usual with value classes, parameter is of the wrapped type, with the implicits providing them only for value class instances.
  */
  case class RecordEntrySyntax[RT <: AnyRecord](val entryRaw: RT#Raw) extends AnyVal {

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
