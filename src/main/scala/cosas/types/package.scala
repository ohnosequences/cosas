package object types {

  type =:[V <: T#Raw, T <: AnyType] = Denotes[V,T]
  type :=[T <: AnyType, V <: T#Raw] = Denotes[V,T]

  type ValueOf[T <: AnyType] = T#Raw Denotes T
  def  valueOf[T <: AnyType, V <: T#Raw](t: T)(v: V): ValueOf[T] = v =: t
}
