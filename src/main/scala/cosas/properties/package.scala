package ohnosequences.cosas

package object properties {

  type EmptyPropertySet = EmptyPropertySet.type

  /* An alias for unicode-lovers */
  type  □   = EmptyPropertySet
  val □ : □ = EmptyPropertySet

  implicit def getPropertySetOps[PS <: AnyPropertySet](record: PS):
    PropertySetOps[PS] = PropertySetOps(record)

}
