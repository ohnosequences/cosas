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
      split: AnyApp1At[Split[E], L] { type Y = (OL,E,OR) }
    )
    : (OL, E, OR) =
      split(l)

    def splitS[
      E >: X <: L#Bound,
      OL <: AnyKList,
      X,
      OR <: AnyKList  {type Bound = OL#Bound }
    ](w: Witness[E])(implicit
      splitS: AnyApp1At[SplitS[E], L] { type Y = (OL,X,OR) }
    )
    : (OL, X, OR) =
      splitS(l)

    def ::[E <: L#Bound](e: E)
    : E :: L =
      KCons[E,L](e, l)

    def find[A <: L#Bound](implicit findIn: AnyApp1At[findIn[A], L] { type Y = A })
    : A =
      findIn(l)

    def findS[Z, X <: Z](w: Witness[Z])(implicit
      findIn: AnyApp1At[FindS[Z], L] { type Y = X }
    )
    : X =
      findIn(l)

    def pick[E <: L#Bound, O <: AnyKList.Of[L#Bound]](w: Witness[E])(implicit
      p: AnyApp1At[pick[E], L] { type Y = (E,O) }
    )
    : (E,O) =
      p(l)

    def pickS[E, X <: E, O <: AnyKList](w: Witness[E])(implicit
      p: AnyApp1At[PickS[E], L] { type Y = (X,O)}
    )
    : (X,O) =
      p(l)

    def takeFirst[Q <: AnyKList.Of[L#Bound]](w: Witness[Q])(implicit
      takeFirst: AnyApp1At[takeFirst[Q], L] { type Y = Q }
    )
    : Q =
      takeFirst(l)

    // def takeFirstS[Q >: QO <: AnyKList.Of[L#Bound], QO](w: Witness[Q])(implicit
    //   takeFirstS: AnyApp1At[TakeFirstS[Q], L] { type Y = QO }
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
      implicit mapper: AnyApp2At[MapKListOf[F,U0], F, L] { type Y = O0 }
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
