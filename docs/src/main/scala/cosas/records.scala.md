
```scala
package ohnosequences.cosas

object records {

  // deps
  import typeSets._, types._, propertyHolders._, properties._
  
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

  type size[R <: AnyRecord] = typeSets.size[R#Raw]

  object AnyRecord {
```

Refiners

```scala
    type withProperties[Ps <: AnyTypeSet.Of[AnyProperty]] = AnyRecord { type Properties = Ps }
    type withRaw[R <: AnyTypeSet] = AnyRecord { type Raw = R }

    implicit def recordOps[R <: AnyRecord](rec: R): 
          RecordOps[R] = 
      new RecordOps[R](rec)
  }

  // class RecordOps[R <: AnyRecord](val rec: R) extends WrapOps[R](rec) {
  class RecordOps[R <: AnyRecord](val rec: R) extends AnyVal {

    def apply(v: R#Raw): ValueOf[R] = new ValueOf[R](v)
```

Same as apply, but you can pass fields in any order

```scala
    def apply[Vs <: AnyTypeSet](values: Vs)(implicit
        reorder: Vs ReorderTo R#Raw
      ): ValueOf[R] = rec := reorder(values)

    def parseFrom[X](x: X)(implicit 
      parseSet: (R#Properties ParseFrom X) { type Out = R#Raw }
    ): ValueOf[R] = rec := parseSet(rec.properties, x)

  }

  // NOTE: you'll only get record ops _if_ you have a ValueOf[R]. From that point, you don't need the wrapper at all, just use `rec.value`. This lets you make RecordRawOps a value class itself!
  // see https://stackoverflow.com/questions/14861862/how-do-you-enrich-value-classes-without-overhead/
  implicit def recordRawOps[R <: AnyRecord](rec: ValueOf[R]): 
        RecordRawOps[R] = 
    new RecordRawOps[R](rec.value)

  class RecordRawOps[R <: AnyRecord](val recRaw: R#Raw) extends AnyVal {
    import ops.records._

    def get[P <: AnyProperty](p: P)
      (implicit get: R Get P): ValueOf[P] = get(recRaw)
      // (implicit lookup: R#Raw Lookup ValueOf[P]): ValueOf[P] = lookup(recRaw.raw)


    def update[P <: AnyProperty](propRep: ValueOf[P])
      (implicit check: (ValueOf[P] :~: ∅) ⊂ R#Raw, 
                upd: R Update (ValueOf[P] :~: ∅)
      ): ValueOf[R] = upd(recRaw, propRep :~: ∅)

    def update[Ps <: AnyTypeSet](propReps: Ps)
      (implicit upd: R Update Ps): ValueOf[R] = upd(recRaw, propReps)


    def as[Other <: AnyRecord](other: Other)
      (implicit project: Take[R#Raw, Other#Raw]): ValueOf[Other] = other := project(recRaw)

    def as[Other <: AnyRecord, Rest <: AnyTypeSet](other: Other, rest: Rest)
      (implicit transform: Transform[R, Other, Rest]): ValueOf[Other] = transform(recRaw, other, rest)


    def serializeTo[X](implicit serializer: R#Raw SerializeTo X): X = serializer(recRaw)
  }

}

```


------

### Index

+ src
  + test
    + scala
      + cosas
        + [SubsetTypesTests.scala][test/scala/cosas/SubsetTypesTests.scala]
        + [PropertyTests.scala][test/scala/cosas/PropertyTests.scala]
        + [TypeUnionTests.scala][test/scala/cosas/TypeUnionTests.scala]
        + [ScalazEquality.scala][test/scala/cosas/ScalazEquality.scala]
        + [DenotationTests.scala][test/scala/cosas/DenotationTests.scala]
        + [RecordTests.scala][test/scala/cosas/RecordTests.scala]
        + [TypeSetTests.scala][test/scala/cosas/TypeSetTests.scala]
  + main
    + scala
      + cosas
        + [properties.scala][main/scala/cosas/properties.scala]
        + [typeSets.scala][main/scala/cosas/typeSets.scala]
        + ops
          + records
            + [Update.scala][main/scala/cosas/ops/records/Update.scala]
            + [Conversions.scala][main/scala/cosas/ops/records/Conversions.scala]
            + [Get.scala][main/scala/cosas/ops/records/Get.scala]
          + typeSets
            + [Filter.scala][main/scala/cosas/ops/typeSets/Filter.scala]
            + [Reorder.scala][main/scala/cosas/ops/typeSets/Reorder.scala]
            + [Conversions.scala][main/scala/cosas/ops/typeSets/Conversions.scala]
            + [AggregateProperties.scala][main/scala/cosas/ops/typeSets/AggregateProperties.scala]
            + [Subtract.scala][main/scala/cosas/ops/typeSets/Subtract.scala]
            + [Pop.scala][main/scala/cosas/ops/typeSets/Pop.scala]
            + [Representations.scala][main/scala/cosas/ops/typeSets/Representations.scala]
            + [Replace.scala][main/scala/cosas/ops/typeSets/Replace.scala]
            + [Take.scala][main/scala/cosas/ops/typeSets/Take.scala]
            + [Union.scala][main/scala/cosas/ops/typeSets/Union.scala]
            + [Mappers.scala][main/scala/cosas/ops/typeSets/Mappers.scala]
        + [typeUnions.scala][main/scala/cosas/typeUnions.scala]
        + [records.scala][main/scala/cosas/records.scala]
        + [fns.scala][main/scala/cosas/fns.scala]
        + [propertyHolders.scala][main/scala/cosas/propertyHolders.scala]
        + [types.scala][main/scala/cosas/types.scala]

[test/scala/cosas/SubsetTypesTests.scala]: ../../../test/scala/cosas/SubsetTypesTests.scala.md
[test/scala/cosas/PropertyTests.scala]: ../../../test/scala/cosas/PropertyTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: ../../../test/scala/cosas/TypeUnionTests.scala.md
[test/scala/cosas/ScalazEquality.scala]: ../../../test/scala/cosas/ScalazEquality.scala.md
[test/scala/cosas/DenotationTests.scala]: ../../../test/scala/cosas/DenotationTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: ../../../test/scala/cosas/TypeSetTests.scala.md
[main/scala/cosas/properties.scala]: properties.scala.md
[main/scala/cosas/typeSets.scala]: typeSets.scala.md
[main/scala/cosas/ops/records/Update.scala]: ops/records/Update.scala.md
[main/scala/cosas/ops/records/Conversions.scala]: ops/records/Conversions.scala.md
[main/scala/cosas/ops/records/Get.scala]: ops/records/Get.scala.md
[main/scala/cosas/ops/typeSets/Filter.scala]: ops/typeSets/Filter.scala.md
[main/scala/cosas/ops/typeSets/Reorder.scala]: ops/typeSets/Reorder.scala.md
[main/scala/cosas/ops/typeSets/Conversions.scala]: ops/typeSets/Conversions.scala.md
[main/scala/cosas/ops/typeSets/AggregateProperties.scala]: ops/typeSets/AggregateProperties.scala.md
[main/scala/cosas/ops/typeSets/Subtract.scala]: ops/typeSets/Subtract.scala.md
[main/scala/cosas/ops/typeSets/Pop.scala]: ops/typeSets/Pop.scala.md
[main/scala/cosas/ops/typeSets/Representations.scala]: ops/typeSets/Representations.scala.md
[main/scala/cosas/ops/typeSets/Replace.scala]: ops/typeSets/Replace.scala.md
[main/scala/cosas/ops/typeSets/Take.scala]: ops/typeSets/Take.scala.md
[main/scala/cosas/ops/typeSets/Union.scala]: ops/typeSets/Union.scala.md
[main/scala/cosas/ops/typeSets/Mappers.scala]: ops/typeSets/Mappers.scala.md
[main/scala/cosas/typeUnions.scala]: typeUnions.scala.md
[main/scala/cosas/records.scala]: records.scala.md
[main/scala/cosas/fns.scala]: fns.scala.md
[main/scala/cosas/propertyHolders.scala]: propertyHolders.scala.md
[main/scala/cosas/types.scala]: types.scala.md