package jp.aquabox.analyzelib.scala.analyze

import net.reduls.igo.Tagger
import net.reduls.igo.Morpheme
import scala.collection.JavaConverters._
import scala.collection.mutable.Buffer

class Analyze(m:String) {
	val morphological:Tagger = new Tagger("./igo_dict/naistdic")
	val data = new AnalyzeData(m)
	var prevElement:WordData = null
  var prevNoun:String = null

	def parse() = dataCreate(morphological.parse(m).asScala)

	private def dataCreate(nodes:Buffer[Morpheme]) = nodes.map {
		v => {
      val regex = """^[@!#$%&*+,-./\\^_|~]+$""".r
      var bracket = false
      v.surface match {
        case regex() =>
        case _ => {
          v match {
            case v if v.surface == " " => {
              if(this.prevElement != null) {
                this.data.nouns match {
                  case v if v.isDefinedAt(this.prevElement.name) => v.get(this.prevElement.name).get.score + this.prevElement.score
                  case v => v.put(this.prevElement.name, this.prevElement)
                }

                this.prevNoun = this.prevElement.name
              }

              this.prevElement = null

            }
            case v if v.surface == "・" => {
              if(this.prevElement != null) {
                this.data.nouns match {
                  case v if v.isDefinedAt(this.prevElement.name) => v.get(this.prevElement.name).get.score + this.prevElement.score
                  case v => v.put(this.prevElement.name, this.prevElement)
                }

                this.prevNoun = this.prevElement.name
              }

              this.prevElement = null
            }
            case v if v.feature.split(",")(0) == "名詞" && v.feature.split(",")(1) == "数" => {

            }
            case v if v.feature.split(",")(0) == "名詞" && v.feature.split(",")(1) == "非自立" => {

            }
            case v if v.feature.split(",")(0) == "名詞" && v.feature.split(",")(2) == "助数詞" => {

            }
            case v if v.feature.split(",")(0) == "名詞" && v.feature.split(",")(1) == "固有名詞"  => {
              v.feature.split(",")(2) match {
                case s if s == "人名" && this.prevElement != null && this.prevElement.feature == "人名" => {
                  this.prevElement.name += v.surface
                  this.prevElement.score = 20
                  this.prevElement.feature = "氏名"
                  this.data.nouns.put(this.prevElement.name, this.prevElement)

                  this.prevNoun = this.prevElement.name
                  this.prevElement = null
                }
                case s if s == "人名" => {
                  if(this.prevElement != null) {
                    this.data.nouns match {
                      case v if v.isDefinedAt(this.prevElement.name) => v.get(this.prevElement.name).get.score += this.prevElement.score
                      case v => v.put(this.prevElement.name, this.prevElement)
                    }

                    this.prevNoun = this.prevElement.name
                  }

                  val noun = new WordData(v.surface, s, 1)
                  this.prevElement = noun
                  data.nouns.put(v.surface, new WordData(v.surface, "人名", 2))
                }
                case s if s == "地域" => {
                  this.prevElement match {
                    case v2 if v != null => {
                      if(this.prevElement != null) {
                        this.data.nouns match {
                          case v if v.isDefinedAt(this.prevElement.name) => v.get(this.prevElement.name).get.score += this.prevElement.score
                          case v => v.put(this.prevElement.name, this.prevElement)
                        }

                        this.prevNoun = this.prevElement.name
                      }
                      else {
                        val noun = new WordData(v.surface, s, 1)
                        this.prevElement = noun
                      }
                    }
                    case _ =>
                  }

                  val noun = new WordData(v.surface, s, 1)
                  this.prevElement = noun
                }
                case s => {
                  if(this.prevElement != null) {
                    this.data.nouns match {
                      case v if v.isDefinedAt(this.prevElement.name) => v.get(this.prevElement.name).get.score + this.prevElement.score
                      case v => v.put(this.prevElement.name, this.prevElement)
                    }

                    this.prevNoun = this.prevElement.name
                  }
                  else {
                    val noun = new WordData(v.surface, s, 1)
                    this.prevElement = noun
                  }
                  data.nouns.put(v.surface, new WordData(v.surface, "固有名詞", 5))
                }
              }
            }
            case v if v.surface == "こと" => {

            }
            case v if v.surface == "もの" => {

            }
            case v if v.surface == "よう" => {

            }
            case v if v.surface == "その" => {

            }
            case v if v.feature.split(",")(1) == "接尾" => {

            }
            case v if v.feature.split(",")(0) == "名詞" => {
              if(this.prevElement != null) {
                this.prevElement match {
//                  case v2 if this.prevElement == null => this.prevElement = new WordData(v.surface, v.feature.split(",")(1), 1)
                  case v2 => this.prevElement.name += v.surface
                }
              }
              else {
                this.prevElement = new WordData(v.surface, v.feature.split(",")(1), 1)
              }
            }
            case v if v.feature.split(",")(1) == "句点" => {
              if(this.prevElement != null) {
                this.data.nouns match {
                  case v if v.isDefinedAt(this.prevElement.name) => v.get(this.prevElement.name).get.score + this.prevElement.score
                  case v => v.put(this.prevElement.name, this.prevElement)
                }
              }
              this.prevNoun = null
              this.prevElement = null
            }
            case v if v.feature.split(",")(0) == "助詞" => {
              if(this.prevElement != null) {
                this.data.nouns match {
                  case v if v.isDefinedAt(this.prevElement.name) => v.get(this.prevElement.name).get.score + this.prevElement.score
                  case v => v.put(this.prevElement.name, this.prevElement)
                }

                if(this.prevNoun != null && this.prevNoun != this.prevElement.name) {
                  v match {
                    case v if v.feature.split(",")(2) == "連体化" => this.data.cooccurrence += this.prevNoun + ":" + this.prevElement.name
                    case v if v.feature.split(",")(2) == "並立助詞" => this.data.cooccurrence += this.prevNoun + ":" + this.prevElement.name
                    case _ =>
                  }

                }

                this.prevNoun = this.prevElement.name
              }

              this.prevElement = null
            }
            case _ => {
              if(this.prevElement != null) {
                this.data.nouns match {
                  case v if v.isDefinedAt(this.prevElement.name) => v.get(this.prevElement.name).get.score + this.prevElement.score
                  case v => v.put(this.prevElement.name, this.prevElement)
                }

                if(this.prevNoun != null && this.prevNoun != this.prevElement.name) {
                  this.data.cooccurrence += this.prevNoun + ":" + this.prevElement.name
                }

                this.prevNoun = this.prevElement.name
              }

              this.prevNoun = null
              this.prevElement = null
            }
          }
        }
      }
    }
	}
}