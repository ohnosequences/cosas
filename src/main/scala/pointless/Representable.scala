package ohnosequences.pointless

/*
  Tagging is used for being able to operate on `Raw` values knowing what they are representing; `Rep` is just `Raw` tagged with the `.type` of this representation. So, summarizing

  - `Raw` is the type used to the represent `this.type`
  - `Rep <: Raw` is just `Raw` tagged with `this.type`; the `Rep`resentation
*/

trait anyRepresentable { 

  type AnyRepresentable <: {
    type Raw
    type Rep <: Raw
  }

  type RawOf[D <: AnyRepresentable] = D#Raw
  type RepOf[D <: AnyRepresentable] = D#Rep

  abstract class AnyRepresentableOps[R <: AnyRepresentable](val representable: AnyRepresentable) {

    def =>>[U <: RawOf[R]](raw: U): RepOf[R] 
  }
}

//   type Raw

//   type Me = me.type
 
//   /*
//     `Raw` tagged with `self.type`; this lets you recognize a denotation while being able to operate on it as `Raw`.
//   */
//   final type Rep = Representable.RepOf[Me]

//   /*
//     This lets you get the instance of the singleton type from a tagged `Rep` value.
//   */
//   implicit def fromRep(x: Representable.RepOf[Me]): Me = me

//   // I don't know why this works
//   implicit def yetAnotherFromRep[X <: Me](rep: Representable.RepOf[X]): Me = me
// }

// object Representable {
  
//   type RepOf[D <: Representable] = D#Raw with Tag[D]
//   type RawOf[D <: Representable] = D#Raw

//   case class TagWith[D <: Representable](val d: D) {

//     def apply(dr : RawOf[D]): RepOf[D] = {

//       dr.asInstanceOf[RepOf[D]]
//     }
//   }

//   // Has to be empty! See http://www.scala-lang.org/old/node/11165.html#comment-49097
//   sealed trait AnyTag
//   sealed trait Tag[D <: Representable] extends AnyTag with shapeless.record.KeyTag[D, D#Raw]

//   def tagWith[D <: Representable, R <: D#Raw](r: R, d: D): RepOf[D] = TagWith[D](d)(r)

//   implicit def ops[D <: Representable](d: D): Ops[D] = Ops[D](d)

//   case class Ops[D <: Representable](d: D) {

//     def =>>[R <: RawOf[D]](raw: R): RepOf[D] = tagWith(raw,d)
//   }
// }


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
