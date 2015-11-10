package ohnosequences.cosas

package object types {

  type :=[T <: AnyType, +V <: T#Raw] = Denotes[V,T]

  type unit = EmptyProductType.type
  val  unit : unit = EmptyProductType

  type Accepts[P <: AnyTypePredicate, X <: P#ArgBound] = P#Condition[X]
}
