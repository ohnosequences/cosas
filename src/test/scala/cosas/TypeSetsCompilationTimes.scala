package ohnosequences.cosas.tests

class TypeSetsCompilationTimes extends org.scalatest.FunSuite {

  import dummyTypes._
  import ohnosequences.cosas._, typeSets._

  val z = 2 :~: ∅

  val u = t1  :~: 
          t2  :~: 
          t3  :~: 
          t4  :~: 
          t5  :~:
          t6  :~:
          t7  :~:
          t8  :~:
          t9  :~:
          ∅

  val x =     t1  :~:
              t2  :~:
              t3  :~:
              t4  :~:
              t5  :~:
              t6  :~:
              t7  :~:
              t8  :~:
              t9  :~:
              t10 :~:
              t11 :~:
              t12 :~:
              t13 :~:
              t14 :~:
              t15 :~:
              t16 :~:
              t17 :~:
              t18 :~:
              t19 :~:
              t20 :~:
              t21 :~:
              t22 :~:
              t23 :~:
              t24 :~:
              t25 :~:
              t26 :~:
              t27 :~:
              t28 :~:
              t29 :~:
              t30 :~:
              t31 :~:
              t32 :~:
              t33 :~:
              t34 :~:
              t35 :~:
              t36 :~:
              t37 :~:
              t38 :~:
              t39 :~:
              t40 :~:
              t41 :~:
              t42 :~:
              t43 :~:
              t44 :~:
              t45 :~:
              t46 :~:
              t47 :~:
              t48 :~:
              t49 :~:
              t50 :~:
              t51 :~:
              t52 :~:
              t53 :~:
              t54 :~:
              t55 :~:
              t56 :~:
              t57 :~:
              t58 :~:
              t59 :~:
              t60 :~:
              ∅
}

object dummyTypes {

  case object t1
  case object t2
  case object t3
  case object t4
  case object t5
  case object t6
  case object t7
  case object t8
  case object t9
  case object t10
  case object t11
  case object t12
  case object t13
  case object t14
  case object t15
  case object t16
  case object t17
  case object t18
  case object t19
  case object t20
  case object t21
  case object t22
  case object t23
  case object t24
  case object t25
  case object t26
  case object t27
  case object t28
  case object t29
  case object t30
  case object t31
  case object t32
  case object t33
  case object t34
  case object t35
  case object t36
  case object t37
  case object t38
  case object t39
  case object t40
  case object t41
  case object t42
  case object t43
  case object t44
  case object t45
  case object t46
  case object t47
  case object t48
  case object t49
  case object t50
  case object t51
  case object t52
  case object t53
  case object t54
  case object t55
  case object t56
  case object t57
  case object t58
  case object t59
  case object t60
}