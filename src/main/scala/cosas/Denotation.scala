package ohnosequences.cosas

/* Something super-generic and ultra-abstract */
trait AnyType {

  val label: String

  type Me = this.type

  implicit def meFrom[D <: AnyDenotationOf[Me]](v: D): Me = this
}

class Type(val label: String) extends AnyType

object AnyType {}


// TODO who knows what's going on here wrt specialization
// http://axel22.github.io/2013/11/03/specialization-quirks.html
sealed trait AnyDenotation extends Any {

  type Tpe <: AnyType

  type Value
  val  value: Value
}

trait AnyDenotationOf[T <: AnyType] extends Any with AnyDenotation { type Tpe = T }

trait AnyDenotes[@specialized V, T <: AnyType] extends Any with AnyDenotationOf[T] {
  
  type Value = V
}
// TODO maybe add a witness req here?
// most likely V won't be specialized here
final class Denotes[V, T <: AnyType](val value: V) extends AnyVal with AnyDenotes[V, T] {

  // NOTE: it may be confusing:
  // override def toString = value.toString
}

class DenotationOps[T <: AnyType](val tpe: T) {

  def denoteWith[@specialized V](v: V): (V Denotes T) = new Denotes(v)
  def :%:[@specialized V](v: V): (V Denotes T) = new Denotes(v)
}

object Denotes {

  // type withRaw[R] = AnyWrap { type Raw = R }
  // type RawOf[W <: AnyWrap] = W#Raw
  

}

/* ### Wrapping types */

trait AnyWrap extends AnyType {

  type Raw
}

trait Wrap[R] extends AnyWrap { type Raw = R }

object AnyWrap {

  type withRaw[R] = AnyWrap { type Raw = R }
  type RawOf[W <: AnyWrap] = W#Raw

  def valueOf[W <: AnyWrap](r: RawOf[W]): ValueOf[W] = new ValueOf[W](r)

  implicit def typeOps[W <: AnyWrap](t: W): WrapOps[W] = new WrapOps[W](t)

  // NOTE: better to do the conversion explicitly
  // implicit def toRaw[W <: AnyWrap](v: ValueOf[W]): RawOf[W] = v.raw
}

import AnyWrap._

class WrapOps[W <: AnyWrap](val t: W) {

  def apply(r: RawOf[W]): ValueOf[W] = new ValueOf[W](r)
  def withValue(r: RawOf[W]): ValueOf[W] = new ValueOf[W](r)
}


/* ### Values of wrapped types */

sealed trait AnyWrappedValue extends Any with AnyDenotation {

  type Tpe <: AnyWrap
  type Value <: Tpe#Raw
  val value: Value
}

object AnyWrappedValue {

  type ofWrap[W <: AnyWrap] = AnyWrappedValue { type Tpe = W }
  type WrapOf[V <: AnyWrappedValue] = V#Tpe
  type RawOf[V <: AnyWrappedValue] = V#Tpe#Raw
}

trait WrappedValue[@specialized V <: W#Raw, W <: AnyWrap] extends Any with AnyWrappedValue {

  type Tpe = W
  type Value = V
}

final class ValueOf[W <: AnyWrap](val value: RawOf[W]) extends AnyVal with WrappedValue[RawOf[W],W] {

  // NOTE: it may be confusing:
  override def toString = value.toString
}

// object ValueOf {

//   implicit def valueOps[W <: AnyWrap](v: ValueOf[W]): ValueOps[W] = new ValueOps[W](v)
// }

// class ValueOps[W <: AnyWrap](v: ValueOf[W]) {
//   // ... //
// }




/*
  This trait represents a mapping between 

  - `Tpe` of a universe of types `TypeBound`
  - `Raw` a type meant to be a denotation of `Tpe` thus the name
*/
// trait AnyDenotation extends AnyWrap {

//   /* The base type for the types that this thing denotes */

//   type Tpe <: AnyType
//   val  tpe: Tpe
// }

// /*
//   Bound the universe of types to be `T`s
// */
// trait AnyDenotationOf[T <: AnyType] extends AnyDenotation { type Tpe <: T }

// object AnyDenotation {

//   type withTpe[T <: AnyType] = AnyDenotation { type Tpe = T }

//   type TpeOf[D <: AnyDenotation] = D#Tpe
// }
