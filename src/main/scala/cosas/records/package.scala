package ohnosequences.cosas

import types._, klists._

package object records {

  type EmptyRecordType = AnyRecordType.withKeys[|[AnyType]]

  implicit def recordReorderSyntax[Vs <: AnyKList.withBound[AnyDenotation]](vs: Vs)
  : syntax.RecordReorderSyntax[Vs] =
    syntax.RecordReorderSyntax(vs)
}
