import org.scalatest._
import org.scalactic.TolerantNumerics
import CurrencyConverter._
import Date._

class ConverterTest extends FunSuite {
  implicit val doubleEquality = TolerantNumerics.tolerantDoubleEquality(0.001)
  assert(0.0 === 0.0001)
  assert(!(0.0 === 0.1))

  test("args parse") {
    val conversion = 42.usd to rub on 14--11--2016
    assert(conversion.amount === 42.0)
    assertResult("USD")(conversion.pair._1)
    assertResult("RUR")(conversion.pair._2)
    assertResult(14)(conversion.date.day)
    assertResult(11)(conversion.date.month)
    assertResult(2016)(conversion.date.year)

    assert(1.0 === (usd to rub).amount)
    assertResult(("USD", "RUR"))((usd to rub).pair)
  }

  test("results") {
    assert(65.2167 === (1.usd to rub on 14--11--2016: Double))
    assert(71.1253 === (1.eur to rub on 14--11--2016: Double))
    assert(1.0905994936879664 === (1.eur to usd on 14--11--2016: Double))
    assert(652.167 === (10.usd to rub on 14--11--2016: Double))
    assertResult(None)(usd to rub on 42--11--2016: Option[Double])
  }
}