package ohnosequences.cosas.typeSets

// deps
import ohnosequences.cosas._, typeUnions._, types._

sealed trait AnyTypeSet extends Any {

  type Types <: AnyTypeUnion
  // type Union // NOTE should be Types#union, but we can't set it here; scalac bugs

  type Union // <: not[not[Bound]]
  type Bound

  type Size <: AnyNat

  def toStr: String
}

trait TypeSet[+B] extends Any with AnyTypeSet {

  type Bound = B @uv
}

trait AnyEmptySet extends Any with AnyTypeSet {

  type Types = empty
  type Union = empty#union
  type Size = _0
}
case object AnyEmptySet
case class EmptySet[+B](val unique: AnyEmptySet.type) extends AnyVal  with AnyEmptySet with TypeSet[B] {

  def toStr: String = ""
}

trait NonEmptySet[+B] extends TypeSet[B] {

  type Head <: Bound
  val  head: Head

  type Union = Types#union

  type Tail <: AnyTypeSet //{ type Bound = B }
  val  tail: Tail

  type Size = Successor[Tail#Size]
  // should be provided implicitly:
  // val headIsNew: Head ∉ Tail
}

case class ConsSet[+H <: T#Bound, +T <: AnyTypeSet](val head : H, val tail : T) extends NonEmptySet[T#Bound] {

  type Head = H @uv; type Tail = T @uv

  type Types = Tail#Types @uv or H @uv

  final def toStr: String = {
    val h = head match {
      case _: String => "\""+head+"\""
      case _: Char   => "\'"+head+"\'"
      case _         => head.toString
    }
    val t = tail.toStr
    if (t.isEmpty) h else h+", "+t
  }
}



// sealed trait AnyTypeSet {
//
//   type Types <: AnyTypeUnion
//   type Bound >: Types#union <: Types#union
//
//   type Size <: AnyNat
//
//   def toStr: String
//   override final def toString: String = "{" + toStr + "}"
// }



// // it's like KList, but a set
case object AnyTypeSet {

  type Of[T] = AnyTypeSet { type Bound = T }

  type SubsetOf[S <: AnyTypeSet] = AnyTypeSet { type Bound <: S#Bound }

  type SupersetOf[S <: AnyTypeSet] = AnyTypeSet { type Bound >: S#Bound }

  type BoundedByUnion[U <: AnyTypeUnion] = AnyTypeSet { type Bound <: U#union }

  implicit def typeSetSyntax[S <: AnyTypeSet](s: S): syntax.TypeSetSyntax[S] =
    syntax.TypeSetSyntax(s)

  implicit def denotationsSetSyntax[DS0 <: AnyTypeSet.Of[AnyDenotation]](ds: DS0): syntax.DenotationsSetSyntax[DS0] =
    syntax.DenotationsSetSyntax(ds)
}
//
// private[cosas] object TypeSetImpl {
//
//   trait EmptySetImpl extends AnyTypeSet with EmptySet {
//
//     type Types = empty
//     type Bound = Types#union
//
//     final def toStr: String = ""
//   }
//
//   case object EmptySetImpl extends EmptySetImpl { override type Types = empty }
//
//
//   case class ConsSet[H, T <: AnyTypeSet]
//     (val head : H,  val tail : T)(implicit val headIsNew: H ∉ T) extends NonEmptySet {
//
//     type Head = H; type Tail = T
//
//     type Types = Tail#Types or H
//     type Bound = Types#union
//
//     final def toStr: String = {
//       val h = head match {
//         case _: String => "\""+head+"\""
//         case _: Char   => "\'"+head+"\'"
//         case _         => head.toString
//       }
//       val t = tail.toStr
//       if (t.isEmpty) h else h+", "+t
//     }
//   }
//
//   /* This method covers constructor to check that you are not adding a duplicate */
//   case object ConsSet {
//
//     def cons[E, S <: AnyTypeSet](e: E, set: S)(implicit check: E ∉ S): ConsSet[E,S] = ConsSet(e, set)
//   }
// }
