package ohnosequences.cosas

package object types {

  type :=[T <: AnyType, +V <: T#Raw] = Denotes[V,T]

  type |[E <: AnyType] = EmptyProductType[E]
  def  |[E <: AnyType]: |[E] = new EmptyProductType[E]
}
