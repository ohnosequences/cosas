package ohnosequences.cosas

package object types {

  type =:[V <: T#Raw, T <: AnyType] = Denotes[V,T]
  type :=[T <: AnyType, +V <: T#Raw] = Denotes[V,T]

  type ValueOf[T <: AnyType] = T#Raw Denotes T
  def  valueOf[T <: AnyType, V <: T#Raw](t: T)(v: V): ValueOf[T] = t := v

  type Accepts[P <: AnyTypePredicate, X <: P#ArgBound] = P#Condition[X]

  implicit def typeSyntax[T <: AnyType](tpe: T): syntax.TypeSyntax[T] = syntax.TypeSyntax(tpe)

  implicit def subsetTypeSyntax[W <: AnyType, ST <: SubsetType[W]](st: ST): syntax.SubsetTypeSyntax[W,ST] =
    syntax.SubsetTypeSyntax(st)

  implicit def ValueOfSubsetTypeSyntax[
    W <: AnyType,
    ST <: SubsetType[W],
    Syntax <: ValueOfSubsetTypeSyntax[W,ST]
  ](v: ValueOf[ST])(implicit conv: ValueOf[ST] => Syntax): Syntax = conv(v)

}
