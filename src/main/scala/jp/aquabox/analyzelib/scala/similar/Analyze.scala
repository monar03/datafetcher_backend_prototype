package jp.aquabox.analyzelib.scala.similar

import scala.collection.mutable.{ListBuffer, Buffer}
import net.reduls.igo.{Tagger, Morpheme}
import scala.collection.JavaConverters._

/**
 * Created with IntelliJ IDEA.
 * User: motonari
 * Date: 2013/08/22
 * Time: 0:13
 * To change this template use File | Settings | File Templates.
 */
class Analyze {
  val morphological:Tagger = new Tagger("./igo_dict/naistdic")
  val list:ListBuffer[Sentence] = ListBuffer.empty[Sentence]

  def parse(m:String) = dataCreate(morphological.parse(m).asScala)

  private def dataCreate(nodes:Buffer[Morpheme]) = {
    var nflag = false
    var pflag = false
    var bracket = 0
    var bracket_str = ""

    var s:Sentence = new Sentence
    nodes.map {
      v => {
        s.words += v.surface
        s.parse += new Word(v.surface, v.feature)

        v match {
          case v if v.feature.split(",")(1) == "括弧開" => {
            bracket+=1
          }
          case v if v.feature.split(",")(1) == "括弧閉" => {
            s.list += bracket_str
            bracket-=1
            bracket_str = ""
          }
          case v if bracket > 0 => {
            bracket_str += v.surface
          }
          case v if v.surface == "\n" => {
            s.list += v.surface
            this.list += s
            s = new Sentence
          }
          case v if v.surface == "。" => {
            s.list += v.surface
            this.list += s
            s = new Sentence
          }
          case v if v.surface == "!" || v.surface == "！" => {
            s.list += v.surface
            this.list += s
            s = new Sentence
          }
          case v if v.surface == "?" || v.surface == "？" => {
            s.list += v.surface
            this.list += s
            s = new Sentence
          }
          case v if v.feature.split(",")(0) == "名詞" => {
            s.list += v.surface
            nflag = true
          }
          case v if v.feature.split(",")(0) == "助詞" => {
            pflag = true
          }
          case v => {}
        }
      }
    }

    if(!s.list.isEmpty) {
      this.list += s
    }

    if(!nflag || !pflag) {
      this.list.clear()
    }
  }
}
