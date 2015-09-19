package ohnosequences.cosas.tests

import ohnosequences.cosas._, fns._

object sampleFunctions {

  case object size extends DepFn1[Any,Int] {

    implicit val sizeForStr: App1[size.type,String] { type Out = Int } = this at { x: String => x.length }
    implicit val sizeForChar  = this at { x: Char   => 1 }
    implicit val sizeForInt   = this at { x: Int    => x }
  }

  import typeSets._

  // alternative mapping

  case object MapToHList extends DepFn2[AnyDepFn1, AnyTypeSet, shapeless.HList] {

    // def apply[F <: Poly1, S <: AnyTypeSet]
    //   (implicit mapper: MapToHList[F, S]): MapToHList[F, S] = mapper
    import shapeless._

    implicit def empty[F <: AnyDepFn1]: App2[MapToHList.type,F,∅] { type Out = HNil } =
      this at { (f:F, e: ∅) => HNil }

    implicit def cons[
      F <: AnyDepFn1,
      H <: F#In1, T <: AnyTypeSet,
      OutH <: F#Out, OutT <: HList
    ](implicit
      h: App1[F,H],
      t: App2[this.type,F,T]
    ): App2[MapToHList.type, F,H :~: T] =
      this at { (f: F, ht: H :~: T) => h(ht.head) :: t(f,ht.tail) }
  }

  // trait MapToHList[F <: Poly1, S <: AnyTypeSet] extends Fn1[S] with OutBound[HList]
}

class DependentFunctionsTests extends org.scalatest.FunSuite {

  import sampleFunctions._
  import shapeless._
  import typeSets._

  test("can apply dependent functions") {

    assert { 2 === size(size(2)) }
    assert { size(4) === size("four") }


    // val buh = "lalala" :~: 'b' :~: 2323 :~: ∅
    // val buhlengths = MapToHList[size.type, String :~: Char :~: Int :~: ∅, Int :: Int :: Int :: HNil](size,buh)
    val b = "lala" :~: 'a' :~: 2 :~: ∅
    import MapToHList._
    import size._
    val bl = MapToHList.aply(size,b)//(cons(size.sizeForStr,empty))
  }
}
