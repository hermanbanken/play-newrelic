package controllers

import java.io.{FileInputStream, InputStream}

import play.api.libs.iteratee._
import play.api.mvc.{Action, _}

import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def zipRandom = Action {    
    val r = new java.util.Random()
    val random: Enumerator[(String,InputStream)] = Enumerator(Range(0,100)).map(i => ("test-zip/README-"+i+".txt", new RandomStream(100000)))
 
    Ok.chunked(ZipTool.multiZipStream(random) >>> Enumerator.eof).withHeaders(
      "Content-Type"->"application/zip", 
      "Content-Disposition"->"attachment; filename=test.zip"
    )
  }

  def zipFiles = Action {
    val files: Enumerator[(String,InputStream)] = Enumerator(1,2,3).map(_.toString+".txt").map(f => (f, new FileInputStream(f)))
 
    Ok.chunked(ZipTool.multiZipStream(files) >>> Enumerator.eof).withHeaders(
      "Content-Type"->"application/zip", 
      "Content-Disposition"->"attachment; filename=test.zip"
    )
  }

}