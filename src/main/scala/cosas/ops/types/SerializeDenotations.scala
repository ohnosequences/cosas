package ohnosequences.cosas.ops.types

import ohnosequences.cosas._, types._, typeSets._, fns._

// TODO msg not found
trait SerializeDenotations[Denotations <: AnyTypeSet, V]
extends Fn2[Denotations, Map[String,V]] {

  type Out = Either[SerializeDenotationsError, Map[String,V]]
}

// errors should be named with the same name + error
trait SerializeDenotationsError
case class KeyPresent[V](val key: String, val map: Map[String,V]) extends SerializeDenotationsError
case class ErrorSerializing[SE <: DenotationSerializerError](val err: SE) extends SerializeDenotationsError

case object SerializeDenotations {

  implicit def atEmpty[V]: SerializeDenotations[∅,V] = new SerializeDenotations[∅,V] {

    def apply(nil: ∅, map: Map[String,V]): Out = Right(map)
  }

  implicit def atCons[
    V,
    H <: AnyType, TD <: AnyTypeSet,
    HR <: H#Raw,
    SH <: AnyDenotationSerializer { type Type = H; type Value = V; type D = HR },
    ST <: SerializeDenotations[TD,V]
  ](implicit
    serializeH: SH,
    serializeT: ST
  ): SerializeDenotations[(H := HR) :~: TD, V] = new SerializeDenotations[(H := HR) :~: TD, V] {

    def apply(denotations: (H := HR) :~: TD, map: Map[String,V]): Out = {

      serializeH(denotations.head).fold(
        l => Left(ErrorSerializing(l)),
        kv => (map get kv._1) match {
          case Some(_)  => Left(KeyPresent(kv._1, map))
          case None     => serializeT(denotations.tail, map + kv)
        }
      )
    }
  }
}
