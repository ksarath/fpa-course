package fpa

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import cats.data.NonEmptyList
import cats.data.Validated.Valid

import scala.util.Try
import scala.util.matching.Regex

object domain {

  /** FP models a domain with explicit types, i.e. wrapping primitives if necessary. */
  type Money = BigDecimal
  val Money = BigDecimal
  case class Name(first: String, last: String)
  case class Address(value: String)
  case class PostalCode(value: String)
  case class Phone(country: Option[String], number: String)
  case class Debit(value: Money)
  case class BirthDay(date: LocalDate)

  /** The goal is to allow valid aggregate structures upon construction only. */
  case class DebitRecord(
                          name: Name,
                          address: Address,
                          postalCode: PostalCode,
                          phone: Phone,
                          debit: Debit,
                          birthDay: BirthDay
                        )
}

object validation {

  /** For which we're going to use cats' `Validated` and `ValidatedNel` */
  import cats.data.ValidatedNel
  import cats.data.Validated.{invalid, invalidNel, valid}

  type Error = String
  val  NEL   = NonEmptyList

  /** We define a type class responsible for decoding strings into domain objects */
  trait Decoder[A] {
    def decode(str: String): ValidatedNel[Error, A]
  }

  /** And add a nice syntax to decode decode `String`s */
  implicit class DecoderOps(val str: String) {
    def as[A : Decoder]: ValidatedNel[Error, A] =
      implicitly[Decoder[A]].decode(str)
  }

  /** Inbound data (see `test/resources/data.csv`) needs to adhere to a couple of patterns */
  val RecordPattern: Regex =
    """\"(.*)\",(.*),(.*),(.*),(.*),(.*)""".r
  val PhonePattern: Regex  =
    "(\\+[0-9][0-9])?([0-9]+)".r
  val BirthDayPattern: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy")


  /** And with those in place we can create implicit decoder instances for the domain */
  import domain._

  /** Q1: Decode address as a trimmed (non-empty) string value. Hint, see `Validated.toValidatedNel` */
  implicit val addressDecoder: Decoder[Address] = (str: String) => {
    str.trim match {
      case a if a.isEmpty => invalidNel("Empty address line")
      case a => Valid(Address(a))
    }
  }

  /** Q2: Decode postal code as a trimmed upper cased (non-empty) value. */
  implicit val postalCodeDecoder: Decoder[PostalCode] = (str: String) => {
    str.trim match {
      case a if a.isEmpty => invalidNel("Empty postal code")
      case a => valid(PostalCode(a.toUpperCase))
    }
  }

  /** Q3: Decode name as comma separated first- and last-name string values, both mandatory and trimmed */
  implicit val nameDecoder: Decoder[Name] = (str: String) => {
    val names = str.trim.split(',')
    if(names.length > 1) {
      (names(1).trim, names(0).trim) match {
        case (f, l) if f.nonEmpty && l.nonEmpty => Valid(Name(f, l))
        case _ => invalidNel(s"Invalid name: '$str'")
      }
    } else {
      invalidNel(s"Invalid name: '$str'")
    }
  }

  /** Q4: Decode phone numbers as trimmed sequences of numbers, `-` and ` ` characters removed,
    * numbers starting with a `+` and the following two digits should be decoded as country.
    * E.g. 0612345 -> Phone(None, "0612345") and +31612345 -> Phone(Some("+31"), "612345")
    */
  implicit val phoneDecoder: Decoder[Phone] = (str: String) => {
    val s = str.trim.replaceAll(" ", "").replaceAll("-", "")
    if(s.matches(PhonePattern.regex)) {
      val (country, phone) = if( s.startsWith("+") ) (Option(s.substring(0, 3)), s.substring(3)) else (None, s)
      valid(Phone(country, phone))
    } else {
      invalidNel(s"Invalid phone number: '$str'")
    }
  }

  /** Q5: Decode debit value currencies as Scala doubles, i.e. `String.toDouble` */
  implicit val debitDecoder: Decoder[Debit] = (str: String) => {
    Try(BigDecimal(str.trim)).map(x => Valid(Debit(x))).getOrElse(invalidNel(s"Invalid debit value: '$str'"))
  }

  /** Q6: Decode birthday values according to format `dd/MM/yyyy` */
  implicit val birthDayDecoder: Decoder[BirthDay] = (str: String) => {
    Try(LocalDate.parse(str.trim, BirthDayPattern)).map(x => Valid(BirthDay(x))).getOrElse(invalidNel(s"Invalid birthday: '$str'"))
  }

  /** You will have noted that although each decoder above may return only one
    * error at a time, we still allowed the result of a decoder to hold multiple
    * errors, i.e. the left side of that result has type `ValidatedNel` instead
    * of `Validated`.  The reason is that aggregations of decoded values may hold
    * more than one error message why the decoding failed.
    *
    * Q7: Decode debit record as a sequence of field d that aggregates its results.
    *
    * Hint: We can apply a number of validations during decoding using `Apply[F[_]].mapN(...)`
    * which takes N decoding applications, applies them in sequence, and aggregates the
    * error messages in the left side of the `ValidatedNel` if one or more of the decoding
    * applications fails.
    */
  import cats.implicits._

  implicit val debitRecordDecoder: Decoder[DebitRecord] =
    (str: String) => str match {
      case RecordPattern(c1, c2, c3, c4, c5, c6) =>
        (c1.as[Name], c2.as[Address], c3.as[PostalCode], c4.as[Phone], c5.as[Debit], c6.as[BirthDay]).mapN(DebitRecord)
      case _ =>
        invalidNel(s"Invalid line pattern `${RecordPattern.regex}`: '$str'")
    }

}
