package ohnosequences.cosas.types

case object syntax {


  final case class TypeSyntax[T <: AnyType](val tpe: T) extends AnyVal {

    /* For example `user denoteWith (String, String, Int)` _not that this is a good idea_ */
    final def =:[@specialized V <: T#Raw](v: V): V =: T = new (V Denotes T)(v)
    final def :=[@specialized V <: T#Raw](v: V): T := V = new (V Denotes T)(v)
  }

  case class SubsetTypeSyntax[W <: AnyType, ST <: SubsetType[W]](val st: ST) extends AnyVal {

    final def apply(raw: W := W#Raw): Option[ValueOf[ST]] = {

      if ( st predicate raw ) None else Some( new Denotes[ST#Raw,ST](raw.value) )
    }

    final def withValue(raw: W := W#Raw): Option[ValueOf[ST]] = apply(raw)
  }
}
