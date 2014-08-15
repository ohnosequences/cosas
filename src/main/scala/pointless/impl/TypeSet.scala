package ohnosequences.pointless.impl

import ohnosequences.pointless._, impl.typeUnion._
import shapeless.{ HList, Poly }

object typeSet extends anyTypeSet {

  type AnyTypeSet = TypeSetImpl

  type ∅ = EmptySetImpl
  val  ∅ : ∅ = emptySet

  type :~:[E, S <: AnyTypeSet] = ConsImpl[E, S]

  /*
    Predicates
  */
  type in[S <: AnyTypeSet] = PredicateOn[Any] {
    type    is[E] = E    isOneOf S#Bound
    type isnot[E] = E isNotOneOf S#Bound
  }
  // type subsetOf[Q <: AnyTypeSet] <: PredicateOn[AnyTypeSet]
  // type sameAs[Q <: AnyTypeSet] <: PredicateOn[AnyTypeSet]



  /*
    Implementations
  */
  trait TypeSetImpl {

    type Bound <: AnyTypeUnion

    def toStr: String
    override def toString = "{" + toStr + "}"
  }


  sealed trait EmptySetImpl extends TypeSetImpl {

    type Bound = either[Nothing]
    def toStr = ""
  }

  private object emptySet extends EmptySetImpl


  case class ConsImpl[E: in[S]#isNot, S <: TypeSetImpl](head: E, tail: S) extends TypeSetImpl {
    type Bound = tail.Bound#or[E]
    def toStr = {
      val h = head match {
        case _: String => "\""+head+"\""
        case _: Char   => "\'"+head+"\'"
        case _         => head.toString
      }
      val t = tail.toStr
      if (t.isEmpty) h else h+", "+t
    }
  }

  /* This method covers constructor to check that you are not adding a duplicate */
  object  ConsImpl {
    def cons[E : in[S]#isNot, S <: TypeSetImpl](e: E, set: S): ConsImpl[E,S] = ConsImpl(e, set) 
  }

}
