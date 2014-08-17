// package ohnosequences.typesets.ops

// import ohnosequences.typesets._, AnyTag._

// import ohnosequences.typesets.syntax.AnyRecordSyntax

// object RecordDefault {
  

//   case class PropertyOps[R <: AnyRecord](val entry: RepOf[R], val record: R)
//   extends AnyRecordSyntax {

//     type Record = R

//     def getIt[P <: Singleton with AnyProperty](p: P)
//       (implicit 
//         isThere: P ∈ Record#Properties
//       ): P#Rep = { implicitly[Lookup[Record#Raw, P#Rep]]; getItImpl(p) }

//     def getItImpl[P <: Singleton with AnyProperty](p: P)
//       (implicit 
//         isThere: P ∈ Record#Properties,
//         lookup: Lookup[Record#Raw, P#Rep]
//       ): P#Rep = lookup(entry)

//     def get[P <: Singleton with AnyProperty](p: P)
//       (implicit 
//         isThere: P ∈ R#Properties,
//         lookup: Lookup[R#Raw, P#Rep]
//       ): P#Rep = lookup(entry)


//     def update[P <: SingletonOf[AnyProperty], S <: TypeSet](pEntry: P#Rep)
//       (implicit 
//         isThere: P ∈ R#Properties,
//         replace: Replace[RepOf[R], (P#Rep :~: ∅)]
//       ): RepOf[R] = {

//         replace(entry, pEntry :~: ∅)
//       }

//     def update[Ps <: TypeSet, S <: TypeSet](pEntries: Ps)
//       (implicit 
//         check: Ps ⊂ RepOf[R],
//         replace: Replace[RepOf[R], Ps]
//       ): RepOf[R] = {
        
//         replace( entry , pEntries )
//       }


//     def as[Other <: Singleton with AnyRecord](other: Other)(implicit
//       project: Choose[R#Raw, Other#Raw]
//     ): Other#Rep = (other:Other) ->> project(entry)

//     def as[Other <: Singleton with AnyRecord, Rest <: TypeSet, Uni <: TypeSet, Missing <: TypeSet](other: Other, rest: Rest)
//       (implicit
//         missing: (Other#Raw \ R#Raw) { type Out = Missing },
//         allMissing: Rest ~ Missing,
//         uni: (R#Raw ∪ Rest) { type Out = Uni },
//         project: Choose[Uni, Other#Raw]
//       ): Other#Rep = (other:Other) ->> project(uni(entry, rest))

//   }
// }
