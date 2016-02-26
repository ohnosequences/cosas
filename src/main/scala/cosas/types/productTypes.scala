package ohnosequences.cosas.types

import ohnosequences.cosas._, klists._, fns._

// TODO reproduce KList
trait AnyProductType extends AnyType {

  type B <: AnyType
  type Types <: AnyKList { type Bound = B }// { type Bound = B } //{ type Bound = prod.Bound } thanks scalac
  val  types: Types

  type Raw <: AnyKList { type Bound <: AnyDenotation { type Tpe <: B; type Value <: B#Raw } }
}

case object AnyProductType {
  type Of[+B <: AnyType] = AnyProductType { type Types <: AnyKList.Of[B] }
  type withBound[B <: AnyType] = AnyProductType { type Types <: AnyKList.withBound[B] }

  // implicit def productTypeSyntax[L <: AnyProductType {
  //     type Types <: AnyKList { type Bound <: AnyType };
  //     type B = Types#Bound;
  //     type Raw <: AnyKList { type Bound = AnyDenotation { type Tpe <: Types#Bound } }
  //   }
  // ](l: L)
  // : syntax.AnyProductTypeSyntax[L] =
  //   syntax.AnyProductTypeSyntax(l)

  implicit def productTypeDenotationSyntax[L <: AnyProductType, Vs <: L#Raw](ds: L := Vs)
  : syntax.AnyProductTypeDenotationSyntax[L,Vs] =
    syntax.AnyProductTypeDenotationSyntax(ds)

}

class EmptyProductType[E <: AnyType] extends AnyProductType {

  type B = E
  type Types = *[E]
  val types: Types = *[E]

  type Raw = *[AnyDenotation { type Tpe <: E }]

  val label: String = "()"
}

trait AnyNonEmptyProductType extends AnyProductType { nept =>

  type Tail <: AnyProductType // { type B = nept.B }
  val tail: Tail
  type Head <: Tail#B
  val head: Head

  // type B >: Tail#B <: Tail#B// <: Tail#Types#Bound
}

case class :×:[
  H0 <: AnyType,
  T0 <: AnyProductType {

    type Types <: AnyKList { type Bound >: H0 <: AnyType }
    type Raw <: AnyKList { type Bound >: (H0 := H0#Raw) <: AnyDenotation { type Tpe <: Types#Bound }  }
  }
](val head: H0, val tail: T0) extends AnyProductType {

  type Tail = T0
  type B = T0#Types#Bound
  type Head = H0

  type Types = Head :: T0#Types // T0#Types#Bound // <: Head :: Tail#Types //<: Head :: Tail#Types
  lazy val types: Types = head  :: (tail.types: T0#Types)

  // type Raw = (AnyDenotation { type Tpe = H0; type Value <: H0#Raw}) :: Tail#Raw
  type Raw = (H0 := H0#Raw) :: Tail#Raw

  lazy val label: String = s"${head.label} :×: ${tail.label}"
  // type            Types = H :: T#Types
  // lazy val types: Types = new ::[H,T#Types](head,tail.types) //(head: H with T#B) :: (tail.types: T#Types)
  //
  // type Raw = (H := H#Raw) :: T#Raw
  //
  // lazy val label: String = s"${head.label} :×: ${tail.label}"
}
