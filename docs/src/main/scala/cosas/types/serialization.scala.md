
```scala
package ohnosequences.cosas.types

import ohnosequences.cosas._, fns._, klists._

// TODO move to DepFns
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

// errors should be named with the same name + error
trait SerializeDenotationsError
case class KeyPresent[V](val key: String, val map: Map[String,V]) extends SerializeDenotationsError
case class ErrorSerializing[SE <: DenotationSerializerError](val err: SE) extends SerializeDenotationsError

class SerializeDenotations[V, Denotations <: AnyKList.withBound[AnyDenotation]] extends DepFn2[
  Map[String,V], Denotations,
  Either[SerializeDenotationsError, Map[String,V]]
]

case object SerializeDenotations {

  implicit def atEmpty[V]
  : AnyApp2At[SerializeDenotations[V,*[AnyDenotation]], Map[String,V], *[AnyDenotation]] { type Y = Either[SerializeDenotationsError, Map[String,V]] } =
    App2 { (map: Map[String,V], nil: *[AnyDenotation]) => Right(map): Either[SerializeDenotationsError, Map[String,V]] }

  implicit def atcons[
    V,
    H <: AnyType, TD <: AnyKList.withBound[AnyDenotation],
    HR <: H#Raw
  ](implicit
    serializeH: DenotationSerializer[H,HR,V],
    serializeT: AnyApp2At[SerializeDenotations[V,TD], Map[String,V], TD] { type Y = Either[SerializeDenotationsError, Map[String,V]] }
  )
  : AnyApp2At[
      SerializeDenotations[V,(H := HR) :: TD],
      Map[String,V], (H := HR) :: TD
    ] { type Y = Either[SerializeDenotationsError, Map[String,V]] } =
  App2 { (map: Map[String,V], denotations: (H := HR) :: TD) => serializeH(denotations.head).fold(
      l => Left(ErrorSerializing(l)),
      kv => (map get kv._1) match {
        case Some(_)  => Left(KeyPresent(kv._1, map))
        case None     => serializeT(map + kv, denotations.tail)
      }
    )
  }
}

```




[test/scala/cosas/DenotationTests.scala]: ../../../../test/scala/cosas/DenotationTests.scala.md
[test/scala/cosas/EqualityTests.scala]: ../../../../test/scala/cosas/EqualityTests.scala.md
[test/scala/cosas/DependentFunctionsTests.scala]: ../../../../test/scala/cosas/DependentFunctionsTests.scala.md
[test/scala/cosas/KListsTests.scala]: ../../../../test/scala/cosas/KListsTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/NatTests.scala]: ../../../../test/scala/cosas/NatTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: ../../../../test/scala/cosas/TypeUnionTests.scala.md
[main/scala/cosas/package.scala]: ../package.scala.md
[main/scala/cosas/types/package.scala]: package.scala.md
[main/scala/cosas/types/types.scala]: types.scala.md
[main/scala/cosas/types/parsing.scala]: parsing.scala.md
[main/scala/cosas/types/productTypes.scala]: productTypes.scala.md
[main/scala/cosas/types/syntax.scala]: syntax.scala.md
[main/scala/cosas/types/project.scala]: project.scala.md
[main/scala/cosas/types/denotations.scala]: denotations.scala.md
[main/scala/cosas/types/functionTypes.scala]: functionTypes.scala.md
[main/scala/cosas/types/serialization.scala]: serialization.scala.md
[main/scala/cosas/klists/replace.scala]: ../klists/replace.scala.md
[main/scala/cosas/klists/cons.scala]: ../klists/cons.scala.md
[main/scala/cosas/klists/klists.scala]: ../klists/klists.scala.md
[main/scala/cosas/klists/take.scala]: ../klists/take.scala.md
[main/scala/cosas/klists/package.scala]: ../klists/package.scala.md
[main/scala/cosas/klists/takeFirst.scala]: ../klists/takeFirst.scala.md
[main/scala/cosas/klists/toList.scala]: ../klists/toList.scala.md
[main/scala/cosas/klists/filter.scala]: ../klists/filter.scala.md
[main/scala/cosas/klists/pick.scala]: ../klists/pick.scala.md
[main/scala/cosas/klists/drop.scala]: ../klists/drop.scala.md
[main/scala/cosas/klists/map.scala]: ../klists/map.scala.md
[main/scala/cosas/klists/at.scala]: ../klists/at.scala.md
[main/scala/cosas/klists/syntax.scala]: ../klists/syntax.scala.md
[main/scala/cosas/klists/fold.scala]: ../klists/fold.scala.md
[main/scala/cosas/klists/noDuplicates.scala]: ../klists/noDuplicates.scala.md
[main/scala/cosas/klists/slice.scala]: ../klists/slice.scala.md
[main/scala/cosas/klists/find.scala]: ../klists/find.scala.md
[main/scala/cosas/records/package.scala]: ../records/package.scala.md
[main/scala/cosas/records/recordTypes.scala]: ../records/recordTypes.scala.md
[main/scala/cosas/records/syntax.scala]: ../records/syntax.scala.md
[main/scala/cosas/records/reorder.scala]: ../records/reorder.scala.md
[main/scala/cosas/typeUnions/typeUnions.scala]: ../typeUnions/typeUnions.scala.md
[main/scala/cosas/typeUnions/package.scala]: ../typeUnions/package.scala.md
[main/scala/cosas/fns/predicates.scala]: ../fns/predicates.scala.md
[main/scala/cosas/fns/instances.scala]: ../fns/instances.scala.md
[main/scala/cosas/fns/package.scala]: ../fns/package.scala.md
[main/scala/cosas/fns/syntax.scala]: ../fns/syntax.scala.md
[main/scala/cosas/fns/functions.scala]: ../fns/functions.scala.md
[main/scala/cosas/subtyping.scala]: ../subtyping.scala.md
[main/scala/cosas/witness.scala]: ../witness.scala.md
[main/scala/cosas/equality.scala]: ../equality.scala.md
[main/scala/cosas/Nat.scala]: ../Nat.scala.md
[main/scala/cosas/Bool.scala]: ../Bool.scala.md