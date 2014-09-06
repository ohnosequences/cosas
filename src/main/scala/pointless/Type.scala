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

  implicit def typeOps[T <: AnyType](t: T): TypeOps[T] = new TypeOps[T](t)
}
import AnyType._


final class ValueOf[T <: AnyType](val raw: RawOf[T]) extends AnyVal {
  type Type = T
}

class TypeOps[T <: AnyType](t: T) {

  // def `<:`(r: RawOf[T]): ValueOf[T] = new ValueOf[T](r)
  def apply(r: RawOf[T]): ValueOf[T] = new ValueOf[T](r)
}

object ValueOf {
  implicit def toRaw[T <: AnyType](v: ValueOf[T]): RawOf[T] = v.raw

  implicit def valueOps[T <: AnyType](v: ValueOf[T]): ValueOps[T] = new ValueOps[T](v)

  implicit def valueOps[X](x: X): AnyOps[X] = new AnyOps[X](x)
}

class AnyOps[X](x: X) {
  def is[T <: AnyType.withRaw[X]](t: T): ValueOf[T] = new ValueOf[T](x)
}

class ValueOps[T <: AnyType](v: ValueOf[T]) {
  // ... //
}
