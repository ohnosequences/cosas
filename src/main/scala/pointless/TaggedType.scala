package ohnosequences.pointless

/*
  Tagging is used for being able to operate on `Raw` values knowing what they are representing; `Rep` is just `Raw` tagged with the `.type` of this representation. So, summarizing

  - `Raw` is the type used to the represent `this.type`
  - `Rep <: Raw` is just `Raw` tagged with `this.type`; the `Rep`resentation
*/

object taggedType {

  trait AnyTaggedType { me => 

    final type Me = me.type

    type Raw

    /*
      `Raw` tagged with `self.type`; this lets you recognize a denotation while being able to operate on it as `Raw`.
    */
    // we don't actually need this
    // final type Rep = Tagged[Me]

    implicit def fromTagged(x: Tagged[Me]): Me = me
    // I don't know why this works
    implicit def yetAnotherFromRep[X <: Me](rep: Tagged[X]): Me = me
  }

  trait TaggedType[R] extends AnyTaggedType {

    type Raw = R
  }

  type Tagged[T <: AnyTaggedType] = T#Raw with Tag[T]
  // FIXME: "Not a simple type"
  type RawOf[T <: AnyTaggedType] = T#Raw
  
  type @@[T <: AnyTaggedType] = Tagged[T]

  implicit def taggedTypeOps[T <: AnyTaggedType](t: T): TaggedTypeOps[T] = new TaggedTypeOps[T](t)
  
  class TaggedTypeOps[T <: AnyTaggedType](val t: T) {

    def =>>[R <: RawOf[T]](raw: R): Tagged[T] = TagWith[T](t)(raw)
  }

  /* 
    Tagging
  */
  case class TagWith[T <: AnyTaggedType](val t: T) {

    def apply(r: RawOf[T]): Tagged[T] = r.asInstanceOf[Tagged[T]]
  }

  // Has to be empty! See http://www.scala-lang.org/old/node/11165.html#comment-49097
  sealed trait AnyTag {}
  sealed trait Tag[T <: AnyTaggedType] extends AnyTag with shapeless.record.KeyTag[T, RawOf[T]]

  // def tagWith[R <: AnyTaggedType, U <: R#Raw](r: R, raw: U): Tagged[R] = TagWith[R](r)(raw)

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
