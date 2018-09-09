package fpa.cats

object A2Monoid extends App {
  // Monoid extends the Semigroup type class, adding an empty method to semigroup's combine.
  // The empty method must return a value that when combined with any other instance of that type returns the other instance
  // i.e. (combine(x, empty) == combine(empty, x) == x)

  import cats.Monoid
  import cats.implicits._

  println(s"'${Monoid[String].empty}'")
  println(Monoid[String].combineAll(List("a", "b", "c")) == "abc")
  println(Monoid[String].combineAll(List()) == "")

  println(Monoid[Map[String, Int]].combineAll(List(Map("a" → 1, "b" → 2), Map("a" → 3))) == Map("a" -> 4, "b" -> 2))
  println(Monoid[Map[String, Int]].combineAll(List()) == Map())

  val l = List(1, 2, 3, 4, 5)
  println(l.foldMap(identity) == 15)
  println(l.foldMap(i ⇒ i.toString) == "12345")



  /* - Monoid of tuple is already defined in cats
  implicit def monoidTuple[A: Monoid, B: Monoid]: Monoid[(A, B)] =
    new Monoid[(A, B)] {
      def combine(x: (A, B), y: (A, B)): (A, B) = {
        (x._1 combine y._1, x._2 combine y._2)
      }

      def empty: (A, B) = (Monoid[A].empty, Monoid[B].empty)
    }*/


  val l1 = List(1, 2, 3, 4, 5)
  println(l1.foldMap(i ⇒ (i, i.toString)) == (15, "12345"))
}
