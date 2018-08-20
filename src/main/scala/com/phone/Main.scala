package com.phone

import scala.io.Source

object Main extends App with CostCalculation {
  val rawCalls = Source.fromResource("calls.log").getLines()
  val costPerCustomer = calculateCosts(rawCalls)
  costPerCustomer.foreach { case (customer, cost) =>
    // scalastyle:off
    println(s"Customer $customer, cost: $cost")
  }
}
