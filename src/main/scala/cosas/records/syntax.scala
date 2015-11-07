package ohnosequences.cosas.records

import ohnosequences.cosas._, types._, fns._, klists._

case object syntax {

  final case class RecordTypeDenotationSyntax[RT <: AnyRecordType, Vs <: RT#Keys#Raw](val vs: Vs) extends AnyVal {

    def get[D <: AnyDenotation { type Tpe = T }, T <: AnyType](tpe: T)(implicit
      get: AnyApp1At[D findIn Vs, Vs] { type Y = D }
    ): D =
      get(vs)

    def altGet[D <: AnyDenotation { type Tpe = T }, T <: AnyType, O <: AnyKList](tpe: T)(implicit
      p: AnyApp1At[PickSubtype[D,AnyDenotation { type Tpe = T }], Vs] { type Y = (D,O) }
    )
    : D =
      p(vs)._1

    def getV[D <: AnyDenotation { type Tpe = T }, T <: AnyType](tpe: T)(implicit
      get: AnyApp1At[D findIn Vs, Vs] { type Y = D }
    ): D#Value =
      get(vs).value

  }

  final case class RecordTypeSyntax[RT <: AnyRecordType](val rt: RT) extends AnyVal {

    def apply[Vs <: AnyKList { type Bound = AnyDenotation }](values: Vs)(implicit
      reorder: AnyApp1At[TakeFirst[RT#Raw], Vs]
    ): RT := RT#Raw = rt := reorder(values)
  }


}

//   /*
//     ### Record entry ops
//
//     Operations on `ValueOf`s a record type. As usual with value classes, parameter is of the wrapped type, with the implicits providing them only for value class instances.
//   */
//   case class RecordEntrySyntax[RT <: AnyRecord](val entryRaw: RT#Raw) extends AnyVal {
//
//     // def serialize[V](implicit
//     //   serialize: RT#SetOfTypes#Raw SerializeDenotations V
//     // ): Either[SerializeDenotationsError, Map[String,V]] = serialize(entryRaw)
//     //
//     // def serializeUsing[V](map: Map[String,V])(implicit
//     //   serialize: RT#SetOfTypes#Raw SerializeDenotations V
//     // ): Either[SerializeDenotationsError, Map[String,V]] = serialize(entryRaw, map)
//     //
//     def getV[P <: AnyProperty](p: P)(implicit
//       get: App1[get[P,RT], ValueOf[RT], ValueOf[P]]
//     )
//     : P#Raw =
//       get(new Denotes[RT#Raw,RT](entryRaw)).value
//
//     def get[P <: AnyProperty](p: P)(implicit
//       get: App1[Get[P,RT], ValueOf[RT], ValueOf[P]]
//     )
//     : ValueOf[P] =
//       get(new Denotes[RT#Raw,RT](entryRaw))
//
//     def update[P <: AnyProperty,V <: P#Raw](field: P := V)(implicit
//       check: (ValueOf[P] :~: ∅[AnyDenotation]) ⊂ RT#Raw,
//       update: App2[Update[RT], ValueOf[RT], (ValueOf[P] :~: ∅[AnyDenotation]), ValueOf[RT]]
//     )
//     : ValueOf[RT] =
//       update(new Denotes[RT#Raw,RT](entryRaw), field :~: ∅)
//
//     def update[Ps <: AnyTypeSet](properties: Ps)(implicit
//       update: App2[Update[RT], ValueOf[RT], Ps, ValueOf[RT]]
//     )
//     : ValueOf[RT] =
//       update(new Denotes[RT#Raw,RT](entryRaw), properties)
//
//     def as[
//       Other <: AnyRecord,
//       Rest <: AnyTypeSet
//     ]
//     (other: Other, rest: Rest)(implicit
//       _transform: App3[transform[RT, Other], ValueOf[RT], Other, Rest, ValueOf[Other]]
//     )
//     : ValueOf[Other] =
//       _transform(new (RT := RT#Raw)(entryRaw), other, rest)
//
//     // def as[Other <: AnyRecord { type Raw = RT#Raw }](otherEntry: ValueOf[Other]): ValueOf[RT] =
//     //   new ValueOf[RT](otherEntry.value)
//   }
// }
