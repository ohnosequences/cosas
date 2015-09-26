package ohnosequences.cosas.typeSets

import ohnosequences.cosas._, typeSets._, types._, fns._

case object syntax {

  case class TypeSetSyntax[S <: AnyTypeSet](val s: S) extends AnyVal {

    /* Element-related */

    def :~:[E](e: E)(implicit check: E ∉ S): (E :~: S) = TypeSetImpl.ConsSet.cons(e, s) : (E :~: S)

    // def pop[E](implicit pop: S Pop E): pop.Out = pop(s)

    def lookup[E](implicit check: E ∈ S, lookup: App1[lookup[E],S,E]): E = lookup(s)

    // deletes the first element of type E
    // def delete[E](implicit check: E ∈ S, del: S Delete E): del.Out = del(s)

    /* Set-related */

    // def \[Q <: AnyTypeSet](q: Q)(implicit sub: S \ Q): sub.Out = sub(s, q)

    def ∪[Q <: AnyTypeSet, O <: AnyTypeSet](q: Q)(implicit evU: App2[union, S, Q, O]): O = union(s, q)

    def take[Q <: AnyTypeSet](implicit
      check: Q ⊂ S,
      take: App1[take[Q],S,Q]
    ): Q = take(s)

    def replace[Q <: AnyTypeSet](q: Q)(implicit
      check: Q ⊂ S,
      replace: App2[replace[S],S,Q,S]
    ): S = replace(s, q)


    /* Conversions */

    def reorderTo[Q <: AnyTypeSet](implicit
      reorderTo: App1[reorderTo[Q],S,Q]
    ): Q = reorderTo(s)

    // def toHList(implicit toHList: ToHList[S]): toHList.Out = toHList(s)

    def toList[X](implicit toList: App1[toListOf[X],S,List[X]]): List[X] = toList(s)

    // def getTypes[X](implicit types: TypesOf[S] { type Out = X }): X = types(s)

    /* Mappers */

    // def mapToHList[F <: Poly1](f: F)(implicit mapF: F MapToHList S): mapF.Out = mapF(s)

    def  mapToList[F <: AnyDepFn1,X](f: F)(implicit
      maptolistof: App2[mapToListOf[X],F,S,List[X]]
    ): List[X] = maptolistof(f,s)

    def map[F <: AnyDepFn1, O <: AnyTypeSet](f: F)(implicit
      mapSet: App2[mapSet,F,S,O]
    ): O = mapSet(f,s)


    //
    // /* Predicates */
    //
    // def filter[P <: AnyTypePredicate, O <: AnyTypeSet](implicit
    //   fltr: Filter[S, P]): fltr.Out = fltr(s)
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

}
