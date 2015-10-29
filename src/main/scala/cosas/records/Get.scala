package ohnosequences.cosas.records

import ohnosequences.cosas._, types._, typeSets._, properties._, fns._

class Get[P <: AnyType, R <: AnyRecord] extends DepFn1[ValueOf[R], ValueOf[P]]

case object Get {

  implicit def default[P0 <: AnyType, R0 <: AnyRecord]
  (implicit
    look: App1[Lookup[ValueOf[P0]], R0#Raw, ValueOf[P0]]
  )
  : App1[get[P0,R0], ValueOf[R0], ValueOf[P0]] =
    App1 { rec: ValueOf[R0] => look(rec.value) }
}
