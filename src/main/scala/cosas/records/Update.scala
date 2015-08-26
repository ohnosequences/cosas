package ohnosequences.cosas.records

import ohnosequences.cosas._, types._, typeSets._, records._, fns._
import ops.typeSets.Replace

@annotation.implicitNotFound(msg = """
  Cannot update property values
    ${Ps}
  from record of type
    ${RT}
""")
trait Update[RT <: AnyRecord, Ps <: AnyTypeSet]
  extends Fn2[RT#Raw, Ps] with Out[ValueOf[RT]]

case object Update {

  implicit def update[RT <: AnyRecord, Ps <: AnyTypeSet]
    (implicit
      check: Ps ⊂ RT#Raw,
      replace: Replace[RT#Raw, Ps]
    ):  Update[RT, Ps] =
    new Update[RT, Ps] {

      def apply(recRaw: RT#Raw, propReps: Ps): Out = new ValueOf[RT](replace(recRaw, propReps))
    }
}
