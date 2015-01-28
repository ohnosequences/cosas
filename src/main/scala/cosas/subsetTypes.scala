package ohnosequences.cosas

object subsetTypes {

  import types._  

  /*
  ### Subset types

  **Warning** _this has nothing to do with the typeSets in this library!_

  The idea of subset types is that you are specifying a type `S` having as values a _subset_ of those of another `W` type; in this case this is modeled as a predicate valued on `W#Raw`.

  For more about this (and its possible uses) see

  - [Adam Chlipala CPDT - Subset types](http://adam.chlipala.net/cpdt/html/Subset.html)
  */
  trait AnySubsetType extends AnyType {

    type W <: AnyType
    type Raw = W#Raw

    def predicate(raw: W := Raw): Boolean
  }

  trait SubsetType[W0 <: AnyType] extends AnySubsetType { type W = W0 }

  object AnySubsetType {

    implicit def sstops[W <: AnyType, ST <: SubsetType[W]](st: ST): SubSetTypeOps[W,ST] = new SubSetTypeOps(st)
  }

  final class SubSetTypeOps[W <: AnyType, ST <: SubsetType[W]](val st: ST) extends AnyVal {

    final def apply(raw: W := W#Raw): Option[ValueOf[ST]] = {

      if ( st predicate raw ) None else Some( new ValueOf[ST](raw.value) )
    }
    
    final def withValue(raw: W := W#Raw): Option[ValueOf[ST]] = apply(raw)
  }

  /* you should implement this trait for providing ops for values of a subset type `ST`. */
  trait ValueOfSubsetTypeOps[W <: AnyType, ST <: SubsetType[W]] extends Any {

    /* use case: concat of sized has the sum of the two arg sizes; but how do you create the corresponding value saving a stupid check (and returning an Option)? `unsafeValueOf`. By implementing this trait you assume the responsibility that comes with being able to create unchecked values of `ST`; use it with caution! */
    protected final def unsafeValueOf[ST0 <: ST](other: ST#Raw): ValueOf[ST] = new ValueOf[ST](other)
  }

  object ValueOfSubsetTypeOps {

    implicit def valueOfSubsetTypeOps[
      W <: AnyType,
      ST <: SubsetType[W],
      Ops <: ValueOfSubsetTypeOps[W,ST]
    ](v: ValueOf[ST])(implicit conv: ValueOf[ST] => Ops): Ops = conv(v)
  }
}