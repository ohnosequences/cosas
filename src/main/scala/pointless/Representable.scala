package ohnosequences.pointless

/*
  Tagging is used for being able to operate on `Raw` values knowing what they are representing; `Rep` is just `Raw` tagged with the `.type` of this representation. So, summarizing

  - `Raw` is the type used to the represent `this.type`
  - `Rep <: Raw` is just `Raw` tagged with `this.type`; the `Rep`resentation
*/

trait AnyRepresentable { me => 

  type Me = me.type

  type Raw

  /*
    `Raw` tagged with `self.type`; this lets you recognize a denotation while being able to operate on it as `Raw`.
  */
  type Rep <: Raw
}

object AnyRepresentable {

  type RawOf[D <: AnyRepresentable] = D#Raw
  type RepOf[D <: AnyRepresentable] = D#Rep

  abstract class Ops[R <: AnyRepresentable](val representable: AnyRepresentable) {

    def =>>[U <: RawOf[R]](raw: U): RepOf[R] 
  }
}


// /*
//   This trait represents a mapping between 

//   - members `Tpe` of a universe of types `TYPE`
//   - and `Raw` a type meant to be a denotation of `Tpe` thus the name

// */
// trait AnyDenotation extends Representable {

//   /* The base type for the types that this thing denotes */
//   type TYPE
//   type Tpe <: TYPE
//   val  tpe: Tpe
// }

// /*
//   Bound the universe of types to be `T`s
// */
// trait Denotation[T] extends AnyDenotation { type TYPE = T }
