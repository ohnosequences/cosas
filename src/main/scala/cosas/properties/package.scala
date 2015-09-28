package ohnosequences.cosas

package object properties {

  type EmptyPropertySet = EmptyPropertySet.type

  /* An alias for unicode-lovers */
  type  □   = EmptyPropertySet
  val □ : □ = EmptyPropertySet

  implicit def propertySyntax[P <: AnyProperty](p: P): syntax.PropertySyntax[P] =
    syntax.PropertySyntax(p)

  implicit def propertySetSyntax[PS <: AnyPropertySet](record: PS):
    syntax.PropertySetSyntax[PS] = syntax.PropertySetSyntax(record)
}
