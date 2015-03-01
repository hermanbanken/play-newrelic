package controllers

import play.api._
import play.api.mvc._
import play.api.libs.iteratee._
import java.util.zip._
    
object Application extends Controller {

  def zipRandom = Action {    
    val r = new java.util.Random()
    val random = Enumerator(Range(0,100)).map(i => ("test-zip/README-"+i+".txt", new RandomStream(100000)))

    Ok.chuncked(random.map(multiZipStream(_)) >>> Enumerator.eof).withHeaders(
      "Content-Type"->"application/zip", 
      "Content-Disposition"->"attachment; filename=test.zip"
    )
  }

  def zipFiles = Action {
    val files = List(1,2,3).map(_.toString+".txt").map(n => (n, new File(n)))

    Ok.chuncked(Enumerator(files).map(multiZipStream(_)) >>> Enumerator.eof).withHeaders(
      "Content-Type"->"application/zip", 
      "Content-Disposition"->"attachment; filename=test.zip"
    )
  }

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  /**
   * Writes Zip archive using Enumerator's
   */
  def multiZipStream(in: Enumerator[(String,java.io.InputStream)]) = {
    val bs = new ByteArrayOutputStream
    val zos = new ZipOutputStream(bs)

    var r : Array[Byte] = null
    def getReset(s: ByteArrayOutputStream) = {
      r = s.toByteArray
      s.reset()
      r
    }

    val e = in.flatMap { case (fileName, is) =>
      // Entry header
      Enumerator({
        //print("f")
        zos.putNextEntry(new ZipEntry(fileName))
        getReset(bs)  
      }) >>>
      // File parts
      Enumerator.fromStream(is, 64 * 1024).map(data => {
        //print(".")
        zos.write(data)
        getReset(bs)
      }) >>>
      // End of entry
      Enumerator(1).map(_ => {
        //print("c")
        is.close()
        zos.closeEntry()
        getReset(bs)
      })
    } >>> 
    // Close zip
    Enumerator(1).map(_ => {
      //print("|\n")
      zos.close()
      getReset(bs)
    })
    
    // e.onDoneEnumerating({ println("Done streaming") })
    e
  }
  
  class RandomStream(val count: Int) extends InputStream {
    var index = 0
    val data = Range(0, count).map(_=>r.nextLong).map(_.toString).mkString("\n").map(_.toByte).toArray
    
    def read(): int = {
      if(index++ < data.length)
        data[index]
      else
        -1
    }
  }
}
