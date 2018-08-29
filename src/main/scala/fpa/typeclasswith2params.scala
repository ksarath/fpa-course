package fpa

trait Convert[VO, DTO] {
  def toVO(dto: DTO): VO
  def toDto(vo: VO): DTO
}

object Convert {
  implicit class VO2DTOSyntax[VO](vo: VO) {
    def toDto[DTO](implicit converter: Convert[VO, DTO]): DTO = converter toDto vo
  }

  implicit class DTO2VOSyntax[DTO](dto: DTO) {
    def toVO[VO](implicit converter: Convert[VO, DTO]): VO = converter toVO dto
  }
}

object Convertion {
  implicit val xConverter = new Convert[XVO, XDto] {
    override def toVO(dto: XDto): XVO = XVO(dto.id)

    override def toDto(vo: XVO): XDto = XDto(vo.id)
  }
}

case class XVO(id: String)

case class XDto(id: String)

object Main extends App {
  import Convertion._
  import Convert._

  val vo = XVO("value")
  println(vo.toDto)
  println(vo.toDto.toVO)
  println(vo)
}