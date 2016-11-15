case class CurrencyValue(value: Double, currency: String) {
  def to(other: CurrencyValue) =
    new CurrencyConverterResult(value / other.value, (currency, other.currency))
}
