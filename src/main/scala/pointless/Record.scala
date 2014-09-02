package ohnosequences.pointless

import AnyTypeSet._, AnyProperty._, AnyTaggedType.Tagged, AnyTypeUnion._, AnyRecord._
import ops.typeSet._


trait AnyRecord extends AnyTaggedType { me =>

  val label: String

  type Properties <: AnyTypeSet.Of[AnyProperty]
  val  properties: Properties

  /* Any record *has* its own properties */
  implicit val myOwnProperties: Me Has Properties = (me: Me) has properties

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
  type RawOf[R <: AnyRecord] = R#Raw

  implicit def recordOps[R <: AnyRecord](rec: R): 
        RecordOps[R] = 
    new RecordOps[R](rec)

  implicit def recordRepOps[R <: AnyRecord](recEntry: Tagged[R]): 
        RecordRepOps[R] = 
    new RecordRepOps[R](recEntry)

}

class RecordOps[R <: AnyRecord](val rec: R) extends TaggedTypeOps[R](rec) {

  /* Same as just tagging with `=>>`, but you can pass fields in any order */
  def fields[Vs <: AnyTypeSet](values: Vs)(implicit
      reorder: Vs ReorderTo AnyRecord.RawOf[R]
    ): Tagged[R] = rec =>> reorder(values)

  import ops.record._

  def parseFrom[X](x: X)(implicit parser: R ParseFrom X): Tagged[R] = parser(rec, x)
}

class RecordRepOps[R <: AnyRecord](val recEntry: Tagged[R]) {
  import ops.record._

  def get[P <: AnyProperty](p: P)
    (implicit get: R Get P): Tagged[P] = get(recEntry)


  def update[P <: AnyProperty](propRep: Tagged[P])
    (implicit check: (Tagged[P] :~: ∅) ⊂ Tagged[R], upd: R Update (Tagged[P] :~: ∅)): Tagged[R] = upd(recEntry, propRep :~: ∅)

  def update[Ps <: AnyTypeSet](propReps: Ps)
    (implicit upd: R Update Ps): Tagged[R] = upd(recEntry, propReps)


  def as[Other <: AnyRecord](other: Other)
    (implicit project: Take[RawOf[R], RawOf[Other]]): Tagged[Other] = (other:Other) =>> project(recEntry)

  def as[Other <: AnyRecord, Rest <: AnyTypeSet](other: Other, rest: Rest)
    (implicit transform: Transform[R, Other, Rest]): Tagged[Other] = transform(recEntry, other, rest)


  def serializeTo[X](implicit serializer: (R SerializeTo X) { type In1 = RawOf[R] }): X = serializer(recEntry)
}
