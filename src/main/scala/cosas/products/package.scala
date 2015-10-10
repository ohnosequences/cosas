package ohnosequences.cosas

import ohnosequences.cosas.fns._

package object products {

  type KNil[+A] = KNilOf[A]
  def  KNil[A]: KNil[A] = KNilOf[A]
  type ::[+H <: T#Bound, +T <: AnyKList] = KCons[H,T]

  type mapKList[F <: AnyDepFn1] =
    MapKListOf[F with AnyDepFn1 { type In1 = F#In1; type Out = F#Out }, F#In1,F#Out]
  def mapKList[F <: AnyDepFn1]: mapKList[F] =
    new MapKListOf[F with AnyDepFn1 { type In1 = F#In1; type Out = F#Out }, F#In1,F#Out]
  def altBuh[F <: AnyDepFn1 { type Out = Y; type In1 = X }, X,Y]: MapKListOf[F,X,Y] = new MapKListOf[F,X,Y]
}
