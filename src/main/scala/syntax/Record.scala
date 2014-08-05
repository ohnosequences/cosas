// package ohnosequences.typesets.syntax

// import ohnosequences.typesets._, AnyTag._

// trait AnyRecordSyntax {

//   type Record <: AnyRecord
//   val entry: TaggedWith[Record]
//   val record: Record

//   // TODO remove this
//   def getIt[P <: Singleton with AnyProperty](p: P)
//   (implicit 
//     isThere: P ∈ Record#Properties
//   )
//   : P#Rep

//   def get[P <: Singleton with AnyProperty](p: P)
//   (implicit 
//     isThere: P ∈ Record#Properties
//   )
//   : P#Rep

//   def update[P <: SingletonOf[AnyProperty], S <: TypeSet](pEntry: P#Rep)
//   (implicit 
//     isThere: P ∈ Record#Properties
//   )
//   : TaggedWith[Record]

//   def update[Ps <: TypeSet, S <: TypeSet](pEntries: Ps)
//   (implicit 
//     check: Ps ⊂ TaggedWith[Record]
//   )
//   : TaggedWith[Record]


//   def as[Other <: Singleton with AnyRecord](other: Other)
//   (implicit
//     project: Choose[Record#Raw, Other#Raw]
//   )
//   : Other#Rep

//   def as [
//     Other <: Singleton with AnyRecord,
//     Rest <: TypeSet,
//     Uni <: TypeSet,
//     Missing <: TypeSet
//   ]
//   (
//     other: Other, 
//     rest: Rest
//   )
//   (implicit
//     missing: (Other#Raw \ Record#Raw) { type Out = Missing },
//     allMissing: Rest ~ Missing,
//     uni: (Record#Raw ∪ Rest) { type Out = Uni },
//     project: Choose[Uni, Other#Raw]
//   )
//   : Other#Rep

// }