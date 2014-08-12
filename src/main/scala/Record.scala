package ohnosequences.typesets

import shapeless._, poly._

import AnyTag._

/*
  - `Properties`: `(id :~: name :~: ∅)
  - `Values`: `(id.Rep :~: name.Rep :~: ∅)`
  - `Entry`: `this.Rep = (id.Rep :~: name.Rep :~: ∅) AsRepOf this.type`
*/
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

  // should be provided implicitly:
  val  representedProperties: Raw isValuesOf Properties

  /* Same as just tagging with `->>`, but you can pass fields in any order */
  def fields[Vs <: TypeSet](values: Vs)(implicit 
    p: Vs ~> RawOf[record.type]
  ): RepOf[record.type] = record ->> p(values)
}

object AnyRecord {

  // extractors
  type withProperties[Ps <: TypeSet] = AnyRecord { type Properties = Ps }

  // accessors
  type PropertiesOf[R <: AnyRecord] = R#Properties

  implicit def otherPropertyOps[R <: AnyRecord](
    entry: RepOf[R]
  ): PropertyOps[R] = PropertyOps(entry)//PropertyOps(entry, getR(entry))
  

  case class PropertyOps[R <: AnyRecord](val recordEntry: RepOf[R]) {

    def getIt[P <: AnyProperty](p: P)
      (implicit 
        isThere: P ∈ PropertiesOf[R],
        lookup: Lookup[RawOf[R], RepOf[P]]
      ): RepOf[P] = lookup(recordEntry)

    def get[P <: AnyProperty](p: P)
      (implicit 
        isThere: P ∈ PropertiesOf[R],
        lookup: Lookup[RawOf[R], RepOf[P]]
      ): RepOf[P] = lookup(recordEntry)


    def update[P <: AnyProperty, S <: TypeSet](pEntry: RepOf[P])
      (implicit 
        isThere: P ∈ PropertiesOf[R],
        replace: Replace[RepOf[R], (RepOf[P] :~: ∅)]
      ): RepOf[R] = {

        replace(recordEntry, pEntry :~: ∅)
      }

    def update[Ps <: TypeSet, S <: TypeSet](pEntries: Ps)
      (implicit 
        check: Ps ⊂ RepOf[R],
        replace: Replace[RepOf[R], Ps]
      )
      : RepOf[R] = replace( recordEntry , pEntries )


    def as[Other <: AnyRecord](other: Other)
    (implicit
      project: Choose[RawOf[R], RawOf[Other]]
    )
    : RepOf[Other] = other =>> project(recordEntry)

    def as[
      Other <: AnyRecord,
      Rest <: TypeSet, 
      Uni <: TypeSet,
      Missing <: TypeSet
    ]
    (
      other: Other, rest: Rest
    )
    (implicit
      missing: (RawOf[Other] \ RawOf[R]) { type Out = Missing },
      allMissing: Rest ~ Missing,
      uni: (RawOf[R] ∪ Rest) { type Out = Uni },
      project: Choose[Uni, RawOf[Other]]
    )
    : RepOf[Other] = other =>> project(uni(recordEntry, rest))

  }
}


class Record[Ps <: TypeSet, Vs <: TypeSet](val properties: Ps)(implicit 
  val representedProperties: Vs isValuesOf Ps,
  val propertiesBound: Ps << AnyProperty
) 
  extends AnyRecord
{

  val label = this.toString

  type Properties = Ps
  type Raw = Vs
}









/* 
  This is a generic thing for deriving the set of representations 
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
    (implicit t: Represented[T]): (H :~: T) By (RepOf[H] :~: t.Out) =
          new Represented[H :~: T] { type Out = RepOf[H] :~: t.Out }
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
    (implicit fromRep: RepOf[H] => H, t: TagsOf[T]): Aux[RepOf[H] :~: T, H :~: t.Out] =
      new TagsOf[RepOf[H] :~: T] {
        type Out = H :~: t.Out
        def apply(s: RepOf[H] :~: T): Out = {

          val uh: H = fromRep(s.head)
          uh :~: t(s.tail)
        }
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
    tagOf: RepOf[AH] => AH,
    listLike: ListLike.Of[Out, E], 
    transform: Case1.Aux[F, (AH, RepOf[AH]), E], 
    recOnTail: FromProperties.Aux[AT, RT, F, Out]
  ): FromProperties.Aux[AH :~: AT, RepOf[AH] :~: RT, F, Out] =
    new FromProperties[AH :~: AT, Out] {
      type Reps = RepOf[AH] :~: RT
      type Fun = F
      def apply(r: RepOf[AH] :~: RT): Out = {
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
    RH <: RepOf[AH], RT <: TypeSet,
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
