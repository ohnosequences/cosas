package ohnosequences.cosas.csv

import ohnosequences.cosas._

import shapeless._

/*
## csv

If one looks at `Sized` what we have is

- a predicate on `GenTraversableLike`: check wether the length of it equals a value. This is indexed by a type (`Nat` in this case) so that we have `check(l: GenTraversableLike): Boolean`; this check is as I said indexed by `Nat` but this is not so important in general.
- a value class wrapping something `GenTraversableLike` whatever; you can only get instances of it _if_ that check works; so in the end is something like `Option[ValueOf[T]]` where `T` is a _static_ type (either an object of a sealed class indexed by `Nat` in this case) which contains the `check` we mentioned above. Of course in practice one needs an ops-private variant which does not require the check; see next point
- You use then values of this type happily, and can write ops that return `ValueOf` for different static types (like concat that would return its sum etc)

#### parsing, serializing

- A record can be mapped to a CSV row with header derived from the record; just map over the properties and call its FQN (for example)
- Given a record you can try to parse it from a Row. For this you need a function from this record and the value to (an option of) Value[Record]. Something like `Row => Value[Record]` should work.

I think that a generic Poly acting over a Map and requiring a conversion from P to Option[P#Raw] should be enough.
*/

// object parsing {

//   type Parse[P]     =         P   => Option[ValueOf[P]]
//   type Serialize[P] = ValueOf[P]  => P#Raw
// }

// trait AnyHeader {

//   type Props <: AnyTypeSet.Of[AnyProperty]
// }

// trait AnyRow extends AnyWrap {

//   type Header <: AnyHeader

//   type Raw = Map[String,String]
// }

// class Row[H <: AnyHeader](val header: H) extends AnyRow {

//   type Header = H
// }



