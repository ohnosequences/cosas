package ohnosequences.pointless

import AnyTypeSet._, AnyProperty._, AnyWrap._, AnyTypeUnion._, AnyFn._
import ops.typeSet._


trait AnyPropertiesHolder {
  type This = this.type

  type Properties <: AnyTypeSet.Of[AnyProperty]
  val  properties: Properties
}

trait Properties[Props <: AnyTypeSet.Of[AnyProperty]]
  extends AnyPropertiesHolder { type Properties = Props }

object AnyPropertiesHolder {
  type PropertiesOf[H <: AnyPropertiesHolder] = H#Properties 

  implicit def hasPropertiesOps[T](t: T): HasPropertiesOps[T] = new HasPropertiesOps[T](t)
}

class HasPropertiesOps[Smth](val smth: Smth) {

  // sorry, only one property a time
  def has[P <: AnyProperty](p: P): 
         Smth HasProperty P = 
    new (Smth HasProperty P)
}


@annotation.implicitNotFound(msg = "Can't prove that ${Smth} has property ${P}")
sealed class HasProperty[Smth, P <: AnyProperty]

object HasProperty {

  implicit def holderProps[H <: AnyPropertiesHolder, P <: AnyProperty]
    (implicit in: P ∈ H#Properties):
        (H HasProperty P) =
    new (H HasProperty P)
}


@annotation.implicitNotFound(msg = "Can't prove that ${Smth} has properties ${Ps}")
sealed class HasProperties[Smth, Ps <: AnyTypeSet.Of[AnyProperty]]

object HasProperties {

  trait BelongsTo[T] extends TypePredicate[AnyProperty] {
    type Condition[P <: AnyProperty] = T HasProperty P
  }

  implicit def hasProps[T, Ps <: AnyTypeSet.Of[AnyProperty]]
    (implicit check: CheckForAll[Ps, BelongsTo[T]]):
        (T HasProperties Ps) =
    new (T HasProperties Ps)

}
