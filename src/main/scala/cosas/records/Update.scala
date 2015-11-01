// package ohnosequences.cosas.records
//
// import ohnosequences.cosas._, types._, typeSets._, properties._, fns._
//
// class Update[RT <: AnyRecord] extends DepFn2[ValueOf[RT], AnyTypeSet, ValueOf[RT]]
//
// case object Update {
//
//   implicit def update[RT <: AnyRecord, Ps <: AnyTypeSet]
//   (implicit
//     check: Ps ⊂ RT#Raw,
//     replace: App2[replace[RT#Raw], RT#Raw, Ps, RT#Raw]
//   )
//   : App2[Update[RT], ValueOf[RT], Ps, ValueOf[RT]] =
//     App2 { (rec: ValueOf[RT], propReps: Ps) => new (RT#Raw Denotes RT)( replace(rec.value: RT#Raw, propReps) ) }
// }
