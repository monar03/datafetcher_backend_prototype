package jp.aquabox.analyzelib.scala.twitter

import twitter4j.conf.ConfigurationBuilder
import twitter4j._
import scala.collection.JavaConverters._

/**
 * Created with IntelliJ IDEA.
 * User: motonari
 * Date: 2013/07/17
 * Time: 12:29
 * To change this template use File | Settings | File Templates.
 */
object TwitterControl {
  val token = ""
  val secret_token = ""
  val consumer_key = ""
  val consumer_secret_key = ""

  private val cb = new ConfigurationBuilder
  cb.setOAuthAccessToken(this.token)
  cb.setOAuthAccessTokenSecret(this.secret_token)
  cb.setOAuthConsumerKey(this.consumer_key)
  cb.setOAuthConsumerSecret(this.consumer_secret_key)

  def execStream(listener:UserStreamListener) = {
    val tstream = new TwitterStreamFactory(cb.build()).getInstance
    tstream.addListener(listener)
    tstream.user
  }
}
