package ohnosequences.cosas.csv

import ohnosequences.cosas._

import shapeless._

/*
## csv

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



