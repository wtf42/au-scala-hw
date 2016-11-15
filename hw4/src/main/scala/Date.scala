case class Date(day: Int, month: Int, year: Int)

case class DayMonth(day: Int, month: Int) {
  def --(year: Int) = Date(day, month, year)
}

case class Day(day: Int) {
  def --(month: Int) = DayMonth(day, month)
}

object Date {
  def today: Date = {
    val now = java.time.LocalDate.now
    new Date(now.getDayOfMonth, now.getMonth.getValue, now.getYear)
  }
  implicit def intToDate(value: Int): Day = Day(value)
}
