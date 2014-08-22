package ohnosequences.pointless

/* Tagging is used for being able to operate on `Raw` values knowing what they are representing */
trait AnyTaggedType { me => 

  final type Me = me.type

  type Raw

  import AnyTaggedType.Tagged
  implicit def fromTagged(x: Tagged[Me]): Me = me
  // NOTE: I don't know why this works
  implicit def yetAnotherFromRep[X <: Me](rep: Tagged[X]): Me = me
}

trait TaggedType[R] extends AnyTaggedType {

  type Raw = R
}

object AnyTaggedType {

  /* Accessors */
  type RawOf[T <: AnyTaggedType] = T#Raw

  /* This lets you recognize a tagged type while being able to operate on it as `Raw` */
  // NOTE: The "Not a simple type" warning is an sbt bug
  type Tagged[T <: AnyTaggedType] = T#Raw with Tag[T]
  type @@[T <: AnyTaggedType] = Tagged[T]

  implicit def taggedTypeOps[T <: AnyTaggedType](t: T): TaggedTypeOps[T] = new TaggedTypeOps[T](t)
  
}

import AnyTaggedType._

class TaggedTypeOps[T <: AnyTaggedType](val t: T) {

  def =>>[R <: RawOf[T]](raw: R): Tagged[T] = TagWith[T](t)(raw)
}

/* 
  Tagging
*/
// Has to be empty! See http://www.scala-lang.org/old/node/11165.html#comment-49097
sealed trait AnyTag {}
sealed trait Tag[T <: AnyTaggedType] extends AnyTag with shapeless.record.KeyTag[T, RawOf[T]]

case class TagWith[T <: AnyTaggedType](val t: T) {

  def apply(r: RawOf[T]): Tagged[T] = r.asInstanceOf[Tagged[T]]
}
