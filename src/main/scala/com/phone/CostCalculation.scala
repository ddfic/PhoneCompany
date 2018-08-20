package com.phone

import scala.annotation.tailrec

object CostCalculation {
  val RateThresholdSeconds = 180
  val CallCostRate1 = 0.05
  val CallCostRate2 = 0.03
  val RoundingScale = 2
}

trait CostCalculation {
  import CostCalculation._

  def calculateCosts(rawCalls: Iterator[String]): Map[CustomerId, Cost] = {
    val costSummary = calculateCostSummary(rawCalls)
    calculateCostPerCustomer(costSummary)
  }

  private def calculateCostPerCustomer(costSummary: CostSummary) = {
    costSummary.groupBy { case ((customerId, _), _) => customerId }
      .map { case (customerId, customerCostSummary) =>
        val callCosts = customerCostSummary.values.toList
        (customerId, round(callCosts.sum - callCosts.max))
      }
  }

  @tailrec
  private def calculateCostSummary(rawCalls: Iterator[String], costSummary: CostSummary = Map.empty): CostSummary = {
    if(rawCalls.hasNext) {
      val updatedCostSummary = for {
        call <- parseCall(rawCalls.next)
      } yield {
        val callCost = calculateCallCost(call.callDurationSeconds)
        val costOfPreviousCalls: Cost = costSummary.getOrElse((call.customerId, call.phoneNumberCalled), 0)
        val totalCost = callCost + costOfPreviousCalls
        costSummary + ((call.customerId, call.phoneNumberCalled) -> totalCost)
      }
      calculateCostSummary(rawCalls, updatedCostSummary.getOrElse(costSummary))
    }
    else {
      costSummary
    }
  }

  private def parseCall(call: String) = {
    call.split(" ") match {
      case Array(customerId, phoneNumberCalled, callDuration) =>
        Some(Call(
          customerId,
          phoneNumberCalled.replace("-", ""),
          getNumberOfSeconds(callDuration).getOrElse(0)
        ))
      case _ => None
    }
  }

  private def getNumberOfSeconds(x: String) = {
    x.split(":") match {
      case Array(hours, minutes, seconds) =>
        Some(hours.toInt * 3600 + minutes.toInt * 60 + seconds.toInt)
      case _ => None
    }
  }

  private def calculateCallCost(callDuration: Int) = {
    if(callDuration > RateThresholdSeconds) {
      RateThresholdSeconds * CallCostRate1 + (callDuration - RateThresholdSeconds) * CallCostRate2
    }
    else {
      callDuration * CallCostRate1
    }
  }

  private def round(x: Double) = BigDecimal(x).setScale(RoundingScale, BigDecimal.RoundingMode.HALF_UP).toDouble
}
