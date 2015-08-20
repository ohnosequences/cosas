package ohnosequences.cosas.ops.records

import ohnosequences.cosas._, types._, fns._, records._, typeSets._
import ops.typeSets.Take

@annotation.implicitNotFound(msg =
  """|Cannot transform record of type
     |  ${RT}
     |to
     |  ${Other}
     |with values
     |  ${Rest}
     |Probably you haven't provided values for all missing properties.
     |""".stripMargin)
trait Transform[RT <: AnyRecord, Other <: AnyRecord, Rest]
  extends Fn3[RT#Raw, Other, Rest] with Out[ValueOf[Other]]

case object Transform {

  implicit def transform[
      RT <: AnyRecord,
      Other <: AnyRecord,
      Rest <: AnyTypeSet,
      Uni <: AnyTypeSet,
      Missing <: AnyTypeSet
    ](implicit
      missing: (Other#Raw \ RT#Raw) { type Out = Missing },
      allMissing: Rest ~:~ Missing,
      uni: (RT#Raw ∪ Rest) { type Out = Uni },
      project: Take[Uni, Other#Raw]
    ):  Transform[RT, Other, Rest] =
    new Transform[RT, Other, Rest] {

      def apply(recRaw: RT#Raw, other: Other, rest: Rest): Out =
        other := project(uni(recRaw, rest))
    }

}
