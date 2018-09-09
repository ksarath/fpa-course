package fpa.cats

object A4Apply extends App {
  // Apply extends the Functor type class (which features the familiar map function) with a new function ap.
  // The ap function is similar to map in that we are transforming a value in a context (a context being the F in F[A]; a context can be Option, List or Future for example).
  // However, the difference between ap and map is that for ap the function that takes care of the transformation is of type F[A => B], whereas for map it is A => B:

  import cats.Apply
  import cats.implicits._

  // map function - by extending functor
  val intToString: Int ⇒ String = _.toString
  val double: Int ⇒ Int = _ * 2
  val addTwo: Int ⇒ Int = _ + 2

  println(Apply[Option].map(Some(1))(intToString) == Some("1"))
  println(Apply[Option].map(Some(1))(double) == Some(2))
  println(Apply[Option].map(None)(addTwo) == None)


  // compose
  // And like functors, Apply instances also compose:
  val listOpt = Apply[List] compose Apply[Option]
  val plusOne = (x: Int) ⇒ x + 1
  println(listOpt.ap(List(Some(plusOne)))(List(Some(1), None, Some(3))) == List(Some(2), None, Some(4)))


  // ap
  // def ap[A, B](ff: F[A => B])(fa: F[A]): F[B] = ff.flatMap(fn => fa map fn)
  println(Apply[Option].ap(Some(intToString))(Some(1)) == Some("1"))
  println(Apply[Option].ap(Some(double))(Some(1)) == Some(2))
  println(Apply[Option].ap(Some(double))(None) == None)
  println(Apply[Option].ap(None)(Some(1)) == None)
  println(Apply[Option].ap(None)(None) == None)



  // ap2, ap3 etc upto ap22
  val addArity2 = (a: Int, b: Int) ⇒ a + b
  println(Apply[Option].ap2(Some(addArity2))(Some(1), Some(2)) == Some(3))
  println(Apply[Option].ap2(Some(addArity2))(Some(1), None) == None)

  val addArity3 = (a: Int, b: Int, c: Int) ⇒ a + b + c
  println(Apply[Option].ap3(Some(addArity3))(Some(1), Some(2), Some(3)) == Some(6))




  // map2, map3 etc upto map22
  println(Apply[Option].map2(Some(1), Some(2))(addArity2) == Some(3))
  println(Apply[Option].map3(Some(1), Some(2), Some(3))(addArity3) == Some(6))




  // tuple2, tuple3 etc upto tuple22
  println(Apply[Option].tuple2(Some(1), Some(2)) == Option((1, 2)))
  println(Apply[Option].tuple3(Some(1), Some(2), Some(3)) == Option(1, 2, 3))



  // Apply builder syntax
  // The |@| operator offers an alternative syntax for the higher-arity Apply functions (apN, mapN and tupleN).
  // All instances created by |@| have map, ap, and tupled methods of the appropriate arity
  val option2 = Option(1) |@| Option(2)
  val option3 = option2 |@| Option.empty[Int]

  println((option2 map addArity2) == Option(3))
  println((option3 map addArity3) == None)
  println((option2 apWith Some(addArity2)) == Option(3))
  println((option3 apWith Some(addArity3)) == None)

  println(option2.tupled == Option(1, 2))
  println(option3.tupled == None)
}
