package ohnosequences.cosas

import ohnosequences.cosas.types._

package object records {

  implicit def recordSyntax[RT <: AnyRecord](recType: RT): syntax.RecordSyntax[RT] =
    syntax.RecordSyntax(recType)

  implicit def recordEntrySyntax[RT <: AnyRecord](entry: ValueOf[RT]): syntax.RecordEntrySyntax[RT] =
    syntax.RecordEntrySyntax(entry.value)
}
