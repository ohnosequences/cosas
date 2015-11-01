package ohnosequences.cosas

package object types {

  type :=[T <: AnyType, +V <: T#Raw] = Denotes[V,T]

  // TODO alias for EmptyProductType

  type ValueOf[T <: AnyType] = T#Raw Denotes T
  def  valueOf[T <: AnyType, V <: T#Raw](t: T)(v: V): ValueOf[T] = t := v

  type Accepts[P <: AnyTypePredicate, X <: P#ArgBound] = P#Condition[X]
}
