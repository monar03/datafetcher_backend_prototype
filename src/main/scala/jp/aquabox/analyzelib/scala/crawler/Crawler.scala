package jp.aquabox.analyzelib.scala.crawler

import akka.actor.{Props, ActorSystem}

import scala.collection.mutable.ListBuffer
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import jp.aquabox.analyzelib.scala.actor.HtmlCalcActor

import scala.sys.Prop

/**
 * Created with IntelliJ IDEA.
 * User: motonari
 * Date: 2013/07/17
 * Time: 19:15
 * To change this template use File | Settings | File Templates.
 */
object Crawler {
  val thread = new CrawlerThread

  def start = {
    this.thread.start
  }

  def stop = {
    this.thread.stopFlg = true
  }

  def add(handler:CrawlerHandler) = {
    this.thread.listenerStack += handler
  }

  class CrawlerThread extends Thread {
    var stopFlg = false
    val listenerStack = ListBuffer.empty[CrawlerHandler]

    override def run = {
      this.stopFlg = false
      while(!this.stopFlg) {
        try {
          val start = System.currentTimeMillis()
          if(!this.listenerStack.isEmpty) {
            val listener = listenerStack.remove(0)
            println(listener.url)
            val doc = Jsoup.connect(listener.url).get
            listener.doc = doc
            val actor = ActorSystem()
            val html_actor = actor.actorOf(Props[HtmlCalcActor], name = "htmlcalc")
            html_actor ! listener
          }

          val wait = 3000 - (System.currentTimeMillis() - start)
          if(wait > 0) {
            println("wait : " + wait)
            Thread.sleep(wait)
          }
        }
        catch {
          case e:Exception => {
            println(e.getMessage)
          }
        }
      }

      println("end crawler")
    }
  }
}

class CrawlerHandler(u:String) {
  val url = u
  var doc:Document = null
  def onCrawl() = {

  }
}