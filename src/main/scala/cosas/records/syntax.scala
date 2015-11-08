package ohnosequences.cosas.records

import ohnosequences.cosas._, types._, fns._, klists._

case object syntax {

  final case class RecordTypeDenotationSyntax[RT <: AnyRecordType, Vs <: RT#Raw](val vs: Vs) extends AnyVal {

    def get[D <: AnyDenotationOf[T], T <: AnyType](tpe: T)(implicit
      p: AnyApp1At[FindS[AnyDenotationOf[T]], Vs] { type Y = D }
    ): D = p(vs)

    def getV[D <: AnyDenotationOf[T], T <: AnyType](tpe: T)(implicit
      p: AnyApp1At[FindS[AnyDenotationOf[T]], Vs] { type Y = D }
    ): D#Value = p(vs).value

    def update[S <: AnyKList.withBound[AnyDenotation]](s: S)(implicit
      repl: AnyApp2At[replace[Vs], Vs, S] { type Y = Vs }
    ):   RT := Vs =
    new (RT := Vs)(repl(vs,s))

    def update[N <: AnyDenotation](n: N)(implicit
      repl: AnyApp2At[replace[Vs], Vs, N :: *[AnyDenotation]] { type Y = Vs }
    ):   RT := Vs =
    new (RT := Vs)(repl(vs, n :: *[AnyDenotation]))

    def as[QT <: AnyRecordType, QTRaw <: QT#Raw](qt: QT)(implicit
      takeFirst: AnyApp1At[TakeFirst[QT#Raw], Vs] { type Y = QTRaw }
    ): QT := QTRaw =
       qt := (takeFirst(vs))
  }

  final case class RecordTypeSyntax[RT <: AnyRecordType](val rt: RT) extends AnyVal {

    def reorder[Vs <: AnyKList.withBound[AnyDenotation], RTRaw <: RT#Raw](values: Vs)(implicit
      takeFirst: AnyApp1At[TakeFirst[Vs], Vs] { type Y = RTRaw }
    ): RT := RTRaw =
       rt := takeFirst(values)
  }

  final case class RecordReorderSyntax[Vs <: AnyKList.withBound[AnyDenotation]](val vs: Vs) extends AnyVal {

    def as[RT <: AnyRecordType, RTRaw <: RT#Raw](rt: RT)(implicit
      takeFirst: AnyApp1At[TakeFirst[RT#Raw], Vs] { type Y = RTRaw }
    ): RT := RTRaw =
       rt := (takeFirst(vs))
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
