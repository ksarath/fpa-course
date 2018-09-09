package fpa.cats

object A3Functor extends App {
  // A Functor is a ubiquitous type class involving types that have one "hole", i.e. types which have the shape F[?], such as Option, List and Future.
  // (This is in contrast to a type like Int which has no hole, or Tuple2 which has two holes (Tuple2[?,?])).
  //
  // The Functor category involves a single operation, named map:
  // def map[A, B](fa: F[A])(f: A => B): F[B]

  import cats.Functor
  import cats.implicits._

  /*implicit val optionFunctor: Functor[Option] = new Functor[Option] {
    def map[A, B](fa: Option[A])(f: A => B) = fa map f
  }

  implicit val listFunctor: Functor[List] = new Functor[List] {
    def map[A, B](fa: List[A])(f: A => B) = fa map f
  }*/

  /*
   implicit def function1Functor[In]: Functor[Function1[In, ?]] =
    new Functor[Function1[In, ?]] {
      def map[A, B](fa: In => A)(f: A => B): Function1[In, B] = fa andThen f
    }
   */

  println(Functor[List].map(List("qwer", "adsfg"))(_.length) == List(4, 5))
  println(Functor[Option].map(Option("Hello"))(_.length) == Option(5))
  println(Functor[Option].map(None: Option[String])(_.length) == None)




  // lift =
  // we can define lift using map
  // def map[A, B](fa: F[A])(f: A => B): F[B]
  //
  // def lift[A, B](f: A => B): F[A] => F[B] = x => map(x)(f)
  //
  // We can use Functor to "lift" a function from A => B to F[A] => F[B]:
  val lenOption: Option[String] => Option[Int] = Functor[Option].lift(_.length)
  println(lenOption(Some("abcd")) == Option(4))

  val lenOption1: Option[String] ⇒ Option[Int] = Functor[Option].lift(_.length)
  println(lenOption1(Some("Hello")) == Option(5))



  // fproduct
  // we can define fproduct using map
  // def map[A, B](fa: F[A])(f: A => B): F[B]
  //
  // def fproduct[A, B](fa: F[A])(f: A => B): F[(A, B)] = map(fa)(a => (a, f(a)))
  //
  // Functor provides an fproduct function which pairs a value with the result of applying a function to that value.
  val source = List("Cats", "is", "awesome")
  val product = Functor[List].fproduct(source)(_.length)

  println(product == List(("Cats", 4), ("is", 2), ("awesome", 7)))




  // compose
  // Functors compose! Given any functor F[_] and any functor G[_] we can create a new functor F[G[_]] by composing them
  //
  // def map[A, B](fga: F[G[A]])(f: A => B): F[G[B]] = map(fga)(ga => map(ga)(f))
  val listOpt = Functor[List] compose Functor[Option]
  println(listOpt.map(List(Some(1), None, Some(3)))(_ + 1) == List(Some(2), None, Some(4)))
}
