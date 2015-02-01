package jp.aquabox.analyzelib.scala.xml

import jp.aquabox.analyzelib.scala.analyze.Analyze
import scala.collection.mutable
import scala.util.parsing.combinator.RegexParsers

/**
 * Created with IntelliJ IDEA.
 * User: motonari
 * Date: 2013/06/16
 * Time: 23:39
 * To change this template use File | Settings | File Templates.
 */
class ArticleData {
  var title = ""
  var content = ""
  var linklist:List[String] = null
  var wordcount:mutable.HashMap[String, Int] = null

  def save:Boolean = {

    if(this.title.startsWith("Wikipedia")) {
      return true
    }

    if(this.title.startsWith("Category")) {
      return true
    }

    if(this.title.startsWith("Template")) {
      return true
    }

    if(this.title.startsWith("Portal")) {
      return true
    }

    if(this.title.startsWith("ファイル")) {
      return true
    }

    val starttime = System.currentTimeMillis()

    try {
    }
    catch {
      case e:Exception => println("continue:" + e.getMessage + ":" + e.getStackTraceString)
    }
    finally {
    }

    val time = System.currentTimeMillis() - starttime
    println("save calc time: " + time)

    true
  }

  def createkeywords = {
    this.wordcount = new mutable.HashMap[String, Int]()

    val analyzer = new Analyze(this.content.take(6000))
    analyzer.parse()
    val nouns = analyzer.data.nouns.take(3000)
    nouns.map {
      case(k,v) if k matches "^[a-zA-Zー]+$" =>
      case(k,v) if wordcount.isDefinedAt(k) == false => wordcount.put(k, v.score)
      case(k,v) => wordcount.update(k, wordcount.get(k).get + v.score)
    }

    val tanalyzer = new Analyze(this.title)
    tanalyzer.parse()
    val tnouns = tanalyzer.data.nouns.take(3000)
    tnouns.map {
      case(k,v) if k matches "^[a-zA-Z]+$" =>
      case(k,v) if wordcount.isDefinedAt(k) == false => wordcount.put(k, v.score * 2)
      case(k,v) => wordcount.update(k, wordcount.get(k).get + v.score * 2)
    }
  }

  def createlinklist = {
    val parser = new LinkDataParser
    println(parser.parse(this.content))
    this.linklist = parser.parse(this.content).get
  }

  def debug = {
    println("title: " + this.title)
    //println("content: " + this.content)
    //println("link: " + this.linklist)
    println("keywords: " + this.wordcount)
  }

  class LinkDataParser extends RegexParsers {
    val data = """[^\[\]]+""".r

    def sections = rep(data~>mac|data~>"["~>".*".r~>" "~>data<~"]"|".+".r)
    def mac = "[["~>data<~"]]"

    def parse(input:String) = parseAll(sections, input)
  }
}