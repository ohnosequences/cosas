package ohnosequences.cosas.types

import ohnosequences.cosas.fns._

trait AnyDenotation extends Any {

  type Tpe <: AnyType

  type Value
  def  value: Value
}

case object denotationValue extends DepFn1[AnyDenotation,Any] {

  implicit def value2[T <: AnyType, V <: T#Raw]: App1[denotationValue.type, V Denotes T, V] =
    denotationValue at { v: V Denotes T => v.value }

  implicit def value[D <: AnyDenotation]: App1[denotationValue.type, D, D#Value] =
    denotationValue at { v: V => v.value }
}

/* Bound the denoted type */
trait AnyDenotationOf[T <: AnyType] extends Any with AnyDenotation { type Tpe = T }

// TODO: who knows what's going on here wrt specialization (http://axel22.github.io/2013/11/03/specialization-quirks.html)
trait AnyDenotes[@specialized V, T <: AnyType] extends Any with AnyDenotationOf[T] {

  final type Value = V
}

/* Denote T with a `value: V`. Normally you write it as `V Denotes T` thus the name. */
// NOTE: most likely V won't be specialized here
final class Denotes[V, T <: AnyType](val value: V) extends AnyVal with AnyDenotes[V, T] {

  final def show(implicit t: T): String = s"(${t.label} := ${value})"
}
