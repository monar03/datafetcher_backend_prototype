package jp.aquabox.analyzelib.scala.analyze

/**
 * Created with IntelliJ IDEA.
 * User: motonari
 * Date: 2013/07/18
 * Time: 7:14
 * To change this template use File | Settings | File Templates.
 */
class WordData(n:String, f:String, s:Int) {
  var score = s
  var feature = f
  var name = n

  override def toString:String = {
    val s = name + ":" + feature + ":" + score
    return s
  }
}
