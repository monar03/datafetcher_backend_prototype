package jp.aquabox.analyzelib.scala.html

import javax.swing.text.html.HTMLEditorKit
import javax.swing.text.html.HTML
import javax.swing.text.MutableAttributeSet
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Stack

class HtmlParserHandler extends HTMLEditorKit.ParserCallback {
	val cur_calc_tag:Stack[String] = new Stack[String]
	var tmp_str:StringBuffer = new StringBuffer
	
	val title:StringBuffer = new StringBuffer
	val contents:ListBuffer[String] = ListBuffer()
	val images:ListBuffer[String] = ListBuffer()

	override def handleStartTag(tag:HTML.Tag, attrs:MutableAttributeSet, pos:Int) = tag.toString() match {
		case v if v.toLowerCase().matches("^h[1-9]$") => {this.cur_calc_tag.push(v)}
		case v if v.toLowerCase() == "div" => {this.cur_calc_tag.push(v)}
		case v if v.toLowerCase() == "a" => {this.cur_calc_tag.push(v)}
		case v if v.toLowerCase() == "p" => {this.cur_calc_tag.push(v)}
		case v if v.toLowerCase() == "article" => {this.cur_calc_tag.push(v)}
		case v if v.toLowerCase() == "title" => {this.cur_calc_tag.push(v)}
		case v => {}
	}
	
	override def handleEndTag(tag:HTML.Tag, pos:Int) = tag.toString() match {
		case v if v.toLowerCase() == "title" => {
			cur_calc_tag.pop
			if(this.tmp_str.toString() != "") {
				this.title.append(this.tmp_str)
				this.tmp_str = new StringBuffer()
			}
		}
		case v if cur_calc_tag.length != 0 && v.toLowerCase() == cur_calc_tag.top => {
			cur_calc_tag.pop
			if(this.tmp_str.toString() != "") {
				this.contents += this.tmp_str.toString().trim()
				this.tmp_str = new StringBuffer()
			}
		}
		case v => {}
	}
	
	override def handleComment(comment:Array[Char], length:Int) {
	}
	
	override def handleText(ch:Array[Char], length:Int) = cur_calc_tag match {
		case v if v != "" => tmp_str.append(new String(ch))
		case _ => {}
	}
}