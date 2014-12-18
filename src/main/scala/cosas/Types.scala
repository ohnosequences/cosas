package ohnosequences.cosas

/* Something super-generic and ultra-abstract */
trait AnyType {

  type Raw
  val label: String

  final type Me = this.type

  implicit final def meFrom[D <: AnyDenotationOf[Me]](v: D): Me = this
}

object AnyType {

  type :%:[V <: T#Raw, T <: AnyType] = Denotes[V,T]
  type =:[V, T <: AnyType] = Denotes[V,T]
  type :=[T <: AnyType, V] = Denotes[V,T]
  type withRaw[R] = AnyType { type Raw = R }
  type RawOf[W <: AnyType] = W#Raw

  type ValueOf[T <: AnyType] = Denotes[T#Raw, T]
  
  implicit def denotationOps[T <: AnyType](tpe: T): DenotationOps[T] = DenotationOps(tpe)
}

class Type(val label: String) extends AnyType { type Raw = Any }
class Wrap[R](val label: String) extends AnyType { final type Raw = R }

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
  
  final type Value = V
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

  final def valueOf[@specialized V <: T#Raw](v: V): AnyType.ValueOf[T] = new Denotes(v)
  /*
  Alternative syntax, suggesting something like type ascription: `"12d655xr9" :%: id`.
  */
  final def :%:[@specialized V <: T#Raw](v: V): (V Denotes T) = new Denotes(v)

  import AnyType.=:
  final def =:[@specialized V](v: V): V =: T = new Denotes(v)
  // I would prefer this, if the vertical alignement were right: 
  // X ⊧: Type (the symbol is normally 'models' in logic, this would be perfect)
  // X ⊨: Type (normally used for true, also fits well)
  final def :=[@specialized V](v: V): V =: T = new Denotes(v)
}

object Denotes {

  type :%:[V <: T#Raw, T <: AnyType] = Denotes[V,T]
  
  implicit def denotationOps[T <: AnyType](tpe: T): DenotationOps[T] = DenotationOps(tpe)

  // implicit def eqForDenotes[V <: T#Raw, T <: AnyType]: 
  //       scalaz.Equal[V Denotes T] =
  //   new scalaz.Equal[V Denotes T] {
  //     def equal(a1: V Denotes T, a2: V Denotes T): Boolean = a1.value == a2.value
  //   }
}

trait AnySubsetType extends AnyType {

  type W <: AnyType
  type Raw = W#Raw
  def predicate(raw: W#Raw): Boolean
}

trait SubsetType[W0 <: AnyType] extends AnySubsetType { type W = W0 }

object AnySubsetType {

  import AnyType._

  implicit def sstops[W <: AnyType, ST <: SubsetType[W]](st: ST): SubSetTypeOps[W,ST] = new SubSetTypeOps(st)
  class SubSetTypeOps[W <: AnyType, ST <: SubsetType[W]](val st: ST) extends AnyVal {

    final def apply(raw: ST#W#Raw): Option[ValueOf[ST]] = {

      if ( st predicate raw ) None else Some( new ValueOf[ST](raw) )
    }
    
    final def withValue(raw: ST#Raw): Option[ValueOf[ST]] = apply(raw)
  }

  object ValueOfSubsetTypeOps {

    implicit def ValueOfSubsetTypeOps[
      W <: AnyType,
      ST <: SubsetType[W],
      Ops <: ValueOfSubsetTypeOps[W,ST]
    ](v: ValueOf[ST])(implicit conv: ValueOf[ST] => Ops): Ops = conv(v)

  }
  /*
  you should implement this trait for providing ops for values of a subset type `ST`.
  */
  trait ValueOfSubsetTypeOps[W <: AnyType, ST <: SubsetType[W]] extends Any {

    /*
    use case: concat of sized has the sum of the two arg sizes; but how do you create the corresponding value saving a stupid check (and returning an Option)? `unsafeValueOf`. By implementing this trait you assume the responsibility that comes with being able to create unchecked values of `ST`; use it with caution!
    */
    protected final def unsafeValueOf[ST0 <: ST](other: ST#Raw): ValueOf[ST] = new ValueOf[ST](other)
  }
}
