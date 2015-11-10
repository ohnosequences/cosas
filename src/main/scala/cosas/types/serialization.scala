package ohnosequences.cosas.types

import ohnosequences.cosas._, fns._, klists._

// TODO move to DepFns
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

// errors should be named with the same name + error
trait SerializeDenotationsError
case class KeyPresent[V](val key: String, val map: Map[String,V]) extends SerializeDenotationsError
case class ErrorSerializing[SE <: DenotationSerializerError](val err: SE) extends SerializeDenotationsError

class SerializeDenotations[V, Denotations <: AnyKList.withBound[AnyDenotation]] extends DepFn2[
  Map[String,V], Denotations,
  Either[SerializeDenotationsError, Map[String,V]]
]

case object SerializeDenotations {

  implicit def atEmpty[V]
  : App2[SerializeDenotations[V,*[AnyDenotation]], Map[String,V], *[AnyDenotation], Either[SerializeDenotationsError, Map[String,V]]] =
    App2 { (map: Map[String,V],nil: *[AnyDenotation]) => Right(map): Either[SerializeDenotationsError, Map[String,V]] }

  implicit def atCons[
    V,
    H <: AnyType, TD <: AnyKList.withBound[AnyDenotation],
    HR <: H#Raw
  ](implicit
    serializeH: DenotationSerializer[H,HR,V],
    serializeT: App2[SerializeDenotations[V,TD], Map[String,V], TD, Either[SerializeDenotationsError, Map[String,V]]]
  )
  : App2[
      SerializeDenotations[V,(H := HR) :: TD],
      Map[String,V], (H := HR) :: TD, Either[SerializeDenotationsError, Map[String,V]]
    ] =
  App2 { (map: Map[String,V], denotations: (H := HR) :: TD) => serializeH(denotations.head).fold(
      l => Left(ErrorSerializing(l)),
      kv => (map get kv._1) match {
        case Some(_)  => Left(KeyPresent(kv._1, map))
        case None     => serializeT(map + kv, denotations.tail)
      }
    )
  }
}
