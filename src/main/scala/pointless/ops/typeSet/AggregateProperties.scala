package ohnosequences.pointless.ops.typeSet

import ohnosequences.pointless._, AnyFn._, AnyTypeSet._

/* This is an op for aggregating properties from a vertex or an edge types set */
@annotation.implicitNotFound(msg = "Can't aggregate properties of elements of ${S}")
trait AggregateProperties[S <: AnyTypeSet.Of[AnyPropertiesHolder]] 
  extends Fn1[S] 
  with OutBound[TypeSet.Of[AnyProperty]]

// TODO: the pattern here is flattening a set of sets, it should be a separate op
object AggregateProperties {

  def apply[S <: AnyTypeSet.Of[AnyPropertiesHolder]]
    (implicit uni: AggregateProperties[S]): AggregateProperties[S] = uni

  implicit def empty[E <: AnyEmptySet.Of[AnyPropertiesHolder]]:
        AggregateProperties[E] with Out[∅[AnyProperty]] =
    new AggregateProperties[E] with Out[∅[AnyProperty]] { 
      def apply(s: In1) = ∅[AnyProperty]
    }

  implicit def cons[
    H <: T#Bound, T <: AnyTypeSet { type Bound <: AnyPropertiesHolder },
    TOut <: AnyTypeSet, 
    U <: TypeSet.Of[AnyProperty]
  ](implicit
      t: AggregateProperties[T] { type Out = TOut },
      u: (H#Properties ∪ TOut) { type Out = U }
    ):  AggregateProperties[H :~: T] with Out[U] =
    new AggregateProperties[H :~: T] with Out[U] { 

      def apply(s: H :~: T) = u(s.head.properties, t(s.tail))
    }
}
