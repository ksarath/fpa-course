package fpa.cats

object B1Foldable extends App {
  import cats.Foldable
  import cats.implicits._

  // foldLeft is an eager left-associative fold on F using the given function
  println(Foldable[List].foldLeft(List(1, 2, 3), 0)(_ + _) == 6)
  println(Foldable[List].foldLeft(List("a", "b", "c"), "")(_ + _) == "abc")


  // foldRight is a lazy right-associative fold on F using the given function.
  // The function has the signature (A, Eval[B]) => Eval[B] to support laziness in a stack-safe way
  import cats.{Later, Now}
  val lazyResult =
  Foldable[List].foldRight(List(1, 2, 3), Now(0))((x, rest) ⇒ Later(x + rest.value))
  println(lazyResult.value == 6)



  // fold, also called combineAll, combines every value in the foldable using the given Monoid instance.
  // def fold[A, B](fa: F[A])(implicit m: Monoid[A]): A = foldLeft(fa)(m.empty) { (acc, a) => A.combine(acc, a) }
  println(Foldable[List].fold(List("a", "b", "c")) == "abc")
  println(Foldable[List].fold(List(1, 2, 3)) == 6)



  // foldMap is similar to fold but maps every A value into B and then combines them using the given Monoid[B] instance.
  println(Foldable[List].foldMap(List("a", "b", "c"))(_.length) == 3)
  println(Foldable[List].foldMap(List(1, 2, 3))(_.toString) == "123")



  // foldK is similar to fold but combines every value in the foldable using the given MonoidK[G] instance instead of Monoid[G].
  println( Foldable[List].foldK(List(List(1, 2), List(3, 4, 5))) )
  println( Foldable[List].foldK(List(None, Option("two"), Option("three"))) )



  // find searches for the first element matching the predicate, if one exists.
  println(Foldable[List].find(List(1, 2, 3))(_ > 2) == Some(3))
  println(Foldable[List].find(List(1, 2, 3))(_ > 5) == None)



  // exists checks whether at least one element satisfies the predicate.
  println(Foldable[List].exists(List(1, 2, 3))(_ > 2) == true)
  println(Foldable[List].exists(List(1, 2, 3))(_ > 5) == false)



  // forall checks whether all elements satisfy the predicate.
  println(Foldable[List].forall(List(1, 2, 3))(_ <= 3) == true)
  println(Foldable[List].forall(List(1, 2, 3))(_ < 3) == false)




  // toList - Convert F[A] to List[A].
  println(Foldable[List].toList(List(1, 2, 3)) == List(1, 2, 3))
  println(Foldable[Option].toList(Option(42)) == List(42))
  println(Foldable[Option].toList(None) == List())




  // filter - Convert F[A] to List[A] only including the elements that match a predicate.
  println(Foldable[List].filter_(List(1, 2, 3))(_ < 3) == List(1, 2))
  println(Foldable[Option].filter_(Option(42))(_ != 42) == List())
}
