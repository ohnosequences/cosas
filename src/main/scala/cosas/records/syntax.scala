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
      takeFirst: AnyApp1At[takeFirst[QT#Raw], Vs] { type Y = QTRaw }
    ): QT := QTRaw =
       qt := (takeFirst(vs): QTRaw)

    def toProduct: RT#Keys := Vs = new (RT#Keys := Vs)(vs)

    def serialize[V](implicit
      serialize: AnyApp2At[V SerializeDenotations Vs, Map[String,V], Vs] {
        type Y = Either[SerializeDenotationsError, Map[String,V]]
      }
    ): Either[SerializeDenotationsError, Map[String,V]] = serialize(Map[String,V](), vs)

    def serializeUsing[V](map: Map[String,V])(implicit
      serialize: AnyApp2At[V SerializeDenotations Vs, Map[String,V], Vs] {
        type Y = Either[SerializeDenotationsError, Map[String,V]]
      }
    ): Either[SerializeDenotationsError, Map[String,V]] = serialize(map, vs)
  }

  final case class RecordTypeSyntax[RT <: AnyRecordType](val rt: RT) extends AnyVal {

    def reorder[Vs <: AnyKList.withBound[AnyDenotation], RTRaw <: RT#Raw](values: Vs)(implicit
      takeFirst: AnyApp1At[takeFirst[Vs], Vs] { type Y = RTRaw }
    ): RT := RTRaw =
       rt := (takeFirst(values): RTRaw)

    def from[Vs <: AnyKList.withBound[AnyDenotation], RTRaw <: RT#Raw](vs: Vs)(implicit
      reorder: AnyApp1At[Reorder[RT#Keys, Vs], Vs] { type Y = RTRaw }
    )
    : RT := RTRaw =
      rt := (reorder(vs): RTRaw)

    def parse[RTRaw <: RT#Raw, V](map: Map[String,V])(implicit
      parse: AnyApp1At[V ParseDenotations RT#Keys, Map[String,V]] {
        type Y = Either[ParseDenotationsError, RTRaw]
      }
    ): Either[ParseDenotationsError, RT := RTRaw] =
      parse(map).fold[Either[ParseDenotationsError, RT := RTRaw]](
        { err => Left(err) },
        { vs => Right(rt := vs) }
      )
  }

  final case class RecordReorderSyntax[Vs <: AnyKList.withBound[AnyDenotation]](val vs: Vs) extends AnyVal {

    def as[RT <: AnyRecordType, RTRaw <: RT#Raw](rt: RT)(implicit
      takeFirst: AnyApp1At[Reorder[RT#Keys, Vs], Vs] { type Y = RTRaw }
    ): RT := RTRaw =
       rt := (takeFirst(vs): RTRaw)
  }
}
