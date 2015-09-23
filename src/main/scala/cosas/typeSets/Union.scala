/* ## Sum/union of two type sets */

package ohnosequences.cosas.typeSets

import ohnosequences.cosas._, fns._, typeSets._

trait Union extends DepFn2[AnyTypeSet, AnyTypeSet, AnyTypeSet] {

  // use this, bound it at the end
  type union <: this.type
  val union: union

  implicit def bothHeads[SH, ST <: AnyTypeSet, QH, QT <: AnyTypeSet, O <: AnyTypeSet](implicit
    sh: SH ∉ (QH :~: QT),
    qh: QH ∉ (SH :~: ST),
    rest: App2[union, ST, QT, O]
  ): App2[union,SH :~: ST,QH :~: QT, SH :~: QH :~: O] =
    union at { (s: SH :~: ST, q: QH :~: QT) => s.head :~: q.head :~: union(s.tail, q.tail) }
}
trait Union_2 extends Union {

  implicit def qHead[S <: AnyTypeSet, QH, QT <: AnyTypeSet, O <: AnyTypeSet](implicit
    qh: QH ∈ S,
    rest: App2[union, S, QT, O]
  ): App2[union,S,QH :~: QT, O] =
    union at { (s: S, q: QH :~: QT) => union(s, q.tail) }
}
trait Union_3 extends Union_2 {

  implicit def sHead[SH, ST <: AnyTypeSet, Q <: AnyTypeSet, O <: AnyTypeSet](implicit
    sh: SH ∈ Q,
    rest: App2[union,ST,Q,O]
  ): App2[union,SH :~: ST,Q,O] =
    union at { (s: SH :~: ST, q: Q) => union(s.tail,q) }
}
trait Union_4 extends Union_3 {

  implicit def qInS[S <: AnyTypeSet.SubsetOf[Q], Q <: AnyTypeSet]: App2[union,Q,S,Q] =
    union at { (q:Q, s: S) => q }
}

case object union extends union_4 {

  type union = this.type
  val union = this

  implicit def sInQ[S <: AnyTypeSet.SubsetOf[Q], Q <: AnyTypeSet]: App2[union,S,Q,Q] =
    union at { (s:S, q:Q) => q }
}
