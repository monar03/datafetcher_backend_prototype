package jp.aquabox.model

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.lifted.ProvenShape

/**
 * Created by motonari on 14/12/17.
 */
class TagData(tag: Tag) extends Table[(String, String, Int)](tag, "tags") {
  def id = column[String]("id")
  def tag_str = column[String]("tag")
  def score = column[Int]("score")

  def * : ProvenShape[(String, String, Int)] = (id, tag_str, score)

}

