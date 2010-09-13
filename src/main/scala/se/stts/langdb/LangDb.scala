package se.stts.langdb // TA BORT FRAN KODRUTA

import org.squeryl._ // TA BORT FRAN KODRUTA
import org.squeryl.dsl._ // TA BORT FRAN KODRUTA
import dsl.CompositeKey2 // TA BORT FRAN KODRUTA
import annotations._ // TA BORT FRAN KODRUTA

// This import gives you 'oneToManyRelation', '===', etc...
import org.squeryl.PrimitiveTypeMode._  // TA BORT FRAN KODRUTA


// A Scala 'trait' is like a mix of Java's 'abtract class' and
// 'interface.  Exending following trait, gives a Squeryl DB object an
// auto-incremented primary key, 'id', of type Int:
 
trait AutoId extends KeyedEntity[Int] {
  val id: Int = 0
}



// Declaring a 'case class' adds functionality to a class. For
// example, 'getters and setters' for constructor arguments and other
// nice stuff.

case class Lang(iso: String, enName: String, cBlock: Option[String]) extends AutoId {
  // 'Option' construnctor parameters make the following zero-argument
  // default constructor necessary (this is a Squeryl thing):
  def this() = this("", "", Some("")) // TA BORT FRAN KODRUTA
  // TA BORT FRAN KODRUTA
  lazy val aNames: OneToMany[AltName] = LangDb.langToAltNames.left(this)
  lazy val families = LangDb.langsAndFamilies.left(this)
  lazy val countries = LangDb.langsAndCountries.left(this)
}

case class AltName(name: String, langId: Int = 0) extends AutoId {
  lazy val enLang: Lang = LangDb.langToAltNames.right(this).single
}


case class Country(iso: String, enName: String, area: Option[String]) extends AutoId 
{ // TA BORT FRAN KODRUTA
  def this() = this("", "", Some(""))  // TA BORT FRAN KODRUTA
} // TA BORT FRAN KODRUTA


// ISO codes for language families: ISO 639-5 
case class Family(name: String, iso: Option[String]) extends AutoId 
{ // TA BORT FRAN KODRUTA
  def this() = this("", Some("")) // TA BORT FRAN KODRUTA
} // TA BORT FRAN KODRUTA


// Association table between Lang and Country, between which there is a many-to-many relationship:
case class LangAndCountry(official: Option[Boolean], speakers: Option[Long], 
                          langId: Int = 0, countryId: Int = 0) 
  extends KeyedEntity[CompositeKey2[Int,Int]] {
  def this() = this(Some(false), Some(0), 0, 0) // TA BORT FRAN KODRUTA
  def id = compositeKey(countryId, langId)
}

// Association table between Lang and Family, between which there is a many-to-many relationship:
case class LangAndFamily(langId: Int = 0, familyId: Int = 0) 
  extends KeyedEntity[CompositeKey2[Int,Int]] {
  def id = compositeKey(langId, familyId)
}

object LangDb extends Schema {
  // TA BORT FRAN KODRUTA
  //
  // Basic data tables
  //
  val langs: Table[Lang] = table[Lang]
  val altNames = table[AltName]
  val countries = table[Country]
  val families = table[Family]

  on(langs)(l => declare(columns(l.iso) are(unique,indexed)))// TA BORT FRAN KODRUTA
  on(countries)(c => declare(columns(c.iso) are(unique,indexed)))// TA BORT FRAN KODRUTA
  on(families)(f => declare(// TA BORT FRAN KODRUTA
    columns(f.iso) are(unique,indexed),// TA BORT FRAN KODRUTA
    columns(f.name) are(unique,indexed)// TA BORT FRAN KODRUTA
  ))// TA BORT FRAN KODRUTA
  on(altNames)(a => declare(// TA BORT FRAN KODRUTA
    columns(a.langId, a.name) are(unique, indexed)// TA BORT FRAN KODRUTA
  ))// TA BORT FRAN KODRUTA
  // TA BORT FRAN KODRUTA
  //
  // Relations
  //
  // TA BORT FRAN KODRUTA
  //
  // Association tables (for many-to-many mappings between two tables)
  //
  // langAndCountry is a ManyToManyRelation, it extends Table[LangAndCountry]
  // TA BORT FRAN KODRUTA  
  val langsAndCountries = 
    manyToManyRelation(langs, countries).via[LangAndCountry]((l, c, lic) => 
      (l.id === lic.langId, c.id === lic.countryId))

  val langsAndFamilies = 
    manyToManyRelation(langs, families).via[LangAndFamily]((l, f, laf) => 
      (l.id === laf.langId, f.id === laf.familyId))

  val langToAltNames = 
    oneToManyRelation(langs, altNames).via((l, a) => l.id === a.langId)
  // TA BORT FRAN KODRUTA
  // Makes it possible to drop (delete) the whole db. Use wisely.
  override def drop = super.drop // TA BORT FRAN KODRUTA
}




