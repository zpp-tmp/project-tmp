package pl.touk.nussknacker.prinz.model.repository
import pl.touk.nussknacker.prinz.model.{Model, ModelName}

class CompositeModelRepository private(repositories: List[ModelRepository]) extends ModelRepository {

  override def listModels: RepositoryResponse[List[Model]] =
    repositories.map(_.listModels).partition(_.isLeft) match {
      case (Nil, lists) => Right((for (Right(list) <- lists) yield list).flatten)
      case(exceptions,  _) => Left((for (Left(exc) <- exceptions) yield exc).head)
    }

  override def getModel(name: ModelName): RepositoryResponse[Model] = {
    val firstRepository = repositories.head
    repositories.drop(1)
      .foldLeft(firstRepository.getModel(name))(accumulateFirstAvailable(name))
  }

  private def accumulateFirstAvailable(name: ModelName)(acc: RepositoryResponse[Model], repository: ModelRepository): RepositoryResponse[Model] =
    if (acc.isRight) {
      acc
    }
    else {
      repository.getModel(name)
    }
}

object CompositeModelRepository {

  def apply(repositories: ModelRepository*): CompositeModelRepository = {
    if (repositories.size < 1) {
      throw new IllegalArgumentException("CompositeModelRepository needs at least one ModelRepository")
    }
    new CompositeModelRepository(repositories.toList)
  }
}
