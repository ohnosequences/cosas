/*
## Type union

After http://www.chuusai.com/2011/06/09/scala-union-types-curry-howard/#comment-179
Credits: Lars Hupel
*/

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

/* These aliases mean that some type is (or isn't) a member of the union */
sealed class :<:[ X : oneOf[U]#is,   U <: TypeUnion]
sealed class :<!:[X : oneOf[U]#isnot, U <: TypeUnion]
