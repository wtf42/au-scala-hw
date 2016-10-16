/*
TODO Прочитайте содержимое данного файла.
В случае неудачи верните сообщение соответствующего исключения.
 */
def readThisWorksheet(): String = try {
  scala.io.Source.fromFile("/home/user/scala/HomeTask.sc", "utf-8").mkString
} catch {
  case e: Exception => e.getMessage
}
