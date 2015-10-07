package ohnosequences.cosas

import ohnosequences.cosas.types._
import ohnosequences.cosas.properties._

package object records {

  type get[P <: AnyProperty, R <: AnyRecord] = Get[P,R]
  def get[P <: AnyProperty, R <: AnyRecord]: get[P,R] = new Get[P,R]

  type update[RT <: AnyRecord] = Update[RT]
  def update[RT <: AnyRecord]: update[RT] = new Update[RT]

  type transform[RT <: AnyRecord, Other <: AnyRecord] = Transform[RT,Other]
  def transform[RT <: AnyRecord, Other <: AnyRecord]: transform[RT,Other] = new Transform[RT,Other]

  implicit def recordSyntax[RT <: AnyRecord](recType: RT): syntax.RecordSyntax[RT] =
    syntax.RecordSyntax(recType)

  implicit def recordEntrySyntax[RT <: AnyRecord](entry: ValueOf[RT]): syntax.RecordEntrySyntax[RT] =
    syntax.RecordEntrySyntax(entry.value)
}
