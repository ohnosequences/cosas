package ohnosequences.pointless

import AnyTypeSet._, AnyProperty._, AnyFn._

// TODO: move all this to pointless
trait AnyPropertiesHolder {
  type MePropertyHolder = this.type

  type Properties <: AnyTypeSet.Of[AnyProperty]
  val  properties: Properties

  implicit val myOwnProperties: MePropertyHolder Has Properties = (this: MePropertyHolder) has properties
}

trait Properties[Props <: AnyTypeSet.Of[AnyProperty]]
  extends AnyPropertiesHolder { type Properties = Props }

object AnyPropertiesHolder {

  type PropertiesOf[H <: AnyPropertiesHolder] = H#Properties 
}
import AnyPropertiesHolder._



trait AnyRecordWrap extends AnyTypeSet with AnyWrap {
  import AnyTypeUnion._
  type Bound <: just[AnyProperty]
  type Raw <: AnyTypeSet
}

trait EmptyRecordWrap extends EmptySet with AnyRecordWrap {
  type Raw = ∅
}

object emptyRec extends EmptyRecordWrap {
  def toStr = "∅"
}

trait AnyNERecordWrap extends NonEmptySet with AnyRecordWrap {
  type Head <: AnyProperty
  type Tail <: AnyRecordWrap
  type Raw = ValueOf[Head] :~: Tail#Raw
}

case class NERecordWrap[H <: AnyProperty, T <: AnyRecordWrap]
    (val head : H,  val tail : T)(implicit val headIsNew: H ∉ T) extends AnyNERecordWrap {
    type Head = H; type Tail = T
  def toStr = (head :~: tail).toStr
}

object AnyRecordWrap {
  val record: EmptyRecordWrap = emptyRec
  implicit def recordWrapOps[R <: AnyRecordWrap](r: R): RecordWrapOps[R] = new RecordWrapOps[R](r)
}

class RecordWrapOps[R <: AnyRecordWrap](r: R) {
  def withProperties[P <: AnyProperty](p: P): NERecordWrap[P, R] = NERecordWrap(p, r)
  def and[P <: AnyProperty](p: P): NERecordWrap[P, R] = NERecordWrap(p, r)
}


trait ToRecord[S <: AnyTypeSet] extends Fn1[S] with OutBound[AnyRecordWrap]

object ToRecord {
  // implicit def toEmptySet[R <: EmptyRecordWrap](r: R): ∅ = ∅

  implicit val toEmptyRec: 
        ToRecord[∅] with Out[EmptyRecordWrap] = 
    new ToRecord[∅] with Out[EmptyRecordWrap] {
      def apply(s: ∅): Out = emptyRec
    }

  implicit def toNecRec[H <: AnyProperty, T <: AnyTypeSet, TR <: AnyRecordWrap]
    (implicit 
      t: ToRecord[T] { type Out = TR }
    ):  ToRecord[H :~: T] with Out[NERecordWrap[H, TR]] = 
    new ToRecord[H :~: T] with Out[NERecordWrap[H, TR]] {
      def apply(s: H :~: T): Out = NERecordWrap[H, TR](s.head, t(s.tail))
    }

}

case object name extends Property[String]
case object age extends Property[Int]
case object weight extends Property[Long]

object bar {

  implicit def record[S <: AnyTypeSet, R <: AnyRecordWrap](s: S)
    (implicit toRec: ToRecord[S] { type Out = R }): R = toRec(s)

  val person = record(name :~: age :~: weight :~: ∅)

  val a = person(name("foo") :~: age(12) :~: weight(34) :~: ∅)
}

object bububu {
  import AnyRecordWrap._

  val person = record withProperties name and age and weight

  val b = person(weight(34) :~: age(12) :~: name("foo") :~: ∅)
}

/* This is an op for aggregating properties from a vertex or an edge types set */
trait AggregateProperties[S <: AnyTypeSet] extends Fn1[S] with OutBound[AnyTypeSet]

object AggregateProperties {

  def apply[S <: AnyTypeSet](implicit uni: AggregateProperties[S]): AggregateProperties[S] = uni

  implicit def empty:
        AggregateProperties[∅] with Out[∅] =
    new AggregateProperties[∅] with Out[∅] { def apply(s: ∅) = ∅ }

  implicit def cons[H <: AnyPropertiesHolder, T <: AnyTypeSet, TOut <: AnyTypeSet, U <: AnyTypeSet]
    (implicit
      t: AggregateProperties[T] { type Out = TOut },
      u: (PropertiesOf[H] ∪ TOut) { type Out = U }
    ):  AggregateProperties[H :~: T] with Out[U] =
    new AggregateProperties[H :~: T] with Out[U] { 

      def apply(s: H :~: T) = u(s.head.properties, t(s.tail))
    }
}
