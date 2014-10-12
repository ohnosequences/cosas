/* 
  ## Building a set of representations

  This is a generic thing for deriving the set of representations 
  from a set of taggedType singletons. For example:
  ```scala
  case object id extends Property[Int]
  case object name extends Property[String]

  implicitly[ValuesOf.By[
    id.type :~: name.type :~: ∅,
    id.Rep  :~: name.Rep  :~: ∅
  ]]
  ```

  See examples of usage it for record properties in tests
*/

package ohnosequences.pointless.ops.typeSet

import ohnosequences.pointless._, AnyFn._, AnyWrap._, AnyTypeSet._

@annotation.implicitNotFound(msg = "Can't construct a set of values for ${S}")
trait ValuesOf[S <: AnyTypeSet.Of[AnyWrap]] extends AnyFn with OutBound[TypeSet.Of[AnyWrappedValue]]

object ValuesOf {

  implicit def empty[E <: AnyEmptySet.Of[AnyWrap]]:
        ValuesOf[E] with Out[∅[AnyWrappedValue]] = 
    new ValuesOf[E] with Out[∅[AnyWrappedValue]]

  implicit def cons[H <: T#Bound, T <: AnyTypeSet { type Bound <: AnyWrap }, TOut <: TypeSet.Of[AnyWrappedValue]]
    (implicit 
      t: ValuesOf[T] { type Out = TOut }
    ):  ValuesOf[H :~: T] with Out[ValueOf[H] :~: TOut] =
    new ValuesOf[H :~: T] with Out[ValueOf[H] :~: TOut]
}

@annotation.implicitNotFound(msg = "Can't construct a set of raw types for ${S}")
trait UnionOfRaws[S <: AnyTypeSet.Of[AnyWrap]] extends AnyFn with OutBound[AnyTypeUnion]

object UnionOfRaws {

  implicit def empty[E <: AnyEmptySet.Of[AnyWrap]]: 
        UnionOfRaws[E] with Out[TypeUnion.empty] =
    new UnionOfRaws[E] with Out[TypeUnion.empty]

  implicit def cons[H <: T#Bound, T <: AnyTypeSet { type Bound <: AnyWrap }, TU <: AnyTypeUnion]
    (implicit 
      t: UnionOfRaws[T] { type Out = TU }
    ):  UnionOfRaws[H :~: T] with Out[TU#or[RawOf[H]]] =
    new UnionOfRaws[H :~: T] with Out[TU#or[RawOf[H]]]
}

@annotation.implicitNotFound(msg = "Can't get wraps of the values set ${S}")
trait WrapsOf[S <: AnyTypeSet.Of[AnyWrappedValue]] extends Fn1[S] with OutBound[TypeSet.Of[AnyWrap]]

object WrapsOf {

  implicit def empty[E <: AnyEmptySet.Of[AnyWrappedValue]]: 
        WrapsOf[E] with Out[∅[AnyWrap]] =
    new WrapsOf[E] with Out[∅[AnyWrap]] { 
      def apply(s: In1): Out = ∅[AnyWrap]
    }

  implicit def cons[HV <: T#Bound, T <: AnyTypeSet { type Bound <: AnyWrappedValue }, TO <: TypeSet.Of[AnyWrap]]
    (implicit 
      getH: HV => HV#Wrap, 
      rest: WrapsOf[T] { type Out = TO }
    ):  WrapsOf[HV :~: T] with Out[HV#Wrap :~: TO] =
    new WrapsOf[HV :~: T] with Out[HV#Wrap :~: TO] {

      def apply(s: In1): Out = getH(s.head) :~: rest(s.tail)
    }
}
