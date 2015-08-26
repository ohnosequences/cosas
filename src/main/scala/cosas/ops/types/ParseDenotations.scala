package ohnosequences.cosas.ops.types

import ohnosequences.cosas._, types._, typeSets._, fns._

trait ParseDenotations[Types <: AnyTypeSet, V]
extends Fn1[Map[String,V]] {

  type Denotations <: AnyTypeSet
  type Out = (Either[AnyDenotationsParsingError, Denotations], Map[String,V])
}

trait AnyDenotationsParsingError
case class KeyNotFound[V](val key: String, val map: Map[String,V]) extends AnyDenotationsParsingError
case class ErrorParsing[PE <: AnyTypeParsingError](val err: PE) extends AnyDenotationsParsingError

case object ParseDenotations {

  // def apply[Types <: AnyTypeSet, V]
  //   (implicit parser: ParsePropertiesFrom[F, V]): ParsePropertiesFrom[F, V] = parser

  implicit def empty[Types <: AnyTypeSet, V]: ParseDenotations[Types,V] =
    new ParseDenotations[Types,V] {

      type Denotations = ∅
      def apply(map: Map[String,V]): Out = (Right(∅), map)
    }

  implicit def cons[
    V0,
    D0 <: H#Raw,
    H <: AnyType, T <: AnyTypeSet,
    PH <: AnyTypeParser { type Type = H; type V = V0; type D = D0 },
    PT <: ParseDenotations[T,V0]
  ](implicit
    parseH: PH,
    parseT: PT
  ): ParseDenotations[(H :~: T), V0] = new ParseDenotations[(H :~: T), V0] {

    type Denotations = (PH#Type := PH#D) :~: PT#Denotations

    def apply(map: Map[String,V0]): Out = parseT(map) match {

      case (Right(td), map) => (map get parseH.labelRep) match {

        case None => (Left(KeyNotFound(parseH.labelRep, map)), map)

        case Some(v) => ( parseH parse (parseH.labelRep, v) ).fold[Out](
          err => ( Left(ErrorParsing(err)), map ),
          { hd: PH#Type := PH#D => (Right( hd :~: (td: PT#Denotations) ), map) }
        )
      }

      case (Left(err), map) => (Left(err), map)
    }
  }
}
