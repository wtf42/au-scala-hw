import CurrencyConverter._
import Date._

object Main extends App {
  println(1.usd to rub on 14--11--2016: Double)
  println(1.eur to rub on 14--11--2016: Double)
  println(1.eur to usd on 14--11--2016: Double)

  println(10.usd to rub on 14--11--2016: Double)
  println(usd to rub: Double)
  println(rub to rub: Double)
  println(usd to rub on 42--11--2016: Option[Double])
}
