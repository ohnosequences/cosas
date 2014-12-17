package ohnosequences.cosas

/* Something super-generic and ultra-abstract */
trait AnyType {

  type Raw
  val label: String

  type Me = this.type

  implicit def meFrom[D <: AnyDenotationOf[Me]](v: D): Me = this
}

object AnyType {

  type :%:[V <: T#Raw, T <: AnyType] = Denotes[V,T]
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

  final def valueOf[@specialized V <: T#Raw](v: V): AnyType.ValueOf[T] = new Denotes(v)
  /*
  Alternative syntax, suggesting something like type ascription: `"12d655xr9" :%: id`.
  */
  final def :%:[@specialized V <: T#Raw](v: V): (V Denotes T) = new Denotes(v)
}

object Denotes {

  type :%:[V <: T#Raw, T <: AnyType] = Denotes[V,T]
  
  implicit def denotationOps[T <: AnyType](tpe: T): DenotationOps[T] = DenotationOps(tpe)
  
  import org.scalactic._
  import TypeCheckedTripleEquals._

  implicit def denotationEquivalence[V, T <: AnyType]: 
        Equivalence[V Denotes T] =
    new Equivalence[V Denotes T] {

      def areEquivalent(a1: V Denotes T, a2: V Denotes T): Boolean =
        a1.value == a2.value
    }

  // trait LowPriorityDenotationConstraints extends TripleEquals {
  //   implicit def lowPriorityDenotationConstraint[A, B](implicit equalOfB: Equal[B], ev: A <:< B): Constraint[A, B] =
  //     new AToBEquivalenceConstraint[A, B](new DenotationEquivalence(equalOfB), ev)
  // }

  // trait DenotationEquality extends LowPriorityDenotationConstraints {
  //   override def convertToEqualizer[T](left: T): Equalizer[T] = super.convertToEqualizer[T](left)
  //   implicit override def convertToCheckingEqualizer[T](left: T): CheckingEqualizer[T] = new CheckingEqualizer(left)
  //   override def unconstrainedEquality[A, B](implicit equalityOfA: Equality[A]): Constraint[A, B] = super.unconstrainedEquality[A, B]
  //   implicit def spireConstraint[A, B](implicit equalOfA: Equal[A], ev: B <:< A): Constraint[A, B] =
  //     new BToAEquivalenceConstraint[A, B](new DenotationEquivalence(equalOfA), ev)
  // }

  // object DenotationEquality extends DenotationEquality

  implicit def eqForDenotes[V <: T#Raw, T <: AnyType]: scalaz.Equal[V Denotes T] = new scalaz.Equal[V Denotes T] {

    def equal(a1: V Denotes T, a2: V Denotes T): Boolean = a1.value == a2.value
  }
  
}
