package pl.touk.nussknacker.prinz.pmml.model

import pl.touk.nussknacker.prinz.util.collection.immutable.VectorMultimap

object VectorMultimapUtils {
  implicit class VectorMultimapAsRowset[K, V](val inputsMap: VectorMultimap[K, V]) {
    def forEachRow[T](f: Map[K, V] => T) : IndexedSeq[T] = {
      val totalTuples = inputsMap.map(_._2.length).max
      val iteratorsMap = inputsMap.mapVectors(_.iterator)

      (1 to totalTuples) map (_ => {
        val row = iteratorsMap map {
          case (key, iter) if iter.hasNext => (key, iter.next())
        }
        f(row.toMap)
      })
    }
  }
}