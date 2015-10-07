
```scala
package ohnosequences.cosas.ops.typeSets

import ohnosequences.cosas._, types._, typeSets._, fns._

@annotation.implicitNotFound(msg = """
  Cannot serialize typeset of denotations
    ${Denotations}
  to a map of type
    Map[String, ${V}]

  Probably some denotation serializers are missing.
""")
trait SerializeDenotations[Denotations <: AnyTypeSet, V]
extends Fn2[Denotations, Map[String,V]] with
        Out[Either[SerializeDenotationsError, Map[String,V]]] {

  final def apply(d: Denotations): Out = apply(d, Map[String,V]())
}

// errors should be named with the same name + error
trait SerializeDenotationsError
case class KeyPresent[V](val key: String, val map: Map[String,V]) extends SerializeDenotationsError
case class ErrorSerializing[SE <: DenotationSerializerError](val err: SE) extends SerializeDenotationsError

case object SerializeDenotations {

  implicit def atEmpty[V]: SerializeDenotations[∅,V] = new SerializeDenotations[∅,V] {

    def apply(nil: ∅, map: Map[String,V]): Out = Right(map)
  }

  implicit def atCons[
    V,
    H <: AnyType, TD <: AnyTypeSet,
    HR <: H#Raw,
    SH <: AnyDenotationSerializer { type Type = H; type Value = V; type D = HR },
    ST <: SerializeDenotations[TD,V]
  ](implicit
    serializeH: SH,
    serializeT: ST
  ): SerializeDenotations[(H := HR) :~: TD, V] = new SerializeDenotations[(H := HR) :~: TD, V] {

    def apply(denotations: (H := HR) :~: TD, map: Map[String,V]): Out = {

      serializeH(denotations.head).fold(
        l => Left(ErrorSerializing(l)),
        kv => (map get kv._1) match {
          case Some(_)  => Left(KeyPresent(kv._1, map))
          case None     => serializeT(denotations.tail, map + kv)
        }
      )
    }
  }
}

```




[test/scala/cosas/asserts.scala]: ../../../../../test/scala/cosas/asserts.scala.md
[test/scala/cosas/DenotationTests.scala]: ../../../../../test/scala/cosas/DenotationTests.scala.md
[test/scala/cosas/SubsetTypesTests.scala]: ../../../../../test/scala/cosas/SubsetTypesTests.scala.md
[test/scala/cosas/EqualityTests.scala]: ../../../../../test/scala/cosas/EqualityTests.scala.md
[test/scala/cosas/PropertyTests.scala]: ../../../../../test/scala/cosas/PropertyTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: ../../../../../test/scala/cosas/TypeSetTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: ../../../../../test/scala/cosas/TypeUnionTests.scala.md
[main/scala/cosas/typeUnions.scala]: ../../typeUnions.scala.md
[main/scala/cosas/properties.scala]: ../../properties.scala.md
[main/scala/cosas/records.scala]: ../../records.scala.md
[main/scala/cosas/fns.scala]: ../../fns.scala.md
[main/scala/cosas/types.scala]: ../../types.scala.md
[main/scala/cosas/typeSets.scala]: ../../typeSets.scala.md
[main/scala/cosas/ops/records/Update.scala]: ../records/Update.scala.md
[main/scala/cosas/ops/records/Transform.scala]: ../records/Transform.scala.md
[main/scala/cosas/ops/records/Get.scala]: ../records/Get.scala.md
[main/scala/cosas/ops/typeSets/SerializeDenotations.scala]: SerializeDenotations.scala.md
[main/scala/cosas/ops/typeSets/ParseDenotations.scala]: ParseDenotations.scala.md
[main/scala/cosas/ops/typeSets/Conversions.scala]: Conversions.scala.md
[main/scala/cosas/ops/typeSets/Filter.scala]: Filter.scala.md
[main/scala/cosas/ops/typeSets/Subtract.scala]: Subtract.scala.md
[main/scala/cosas/ops/typeSets/Mappers.scala]: Mappers.scala.md
[main/scala/cosas/ops/typeSets/Union.scala]: Union.scala.md
[main/scala/cosas/ops/typeSets/Reorder.scala]: Reorder.scala.md
[main/scala/cosas/ops/typeSets/Take.scala]: Take.scala.md
[main/scala/cosas/ops/typeSets/Representations.scala]: Representations.scala.md
[main/scala/cosas/ops/typeSets/Pop.scala]: Pop.scala.md
[main/scala/cosas/ops/typeSets/Replace.scala]: Replace.scala.md
[main/scala/cosas/equality.scala]: ../../equality.scala.md