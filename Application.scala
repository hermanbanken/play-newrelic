package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  
  def zip = Action {
    import play.api.libs.iteratee._
    import java.util.zip._

    val r = new java.util.Random()

    val enumerator = Enumerator.outputStream { os =>
      val zip = new ZipOutputStream(os);
      Range(0, 100).map { i =>
        zip.putNextEntry(new ZipEntry("test-zip/README-"+i+".txt"))
        zip.write("Here are 100000 random numbers:\n".map(_.toByte).toArray)
        // Let's do 100 writes of 1'000 numbers
        Range(0, 100).map { j =>
          zip.write((Range(0, 1000).map(_=>r.nextLong).map(_.toString).mkString("\n")).map(_.toByte).toArray);
        }
        zip.closeEntry()
      }
      zip.close()
    }
    Ok.stream(enumerator >>> Enumerator.eof).withHeaders(
      "Content-Type"->"application/zip", 
      "Content-Disposition"->"attachment; filename=test.zip"
    )
  }

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  
}
