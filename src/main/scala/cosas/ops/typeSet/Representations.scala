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

package ohnosequences.cosas.ops.typeSet

import ohnosequences.cosas._, fn._, denotation._, typeUnion._, AnyTypeSet._

@annotation.implicitNotFound(msg = "Can't construct a set of values for ${S}")
trait ValuesOf[S <: AnyTypeSet] extends AnyFn with OutBound[AnyTypeSet]

object ValuesOf {

  implicit val empty: 
        ValuesOf[∅] with Out[∅] = 
    new ValuesOf[∅] with Out[∅]

  implicit def cons[H <: AnyType, T <: AnyTypeSet, TR <: AnyTypeSet]
    (implicit 
      t: ValuesOf[T] { type Out = TR }
    ):  ValuesOf[H :~: T] with Out[ValueOf[H] :~: TR] =
    new ValuesOf[H :~: T] with Out[ValueOf[H] :~: TR]
}

@annotation.implicitNotFound(msg = "Can't construct a set of raw types for ${S}")
trait UnionOfRaws[S <: AnyTypeSet] extends AnyFn with OutBound[AnyTypeUnion]

object UnionOfRaws {

  implicit val empty: UnionOfRaws[∅] with Out[empty] =
                  new UnionOfRaws[∅] with Out[empty]

  implicit def cons[H <: AnyType, T <: AnyTypeSet, TU <: AnyTypeUnion]
    (implicit 
      t: UnionOfRaws[T] { type Out = TU }
    ):  UnionOfRaws[H :~: T] with Out[TU#or[RawOf[H]]] =
    new UnionOfRaws[H :~: T] with Out[TU#or[RawOf[H]]]
}

@annotation.implicitNotFound(msg = "Can't get wraps of the values set ${S}")
trait WrapsOf[S <: AnyTypeSet] extends Fn1[S] with OutBound[AnyTypeSet]

object WrapsOf {

  implicit val empty: 
        WrapsOf[∅] with Out[∅] =
    new WrapsOf[∅] with Out[∅] { def apply(s: ∅): Out = ∅ }

  implicit def cons[H <: AnyType, T <: AnyTypeSet, TO <: AnyTypeSet]
    (implicit 
      getH: ValueOf[H] => H, 
      rest: WrapsOf[T] { type Out = TO }
    ):  WrapsOf[ValueOf[H] :~: T] with Out[H :~: TO] =
    new WrapsOf[ValueOf[H] :~: T] with Out[H :~: TO] {

      def apply(s: ValueOf[H] :~: T): Out = getH(s.head) :~: rest(s.tail)
    }
}
