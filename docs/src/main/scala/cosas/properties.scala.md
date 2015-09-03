
```scala
package ohnosequences.cosas

object properties {

  // deps
  import types._, typeSets._

  trait AnyProperty extends AnyType

  class Property[V](val label: String) extends AnyProperty { type Raw = V }

  case object AnyProperty {

    type ofType[T] = AnyProperty { type Raw = T }

    implicit def propertyOps[P <: AnyProperty](p: P): PropertyOps[P] = PropertyOps(p)
  }

  trait AnyPropertySerializer extends AnyDenotationSerializer {

    type Type <: AnyProperty
    type D = Type#Raw
  }

  case class PropertySerializer[P <: AnyProperty,V](
    val tpe: P,
    val labelRep: String
  )(
    val serializer: P#Raw => Option[V]
  ) extends AnyPropertySerializer {

    type Type = P
    type Value = V
  }

  trait AnyPropertyParser extends AnyDenotationParser { parser =>

    type Type <: AnyProperty
    type D = Type#Raw
  }
  case class PropertyParser[P <: AnyProperty,V](
    val tpe: P,
    val labelRep: String)(
    val parser: V => Option[P#Raw]
  ) extends AnyPropertyParser {

    type Type = P
    type Value = V
  }

  case class PropertyOps[P <: AnyProperty](val p: P) extends AnyVal {

    def apply(v: P#Raw): ValueOf[P] = valueOf(p)(v)
  }
```


## PropertySet

PropertySet wraps a typeset of properties, constructing along the way the typeset of their `ValueOf`s.


```scala
  trait AnyPropertySet extends AnyType {

    type Properties <: AnyTypeSet // of AnyProperty's
    val  properties: Properties

    type Raw <: AnyTypeSet

    type Size = Properties#Size
  }

  case object EmptyPropertySet extends AnyPropertySet {

    type Properties = ∅
    val  properties = ∅

    type Raw = ∅

    val label: String = "□"
  }
  type EmptyPropertySet = EmptyPropertySet.type
```

An alias for unicode-lovers

```scala
  type  □   = EmptyPropertySet
  val □ : □ = EmptyPropertySet

  // TODO review this symbol; I'm fine with any other
  case class :&:[P <: AnyProperty, T <: AnyPropertySet]
  (val head: P, val tail: T)(implicit val headIsNew: P ∉ T#Properties) extends AnyPropertySet {

    type Properties = P :~: T#Properties
    val  properties: Properties = head :~: (tail.properties: T#Properties)

    type Raw = ValueOf[P] :~: T#Raw

    lazy val label: String = s"${head.label} :&: ${tail.label}"
  }

  case object AnyPropertySet {
```

Refiners

```scala
    type withBound[B <: AnyProperty] = AnyPropertySet { type Properties <: AnyTypeSet.Of[B] }

    type withProperties[Ps <: AnyTypeSet.Of[AnyProperty]] = AnyPropertySet { type Properties = Ps }
    type withRaw[Vs <: AnyTypeSet] = AnyPropertySet { type Raw = Vs }
  }

  implicit def getPropertySetOps[PS <: AnyPropertySet](record: PS):
    PropertySetOps[PS] = PropertySetOps(record)

  case class PropertySetOps[PS <: AnyPropertySet](val propSet: PS) extends AnyVal {

    def :&:[P <: AnyProperty](p: P)(implicit check: P ∉ PS#Properties): (P :&: PS) = properties.:&:(p, propSet)
  }


  @annotation.implicitNotFound(msg = """
    Cannot prove that record
      ${PS}
    has property
      ${P}
  """)
  sealed class HasProperty[PS <: AnyPropertySet, P <: AnyProperty]

  case object HasProperty {

    implicit def pIsInProperties[PS <: AnyPropertySet, P <: AnyProperty]
      (implicit in: P ∈ PS#Properties):
          (PS HasProperty P) =
      new (PS HasProperty P)
  }

  @annotation.implicitNotFound(msg = """
    Cannot prove that record
      ${PS}
    has properties
      ${P}
  """)
  sealed class HasProperties[PS <: AnyPropertySet, P <: AnyTypeSet.Of[AnyProperty]]

  object HasProperties {
    import ops.typeSets.CheckForAll

    trait PropertyIsIn[PS <: AnyPropertySet] extends TypePredicate[AnyProperty] {
      type Condition[P <: AnyProperty] = PS HasProperty P
    }

    implicit def recordHasP[PS <: AnyPropertySet, P <: AnyTypeSet.Of[AnyProperty]]
      (implicit check: CheckForAll[P, PropertyIsIn[PS]]):
          (PS HasProperties P) =
      new (PS HasProperties P)
  }


}

```




[test/scala/cosas/asserts.scala]: ../../../test/scala/cosas/asserts.scala.md
[test/scala/cosas/DenotationTests.scala]: ../../../test/scala/cosas/DenotationTests.scala.md
[test/scala/cosas/SubsetTypesTests.scala]: ../../../test/scala/cosas/SubsetTypesTests.scala.md
[test/scala/cosas/EqualityTests.scala]: ../../../test/scala/cosas/EqualityTests.scala.md
[test/scala/cosas/PropertyTests.scala]: ../../../test/scala/cosas/PropertyTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: ../../../test/scala/cosas/TypeSetTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: ../../../test/scala/cosas/TypeUnionTests.scala.md
[main/scala/cosas/typeUnions.scala]: typeUnions.scala.md
[main/scala/cosas/properties.scala]: properties.scala.md
[main/scala/cosas/records.scala]: records.scala.md
[main/scala/cosas/fns.scala]: fns.scala.md
[main/scala/cosas/types.scala]: types.scala.md
[main/scala/cosas/typeSets.scala]: typeSets.scala.md
[main/scala/cosas/ops/records/Update.scala]: ops/records/Update.scala.md
[main/scala/cosas/ops/records/Transform.scala]: ops/records/Transform.scala.md
[main/scala/cosas/ops/records/Get.scala]: ops/records/Get.scala.md
[main/scala/cosas/ops/typeSets/SerializeDenotations.scala]: ops/typeSets/SerializeDenotations.scala.md
[main/scala/cosas/ops/typeSets/ParseDenotations.scala]: ops/typeSets/ParseDenotations.scala.md
[main/scala/cosas/ops/typeSets/Conversions.scala]: ops/typeSets/Conversions.scala.md
[main/scala/cosas/ops/typeSets/Filter.scala]: ops/typeSets/Filter.scala.md
[main/scala/cosas/ops/typeSets/Subtract.scala]: ops/typeSets/Subtract.scala.md
[main/scala/cosas/ops/typeSets/Mappers.scala]: ops/typeSets/Mappers.scala.md
[main/scala/cosas/ops/typeSets/Union.scala]: ops/typeSets/Union.scala.md
[main/scala/cosas/ops/typeSets/Reorder.scala]: ops/typeSets/Reorder.scala.md
[main/scala/cosas/ops/typeSets/Take.scala]: ops/typeSets/Take.scala.md
[main/scala/cosas/ops/typeSets/Representations.scala]: ops/typeSets/Representations.scala.md
[main/scala/cosas/ops/typeSets/Pop.scala]: ops/typeSets/Pop.scala.md
[main/scala/cosas/ops/typeSets/Replace.scala]: ops/typeSets/Replace.scala.md
[main/scala/cosas/equality.scala]: equality.scala.md