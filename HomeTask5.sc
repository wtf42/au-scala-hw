// HList = HNil | Cons(T, HList)
sealed trait HList {
  def ::[H](h: H): HCons[H, this.type] = HCons(h, this)
}
case object HNil extends HList
case class HCons[+H, +T <: HList](head: H, tail: T) extends HList

sealed trait Num
case object Zero extends Num
case class Succ[N <: Num](minus1: N) extends Num

object HList {
  type ::[+H, +T <: HList] = HCons[H, T]
  type HNil = HNil.type
  type Zero = Zero.type

  trait Appendable[L <: HList, R <: HList, Res <: HList] {
    def apply(l: L, r: R): Res
  }
  object Appendable {
    implicit def base[R <: HList]: Appendable[HNil, R, R] = new Appendable[HNil, R, R] {
      override def apply(l: HNil, r: R): R = r
    }
    implicit def step
    [H, L <: HList, R <: HList, Res <: HList]
    (implicit appendable: Appendable[L, R, Res])
    : Appendable[H :: L, R, H :: Res] =
      new Appendable[H :: L, R, H :: Res] {
        override def apply(l: H :: L, r: R): H :: Res = {
          l.head :: appendable.apply(l.tail, r)
        }
      }
  }

  trait Splittable[T <: HList, I <: Num, L <: HList, R <: HList] {
    def apply(l: T, i: I): (L, R)
  }
  object Splittable {
    implicit def base[R <: HList]: Splittable[R, Zero, HNil, R] = new Splittable[R, Zero, HNil, R] {
      override def apply(t: R, i: Zero): (HNil, R) = (HNil, t)
    }
    implicit def step
    [T <: HList, I <: Num, H, L <: HList, R <: HList]
    (implicit splittable: Splittable[T, I, L, R])
    : Splittable[H :: T, Succ[I], H :: L, R] =
      new Splittable[H :: T, Succ[I], H :: L, R] {
        override def apply(t: H :: T, i: Succ[I]): (H :: L, R) = {
          val (l, r) = splittable.apply(t.tail, i.minus1)
          (t.head :: l, r)
        }
      }
  }

  def append[L <: HList, R <: HList, Res <: HList]
  (l: L, r: R)(implicit appendable: Appendable[L, R, Res])
  : Res = {
    appendable(l, r)
  }

  def splitAt[T <: HList, I <: Num, L <: HList, R <: HList]
  (l: T, i: I)(implicit splittable: Splittable[T, I, L, R])
  : (L, R) = {
    splittable.apply(l, i)
  }
}



import HList._

val l: String :: Int :: HNil = "text" :: 1 :: HNil
val r: String :: Boolean :: HNil = "text2" :: false :: HNil
val t = append(l, r)
val (l1, r1) = splitAt(t, Succ(Succ(Zero)))

val ok1 = splitAt(t, Zero)
val ok2 = splitAt(t, Succ(Succ(Succ(Succ(Zero)))))
//val fail = splitAt(t, Succ(Succ(Succ(Succ(Succ(Succ(Zero)))))))
