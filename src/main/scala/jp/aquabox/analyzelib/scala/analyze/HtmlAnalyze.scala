package jp.aquabox.analyzelib.scala.analyze

import scala.collection.JavaConverters._
import org.jsoup.Jsoup
import org.apache.commons.lang3.StringEscapeUtils
import jp.aquabox.analyzelib.scala.similar.Similar

/**
 * Created with IntelliJ IDEA.
 * User: motonari
 * Date: 2013/07/17
 * Time: 8:25
 * To change this template use File | Settings | File Templates.
 */
class HtmlAnalyze {
  var main_data:String = ""
  var title:String = ""
  var thumbnail:String = ""
  var description:String = ""
  var canonical:String = ""
  var url:String = ""

  def analyze(html:String) = {
    val h = Jsoup.parse(html)

    try {
      this.title = StringEscapeUtils.unescapeHtml4(h.title)
      val ogtitle = StringEscapeUtils.unescapeHtml4(h.head.getElementsByAttributeValue("property", "og:title").get(0).attributes().get("content"))
      if(this.title.length > ogtitle.length || ogtitle.isEmpty) {
      }
      else {
        this.title = ogtitle
      }
    }
    catch {
      case e:Exception =>
    }

    try {
      this.description = StringEscapeUtils.unescapeHtml4(h.head.getElementsByAttributeValue("name", "description").get(0).attributes().get("content"))
    }
    catch {
      case e:Exception => this.description = ""
    }

    try {
      val ogdesc = StringEscapeUtils.unescapeHtml4(h.head.getElementsByAttributeValue("property", "og:description").get(0).attributes().get("content"))
      if(this.description.length < ogdesc.length) {
        this.description = ogdesc
      }
    }
    catch {
      case e:Exception => this.description = ""
    }

    try {
      this.thumbnail = h.head.getElementsByAttributeValue("property", "og:image").get(0).attributes().get("content")
    }
    catch {
      case e:Exception => this.thumbnail = ""
    }

    try {
      this.canonical = h.head.getElementsByAttributeValue("rel", "canonical").get(0).attributes().get("href")
    }
    catch {
      case e:Exception => this.thumbnail = ""
    }

    try {
      this.url = h.head.getElementsByAttributeValue("property", "og:url").get(0).attributes().get("content")
    }
    catch {
      case e:Exception => this.thumbnail = ""
    }

    var t = ""
    val jsoup = Jsoup.parse(html)
    try {
      jsoup.select("input").remove()
    }
    catch {
      case e:Exception =>
    }

    try {
      jsoup.select("script").remove()
    }
    catch {
      case e:Exception =>
    }

    try {
      jsoup.select("noscript").remove()
    }
    catch {
      case e:Exception =>
    }

    try {
      jsoup.select("style").remove()
    }
    catch {
      case e:Exception =>
    }

    try {
      jsoup.select("rdf:RDF").remove()
    }
    catch {
      case e:Exception =>
    }

    t = jsoup.select("body").html()
    t = t.replaceAll("<.+?>", "")
    t = t.replaceAll("http(s)?://([a-zA-Z0-9;%.?&=_/~:]|-)+", "")

    val simirary = new Similar(this.title + "。" + this.description)
    this.main_data = simirary.text(t)
    this.main_data = StringEscapeUtils.escapeHtml4(this.main_data)
  }

}
