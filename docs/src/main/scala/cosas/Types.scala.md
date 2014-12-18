
```scala
package ohnosequences.cosas
```

Something super-generic and ultra-abstract

```scala
trait AnyType {

  type Raw
  val label: String

  final type Me = this.type

  implicit final def meFrom[D <: AnyDenotationOf[Me]](v: D): Me = this
}

object AnyType {

  type :%:[V <: T#Raw, T <: AnyType] = Denotes[V,T]
  type withRaw[R] = AnyType { type Raw = R }
  type RawOf[W <: AnyType] = W#Raw

  type ValueOf[T <: AnyType] = Denotes[T#Raw, T]
  
  implicit def denotationOps[T <: AnyType](tpe: T): DenotationOps[T] = DenotationOps(tpe)
}

class Type(val label: String) extends AnyType { type Raw = Any }
class Wrap[R](val label: String) extends AnyType { final type Raw = R }

// TODO who knows what's going on here wrt specialization
// http://axel22.github.io/2013/11/03/specialization-quirks.html

```


You denote a `Type` using a `Value`


```scala
sealed trait AnyDenotation extends Any {

  type Tpe <: AnyType

  type Value
  val  value: Value
}
```


Bound the denoted type


```scala
trait AnyDenotationOf[T <: AnyType] extends Any with AnyDenotation { type Tpe = T }

trait AnyDenotes[@specialized V, T <: AnyType] extends Any with AnyDenotationOf[T] {
  
  final type Value = V
}
```


Denote T with a `value: V`. Normally you write it as `V Denotes T` thus the name.


```scala
// most likely V won't be specialized here
final class Denotes[V, T <: AnyType](val value: V) extends AnyVal with AnyDenotes[V, T] {}

final case class DenotationOps[T <: AnyType](val tpe: T) extends AnyVal {
```


  For example `user denoteWith (String, String, Int)` _not that this is a good idea_


```scala
  final def denoteWith[@specialized V](v: V): (V Denotes T) = new Denotes(v)

  final def valueOf[@specialized V <: T#Raw](v: V): AnyType.ValueOf[T] = new Denotes(v)
```


  Alternative syntax, suggesting something like type ascription: `"12d655xr9" :%: id`.


```scala
  final def :%:[@specialized V <: T#Raw](v: V): (V Denotes T) = new Denotes(v)
}

object Denotes {

  type :%:[V <: T#Raw, T <: AnyType] = Denotes[V,T]
  
  implicit def denotationOps[T <: AnyType](tpe: T): DenotationOps[T] = DenotationOps(tpe)

  // implicit def eqForDenotes[V <: T#Raw, T <: AnyType]: 
  //       scalaz.Equal[V Denotes T] =
  //   new scalaz.Equal[V Denotes T] {
  //     def equal(a1: V Denotes T, a2: V Denotes T): Boolean = a1.value == a2.value
  //   }
}

trait AnySubsetType extends AnyType {

  type W <: AnyType
  type Raw = W#Raw
  def predicate(raw: W#Raw): Boolean
}

trait SubsetType[W0 <: AnyType] extends AnySubsetType { type W = W0 }

object AnySubsetType {

  import AnyType._

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