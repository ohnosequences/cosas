
```scala
package ohnosequences.typesets

import shapeless._, poly._
```

Mapping a set to another set, i.e. the results of mapping should have distinct types

```scala
@annotation.implicitNotFound(msg = "Can't map ${F} over ${In} (maybe the resulting types are not distinct)")
trait SetMapper[F, In <: TypeSet] extends DepFn1[In] { type Out <: TypeSet }

object SetMapper {
  def apply[F, S <: TypeSet](implicit mapper: SetMapper[F, S]): Aux[F, S, mapper.Out] = mapper

  type Aux[F, In <: TypeSet, O <: TypeSet] = SetMapper[F, In] { type Out = O }
  
  implicit def emptyMapper[F]: Aux[F, ?, ?] =
    new SetMapper[F, ?] {
      type Out = ?
      def apply(s: ?): Out = ?
    }
  
  implicit def consMapper[F <: Poly, H, OutH, T <: TypeSet, OutT <: TypeSet]
    (implicit
      h: Case1.Aux[F, H, OutH], 
      t: Aux[F, T, OutT],
      e: OutH ? OutT  // the key check here
    ): Aux[F, H :~: T, OutH :~: OutT] =
      new SetMapper[F, H :~: T] { type Out = OutH :~: OutT
        def apply(s: H :~: T): Out = h(s.head) :~: t(s.tail)
      }
}
```

Mapping a set to an HList: when you want to preserve precise types, but they are not distinct

```scala
@annotation.implicitNotFound(msg = "Can't map ${F} over ${In} to an HList")
trait HListMapper[F, In <: TypeSet] extends DepFn1[In] { type Out <: HList }

object HListMapper {
  def apply[F, S <: TypeSet](implicit mapper: HListMapper[F, S]): Aux[F, S, mapper.Out] = mapper

  type Aux[F, In <: TypeSet, O <: HList] = HListMapper[F, In] { type Out = O }
  
  implicit def emptyHListMapper[F]: Aux[F, ?, HNil] =
    new HListMapper[F, ?] {
      type Out = HNil
      def apply(s: ?): Out = HNil
    }
  
  implicit def consHListMapper[F <: Poly, H, OutH, T <: TypeSet, OutT <: HList]
    (implicit
      h: Case1.Aux[F, H, OutH], 
      t: Aux[F, T, OutT]
    ): Aux[F, H :~: T, OutH :: OutT] =
      new HListMapper[F, H :~: T] { type Out = OutH :: OutT
        def apply(s: H :~: T): Out = h(s.head) :: t(s.tail)
      }
}
```

Mapping a set to a List: normally, when you are mapping everything to one type

```scala
@annotation.implicitNotFound(msg = "Can't map ${F} over ${In} to a List")
trait ListMapper[F, In <: TypeSet] extends DepFn1[In] { 
  type O
  type Out = List[O]
}

object ListMapper {
  def apply[F, S <: TypeSet](implicit mapper: ListMapper[F, S]): Aux[F, S, mapper.O] = mapper

  type Aux[F, In <: TypeSet, O_] = ListMapper[F, In] { type O = O_ }
  
  implicit def emptyListMapper[F, O_]: Aux[F, ?, O_] =
    new ListMapper[F, ?] { type O = O_
      def apply(s: ?): Out = Nil
    }
  
  implicit def oneListMapper[F <: Poly, OH, H]
    (implicit h: Case1.Aux[F, H, OH]): Aux[F, H :~: ?, OH] =
      new ListMapper[F, H :~: ?] { type O = OH
        def apply(s: H :~: ?): Out = List[OH](h(s.head))
      }

  implicit def consListMapper[F <: Poly, O_, H1, H2, T <: TypeSet]
    (implicit
      h: Case1.Aux[F, H1, O_], 
      t: Aux[F, H2 :~: T, O_]
    ): Aux[F, H1 :~: H2 :~: T, O_] =
      new ListMapper[F, H1 :~: H2 :~: T] { type O = O_
        def apply(s: H1 :~: H2 :~: T): Out = h(s.head) :: t(s.tail)
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

[main/scala/items/items.scala]: ../items/items.scala.md
[main/scala/ops/Choose.scala]: Choose.scala.md
[main/scala/ops/Lookup.scala]: Lookup.scala.md
[main/scala/ops/Map.scala]: Map.scala.md
[main/scala/ops/MapFold.scala]: MapFold.scala.md
[main/scala/ops/Pop.scala]: Pop.scala.md
[main/scala/ops/Reorder.scala]: Reorder.scala.md
[main/scala/ops/Replace.scala]: Replace.scala.md
[main/scala/ops/Subtract.scala]: Subtract.scala.md
[main/scala/ops/ToList.scala]: ToList.scala.md
[main/scala/ops/Union.scala]: Union.scala.md
[main/scala/package.scala]: ../package.scala.md
[main/scala/Property.scala]: ../Property.scala.md
[main/scala/Record.scala]: ../Record.scala.md
[main/scala/Representable.scala]: ../Representable.scala.md
[main/scala/TypeSet.scala]: ../TypeSet.scala.md
[main/scala/TypeUnion.scala]: ../TypeUnion.scala.md
[test/scala/items/itemsTests.scala]: ../../../test/scala/items/itemsTests.scala.md
[test/scala/RecordTests.scala]: ../../../test/scala/RecordTests.scala.md
[test/scala/TypeSetTests.scala]: ../../../test/scala/TypeSetTests.scala.md