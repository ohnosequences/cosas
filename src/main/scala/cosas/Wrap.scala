package ohnosequences.cosas

/* 
### Wrapping types 
*/

trait AnyWrap {

  type Raw

  type Me = this.type
  implicit def getWrap[V <: ValueOf[Me]](v: V): Me = this
}

trait Wrap[R] extends AnyWrap { type Raw = R }

object AnyWrap {

  type withRaw[R] = AnyWrap { type Raw = R }
  type RawOf[W <: AnyWrap] = W#Raw

  def valueOf[W <: AnyWrap](r: RawOf[W]): ValueOf[W] = new ValueOf[W](r)

  implicit def typeOps[W <: AnyWrap](t: W): WrapOps[W] = new WrapOps[W](t)

  // NOTE: better to do the conversion explicitly
  // implicit def toRaw[W <: AnyWrap](v: ValueOf[W]): RawOf[W] = v.raw
}
import AnyWrap._

class WrapOps[W <: AnyWrap](t: W) {

  def apply(r: RawOf[W]): ValueOf[W] = new ValueOf[W](r)
  def withValue(r: RawOf[W]): ValueOf[W] = new ValueOf[W](r)
}


/* ### Values of wrapped types */

sealed trait AnyWrappedValue extends Any {

  type Wrap <: AnyWrap
  type Value <: Wrap#Raw
}

object AnyWrappedValue {

  type ofWrap[W <: AnyWrap] = AnyWrappedValue { type Wrap = W }
  type WrapOf[V <: AnyWrappedValue] = V#Wrap
  type RawOf[V <: AnyWrappedValue] = AnyWrap.RawOf[WrapOf[V]]
}

trait WrappedValue[W <: AnyWrap, @specialized V <: W#Raw] extends Any with AnyWrappedValue {

  type Wrap = W
  type Value = V
}

// what about making it non-final to support subset types?
final class ValueOf[W <: AnyWrap](val raw: RawOf[W]) extends AnyVal with WrappedValue[W, RawOf[W]] {

  // NOTE: it may be confusing:
  override def toString = raw.toString
}

// object ValueOf {

//   implicit def valueOps[W <: AnyWrap](v: ValueOf[W]): ValueOps[W] = new ValueOps[W](v)
// }

// class ValueOps[W <: AnyWrap](v: ValueOf[W]) {
//   // ... //
// }

trait AnySubsetType extends AnyWrap {

  val predicate: Raw => Boolean
}

abstract class SubsetType[R] extends Wrap[R] with AnySubsetType

object AnySubsetType {

    implicit class SubSetTypeOps[ST <: AnySubsetType](st: ST) {

      def apply(raw: st.Raw): Option[ValueOf[ST]] = if ( st.predicate(raw) ) None else Some( new ValueOf[ST](raw) )
      def withValue(raw: st.Raw): Option[ValueOf[ST]] = apply(raw)
  }

}





