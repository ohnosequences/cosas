package ohnosequences.cosas.types

import ohnosequences.cosas._, fns._

trait AnyDenotation extends Any {

  type Tpe <: AnyType
  def tpe: Tpe

  type Value <: Tpe#Raw
  def  value: Value
}

case object AnyDenotation {

  implicit def denotationSyntax[T <: AnyType, V <: T#Raw](d: T := V):
    syntax.DenotationSyntax[T, V] =
    syntax.DenotationSyntax[T, V](d.value)
}

/* Bound the denoted type */
trait AnyDenotationOf[T <: AnyType] extends Any with AnyDenotation { type Tpe = T }

// TODO: who knows what's going on here wrt specialization (http://axel22.github.io/2013/11/03/specialization-quirks.html)
trait AnyDenotes[@specialized +V <: T#Raw, T <: AnyType] extends Any with AnyDenotationOf[T] {

  final type Value = V @uv
}

/* Denote T with a `value: V`. Normally you write it as `V Denotes T` thus the name. */
// NOTE: most likely V won't be specialized here
final case class Denotes[+V <: T#Raw, T <: AnyType](val tpe: T, val value: V) extends AnyDenotes[V, T] {

  final def show(implicit t: T): String = s"(${t.label} := ${value})"
}

case object denotationValue extends DepFn1[AnyDenotation,Any] {

  implicit def value[D <: AnyDenotation]: AnyApp1At[denotationValue.type, D] { type Y = D#Value } =
    denotationValue at { d: D => d.value }
}

case object typeOf extends DepFn1[AnyDenotation,AnyType] {

  implicit def default[D <: AnyDenotation]: AnyApp1At[typeOf.type, D] { type Y = D#Tpe } =
    typeOf at { d: D => d.tpe }
}
