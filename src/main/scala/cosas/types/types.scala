package ohnosequences.cosas.types

import fns._

trait AnyType {

  type Raw
  val label: String

  final type Me = this.type
  implicit final val justMe: Me = this
}

case object AnyType {

  type withRaw[V] = AnyType { type Raw = V }

  implicit def typeOps[T <: AnyType](tpe: T): TypeOps[T] = TypeOps(tpe)
}

// class Type(val label: String) extends AnyType { type Raw = Any }

final case class TypeOps[T <: AnyType](val tpe: T) extends AnyVal {

  /* For example `user denoteWith (String, String, Int)` _not that this is a good idea_ */
  final def =:[@specialized V <: T#Raw](v: V): V =: T = new (V Denotes T)(v)
  final def :=[@specialized V <: T#Raw](v: V): T := V = new (V Denotes T)(v)
}

case object typeLabel extends DepFn1[AnyType, String] {

  implicit def atType[T <: AnyType]: App1[typeLabel.type,T,String] = typeLabel at { t: T => t.label }
}
