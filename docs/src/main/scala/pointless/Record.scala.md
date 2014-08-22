
```scala
package ohnosequences.pointless

import ohnosequences.pointless._, typeSet._, taggedType._, property._

object record {

  trait AnyRecord extends AnyTaggedType {

    type Properties <: AnyTypeSet
    val  properties: Properties
    // should be provided implicitly:
    val  propertiesBound: Properties isBoundedBy AnyProperty

    type Raw <: AnyTypeSet
    // should be provided implicitly:
    val representedProperties: Properties isRepresentedBy Raw
  }

  // extractors
  type withProperties[Ps <: AnyTypeSet] = AnyRecord { type Properties = Ps }

  // accessors
  type PropertiesOf[R <: AnyRecord] = R#Properties

  class Record[Props <: AnyTypeSet, Vals <: AnyTypeSet](val properties: Props)(implicit 
    val propertiesBound: Props isBoundedBy AnyProperty,
    val representedProperties: Props isRepresentedBy Vals
  ) extends AnyRecord {

    val label = this.toString

    type Properties = Props
    type Raw = Vals
  }

  object AnyRecord {

    type withProperties[Ps <: AnyTypeSet] = AnyRecord { type Properties = Ps }
    type PropertiesOf[R <: AnyRecord] = R#Properties
  }
```


Ops 


```scala
  import ops.record._

  implicit def recordOps[R <: AnyRecord](rec: R): RecordOps[R] = new RecordOps(rec)
  class RecordOps[R <: AnyRecord](val rec: R) extends TaggedTypeOps(rec) {
```

Same as just tagging with `=>>`, but you can pass fields in any order

```scala
    def fields[Vs <: AnyTypeSet](values: Vs)
      (implicit 
        p: Vs As RawOf[R]
      ): Tagged[R] = rec =>> p(values)
  }

  implicit def recordRepOps[R <: AnyRecord](recEntry: Tagged[R]): RepOps[R] = new RepOps(recEntry)
  class RepOps[R <: AnyRecord](val recEntry: Tagged[R]) {

    def get[P <: AnyProperty](p: P)
      (implicit get: R Get P): Tagged[P] = get(recEntry)


    def update[P <: AnyProperty](propRep: Tagged[P])
      (implicit upd: R Update (Tagged[P] :~: ∅)): Tagged[R] = upd(recEntry, propRep :~: ∅)

    def update[Ps <: AnyTypeSet](propReps: Ps)
      (implicit upd: R Update Ps): Tagged[R] = upd(recEntry, propReps)


    def as[Other <: AnyRecord](other: Other)
      (implicit project: Take[RawOf[R], RawOf[Other]]): Tagged[Other] = other =>> project(recEntry)

    def as[Other <: AnyRecord, Rest <: AnyTypeSet](other: Other, rest: Rest)
      (implicit transform: Transform[R, Other, Rest]): Tagged[Other] = transform(recEntry, other, rest)

  }

}

// trait ListLike[L] {
//   type E // elements type

//   val nil: L
//   def cons(h: E, t: L): L

//   def head(l: L): E
//   def tail(l: L): L
// }

// object ListLike {
//   type Of[L, T] = ListLike[L] { type E = T }
// }

// /* Transforms a representation of item to something else */
// trait FromProperties[
//     A <: TypeSet, // set of properties
//     Out           // what we want to get
//   ] {

//   type Reps <: TypeSet            // representation of properties
//   type Fun <: Singleton with Poly // transformation function

//   def apply(r: Reps): Out
// }

// object FromProperties {
  
//   def apply[A <: TypeSet, Reps <: TypeSet, F <: Singleton with Poly, Out](implicit tr: FromProperties.Aux[A, Reps, F, Out]):
//     FromProperties.Aux[A, Reps, F, Out] = tr

//   type Aux[A <: TypeSet, R <: TypeSet, F <: Singleton with Poly, Out] =
//     FromProperties[A, Out] { 
//       type Reps = R
//       type Fun = F
//     }

//   type Anyhow[A <: TypeSet, R <: TypeSet, Out] =
//     FromProperties[A, Out] { 
//       type Reps = R
//     }

//   implicit def empty[Out, F <: Singleton with Poly]
//     (implicit m: ListLike[Out]): FromProperties.Aux[∅, ∅, F, Out] = new FromProperties[∅, Out] {
//       type Reps = ∅
//       type Fun = F
//       def apply(r: ∅): Out = m.nil
//     }

//   implicit def cons[
//     F <: Singleton with Poly,
//     AH <: Singleton with AnyProperty, AT <: TypeSet,
//     RT <: TypeSet,
//     E, Out
//   ](implicit
//     tagOf: Tagged[AH] => AH,
//     listLike: ListLike.Of[Out, E], 
//     transform: Case1.Aux[F, (AH, Tagged[AH]), E], 
//     recOnTail: FromProperties.Aux[AT, RT, F, Out]
//   ): FromProperties.Aux[AH :~: AT, Tagged[AH] :~: RT, F, Out] =
//     new FromProperties[AH :~: AT, Out] {
//       type Reps = Tagged[AH] :~: RT
//       type Fun = F
//       def apply(r: Tagged[AH] :~: RT): Out = {
//         listLike.cons(
//           transform((tagOf(r.head), r.head)),
//           recOnTail(r.tail)
//         )
//       }
//     }
// }

// ///////////////////////////////////////////////////////////////

// /* Transforms properties set representation from something else */
// trait ToProperties[
//     In,          // some other representation
//     A <: TypeSet // set of corresponding properties
//   ] {

//   type Out <: TypeSet             // representation of properties
//   type Fun <: Singleton with Poly // transformation function

//   def apply(in: In, a: A): Out
// }

// object ToProperties {
//   type Aux[In, A <: TypeSet, O <: TypeSet, F <: Singleton with Poly] = ToProperties[In, A] { type Out = O; type Fun = F } 

//   def apply[In, A <: TypeSet, O <: TypeSet, F <: Singleton with Poly]
//     (implicit form: ToProperties.Aux[In, A, O, F]): ToProperties.Aux[In, A, O, F] = form

//   implicit def empty[In, F <: Singleton with Poly]: ToProperties.Aux[In, ∅, ∅, F] = new ToProperties[In, ∅] {
//       type Out = ∅
//       type Fun = F
//       def apply(in: In, a: ∅): Out = ∅
//     }

//   implicit def cons[
//     In,
//     AH <: Singleton with AnyProperty, AT <: TypeSet,
//     RH <: Tagged[AH], RT <: TypeSet,
//     F <: Singleton with Poly
//   ](implicit
//     f: Case1.Aux[F, (In, AH), RH], 
//     t: ToProperties.Aux[In, AT, RT, F]
//   ): ToProperties.Aux[In, AH :~: AT, RH :~: RT, F] =
//     new  ToProperties[In, AH :~: AT] {
      
//       type Out = RH :~: RT
//       type Fun = F
//       def apply(in: In, a: AH :~: AT): Out = f((in, a.head)) :~: t(in, a.tail)
//     }
// }

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