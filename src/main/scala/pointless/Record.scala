package ohnosequences.pointless

import AnyTypeSet._, AnyProperty._, AnyType._, AnyTypeUnion._, AnyFn._
import ops.typeSet._


trait AnyRecord extends AnyType {

  val label: String

  type Properties <: AnyTypeSet.Of[AnyProperty]
  val  properties: Properties

  /* Any record *has* its own properties */
  implicit val myOwnProperties: Me Has Properties = (this: Me) has properties

  type Raw <: AnyTypeSet
  // should be provided implicitly:
  implicit val representedProperties: Properties isRepresentedBy Raw
}

class Record[Props <: AnyTypeSet.Of[AnyProperty], Vals <: AnyTypeSet](val properties: Props)(implicit 
  val representedProperties: Props isRepresentedBy Vals
) extends AnyRecord {

  val label = this.toString

  type Properties = Props
  type Raw = Vals
}

object AnyRecord {

  /* Refiners */
  type withProperties[Ps <: AnyTypeSet.Of[AnyProperty]] = AnyRecord { type Properties = Ps }
  type withRaw[R <: AnyTypeSet] = AnyRecord { type Raw = R }

  /* Accessors */
  type PropertiesOf[R <: AnyRecord] = R#Properties
  // type RawOf[R <: AnyRecord] = R#Raw

  implicit def recordOps[R <: AnyRecord](rec: R): 
        RecordOps[R] = 
    new RecordOps[R](rec)

  implicit def recordRepOps[R <: AnyRecord](recEntry: ValueOf[R]): 
        RecordRepOps[R] = 
    new RecordRepOps[R](recEntry)

}

class RecordOps[R <: AnyRecord](val rec: R) extends TypeOps[R](rec) {

  /* Same as just tagging with `=>>`, but you can pass fields in any order */
  def fields[Vs <: AnyTypeSet](values: Vs)(implicit
      reorder: Vs ReorderTo RawOf[R]
    ): ValueOf[R] = valueOf[R](reorder(values))

  def parseFrom[X](x: X)(implicit parseSet: (R#Properties ParseFrom X) with out[R#Raw]): ValueOf[R] = 
    valueOf[R](parseSet(rec.properties, x))

}

class RecordRepOps[R <: AnyRecord](val recEntry: ValueOf[R]) {
  import ops.record._

  def get[P <: AnyProperty](p: P)
    (implicit lookup: RawOf[R] Lookup ValueOf[P]): ValueOf[P] = lookup(recEntry.raw)
    // (implicit get: R Get P): ValueOf[P] = get(recEntry)


  def update[P <: AnyProperty](propRep: ValueOf[P])
    (implicit check: (ValueOf[P] :~: ∅) ⊂ RawOf[R], 
              upd: R Update (ValueOf[P] :~: ∅)
    ): ValueOf[R] = upd(recEntry, propRep :~: ∅)

  def update[Ps <: AnyTypeSet](propReps: Ps)
    (implicit upd: R Update Ps): ValueOf[R] = upd(recEntry, propReps)


  def as[Other <: AnyRecord](other: Other)
    (implicit project: Take[RawOf[R], RawOf[Other]]): ValueOf[Other] = valueOf[Other](project(recEntry.raw))

  def as[Other <: AnyRecord, Rest <: AnyTypeSet](other: Other, rest: Rest)
    (implicit transform: Transform[R, Other, Rest]): ValueOf[Other] = transform(recEntry, other, rest)


  def serializeTo[X](implicit serializer: R#Raw SerializeTo X): X = serializer(recEntry.raw)
}
