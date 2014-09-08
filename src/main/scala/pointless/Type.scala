package ohnosequences.pointless

trait AnyType {

  type Raw

  type Me = this.type
  implicit def getType[V <: ValueOf[Me]](v: V): Me = this
}

trait Type[R] extends AnyType { type Raw = R }

object AnyType {

  type withRaw[R] = AnyType { type Raw = R }
  type RawOf[T <: AnyType] = T#Raw

  def valueOf[T <: AnyType](r: RawOf[T]): ValueOf[T] = new ValueOf[T](r)

  implicit def typeOps[T <: AnyType](t: T): TypeOps[T] = new TypeOps[T](t)

  // implicit def toRaw[T <: AnyType](v: ValueOf[T]): RawOf[T] = v.raw
}
import AnyType._

object AnyValue {

  type ofType[T <: AnyType] = AnyValue { type Type = T }
  type TypeOf[V <: AnyValue] = V#Type
  type RawOf[V <: AnyValue] = AnyType.RawOf[TypeOf[V]]
}

sealed trait AnyValue extends Any {

  type Type <: AnyType
  type Value <: Type#Raw
}
trait Value[T <: AnyType, @specialized V <: T#Raw] extends Any with AnyValue {

  type Type = T
  type Value = V
}

final class ValueOf[T <: AnyType](val raw: RawOf[T]) extends AnyVal with Value[T,RawOf[T]] {

  override def toString = raw.toString
}

class TypeOps[T <: AnyType](t: T) {

  // def `<:`(r: RawOf[T]): ValueOf[T] = new ValueOf[T](r)
  def apply(r: RawOf[T]): ValueOf[T] = new ValueOf[T](r)
  def withValue(r: RawOf[T]): ValueOf[T] = new ValueOf[T](r)
}

object ValueOf {

  implicit def valueOps[T <: AnyType](v: ValueOf[T]): ValueOps[T] = new ValueOps[T](v)

  implicit def valueOps[X](x: X): AnyOps[X] = new AnyOps[X](x)
}

class AnyOps[X](x: X) {
  // def is[T <: AnyType.withRaw[X]](t: T): ValueOf[T] = new ValueOf[T](x)
}

class ValueOps[T <: AnyType](v: ValueOf[T]) {
  // ... //
}
