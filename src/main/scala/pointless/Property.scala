package ohnosequences.pointless

import ohnosequences.pointless._, representable._
import scala.reflect.ClassTag

object property {

  trait AnyProperty extends AnyRepresentable {

    val label: String
    val classTag: ClassTag[Raw]
  }

  class Property[V](val label: String)(implicit val classTag: ClassTag[V]) extends AnyProperty {

    type Raw = V
  }

  object AnyProperty {

    import AnyRepresentable._

    implicit def ops[P <: AnyProperty](p: P): Ops[P] = Ops[P](p)
    case class   Ops[P <: AnyProperty](p: P) {

      def is(value: RawOf[P]): RepOf[P] = p =>> value
    }
  }

}
