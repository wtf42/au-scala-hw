//Реалзуйте IntArrayBuffer с интерфейсом IntTraversable
trait IntTraversable {
  def isEmpty: Boolean

  def size: Int

  def contains(element: Int): Boolean

  def head: Int

  def tail: IntTraversable

  def ++(traversable: IntTraversable): IntTraversable

  def filter(predicate: Int => Boolean): IntTraversable

  def map(function: Int => Int): IntTraversable

  def flatMap(function: Int => IntTraversable): IntTraversable

  def foreach(function: Int => Unit): Unit
}

class IntArrayBuffer() extends IntTraversable {
  private var buffer = new Array[Int](1)
  private var actualSize = 0

  private def this(elements: Array[Int]) = {
    this()
    buffer = elements
    actualSize = elements.length
    ensureSize(1)
  }

  def apply(index: Int): Int = {
    boundsCheck(index)
    buffer(index)
  }

  def update(index: Int, element: Int): Unit = {
    boundsCheck(index)
    buffer.update(index, element)
  }

  def clear(): Unit = {
    actualSize = 0
  }

  def +=(element: Int): IntArrayBuffer = {
    ensureSize(actualSize + 1)
    buffer.update(actualSize, element)
    actualSize += 1
    this
  }

  def ++=(elements: IntTraversable): IntArrayBuffer = {
    ensureSize(actualSize + elements.size)
    elements.foreach(+=(_))
    this
  }

  def remove(index: Int): Int = {
    val element = apply(index)
    Array.copy(buffer, index + 1, buffer, index, actualSize - index - 1)
    actualSize -= 1
    element
  }

  override def isEmpty: Boolean = actualSize == 0

  override def size: Int = actualSize

  override def contains(element: Int): Boolean =
    buffer.take(size).contains(element)

  override def head: Int = apply(0)

  override def tail: IntArrayBuffer =
    new IntArrayBuffer(buffer.slice(1, size))

  override def ++(traversable: IntTraversable): IntArrayBuffer =
    new IntArrayBuffer(buffer.take(size)) ++= traversable

  protected def ensureSize(size: Int): Unit =
    if (buffer.length < size) {
      var newSize = 1 max buffer.length
      while (newSize < size)
        newSize *= 2
      val result = new Array[Int](newSize)
      Array.copy(buffer, 0, result, 0, actualSize)
      buffer = result
    }

  override def filter(predicate: (Int) => Boolean): IntTraversable =
    new IntArrayBuffer(buffer.take(size).filter(predicate))

  override def map(function: (Int) => Int): IntTraversable =
    new IntArrayBuffer(buffer.take(size).map(function))

  override def flatMap(function: (Int) => IntTraversable): IntTraversable = {
    val result = new IntArrayBuffer()
    foreach(result ++= function(_))
    result
  }

  override def foreach(function: (Int) => Unit): Unit =
    buffer.take(size).foreach(function)

  private def boundsCheck(index: Int) =
    if (index < 0 || index >= size)
      throw new IndexOutOfBoundsException
}

object IntArrayBuffer {
  def empty: IntArrayBuffer = new IntArrayBuffer()

  def apply(elements: Int*): IntArrayBuffer =
    new IntArrayBuffer(elements.toArray)

  def unapplySeq(buffer: IntArrayBuffer): Option[Seq[Int]] =
    if (buffer == null) None else Some(buffer.buffer.take(buffer.size))
}
