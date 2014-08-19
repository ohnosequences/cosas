package ohnosequences.pointless

/*
  Tagging is used for being able to operate on `Raw` values knowing what they are representing; `Rep` is just `Raw` tagged with the `.type` of this representation. So, summarizing

  - `Raw` is the type used to the represent `this.type`
  - `Rep <: Raw` is just `Raw` tagged with `this.type`; the `Rep`resentation
*/

object representable {

  trait AnyRepresentable { me => 

    type Me = me.type

    type Raw

    /*
      `Raw` tagged with `self.type`; this lets you recognize a denotation while being able to operate on it as `Raw`.
    */
    type Rep = RepOf[Me]
  }

  // FIXME: "Not a simple type"
  final type RepOf[R <: AnyRepresentable] = R#Raw with Tag[R]
  final type RawOf[R <: AnyRepresentable] = R#Raw

  implicit def representableOps[R <: AnyRepresentable](r: R): RepresentableOps[R] = new RepresentableOps[R](r)
  class RepresentableOps[R <: AnyRepresentable](r: R) {

    def =>>[U <: RawOf[R]](raw: U): RepOf[R] = TagWith[R](r)(raw)
  }

  /* 
    Tagging
  */
  case class TagWith[R <: AnyRepresentable](val d: R) {

    def apply(r: RawOf[R]): RepOf[R] = r.asInstanceOf[RepOf[R]]
  }

  // Has to be empty! See http://www.scala-lang.org/old/node/11165.html#comment-49097
  sealed trait AnyTag {}
  sealed trait Tag[R <: AnyRepresentable] extends AnyTag with shapeless.record.KeyTag[R, RawOf[R]]

  // def tagWith[R <: AnyRepresentable, U <: RawOf[R]](r: R, raw: U): RepOf[R] = TagWith[R](r)(raw)

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
