package jp.aquabox.model

import java.sql.Timestamp

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.lifted.ProvenShape

/**
  * Created by motonari on 15/02/01.
  */
class SitelistData(tag: Tag) extends Table[(String, String, String, String, Option[Timestamp])](tag, "site_list") {
   def id = column[String]("id", O.PrimaryKey)

   def title = column[String]("title")

   def image =column[String]("description")

   def description =column[String]("description")

   def date = column[Option[Timestamp]]("date")

   def * : ProvenShape[(String, String, String, String, Option[Timestamp])] = (id, title, image, description, date)
 }
