package jp.aquabox.model

import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.lifted.ProvenShape

/**
 * Created by motonari on 14/12/16.
 */
class UrlData(tag: Tag) extends Table[(String, String, String, String, String, String, Option[Timestamp])](tag, "site_data") {
  def id = column[String]("id", O.PrimaryKey)

  def url = column[String]("url")

  def domain = column[String]("host")

  def title = column[String]("title", O.DBType("VARCHAR(2048)"))

  def description = column[String]("description", O.DBType("VARCHAR(2048)"))

  def image = column[String]("image")

  def date = column[Option[Timestamp]]("date")

  def * : ProvenShape[(String, String, String, String, String, String, Option[Timestamp])] = (id, url, domain, title, description, image, date)
}