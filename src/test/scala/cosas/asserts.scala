package ohnosequences.cosas.tests

case object asserts {

  import ohnosequences.cosas._, types._
  import org.scalatest.Assertions._

  // not only compares the values, but also check the types equality (essential for tagged values)
  def checkTypedEq[A, B](a: A, b: B)(implicit typesEq: A ≃ B): Boolean = a == b

  implicit def taggedSyntax[T <: AnyType, V <: T#Raw](v: T := V):
    TaggedSyntax[T, V] =
    TaggedSyntax[T, V](v)

  case class TaggedSyntax[T <: AnyType, V <: T#Raw](v: T := V) {

    def =~=[W <: T#Raw](w: T := W)(implicit typesEq: V ≃ W): Boolean = v.value == w.value
  }

  def assertTypedEq[A, B](a: A, b: B)(implicit typesEq: A ≃ B): Unit =
    assert(a == b)

  def assertTaggedEq[TA <: AnyType, A <: TA#Raw, TB <: AnyType, B <: TB#Raw](a: TA := A, b: TB := B)
    (implicit
      tagsEq: TA ≃ TB,
      valsEq: A ≃ B
    ): Unit = assert(a.value == b.value)
}
