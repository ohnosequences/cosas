
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

  // Shortcut for a one element set 
  def set[E](e: E): E :~: ? = e :~: ?

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
    // type isnot[S <: TypeSet] = S#Bound#get =:!= Q#Bound#get
  }
  implicit def ~[S <: TypeSet : sameAs[Q]#is, Q <: TypeSet] = new (S ~ Q)
```

- All elements of the set are bounded by the given type

```scala
  type boundedBy[B] = { 
    type    is[S <: TypeSet] = S#Bound#get <:<  not[not[B]]
    type isnot[S <: TypeSet] = S#Bound#get <:!< not[not[B]]
  }
```

- Subtraction of sets

```scala
  type \[S <: TypeSet, Q <: TypeSet] = SubtractSets[S, Q]
```

- Union of sets

```scala
  type U[S <: TypeSet, Q <: TypeSet] = UnionSets[S, Q]

}

```


------

### Index

+ src
  + main
    + scala
      + [HListOps.scala][main/scala/HListOps.scala]
      + [LookupInSet.scala][main/scala/LookupInSet.scala]
      + [MapFoldSets.scala][main/scala/MapFoldSets.scala]
      + [package.scala][main/scala/package.scala]
      + [SetMapper.scala][main/scala/SetMapper.scala]
      + [SubtractSets.scala][main/scala/SubtractSets.scala]
      + [TypeSet.scala][main/scala/TypeSet.scala]
      + [TypeUnion.scala][main/scala/TypeUnion.scala]
      + [UnionSets.scala][main/scala/UnionSets.scala]
  + test
    + scala
      + [TypeSetTests.scala][test/scala/TypeSetTests.scala]

[main/scala/HListOps.scala]: HListOps.scala.md
[main/scala/LookupInSet.scala]: LookupInSet.scala.md
[main/scala/MapFoldSets.scala]: MapFoldSets.scala.md
[main/scala/package.scala]: package.scala.md
[main/scala/SetMapper.scala]: SetMapper.scala.md
[main/scala/SubtractSets.scala]: SubtractSets.scala.md
[main/scala/TypeSet.scala]: TypeSet.scala.md
[main/scala/TypeUnion.scala]: TypeUnion.scala.md
[main/scala/UnionSets.scala]: UnionSets.scala.md
[test/scala/TypeSetTests.scala]: ../../test/scala/TypeSetTests.scala.md