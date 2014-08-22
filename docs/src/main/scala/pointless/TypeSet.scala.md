
```scala
package ohnosequences.pointless

import AnyFn._, typeUnion._, taggedType._
import shapeless.{ HList, Poly1, <:!<, =:!= }

object typeSet {

  // type typeUnion <: anyTypeUnion

```


### ADT


```scala
  sealed trait AnyTypeSet {

    type Types <: AnyTypeUnion

    def toStr: String
    override def toString = "{" + toStr + "}"
  }


  sealed trait AnyEmptySet extends AnyTypeSet {

    type Types = either[Nothing]
    def toStr = ""
  }

  private object EmptySet extends AnyEmptySet


  protected case class ConsSet[E, S <: AnyTypeSet](head: E, tail: S)(implicit check: E ∉ S) extends AnyTypeSet {

    type Types = tail.Types#or[E]
    
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

This method covers constructor to check that you are not adding a duplicate

```scala
  private object ConsSet {
    def cons[E, S <: AnyTypeSet](e: E, set: S)(implicit check: E ∉ S): ConsSet[E,S] = ConsSet(e, set) 
  }


  final type ∅ = AnyEmptySet
  val ∅ : ∅ = EmptySet // the space before : is needed
  val emptySet : ∅ = EmptySet

  final type :~:[E, S <: AnyTypeSet] = ConsSet[E, S]
```


### Predicates and aliases

An element is in the set

```scala
  @annotation.implicitNotFound(msg = "Can't prove that ${E} is an element of ${S}")
  type    isIn[E, S <: AnyTypeSet] = E    isOneOf S#Types
  final type ∈[E, S <: AnyTypeSet] = E isIn S

  @annotation.implicitNotFound(msg = "Can't prove that ${E} is not an element of ${S}")
  type isNotIn[E, S <: AnyTypeSet] = E isNotOneOf S#Types
  final type ∉[E, S <: AnyTypeSet] = E isNotIn S

  final type in[S <: AnyTypeSet] = {
    type    is[E] = E    isIn S
    type isNot[E] = E isNotIn S
  }
```

One set is a subset of another

```scala
  @annotation.implicitNotFound(msg = "Can't prove that ${Q} is a subset of ${Q}")
  type isSubsetOf[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Types#union <:<  Q#Types#union 
  final type ⊂[S <: AnyTypeSet, Q <: AnyTypeSet] = S isSubsetOf Q

  @annotation.implicitNotFound(msg = "Can't prove that ${Q} is not a subset of ${Q}")
  type isNotSubsetOf[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Types#union <:!< Q#Types#union
  final type ⊄[S <: AnyTypeSet, Q <: AnyTypeSet] = S isNotSubsetOf Q

  final type subsetOf[Q <: AnyTypeSet] = {
    type    is[S <: AnyTypeSet] = S    isSubsetOf Q
    type isNot[S <: AnyTypeSet] = S isNotSubsetOf Q
  }
```

Two sets have the same type union bound

```scala
  @annotation.implicitNotFound(msg = "Can't prove that ${S} is the same as ${Q}")
  type isSameAs[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Types#union =:=  Q#Types#union
  type ~:~[S <: AnyTypeSet, Q <: AnyTypeSet] = S isSameAs Q

  @annotation.implicitNotFound(msg = "Can't prove that ${S} is not the same as ${Q}")
  type isNotSameAs[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Types#union =:!= Q#Types#union
  type ~:!~[S <: AnyTypeSet, Q <: AnyTypeSet] = S isNotSameAs Q

  final type sameAs[Q <: AnyTypeSet] = {
    type    is[S <: AnyTypeSet] = S    isSameAs Q
    type isNot[S <: AnyTypeSet] = S isNotSameAs Q
  }
```

Elements of the set are bounded by the type

```scala
  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are bounded by ${B}")
  type isBoundedBy[S <: AnyTypeSet, B] = S#Types#union <:<  either[B]#union

  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are not bounded by ${B}")
  type isNotBoundedBy[S <: AnyTypeSet, B] = S#Types#union <:!< either[B]#union

  final type boundedBy[B] = {
    type    is[S <: AnyTypeSet] = S    isBoundedBy B
    type isNot[S <: AnyTypeSet] = S isNotBoundedBy B
  }
```

Elements of the set are from the type union

```scala
  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are from the type union ${U}")
  type    isBoundedByUnion[S <: AnyTypeSet, U <: AnyTypeUnion] = S#Types#union <:<  U#union

  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are not from the type union ${U}")
  type isNotBoundedByUnion[S <: AnyTypeSet, U <: AnyTypeUnion] = S#Types#union <:!< U#union

  final type boundedByUnion[U <: AnyTypeUnion] = {
    type    is[S <: AnyTypeSet] = S    isBoundedByUnion U
    type isNot[S <: AnyTypeSet] = S isNotBoundedByUnion U
  }
```

One set consists of representations of the types in another

```scala
  @annotation.implicitNotFound(msg = "Can't prove that ${S} is represented by ${R}")
  type isRepresentedBy[S <: AnyTypeSet, R <: AnyTypeSet] = ops.typeSet.Represented[S] with out[R]

  // type tagsOf[S <: AnyTypeSet, R <: AnyTypeSet] = ops.typeSet.TagsOf[S] with out[R]


  // object AnyTypeSet {

```


### Functions


```scala
  type \[S <: AnyTypeSet, Q <: AnyTypeSet] = ops.typeSet.Subtract[S, Q]

  type ∪[S <: AnyTypeSet, Q <: AnyTypeSet] = ops.typeSet.Union[S, Q]

  type Pop[S <: AnyTypeSet, E] = ops.typeSet.Pop[S, E]

  type Lookup[S <: AnyTypeSet, E] = ops.typeSet.Lookup[S, E]

  type Take[S <: AnyTypeSet, Q <: AnyTypeSet] = ops.typeSet.Take[S, Q]

  type Replace[S <: AnyTypeSet, Q <: AnyTypeSet] = ops.typeSet.Replace[S, Q]

  type As[S <: AnyTypeSet, Q <: AnyTypeSet] = ops.typeSet.As[S, Q]

  type FromHList[L <: HList] = ops.typeSet.FromHList[L]

  type ToHList[S <: AnyTypeSet] = ops.typeSet.ToHList[S]

  type  ToList[S <: AnyTypeSet] = ops.typeSet.ToList[S]

  final type ToListOf[S <: AnyTypeSet, T] = ToList[S] with o[T]

  type MapToHList[F <: Poly1, S <: AnyTypeSet] = ops.typeSet.MapToHList[F, S]

  type  MapToList[F <: Poly1, S <: AnyTypeSet] = ops.typeSet.MapToList[F, S]

  type     MapSet[F <: Poly1, S <: AnyTypeSet] = ops.typeSet.MapSet[F, S]

  type MapFoldSet[F <: Poly1, S <: AnyTypeSet, R] = ops.typeSet.MapFoldSet[F, S, R]
```


Ops


```scala
  implicit def typeSetOps[S <: AnyTypeSet](s: S): Ops[S] = Ops[S](s)
  case class   Ops[S <: AnyTypeSet](s: S) {
```

Element-related

```scala
    def :~:[E](e: E)(implicit check: E ∉ S): (E :~: S) = ConsSet.cons(e, s)

    def pop[E](implicit check: E ∈ S, pop: S Pop E): pop.Out = pop(s)

    def lookup[E](implicit check: E ∈ S, lookup: S Lookup E): lookup.Out = lookup(s)
```

Set-related

```scala
    def \[Q <: AnyTypeSet](q: Q)(implicit sub: S \ Q): sub.Out = sub(s, q)

    def ∪[Q <: AnyTypeSet](q: Q)(implicit uni: S ∪ Q): uni.Out = uni(s, q)

    def take[Q <: AnyTypeSet](implicit check: Q ⊂ S, take: S Take Q): take.Out = take(s)

    def replace[Q <: AnyTypeSet](q: Q)(implicit check: Q ⊂ S, replace: S Replace Q): replace.Out = replace(s, q)
```

Conversions

```scala
    def as[Q <: AnyTypeSet]      (implicit check: S ~:~ Q, reorder: S As Q): reorder.Out = reorder(s)
    def as[Q <: AnyTypeSet](q: Q)(implicit check: S ~:~ Q, reorder: S As Q): reorder.Out = reorder(s)

    def toHList(implicit toHList: ToHList[S]): toHList.Out = toHList(s)

    def  toList(implicit  toList:  ToList[S]):  toList.Out =  toList(s)

    def toListOf[T](implicit toListOf: S ToListOf T): List[T] = toListOf(s)
```

Mappers

```scala
    def mapToHList[F <: Poly1](f: F)(implicit mapF: F MapToHList S): mapF.Out = mapF(s)

    def  mapToList[F <: Poly1](f: F)(implicit mapF: F  MapToList S): mapF.Out = mapF(s)

    def        map[F <: Poly1](f: F)(implicit mapF: F     MapSet S): mapF.Out = mapF(s)

    def mapFold[F <: Poly1, R](f: F)(r: R)(op: (R, R) => R)(implicit mapFold: MapFoldSet[F, S, R]): mapFold.Out = mapFold(s, r, op)

  }

  implicit def hListOps[L <: HList](l: L): HListOps[L] = HListOps[L](l)
  case class   HListOps[L <: HList](l: L) {

    def toTypeSet(implicit fromHList: FromHList[L]): fromHList.Out = fromHList(l)
  }

  def fromHList[L <: HList](l: L)(implicit fromHList: FromHList[L]): fromHList.Out = fromHList(l)

}

```


------

### Index

+ src
  + test
    + scala
      + pointless
        + [PropertyTests.scala][test/scala/pointless/PropertyTests.scala]
        + [RecordTests.scala][test/scala/pointless/RecordTests.scala]
        + [TypeSetTests.scala][test/scala/pointless/TypeSetTests.scala]
  + main
    + scala
      + pointless
        + [TaggedType.scala][main/scala/pointless/TaggedType.scala]
        + [Record.scala][main/scala/pointless/Record.scala]
        + ops
          + typeSet
            + [Lookup.scala][main/scala/pointless/ops/typeSet/Lookup.scala]
            + [Reorder.scala][main/scala/pointless/ops/typeSet/Reorder.scala]
            + [Conversions.scala][main/scala/pointless/ops/typeSet/Conversions.scala]
            + [Subtract.scala][main/scala/pointless/ops/typeSet/Subtract.scala]
            + [Pop.scala][main/scala/pointless/ops/typeSet/Pop.scala]
            + [Representations.scala][main/scala/pointless/ops/typeSet/Representations.scala]
            + [Replace.scala][main/scala/pointless/ops/typeSet/Replace.scala]
            + [Take.scala][main/scala/pointless/ops/typeSet/Take.scala]
            + [Union.scala][main/scala/pointless/ops/typeSet/Union.scala]
            + [Mappers.scala][main/scala/pointless/ops/typeSet/Mappers.scala]
          + record
            + [Update.scala][main/scala/pointless/ops/record/Update.scala]
            + [Conversions.scala][main/scala/pointless/ops/record/Conversions.scala]
            + [Get.scala][main/scala/pointless/ops/record/Get.scala]
        + [Denotation.scala][main/scala/pointless/Denotation.scala]
        + [TypeUnion.scala][main/scala/pointless/TypeUnion.scala]
        + [Fn.scala][main/scala/pointless/Fn.scala]
        + [Property.scala][main/scala/pointless/Property.scala]
        + [TypeSet.scala][main/scala/pointless/TypeSet.scala]

[test/scala/pointless/PropertyTests.scala]: ../../../test/scala/pointless/PropertyTests.scala.md
[test/scala/pointless/RecordTests.scala]: ../../../test/scala/pointless/RecordTests.scala.md
[test/scala/pointless/TypeSetTests.scala]: ../../../test/scala/pointless/TypeSetTests.scala.md
[main/scala/pointless/TaggedType.scala]: TaggedType.scala.md
[main/scala/pointless/Record.scala]: Record.scala.md
[main/scala/pointless/ops/typeSet/Lookup.scala]: ops/typeSet/Lookup.scala.md
[main/scala/pointless/ops/typeSet/Reorder.scala]: ops/typeSet/Reorder.scala.md
[main/scala/pointless/ops/typeSet/Conversions.scala]: ops/typeSet/Conversions.scala.md
[main/scala/pointless/ops/typeSet/Subtract.scala]: ops/typeSet/Subtract.scala.md
[main/scala/pointless/ops/typeSet/Pop.scala]: ops/typeSet/Pop.scala.md
[main/scala/pointless/ops/typeSet/Representations.scala]: ops/typeSet/Representations.scala.md
[main/scala/pointless/ops/typeSet/Replace.scala]: ops/typeSet/Replace.scala.md
[main/scala/pointless/ops/typeSet/Take.scala]: ops/typeSet/Take.scala.md
[main/scala/pointless/ops/typeSet/Union.scala]: ops/typeSet/Union.scala.md
[main/scala/pointless/ops/typeSet/Mappers.scala]: ops/typeSet/Mappers.scala.md
[main/scala/pointless/ops/record/Update.scala]: ops/record/Update.scala.md
[main/scala/pointless/ops/record/Conversions.scala]: ops/record/Conversions.scala.md
[main/scala/pointless/ops/record/Get.scala]: ops/record/Get.scala.md
[main/scala/pointless/Denotation.scala]: Denotation.scala.md
[main/scala/pointless/TypeUnion.scala]: TypeUnion.scala.md
[main/scala/pointless/Fn.scala]: Fn.scala.md
[main/scala/pointless/Property.scala]: Property.scala.md
[main/scala/pointless/TypeSet.scala]: TypeSet.scala.md