package ohnosequences.cosas

object types {

  /* 
  ### Types

  This trait is used to represent a type system embedded in Scala, à la universes in dependently typed programming. 
  */
  trait AnyType { me =>

    /* The type required to denote this type. Note that this is used as a _bound_ on `ValueOf` */
    type Raw
    /* a label which can help at the runtime level to disambiguate between values of the same type denoting different `AnyType`s. A good default is to use the FQN of the corresponding `AnyType`. */
    val label: String

    final type Me = me.type
    /* This lets you get _at compile time_ the value representing this `AnyType` from its denotation. */
    implicit final val justMe: Me = me
  }

  object AnyType {

    type withRaw[V] = AnyType { type Raw = V }
    
    implicit def typeOps[T <: AnyType](tpe: T): TypeOps[T] = TypeOps(tpe)
  }

  class Type(val label: String) extends AnyType { type Raw = Any }
  trait AnyWrap extends AnyType
  class Wrap[R](val label: String) extends AnyWrap { final type Raw = R }
  object Wrap {

    implicit def wrapOps[W <: AnyWrap](tpe: W): WrapOps[W] = WrapOps(tpe)
    implicit def asRaw[T <: AnyType](d: ValueOf[T]): T#Raw = d.value
  }

  /*
  ### Denotations

  The value class `V Denotes T`, which wraps a value of type `V` is used to denote `T` with `V`. There are different aliases for this to suit your preferences, with `ValueOf` being used for `AnyType`s which declare a bound (through its `Raw` type member) on which types can be used to denote them.
  */

  type =:[V, T <: AnyType] = Denotes[V,T]
  type :=[T <: AnyType, V] = Denotes[V,T]

  type ValueOf[T <: AnyType] = T#Raw Denotes T
  def  valueOf[T <: AnyType, V <: T#Raw](t: T)(v: V): ValueOf[T] = t := v
  def  valueOf[T <: AnyType](v: T#Raw): ValueOf[T] = new (T#Raw Denotes T)(v)

  trait AnyDenotation extends Any {

    /* the type being denoted */
    type Tpe <: AnyType

    type Value
    def  value: Value
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

    /*
    Here `t: T` will be resolved through the `implicit val` of type `T#Me` inside `T`
    */
    final def show(implicit t: T): String = s"(${t.label} := ${value})"
  }

  /*
  This value class provides denotation facilities to a type.
  */
  final case class TypeOps[T <: AnyType](val tpe: T) extends AnyVal {

    /* For example `user denoteWith (String, String, Int)` _not that this is a good idea_ */
    final def =:[@specialized V](v: V): V =: T = new (V Denotes T)(v)
    final def :=[@specialized V](v: V): T := V = new (V Denotes T)(v)
  }

  final case class WrapOps[T <: AnyWrap](val tpe: T) extends AnyVal {

    final def =:[@specialized V <: T#Raw](v: V): ValueOf[T] = new ValueOf[T](v)
    final def :=[@specialized V <: T#Raw](v: V): ValueOf[T] = new ValueOf[T](v)
  }

}
