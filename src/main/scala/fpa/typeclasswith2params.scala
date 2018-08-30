package fpa

trait VO

trait DTO

trait Convert[V <: VO, D <: DTO] {
  def toVO(dto: D): V
  def toDto(vo: V): D
}

object Convert {
  implicit class VO2DTOSyntax[V <: VO](vo: V) {
    def toDto[D <: DTO](implicit converter: Convert[V, D]): D = converter toDto vo
  }

  implicit class DTO2VOSyntax[D <: DTO](dto: D) {
    def toVO[V <: VO](implicit converter: Convert[V, D]): V = converter toVO dto
  }
}

object Convertion {
  implicit val xConverter = new Convert[XVO, XDto] {
    override def toVO(dto: XDto): XVO = XVO(dto.id)

    override def toDto(vo: XVO): XDto = XDto(vo.id)
  }
}

case class XVO(id: String) extends VO

case class XDto(id: String) extends DTO

object Main extends App {
  import Convertion._
  import Convert._

  val vo = XVO("value")
  println(vo.toDto)
  println(vo.toDto.toVO)
}