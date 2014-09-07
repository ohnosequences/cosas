package bug.scala

trait AnyFoo {
  
  type Bound
  type Bar <: Bound
}

trait Foo[X <: AnyFoo, B <: X#Bound] extends AnyFoo {
  
  type Bound = X#Bound
  type Bar = B
}

trait WrapFoo[L <: AnyFoo] extends Foo[L,L#Bar]