package ohnosequences.typesets

import shapeless.record.KeyTag

/*
  Tagging is used for being able to operate on `Raw` values knowing what they are representing; `Rep` is just `Raw` tagged with the `.type` of this representation. So, summarizing

  - `Raw` is the type used to the represent `this.type`
  - `Rep <: Raw` is just `Raw` tagged with `this.type`; the `Rep`resentation
*/

trait Representable { self =>

  type Raw

  type Me = this.type
 
  /*
    `Raw` tagged with `self.type`; this lets you recognize a denotation while being able to operate on it as `Raw`.
  */
  final type Rep = AnyTag.RepOf[self.type]

  /*
    `Raw` enters, `Rep` leaves
  */
  final def ->>(r: AnyTag.RawOf[Me]): AnyTag.RepOf[Me] = AnyTag.TagWith[Me](self)(r)

  /*
    This lets you get the instance of the singleton type from a tagged `Rep` value.
  */
  // implicit def fromRep(x: self.Rep): self.type = self
  implicit def fromRep(x: AnyTag.RepOf[self.type]): self.type = self

  // I don't know why this works
  implicit def yetAnotherFromRep[X <: Me](rep: X#Rep): Me = {

    self
  }
}

object Representable {

  import AnyTag.Tag
  
  type RepOf[D <: Representable] = D#Raw with Tag[D]
  type RawOf[D <: Representable] = D#Raw

  implicit def ops[D <: Representable](d: D): Ops[D] = Ops[D](d)

  case class Ops[D <: Representable](d: D) {

    def =>>[R <: AnyTag.RawOf[D]](raw: R): AnyTag.RepOf[D] = AnyTag.=>>(d,raw)
  }
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

  case class TagWith[D <: Representable](val d: D) {

    def apply(dr : RawOf[D]): RepOf[D] = {

      dr.asInstanceOf[RepOf[D]]
    }
  }

  // Has to be empty! See http://www.scala-lang.org/old/node/11165.html#comment-49097
  sealed trait AnyTag {}
  sealed trait Tag[D <: Representable] extends AnyTag with KeyTag[D, D#Raw] {}

  // compiler bug; do not use type aliases in type aliases
  // type RepOf[D <: Representable] = RawOf[D] with Tag[D]
  type RepOf[D <: Representable] = D#Raw with Tag[D]
  type RawOf[D <: Representable] = D#Raw

  type AsRepOf[X, D <: Representable] = X with RawOf[D] with Tag[D]
  type SingletonOf[X <: AnyRef] = Singleton with X

  def =>>[D <: Representable, R <: D#Raw](d: D, r: R): AnyTag.RepOf[D] = AnyTag.TagWith[D](d)(r)
}