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
trait ValuesOf[S <: AnyTypeSet] extends AnyFn with OutBound[AnyTypeSet]

object ValuesOf {

  implicit val empty: 
        ValuesOf[∅] with Out[∅] = 
    new ValuesOf[∅] with Out[∅]

  implicit def cons[H <: AnyWrap, T <: AnyTypeSet, TR <: AnyTypeSet]
    (implicit t: ValuesOf[T] with out[TR]): 
          ValuesOf[H :~: T] with Out[ValueOf[H] :~: TR] =
      new ValuesOf[H :~: T] with Out[ValueOf[H] :~: TR]
}

@annotation.implicitNotFound(msg = "Can't construct a set of raw types for ${S}")
trait UnionOfRaws[S <: AnyTypeSet] extends AnyFn with OutBound[AnyTypeUnion]

object UnionOfRaws {

  implicit val empty: UnionOfRaws[∅] with Out[TypeUnion.empty] =
                  new UnionOfRaws[∅] with Out[TypeUnion.empty]

  implicit def cons[H <: AnyWrap, T <: AnyTypeSet, TU <: AnyTypeUnion]
    (implicit t: UnionOfRaws[T] with out[TU]): 
           UnionOfRaws[H :~: T] with Out[TU#or[RawOf[H]]] =
       new UnionOfRaws[H :~: T] with Out[TU#or[RawOf[H]]]
}

@annotation.implicitNotFound(msg = "Can't get types of the values set ${S}")
trait TypesOf[S <: AnyTypeSet] extends Fn1[S] with OutBound[AnyTypeSet]

object TypesOf {

  implicit val empty: 
        TypesOf[∅] with Out[∅] =
    new TypesOf[∅] with Out[∅] { def apply(s: ∅): Out = ∅ }

  implicit def cons[H <: AnyWrap, T <: AnyTypeSet, TO <: AnyTypeSet]
    (implicit 
      getH: ValueOf[H] => H, 
      rest: TypesOf[T] with out[TO]
    ):  TypesOf[ValueOf[H] :~: T] with Out[H :~: TO] =
    new TypesOf[ValueOf[H] :~: T] with Out[H :~: TO] {

      def apply(s: ValueOf[H] :~: T): Out = getH(s.head) :~: rest(s.tail)
    }
}
