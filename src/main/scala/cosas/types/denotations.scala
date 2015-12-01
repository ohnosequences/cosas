package ohnosequences.cosas.types

import ohnosequences.cosas._, fns._

trait AnyDenotation extends Any {

  type Tpe <: AnyType
  def tpe: Tpe

  type Value <: Tpe#Raw
  def  value: Value

  override def toString: String = s"(${tpe.label} := ${value.toString})"
}

case object AnyDenotation {

  type Of[T <: AnyType] = AnyDenotation { type Tpe = T }

  implicit def denotationSyntax[D <: AnyDenotation](d: D):
    syntax.DenotationSyntax[D] =
    syntax.DenotationSyntax[D](d)
}

/* Denote `tpe: T` with a `value: V` */
final case class Denotation[T <: AnyType, +V <: T#Raw](val tpe: T, val value: V) extends AnyDenotation {

  final type Tpe = T
  final type Value = V @uv
}


case object denotationValue extends DepFn1[AnyDenotation,Any] {

  implicit def value[D <: AnyDenotation]: AnyApp1At[denotationValue.type, D] { type Y = D#Value } =
    denotationValue at { d: D => d.value }
}

case object typeOf extends DepFn1[AnyDenotation,AnyType] {

  implicit def default[D <: AnyDenotation]: AnyApp1At[typeOf.type, D] { type Y = D#Tpe } =
    typeOf at { d: D => d.tpe }
}
