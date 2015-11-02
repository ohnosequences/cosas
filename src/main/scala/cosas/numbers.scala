package ohnosequences.cosas

// import ohnosequences.cosas.fns._

trait AnyDigit { val value: Int }

sealed trait AnySucc extends AnyDigit { type Pred <: AnyDigit }
sealed trait AnyPred extends AnyDigit { type Succ <: AnyDigit }

sealed trait Succ[N <: AnyDigit] extends AnySucc { type Pred = N }
sealed trait Pred[N <: AnyDigit] extends AnyPred { type Succ = N }

sealed trait _0 extends               Pred[_1] { val value = 0 }
sealed trait _1 extends Succ[_0] with Pred[_2] { val value = 1 }
sealed trait _2 extends Succ[_1] with Pred[_3] { val value = 2 }
sealed trait _3 extends Succ[_2] with Pred[_4] { val value = 3 }
sealed trait _4 extends Succ[_3] with Pred[_5] { val value = 4 }
sealed trait _5 extends Succ[_4] with Pred[_6] { val value = 5 }
sealed trait _6 extends Succ[_5] with Pred[_7] { val value = 6 }
sealed trait _7 extends Succ[_6] with Pred[_8] { val value = 7 }
sealed trait _8 extends Succ[_7] with Pred[_9] { val value = 8 }
sealed trait _9 extends Succ[_8]               { val value = 9 }


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

case object test_LT {

  implicitly[_0 LT _1]
  implicitly[_0 LT _9]
  implicitly[_1 LT _2]
  implicitly[_1 LT _9]

  // FIXME: instead of "not found" these it causes "diverging implicits"
  // implicitly[_0 LT _0]
  // implicitly[_3 LT _2]
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

case object test_EQ {
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
}


@annotation.implicitNotFound(msg = "${A} is not greater than ${B}")
sealed trait GT[A <: AnyDigit, B <: AnyDigit]

case object GT {

  implicit def pred[A <: AnyDigit, B <: AnyDigit](
    implicit lt: B LT A
  ):  (A GT B) =
  new (A GT B) {}
}

case object test_GT {

  implicitly[_2 GT _0]
  implicitly[_2 GT _1]
}


@annotation.implicitNotFound(msg = "Cannot add ${A} and ${B}")
sealed trait add[A <: AnyDigit, B <: AnyDigit] {
  type Out <: AnyPair.of[AnyDigit, AnyDigit]
}

case object add extends add_2 {

  implicit def zero[D <: AnyDigit]:
      add[D, _0] { type Out = Pair[_0, D] } =
  new add[D, _0] { type Out = Pair[_0, D] }
}

sealed trait add_2 extends add_3 {

  implicit def nine[D <: AnySucc]:
      add[_9, D] { type Out = Pair[_1, D#Pred] } =
  new add[_9, D] { type Out = Pair[_1, D#Pred] }
}

sealed trait add_3 {

  implicit def rec[A <: AnyPred, B <: AnySucc, C <: AnyPair.of[AnyDigit, AnyDigit]](
    implicit rest: add[A#Succ, B#Pred] { type Out = C }
  ):  add[A, B] { type Out = C } =
  new add[A, B] { type Out = C }
}

case object test_add {

  implicitly[add[_0, _0] { type Out = Pair[_0, _0] }]
  implicitly[add[_2, _3] { type Out = Pair[_0, _5] }]
  implicitly[add[_8, _3] { type Out = Pair[_1, _1] }]
  implicitly[add[_9, _1] { type Out = Pair[_1, _0] }]
  implicitly[add[_9, _9] { type Out = Pair[_1, _8] }]
}

trait AnyNumber

trait Num extends AnyNumber

trait |[N <: AnyNumber, D <: AnyDigit] extends AnyNumber {
  type Head = D
  type Tail = N
}

case object test_number {

  type _103 = Num|_1|_0|_3
  type _93 = Num|_9|_3
}
