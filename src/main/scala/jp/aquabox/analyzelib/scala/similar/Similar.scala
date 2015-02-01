package jp.aquabox.analyzelib.scala.similar

import scala.collection.mutable.ListBuffer

/**
 * Created with IntelliJ IDEA.
 * User: motonari
 * Date: 2013/08/22
 * Time: 0:12
 * To change this template use File | Settings | File Templates.
 */
class Similar(teacher:String) {
  val a = new Analyze
  a.parse(teacher)
  val teach:ListBuffer[String] = a.list.head.list


  def text(text:String) = {
    var str = ""
    var coefficient = 5f

    this.sentences(text) map {
      case v if v.isSentence => {
        val words = v.list
        val cnt = this.count(words.toList, teach.toList)
        val mcnt = this.merge(words.toList, teach.toList).size
        cnt match {
          case c if c / mcnt.toFloat * coefficient > 0.05f => {
            str += this.join(v.words.toList) + "\n"
          }
          case c =>
        }
        coefficient *= 0.7f
      }
      case _ => {}
    }

    str
  }

  def join(words:List[String]) = {
    var str = ""
    words map {
      v => str += v
    }

    str
  }

  def merge(from:List[String], to:List[String]) = {
    var m = from
    to.map {
      case v if from.contains(v) == true => {}
      case v => try {
        m = v :: m
      }
      catch {
        case e:Exception =>
      }
    }

    m
  }

  def count(from:List[String], to:List[String]) = {
    var cnt = 0
    from map {
      case v if to.contains(v) => cnt += 1
      case v =>
    }

    cnt.toFloat
  }

  def pearson(from:List[String], to:List[String]) = {
    val x1 = 1f
    val x2 = 1f

    var ret = 0f
    var sig1 = 0f
    var sig2 = 0f
    var sig3 = 0f
    from.map {
      case v if to.contains(v) == true => try {
        sig1 += (1 - x1) * (1 - x2)
        sig2 += (1 - x1) * (1 - x1)
      }
      catch {
        case e:Exception =>
      }
      case v => try {
        sig1 += ( 1 - x1 ) * ( 0 - x2 )
        sig2 += (1 - x1) * (1 - x1)
        sig3 += (x2) * (x2)
      }
      catch {
        case e:Exception =>
      }
    }

    to.map {
      case v if from.contains(v) == true => sig3 += (1 - x2) * (1 - x2)
      case v => try {
        sig1 += (0 - x1 ) * (1 - x2 )
        sig3 += (1 - x2) * (1 - x2)
        sig2 += (x1) * (x1)
      }
      catch {
        case e:Exception =>
      }
    }

    if(sig2 == 0) {
      sig1 = 0
      sig2 = 1
    }
    if(sig3 == 0) {
      sig1 = 0
      sig3 = 1
    }

    ret = sig1 / (Math.sqrt(sig2).toFloat * Math.sqrt(sig3).toFloat)
  }

  def sentences(text:String):ListBuffer[Sentence] = {
    val list:ListBuffer[Sentence] = ListBuffer.empty[Sentence]
    val analyze:Analyze = new Analyze
    text.split("\n").map {
      case v if v.trim.isEmpty => {

      }
      case v => {
        analyze.parse(v)
        analyze.list.map( v => list += v )
        analyze.list.clear()
      }
    }
    list
  }
}
