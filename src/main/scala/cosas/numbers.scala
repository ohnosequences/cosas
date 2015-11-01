package ohnosequences.cosas

import ohnosequences.cosas.fns._

trait AnyDigit

sealed trait AnySucc extends AnyDigit { type Pred <: AnyDigit }
sealed trait AnyPred extends AnyDigit { type Succ <: AnyDigit }

sealed trait Succ[N <: AnyDigit] extends AnySucc { type Pred = N }
sealed trait Pred[N <: AnyDigit] extends AnyPred { type Succ = N }

sealed trait _0 extends               Pred[_1]
sealed trait _1 extends Succ[_0] with Pred[_2]
sealed trait _2 extends Succ[_1] with Pred[_3]
sealed trait _3 extends Succ[_2] with Pred[_4]
sealed trait _4 extends Succ[_3] with Pred[_5]
sealed trait _5 extends Succ[_4] with Pred[_6]
sealed trait _6 extends Succ[_5] with Pred[_7]
sealed trait _7 extends Succ[_6] with Pred[_8]
sealed trait _8 extends Succ[_7] with Pred[_9]
sealed trait _9 extends Succ[_8]


@annotation.implicitNotFound(msg = "${A} is not less than ${B}")
sealed trait LT[A <: AnyDigit, B <: AnyDigit]

case object LT extends LT2 {

  implicit def next[A <: AnyPred, B <: Succ[A]]:
  // implicit def next[A <: B#Pred, B <: AnySucc]:
      (A LT B) =
  new (A LT B) {}
}

sealed trait LT2 {

  implicit def rec[A <: AnyPred, B <: AnySucc](
    implicit ax: A LT B#Pred
  ):  (A LT B) =
  new (A LT B) {}
}


@annotation.implicitNotFound(msg = "${A} is not equal to ${B}")
sealed trait EQ[A <: AnyDigit, B <: AnyDigit]

case object EQ extends EQ2 {

  implicit def pred[
    A <: AnyPred,
    B <: AnyPred { type Succ = A#Succ }
  ]:  (A EQ B) =
  new (A EQ B) {}
}

sealed trait EQ2 {

  implicit def succ[
    A <: AnySucc,
    B <: AnySucc { type Pred = A#Pred }
  ]:  (A EQ B) =
  new (A EQ B) {}
}


@annotation.implicitNotFound(msg = "${A} is not greater than ${B}")
sealed trait GT[A <: AnyDigit, B <: AnyDigit]

case object GT {

  implicit def pred[A <: AnyDigit, B <: AnyDigit](
    implicit lt: B LT A
  ):  (A GT B) =
  new (A GT B) {}
}

case object test {

  implicitly[_0 LT _1]
  implicitly[_0 LT _9]
  implicitly[_1 LT _2]
  implicitly[_1 LT _9]

  // FIXME: instead of "not found" these it causes "diverging implicits"
  // implicitly[_0 LT _0]
  // implicitly[_3 LT _2]


  implicitly[_0 EQ _0]
  implicitly[_1 EQ _1]
  implicitly[_2 EQ _2]
  implicitly[_3 EQ _3]
  implicitly[_4 EQ _4]
  implicitly[_5 EQ _5]
  implicitly[_6 EQ _6]
  implicitly[_7 EQ _7]
  implicitly[_8 EQ _8]
  implicitly[_9 EQ _9]

  // implicitly[_0 EQ _1]

  implicitly[_2 GT _0]
  implicitly[_2 GT _1]
}
