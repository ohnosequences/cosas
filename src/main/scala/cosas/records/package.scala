package ohnosequences.cosas

import ohnosequences.cosas._, types._

package object records {

  type EmptyRecordType = AnyRecordType { type Keys = unit }

  // type get[P <: AnyType, R <: AnyRecord] = Get[P,R]
  // def get[P <: AnyType, R <: AnyRecord]: get[P,R] = new Get[P,R]
  //
  // type update[RT <: AnyRecord] = Update[RT]
  // def update[RT <: AnyRecord]: update[RT] = new Update[RT]
  //
  // type transform[RT <: AnyRecord, Other <: AnyRecord] = Transform[RT,Other]
  // def transform[RT <: AnyRecord, Other <: AnyRecord]: transform[RT,Other] = new Transform[RT,Other]
}
