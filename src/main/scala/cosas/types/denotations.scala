package ohnosequences.cosas.types

import ohnosequences.cosas.fns._
import annotation.unchecked.{ uncheckedVariance => uv }

trait AnyDenotation extends Any {

  type Tpe <: AnyType

  type Value <: Tpe#Raw
  def  value: Value
}

/* Bound the denoted type */
trait AnyDenotationOf[T <: AnyType] extends Any with AnyDenotation { type Tpe = T }

// TODO: who knows what's going on here wrt specialization (http://axel22.github.io/2013/11/03/specialization-quirks.html)
trait AnyDenotes[@specialized +V <: T#Raw, T <: AnyType] extends Any with AnyDenotationOf[T] {

  final type Value = V @uv
}

/* Denote T with a `value: V`. Normally you write it as `V Denotes T` thus the name. */
// NOTE: most likely V won't be specialized here
final class Denotes[+V <: T#Raw, T <: AnyType](val value: V) extends AnyVal with AnyDenotes[V, T] {

  final def show(implicit t: T): String = s"(${t.label} := ${value})"
}

case object denotationValue extends DepFn1[AnyDenotation,Any] with denotationValue_2 {

  implicit def value[D <: AnyDenotation]: App1[denotationValue.type, D, D#Value] =
    denotationValue at { d: D => d.value }
}

trait denotationValue_2 {

  implicit def valueForValueOf[T <: AnyType { type Raw <: V}, V]: App1[denotationValue.type, ValueOf[T], V] =
    App1 { (v: ValueOf[T]) =>  v.value }
}

case object typeOf extends DepFn1[AnyDenotation,AnyType] {

  implicit def default[D <: AnyDenotation](implicit tpe: D#Tpe): App1[typeOf.type, D, D#Tpe] =
    typeOf at { d: D => tpe }
}
