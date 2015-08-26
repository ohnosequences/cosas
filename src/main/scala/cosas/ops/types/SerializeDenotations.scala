package ohnosequences.cosas.ops.types

import ohnosequences.cosas._, types._, typeSets._, fns._

trait SerializeDenotations[Denotations <: AnyTypeSet, V]
extends Fn2[Denotations, Map[String,V]] {

  type Out = Either[AnySerializeDenotationsError, Map[String,V]]
}

trait AnySerializeDenotationsError
case class KeyPresent[V](val key: String, val map: Map[String,V]) extends AnySerializeDenotationsError
case class ErrorSerializing[SE <: AnyTypeSerializationError](val err: SE) extends AnySerializeDenotationsError

case object SerializeDenotations {

  implicit def empty[V]: SerializeDenotations[∅,V] = new SerializeDenotations[∅,V] {

    def apply(nil: ∅, map: Map[String,V]): Out = Right(map)
  }

  implicit def cons[
    V,
    H <: AnyType, TD <: AnyTypeSet,
    HR <: H#Raw,
    SH <: AnyDenotationSerializer { type Type = H; type Value = V; type D = HR },
    ST <: SerializeDenotations[TD,V]
  ](implicit
    serializeH: SH,
    serializeT: ST
  ): SerializeDenotations[(H := HR) :~: TD, V] = new SerializeDenotations[(H := HR) :~: TD, V] {

    def apply(denotations: (H := HR) :~: TD, map: Map[String,V]): Out = serializeT(denotations.tail, map).fold(
      l => Left(l),
      tmap => serializeH(denotations.head).fold(
        l => Left(ErrorSerializing(l)),
        kv => (tmap get kv._1) match {
          case Some(_)  => Left(KeyPresent(kv._1, tmap))
          case None     => Right(tmap + kv)
        }
      )
    )
  }
}
