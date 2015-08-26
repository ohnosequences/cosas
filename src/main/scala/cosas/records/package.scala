package ohnosequences.cosas

import ohnosequences.cosas.types._

package object records {

  implicit def recordOps[RT <: AnyRecord](recType: RT): syntax.RecordOps[RT] =
    syntax.RecordOps(recType)

  implicit def recordEntryOps[RT <: AnyRecord](entry: ValueOf[RT]): syntax.RecordEntryOps[RT] =
    syntax.RecordEntryOps(entry.value)
}
