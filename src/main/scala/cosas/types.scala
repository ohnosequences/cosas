package ohnosequences.cosas

object types {

  /* Something super-generic and ultra-abstract */
  trait AnyType {

    type Raw
    val label: String

    final type Me = this.type
    implicit final val justMe: Me = this
  }

  object AnyType {

    type withRaw[V] = AnyType { type Raw = V }

    implicit def typeOps[T <: AnyType](tpe: T): TypeOps[T] = TypeOps(tpe)
  }

  class Type(val label: String) extends AnyType { type Raw = Any }
  class Wrap[R](val label: String) extends AnyType { final type Raw = R }

  type =:[V <: T#Raw, T <: AnyType] = Denotes[V,T]
  type :=[T <: AnyType, V <: T#Raw] = Denotes[V,T]

  type ValueOf[T <: AnyType] = T#Raw Denotes T
  def  valueOf[T <: AnyType, V <: T#Raw](t: T)(v: V): ValueOf[T] = v =: t

  final case class TypeOps[T <: AnyType](val tpe: T) extends AnyVal {

    /* For example `user denoteWith (String, String, Int)` _not that this is a good idea_ */
    final def =:[@specialized V <: T#Raw](v: V): V =: T = new (V Denotes T)(v)
    final def :=[@specialized V <: T#Raw](v: V): V =: T = new (V Denotes T)(v)
  }

  /* You denote a `Type` using a `Value` */
  trait AnyDenotation extends Any {

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
  final class Denotes[V <: T#Raw, T <: AnyType](val value: V) extends AnyVal with AnyDenotes[V, T] {

    final def show(implicit t: T): String = s"(${t.label} := ${value})"
  }


  /*
  ### Subset types

  **Warning** _this has nothing to do with the typeSets in this library!_

  The idea of subset types is that you are specifying a type `S` having as values a _subset_ of those of another `W` type; in this case this is modeled as a predicate valued on `W#Raw`.

  For more about this (and its possible uses) see

  - [Adam Chlipala CPDT - Subset types](http://adam.chlipala.net/cpdt/html/Subset.html)
  */
  trait AnySubsetType extends AnyType {

    type W <: AnyType
    type Raw = W#Raw

    def predicate(raw: W := Raw): Boolean
  }

  trait SubsetType[W0 <: AnyType] extends AnySubsetType { type W = W0 }

  object AnySubsetType {

    implicit def getSubsetTypeOps[W <: AnyType, ST <: SubsetType[W]](st: ST): SubsetTypeOps[W,ST] =
      SubsetTypeOps(st)
    case class SubsetTypeOps[W <: AnyType, ST <: SubsetType[W]](val st: ST) extends AnyVal {

      final def apply(raw: W := W#Raw): Option[ValueOf[ST]] = {

        if ( st predicate raw ) None else Some( new ValueOf[ST](raw.value) )
      }

      final def withValue(raw: W := W#Raw): Option[ValueOf[ST]] = apply(raw)
    }

    object ValueOfSubsetTypeOps {

      implicit def ValueOfSubsetTypeOps[
        W <: AnyType,
        ST <: SubsetType[W],
        Ops <: ValueOfSubsetTypeOps[W,ST]
      ](v: ValueOf[ST])(implicit conv: ValueOf[ST] => Ops): Ops = conv(v)

    }

    /* you should implement this trait for providing ops for values of a subset type `ST`. */
    trait ValueOfSubsetTypeOps[W <: AnyType, ST <: SubsetType[W]] extends Any {

      /* use case: concat of sized has the sum of the two arg sizes; but how do you create the corresponding value saving a stupid check (and returning an Option)? `unsafeValueOf`. By implementing this trait you assume the responsibility that comes with being able to create unchecked values of `ST`; use it with caution! */
      protected final def unsafeValueOf[ST0 <: ST](other: ST#Raw): ValueOf[ST] = new ValueOf[ST](other)
    }
  }
}
