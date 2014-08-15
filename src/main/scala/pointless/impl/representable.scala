package ohnosequences.pointless.impl

object representable extends ohnosequences.pointless.anyRepresentable {
  
  type AnyRepresentable = RepresentableImpl

  trait RepresentableImpl { me =>

    type Me = me.type

    type Raw
    type Rep = RepresentableImpl.RawOf[Me] with RepresentableImpl.Tag[Me]
  }

  object RepresentableImpl {
  
    type RepOf[D <: AnyRepresentable] = D#Rep
    type RawOf[D <: AnyRepresentable] = D#Raw

    case class TagWith[D <: AnyRepresentable](val d: D) {

      def apply(dr : RawOf[D]): RepOf[D] = {

        dr.asInstanceOf[RepOf[D]]
      }
    }

    // Has to be empty! See http://www.scala-lang.org/old/node/11165.html#comment-49097
    sealed trait AnyTag
    sealed trait Tag[D <: AnyRepresentable] extends AnyTag with shapeless.record.KeyTag[D, RawOf[D]]

    def tagWith[R <: AnyRepresentable, U <: RawOf[R]](raw: U, r: R): RepOf[R] = TagWith[R](r)(raw)
  }


  implicit def representableOps[R <: AnyRepresentable](r: R): RepresentableOps[R] = RepresentableOps[R](r)
  case class   RepresentableOps[R <: AnyRepresentable](r: R) extends AnyRepresentableOps[R](r) {

    def =>>[U <: RepresentableImpl.RawOf[R]](raw: U): RepresentableImpl.RepOf[R] = 
      RepresentableImpl.tagWith[R,U](raw, r:R)
  }


}
