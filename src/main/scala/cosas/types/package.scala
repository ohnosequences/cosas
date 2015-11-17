package ohnosequences.cosas

package object types {

  type :=[T <: AnyType, +V <: T#Raw] = Denotes[V,T]

  type unit = EmptyProductType[AnyType]
  val  unit : unit = new EmptyProductType[AnyType]

  type In[E <: AnyType] = EmptyProductType[E]
  def In[E <: AnyType]: In[E] = new EmptyProductType[E]

}
