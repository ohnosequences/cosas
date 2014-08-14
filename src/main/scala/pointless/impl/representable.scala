package ohnosequences.pointless.impl

object representable extends ohnosequences.pointless.representable {
  
  type Representable = RepresentableImpl

  case class Ops[R <: Representable](r: R) extends ops[R](r) {

    def =>>[U <: R#Raw](raw: U): R#Rep = RepresentableImpl.tagWith[R,U](raw, r)
  }

  implicit def ops[R <: Representable](d: R): Ops[R] = Ops[R](d)

  // impl

  trait RepresentableImpl { me =>

    type Me = me.type

    type Raw
    type Rep = Me#Raw with RepresentableImpl.Tag[Me]
  }

  object RepresentableImpl {
  
    type RepOf[D <: Representable] = D#Rep
    type RawOf[D <: Representable] = D#Raw

    case class TagWith[D <: Representable](val d: D) {

      def apply(dr : D#Raw): D#Rep = {

        dr.asInstanceOf[D#Rep]
      }
    }

    // Has to be empty! See http://www.scala-lang.org/old/node/11165.html#comment-49097
    sealed trait AnyTag
    sealed trait Tag[D <: Representable] extends AnyTag with shapeless.record.KeyTag[D, D#Raw]

    def tagWith[R <: Representable, U <: RawOf[R]](raw: U, r: R): R#Rep = TagWith[R](r)(raw)
  }
}