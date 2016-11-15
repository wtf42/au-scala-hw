import scala.io.Codec
import scala.xml.XML

case class CurrencyConverterResult(amount: Double, pair: (String, String)) {
  var date = Date.today
  def on(date: Date) = { this.date = date; this }

  def calculate: Option[Double] = {
    pair match {
      case (a, b) if a == b => Some(1.0)
      case (a, "RUR") => calculateFromRUR(a)
      case ("RUR", a) => calculateFromRUR(a).map(v => 1 / v)
      case (a, b) =>
        val va = calculateFromRUR(a)
        val vb = calculateFromRUR(b)
        va flatMap (aa => vb map (bb => aa / bb))
    }
  } map (v => v * amount)

  private def calculateFromRUR(currency: String): Option[Double] = {
    val url = f"http://www.cbr.ru/scripts/XML_daily.asp?date_req=${date.day}%02d/${date.month}%02d/${date.year}"
    val data = scala.io.Source.fromURL(url)(Codec("CP1251")).mkString
    val xml = XML.loadString(data)
    val value = (xml \\ "ValCurs" \\ "Valute")
      .filter(p => (p \\ "CharCode").text == currency)
      .map(p => (p \\ "Value").text)
      .headOption
    value.map(v => java.text.NumberFormat.getInstance().parse(v).doubleValue())
  }
}

object CurrencyConverterResult {
  implicit def converterResultToDouble(r: CurrencyConverterResult): Double = r.calculate.get
  implicit def converterResultToOptionDouble(r: CurrencyConverterResult): Option[Double] = r.calculate
}
