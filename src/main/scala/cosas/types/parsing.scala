package ohnosequences.cosas.types

// TODO update to DepFns
trait AnyDenotationParser {

  type Type <: AnyType
  val tpe: Type

  // the type used to denote Type
  type D <: Type#Raw

  type Value
  type From = (String, Value)

  val parser: Value => Option[D]

  val labelRep: String

  def apply(k: String, v: Value): Either[DenotationParserError, Type := D] = k match {

    case `labelRep` => parser(v).fold[Either[DenotationParserError, Type := D]](
        Left(ErrorParsingValue(tpe)(v))
      )(
        d => Right(tpe := d)
      )

    case _ => Left(WrongKey(tpe, k, labelRep))
  }
}

sealed trait DenotationParserError
case class ErrorParsingValue[Tpe <: AnyType, Value](val tpe: Tpe)(val from: Value)
extends DenotationParserError
case class WrongKey[Tpe <: AnyType](val tpe: Tpe, val got: String, val expected: String)
extends DenotationParserError

class DenotationParser[T <: AnyType, D0 <: T#Raw, V](
  val tpe: T,
  val labelRep: String
)(
  val parser: V => Option[D0]
)
extends AnyDenotationParser {

  type Type = T
  type Value = V
  type D = D0
}

case object AnyDenotationParser {

  implicit def genericParser[T <: AnyType, D <: T#Raw](implicit tpe: T): DenotationParser[T,D,D] =
    new DenotationParser(tpe, tpe.label)(d => Some(d))
}
