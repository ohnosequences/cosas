package ohnosequences.cosas.types

import ohnosequences.cosas._, fns._, klists._

// TODO update to DepFns
trait AnyDenotationParser {

  type Type <: AnyType
  val tpe: Type

  // the type used to denote Type
  type D <: Type#Raw

  type Value
  type From = (String, Value)

  val parser: Value => Option[D]

  val labelRep: String

  def apply(k: String, v: Value): Either[DenotationParserError, Type := D] = k match {

    case `labelRep` => parser(v).fold[Either[DenotationParserError, Type := D]](
        Left(ErrorParsingValue(tpe)(v))
      )(
        d => Right(tpe := d)
      )

    case _ => Left(WrongKey(tpe, k, labelRep))
  }
}

sealed trait DenotationParserError
case class ErrorParsingValue[Tpe <: AnyType, Value](val tpe: Tpe)(val from: Value)
extends DenotationParserError
case class WrongKey[Tpe <: AnyType](val tpe: Tpe, val got: String, val expected: String)
extends DenotationParserError

class DenotationParser[T <: AnyType, D0 <: T#Raw, V](
  val tpe: T,
  val labelRep: String
)(
  val parser: V => Option[D0]
)
extends AnyDenotationParser {

  type Type = T
  type D = D0
  type Value = V
}

case object AnyDenotationParser {

  implicit def genericParser[T <: AnyType { type Raw >: D }, D](implicit tpe: T): DenotationParser[T,D,D] =
    new DenotationParser(tpe, tpe.label)(d => Some(d))
}

trait ParseDenotationsError
case class KeyNotFound[V](val key: String, val map: Map[String,V]) extends ParseDenotationsError
case class ErrorParsing[PE <: DenotationParserError](val err: PE) extends ParseDenotationsError

class ParseDenotations[V, Ts <: AnyProductType] extends DepFn1[Map[String,V], Either[ParseDenotationsError,Ts#Raw]]

case object ParseDenotations {

  implicit def empty[V,X]
  : AnyApp1At[ParseDenotations[V,unit], Map[String,V]] { type Y =  Either[ParseDenotationsError,*[AnyDenotation]] } =
    App1 { map: Map[String,V] => Right(*[AnyDenotation]) }

  implicit def emptyParam[V, T <: AnyType, X]
  : AnyApp1At[ParseDenotations[V,In[T]], Map[String,V]] { type Y =  Either[ParseDenotationsError,*[AnyDenotation]] } =
    App1 { map: Map[String,V] => Right(*[AnyDenotation]) }

  implicit def nonEmpty[
    V,
    H <: Ts#Types#Bound { type Raw >: HR }, HR, Ts <: AnyProductType { type Raw >: Ds }, Ds <: AnyKList.withBound[AnyDenotation]
  ](implicit
    parseRest: AnyApp1At[ParseDenotations[V,Ts], Map[String,V]] { type Y  = Either[ParseDenotationsError,Ds] },
    parseH: DenotationParser[H,HR,V]
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
