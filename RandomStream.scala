import play.api._
import play.api.mvc._
import play.api.libs.iteratee._
import java.util.zip._
import java.util.Random
  
class RandomStream(val count: Int) extends InputStream {
  var index = 0
  val r = new Random()
  val data = Range(0, count).map(_=>r.nextLong).map(_.toString).mkString("\n").map(_.toByte).toArray
  
  def read(): Int = {
    if(index < data.length){
      index += 1
      data(index-1)
    }
    else
      -1
  }
}