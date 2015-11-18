package ohnosequences.cosas.tests

import ohnosequences.cosas._

final class NatTests extends org.scalatest.FunSuite {

  test("can sum nat vals and types") {

    assert { sum(_1, _0) === sum(_0, _1) }
    assert { sum(_2, _1) === sum(_1, _2) }

    assert { _3 === sum(_2, _1) }
  }
}
