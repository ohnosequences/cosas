
```scala
package ohnosequences.cosas

case object types {
```

Something super-generic and ultra-abstract

```scala
  trait AnyType {

    type Raw
    val label: String

    final type Me = this.type
    implicit final val justMe: Me = this
  }

  object AnyType {

    type withRaw[V] = AnyType { type Raw = V }

    implicit def typeOps[T <: AnyType](tpe: T): TypeOps[T] = TypeOps(tpe)
  }

  class Type(val label: String) extends AnyType { type Raw = Any }
  class Wrap[R](val label: String) extends AnyType { final type Raw = R }

  type =:[V <: T#Raw, T <: AnyType] = Denotes[V,T]
  type :=[T <: AnyType, V <: T#Raw] = Denotes[V,T]

  type ValueOf[T <: AnyType] = T#Raw Denotes T
  def  valueOf[T <: AnyType, V <: T#Raw](t: T)(v: V): ValueOf[T] = v =: t

  final case class TypeOps[T <: AnyType](val tpe: T) extends AnyVal {
```

For example `user denoteWith (String, String, Int)` _not that this is a good idea_

```scala
    final def =:[@specialized V <: T#Raw](v: V): V =: T = new (V Denotes T)(v)
    final def :=[@specialized V <: T#Raw](v: V): T := V = new (V Denotes T)(v)
  }

  case object typeLabel extends shapeless.Poly1 {

    implicit def labelOf[T <: AnyType] = at[T]{ t: T => t.label }
  }
```

You denote a `Type` using a `Value`

```scala
  trait AnyDenotation extends Any {

    type Tpe <: AnyType

    type Value
    def  value: Value
  }

  case object denotationValue extends shapeless.Poly1 {

    implicit def value2[T <: AnyType, V <: T#Raw] = at[V Denotes T]( v => v.value: V )
    implicit def value[D <: AnyDenotation] = at[D](v => v.value: D#Value)
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

    final def show(implicit t: T): String = s"(${t.label} := ${value})"
  }
```


### Type parsing and serialization


```scala
  trait AnyDenotationParser {

    type Type <: AnyType
    val tpe: Type

    // the type used to denote Type
    type D <: Type#Raw

    type Value
    type From = (String, Value)

    val parser: Value => Option[D]

    val labelRep: String

    def apply(k: String, v: Value): Either[DenotationParserError, Type := D] = k match {

      case `labelRep` => parser(v).fold[Either[DenotationParserError, Type := D]](
          Left(ErrorParsingValue(tpe)(v))
        )(
          d => Right(tpe := d)
        )

      case _ => Left(WrongKey(tpe, k, labelRep))
    }
  }

  sealed trait DenotationParserError
  case class ErrorParsingValue[Tpe <: AnyType, Value](val tpe: Tpe)(val from: Value)
  extends DenotationParserError
  case class WrongKey[Tpe <: AnyType](val tpe: Tpe, val got: String, val expected: String)
  extends DenotationParserError

  class DenotationParser[T <: AnyType, D0 <: T#Raw, V](
    val tpe: T,
    val labelRep: String
  )(
    val parser: V => Option[D0]
  )
  extends AnyDenotationParser {

    type Type = T
    type Value = V
    type D = D0
  }

  case object AnyDenotationParser {

    implicit def genericParser[T <: AnyType, D <: T#Raw](implicit tpe: T): DenotationParser[T,D,D] =
      new DenotationParser(tpe, tpe.label)(d => Some(d))
  }

  trait AnyDenotationSerializer {

    type Type <: AnyType
    val tpe: Type

    // the type used to denote Type
    type D <: Type#Raw

    type Value
    type To = (String, Value)

    val serializer: D => Option[Value]

    val labelRep: String

    def apply(d: Type := D): Either[DenotationSerializerError, To] = serializer(d.value)
      .fold[Either[DenotationSerializerError, To]](
        Left(ErrorSerializingValue(d))
      )(
        v => Right(labelRep -> v)
      )
  }

  sealed trait DenotationSerializerError
  case class ErrorSerializingValue[T <: AnyType, D <: T#Raw](d: T := D) extends DenotationSerializerError

  class DenotationSerializer[T <: AnyType, D0 <: T#Raw, V](
    val tpe: T,
    val labelRep: String
  )(
    val serializer: D0 => Option[V]
  )
  extends AnyDenotationSerializer {

    type Type = T
    type D = D0
    type Value = V
  }

  case object AnyDenotationSerializer {

    implicit def genericSerializer[T <: AnyType, D <: T#Raw](implicit tpe: T): DenotationSerializer[T,D,D] =
      new DenotationSerializer(tpe, tpe.label)(d => Some(d))
  }
```


  ### Subset types

  **Warning** _this has nothing to do with the typeSets in this library!_

  The idea of subset types is that you are specifying a type `S` having as values a _subset_ of those of another `W` type; in this case this is modeled as a predicate valued on `W#Raw`.

  For more about this (and its possible uses) see

  - [Adam Chlipala CPDT - Subset types](http://adam.chlipala.net/cpdt/html/Subset.html)


```scala
  trait AnySubsetType extends AnyType {

    type W <: AnyType
    type Raw = W#Raw

    def predicate(raw: W := Raw): Boolean
  }

  trait SubsetType[W0 <: AnyType] extends AnySubsetType { type W = W0 }

  object AnySubsetType {

    implicit def getSubsetTypeOps[W <: AnyType, ST <: SubsetType[W]](st: ST): SubsetTypeOps[W,ST] =
      SubsetTypeOps(st)
    case class SubsetTypeOps[W <: AnyType, ST <: SubsetType[W]](val st: ST) extends AnyVal {

      final def apply(raw: W := W#Raw): Option[ValueOf[ST]] = {

        if ( st predicate raw ) None else Some( new ValueOf[ST](raw.value) )
      }

      final def withValue(raw: W := W#Raw): Option[ValueOf[ST]] = apply(raw)
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