package pl.touk.nussknacker.prinz.sample

import java.net.URL

import com.typesafe.scalalogging.Logger
import pl.touk.nussknacker.engine.api.Service
import pl.touk.nussknacker.engine.api.process.{ProcessObjectDependencies, SinkFactory, SourceFactory, WithCategories}
import pl.touk.nussknacker.engine.flink.util.sink.EmptySink
import pl.touk.nussknacker.engine.flink.util.transformer.PeriodicSourceFactory
import pl.touk.nussknacker.engine.util.process.EmptyProcessConfigCreator
import pl.touk.nussknacker.prinz.enrichers.PrinzEnricher
import pl.touk.nussknacker.prinz.mlflow.MLFConfig
import pl.touk.nussknacker.prinz.mlflow.repository.MLFRepository

class SampleConfigCreator extends EmptyProcessConfigCreator {

  private val logger = Logger[this.type]

  protected def allCategories[T](obj: T): WithCategories[T] = WithCategories(obj, "FraudDetection", "Recommendations")

  override def sourceFactories(processObjectDependencies: ProcessObjectDependencies): Map[String, WithCategories[SourceFactory[_]]] = Map(
    "periodic" -> allCategories(PeriodicSourceFactory)
  )

  override def sinkFactories(processObjectDependencies: ProcessObjectDependencies): Map[String, WithCategories[SinkFactory]] = Map(
    "empty" -> allCategories(SinkFactory.noParam(EmptySink))
  )

  override def services(processObjectDependencies: ProcessObjectDependencies): Map[String, WithCategories[Service]] = {
    val repo = MLFRepository(new URL(MLFConfig.mlflowProxyUrl))
    val response = repo.listModels
    logger.debug(response.toString)

    if(response.isRight) {
      val modelsList = response.right.get
      modelsList.foldLeft(Map.empty[String, WithCategories[Service]])(
        (s, m) => s + (m.getName.toString -> allCategories(new PrinzEnricher(m)))
      )
    }
    else {
      logger.error(s"Unable to download available models: ${response.left.get.toString}")
      Map()
    }
  }
}
