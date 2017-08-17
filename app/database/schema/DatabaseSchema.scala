package database.schema

import java.time.LocalDate
import java.util.UUID

import conversions.SlickConversions
import models.{Company, Person}
import slick.jdbc
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape}


// TODO: split tables into separate classes
// TODO: generate using slick-codegen
/**
  * Domain model for a relational database
  */
trait DatabaseSchema {

  // LocalDate conversion
  import SlickConversions._

  val companies: TableQuery[Companies] = TableQuery[Companies]
  val people: TableQuery[People] = TableQuery[People]
  val allTables = Seq(companies, people)
  val allSchemas: jdbc.PostgresProfile.DDL = allTables map (_.schema) reduce (_ ++ _)

  // table definition
  class Companies(tag: Tag) extends Table[Company](tag, "COMPANIES") {
    // how to convert tuple with data from db into a class and the other way around
    override def * : ProvenShape[Company] = (id, name) <> (Company.tupled, Company.unapply)

    // column definition
    def id: Rep[UUID] = column[UUID]("ID", O.PrimaryKey)

    def name: Rep[String] = column[String]("NAME")
  }

  class People(tag: Tag) extends Table[Person](tag, "PEOPLE") {
    override def * : ProvenShape[Person] =
      (id, firstName, lastName, birthDate, companyId) <> (Person.tupled, Person.unapply)

    def id: Rep[UUID] = column[UUID]("ID", O.PrimaryKey)

    def firstName: Rep[String] = column[String]("FIRST_NAME")

    def lastName: Rep[String] = column[String]("LAST_NAME")

    // conversion for LocalDate (import SlickConversions._) must be specified
    def birthDate: Rep[LocalDate] = column[LocalDate]("BIRTH_DATE")

    def companyId: Rep[UUID] = column[UUID]("COMPANY_ID")

    def company: ForeignKeyQuery[Companies, Company] = foreignKey("FK_COMPANY", companyId, companies)(_.id)
  }

}