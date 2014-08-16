// package ohnosequences.typesets.syntax

// import ohnosequences.typesets.{ Representable, AnyTag, AnyProperty } 
// import AnyTag._

// trait RecordSyntax {

//   // dependencies
//   type TypeSetSyntax <: Types
//   val typeSetSyntax: TypeSetSyntax
//   import typeSetSyntax._

//   type Record <: Representable { 

//     type Properties <: typeSetSyntax.TypeSet
//     type Raw <: typeSetSyntax.TypeSet
//   }

//   type PropertiesOf[R <: Record] <: TypeSet

//   // implicit def to
// }

// trait RecordOps {

//   type MySyntax <: RecordSyntax
//   val syntax: MySyntax

//   import syntax._
//   import syntax.typeSetSyntax._

//   type R <: Record
//   val entry: RepOf[R]

//   def get[P <: AnyProperty](p: P)
//     (implicit 
//       isThere: P ∈ PropertiesOf[R],
//       lookup: FirstOf[RawOf[R], RepOf[P]]
//     )
//     : lookup.Out
// }
