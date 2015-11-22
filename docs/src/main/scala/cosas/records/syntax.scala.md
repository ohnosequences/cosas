
```scala
package ohnosequences.cosas.records

import ohnosequences.cosas._, types._, fns._, klists._

case object syntax {

  final case class RecordTypeDenotationSyntax[RT <: AnyRecordType, Vs <: RT#Raw](val vs: Vs) extends AnyVal {

    def get[D <: AnyDenotationOf[T], T <: AnyType](tpe: T)(implicit
      p: AnyApp1At[FindS[AnyDenotationOf[T]], Vs] { type Y = D }
    ): D = p(vs)

    def getV[D <: AnyDenotationOf[T], T <: AnyType](tpe: T)(implicit
      p: AnyApp1At[FindS[AnyDenotationOf[T]], Vs] { type Y = D }
    ): D#Value = p(vs).value

    def update[S <: AnyKList.withBound[AnyDenotation]](s: S)(implicit
      repl: AnyApp2At[replace[Vs], Vs, S] { type Y = Vs }
    ):   RT := Vs =
    new (RT := Vs)(repl(vs,s))

    def update[N <: AnyDenotation](n: N)(implicit
      repl: AnyApp2At[replace[Vs], Vs, N :: *[AnyDenotation]] { type Y = Vs }
    ):   RT := Vs =
    new (RT := Vs)(repl(vs, n :: *[AnyDenotation]))

    // TODO see if this is really needed
    // def as[QT <: AnyRecordType, QTRaw <: QT#Raw](qt: QT)(implicit
    //   takeFirst: AnyApp1At[takeFirst[QT#Raw], Vs] { type Y = QTRaw }
    // ): QT := QTRaw =
    //    qt := (takeFirst(vs): QTRaw)

    def toProduct: RT#Keys := Vs = new (RT#Keys := Vs)(vs)

    def serialize[V](implicit
      serialize: AnyApp2At[V SerializeDenotations Vs, Map[String,V], Vs] {
        type Y = Either[SerializeDenotationsError, Map[String,V]]
      }
    ): Either[SerializeDenotationsError, Map[String,V]] = serialize(Map[String,V](), vs)

    def serializeUsing[V](map: Map[String,V])(implicit
      serialize: AnyApp2At[V SerializeDenotations Vs, Map[String,V], Vs] {
        type Y = Either[SerializeDenotationsError, Map[String,V]]
      }
    ): Either[SerializeDenotationsError, Map[String,V]] = serialize(map, vs)
  }

  final case class RecordTypeSyntax[RT <: AnyRecordType](val rt: RT) extends AnyVal {

    // TODO was this ever used?
    // def reorder[Vs <: AnyKList.withBound[AnyDenotation], RTRaw <: RT#Raw](values: Vs)(implicit
    //   takeFirst: AnyApp1At[takeFirst[Vs], Vs] { type Y = RTRaw }
    // ): RT := RTRaw =
    //    rt := (takeFirst(values): RTRaw)

    def from[Vs <: AnyKList.withBound[AnyDenotation], RTRaw <: RT#Raw](vs: Vs)(implicit
      reorder: AnyApp1At[Reorder[RT#Keys, Vs], Vs] { type Y = RTRaw }
    )
    : RT := RTRaw =
      rt := (reorder(vs): RTRaw)

    def parse[RTRaw <: RT#Raw, V](map: Map[String,V])(implicit
      parse: AnyApp1At[V ParseDenotations RT#Keys, Map[String,V]] {
        type Y = Either[ParseDenotationsError, RTRaw]
      }
    ): Either[ParseDenotationsError, RT := RTRaw] =
      parse(map).fold[Either[ParseDenotationsError, RT := RTRaw]](
        { err => Left(err) },
        { vs => Right(rt := vs) }
      )
  }

  final case class RecordReorderSyntax[Vs <: AnyKList.withBound[AnyDenotation]](val vs: Vs) extends AnyVal {

    def as[RT <: AnyRecordType, RTRaw <: RT#Raw](rt: RT)(implicit
      takeFirst: AnyApp1At[Reorder[RT#Keys, Vs], Vs] { type Y = RTRaw }
    ): RT := RTRaw =
       rt := (takeFirst(vs): RTRaw)
  }
}

```




[test/scala/cosas/asserts.scala]: ../../../../test/scala/cosas/asserts.scala.md
[test/scala/cosas/DenotationTests.scala]: ../../../../test/scala/cosas/DenotationTests.scala.md
[test/scala/cosas/EqualityTests.scala]: ../../../../test/scala/cosas/EqualityTests.scala.md
[test/scala/cosas/DependentFunctionsTests.scala]: ../../../../test/scala/cosas/DependentFunctionsTests.scala.md
[test/scala/cosas/KListsTests.scala]: ../../../../test/scala/cosas/KListsTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/NatTests.scala]: ../../../../test/scala/cosas/NatTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: ../../../../test/scala/cosas/TypeUnionTests.scala.md
[main/scala/cosas/package.scala]: ../package.scala.md
[main/scala/cosas/types/package.scala]: ../types/package.scala.md
[main/scala/cosas/types/types.scala]: ../types/types.scala.md
[main/scala/cosas/types/parsing.scala]: ../types/parsing.scala.md
[main/scala/cosas/types/productTypes.scala]: ../types/productTypes.scala.md
[main/scala/cosas/types/syntax.scala]: ../types/syntax.scala.md
[main/scala/cosas/types/project.scala]: ../types/project.scala.md
[main/scala/cosas/types/denotations.scala]: ../types/denotations.scala.md
[main/scala/cosas/types/functionTypes.scala]: ../types/functionTypes.scala.md
[main/scala/cosas/types/serialization.scala]: ../types/serialization.scala.md
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
[main/scala/cosas/records/package.scala]: package.scala.md
[main/scala/cosas/records/recordTypes.scala]: recordTypes.scala.md
[main/scala/cosas/records/syntax.scala]: syntax.scala.md
[main/scala/cosas/records/reorder.scala]: reorder.scala.md
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