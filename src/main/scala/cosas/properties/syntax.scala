package ohnosequences.cosas.properties

import ohnosequences.cosas._, typeSets._, types._

case object syntax {

  case class PropertySyntax[P <: AnyProperty](val p: P) extends AnyVal {

    def apply[PV <: P#Raw](v: PV): P := PV = p := v
  }

  case class PropertySetSyntax[PS <: AnyPropertySet](val propSet: PS) extends AnyVal {

    def :&:[P <: AnyProperty](p: P)(implicit check: P ∉ PS#Properties): (P :&: PS) = new :&:(p, propSet)
  }
}
