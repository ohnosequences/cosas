package ohnosequences.cosas

package object types {

  type =:[V <: T#Raw, T <: AnyType] = Denotes[V,T]
  type :=[T <: AnyType, V <: T#Raw] = Denotes[V,T]

  type ValueOf[T <: AnyType] = T#Raw Denotes T
  def  valueOf[T <: AnyType, V <: T#Raw](t: T)(v: V): ValueOf[T] = v =: t

  type Accepts[P <: AnyTypePredicate, X <: P#ArgBound] = P#Condition[X]

  implicit def typeSyntax[T <: AnyType](tpe: T): syntax.TypeSyntax[T] = syntax.TypeSyntax(tpe)
}
