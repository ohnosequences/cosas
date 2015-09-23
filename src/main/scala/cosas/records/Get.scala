package ohnosequences.cosas.records

import ohnosequences.cosas._, types._, records._, properties._, fns._

class get[P <: AnyProperty, R <: AnyRecord] extends DepFn1[ValueOf[R], ValueOf[P]]

case object get {

  // implicit def getter[P0 <: AnyProperty, R0 <: AnyRecord]
  // (implicit
  //   lookup: R#Raw Lookup ValueOf[P]
  // ): App1[get[P0,R0], ValueOf[R0], ValueOf[P0]] =
  //   App1 { rec: ValueOf[R] => lookup(rec.value) }
}
