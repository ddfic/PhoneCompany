package com

package object phone {
  type CustomerId = String
  type PhoneNumber = String
  type Cost = Double
  type CostSummary = Map[(CustomerId, PhoneNumber), Cost]
}
