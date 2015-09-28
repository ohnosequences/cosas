package ohnosequences.cosas.ops.typeSets

import ohnosequences.cosas._, types._, typeSets._, fns._

@annotation.implicitNotFound(msg = """
  Cannot serialize typeset of denotations
    ${Denotations}
  to a map of type
    Map[String, ${V}]

  Probably some denotation serializers are missing.
""")
trait SerializeDenotations[Denotations <: AnyTypeSet, V]
extends Fn2[Denotations, Map[String,V]] with
        Out[Either[SerializeDenotationsError, Map[String,V]]] {

  final def apply(d: Denotations): Out = apply(d, Map[String,V]())
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
    HR <: H#Raw
  ](implicit
    serializeH: DenotationSerializer[H,HR,V],
    serializeT: SerializeDenotations[TD,V]
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
