package ohnosequences.pointless

import AnyTypeSet._, AnyProperty._, AnyFn._, AnyTypeUnion._

trait AnyWrapSet extends AnyTypeSet with AnyWrap {
  override def toString = "<" + toStr + ">"

  // type Bound <: just[AnyWrap]
  type Raw <: AnyTypeSet
}

trait EmptyWrapSet extends EmptySet with AnyWrapSet {
  def toStr = "-"

  type Types = TypeUnion.empty
  type Bound = Types#union
  type Raw = ∅
}

object EmptyWrapSet extends EmptyWrapSet


trait NonEmptyWrapSet extends NonEmptySet with AnyWrapSet {

  type Head <: AnyWrap
  type Tail <: AnyWrapSet

  type Types = TypesOf[Tail]#or[Head]
  type Bound = Types#union

  type Raw = ValueOf[Head] :~: Tail#Raw
}

case class ConsWrapSet[H <: AnyWrap, T <: AnyWrapSet]
  (val head : H,  val tail : T)(implicit val headIsNew: H ∉ T) extends NonEmptyWrapSet {
  type Head = H; type Tail = T

  def toStr = head.toString + ", " + tail.toStr
}

/* This method covers constructor to check that you are not adding a duplicate */
object ConsWrapSet {
  def cons[H <: AnyWrap, T <: AnyWrapSet](h: H, set: T)(implicit check: H ∉ T): ConsWrapSet[H,T] = ConsWrapSet(h, set) 
}

object AnyWrapSet {

  // final type ⦲ = EmptyWrapSet
  // val ⦲ : ⦲ = EmptyWrapSet

  final type :^:[H <: AnyWrap, T <: AnyWrapSet] = ConsWrapSet[H, T]

  type Of[T] = AnyWrapSet { type Bound <: just[T] }

  implicit def wrapSetOps[W <: AnyWrapSet](w: W): 
        WrapSetOps[W] = 
    new WrapSetOps[W](w)

  implicit def emptySetOps[E <: EmptySet](e: E): 
        WrapSetOps[EmptyWrapSet] = 
    new WrapSetOps[EmptyWrapSet](EmptyWrapSet)
}

class WrapSetOps[W <: AnyWrapSet](w: W) {
  import AnyWrapSet._

  def :^:[H <: AnyWrap](h: H)(implicit check: H ∉ W): (H :^: W) = ConsWrapSet.cons(h, w)(check)

  import ops.typeSet.ParseFrom
  def parseFrom[X](x: X)(implicit parser: W ParseFrom X): parser.Out = parser(w, x)
}
