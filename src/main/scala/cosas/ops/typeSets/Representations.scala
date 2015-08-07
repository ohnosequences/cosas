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

package ohnosequences.cosas.ops.typeSets

import ohnosequences.cosas._, fns._, types._, typeUnions._, typeSets._

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
    ):  UnionOfRaws[H :~: T] with Out[TU#or[H#Raw]] =
    new UnionOfRaws[H :~: T] with Out[TU#or[H#Raw]]
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

@annotation.implicitNotFound(msg = "Can't get types of the denotations set ${S}")
trait TypesOf[S <: AnyTypeSet] extends Fn1[S] with OutBound[AnyTypeSet]

object TypesOf {

  implicit val empty:
        TypesOf[∅] with Out[∅] =
    new TypesOf[∅] with Out[∅] { def apply(s: ∅): Out = ∅ }

  implicit def cons[H <: AnyType, V, T <: AnyTypeSet, TO <: AnyTypeSet]
    (implicit
      getH: H,
      rest: TypesOf[T] { type Out = TO }
    ):  TypesOf[Denotes[V, H] :~: T] with Out[H :~: TO] =
    new TypesOf[Denotes[V, H] :~: T] with Out[H :~: TO] {

      def apply(s: Denotes[V, H] :~: T): Out = getH :~: rest(s.tail)
    }
}
