/*
## Type sets

Here we define type-level sets. The main idea behind this construction is that it cannot contain type-duplicates and uses [TypeUnion](TypeUnion.md), which provides cheap checks whether a type is in the set, and allows to construct an effective union and similar operations.

- all operations are on type level, so `set(true) ~ set(false)`, because they both have type `Boolean :~: ∅`;
- after subtraction, union or other operations on two sets, the order of elements can change, so that operations (like union) are not commutative, but they yield the same type, so the results are considered equivalent (see `~` operator);
*/

package ohnosequences.typesets


/* ### The main type representing set */
trait TypeSet { set =>
  type Bound <: TypeUnion
  def toStr: String
  override def toString = "{" + toStr + "}"
}


/* ### Empty set */
sealed trait ∅ extends TypeSet {
  type Bound = either[Nothing]
  def toStr = ""
}

private object empty extends ∅


/* ### Cons constructor */
case class :~:[E, S <: TypeSet] private[:~:](head: E, tail: S) extends TypeSet {
  type Bound = tail.Bound#or[E]
  def toStr = {
    val h = head match {
      case _: String => "\""+head+"\""
      case _: Char   => "\'"+head+"\'"
      case _         => head.toString
    }
    val t = tail.toStr
    if (t.isEmpty) h else h+", "+t
  }
}

/* This `cons` method covers the `:~:` constructor to check that you are not adding a duplicate */
object :~: { 
  def cons[E : in[S]#isnot, S <: TypeSet](e: E, set: S) = :~:(e, set) 
}


/*
### Type operators and aliases 

See implicits for these operators in the [package object](package.md)
*/
@annotation.implicitNotFound(msg = "Can't prove that ${E} ∈ ${S}")
sealed class ∈[E : in[S]#is, S <: TypeSet]

@annotation.implicitNotFound(msg = "Can't prove that ${E} ∉ ${S}")
sealed class ∉[E : in[S]#isnot, S <: TypeSet]

@annotation.implicitNotFound(msg = "Can't prove that ${S} ⊃ ${Q}")
sealed class ⊃[S <: TypeSet : supersetOf[Q]#is, Q <: TypeSet]

@annotation.implicitNotFound(msg = "Can't prove that ${S} ⊂ ${Q}")
sealed class ⊂[S <: TypeSet : subsetOf[Q]#is, Q <: TypeSet]

@annotation.implicitNotFound(msg = "Can't prove that ${S} ~ ${Q} (i.e. that sets are type-equivalent)")
sealed class ~[S <: TypeSet : sameAs[S]#is, Q <: TypeSet]

/* ### Adding methods to TypeSet */
class TypeSetOps[S <: TypeSet](set: S) {
  def :~:[E](e: E)(implicit n: E ∉ S) = ohnosequences.typesets.:~:.cons(e, set)

  def lookup[E](implicit l: Lookup[S, E]): l.Out = l(set)

  def \[Q <: TypeSet](q: Q)(implicit sub: S \ Q) = sub(set, q)
  def U[Q <: TypeSet](q: Q)(implicit uni: S U Q) = uni(set, q)

  def mapFold[R, F](z: R)(f: F)(op: (R, R) => R)
    (implicit smf: SetMapFolder[S, R, F]): R = smf(set, z, op)
}
