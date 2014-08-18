
```scala
package ohnosequences.typesets

import scala.reflect.ClassTag
```

Properties

```scala
trait AnyProperty extends Representable {
  val label: String
  // TODO make this optional?
  val classTag: ClassTag[Raw]

  // WARNING it does not work; compiler bug
  final type Entry = Rep
  final type Value = Raw

  final def is(r: Raw): Rep = this ->> r
}
```

Evidence that an arbitrary type `Smth` has property `P`

```scala
sealed class HasProperty[S, P <: AnyProperty]
```

or a set of properties `Ps`

```scala
sealed class HasProperties[S, Ps <: TypeSet: boundedBy[AnyProperty]#is] 

object AnyProperty {
```

This implicit is a bridge from `HasProperties` to `HasProperty`

```scala
  implicit def FromSetToAProperty[T, P <: AnyProperty, Ps <: TypeSet]
    (implicit ps: T HasProperties Ps, ep: P ? Ps): HasProperty[T, P] = new HasProperty[T, P]

  type ofValue[R] = AnyProperty { type Raw = R }
  type Of[S] = { type is[P <: AnyProperty] = S HasProperty P }
}
```

Properties sould be defined as case objects: `case object Name extends Property[String]`

```scala
class Property[V](implicit val classTag: ClassTag[V]) extends AnyProperty {
  val label = this.toString
  type Raw = V
}

object Property {
```

For context bounds: `P <: AnyProperty: Property.Of[X]#is`

```scala
  type Of[S] = { type is[P <: AnyProperty] = S HasProperty P }
```

Common ops for getting properties

```scala
  implicit def propertyGetterOps[T <: Singleton with AnyDenotation with CanGetProperties](rep: AnyTag.TaggedWith[T]): 
               PropertyGetterOps[T] = PropertyGetterOps[T](rep)
  case class   PropertyGetterOps[T <: Singleton with AnyDenotation with CanGetProperties](rep: AnyTag.TaggedWith[T]) {

    def get[P <: Singleton with AnyProperty: Property.Of[T#Tpe]#is](p: P)
      (implicit mkGetter: p.type => T#PropertyGetter[p.type]): p.Raw = 
        mkGetter(p).apply(rep)
  }
}

class HasPropertiesOps[T](t: T) {
```

Handy way of creating an implicit evidence saying that this vertex type has that property

```scala
  def has[P <: AnyProperty](p: P) = new (T HasProperty P)
  def has[Ps <: TypeSet : boundedBy[AnyProperty]#is](ps: Ps) = new (T HasProperties Ps)
```

Takes a set of properties and filters out only those, which this vertex "has"

```scala
  def filterMyProps[Ps <: TypeSet : boundedBy[AnyProperty]#is](ps: Ps)
    (implicit f: FilterProps[T, Ps]) = f(ps)
}
```

Read a property from a representation

```scala
trait CanGetProperties { self: Representable =>

  abstract class PropertyGetter[P <: AnyProperty](val p: P) {
    def apply(rep: self.Rep): p.Raw
  }
}

import shapeless._, poly._
```


For a given arbitrary type `Smth`, filters any property set, 
leaving only those which have the `Smth HasProperty _` evidence


```scala
trait FilterProps[Smth, Ps <: TypeSet] extends DepFn1[Ps] {
  type Out <: TypeSet
}

object FilterProps extends FilterProps2 {
  // the case when there is this evidence (leaving the head)
  implicit def consFilter[Smth, H <: AnyProperty, T <: TypeSet, OutT <: TypeSet]
    (implicit
      h: Smth HasProperty H,
      t: Aux[Smth, T, OutT]
    ): Aux[Smth, H :~: T, H :~: OutT] =
      new FilterProps[Smth, H :~: T] { type Out = H :~: OutT
        def apply(s: H :~: T): Out = s.head :~: t(s.tail)
      }
}

trait FilterProps2 {
  def apply[Smth, Ps <: TypeSet](implicit filt: FilterProps[Smth, Ps]): Aux[Smth, Ps, filt.Out] = filt

  type Aux[Smth, In <: TypeSet, O <: TypeSet] = FilterProps[Smth, In] { type Out = O }
  
  implicit def emptyFilter[Smth]: Aux[Smth, ?, ?] =
    new FilterProps[Smth, ?] {
      type Out = ?
      def apply(s: ?): Out = ?
    }

  // the low-priority case when there is no evidence (just skipping head)
  implicit def skipFilter[Smth, H <: AnyProperty, T <: TypeSet, OutT <: TypeSet]
    (implicit t: Aux[Smth, T, OutT]): Aux[Smth, H :~: T, OutT] =
      new FilterProps[Smth, H :~: T] { type Out = OutT
        def apply(s: H :~: T): Out = t(s.tail)
      }
}
```

This applies `FilterProps` to a list of `Smth`s (`Ts` here)

```scala
trait ZipWithProps[Ts <: TypeSet, Ps <: TypeSet] extends DepFn2[Ts, Ps] {
  type Out <: TypeSet
}

object ZipWithProps {
  def apply[Ts <: TypeSet, Ps <: TypeSet]
    (implicit z: ZipWithProps[Ts, Ps]): Aux[Ts, Ps, z.Out] = z

  type Aux[Ts <: TypeSet, Ps <: TypeSet, O <: TypeSet] = ZipWithProps[Ts, Ps] { type Out = O }
  
  implicit def emptyZipWithProps[Ps <: TypeSet]: Aux[?, Ps, ?] =
    new ZipWithProps[?, Ps] {
      type Out = ?
      def apply(s: ?, ps: Ps): Out = ?
    }

  implicit def consZipWithProps[H, T <: TypeSet, Ps <: TypeSet, OutT <: TypeSet]
    (implicit 
      h: FilterProps[H, Ps],
      t: Aux[T, Ps, OutT]
    ): Aux[H :~: T, Ps, (H, h.Out) :~: OutT] =
      new ZipWithProps[H :~: T, Ps] { type Out = (H, h.Out) :~: OutT
        def apply(s: H :~: T, ps: Ps): Out = (s.head, h(ps)) :~: t(s.tail, ps)
      }
}
```


------

### Index

+ src
  + main
    + scala
      + items
        + [items.scala][main/scala/items/items.scala]
      + ops
        + [Choose.scala][main/scala/ops/Choose.scala]
        + [Lookup.scala][main/scala/ops/Lookup.scala]
        + [Map.scala][main/scala/ops/Map.scala]
        + [MapFold.scala][main/scala/ops/MapFold.scala]
        + [Pop.scala][main/scala/ops/Pop.scala]
        + [Reorder.scala][main/scala/ops/Reorder.scala]
        + [Replace.scala][main/scala/ops/Replace.scala]
        + [Subtract.scala][main/scala/ops/Subtract.scala]
        + [ToList.scala][main/scala/ops/ToList.scala]
        + [Union.scala][main/scala/ops/Union.scala]
      + [package.scala][main/scala/package.scala]
      + pointless
        + impl
      + [Property.scala][main/scala/Property.scala]
      + [Record.scala][main/scala/Record.scala]
      + [Representable.scala][main/scala/Representable.scala]
      + [TypeSet.scala][main/scala/TypeSet.scala]
      + [TypeUnion.scala][main/scala/TypeUnion.scala]
  + test
    + scala
      + items
        + [itemsTests.scala][test/scala/items/itemsTests.scala]
      + [RecordTests.scala][test/scala/RecordTests.scala]
      + [TypeSetTests.scala][test/scala/TypeSetTests.scala]

[main/scala/items/items.scala]: items/items.scala.md
[main/scala/ops/Choose.scala]: ops/Choose.scala.md
[main/scala/ops/Lookup.scala]: ops/Lookup.scala.md
[main/scala/ops/Map.scala]: ops/Map.scala.md
[main/scala/ops/MapFold.scala]: ops/MapFold.scala.md
[main/scala/ops/Pop.scala]: ops/Pop.scala.md
[main/scala/ops/Reorder.scala]: ops/Reorder.scala.md
[main/scala/ops/Replace.scala]: ops/Replace.scala.md
[main/scala/ops/Subtract.scala]: ops/Subtract.scala.md
[main/scala/ops/ToList.scala]: ops/ToList.scala.md
[main/scala/ops/Union.scala]: ops/Union.scala.md
[main/scala/package.scala]: package.scala.md
[main/scala/Property.scala]: Property.scala.md
[main/scala/Record.scala]: Record.scala.md
[main/scala/Representable.scala]: Representable.scala.md
[main/scala/TypeSet.scala]: TypeSet.scala.md
[main/scala/TypeUnion.scala]: TypeUnion.scala.md
[test/scala/items/itemsTests.scala]: ../../test/scala/items/itemsTests.scala.md
[test/scala/RecordTests.scala]: ../../test/scala/RecordTests.scala.md
[test/scala/TypeSetTests.scala]: ../../test/scala/TypeSetTests.scala.md