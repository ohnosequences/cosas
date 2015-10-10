package ohnosequences.cosas

import scala.annotation.unchecked.{uncheckedVariance => uv}

package object products {

  type KNil[+A] = KNilOf[A]
  def  KNil[A]: KNil[A] = KNilOf[A]
  // type ::[H <: T#Bound, T <: AnyKList] = KCons[H,T]
}
