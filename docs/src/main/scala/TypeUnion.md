### Index

+ src
  + main
    + scala
      + [LookupInSet.scala](LookupInSet.md)
      + [MapFoldSets.scala](MapFoldSets.md)
      + [package.scala](package.md)
      + [SubtractSets.scala](SubtractSets.md)
      + [TypeSet.scala](TypeSet.md)
      + [TypeUnion.scala](TypeUnion.md)
      + [UnionSets.scala](UnionSets.md)
  + test
    + scala
      + [TypeSetTests.scala](../../test/scala/TypeSetTests.md)

------

## Type union

After http://www.chuusai.com/2011/06/09/scala-union-types-curry-howard/#comment-179
Credits: Lars Hupel


```scala
package ohnosequences.typesets

trait TypeUnion {
  type or[S] <: TypeUnion
  type get
}

// need to add NotIn based on sum type bounds
trait OneOf[T] extends TypeUnion {
  type or[S] = OneOf[T with not[S]]  
  type get = not[T]
}

```

