package fpa.cats

// Cats exercises - https://www.scala-exercises.org/cats

object A1Semigroup extends App {
  // A semigroup for some given type A has a single operation (which we will call combine), which takes two values of type A, and returns a value of type A.
  // This operation must be guaranteed to be associative.
  // That is to say that: ((a combine b) combine c) == (a combine (b combine c)) for all possible values of a,b,c.

  import cats.Semigroup
  import cats.implicits._

  println(Semigroup[Int].combine(1, 2) == 3)

  println(Semigroup[List[Int]].combine(List(1, 2, 3), List(4, 5, 6)) == List(1, 2, 3, 4, 5, 6))

  println(Semigroup[Option[Int]].combine(Option(1), Option(2)) == Option(3))

  println(Semigroup[Option[Int]].combine(Option(1), None) == Option(1))

  println(Semigroup[String].combine("a", "b"))



  println(Semigroup[Int => Int].combine(_ + 1, _ * 10).apply(6) == 67)
  println(((n: Int) => n + 1).combine(_ * 10).apply(6) == 67)

  println(Map("foo" → Map("bar" → 5)).combine(Map("foo" → Map("bar" → 6))) == Map("foo" → Map("bar" → 11)))


  val one: Option[Int] = Option(1)
  val two: Option[Int] = Option(2)
  val n: Option[Int] = None

  println((one |+| two) == Option(3))
  println((n |+| two) == Option(2))
  println((n |+| n) == None)
  println((two |+| n) == Option(2))
}
