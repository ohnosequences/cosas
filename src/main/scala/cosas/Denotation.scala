package ohnosequences.cosas

/* Something super-generic and ultra-abstract */
trait AnyType {

  val label: String

  type Me = this.type

  implicit def meFrom[D <: AnyDenotationOf[Me]](v: D): Me = this
}

object AnyType {

  type :%:[V, T <: AnyType] = Denotes[V,T]
  
  implicit def denotationOps[T <: AnyType](tpe: T): DenotationOps[T] = DenotationOps(tpe)
}

class Type(val label: String) extends AnyType

// TODO who knows what's going on here wrt specialization
// http://axel22.github.io/2013/11/03/specialization-quirks.html
/*
You denote a `Type` using a `Value`
*/
sealed trait AnyDenotation extends Any {

  type Tpe <: AnyType

  type Value
  val  value: Value
}
/*
Bound the denoted type
*/
trait AnyDenotationOf[T <: AnyType] extends Any with AnyDenotation { type Tpe = T }

trait AnyDenotes[@specialized V, T <: AnyType] extends Any with AnyDenotationOf[T] {
  
  type Value = V
}
/*
Denote T with a `value: V`. Normally you write it as `V Denotes T` thus the name.
*/
// most likely V won't be specialized here
final class Denotes[V, T <: AnyType](val value: V) extends AnyVal with AnyDenotes[V, T] {}

final case class DenotationOps[T <: AnyType](val tpe: T) extends AnyVal {

  /*
  For example `user denoteWith (String, String, Int)` _not that this is a good idea_
  */
  final def denoteWith[@specialized V](v: V): (V Denotes T) = new Denotes(v)
  /*
  Alternative syntax, suggesting something like type ascription: `"12d655xr9" :%: id`.
  */
  final def :%:[@specialized V](v: V): (V Denotes T) = new Denotes(v)
}

object Denotes {

  type :%:[V, T <: AnyType] = Denotes[V,T]
  
  implicit def denotationOps[T <: AnyType](tpe: T): DenotationOps[T] = DenotationOps(tpe)
  
  implicit def eqForDenotes[V, T <: AnyType]: scalaz.Equal[V Denotes T] = new scalaz.Equal[V Denotes T] {

    def equal(a1: V Denotes T, a2: V Denotes T): Boolean = a1.value == a2.value
  }
  
}

/* 
### Wrapping types 

Sometimes you want to restrict the `Value`s that can be used to denote a type; that's the purpose of `AnyWrap`
*/
trait AnyWrap extends AnyType {

  type Raw
}

trait Wrap[R] extends AnyWrap { type Raw = R }

object AnyWrap {

  type withRaw[R] = AnyWrap { type Raw = R }
  type RawOf[W <: AnyWrap] = W#Raw

  def valueOf[W <: AnyWrap](r: W#Raw): ValueOf[W] = new ValueOf[W](r)

  implicit def typeOps[W <: AnyWrap](t: W): WrapOps[W] = new WrapOps[W](t)
}

import AnyWrap._

class WrapOps[W <: AnyWrap](val t: W) {

  def apply(raw: W#Raw): ValueOf[W] = new ValueOf[W](raw)
  def withValue(raw: W#Raw): ValueOf[W] = new ValueOf[W](raw)
}


/* ### Values of wrapped types */
sealed trait AnyWrappedValue extends Any with AnyDenotation  {

  type Tpe <: AnyWrap
  type Value <: Tpe#Raw
}

trait WrappedValue[@specialized V <: W#Raw, W <: AnyWrap] extends Any with AnyWrappedValue {

  type Tpe = W
  type Value = V
}

final class ValueOf[W <: AnyWrap](val value: W#Raw) extends AnyVal with WrappedValue[W#Raw,W] {}

trait AnySubsetType extends AnyWrap {

  type W <: AnyWrap
  type Raw = W#Raw
  def predicate(raw: W#Raw): Boolean
}

trait SubsetType[W0 <: AnyWrap] extends AnySubsetType { type W = W0 }

object AnySubsetType {

    class SubSetTypeOps[W <: AnyWrap, ST <: SubsetType[W]](val st: ST) {

      // TODO why this dot?
      def apply(raw: ST#W#Raw): Option[ValueOf[ST]] = {

        if ( st.predicate(raw) ) None else Some( new ValueOf[ST](raw) )
      }
      def withValue(raw: ST#Raw): Option[ValueOf[ST]] = apply(raw)
  }

  object ValueOfSubsetTypeOps {

    implicit def ValueOfSubsetTypeOps[
      W <: AnyWrap,
      ST <: SubsetType[W],
      Ops <: ValueOfSubsetTypeOps[W,ST]
    ](v: ValueOf[ST])(implicit conv: ValueOf[ST] => Ops): Ops = conv(v)

  }
  /*
  you should implement this trait for providing ops for values of a subset type `ST`.
  */
  trait ValueOfSubsetTypeOps[W <: AnyWrap, ST <: SubsetType[W]] extends Any {

    /*
    use case: concat of sized has the sum of the two arg sizes; but how do you create the corresponding value saving a stupid check (and returning an Option)? `unsafeValueOf`. By implementing this trait you assume the responsibility that comes with being able to create unchecked values of `ST`; use it with caution!
    */
    protected def unsafeValueOf[ST0 <: ST](other: ST#Raw): ValueOf[ST] = new ValueOf[ST](other)
  }
}