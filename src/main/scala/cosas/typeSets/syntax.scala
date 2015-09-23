package ohnosequences.cosas.typeSets

import shapeless.{union => _, _}
import ohnosequences.cosas._, typeSets._, types._, fns._

case object syntax {

  case class TypeSetSyntax[S <: AnyTypeSet](val s: S) extends AnyVal {

    /* Element-related */

    def :~:[E](e: E)(implicit check: E ∉ S): (E :~: S) = TypeSetImpl.ConsSet.cons(e, s) : (E :~: S)

    // def pop[E](implicit pop: S Pop E): pop.Out = pop(s)

    // def lookup[E](implicit check: E ∈ S, lookup: S Lookup E): E = lookup(s)

    // deletes the first element of type E
    // def delete[E](implicit check: E ∈ S, del: S Delete E): del.Out = del(s)

    /* Set-related */

    // def \[Q <: AnyTypeSet](q: Q)(implicit sub: S \ Q): sub.Out = sub(s, q)

    def ∪[Q <: AnyTypeSet, O <: AnyTypeSet](q: Q)(implicit evU: App2[union, S, Q, O]): O = union(s, q)

    // def take[Q <: AnyTypeSet](implicit check: Q ⊂ S, take: S Take Q): take.Out = take(s)

    // def replace[Q <: AnyTypeSet](q: Q)(implicit check: Q ⊂ S, replace: S Replace Q): replace.Out = replace(s, q)


    /* Conversions */

    def reorderTo[Q <: AnyTypeSet](implicit
      // check: S ~:~ Q,
      reorderTo: App1[reorderTo[Q],S,Q]
    ): Q = reorderTo(s)

    // def reorderTo[Q <: AnyTypeSet](q: Q)(implicit check: S ~:~ Q, reorder: S ReorderTo Q): reorder.Out = reorder(s)
    //
    // def toHList(implicit toHList: ToHList[S]): toHList.Out = toHList(s)

    // def  toList(implicit  toList:  ToList[S]):  toList.Out =  toList(s)

    def toListOf[T](implicit  toListOf: App1[toListOf[T], S, List[T]]): List[T] = toListOf(s)

    // def getTypes[X](implicit types: TypesOf[S] { type Out = X }): X = types(s)

    /* Mappers */

    // def mapToHList[F <: Poly1](f: F)(implicit mapF: F MapToHList S): mapF.Out = mapF(s)

    // def  mapToList[F <: Poly1](f: F)(implicit mapF: F  MapToList S): mapF.Out = mapF(s)
    //
    // def        map[F <: Poly1](f: F)(implicit mapF: F     MapSet S): mapF.Out = mapF(s)
    //
    // def mapFold[F <: Poly1, R](f: F)(r: R)(op: (R, R) => R)(implicit mapFold: MapFoldSet[F, S, R]): mapFold.Out = mapFold(s, r, op)
    //
    // /* Predicates */
    //
    // def filter[P <: AnyTypePredicate](implicit fltr: Filter[S, P]): fltr.Out = fltr(s)
    //
    // def checkForAll[P <: AnyTypePredicate](implicit prove: CheckForAll[S, P]): CheckForAll[S, P] = prove
    //
    // def checkForAny[P <: AnyTypePredicate](implicit prove: CheckForAny[S, P]): CheckForAny[S, P] = prove
  }

  case class DenotationsSetSyntax[DS <: AnyTypeSet.Of[AnyDenotation]](val ds: DS) extends AnyVal {

    // def toMap[T <: AnyType, V](implicit toMap: ToMap[DS, T, V]): Map[T, V] = toMap(ds)
    //
    // def serialize[V](implicit serialize: SerializeDenotations[DS, V]): Either[SerializeDenotationsError, Map[String,V]] =
    //   serialize(ds, Map())
    //
    // def serialize[V](map: Map[String,V])(implicit serialize: SerializeDenotations[DS, V]): Either[SerializeDenotationsError, Map[String,V]] =
    //   serialize(ds, map)
  }

  case class HListSyntax[L <: HList](l: L) extends AnyVal {

    // def toTypeSet(implicit fromHList: FromHList[L]): fromHList.Out = fromHList(l)
  }
}
