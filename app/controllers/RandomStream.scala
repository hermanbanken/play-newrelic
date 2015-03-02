package controllers
 
import java.io.InputStream
import scala.util.Random

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