package com.phone

import org.scalatest.{FlatSpecLike, Matchers}

class CostCalculationSpec extends FlatSpecLike with Matchers with CostCalculation {

  val callsLog = List(
    "A 555-333-212 00:02:03",
    "A 555-433-242 00:06:41",
    "A 555-433-242 00:01:03",
    "B 555-333-212 00:01:20",
    "A 555-333-212 00:01:10",
    "A 555-663-111 00:02:09",
    "A 555-333-212 00:04:28",
    "B 555-334-789 00:00:03",
    "A 555-663-111 00:02:03",
    "B 555-334-789 00:00:53",
    "B 555-971-219 00:09:51",
    "B 555-333-212 00:02:03",
    "B 555-333-212 00:04:31",
    "B 555-334-789 00:01:59"
  )

  it should "Calculate call costs correctly" in {
    calculateCosts(callsLog.iterator) shouldBe Map("A" -> 31.38, "B" -> 30.08)
  }
}
