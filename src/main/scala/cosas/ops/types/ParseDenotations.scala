package ohnosequences.cosas.ops.types

import ohnosequences.cosas._, types._, typeSets._, fns._

trait ParseDenotations[Denotations <: AnyTypeSet, V]
extends Fn1[Map[String,V]] with
        Out[(Either[ParseDenotationsError, Denotations], Map[String,V])]

trait ParseDenotationsError
case class KeyNotFound[V](val key: String, val map: Map[String,V]) extends ParseDenotationsError
case class ErrorParsing[PE <: DenotationParserError](val err: PE) extends ParseDenotationsError

case object ParseDenotations {

  implicit def atEmpty[V]: ParseDenotations[∅,V] = new ParseDenotations[∅,V] {

    def apply(map: Map[String,V]): Out = (Right(∅), map)
  }

  implicit def atCons[
    V,
    H <: AnyType, TD <: AnyTypeSet,
    HR <: H#Raw,
    PH <: AnyDenotationParser { type Type = H; type Value = V; type D = HR },
    PT <: ParseDenotations[TD,V]
  ](implicit
    parseH: PH,
    parseT: PT
  ): ParseDenotations[(H := HR) :~: TD, V] = new ParseDenotations[(H := HR) :~: TD, V] {

    def apply(map: Map[String,V]): Out =
      map.get(parseH.labelRep).fold[Out](
        ( Left(KeyNotFound(parseH.labelRep, map)), map )
      )(
        v => parseH(parseH.labelRep, v) fold (

          l => ( Left(ErrorParsing(l)), map ),

          r => parseT(map) match {

            case (Right(td), map) => ( Right(r :~: (td: TD)), map )

            case (Left(err), map) => ( Left(err), map )
          }
        )
      )
  }
}
