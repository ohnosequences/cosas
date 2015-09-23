package ohnosequences.cosas.typeSets

// deps
import ohnosequences.cosas.typeUnions._
import shapeless.{ HList, Poly1, <:!<, =:!= }
import shapeless.Nat._
import shapeless.{Nat, Succ}

sealed trait AnyTypeSet {

  type Types <: AnyTypeUnion
  type Bound // should be Types#union, but we can't set it here

  type Size <: Nat

  def toStr: String
  override def toString = "{" + toStr + "}"
}

trait EmptySet extends AnyTypeSet { type Size = _0 }
trait NonEmptySet extends AnyTypeSet {

    type Head
    val  head: Head

    type Tail <: AnyTypeSet
    val  tail: Tail

    type Size = Succ[Tail#Size]
    // should be provided implicitly:
    val headIsNew: Head ∉ Tail
}

// it's like KList, but a set
case object AnyTypeSet {

  type Of[T] = AnyTypeSet { type Bound <: just[T] }

  type SubsetOf[S <: AnyTypeSet] = AnyTypeSet { type Bound <: S#Bound }

  type SupersetOf[S <: AnyTypeSet] = AnyTypeSet { type Bound >: S#Bound }

  type BoundedByUnion[U <: AnyTypeUnion] = AnyTypeSet { type Bound <: U#union }

  // type SameAs[S <: AnyTypeSet] = SubsetOf[S] with SupersetOf[S]

  implicit def typeSetOps[S <: AnyTypeSet](s: S): TypeSetSyntax[S] = new TypeSetSyntax[S](s)
}

private[cosas] object TypeSetImpl {

  trait EmptySetImpl extends AnyTypeSet with EmptySet {

    type Types = empty
    type Bound = Types#union

    def toStr = ""
  }

  case object EmptySetImpl extends EmptySetImpl { override type Types = empty }


  case class ConsSet[H, T <: AnyTypeSet]
    (val head : H,  val tail : T)(implicit val headIsNew: H ∉ T) extends NonEmptySet {
    type Head = H; type Tail = T

    type Types = Tail#Types or Head
    type Bound = Types#union

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
  case object ConsSet {

    def cons[E, S <: AnyTypeSet](e: E, set: S)(implicit check: E ∉ S): ConsSet[E,S] = ConsSet(e, set)
  }
}


/*
  ### Predicates and aliases
*/
