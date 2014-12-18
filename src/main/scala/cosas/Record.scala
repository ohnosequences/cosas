package ohnosequences.cosas

import AnyTypeSet._, AnyProperty._
import denotation._, propertiesHolder._
import ops.typeSet._


trait AnyRecord extends AnyType with AnyPropertiesHolder {

  val label: String

  /* Record wraps a set of values of it's properties */
  type Raw <: AnyTypeSet
  // should be provided implicitly:
  implicit val valuesOfProperties: Raw areValuesOf Properties
}

class Record[Props <: AnyTypeSet.Of[AnyProperty], Vals <: AnyTypeSet]
  (val properties: Props)
  (implicit 
    val valuesOfProperties: Vals areValuesOf Props
  ) extends AnyRecord {

  val label = this.toString

  type Properties = Props
  type Raw = Vals
}

object AnyRecord {

  /* Refiners */
  type withProperties[Ps <: AnyTypeSet.Of[AnyProperty]] = AnyRecord { type Properties = Ps }
  type withRaw[R <: AnyTypeSet] = AnyRecord { type Raw = R }

  type size[R <: AnyRecord] = AnyTypeSet.size[R#Raw]

  /* Accessors */
  type PropertiesOf[R <: AnyRecord] = R#Properties

  implicit def recordOps[R <: AnyRecord](rec: R): 
        RecordOps[R] = 
    new RecordOps[R](rec)

  // NOTE: you'll only get record ops _if_ you have a ValueOf[R]. From that point, you don't need the wrapper at all, just use `recEntry.value`. This lets you make RecordRawOps a value class itself!
  // see https://stackoverflow.com/questions/14861862/how-do-you-enrich-value-classes-without-overhead/
  implicit def recordRepOps[R <: AnyRecord](recEntry: ValueOf[R]): 
        RecordRawOps[R] = 
    new RecordRawOps[R](recEntry.value)

}

// class RecordOps[R <: AnyRecord](val rec: R) extends WrapOps[R](rec) {
class RecordOps[R <: AnyRecord](val rec: R) extends AnyVal {

  def apply(v: R#Raw): ValueOf[R] = new ValueOf[R](v)
  /* Same as just tagging with `=>>`, but you can pass fields in any order */
  def fields[Vs <: AnyTypeSet](values: Vs)(implicit
      reorder: Vs ReorderTo RawOf[R]
    ): ValueOf[R] = rec := reorder(values)

  def parseFrom[X](x: X)(implicit 
    parseSet: (R#Properties ParseFrom X) { type Out = R#Raw }
  ): ValueOf[R] = rec := parseSet(rec.properties, x)

}

class RecordRawOps[R <: AnyRecord](val recRaw: RawOf[R]) extends AnyVal {
  
  import ops.record._

  def get[P <: AnyProperty](p: P)
    (implicit get: R Get P): ValueOf[P] = get(recRaw)
    // (implicit lookup: RawOf[R] Lookup ValueOf[P]): ValueOf[P] = lookup(recRaw.raw)


  def update[P <: AnyProperty](propRep: ValueOf[P])
    (implicit check: (ValueOf[P] :~: ∅) ⊂ RawOf[R], 
              upd: R Update (ValueOf[P] :~: ∅)
    ): ValueOf[R] = upd(recRaw, propRep :~: ∅)

  def update[Ps <: AnyTypeSet](propReps: Ps)
    (implicit upd: R Update Ps): ValueOf[R] = upd(recRaw, propReps)


  def as[Other <: AnyRecord](other: Other)
    (implicit project: Take[RawOf[R], RawOf[Other]]): ValueOf[Other] = other := project(recRaw)

  def as[Other <: AnyRecord, Rest <: AnyTypeSet](other: Other, rest: Rest)
    (implicit transform: Transform[R, Other, Rest]): ValueOf[Other] = transform(recRaw, other, rest)


  def serializeTo[X](implicit serializer: R#Raw SerializeTo X): X = serializer(recRaw)
}
