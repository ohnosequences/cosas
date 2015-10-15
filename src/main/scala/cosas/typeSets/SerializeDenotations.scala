package ohnosequences.cosas.typeSets

import ohnosequences.cosas._, types._, typeSets._, fns._

// errors should be named with the same name + error
trait SerializeDenotationsError
case class KeyPresent[V](val key: String, val map: Map[String,V]) extends SerializeDenotationsError
case class ErrorSerializing[SE <: DenotationSerializerError](val err: SE) extends SerializeDenotationsError

class SerializeDenotations[V,Denotations <: AnyTypeSet] extends DepFn2[
  Map[String,V], Denotations,
  Either[SerializeDenotationsError, Map[String,V]]
]

case object SerializeDenotations {

  implicit def atEmpty[V]
  : App2[SerializeDenotations[V,∅], Map[String,V], ∅, Either[SerializeDenotationsError, Map[String,V]]] =
    App2 { (map: Map[String,V],nil: ∅) => Right(map): Either[SerializeDenotationsError, Map[String,V]] }

  implicit def atCons[
    V,
    H <: AnyType, TD <: AnyTypeSet,
    HR <: H#Raw
  ](implicit
    serializeH: DenotationSerializer[H,HR,V],
    serializeT: App2[SerializeDenotations[V,TD], Map[String,V], TD, Either[SerializeDenotationsError, Map[String,V]]]
  )
  : App2[
      SerializeDenotations[V,(H := HR) :~: TD],
      Map[String,V], (H := HR) :~: TD, Either[SerializeDenotationsError, Map[String,V]]
    ] =
  App2 { (map: Map[String,V], denotations: (H := HR) :~: TD) => serializeH(denotations.head).fold(
      l => Left(ErrorSerializing(l)),
      kv => (map get kv._1) match {
        case Some(_)  => Left(KeyPresent(kv._1, map))
        case None     => serializeT(map + kv, denotations.tail)
      }
    )
  }

  //
  //
  //   def apply(denotations: (H := HR) :~: TD, map: Map[String,V]): Out = {
  //
  //     serializeH(denotations.head).fold(
  //       l => Left(ErrorSerializing(l)),
  //       kv => (map get kv._1) match {
  //         case Some(_)  => Left(KeyPresent(kv._1, map))
  //         case None     => serializeT(denotations.tail, map + kv)
  //       }
  //     )
  //   }
  // }
}
