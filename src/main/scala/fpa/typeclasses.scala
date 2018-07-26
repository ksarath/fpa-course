package fpa
package typeclasses

import fpa.typeclasses.library.Mask

object library {

  /** Type class that (optionally) masks, i.e. hides, information when disclosing it as a string. */
  trait Mask[A] {
    def disclose(a: A): String
  }

  implicit class MaskOps[A: Mask](a: A) {
    val disclose = implicitly[Mask[A]].disclose(a)
  }

  /** Type class companion object containing default Mask instances */
  object Mask {
    /** The default is not to mask at all */
    implicit def noMask[A]: Mask[A] = (a: A) => a.toString

  }
}

// Client side domain
case class BankNumber(str: String)

object BankNumber {
  implicit val maskBankNumber: Mask[BankNumber] = (a: BankNumber) => "BankNumber(masked)"
}

case class Customer(name: String, bankNumber: BankNumber)

object Customer {
  import library.MaskOps

  implicit def maskCustomer: Mask[Customer] =
    (customer: Customer) => s"Customer(${customer.name},${customer.bankNumber.disclose})"
}