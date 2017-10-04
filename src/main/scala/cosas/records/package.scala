package ohnosequences.cosas

import types._, klists._, typeUnions._

package object records {

  type EmptyRecordType = AnyRecordType.withKeys[|[AnyType]]

  implicit def recordReorderSyntax[Vs <: AnyKList.withBound[AnyDenotation]](vs: Vs)
  : syntax.RecordReorderSyntax[Vs] =
    syntax.RecordReorderSyntax(vs)

  @annotation.implicitNotFound(msg = "Can't prove that record type ${R} has key ${K}")
  type   hasKey[R <: AnyRecordType, K <: AnyType] = K isOneOf R#Keys#Types#AllTypes

  @annotation.implicitNotFound(msg = "Can't prove that record type ${R} has no key ${K}")
  type hasNoKey[R <: AnyRecordType, K <: AnyType] = K isNotOneOf R#Keys#Types#AllTypes

}
