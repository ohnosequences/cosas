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

import ohnosequences.pointless._, AnyFn._, taggedType._, typeSet._

@annotation.implicitNotFound(msg = "Can't construct a set of representations for ${S}")
trait Represented[S <: AnyTypeSet] extends AnyFn with WithCodomain[AnyTypeSet]

object Represented {

  implicit val empty: Represented[∅] with out[∅] = new Represented[∅] { type Out = ∅ }

  implicit def cons[H <: AnyTaggedType, T <: AnyTypeSet]
    (implicit t: Represented[T]): Represented[H :~: T] with out[Tagged[H] :~: t.Out] =
      new Represented[H :~: T] { type Out = Tagged[H] :~: t.Out }
}


// @annotation.implicitNotFound(msg = "")
// trait TagsOf[S <: TypeSet] extends DepFn1[S] { type Out <: TypeSet }

// object TagsOf {

//   def apply[S <: TypeSet](implicit keys: TagsOf[S]): Aux[S, keys.Out] = keys

//   type Aux[S <: TypeSet, O <: TypeSet] = TagsOf[S] { type Out = O }

//   implicit val empty: Aux[∅, ∅] =
//     new TagsOf[∅] {
//       type Out = ∅
//       def apply(s: ∅): Out = ∅
//     }

//   implicit def cons[H <: Singleton with Representable, T <: TypeSet]
//     (implicit fromRep: Tagged[H] => H, t: TagsOf[T]): Aux[Tagged[H] :~: T, H :~: t.Out] =
//       new TagsOf[Tagged[H] :~: T] {
//         type Out = H :~: t.Out
//         def apply(s: Tagged[H] :~: T): Out = {

//           val uh: H = fromRep(s.head)
//           uh :~: t(s.tail)
//         }
//       }
// }
