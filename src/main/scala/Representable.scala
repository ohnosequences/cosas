package ohnosequences.typesets

import shapeless.record.KeyTag

/*
  Tagging is used for being able to operate on `Raw` values knowing what they are representing; `Rep` is just `Raw` tagged with the `.type` of this representation. So, summarizing

  - `Raw` is the type used to the represent `this.type`
  - `Rep <: Raw` is just `Raw` tagged with `this.type`; the `Rep`resentation
*/

trait Representable { self =>

  type Raw
 
  /*
    `Raw` tagged with `self.type`; this lets you recognize a denotation while being able to operate on it as `Raw`.
  */
  final type Rep = AnyTag.TaggedWith[self.type]

  /*
    `Raw` enters, `Rep` leaves
  */
  final def ->>(r: Raw): self.Rep = AnyTag.TagWith[self.type](self)(r)

  /*
    This lets you get the instance of the singleton type from a tagged `Rep` value.
  */
  implicit def fromRep(x: self.Rep): self.type = self
}


/*
  This trait represents a mapping between 

  - members `Tpe` of a universe of types `TYPE`
  - and `Raw` a type meant to be a denotation of `Tpe` thus the name

*/
trait AnyDenotation extends Representable {

  /* The base type for the types that this thing denotes */
  type TYPE
  type Tpe <: TYPE
  val  tpe: Tpe
}

/*
  Bound the universe of types to be `T`s
*/
trait Denotation[T] extends AnyDenotation { type TYPE = T }

/*
  The companion object contains mainly tagging functionality.
*/
object AnyTag {

  case class TagWith[D <: Singleton with Representable](val d: D) {
    def apply(dr : d.Raw): TaggedWith[d.type] = dr.asInstanceOf[TaggedWith[d.type]]
  }

  type TaggedWith[D <: Singleton with Representable] = D#Raw with Tag[D]

  // Has to be empty! See http://www.scala-lang.org/old/node/11165.html#comment-49097
  sealed trait AnyTag 
  sealed trait Tag[D <: Singleton with Representable] extends AnyTag with KeyTag[D, D#Raw]

}