package controllers

import java.io.{FileInputStream, InputStream}

import play.api.libs.iteratee._
import play.api.mvc.{Action, _}

import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.index("Your new application is ready."))
  }

  def zipRandom = Action {    
    val r = new java.util.Random()
    val random: Enumerator[(String,InputStream)] = Enumerator.enumerate(Range(0,100))
      .map(i => ("test-zip/README-"+i+".txt", new RandomStream(100000)))

    random.onDoneEnumerating({
      println("Done!")
    })

    println("Starting download")
    Ok.chunked(ZipTool.multiZipStream(random) >>> Enumerator.eof).withHeaders(
      "Content-Type"->"application/zip", 
      "Content-Disposition"->"attachment; filename=test.zip"
    )
  }

  def manyFutures = Action {
    Ok("")
  }

  /**
   * How the issue was discovered was when streaming many files (1000+) in a single request in zip archive
   * @return
   */
  def zipFiles = Action {
    val files: Enumerator[(String,InputStream)] = Enumerator(1,2,3).map(_.toString+".txt").map(f => (f, new FileInputStream(f)))

    println("Starting download")
    Ok.chunked(ZipTool.multiZipStream(files) >>> Enumerator.eof).withHeaders(
      "Content-Type"->"application/zip",
      "Content-Disposition"->"attachment; filename=test.zip"
    )
  }

}