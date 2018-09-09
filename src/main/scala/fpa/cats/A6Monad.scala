package fpa.cats

object A6Monad extends App {
  import cats.Monad
  import cats.implicits._

  // Monad extends the Applicative type class with a new function flatten.
  // Flatten takes a value in a nested context (eg. F[F[A]] where F is the context) and "joins" the contexts together so that we have a single context (ie. F[A]).

  // Monad instances
  // If Applicative is already present and flatten is well-behaved, extending the Applicative to a Monad is trivial.
  // To provide evidence that a type belongs in the Monad type class, cats' implementation requires us to provide
  // an implementation of pure (which can be reused from Applicative) and flatMap.

  // We can use flatten to define flatMap: flatMap is just map followed by flatten.
  // Conversely, flatten is just flatMap using the identity function x => x (i.e. flatMap(_)(x => x)).

  /*
    implicit def optionMonad(implicit app: Applicative[Option]) =
      new Monad[Option] {
        // Define flatMap using Option's flatten method
        override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] =
          app.map(fa)(f).flatten
        // Reuse this definition from Applicative.
        override def pure[A](a: A): Option[A] = app.pure(a)
      }

      implicit val listMonad = new Monad[List] {
        def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] = fa.flatMap(f)
        def pure[A](a: A): List[A] = List(a)
      }
   */

  println(Monad[Option].pure(42) == Option(42))
  println(Monad[List].flatMap(List(1, 2, 3))(x ⇒ List(x, x)) == List(1, 1, 2, 2, 3, 3))



  // ifM
  // Monad provides the ability to choose later operations in a sequence based on the results of earlier ones.
  // This is embodied in ifM, which lifts an if statement into the monadic context.
  // def ifM[B](fa: F[Boolean])(ifTrue: => F[B], ifFalse: => F[B]): F[B] = flatMap(fa)(bool => if(bool) ifTrue else ifFalse)
  println(Monad[Option].ifM(Option(true))(Option("truthy"), Option("falsy")) == Option("truthy"))
  println(Monad[List].ifM(List(true, false, true))(List(1, 2), List(3, 4)) == List(1, 2, 3, 4, 1, 2))



  // composition
  // Unlike Functors and Applicatives, you cannot derive a monad instance for a generic M[N[_]] where both M[_] and N[_] have an instance of a monad.
  // However, it is common to want to compose the effects of both M[_] and N[_].
  // One way of expressing this is to provide instructions on how to compose any outer monad (F in the following example)
  // with a specific inner monad (Option in the following example).
  // This sort of construction is called a monad transformer. Cats already provides a monad transformer for Option called OptionT.

  /*
      case class OptionT[F[_], A](value: F[Option[A]])

      implicit def optionTMonad[F[_]](implicit F: Monad[F]) = {
        new Monad[OptionT[F, ?]] {
          def pure[A](a: A): OptionT[F, A] = OptionT(F.pure(Some(a)))
          def flatMap[A, B](fa: OptionT[F, A])(f: A => OptionT[F, B]): OptionT[F, B] =
            OptionT {
              F.flatMap(fa.value) {
                case None => F.pure(None)
                case Some(a) => f(a).value
              }
            }
          def tailRecM[A, B](a: A)(f: A => OptionT[F, Either[A, B]]): OptionT[F, B] =
            defaultTailRecM(a)(f)
        }
      }
   */

  import cats.data.OptionT
  println(OptionT.pure[List](42) == OptionT(List(Option(42))))
}
