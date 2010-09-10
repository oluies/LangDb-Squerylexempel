package se.stts.langdb

import org.specs._
import org.squeryl._
import org.squeryl.adapters.H2Adapter

class ArtikelKodTest extends Specification {

  System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
  System.setErr(new java.io.PrintStream(System.err, true, "UTF-8"));

  
  "A LangDb instance" should {

    "be initialized without errors" in {
      import org.squeryl.PrimitiveTypeMode._
      val dbPath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "ArtikelKodTest"
      Class.forName("org.h2.Driver")
    
      def session = Session.create(java.sql.DriverManager.getConnection("jdbc:h2:"+ dbPath +";AUTO_SERVER=TRUE"), new H2Adapter)
      SessionFactory.concreteFactory = Some(()=> session)
      transaction {
	LangDb.drop
	LangDb.create
	MediumDataSample.createDb(new java.io.File(dbPath))
      }
      true mustEqual true
    }


    "with example search: list all language names (enName)" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
     	val session = Session.currentSession

	val names = from(LangDb.langs)(l => select(l.enName))
	// -> Swedish
	// -> English
	// -> Finnish
	// -> ...
	names.toSet must contain("Swedish")
	names.toSet must contain("English")
	names.toSet must contain("Finnish")
      }
    }

    "with example search: count number of languages" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
     	val session = Session.currentSession

	val nLangs = from(LangDb.langs)(l => 
	  compute(count)
        ).single.measures
	// -> 22
	nLangs mustEqual 22
      }
    }

    "with example search: list altNames for a given Lang.enName (1: 'shortcuts')" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
     	val session = Session.currentSession

	val lang = from(LangDb.langs)(
	  l => 
    	    where(l.enName === "Finnish")
	    select(l)).single
	// -> finska
	// -> suomi
	lang.altNames.map(_.name).toSet mustEqual Set("finska","suomi")
      }
    }

    "with example search: list altNames for a given Lang.enName (2: id join)" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
     	val session = Session.currentSession

	val altNames = from(LangDb.langs, LangDb.altNames)(
	  (l,a) => 
    	    where(l.enName === "Finnish" and
		l.id === a.langId)
	    select(a.name))
	// -> finska
	// -> suomi
	altNames.toSet mustEqual Set("finska","suomi")
      }
    }

    "with example search: list altNames for a given Lang.enName (3: left join)" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
     	val session = Session.currentSession

	val altNames = join(LangDb.langs, LangDb.altNames.leftOuter)(
	  (l,a) => 
    	    where(l.enName === "Finnish")
	    select(a.map(_.name))
	on(l.id === a.map(_.langId)))
	// -> finska
	// -> suomi
	altNames.flatten.toSet mustEqual Set("finska","suomi")
      }
    }

    "with example search: list enName, altName pairs" in {
      import org.squeryl.PrimitiveTypeMode._

//       SELECT LANG.ENNAME, ALT_NAMES.NAME
//         FROM LANG 
//         LEFT JOIN ALT_NAMES ON (ALT_NAMES.LANGID = LANG.ID)

      inTransaction {
     	val session = Session.currentSession

	val names = join(LangDb.langs, LangDb.altNames.leftOuter)(
	  (l, a) => 
	    select((l.enName, a.map(_.name)) )
    	  on(l.id === a.map(_.langId)))

	// -> Swedish => svenska
	// -> English => n/a
	// -> Finnish => finska
	// -> Finnish => suomi
	//...
	val map = names.filter( n => n._2 != None).map( n => (n._1, n._2.get)).toMap
	map("Swedish") mustEqual "svenska"
	map must notContain("English")
      }
    }

    "with example search: compute total number of speakers per language" in {
      import org.squeryl.PrimitiveTypeMode._
      inTransaction {
     	val session = Session.currentSession

	val query = join(LangDb.langs, 
			 LangDb.langsAndCountries) (
	  (lang,l2c) =>
	    groupBy(lang.enName)
	  compute(sum(l2c.speakers))
	  orderBy(sum(l2c.speakers))
	  on(lang.id === l2c.langId)
	)
	// -> Swedish => Some(9300000)
	// -> Dutch => None
	val map = query.map( n => (n.key, n.measures)).toMap
	map("Swedish") mustEqual Some(9300000)
	map("Dutch") mustEqual None
      }
    }

    "with example search: [TODO REGEXP SEARCH]" in {
      import org.squeryl.PrimitiveTypeMode._
      inTransaction {
     	val session = Session.currentSession

      }
    }


  }
}
