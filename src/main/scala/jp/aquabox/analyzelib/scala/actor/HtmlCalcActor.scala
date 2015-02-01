package jp.aquabox.analyzelib.scala.actor

import akka.actor.Actor
import jp.aquabox.analyzelib.scala.crawler.CrawlerHandler

/**
 * Created with IntelliJ IDEA.
 * User: motonari
 * Date: 2013/08/11
 * Time: 12:41
 * To change this template use File | Settings | File Templates.
 */
class HtmlCalcActor extends Actor {
  override def receive = {
      case v:CrawlerHandler => {
        var t = Thread.currentThread()
        println("thread : id = " + t.getId() + ", name = " + t.getName())
        v.onCrawl()
      }
      case v =>
  }
}
