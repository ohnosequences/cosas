package ohnosequences.pointless.impl

import ohnosequences.pointless._
import shapeless.{ HList, Poly, <:!<, =:!= }

object typeSet extends anyTypeSet {

  type typeUnion = impl.typeUnion.type
  import typeUnion._

  type AnyTypeSet = TypeSetImpl

  type ∅ = EmptySetImpl
  val  ∅ : ∅ = emptySet

  type :~:[E, S <: AnyTypeSet] = ConsImpl[E, S]


  /*
    Implementations
  */
  trait TypeSetImpl {

    type Types <: AnyTypeUnionImpl

    def toStr: String
    override def toString = "{" + toStr + "}"
  }


  sealed trait EmptySetImpl extends TypeSetImpl {

    type Types = either[Nothing]
    def toStr = ""
  }

  private object emptySet extends EmptySetImpl


  case class ConsImpl[E, S <: TypeSetImpl](head: E, tail: S)(implicit check: E ∉ S) extends TypeSetImpl {
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
    def cons[E, S <: TypeSetImpl](e: E, set: S)(implicit check: E ∉ S): ConsImpl[E,S] = ConsImpl(e, set) 
  }


  /*
    Predicates
  */
  type    isIn[E, S <: AnyTypeSet] = E    isOneOf S#Types
  type isNotIn[E, S <: AnyTypeSet] = E isNotOneOf S#Types

  type    isSubsetOf[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Types#get <:<  Q#Types#get 
  type isNotSubsetOf[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Types#get <:!< Q#Types#get

  type    isSameAs[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Types#get =:=  Q#Types#get
  type isNotSameAs[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Types#get =:!= Q#Types#get

  type    isBoundedBy[S <: AnyTypeSet, B] = S#Types#get <:<  either[B]#get
  type isNotBoundedBy[S <: AnyTypeSet, B] = S#Types#get <:!< either[B]#get

  type    isBoundedByUnion[S <: AnyTypeSet, U <: AnyTypeUnion] = S#Types#get <:<  U#get
  type isNotBoundedByUnion[S <: AnyTypeSet, U <: AnyTypeUnion] = S#Types#get <:!< U#get

}
