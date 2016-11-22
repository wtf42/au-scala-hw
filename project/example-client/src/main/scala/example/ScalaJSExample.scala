package example

import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.raw.{Element, HTMLElement}
import shared.SharedMessages
import org.scalajs.jquery.{JQuery, jQuery => $}

import scalatags.JsDom._
import all._

object ScalaJSExample extends js.JSApp {
  case class Cell(t: String, x: Int, y: Int)
  val tab1 = "t1"
  val tab2 = "t2"

  def main(): Unit = {
    $("#opponent").html("your opponent: wtf42")
    dom.document.getElementById("fields").appendChild(div() (
      div(float.left, width:=500) { genTab(tab1) },
      div(float.left, width:=500) { genTab(tab2) }
    ).render)
    $(".cell").click(((e: HTMLElement) =>
      cellClick(Cell(
        e.getAttribute("t"),
        e.getAttribute("x").toInt,
        e.getAttribute("y").toInt))
      ): js.ThisFunction
    )
  }
  def genTab(tid: String) =
    table(id:=tid) {
      tbody() {
        for (r <- 1 to 10) yield tr() {
          for (c <- 1 to 10) yield td() {
            div("t".attr:=tid, "x".attr:=r, "y".attr:=c, "s".attr:=0, `class`:="cell")
          }
        }
      }
    }
  def cellClick(c: Cell): Unit = {
    val cell = getCell(c)
    cell.removeClass("c0")
    cell.addClass("c1")
  }
  def getCell(c: Cell): JQuery = $(f".cell[t=${c.t}][x=${c.x}][y=${c.y}]")
}
