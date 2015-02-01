import java.net.URL
import java.sql.Timestamp
import java.util.{Date, Calendar}

import jp.aquabox.analyzelib.scala.analyze.{Analyze, HtmlAnalyze}
import jp.aquabox.analyzelib.scala.crawler.{CrawlerHandler, Crawler}
import jp.aquabox.analyzelib.scala.similar.Similar
import jp.aquabox.analyzelib.scala.twitter.TwitterControl
import jp.aquabox.model.{TagData, UrlData}
import scala.collection.mutable
import twitter4j._
import scala.slick.driver.MySQLDriver.simple._


/**
 * Created with IntelliJ IDEA.
 * User: motonari
 * Date: 2013/07/10
 * Time: 23:04
 * To change this template use File | Settings | File Templates.
 */
object TwitterCrawler {
  def main(args: Array[String]) {
    Database.forURL("jdbc:mysql://localhost/datafetcher?user=root&password=", driver = "com.mysql.jdbc.Driver") withSession {
      implicit session =>
        val urlData: TableQuery[UrlData] = TableQuery[UrlData]
        val tagData: TableQuery[TagData] = TableQuery[TagData]
        try {
          (urlData.ddl ++ tagData.ddl).create
        }
        catch {
          case e:Exception =>
        }

    }

    Crawler.start

    TwitterControl.execStream(new UserStreamListener {
      def onFriendList(p1: Array[Long]) {}

      def onUserListUnsubscription(p1: User, p2: User, p3: UserList) {}

      def onStallWarning(p1: StallWarning) {}

      def onBlock(p1: User, p2: User) {}

      def onUserListSubscription(p1: User, p2: User, p3: UserList) {}

      def onFollow(p1: User, p2: User) {}

      def onUserListMemberAddition(p1: User, p2: User, p3: UserList) {}

      def onDirectMessage(p1: DirectMessage) {}

      def onUserListUpdate(p1: User, p2: UserList) {}

      def onUnblock(p1: User, p2: User) {}

      def onUserProfileUpdate(p1: User) {}

      def onException(p1: Exception) {}

      def onUserListMemberDeletion(p1: User, p2: User, p3: UserList) {}

      def onDeletionNotice(p1: Long, p2: Long) {}

      def onDeletionNotice(p1: StatusDeletionNotice) {}

      def onFavorite(p1: User, p2: User, p3: Status) {}

      def onScrubGeo(p1: Long, p2: Long) {}

      def onUnfavorite(p1: User, p2: User, p3: Status) {}

      def onStatus(p1: Status) {
        p1.getURLEntities.toList.map (
          v => {
            val h = new CrawlerHandler(v.getExpandedURL) {
              override def onCrawl() = {
                println("url : " + v.getExpandedURL() + " : " + v.getURL + " : " + v.getDisplayURL)

                val wordcount = new mutable.HashMap[String, Int]()
                val a = new HtmlAnalyze
                a.analyze(this.doc.html)

                if(!a.title.isEmpty) {
                  val simiraly = new Similar(a.title + "。" + a.description)
                  val manalyzer = new Analyze(simiraly.text(a.main_data))
                  println("main_data : " + simiraly.text(a.main_data))
                  manalyzer.parse()
                  manalyzer.data.debug
                  val mnouns = manalyzer.data.nouns
                  mnouns.map {
                    case (k, v) if wordcount.isDefinedAt(k) == false => wordcount.put(k, v.score)
                    case (k, v) => wordcount.update(k, wordcount.get(k).get + v.score)
                  }
                  val cooccurrence = manalyzer.data.cooccurrence

                  //title
                  val tanalyzer = new Analyze(a.title)
                  tanalyzer.parse()
                  tanalyzer.data.debug
                  println("title : " + a.title)
                  println("canonical : " + a.canonical)
                  var url = a.canonical
                  if (url.isEmpty) {
                    url = a.url
                    if (url.isEmpty) {
                      url = v.getExpandedURL
                    }
                  }

                  if ((new URL(url)).getHost != "dlvr.it" && (new URL(url)).getHost != "bit.ly" && (new URL(url)).getHost != "ift.tt") {

                    Database.forURL("jdbc:mysql://localhost/datafetcher?user=root&password=", driver = "com.mysql.jdbc.Driver") withSession {
                      implicit session =>
                        val urlData: TableQuery[UrlData] = TableQuery[UrlData]
                        val tagData: TableQuery[TagData] = TableQuery[TagData]
                        urlData +=(url, url, (new URL(url)).getHost, a.title, a.main_data, a.thumbnail, Option(new Timestamp(new Date().getTime)))
                        tanalyzer.data.nouns.map {
                          case (k, v) => tagData +=(url, k, 1)
                        }

                    }

                    val tnouns = tanalyzer.data.nouns
                    tnouns.map {
                      case (k, v) if wordcount.isDefinedAt(k) == false => wordcount.put(k, (v.score * 2))
                      case (k, v) => wordcount.update(k, wordcount.get(k).get + (v.score * 2))
                    }

                    tanalyzer.data.cooccurrence.map(
                      v => cooccurrence += v
                    )

                    var total = 0
                    wordcount.map {
                      case (k, v) => total += v
                    }

                    if (wordcount.size != 0) {
                    }
                  }
                  else {
                    println("skip database insert.")
                  }
                }
              }
            }
            Crawler.add(h)
          }
        )
      }

      def onUserListDeletion(p1: User, p2: UserList) {}

      def onTrackLimitationNotice(p1: Int) {}

      def onUserListCreation(p1: User, p2: UserList) {}

      override def onUnfollow(source: User, unfollowedUser: User): Unit = ???
    })
  }
}
