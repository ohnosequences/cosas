package ohnosequences.cosas.records

import ohnosequences.cosas._, types._, fns._, records._, typeSets._

// TODO add it once DepFn3 is there
// case object Transform extends DepFn3[ValueOf[RT], Other, ... ]{
//
//   implicit def default[
//     RT <: AnyRecord,
//     Other <: AnyRecord,
//     Rest <: AnyTypeSet,
//     Uni <: AnyTypeSet,
//     Missing <: AnyTypeSet
//   ](implicit
//     missing: App2[subtract, Other#Raw, RT#Raw, Missing],
//     allMissing: Rest ~:~ Missing,
//     uni: App2[union, RT#Raw, Rest, Uni], //(RT#Raw ∪ Rest) { type Out = Uni },
//     project: App2[take[Uni],Other#Raw,Uni]
//   )
//   : App3[Transform.type, RT, Other, Rest, ValueOf[Other]] =
//     App3 { (recRaw: RT#Raw, other: Other, rest: Rest) => other := project(uni(recRaw, rest)) }
// }
