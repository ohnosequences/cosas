package ohnosequences.cosas.types

import ohnosequences.cosas._, fns._, klists._

/* This is a parser for a particular _single_ denotation */
// TODO: update to DepFns
trait AnyDenotationParser {

  type Type <: AnyType
  val  tpe: Type

  /* This is normally `tpe.label`, but it's left free so that you can parse type label from a different representation */
  val labelRep: String

  /* The type used to denote Type */
  type D <: Type#Raw

  /* The type from which we try to parse `D` */
  type Value

  val parser: Value => Option[D]

  def apply(k: String, v: Value): Either[DenotationParserError, Type := D] = k match {
    case `labelRep` => parser(v)
      .fold[Either[DenotationParserError, Type := D]](
        Left(ErrorParsingValue(tpe)(v))
      )(
        d => Right(tpe := d)
      )
    case _ => Left(WrongKey(tpe, k, labelRep))
  }
}

sealed trait DenotationParserError
/* This type of error occurs when the `parser` function returns `None` */
case class ErrorParsingValue[Tpe <: AnyType, Value](val tpe: Tpe)(val from: Value) extends DenotationParserError
/* This error may occur when the parsed value pair has a wrong key label */
case class WrongKey[Tpe <: AnyType](val tpe: Tpe, val got: String, val expected: String) extends DenotationParserError


// TODO: I think this constructor is enough and AnyDenotationParser is not needed
class DenotationParser[T <: AnyType, D0 <: T#Raw, V](
  val tpe: T,
  val labelRep: String
)(val parser: V => Option[D0]
) extends AnyDenotationParser {

  type Type = T
  type D = D0
  type Value = V
}

case object AnyDenotationParser {

  // NOTE: this won't work for a parametrized type T, because there is no implicit `tpe`
  implicit def genericParser[T <: AnyType { type Raw >: D }, D](implicit tpe: T): DenotationParser[T,D,D] =
    new DenotationParser(tpe, tpe.label)(d => Some(d))
}





/* This is a DepFn which parses a _KList of denotations_ from a Map of pairs (type label -> value: V) */
class ParseDenotations[V, Ts <: AnyProductType] extends DepFn1[
  Map[String, V],
  Either[ParseDenotationsError, Ts#Raw]
]


trait ParseDenotationsError
case class KeyNotFound[V](val key: String, val map: Map[String,V]) extends ParseDenotationsError
case class ErrorParsing[PE <: DenotationParserError](val err: PE) extends ParseDenotationsError


case object ParseDenotations {

  implicit def emptyParam[V, T <: AnyType]
  : AnyApp1At[ParseDenotations[V, |[T]], Map[String,V]] { type Y =  Either[ParseDenotationsError,*[AnyDenotation]] } =
    App1 { map: Map[String,V] => Right(*[AnyDenotation]) }

  // TODO: improve parameters names
  implicit def nonEmpty[
    V,
    H <: Ts#Types#Bound { type Raw >: HR }, HR, Ts <: AnyProductType { type Raw >: Ds },
    Ds <: AnyKList.withBound[AnyDenotation]
  ](implicit
    parseH: DenotationParser[H,HR,V],
    parseRest: AnyApp1At[ParseDenotations[V,Ts], Map[String,V]] { type Y  = Either[ParseDenotationsError,Ds] }
  )
  : AnyApp1At[ParseDenotations[V, H :×: Ts], Map[String,V]] { type Y = Either[ParseDenotationsError, (H := HR) :: Ds] } =
    App1 { map: Map[String,V] => {

      map.get(parseH.labelRep).fold[Either[ParseDenotationsError, (H := HR) :: Ds]](
        Left(KeyNotFound(parseH.labelRep, map))
      )(
        v => parseH(parseH.labelRep, v) fold (

          l => Left(ErrorParsing(l)),

          r => parseRest(map).fold[Either[ParseDenotationsError, (H := HR) :: Ds]] (
            err => Left(err),
            td  => Right(r :: (td: Ds))
          )
        )
      )
    }
  }
}
