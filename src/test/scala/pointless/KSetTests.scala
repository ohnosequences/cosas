// package ohnosequences.pointless.test
//
// import ohnosequences.pointless._, AnyKSet._
//
// class KSetTests extends org.scalatest.FunSuite {
//
//   object TestsContext {
//
//     trait superfoo
//     object stupidfoo extends superfoo
//
//     trait foo extends superfoo { val boo: Int }
//     object bar extends foo { val boo = 1 }
//     object qux extends foo { val boo = 2 }
//     object buh extends foo { val boo = 3 }
//
//   }
//
//   test("kset") {
//     import TestsContext._
//
//     val kset = kcons(buh, kcons(qux, kone(bar)))
//
//     implicitly[kset.Bound <:< superfoo]
//     implicitly[kset.Bound =:= foo]
//     implicitly[kset.tail.Bound <:< foo] // but not =:=
//
//     type NEKSetOf[B] = NEKSet { type Bound <: B }
//     def fun1[S <: NEKSetOf[foo] ](s: S): Int = s.head.boo
//     def fun2[S <: NEKSetOf[foo] { type Tail <: NEKSetOf[foo] }](s: S): Int = s.tail.head.boo
//
//     // val uh: KList[A] = adsfadfasdf :: Nil[A]
//     // trait A { val uh: KList[B]; val z: B; val other: KList[B]; val s = z :: other: KList[B] }
//
//     val foobuh: foo = buh
//     val foorest = kcons(qux, kone(bar))
//
//     implicitly[foorest.type <:< AnyKSet.Of[foo]]
//
//     val todos: AnyKSet.Of[foo] = kcons(foobuh, foorest)
//
//     assert{ fun1(kset) == buh.boo }
//     assert{ fun2(kset) == qux.boo }
//
//   }
// }
