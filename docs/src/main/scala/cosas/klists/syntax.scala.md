
```scala
package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

case object syntax {

  case class KListSyntax[L <: AnyKList](val l: L) extends AnyVal {

    def head[H <: L#Bound, T <: AnyKList.Of[L#Bound]](implicit c: IsKCons[L,H,T])
    : H =
      c.h(l)

    def tail[H <: L#Bound, T <: AnyKList.Of[L#Bound]](implicit c: IsKCons[L,H,T])
    : T =
      c.t(l)

    def at[N <: AnyNat, Z <: L#Bound](n: N)(implicit a: AnyApp1At[L at N, L] { type Y = Z })
    : Z =
      a(l)

    def take[N <: AnyNat, Z <: AnyKList.Of[L#Bound]](n: N)(implicit take: AnyApp1At[take[N], L] {type Y = Z })
    : Z =
      take(l)

    def drop[N <: AnyNat, Z <: AnyKList.Of[L#Bound]](n: N)(implicit drop: AnyApp1At[drop[N], L] { type Y = Z })
    : Z =
      drop(l)

    def slice[A <: AnyNat, B <: AnyNat, Z <: AnyKList.Of[L#Bound]](start: A, end: B)(implicit
      sp: AnyApp1At[slice[A,B], L] { type Y = Z }
    )
    : Z =
      sp(l)

    def split[
      OL <: AnyKList,
      E <: L#Bound,
      OR <: AnyKList  {type Bound = OL#Bound }
    ](w: Witness[E])(implicit
      split: AnyApp1At[split[E], L] { type Y = (OL,E,OR) }
    )
    : (OL, E, OR) =
      split(l)

    def splitS[
      E >: X <: L#Bound,
      OL <: AnyKList,
      X,
      OR <: AnyKList  {type Bound = OL#Bound }
    ](w: Witness[E])(implicit
      splitS: AnyApp1At[splitS[E], L] { type Y = (OL,X,OR) }
    )
    : (OL, X, OR) =
      splitS(l)

    def ::[E <: L#Bound](e: E)
    : E :: L =
      KCons[E,L](e, l)

    def find[A <: L#Bound](implicit find: AnyApp1At[find[A], L] { type Y = A })
    : A =
      find(l)

    def findS[Z, X <: Z](w: Witness[Z])(implicit
      find: AnyApp1At[FindS[Z], L] { type Y = X }
    )
    : X =
      find(l)

    def pick[E <: L#Bound, O <: AnyKList.Of[L#Bound]](w: Witness[E])(implicit
      p: AnyApp1At[pick[E], L] { type Y = (E,O) }
    )
    : (E,O) =
      p(l)

    def pickS[E, X <: E, O <: AnyKList](w: Witness[E])(implicit
      p: AnyApp1At[pickS[E], L] { type Y = (X,O)}
    )
    : (X,O) =
      p(l)

    def takeFirst[Q <: AnyKList.Of[L#Bound]](w: Witness[Q])(implicit
      takeFirst: AnyApp1At[takeFirst[Q], L] { type Y = Q }
    )
    : Q =
      takeFirst(l)

    // def takeFirstS[Q >: QO <: AnyKList.Of[L#Bound], QO](w: Witness[Q])(implicit
    //   takeFirstS: AnyApp1At[takeFirstS[Q], L] { type Y = QO }
    // )
    // : QO =
    //   takeFirstS(l)

    def replaceFirst[S <: AnyKList { type Bound = L#Bound }](s: S)(implicit
      replaceFirst: AnyApp2At[replace[L], L, S] { type Y = L }
    )
    : L =
      replaceFirst(l,s)

    def asList: List[L#Bound] = {

      // so tailrec, much constant append, very mutable
      @scala.annotation.tailrec
      def asList_rec[X](
        list: AnyKList.Of[X],
        acc: scala.collection.mutable.ListBuffer[X]
      ): List[X] = list match {
        case KNilOf() => acc.toList
        case xs: KCons[X,AnyKList.Of[X]] => asList_rec[X](xs.tail, acc += xs.head)
      }

      asList_rec(l, new scala.collection.mutable.ListBuffer())
    }

    def toList(implicit conv: App1[toList[L], L, List[L#Bound]])
    : List[L#Bound] =
      conv(l)

    def toListOf[B >: L#Bound](implicit conv: App1[toListOf[L,B], L, List[B]])
    : List[B] =
      conv(l)

    def ++[
      M <: AnyKList { type Bound >: L#Bound },
      LM <: AnyKList { type Bound = M#Bound }
    ](m: M)(implicit
      foldr: AnyApp3At[FoldRight[cons], cons, M, L] { type Y = LM }
    ): LM =
      foldr(cons, m, l)

    def map[
      F <: AnyDepFn1 { type In1 >: L#Bound; type Out >: U0 },
      U0,
      O0 <: AnyKList { type Bound = U0 }
    ](f: F)(
      implicit mapper: AnyApp2At[mapKList[F,U0], F, L] { type Y = O0 }
    )
    : O0 =
      mapper(f,l)

    // reverse = snoc.foldLeft(Nil)
    def reverse[
      R <: AnyKList.withBound[L#Bound]
    ](implicit
      foldl: AnyApp3At[FoldLeft[snoc], snoc, *[L#Bound], L] { type Y = R }
    ): R =
      foldl(snoc, *[L#Bound], l)

    def filter[
      P <: AnyPredicate { type In1 >: L#Bound },
      O <: AnyKList.Of[P#In1]
    ](p: P)(implicit
      appFilter: AnyApp2At[filter[P], P, L] { type Y = O }
    ): O = appFilter(p, l)
  }
}

```




[test/scala/cosas/asserts.scala]: ../../../../test/scala/cosas/asserts.scala.md
[test/scala/cosas/DenotationTests.scala]: ../../../../test/scala/cosas/DenotationTests.scala.md
[test/scala/cosas/EqualityTests.scala]: ../../../../test/scala/cosas/EqualityTests.scala.md
[test/scala/cosas/DependentFunctionsTests.scala]: ../../../../test/scala/cosas/DependentFunctionsTests.scala.md
[test/scala/cosas/KListsTests.scala]: ../../../../test/scala/cosas/KListsTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/NatTests.scala]: ../../../../test/scala/cosas/NatTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: ../../../../test/scala/cosas/TypeUnionTests.scala.md
[main/scala/cosas/package.scala]: ../package.scala.md
[main/scala/cosas/types/package.scala]: ../types/package.scala.md
[main/scala/cosas/types/types.scala]: ../types/types.scala.md
[main/scala/cosas/types/parsing.scala]: ../types/parsing.scala.md
[main/scala/cosas/types/productTypes.scala]: ../types/productTypes.scala.md
[main/scala/cosas/types/syntax.scala]: ../types/syntax.scala.md
[main/scala/cosas/types/project.scala]: ../types/project.scala.md
[main/scala/cosas/types/denotations.scala]: ../types/denotations.scala.md
[main/scala/cosas/types/functionTypes.scala]: ../types/functionTypes.scala.md
[main/scala/cosas/types/serialization.scala]: ../types/serialization.scala.md
[main/scala/cosas/klists/replace.scala]: replace.scala.md
[main/scala/cosas/klists/cons.scala]: cons.scala.md
[main/scala/cosas/klists/klists.scala]: klists.scala.md
[main/scala/cosas/klists/take.scala]: take.scala.md
[main/scala/cosas/klists/package.scala]: package.scala.md
[main/scala/cosas/klists/takeFirst.scala]: takeFirst.scala.md
[main/scala/cosas/klists/toList.scala]: toList.scala.md
[main/scala/cosas/klists/filter.scala]: filter.scala.md
[main/scala/cosas/klists/pick.scala]: pick.scala.md
[main/scala/cosas/klists/drop.scala]: drop.scala.md
[main/scala/cosas/klists/map.scala]: map.scala.md
[main/scala/cosas/klists/at.scala]: at.scala.md
[main/scala/cosas/klists/syntax.scala]: syntax.scala.md
[main/scala/cosas/klists/fold.scala]: fold.scala.md
[main/scala/cosas/klists/noDuplicates.scala]: noDuplicates.scala.md
[main/scala/cosas/klists/slice.scala]: slice.scala.md
[main/scala/cosas/klists/find.scala]: find.scala.md
[main/scala/cosas/records/package.scala]: ../records/package.scala.md
[main/scala/cosas/records/recordTypes.scala]: ../records/recordTypes.scala.md
[main/scala/cosas/records/syntax.scala]: ../records/syntax.scala.md
[main/scala/cosas/records/reorder.scala]: ../records/reorder.scala.md
[main/scala/cosas/typeUnions/typeUnions.scala]: ../typeUnions/typeUnions.scala.md
[main/scala/cosas/typeUnions/package.scala]: ../typeUnions/package.scala.md
[main/scala/cosas/fns/predicates.scala]: ../fns/predicates.scala.md
[main/scala/cosas/fns/instances.scala]: ../fns/instances.scala.md
[main/scala/cosas/fns/package.scala]: ../fns/package.scala.md
[main/scala/cosas/fns/syntax.scala]: ../fns/syntax.scala.md
[main/scala/cosas/fns/functions.scala]: ../fns/functions.scala.md
[main/scala/cosas/subtyping.scala]: ../subtyping.scala.md
[main/scala/cosas/witness.scala]: ../witness.scala.md
[main/scala/cosas/equality.scala]: ../equality.scala.md
[main/scala/cosas/Nat.scala]: ../Nat.scala.md
[main/scala/cosas/Bool.scala]: ../Bool.scala.md