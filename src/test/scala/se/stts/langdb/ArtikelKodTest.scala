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
      }
      true mustEqual true
    }


    "work with sample test insertions" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
     	val session = Session.currentSession

	val swe0 = Lang("swe", "Swedish", Some("LATIN"))
	val swe = LangDb.langs.insert(swe0)
	swe.altNames.associate(AltName("svenska"))
	
	val eng0 = Lang("eng", "English", Some("LATIN"))
	val eng = LangDb.langs.insert(eng0)
	
	val fin0 = Lang("fin", "Finnish", Some("LATIN"))
	val fin = LangDb.langs.insert(fin0)
	fin.altNames.associate(AltName("finska"))
	fin.altNames.associate(AltName("suomi"))
      }      
      true mustEqual true
    }

    "work with sample test queries (1)" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
     	val session = Session.currentSession

	val names = from(LangDb.langs)(l => select(l.enName))
	names.foreach(println(_))
	names.toSet mustEqual Set("Swedish","English","Finnish")
      }
    }

    "work with sample test queries (2)" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
     	val session = Session.currentSession

	val nLangs = from(LangDb.langs)(l => 
	  compute(count)
        ).single.measures
	println(nLangs)
	nLangs mustEqual 3
      }
    }

    "work with sample test queries (3)" in {
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
	names.foreach( n => println(n._1 + " => " + n._2.getOrElse("null")))

	val map = names.filter( n => n._2 != None).map( n => (n._1, n._2.get)).toMap
	map("Swedish") mustEqual "svenska"
	map must notContain("English")
      }
    }

    "find altNames for given Lang.enName (1: 'shortcuts')" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
     	val session = Session.currentSession

	val lang = from(LangDb.langs)(
	  l => 
    	    where(l.enName === "Finnish")
	    select(l)).single
	lang.altNames.foreach(a => println(a.name))
	lang.altNames.map(_.name).toSet mustEqual Set("finska","suomi")
      }
    }

    "find altNames for given Lang.enName (2: id join)" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
     	val session = Session.currentSession

	val altNames = from(LangDb.langs, LangDb.altNames)(
	  (l,a) => 
    	    where(l.enName === "Finnish" and
		l.id === a.langId)
	    select(a.name))
	altNames.foreach(println(_))
	altNames.toSet mustEqual Set("finska","suomi")
      }
    }

    "find altNames for given Lang.enName (3: left join)" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
     	val session = Session.currentSession

	val altNames = join(LangDb.langs, LangDb.altNames.leftOuter)(
	  (l,a) => 
    	    where(l.enName === "Finnish")
	    select(a.map(_.name))
	on(l.id === a.map(_.langId)))
	altNames.flatten.foreach(println(_))
	altNames.flatten.toSet mustEqual Set("finska","suomi")
      }
    }

  }
}
