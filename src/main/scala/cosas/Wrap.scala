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

  // TODO this apply conflicts with what I want for subset types. Maybe I should just duplicate ValueOf for this case.
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

// what about making it non-final to support subset types? value classes cannot be extended
final class ValueOf[W <: AnyWrap](val raw: W#Raw) extends AnyVal with WrappedValue[W, RawOf[W]] {

  // NOTE: it may be confusing:
  override def toString = raw.toString
}

object ValueOfOps {

  implicit def valueOps[W <: AnyWrap](v: ValueOf[W]): ValueOfOps[W] = new ValueOfOps[W](v.raw)
}

/*
  This class wraps RawOf but it should only be accesible from `v' : ValueOf[W]`
*/
class ValueOfOps[W <: AnyWrap](val v: RawOf[W]) extends AnyVal {
  // ... //
}

trait AnySubsetType extends AnyWrap {

  type W <: AnyWrap
  type Raw = W#Raw
  def predicate(raw: W#Raw): Boolean
}

trait SubsetType[W0 <: AnyWrap] extends AnySubsetType { type W = W0 }

object AnySubsetType {

    class SubSetTypeOps[W <: AnyWrap, ST <: SubsetType[W]](val st: ST) {

      // TODO why this dot?
      def apply(raw: ST#W#Raw): Option[ValueOf[ST]] = {

        if ( st.predicate(raw) ) None else Some( new ValueOf[ST](raw) )
      }
      def withValue(raw: ST#Raw): Option[ValueOf[ST]] = apply(raw)
  }

  object ValueOfSubsetTypeOps {

    implicit def ValueOfSubsetTypeOps[
      W <: AnyWrap,
      ST <: SubsetType[W],
      Ops <: ValueOfSubsetTypeOps[W,ST]
    ](v: ValueOf[ST])(implicit conv: ValueOf[ST] => Ops): Ops = conv(v)

  }
  /*
  you should implement this trait for providing ops for values of a subset type `ST`.
  */
  trait ValueOfSubsetTypeOps[W <: AnyWrap, ST <: SubsetType[W]] extends Any {

    /*
    use case: concat of sized has the sum of the two arg sizes; but how do you create the corresponding value saving a stupid check (and returning an Option)? `unsafeValueOf`. By implementing this trait you assume the responsibility that comes with being able to create unchecked values of `ST`; use it with caution!
    */
    protected def unsafeValueOf[ST0 <: ST](other: ST#Raw): ValueOf[ST] = new ValueOf[ST](other)
  }

}





