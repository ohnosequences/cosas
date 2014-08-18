
## Type sets

Here we define type-level sets. The main idea behind this construction is that it cannot contain type-duplicates and uses [TypeUnion](TypeUnion.md), which provides cheap checks whether a type is in the set, and allows to construct an effective union and similar operations.

- all operations are on type level, so `set(true) ~ set(false)`, because they both have type `Boolean :~: ?`;
- after subtraction, union or other operations on two sets, the order of elements can change, so that operations (like union) are not commutative, but they yield the same type, so the results are considered equivalent (see `~` operator);


```scala
package ohnosequences.typesets
```

### The main type representing set

```scala
trait TypeSet { set =>
  type Bound <: TypeUnion
  def toStr: String
  override def toString = "{" + toStr + "}"
}
```

### Empty set

```scala
sealed trait ? extends TypeSet {
  type Bound = either[Nothing]
  def toStr = ""
}

object empty extends ?
```

### Cons constructor

```scala
case class :~:[E, S <: TypeSet] private[:~:](head: E, tail: S) extends TypeSet {
  type Bound = tail.Bound#or[E]
  def toStr = {
    val h = head match {
      case _: String => "\""+head+"\""
      case _: Char   => "\'"+head+"\'"
      case _         => head.toString
    }
    val t = tail.toStr
    if (t.isEmpty) h else h+", "+t
  }
}
```

This `cons` method covers the `:~:` constructor to check that you are not adding a duplicate

```scala
object :~: { 
  def cons[E : in[S]#isnot, S <: TypeSet](e: E, set: S): ohnosequences.typesets.:~:[E,S] = :~:(e, set) 
}
```


### Type operators and aliases 

See implicits for these operators in the [package object](package.md)


```scala
@annotation.implicitNotFound(msg = "Can't prove that ${S} is bounded by ${B}")
sealed class  <<[S <: TypeSet : boundedBy[B]#is, B]

@annotation.implicitNotFound(msg = "Can't prove that ${S} is not bounded by ${B}")
sealed class <<![S <: TypeSet : boundedBy[B]#isnot, B]

@annotation.implicitNotFound(msg = "Can't prove that every element of ${S} is one of ${U}")
sealed class SetIsBoundedByUnion[S <: TypeSet, U <: TypeUnion]

@annotation.implicitNotFound(msg = "Can't prove that ${E} ? ${S}")
sealed class ?[E : in[S]#is, S <: TypeSet]

@annotation.implicitNotFound(msg = "Can't prove that ${E} ? ${S}")
sealed class ?[E : in[S]#isnot, S <: TypeSet]

@annotation.implicitNotFound(msg = "Can't prove that ${S} ? ${Q}")
sealed class ?[S <: TypeSet : supersetOf[Q]#is, Q <: TypeSet]

@annotation.implicitNotFound(msg = "Can't prove that ${S} ? ${Q}")
sealed class ?[S <: TypeSet : subsetOf[Q]#is, Q <: TypeSet]

@annotation.implicitNotFound(msg = "Can't prove that ${S} ~ ${Q} (i.e. that sets are type-equivalent)")
sealed class ~[S <: TypeSet : sameAs[Q]#is, Q <: TypeSet]
```

### Adding methods to TypeSet

```scala
class TypeSetOps[S <: TypeSet](set: S) {
  def :~:[E](e: E)(implicit n: E ? S) = ohnosequences.typesets.:~:.cons(e, set)

  def lookup[E](implicit e: E ? S, l: Lookup[S, E]): l.Out = l(set)
  def pop[E](implicit e: E ? S, p: Pop[S, E]): p.Out = p(set)

  def project[P <: TypeSet](implicit e: P ? S, p: Choose[S, P]): P = p(set)

  def replace[P <: TypeSet](p: P)(implicit e: P ? S, r: Replace[S, P]): S = r(set, p)

  def reorder[P <: TypeSet](implicit e: S ~ P, t: Reorder[S, P]): P = t(set)
  def ~>[P <: TypeSet](p: P)(implicit e: S ~ P, t: Reorder[S, P]): P = t(set)

  def \[Q <: TypeSet](q: Q)(implicit sub: S \ Q): sub.Out = sub(set, q)
  def ?[Q <: TypeSet](q: Q)(implicit uni: S ? Q): uni.Out = uni(set, q)

  import shapeless._
  import poly._
  def map(f: Poly)(implicit m: SetMapper[f.type, S]): m.Out = m(set)
  def mapHList(f: Poly)(implicit m: HListMapper[f.type, S]): m.Out = m(set)
  def mapList(f: Poly)(implicit m: ListMapper[f.type, S]): m.Out = m(set)

  def mapFold[R, F](z: R)(f: F)(op: (R, R) => R)
    (implicit smf: SetMapFolder[S, R, F]): R = smf(set, z, op)

  def toHList(implicit toHList: ToHList[S]): toHList.Out = toHList(set)
  def toList(implicit toList: ToList[S]): toList.Out = toList(set)
  def toListWith[O](implicit toList: ToList.Aux[S, O]): toList.Out = toList(set)
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