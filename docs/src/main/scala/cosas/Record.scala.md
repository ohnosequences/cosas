
```scala
package ohnosequences.cosas

import AnyTypeSet._, AnyProperty._, AnyType._, AnyTypeUnion._, AnyFn._
import ops.typeSets._


trait AnyRecord extends AnyType with AnyPropertiesHolder {

  val label: String
```

Record wraps a set of values of it's properties

```scala
  type Raw <: AnyTypeSet
  // should be provided implicitly:
  implicit val valuesOfProperties: Raw areValuesOf Properties
}

class Record[Props <: AnyTypeSet.Of[AnyProperty], Vals <: AnyTypeSet]
  (val properties: Props)
  (implicit 
    val valuesOfProperties: Vals areValuesOf Props
  ) extends AnyRecord {

  val label = this.toString

  type Properties = Props
  type Raw = Vals
}

object AnyRecord {
```

Refiners

```scala
  type withProperties[Ps <: AnyTypeSet.Of[AnyProperty]] = AnyRecord { type Properties = Ps }
  type withRaw[R <: AnyTypeSet] = AnyRecord { type Raw = R }

  type size[R <: AnyRecord] = AnyTypeSet.size[R#Raw]
```

Accessors

```scala
  type PropertiesOf[R <: AnyRecord] = R#Properties

  implicit def recordOps[R <: AnyRecord](rec: R): 
        RecordOps[R] = 
    new RecordOps[R](rec)

  // NOTE: you'll only get record ops _if_ you have a ValueOf[R]. From that point, you don't need the wrapper at all, just use `recEntry.value`. This lets you make RecordRawOps a value class itself!
  // see https://stackoverflow.com/questions/14861862/how-do-you-enrich-value-classes-without-overhead/
  implicit def recordRepOps[R <: AnyRecord](recEntry: ValueOf[R]): 
        RecordRawOps[R] = 
    new RecordRawOps[R](recEntry.value)

}

// class RecordOps[R <: AnyRecord](val rec: R) extends WrapOps[R](rec) {
class RecordOps[R <: AnyRecord](val rec: R) extends AnyVal {

  def apply(v: R#Raw): ValueOf[R] = new ValueOf[R](v)
```

Same as just tagging with `=>>`, but you can pass fields in any order

```scala
  def fields[Vs <: AnyTypeSet](values: Vs)(implicit
      reorder: Vs ReorderTo RawOf[R]
    ): ValueOf[R] = rec denoteWith (reorder(values))

  def parseFrom[X](x: X)(implicit parseSet: (R#Properties ParseFrom X) { type Out = R#Raw }): ValueOf[R] = 
    rec denoteWith (parseSet(rec.properties, x))

}

class RecordRawOps[R <: AnyRecord](val recRaw: RawOf[R]) extends AnyVal {
  
  import ops.records._

  def get[P <: AnyProperty](p: P)
    (implicit get: R Get P): ValueOf[P] = get(recRaw)
    // (implicit lookup: RawOf[R] Lookup ValueOf[P]): ValueOf[P] = lookup(recRaw.raw)


  def update[P <: AnyProperty](propRep: ValueOf[P])
    (implicit check: (ValueOf[P] :~: ∅) ⊂ RawOf[R], 
              upd: R Update (ValueOf[P] :~: ∅)
    ): ValueOf[R] = upd(recRaw, propRep :~: ∅)

  def update[Ps <: AnyTypeSet](propReps: Ps)
    (implicit upd: R Update Ps): ValueOf[R] = upd(recRaw, propReps)


  def as[Other <: AnyRecord](other: Other)
    (implicit project: Take[RawOf[R], RawOf[Other]]): ValueOf[Other] = other denoteWith (project(recRaw))

  def as[Other <: AnyRecord, Rest <: AnyTypeSet](other: Other, rest: Rest)
    (implicit transform: Transform[R, Other, Rest]): ValueOf[Other] = transform(recRaw, other, rest)


  def serializeTo[X](implicit serializer: R#Raw SerializeTo X): X = serializer(recRaw)
}

```


------

### Index

+ src
  + test
    + scala
      + cosas
        + [PropertyTests.scala][test/scala/cosas/PropertyTests.scala]
        + [TypeUnionTests.scala][test/scala/cosas/TypeUnionTests.scala]
        + [ScalazEquality.scala][test/scala/cosas/ScalazEquality.scala]
        + [WrapTests.scala][test/scala/cosas/WrapTests.scala]
        + [RecordTests.scala][test/scala/cosas/RecordTests.scala]
        + [TypeSetTests.scala][test/scala/cosas/TypeSetTests.scala]
  + main
    + scala
      + cosas
        + [PropertiesHolder.scala][main/scala/cosas/PropertiesHolder.scala]
        + [Record.scala][main/scala/cosas/Record.scala]
        + ops
          + typeSet
            + [Check.scala][main/scala/cosas/ops/typeSet/Check.scala]
            + [Reorder.scala][main/scala/cosas/ops/typeSet/Reorder.scala]
            + [Conversions.scala][main/scala/cosas/ops/typeSet/Conversions.scala]
            + [AggregateProperties.scala][main/scala/cosas/ops/typeSet/AggregateProperties.scala]
            + [Subtract.scala][main/scala/cosas/ops/typeSet/Subtract.scala]
            + [Pop.scala][main/scala/cosas/ops/typeSet/Pop.scala]
            + [Representations.scala][main/scala/cosas/ops/typeSet/Representations.scala]
            + [Replace.scala][main/scala/cosas/ops/typeSet/Replace.scala]
            + [Take.scala][main/scala/cosas/ops/typeSet/Take.scala]
            + [Union.scala][main/scala/cosas/ops/typeSet/Union.scala]
            + [Mappers.scala][main/scala/cosas/ops/typeSet/Mappers.scala]
          + record
            + [Update.scala][main/scala/cosas/ops/record/Update.scala]
            + [Conversions.scala][main/scala/cosas/ops/record/Conversions.scala]
            + [Get.scala][main/scala/cosas/ops/record/Get.scala]
        + [TypeUnion.scala][main/scala/cosas/TypeUnion.scala]
        + [Fn.scala][main/scala/cosas/Fn.scala]
        + [Types.scala][main/scala/cosas/Types.scala]
        + csv
          + [csv.scala][main/scala/cosas/csv/csv.scala]
        + [Property.scala][main/scala/cosas/Property.scala]
        + [TypeSet.scala][main/scala/cosas/TypeSet.scala]

[test/scala/cosas/PropertyTests.scala]: ../../../test/scala/cosas/PropertyTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: ../../../test/scala/cosas/TypeUnionTests.scala.md
[test/scala/cosas/ScalazEquality.scala]: ../../../test/scala/cosas/ScalazEquality.scala.md
[test/scala/cosas/WrapTests.scala]: ../../../test/scala/cosas/WrapTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: ../../../test/scala/cosas/TypeSetTests.scala.md
[main/scala/cosas/PropertiesHolder.scala]: PropertiesHolder.scala.md
[main/scala/cosas/Record.scala]: Record.scala.md
[main/scala/cosas/ops/typeSet/Check.scala]: ops/typeSet/Check.scala.md
[main/scala/cosas/ops/typeSet/Reorder.scala]: ops/typeSet/Reorder.scala.md
[main/scala/cosas/ops/typeSet/Conversions.scala]: ops/typeSet/Conversions.scala.md
[main/scala/cosas/ops/typeSet/AggregateProperties.scala]: ops/typeSet/AggregateProperties.scala.md
[main/scala/cosas/ops/typeSet/Subtract.scala]: ops/typeSet/Subtract.scala.md
[main/scala/cosas/ops/typeSet/Pop.scala]: ops/typeSet/Pop.scala.md
[main/scala/cosas/ops/typeSet/Representations.scala]: ops/typeSet/Representations.scala.md
[main/scala/cosas/ops/typeSet/Replace.scala]: ops/typeSet/Replace.scala.md
[main/scala/cosas/ops/typeSet/Take.scala]: ops/typeSet/Take.scala.md
[main/scala/cosas/ops/typeSet/Union.scala]: ops/typeSet/Union.scala.md
[main/scala/cosas/ops/typeSet/Mappers.scala]: ops/typeSet/Mappers.scala.md
[main/scala/cosas/ops/record/Update.scala]: ops/record/Update.scala.md
[main/scala/cosas/ops/record/Conversions.scala]: ops/record/Conversions.scala.md
[main/scala/cosas/ops/record/Get.scala]: ops/record/Get.scala.md
[main/scala/cosas/TypeUnion.scala]: TypeUnion.scala.md
[main/scala/cosas/Fn.scala]: Fn.scala.md
[main/scala/cosas/Types.scala]: Types.scala.md
[main/scala/cosas/csv/csv.scala]: csv/csv.scala.md
[main/scala/cosas/Property.scala]: Property.scala.md
[main/scala/cosas/TypeSet.scala]: TypeSet.scala.md