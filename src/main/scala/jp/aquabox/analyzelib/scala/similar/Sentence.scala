package jp.aquabox.analyzelib.scala.similar

import scala.collection.mutable.ListBuffer

/**
 * Created with IntelliJ IDEA.
 * User: motonari
 * Date: 2013/08/22
 * Time: 0:17
 * To change this template use File | Settings | File Templates.
 */
class Sentence {
  val list:ListBuffer[String] = ListBuffer.empty[String]
  val words:ListBuffer[String] = ListBuffer.empty[String]
  val parse:ListBuffer[Word] = ListBuffer.empty[Word]

  def isSentence = {
    var particle = false
    var verb = false
    var noun = false

    this.parse.map {
      case v if v.feature.split(",")(0) == "助詞" => {
        particle = true
      }
      case v if v.feature.split(",")(0) == "動詞" => {
        verb = true
      }
      case v if v.feature.split(",")(0) == "名詞" => {
        noun = true
      }
      case _ => {}
    }

    if(particle && verb && noun) {
      true
    }
    else {
      false
    }


  }
}

class Word(s:String, f:String) {
  val surface = s
  val feature = f

}
