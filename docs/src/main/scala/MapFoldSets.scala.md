
## Map-folder for sets 
  
Just a copy of MapFolder for `HList`s from shapeless.


```scala
package ohnosequences.typesets

trait SetMapFolder[S <: TypeSet, R, F] {
  def apply(s: S, in: R, op: (R, R) => R): R 
}
  
object SetMapFolder {
  import shapeless._
  import poly._
  
  implicit def empty[R, F] = new SetMapFolder[?, R, F] {
    def apply(s: ?, in: R, op: (R, R) => R) = in
  }
  
  implicit def cons[H, T <: TypeSet, R, F <: Poly]
    (implicit hc: Case.Aux[F, H :: HNil, R], tf: SetMapFolder[T, R, F]) =
      new SetMapFolder[H :~: T, R, F] {
          def apply(s: H :~: T, in: R, op: (R, R) => R) = op(hc(s.head), tf(s.tail, in, op))
      }
}

```


------

### Index

+ src
  + main
    + scala
      + [HListOps.scala][main/scala/HListOps.scala]
      + [LookupInSet.scala][main/scala/LookupInSet.scala]
      + [MapFoldSets.scala][main/scala/MapFoldSets.scala]
      + [package.scala][main/scala/package.scala]
      + [SetMapper.scala][main/scala/SetMapper.scala]
      + [SubtractSets.scala][main/scala/SubtractSets.scala]
      + [TypeSet.scala][main/scala/TypeSet.scala]
      + [TypeUnion.scala][main/scala/TypeUnion.scala]
      + [UnionSets.scala][main/scala/UnionSets.scala]
  + test
    + scala
      + [TypeSetTests.scala][test/scala/TypeSetTests.scala]

[main/scala/HListOps.scala]: HListOps.scala.md
[main/scala/LookupInSet.scala]: LookupInSet.scala.md
[main/scala/MapFoldSets.scala]: MapFoldSets.scala.md
[main/scala/package.scala]: package.scala.md
[main/scala/SetMapper.scala]: SetMapper.scala.md
[main/scala/SubtractSets.scala]: SubtractSets.scala.md
[main/scala/TypeSet.scala]: TypeSet.scala.md
[main/scala/TypeUnion.scala]: TypeUnion.scala.md
[main/scala/UnionSets.scala]: UnionSets.scala.md
[test/scala/TypeSetTests.scala]: ../../test/scala/TypeSetTests.scala.md