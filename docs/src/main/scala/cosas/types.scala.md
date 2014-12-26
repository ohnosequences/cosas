
```scala
package ohnosequences.cosas

object types {
```

Something super-generic and ultra-abstract

```scala
  trait AnyType {

    type Raw
    val label: String

    final type Me = this.type

    implicit final def meFrom[D <: AnyDenotationOf[Me]](v: D): Me = this
    implicit final def meFrom2[D <: AnyDenotationOf[Me]]: Me = this
    // implicit def meFrom[V](v: V Denotes Me): this.type = this
  }

  object AnyType {

    type withRaw[R] = AnyType { type Raw = R }
    
    implicit def typeOps[T <: AnyType](tpe: T): TypeOps[T] = TypeOps(tpe)
  }

  class Type(val label: String) extends AnyType { type Raw = Any }
  class Wrap[R](val label: String) extends AnyType { final type Raw = R }
```

You denote a `Type` using a `Value`

```scala
  sealed trait AnyDenotation extends Any {

    type Tpe <: AnyType

    type Value
    def  value: Value
  }
```

Bound the denoted type

```scala
  trait AnyDenotationOf[T <: AnyType] extends Any with AnyDenotation { type Tpe = T }

  // TODO: who knows what's going on here wrt specialization (http://axel22.github.io/2013/11/03/specialization-quirks.html)
  trait AnyDenotes[@specialized V, T <: AnyType] extends Any with AnyDenotationOf[T] {
    
    final type Value = V
  }
```

Denote T with a `value: V`. Normally you write it as `V Denotes T` thus the name.

```scala
  // NOTE: most likely V won't be specialized here
  final class Denotes[V, T <: AnyType](val value: V) extends AnyVal with AnyDenotes[V, T] {

    final def show(implicit t: T): String = s"${t.label} := ${value}"
  }

  type =:[V, T <: AnyType] = Denotes[V,T]
  type :=[T <: AnyType, V] = Denotes[V,T]

  type ValueOf[T <: AnyType] = T#Raw Denotes T
  def  valueOf[T <: AnyType, V <: T#Raw](t: T)(v: V): ValueOf[T] = v =: t

  final case class TypeOps[T <: AnyType](val tpe: T) extends AnyVal {
```

For example `user denoteWith (String, String, Int)` _not that this is a good idea_

```scala
    final def =:[@specialized V](v: V): V =: T = new (V Denotes T)(v)
    final def :=[@specialized V](v: V): V =: T = new (V Denotes T)(v)
  }


  trait AnySubsetType extends AnyType {

    type W <: AnyType
    type Raw = W#Raw

    def predicate(raw: W#Raw): Boolean
  }

  trait SubsetType[W0 <: AnyType] extends AnySubsetType { type W = W0 }

  object AnySubsetType {

    implicit def sstops[W <: AnyType, ST <: SubsetType[W]](st: ST): SubSetTypeOps[W,ST] = new SubSetTypeOps(st)
    class SubSetTypeOps[W <: AnyType, ST <: SubsetType[W]](val st: ST) extends AnyVal {

      final def apply(raw: ST#W#Raw): Option[ValueOf[ST]] = {

        if ( st predicate raw ) None else Some( new ValueOf[ST](raw) )
      }
      
      final def withValue(raw: ST#Raw): Option[ValueOf[ST]] = apply(raw)
    }

    object ValueOfSubsetTypeOps {

      implicit def ValueOfSubsetTypeOps[
        W <: AnyType,
        ST <: SubsetType[W],
        Ops <: ValueOfSubsetTypeOps[W,ST]
      ](v: ValueOf[ST])(implicit conv: ValueOf[ST] => Ops): Ops = conv(v)

    }
```

you should implement this trait for providing ops for values of a subset type `ST`.

```scala
    trait ValueOfSubsetTypeOps[W <: AnyType, ST <: SubsetType[W]] extends Any {
```

use case: concat of sized has the sum of the two arg sizes; but how do you create the corresponding value saving a stupid check (and returning an Option)? `unsafeValueOf`. By implementing this trait you assume the responsibility that comes with being able to create unchecked values of `ST`; use it with caution!

```scala
      protected final def unsafeValueOf[ST0 <: ST](other: ST#Raw): ValueOf[ST] = new ValueOf[ST](other)
    }
  }
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
        + csv
          + [csv.scala][main/scala/cosas/csv/csv.scala]
        + [fns.scala][main/scala/cosas/fns.scala]
        + [propertyHolders.scala][main/scala/cosas/propertyHolders.scala]
        + [types.scala][main/scala/cosas/types.scala]

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
[main/scala/cosas/csv/csv.scala]: csv/csv.scala.md
[main/scala/cosas/fns.scala]: fns.scala.md
[main/scala/cosas/propertyHolders.scala]: propertyHolders.scala.md
[main/scala/cosas/types.scala]: types.scala.md