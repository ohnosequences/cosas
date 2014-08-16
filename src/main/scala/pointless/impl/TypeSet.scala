package ohnosequences.pointless.impl

import ohnosequences.pointless._, AnyFn._
import shapeless.{ HList, Poly1, <:!<, =:!= }

object typeSet extends anyTypeSet {

  type typeUnion = impl.typeUnion.type
  import typeUnion._

  type AnyTypeSet = AnyTypeSetImpl

  type ∅ = EmptySetImpl
  val  ∅ : ∅ = EmptySet
  val  emptySet : ∅ = EmptySet

  type :~:[E, S <: AnyTypeSet] = ConsImpl[E, S]


  /*
    Implementations
  */
  trait AnyTypeSetImpl {

    type Types <: AnyTypeUnion

    def toStr: String
    override def toString = "{" + toStr + "}"
  }


  sealed trait EmptySetImpl extends AnyTypeSetImpl {

    type Types = either[Nothing]
    def toStr = ""
  }

  object EmptySet extends EmptySetImpl


  case class ConsImpl[E, S <: AnyTypeSetImpl](head: E, tail: S)(implicit check: E ∉ S) extends AnyTypeSetImpl {
    type Types = tail.Types#or[E]
    def toStr = {
      val h = head match {
        case _: String => "\""+head+"\""
        case _: Char   => "\'"+head+"\'"
        case _         => head.toString
      }
      val t = tail.toStr
      if (t.isEmpty) h else h+", "+t
    }
  }

  /* This method covers constructor to check that you are not adding a duplicate */
  object  ConsImpl {
    def cons[E, S <: AnyTypeSetImpl](e: E, set: S)(implicit check: E ∉ S): ConsImpl[E,S] = ConsImpl(e, set) 
  }


  /*
    Predicates
  */
  type    isIn[E, S <: AnyTypeSet] = E    isOneOf S#Types
  type isNotIn[E, S <: AnyTypeSet] = E isNotOneOf S#Types

  type    isSubsetOf[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Types#union <:<  Q#Types#union 
  type isNotSubsetOf[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Types#union <:!< Q#Types#union

  type    isSameAs[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Types#union =:=  Q#Types#union
  type isNotSameAs[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Types#union =:!= Q#Types#union

  type    isBoundedBy[S <: AnyTypeSet, B] = S#Types#union <:<  either[B]#union
  type isNotBoundedBy[S <: AnyTypeSet, B] = S#Types#union <:!< either[B]#union

  type    isBoundedByUnion[S <: AnyTypeSet, U <: AnyTypeUnion] = S#Types#union <:<  U#union
  type isNotBoundedByUnion[S <: AnyTypeSet, U <: AnyTypeUnion] = S#Types#union <:!< U#union


  /*
    Function types
  */
  type \[S <: AnyTypeSet, Q <: AnyTypeSet] = ops.Subtract[S, Q]

  type ∪[S <: AnyTypeSet, Q <: AnyTypeSet] = ops.Union[S, Q]

  type Pop[S <: AnyTypeSet, E] = ops.Pop[S, E]

  type Lookup[S <: AnyTypeSet, E] = ops.Lookup[S, E]

  type Take[S <: AnyTypeSet, Q <: AnyTypeSet] = ops.Take[S, Q]

  type Replace[S <: AnyTypeSet, Q <: AnyTypeSet] = ops.Replace[S, Q]

  type As[S <: AnyTypeSet, Q <: AnyTypeSet] = ops.As[S, Q]

  type FromHList[L <: HList] = ops.FromHList[L]

  type ToHList[S <: AnyTypeSet] = ops.ToHList[S]

  type  ToList[S <: AnyTypeSet] = ops.ToList[S]

  type MapToHList[F <: Poly1, S <: AnyTypeSet] = ops.MapToHList[F, S]

  type  MapToList[F <: Poly1, S <: AnyTypeSet] = ops.MapToList[F, S]

  type     MapSet[F <: Poly1, S <: AnyTypeSet] = ops.MapSet[F, S]

  type MapFoldSet[F <: Poly1, S <: AnyTypeSet, R] = ops.MapFoldSet[F, S, R]

  /*
    Ops
  */
  implicit def typeSetOps[S <: AnyTypeSet](s: S): TypeSetOps[S] = TypeSetOps[S](s)
  case class   TypeSetOps[S <: AnyTypeSet](s: S) extends AnyTypeSetOps[S](s) {

    def :~:[E](e: E)(implicit check: E ∉ S): (E :~: S) = ConsImpl.cons(e, s)

  }

}
