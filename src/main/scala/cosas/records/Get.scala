package ohnosequences.cosas.records

import ohnosequences.cosas._, types._, records._, properties._, fns._, typeSets.Lookup

class get[P <: AnyProperty, R <: AnyRecord] extends DepFn1[ValueOf[R], ValueOf[P]] {

  // TODO Lookup is a function
  implicit def getter[P <: AnyProperty, R <: AnyRecord]
  (implicit
    lookup: R#Raw Lookup ValueOf[P]
  ): App1[get[P,R], ValueOf[R], ValueOf[P]] =
    App1 { rec: ValueOf[R] => lookup(rec.value) }
}
