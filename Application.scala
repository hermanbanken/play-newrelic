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
}
