package ohnosequences.cosas

package object products {

  type KNil[A] = KNilOf[A]
  def  KNil[A]: KNil[A] = KNilOf[A]

  type ::[+H <: T#Bound, +T <: AnyKList] = KCons[H,T]
}
