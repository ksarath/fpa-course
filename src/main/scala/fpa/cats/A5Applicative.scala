package fpa.cats

object A5Applicative extends App {
  // Applicative extends Apply by adding a single method, pure:
  // def pure[A](x: A): F[A]

  import cats.{Applicative, Monad}
  import cats.implicits._

  println(Applicative[Option].pure(1) == Option(1))
  println(Applicative[List].pure(1) == List(1))


  // compose
  println((Applicative[List] compose Applicative[Option]).pure(1) == List(Some(1)))


  // applicative functors and monads
  // Applicative is a generalization of Monad, allowing expression of effectful computations in a pure functional way.
  // Applicative is generally preferred to Monad when the structure of a computation is fixed a priori.
  // That makes it possible to perform certain kinds of static analysis on applicative values.
  println(Monad[Option].pure(1) == Option(1))
  println(Applicative[Option].pure(1) == Option(1))
}
