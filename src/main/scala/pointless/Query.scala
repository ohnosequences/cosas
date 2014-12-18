package ohnosequences.pointless

/*
  ## Queries

  Queries represent expressions combining several conditions for a particular item.
  You can combine conditions **either** by `OR` or by `AND` conditional operator (_you can't mix them_).
  Query constructors check that the item has the attribute used in the applied condition.
*/
trait AnyQuery {

  type Body <: AnyQuery
  val  body: Body

  type Head <: AnyCondition
  val  head: Head

  // type TYPE <: AnyItemType
  type ItemType <: AnyPropertiesHolder
  val  itemType: ItemType

  type Out[X] = List[X]
}

/*
  ### OR Queries
*/
trait AnyOrQuery extends AnyQuery {

  type Body <: AnyOrQuery

  def or[Head <: AnyCondition](other: Head)(implicit 
    ev: ItemType HasProperty other.Property
  ): OR[Body, Head] = 
     OR(body, other)
}

case class OR[B <: AnyOrQuery, H <: AnyCondition]
  (val body : B,  val head : H) extends AnyOrQuery {
  type Body = B; type Head = H

  // type TYPE = body.TYPE
  type ItemType = body.ItemType
  val  itemType = body.itemType
} 


/* 
  ### AND Queries
*/
trait AnyAndQuery extends AnyQuery {

  type Body <: AnyAndQuery

  def and[Head <: AnyCondition](other: Head)(implicit 
    ev: ItemType HasProperty other.Property
  ): AND[Body, Head] = 
     AND(body, other)
}

case class AND[B <: AnyAndQuery, H <: AnyCondition]
  (val body : B,  val head : H) extends AnyAndQuery {
  type Body = B; type Head = H

  // type TYPE = body.TYPE
  type ItemType = body.ItemType
  val  itemType = body.itemType 
}


/* 
  ### Simple Queries

  It contains only one condition and can be extended either to `OR` or `AND` predicate
*/
trait AnySimpleQuery extends AnyOrQuery with AnyAndQuery {
  type Body = this.type
  val  body = this: this.type
}

case class SimpleQuery[X <: AnyPropertiesHolder, C <: AnyCondition]
  (val itemType : X,  val head : C) extends AnySimpleQuery {
  type ItemType = X; type Head = C
}


object AnyQuery {

  type HeadedBy[C <: AnyCondition] = AnyQuery { type Head <: C }

  type On[I] = AnyQuery { type ItemType = I }

  /* With this you can write `item ? condition` which means `SimpleQuery(item, condition)` */
  implicit def smthQueryOps[X <: AnyPropertiesHolder](x: X): SmthQueryOps[X] = SmthQueryOps(x)
  case class   SmthQueryOps[X <: AnyPropertiesHolder](x: X) {
    def ?[C <: AnyCondition](c: C)(implicit 
        ev: X HasProperty c.Property
      ): SimpleQuery[X, C] = SimpleQuery(x, c)
  }

}
