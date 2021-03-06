package pl.touk.nussknacker.prinz.model

import pl.touk.nussknacker.prinz.util.exceptions.PrinzException

class ModelRunException(cause: Throwable) extends PrinzException(cause) {

  def this(message: String) = this(new Exception(message))
}
