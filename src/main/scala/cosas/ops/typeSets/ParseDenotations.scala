package ohnosequences.cosas.ops.typeSets

import ohnosequences.cosas._, types._, typeSets._, fns._

@annotation.implicitNotFound(msg = """
  Cannot parse typeset of denotations
    ${Denotations}
  from a map of type
    Map[String, ${V}]

  Probably some denotation parsers are missing.
""")
trait ParseDenotations[Denotations <: AnyTypeSet, V]
extends Fn1[Map[String,V]] with
        Out[Either[ParseDenotationsError, Denotations]]

trait ParseDenotationsError
case class KeyNotFound[V](val key: String, val map: Map[String,V]) extends ParseDenotationsError
case class ErrorParsing[PE <: DenotationParserError](val err: PE) extends ParseDenotationsError

case object ParseDenotations {

  def apply[Ds <: AnyTypeSet,V](implicit p: ParseDenotations[Ds,V]): ParseDenotations[Ds,V] = p
  implicit def atEmpty[V]: ParseDenotations[∅,V] = new ParseDenotations[∅,V] {

    def apply(map: Map[String,V]): Out = Right(∅)
  }

  implicit def atCons[
    HType <: AnyType, TailDenotations <: AnyTypeSet,
    HRaw <: HType#Raw,
    Vv
  ](implicit
    parseH: DenotationParser[HType,HRaw,Vv],
    parseT: ParseDenotations[TailDenotations,Vv]
  ): ParseDenotations[(HType := HRaw) :~: TailDenotations, Vv] = new ParseDenotations[(HType := HRaw) :~: TailDenotations, Vv] {

    def apply(map: Map[String,Vv]): Out =
      map.get(parseH.labelRep).fold[Out](
        Left(KeyNotFound(parseH.labelRep, map))
      )(
        v => parseH(parseH.labelRep, v) fold (

          l => Left(ErrorParsing(l)),

          r => parseT(map).fold[Out] (
            err => Left(err),
            td  => Right(r :~: (td: TailDenotations))
          )
        )
      )
  }
}
