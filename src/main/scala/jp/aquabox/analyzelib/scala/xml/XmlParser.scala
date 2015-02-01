package jp.aquabox.analyzelib.scala.xml

import org.xml.sax.helpers.DefaultHandler

class XmlParser extends DefaultHandler {
	var articles = List.empty[ArticleData]
  var article:ArticleData = null
  var datatype = ""
  var sublink = new SublinkData

  override def startDocument() = {
		println("document start")
	}

	override def startElement(uri:String, localName:String, qName:String, arrtibute:org.xml.sax.Attributes) = qName match {
		case "page" => { this.article = new ArticleData() }
    case _ => { this.datatype = qName }
	}
	
	override def characters(ch:Array[Char], offset:Int, length:Int) = this.datatype match {
    case "title" => { this.article.title = new String(ch, offset, length) }
    case "text" => { this.article.content += new String(ch, offset, length) }
    case _ => {}
	}
	
	override def endElement(uri:String, localName:String, qName:String) = qName match {
    case "page" => {
//      this.articles = this.article :: this.articles
      this.article.save
     }
    case "text" => {
      this.article.createkeywords
      //this.article.createlinklist
    }
    case _ => { this.datatype = "" }
	}
	
	override def endDocument() = {
		println("document end")
	}
}