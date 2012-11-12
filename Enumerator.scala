  /** Create an Enumerator of bytes with an OutputStream.
   */
  def outputStream(a: java.io.OutputStream => Unit): Enumerator[Array[Byte]] = {
    Concurrent.unicast[Array[Byte]] { channel =>
      val outputStream = new java.io.OutputStream(){
        override def close() {
          channel.end()
        }
        override def flush() {}
        override def write(value: Int) {
          channel.push(Array(value.toByte))
        }
        override def write(buffer: Array[Byte]) {
          write(buffer, 0, buffer.length)
        }
        override def write(buffer: Array[Byte], start: Int, count: Int) {
          channel.push(buffer.slice(start, start+count))
        }
      }
      a(outputStream)
    }
  }