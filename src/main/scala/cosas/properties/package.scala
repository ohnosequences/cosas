package ohnosequences.cosas

package object properties {

  /* An alias for unicode-lovers */
  type  □   = EmptyPropertySet.type
  val □ : □ = EmptyPropertySet

  implicit def propertySyntax[P <: AnyProperty](p: P): syntax.PropertySyntax[P] =
    syntax.PropertySyntax(p)

  implicit def propertySetSyntax[PS <: AnyPropertySet](record: PS): syntax.PropertySetSyntax[PS] =
    syntax.PropertySetSyntax(record)
}
