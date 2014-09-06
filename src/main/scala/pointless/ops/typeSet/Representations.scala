/* 
  ## Building a set of representations

  This is a generic thing for deriving the set of representations 
  from a set of taggedType singletons. For example:
  ```scala
  case object id extends Property[Int]
  case object name extends Property[String]

  implicitly[Represented.By[
    id.type :~: name.type :~: ∅,
    id.Rep  :~: name.Rep  :~: ∅
  ]]
  ```

  See examples of usage it for record properties in tests
*/

package ohnosequences.pointless.ops.typeSet

import ohnosequences.pointless._, AnyFn._, AnyType._, AnyTypeSet._

@annotation.implicitNotFound(msg = "Can't construct a set of representations for ${S}")
trait Represented[S <: AnyTypeSet] extends AnyFn with OutBound[AnyTypeSet]

object Represented {

  implicit val empty: 
        Represented[∅] with Out[∅] = 
    new Represented[∅] with Out[∅]

  implicit def cons[H <: AnyType, T <: AnyTypeSet, TR <: AnyTypeSet]
    (implicit t: Represented[T] with Out[TR]): 
          Represented[H :~: T] with Out[ValueOf[H] :~: TR] =
      new Represented[H :~: T] with Out[ValueOf[H] :~: TR]
}


@annotation.implicitNotFound(msg = "Can't get tags of the set ${S}")
trait TagsOf[S <: AnyTypeSet] extends Fn1[S] with OutBound[AnyTypeSet]

object TagsOf {

  implicit val empty: 
        TagsOf[∅] with Out[∅] =
    new TagsOf[∅] with Out[∅] { def apply(s: ∅): Out = ∅ }

  implicit def cons[H <: AnyType, T <: AnyTypeSet, TO <: AnyTypeSet]
    (implicit 
      getH: ValueOf[H] => H, 
      rest: TagsOf[T] with out[TO]
    ):  TagsOf[ValueOf[H] :~: T] with Out[H :~: TO] =
    new TagsOf[ValueOf[H] :~: T] with Out[H :~: TO] {

      def apply(s: ValueOf[H] :~: T): Out = getH(s.head) :~: rest(s.tail)
    }
}
