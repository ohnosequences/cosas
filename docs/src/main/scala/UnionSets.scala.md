## Sum/union of two type sets

```scala
package ohnosequences.typesets


trait UnionSets[S <: TypeSet, Q <: TypeSet] {
  type Out <: TypeSet
  def apply(s: S, q: Q): Out
}
```

* Case when S is a subset of Q => just Q:

```scala
object UnionSets extends UnionSets_2 {
  implicit def sInQ[S <: TypeSet, Q <: TypeSet]
    (implicit e: S ? Q) =
      new UnionSets[S, Q] { type Out = Q
        def apply(s: S, q: Q) = q
      }
}
```

* (Dual) case when Q is a subset of S => just S:

```scala
trait UnionSets_2 extends UnionSets_3 {
  implicit def qInS[S <: TypeSet, Q <: TypeSet]
    (implicit e: Q ? S) =
      new UnionSets[S, Q] { type Out = S
        def apply(s: S, q: Q) = s
      }
}
```

* Case when S.head is in Q => throwing it away:

```scala
trait UnionSets_3 extends UnionSets_4 {
  implicit def sConsWithoutHead[SH, ST <: TypeSet,  Q <: TypeSet]
    (implicit sh: SH ? Q, rest: ST U Q) =
      new UnionSets[ SH :~: ST,    Q] { type Out = rest.Out
        def apply(s: SH :~: ST, q: Q) = rest(s.tail, q)
      }
}
```

* (Dual) case when Q.head is in S => throwing it away:

```scala
trait UnionSets_4 extends UnionSets_5 {
  implicit def qConsWithoutHead[S <: TypeSet,  QH, QT <: TypeSet]
    (implicit qh: QH ? S, rest: S U QT) =
      new UnionSets[ S,    QH :~: QT] { type Out = rest.Out
        def apply(s: S, q: QH :~: QT) = rest(s, q.tail)
      }
}
```

* Otherwise both heads are new => adding both:

```scala
trait UnionSets_5 {
  implicit def newHeads[SH, ST <: TypeSet,  QH, QT <: TypeSet]
    (implicit rest: ST U QT) =
      new UnionSets[ SH :~: ST,    QH :~: QT] { type Out = SH :~: QH :~: rest.Out
        def apply(s: SH :~: ST, q: QH :~: QT) = s.head :~: q.head :~: rest(s.tail, q.tail)
      }
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