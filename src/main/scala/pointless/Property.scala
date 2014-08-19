package ohnosequences.pointless

import ohnosequences.pointless._, representable._
import scala.reflect.ClassTag

import AnyRepresentable._

object property {

  trait AnyProperty extends AnyRepresentable {

    val label: String
    val classTag: ClassTag[Raw]
  }

  class Property[V](val label: String)(implicit val classTag: ClassTag[V]) extends AnyProperty {

    type Raw = V
  }

  object AnyProperty {

    implicit def ops[P <: AnyProperty](p: P): Ops[P] = new Ops[P](p)
    class   Ops[P <: AnyProperty](p: P) extends AnyRepresentable.Ops(p) { self =>

      def is(value: RawOf[P]): RepOf[P] = self =>> value
    }
  }

}
