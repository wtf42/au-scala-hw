case class CurrencyConverter(value: Double) {
  val usd = new CurrencyValue(value, "USD")
  val rub = new CurrencyValue(value, "RUR")
  val eur = new CurrencyValue(value, "EUR")
}

object CurrencyConverter extends CurrencyConverter(1) {
  implicit def doubleToCurrencyConverter(value: Double): CurrencyConverter =
    new CurrencyConverter(value)
}
