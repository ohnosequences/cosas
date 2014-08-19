package ohnosequences.pointless.impl

trait AnyRepresentable extends ohnosequences.pointless.AnyRepresentable {

  type Rep = AnyRepresentable.RawOf[Me] with AnyRepresentable.Tag[Me]
}

object AnyRepresentable {

  type RepOf[D <: AnyRepresentable] = D#Rep
  type RawOf[D <: AnyRepresentable] = D#Raw

  case class TagWith[D <: AnyRepresentable](val d: D) {

    def apply(r: RawOf[D]): RepOf[D] = r.asInstanceOf[RepOf[D]]
  }

  // Has to be empty! See http://www.scala-lang.org/old/node/11165.html#comment-49097
  sealed trait AnyTag
  sealed trait Tag[D <: AnyRepresentable] extends AnyTag with shapeless.record.KeyTag[D, RawOf[D]]

  def tagWith[R <: AnyRepresentable, U <: RawOf[R]](raw: U, r: R): RepOf[R] = TagWith[R](r)(raw)

  implicit def representableOps[R <: AnyRepresentable](r: R): RepresentableOps[R] = RepresentableOps[R](r)
  case class   RepresentableOps[R <: AnyRepresentable](r: R) extends ohnosequences.pointless.AnyRepresentable.Ops[R](r) {

    def =>>[U <: RawOf[R]](raw: U): RepOf[R] = tagWith[R,U](raw, r:R)
  }
}
