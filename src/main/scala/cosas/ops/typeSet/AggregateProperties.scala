package ohnosequences.cosas.ops.typeSet

import ohnosequences.cosas._, fn._, propertiesHolder._, typeSet._

/* This is an op for aggregating properties from a vertex or an edge types set */
@annotation.implicitNotFound(msg = "Can't aggregate properties of elements of ${S}")
trait AggregateProperties[S <: AnyTypeSet] extends Fn1[S] with OutBound[AnyTypeSet]

// TODO: the pattern here is flattening a set of sets, it should be a separate op
object AggregateProperties {

  def apply[S <: AnyTypeSet](implicit uni: AggregateProperties[S]): AggregateProperties[S] = uni

  implicit def empty:
        AggregateProperties[∅] with Out[∅] =
    new AggregateProperties[∅] with Out[∅] { def apply(s: ∅) = ∅ }

  implicit def cons[H <: AnyPropertiesHolder, T <: AnyTypeSet, TOut <: AnyTypeSet, U <: AnyTypeSet]
    (implicit
      t: AggregateProperties[T] { type Out = TOut },
      u: (H#Properties ∪ TOut) { type Out = U }
    ):  AggregateProperties[H :~: T] with Out[U] =
    new AggregateProperties[H :~: T] with Out[U] { 

      def apply(s: H :~: T) = u(s.head.properties, t(s.tail))
    }
}
