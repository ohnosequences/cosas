package ohnosequences.cosas.ops.records

import ohnosequences.cosas._, types._, records._, properties._, fns._, ops.typeSets.Lookup

@annotation.implicitNotFound(msg = """
  Cannot get property
    ${P}
  from record of type
    ${RT}
""")
trait Get[RT <: AnyRecord, P <: AnyProperty]
  extends Fn1[RT#Raw] with Out[P#Raw]

case object Get {

  implicit def getter[R <: AnyRecord, P <: AnyProperty]
    (implicit
      lookup: R#Raw Lookup ValueOf[P]
    ):  Get[R, P] =
    new Get[R, P] { def apply(recEntry: R#Raw): Out = lookup(recEntry).value }
}
