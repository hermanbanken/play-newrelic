import java.io.ByteArrayOutputStream
import java.util.zip._
import play.api.libs.iteratee._
import scala.concurrent.ExecutionContext.Implicits.global

object ZipTool {

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

}