package pl.touk.nussknacker.prinz

import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpecLike

import java.io.File
import java.sql.{Connection, DriverManager, ResultSet}
import scala.reflect.io.Directory

trait H2Database extends AnyFlatSpecLike with BeforeAndAfterAll {

  private val DATABASE_PARENT_DIR: String = "./h2-test-database"

  private val DATABASE_NAME: String = "prinz-test-db"

  private val DATABASE_DIR: String = s"$DATABASE_PARENT_DIR/$DATABASE_NAME"

  private val DATABASE_URL: String = s"jdbc:h2:$DATABASE_DIR"

  private var connection: Option[Connection] = None

  def executeQuery(query: String): Option[ResultSet] = {
    for {
      stmt <- connection.map(_.createStatement)
    } yield stmt.executeQuery(query)
  }

  def executeUpdate(query: String): Option[Int] = {
    for {
      stmt <- connection.map(_.createStatement)
    } yield stmt.executeUpdate(query)
  }

  override protected def afterAll(): Unit = {
    new Directory(new File(DATABASE_PARENT_DIR)).deleteRecursively()
  }

  override protected def beforeAll(): Unit = {
    connection = Some(DriverManager.getConnection(DATABASE_URL))
  }
}
