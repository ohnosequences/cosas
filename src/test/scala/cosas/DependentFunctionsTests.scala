package ohnosequences.cosas.tests

import ohnosequences.cosas._, fns._
import typeSets._
import sampleFunctions._
import shapeless.{union => _, DepFn1 => _, DepFn2 => _, _ }

object sampleFunctions {

  case object size extends DepFn1[Any,Int] {

    implicit val sizeForInt: App1[this.type,Int,Int]   = this at { x: Int    => x }
    implicit val sizeForStr: App1[this.type,String,Int] = this at { _.length }
    implicit val sizeForChar: App1[this.type,Char,Int]  = this at { x: Char   => 1 }
  }

  case object print extends DepFn1[Any,String] {

    implicit val atInt = this at { n: Int => s"${n}: Int" }
    implicit val atString = this at { str: String => s"""'${str}': String""" }
  }


  // alternative mapping over typeSets
  case object MapToHList extends DepFn2[AnyDepFn1, AnyTypeSet, shapeless.HList] {

    implicit def empty[F <: In1]: App2[this.type,F,∅,HNil] = this at { (f: F, e: ∅) => HNil }

    implicit def cons[
      F <: In1,
      H <: F#In1, T <: In2,
      OutH <: F#Out, OutT <: Out
    ](implicit
      evF: App1[F,H,OutH],
      evThis: App2[this.type,F,T,OutT]
    )
    : App2[this.type, F, H :~: T, OutH :: OutT] =
      this at { (f: F, ht: H :~: T) => f(ht.head) :: this(f,ht.tail) }
  }


  // pop
  def pop[E]: Pop[E] = new Pop[E]
  class Pop[E] extends DepFn1[AnyTypeSet, (E, AnyTypeSet)]

  object Pop extends Pop_2 {
    // def apply[S <: AnyTypeSet, E](implicit pop: Pop[S, E]): Pop[S, E] = pop

    implicit def foundInHead[E, H <: E, T <: AnyTypeSet]: App1[Pop[E], H :~: T, (E, T)] =
      App1 { (s: H :~: T) => (s.head, s.tail) }
  }

  trait Pop_2  {

    implicit def foundInTail[H, T <: AnyTypeSet, E, TO <: AnyTypeSet](implicit
      e: E ∈ T,
      l: App1[Pop[E], T, (E, TO)]
    )
    : App1[Pop[E], H :~: T, (E, H :~: TO)] =
      App1 { (s: H :~: T) => val (e, t) = l(s.tail); (e, s.head :~: t) }
  }

  // union
  trait union_5 extends DepFn2[AnyTypeSet, AnyTypeSet, AnyTypeSet] {

    // use this, bound it at the end
    type Union <: this.type
    val union: Union

    implicit def bothHeads[SH, ST <: AnyTypeSet, QH, QT <: AnyTypeSet, O <: AnyTypeSet](implicit
      sh: SH ∉ (QH :~: QT),
      qh: QH ∉ (SH :~: ST),
      rest: App2[Union, ST, QT, O]
    ) =
      union at { (s: SH :~: ST, q: QH :~: QT) => s.head :~: q.head :~: union(s.tail, q.tail) }
  }
  trait union_4 extends union_5 {

    implicit def qHead[S <: AnyTypeSet, QH, QT <: AnyTypeSet, O <: AnyTypeSet](implicit
      qh: QH ∈ S,
      rest: App2[Union, S, QT, O]
    ) =
      union at { (s: S, q: QH :~: QT) => union(s, q.tail) }
  }
  trait union_3 extends union_4 {

    implicit def sHead[SH, ST <: AnyTypeSet, Q <: AnyTypeSet, O <: AnyTypeSet](implicit
      sh: SH ∈ Q,
      rest: App2[Union,ST,Q,O]
    ) =
      union at { (s: SH :~: ST, q: Q) => union(s.tail,q) }
  }
  trait union_2 extends union_3 {

    implicit def qInS[S <: AnyTypeSet.SubsetOf[Q], Q <: AnyTypeSet] =
      union at { (q:Q, s: S) => q }
  }
  case object union extends union_2 {

    type Union = this.type
    val union = this

    implicit def sInQ[S <: AnyTypeSet.SubsetOf[Q], Q <: AnyTypeSet] =
      union at { (s:S, q:Q) => q }
  }
}

class DependentFunctionsTests extends org.scalatest.FunSuite {

  test("can apply dependent functions") {

    val uh = print(2)
    assert { 2 === size(size("bu")) }
    assert { size(4) === size("four") }

    val a = "ohoho" :~: 'c' :~: ∅
    val b = "lala" :~: 'a' :~: 2 :~: ∅
    val c = "lololo" :~: true :~: ∅

    assert { ("ohoho", 'c' :~: ∅) === pop[String](a) }
    assert { (true, "lololo" :~: ∅) === pop[Boolean](c) }

    val zzz = depFn2ApplyOps[
      MapToHList.type,
      size.type,
      String :~: Char :~: Int :~: ∅,
      Int :: Int :: Int :: HNil
    ](MapToHList).apply(size,b)

    depFn2ApplyOps[MapToHList.type, size.type, Int :~: ∅, Int :: HNil](MapToHList).apply(size,2 :~: ∅)

    // assert { 4 :: 1 :: 2 :: HNil === MapToHList(size,b) }

    val ab = union(union(a,b),a)
    val ba = union(b,a)
    val bc = union(b,c)
    val cb = union(c,b)
    val abc = union(union(a,b),c)
  }
}
