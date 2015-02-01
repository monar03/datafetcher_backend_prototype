package jp.aquabox.analyzelib.scala.analyze

import scala.collection.mutable.ListBuffer
import scala.collection.mutable

class AnalyzeData(t:String) {
	val text = t
	val nouns = mutable.HashMap[String, WordData]()
	val verbs = ListBuffer[String]()
	val adjective = ListBuffer[String]()
  val cooccurrence = ListBuffer[String]()

	def cosineSimilarity(v:AnalyzeData) = {
//		val v1 = math.sqrt(this.nouns.distinct.length)
//		val v2 = math.sqrt(v.nouns.distinct.length)
		
//		val v3 = this.nouns.distinct.intersect(v.nouns.distinct).length
	  val v1 = 1
    val v2 = 2
    val v3 = 1
		val cs = v3 / (v1 * v2)
		cs
	}
	
	def debug = {
		println("noun " + this.nouns.toString)
		println("verbs " + this.verbs.toString)
		println("adjective " + this.adjective.toString)
    println("cooccurrence " + this.cooccurrence.toString)
	}
}