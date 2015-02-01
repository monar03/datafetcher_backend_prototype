package jp.aquabox.analyzelib.scala.html

import java.net.URL
import javax.swing.text.html.parser.ParserDelegator
import scala.util.control.Exception
import java.io.InputStreamReader
import scala.collection.immutable.Vector

class HTMLParser {	
	val handler = new HtmlParserHandler
	
	def parse(url_str:String) = {
		val url:URL = new URL(url_str)
		val url_stream = url.openStream()
		val input = new InputStreamReader(url_stream, "UTF-8")
		try {
			val pd = new ParserDelegator
			pd.parse(input, handler, true)
		}
		catch {
			case e:Exception => {println(e.getMessage())}
		}
		finally {
			input.close()
			url_stream.close()
		}
	}
}