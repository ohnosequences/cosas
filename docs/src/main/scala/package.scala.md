
## Implicits and aliases

This is a package object which contains all the needed implicits and type aliases. So when using the library, you should use just one import:

```scala
import ohnosequences.typesets._
```

------


```scala
package ohnosequences

import shapeless._

package object typesets {
```

### Type union

```scala
  type not[T] = T => Nothing
  type either[T] = OneOf[not[T]]
  type oneOf[U <: TypeUnion] = { 
    type is[T]    = not[not[T]] <:<  U#get
    type isnot[T] = not[not[T]] <:!< U#get
  }

  implicit def :<:[ X : oneOf[U]#is,   U <: TypeUnion] = new (X :<: U)
  implicit def :<!:[X : oneOf[U]#isnot, U <: TypeUnion] = new (X :<!: U)
```

### Type sets

```scala
  val ? : ? = empty
  // type ? = Empty // with empty.type

  // Shortcut for a one element set 
  def set[E](e: E): E :~: ? =  :~:.cons(e,?)

  // Any type can be converted to a one element set
  implicit def typeSetOps[S <: TypeSet](s: S) = new TypeSetOps(s)
  // implicit def oneElemSet[E](e: E) = new TypeSetOps(set(e))

  type in[S <: TypeSet] = { 
    type    is[E] = E :<:  S#Bound
    type isnot[E] = E :<!: S#Bound
  }
  implicit def ?[E : in[S]#is, S <: TypeSet] = new (E ? S)
  implicit def ?[E : in[S]#isnot, S <: TypeSet] = new (E ? S)
```

- `S ? Q`

```scala
  type supersetOf[Q <: TypeSet] = { 
    type    is[S <: TypeSet] = Q#Bound#get <:<  S#Bound#get
    type isnot[S <: TypeSet] = Q#Bound#get <:!< S#Bound#get
  }
  implicit def ?[S <: TypeSet : supersetOf[Q]#is, Q <: TypeSet] = new (S ? Q)
```

- `S ? Q`

```scala
  type subsetOf[Q <: TypeSet] = { 
    type    is[S <: TypeSet] = S#Bound#get <:<  Q#Bound#get
    type isnot[S <: TypeSet] = S#Bound#get <:!< Q#Bound#get
  }
  implicit def ?[S <: TypeSet : subsetOf[Q]#is, Q <: TypeSet] = new (S ? Q)
```

- The union types of two sets are the same

```scala
  type sameAs[Q <: TypeSet] = { 
    type    is[S <: TypeSet] = S#Bound#get =:= Q#Bound#get
    type isnot[S <: TypeSet] = S#Bound#get =:!= Q#Bound#get
  }
  implicit def ~[S <: TypeSet : sameAs[Q]#is, Q <: TypeSet] = new (S ~ Q)
```

- All elements of the set are bounded by the given type

```scala
  type boundedBy[B] = { 
    type    is[S <: TypeSet] = S#Bound#get <:<  not[not[B]]
    type isnot[S <: TypeSet] = S#Bound#get <:!< not[not[B]]
  }
  implicit def <<[S <: TypeSet : boundedBy[B]#is, B] = new (S << B)
  implicit def <<![S <: TypeSet : boundedBy[B]#isnot, B] = new (S <<! B)

  implicit def setIsBoundedByUnion[S <: TypeSet, U <: TypeUnion]
    (implicit ev: S#Bound#get <:< U#get) = new SetIsBoundedByUnion[S, U]

  type everyElementOf[S <: TypeSet] = { 
    type isOneOf[U <: TypeUnion] = SetIsBoundedByUnion[S, U]
  }

  type \[S <: TypeSet, Q <: TypeSet] = Subtract[S, Q]

  type ?[S <: TypeSet, Q <: TypeSet] = Union[S, Q]

  type ~>[S <: TypeSet, Q <: TypeSet] = Reorder[S, Q]

  // record-related aliases

  // id.type :~: name.type :~: ?, TaggedWith[id.type] :~: TaggedWith[name.type] :~: ?
  type valuesOf[Ps <: TypeSet] = {

    type is[VT <: TypeSet] = Represented.By[Ps,VT]
  }

  type isValuesOf[VT <: TypeSet, Ps <: TypeSet] = Represented.By[Ps,VT]
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