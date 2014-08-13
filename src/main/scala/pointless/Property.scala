package ohnosequences.pointless

import scala.reflect.ClassTag

/* Properties */
trait AnyProperty extends Representable {

  val label: String
  // TODO make this optional?
  val classTag: ClassTag[Raw]
}