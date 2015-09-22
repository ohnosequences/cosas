package ohnosequences.cosas.types

trait AnyDenotationSerializer {

  type Type <: AnyType
  val tpe: Type

  // the type used to denote Type
  type D <: Type#Raw

  type Value
  type To = (String, Value)

  val serializer: D => Option[Value]

  val labelRep: String

  def apply(d: Type := D): Either[DenotationSerializerError, To] = serializer(d.value)
    .fold[Either[DenotationSerializerError, To]](
      Left(ErrorSerializingValue(d))
    )(
      v => Right(labelRep -> v)
    )
}

sealed trait DenotationSerializerError
case class ErrorSerializingValue[T <: AnyType, D <: T#Raw](d: T := D) extends DenotationSerializerError

class DenotationSerializer[T <: AnyType, D0 <: T#Raw, V](
  val tpe: T,
  val labelRep: String
)(
  val serializer: D0 => Option[V]
)
extends AnyDenotationSerializer {

  type Type = T
  type D = D0
  type Value = V
}

case object AnyDenotationSerializer {

  implicit def genericSerializer[T <: AnyType, D <: T#Raw](implicit tpe: T): DenotationSerializer[T,D,D] =
    new DenotationSerializer(tpe, tpe.label)(d => Some(d))
}
