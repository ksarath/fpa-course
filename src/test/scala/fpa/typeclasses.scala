package fpa
package typeclasses
package tests

import java.util.UUID

import org.scalatest._
import library._

class MaskSpec extends FlatSpec with Matchers {

  "The Mask type class" should "default to disclosing everything" in {
    "<any-string>".disclose should be ("<any-string>")
    666.disclose should be ("666")
    6.6.disclose should be ("6.6")

    val UUIDPattern = "([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})".r
    UUID.randomUUID.disclose match {
      case UUIDPattern(_) => // success
      case str: String       => fail(s"Expected to match: ${UUIDPattern.regex} but was: $str")
    }
  }

  it should "not disclose client bank numbers" in {
    val bankNumber = BankNumber("1234567890")
    bankNumber.disclose should be ("BankNumber(masked)")
  }

  it should "not disclose bank numbers used as case class attributes" in {
    val customer = Customer("Name", BankNumber("1234567890"))
    customer.disclose should be ("Customer(Name,BankNumber(masked))")
  }

  it should "have a nice syntax" in {
    val customer = Customer("Name", BankNumber("1234567890"))
    "customer.disclose" should compile
  }
}
