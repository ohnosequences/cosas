package ohnosequences.typesets

import shapeless._, poly._

trait AnyRecord extends Representable { record =>
  
  /* Any item has a fixed set of properties */
  type Properties <: TypeSet
  val  properties: Properties
  // should be provided implicitly:
  val  propertiesBound: Properties << AnyProperty

  /* Then the raw presentation of the item is kind of a record 
     in which the keys set is exactly the `Properties` type,
     i.e. it's a set of properties representations */
  type Raw <: TypeSet
  final type Entry = Raw
  // should be provided implicitly:
  val  representedProperties: Represented.By[Properties, Raw]

  /*
  ### Ops

  This extends representation type by a getter method 
  */
  // TODO is it possible to move this out of the Record type?
  implicit def propertyOps(rep: record.Rep): PropertyOps = PropertyOps(rep)
  case class   PropertyOps(rep: record.Rep) {

    def get[A <: Singleton with AnyProperty](a: A)
      (implicit 
        isThere: A ∈ record.Properties,
        lookup: Lookup[record.Raw, a.Rep]
      ): a.Rep = lookup(rep)


    def update[A <: Singleton with AnyProperty, S <: TypeSet](arep: A#Rep)
      (implicit 
        isThere: A ∈ record.Properties,
        replace: Replace[record.Raw, (A#Rep :~: ∅)]
      ): record.Rep = record ->> replace(rep, arep :~: ∅)

    def update[As <: TypeSet, S <: TypeSet](as: As)
      (implicit 
        check: As ⊂ record.Raw,
        replace: Replace[record.Raw, As]
      ): record.Rep = record ->> replace(rep, as)


    def as[I <: AnyRecord](i: I)(implicit
      project: Choose[record.Raw, i.Raw]
    ): i.Rep = i ->> project(rep)

    def as[I <: AnyRecord, Rest <: TypeSet, Uni <: TypeSet, Missing <: TypeSet](i: I, rest: Rest)
      (implicit
        missing: (i.Raw \ record.Raw) { type Out = Missing },
        allMissing: Rest ~ Missing,
        uni: (record.Raw ∪ Rest) { type Out = Uni },
        project: Choose[Uni, i.Raw]
      ): i.Rep = i ->> project(uni(rep, rest))

  }

  /* Same as just tagging with `->>`, but you can pass fields in any order */
  def fields[R <: TypeSet](r: R)(implicit 
    p: R ~> record.Raw
  ): record.Rep = record ->> p(r)
}

class Record[Ps <: TypeSet, RPs <: TypeSet](val properties: Ps)(implicit 
  val representedProperties: Represented.By[Ps, RPs],
  val propertiesBound: Ps << AnyProperty
) 
  extends AnyRecord
{

  val label = this.toString

  type Properties = Ps
  type Raw = RPs
}


/* 
  This is a generic thing for dereriving the set of representations 
  from a set of representable singletons. For example:
  ```scala
  case object id extends Property[Int]
  case object name extends Property[String]

  implicitly[Represented.By[
    id.type :~: name.type :~: ∅,
    id.Rep  :~: name.Rep  :~: ∅
  ]]
  ```

  See examples of usage it for record properties in tests
*/
@annotation.implicitNotFound(msg = "Can't construct a set of representations for ${S}")
sealed class Represented[S <: TypeSet] { type Out <: TypeSet }

object Represented {
  type By[S <: TypeSet, O <: TypeSet] = Represented[S] { type Out = O }

  implicit val empty: ∅ By ∅ = new Represented[∅] { type Out = ∅ }

  implicit def cons[H <: Singleton with Representable, T <: TypeSet]
    (implicit t: Represented[T]): (H :~: T) By (H#Rep :~: t.Out) =
          new Represented[H :~: T] { type Out = H#Rep :~: t.Out }
}


/* Takes a set of Reps and returns the set of what they represent */
import shapeless._, poly._

trait TagsOf[S <: TypeSet] extends DepFn1[S] { type Out <: TypeSet }

object TagsOf {
  def apply[S <: TypeSet](implicit keys: TagsOf[S]): Aux[S, keys.Out] = keys

  type Aux[S <: TypeSet, O <: TypeSet] = TagsOf[S] { type Out = O }

  implicit val empty: Aux[∅, ∅] =
    new TagsOf[∅] {
      type Out = ∅
      def apply(s: ∅): Out = ∅
    }

  implicit def cons[H <: Singleton with Representable, T <: TypeSet]
    (implicit fromRep: H#Rep => H, t: TagsOf[T]): Aux[H#Rep :~: T, H :~: t.Out] =
      new TagsOf[H#Rep :~: T] {
        type Out = H :~: t.Out
        def apply(s: H#Rep :~: T): Out = fromRep(s.head) :~: t(s.tail)
      }
}

//////////////////////////////////////////////

trait ListLike[L] {
  type E // elements type

  val nil: L
  def cons(h: E, t: L): L

  def head(l: L): E
  def tail(l: L): L
}

object ListLike {
  type Of[L, T] = ListLike[L] { type E = T }
}

/* Transforms a representation of item to something else */
trait FromProperties[
    A <: TypeSet, // set of properties
    Out           // what we want to get
  ] {

  type Reps <: TypeSet            // representation of properties
  type Fun <: Singleton with Poly // transformation function

  def apply(r: Reps): Out
}

object FromProperties {
  def apply[A <: TypeSet, Reps <: TypeSet, F <: Singleton with Poly, Out](implicit tr: FromProperties.Aux[A, Reps, F, Out]):
    FromProperties.Aux[A, Reps, F, Out] = tr

  type Aux[A <: TypeSet, R <: TypeSet, F <: Singleton with Poly, Out] =
    FromProperties[A, Out] { 
      type Reps = R
      type Fun = F
    }

  type Anyhow[A <: TypeSet, R <: TypeSet, Out] =
    FromProperties[A, Out] { 
      type Reps = R
    }

  implicit def empty[Out, F <: Singleton with Poly]
    (implicit m: ListLike[Out]): FromProperties.Aux[∅, ∅, F, Out] = new FromProperties[∅, Out] {
      type Reps = ∅
      type Fun = F
      def apply(r: ∅): Out = m.nil
    }

  implicit def cons[
    F <: Singleton with Poly,
    AH <: Singleton with AnyProperty, AT <: TypeSet,
    RT <: TypeSet,
    E, Out
  ](implicit
    tagOf: AH#Rep => AH,
    listLike: ListLike.Of[Out, E], 
    transform: Case1.Aux[F, (AH, AH#Rep), E], 
    recOnTail: FromProperties.Aux[AT, RT, F, Out]
  ): FromProperties.Aux[AH :~: AT, AH#Rep :~: RT, F, Out] =
    new FromProperties[AH :~: AT, Out] {
      type Reps = AH#Rep :~: RT
      type Fun = F
      def apply(r: AH#Rep :~: RT): Out = {
        listLike.cons(
          transform((tagOf(r.head), r.head)),
          recOnTail(r.tail)
        )
      }
    }
}

///////////////////////////////////////////////////////////////

/* Transforms properties set representation from something else */
trait ToProperties[
    In,          // some other representation
    A <: TypeSet // set of corresponding properties
  ] {

  type Out <: TypeSet             // representation of properties
  type Fun <: Singleton with Poly // transformation function

  def apply(in: In, a: A): Out
}

object ToProperties {
  type Aux[In, A <: TypeSet, O <: TypeSet, F <: Singleton with Poly] = ToProperties[In, A] { type Out = O; type Fun = F } 

  def apply[In, A <: TypeSet, O <: TypeSet, F <: Singleton with Poly]
    (implicit form: ToProperties.Aux[In, A, O, F]): ToProperties.Aux[In, A, O, F] = form

  implicit def empty[In, F <: Singleton with Poly]: ToProperties.Aux[In, ∅, ∅, F] = new ToProperties[In, ∅] {
      type Out = ∅
      type Fun = F
      def apply(in: In, a: ∅): Out = ∅
    }

  implicit def cons[
    In,
    AH <: Singleton with AnyProperty, AT <: TypeSet,
    RH <: AH#Rep, RT <: TypeSet,
    F <: Singleton with Poly
  ](implicit
    f: Case1.Aux[F, (In, AH), RH], 
    t: ToProperties.Aux[In, AT, RT, F]
  ): ToProperties.Aux[In, AH :~: AT, RH :~: RT, F] =
    new  ToProperties[In, AH :~: AT] {
      type Out = RH :~: RT
      type Fun = F
      def apply(in: In, a: AH :~: AT): Out = f((in, a.head)) :~: t(in, a.tail)
    }
}
