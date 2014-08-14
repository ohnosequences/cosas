package ohnosequences.pointless.impl

object representable extends ohnosequences.pointless.representable {
  
  type Representable = RepresentableImpl

  case class RepresentableOpsImpl[R <: RepresentableImpl](override val r: R) extends RepresentableOps[R](r) {

    def =>>[U <: RepresentableImpl.RawOf[R]](raw: U): RepresentableImpl.RepOf[R] = 
      RepresentableImpl.tagWith[R,U](raw, r:R)
  }

  implicit def representableOps[R <: Representable](d: R): RepresentableOpsImpl[R] = RepresentableOpsImpl[R](d)

  // impl

  trait RepresentableImpl extends ohnosequences.pointless.AnyRepresentable { me =>

    type Me = me.type
    type Rep = RepresentableImpl.RawOf[Me] with RepresentableImpl.Tag[Me]
  }

  object RepresentableImpl {
  
    type RepOf[D <: Representable] = D#Rep
    type RawOf[D <: Representable] = D#Raw

    case class TagWith[D <: Representable](val d: D) {

      def apply(dr : RawOf[D]): RepOf[D] = {

        dr.asInstanceOf[RepOf[D]]
      }
    }

    // Has to be empty! See http://www.scala-lang.org/old/node/11165.html#comment-49097
    sealed trait AnyTag
    sealed trait Tag[D <: Representable] extends AnyTag with shapeless.record.KeyTag[D, RawOf[D]]

    def tagWith[R <: Representable, U <: RawOf[R]](raw: U, r: R): RepOf[R] = TagWith[R](r)(raw)
  }
}
