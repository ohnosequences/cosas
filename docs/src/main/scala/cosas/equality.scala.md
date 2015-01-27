
```scala
package ohnosequences.cosas

object equals {
  
  @annotation.implicitNotFound( msg = 
  """
  No proof of equality found for types:  

    ${A}

    ${B}
  """)
  sealed trait ≃[A, B] { 

    type Left = A
    type Right = B
    type Out >: A with B <: A with B 

    implicit def inL(a: A): Out
    implicit def inR(b: B): Out

    final implicit def elimL(o: Out): A = o
    final implicit def elimR(o: Out): B = o

    def sym: ≃[B, A]
  }

  final case class Refl[A]() extends (A ≃ A) { 

    final type Out = A

    final implicit def inL(a: A): Out = a
    final implicit def inR(b: A): Out = b

    final def sym = this
  } 

  type ?≃[X, Y] = Either[X,Y]
  type <≃>[A, B] = (A ?≃ B) => (A ≃ B)

  implicit def refl[A >: B <: B, B]: (A <≃> B) = x => Refl[B]()
  implicit def sym[A, B](implicit p: B <≃> A): A <≃> B = x => (p(x.swap).sym)
  implicit def reflInst[B]: B ≃ B = Refl[B]()
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
        + [EqualityTests.scala][test/scala/cosas/EqualityTests.scala]
        + [DenotationTests.scala][test/scala/cosas/DenotationTests.scala]
        + [RecordTests.scala][test/scala/cosas/RecordTests.scala]
        + [TypeSetTests.scala][test/scala/cosas/TypeSetTests.scala]
  + main
    + scala
      + cosas
        + [equality.scala][main/scala/cosas/equality.scala]
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
        + [subsetTypes.scala][main/scala/cosas/subsetTypes.scala]
        + [fns.scala][main/scala/cosas/fns.scala]
        + [propertyHolders.scala][main/scala/cosas/propertyHolders.scala]
        + [types.scala][main/scala/cosas/types.scala]

[test/scala/cosas/SubsetTypesTests.scala]: ../../../test/scala/cosas/SubsetTypesTests.scala.md
[test/scala/cosas/PropertyTests.scala]: ../../../test/scala/cosas/PropertyTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: ../../../test/scala/cosas/TypeUnionTests.scala.md
[test/scala/cosas/ScalazEquality.scala]: ../../../test/scala/cosas/ScalazEquality.scala.md
[test/scala/cosas/EqualityTests.scala]: ../../../test/scala/cosas/EqualityTests.scala.md
[test/scala/cosas/DenotationTests.scala]: ../../../test/scala/cosas/DenotationTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: ../../../test/scala/cosas/TypeSetTests.scala.md
[main/scala/cosas/equality.scala]: equality.scala.md
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
[main/scala/cosas/subsetTypes.scala]: subsetTypes.scala.md
[main/scala/cosas/fns.scala]: fns.scala.md
[main/scala/cosas/propertyHolders.scala]: propertyHolders.scala.md
[main/scala/cosas/types.scala]: types.scala.md