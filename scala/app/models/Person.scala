package models

// import com.google.inject.Inject
// import play.api.data.Form
// import play.api.data.Forms.mapping
// import play.api.data.Forms._
// import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
// import slick.jdbc.JdbcProfile
// import scala.concurrent.{ExecutionContext, Future}

case class Person(id: Long, name: String, nickname: String, birthdate: String, stack: List[String])

// class PeopleTable(tag: Tag) extends Table[Person](tag, "people") {
//   def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
//   def name = column[String]("name")
//   def nickname = column[String]("nickname")
//   def birthdate = column[String]("birthdate")
//   def stack = column[List[String]]("stack")

//   def * = (id, name, nickname, birthdate) <> (Person.tupled, Person.unapply)
// }
// object PersonForm {
//   val form = Form(
//     mapping(
//       "name" -> nonEmptyText,
//       "nickname" -> nonEmptyText,
//       "birthdate" -> nonEmptyText,
//       "stack" -> list(text)
//     )(PersonForm.apply)(PersonForm.unapply)
//   )
// }

// class PersonTable(tag: Tag) extends Table[Person](tag, "person") {
//   def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
//   def name = column[String]("name")
//   def nickanme = column[String]("nickname")
//   def birthdate = column[String]("birthdate")
//   def stack = column[List[String]]("stack")

//   override def * = (id, name, nickanme, birthdate, stack) <> (Person.tupled, Person.unapply)
// }

// class PersonList @Inject()(
//   protected val dbConfigProvider: DatabaseConfigProvider
// )(implicit executionCtx: ExecutionContext)
//     extends HasDatabaseConfigProvider[JdbcProfile] {

//   val persons = TableQuery[PersonTable]

//   def add(person: Person): Future[Long] = {
//     dbConfig.db
//     .run(persons += person)
//     .map(_.id)
//     .recover {
//       case ex: Exception => {
//         printf(ex.getMessage())
//       }
//     }
//   }

//   def delete(id: Long): Future[Int] = {
//     dbConfig.db.run(persons.filter(_.id === id).delete)
//   }

//   def getAll: Future[Seq[Person]] = {
//     dbConfig.db.run(persons.result)
//   }
// }