package se.stts.langdb

import org.specs._
import org.squeryl._
import org.squeryl.adapters.H2Adapter

class MediumDataSampleTest extends Specification {

  System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
  System.setErr(new java.io.PrintStream(System.err, true, "UTF-8"));

  
  "A MediumDataSample database instance" should {

    "be initialized without errors" in {
      val dbPath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "MediumDataSample"
      MediumDataSample.createDb(new java.io.File(dbPath))
      true mustEqual true
    }

    "have the correct number of languages" in {
      import org.squeryl.PrimitiveTypeMode._
      
      inTransaction {
    	val session = Session.currentSession
    	val lCount = from (LangDb.langs)(l => 
    	  compute(count)
    	).single.measures
    	lCount mustEqual MediumDataSample.langs.size
    	lCount mustEqual 22
    	lCount must beGreaterThan(10L)
      }
    }

    "have the correct number of countries" in {
      import org.squeryl.PrimitiveTypeMode._
      
      inTransaction {
    	val session = Session.currentSession
    	val cCount = from (LangDb.countries)(c => 
    	  compute(count)
    	).single.measures
    	cCount mustEqual MediumDataSample.countries.size
    	cCount mustEqual 24
      }
    }

    "be able to perform queries on query results" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
    	val session = Session.currentSession
    	val allLangs = from(LangDb.langs)(l => select(l))
    	val lang7 = from(allLangs)(l => where(l.id === 7) select(l)).single
    	lang7.id mustEqual 7
      }
    }

    "be able to look up lang.enName => lang.altNames" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
    	val session = Session.currentSession
    	val langs = from(LangDb.langs)(l => 
    	  select(l) 
    	  orderBy(l.enName)).map(lang => 
    	     (lang.enName -> lang.altNames.map(_.name)))
    	langs.size mustEqual(MediumDataSample.langs.size)
      }
    }

    "return None/null as alt names for 'English'" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
    	val session = Session.currentSession
    	val lang = from(LangDb.langs)(l => 
    	  where(l.enName === "English")
    	  select(l) 
    	  orderBy(l.enName)).single

    	lang.altNames.size mustEqual 0
      }
    }

    "return 'franska' and 'français' as alt names for 'French'" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
    	val session = Session.currentSession
    	val altNames = from(LangDb.langs)(l => 
    	  where(l.enName === "French")
    	  select(l) 
    	  orderBy(l.enName)).
    	single.altNames.map(_.name)

    	altNames.size mustEqual 2
    	altNames mustContain "français"
    	altNames mustContain "franska"
      }
    }


    "be able to sum up the number of alt names for each language (with join)" in {
      import org.squeryl.PrimitiveTypeMode._

      // SELECT LANG.ENNAME, COUNT(ALT_LANG_NAMES.NAME) 
      //  FROM LANG
      //  LEFT JOIN ALT_LANG_NAMES ON(LANG.ID = ALT_LANG_NAMES.LANGID)
      //  GROUP BY LANG.ENNAME
      
       inTransaction {
    	 val session = Session.currentSession

    	 val query = join(LangDb.langs, 
    			  LangDb.altNames.leftOuter) (
    	   (lang, altOption) => 
    	   groupBy(lang.enName)
    	   compute(count(altOption.map(_.id)))
    	   orderBy(count(altOption.map(_.id)))
    	     on(lang.id === altOption.map(_.langId)))

    	 val altNameCount = query.map(q => (q.key -> q.measures)).toMap
    	 altNameCount("English") mustEqual 0
    	 altNameCount("French") mustEqual 2
    	 altNameCount("Kväni") mustEqual 3
    	 altNameCount("Russian") mustEqual 1
	 
       }
    }

     "be able to sum up the total number of speakers per language (stored in db as speakers per lang+country)" in {
       import org.squeryl.PrimitiveTypeMode._

       
       // Select
       //   Lang.enName as LANG_NAME,
       //   sum(LangAndCountry.speakers) as TOTAL_N_SPEAKERS
       // From
       //  LangAndCountry
       //  inner join Lang on (Lang.id = LangAndCountry.langId)
       // Group By
       //   Lang.enName
       

        inTransaction {
     	 val session = Session.currentSession

    	  // returnerar totalt antal talare per språknamn
    	  val query = join(LangDb.langs, LangDb.langsAndCountries) (
    	    (lang,l2c) =>
    	      groupBy(lang.enName)
    	    compute(sum(l2c.speakers))
    	    orderBy(sum(l2c.speakers))
    	    on(lang.id === l2c.langId)
    	  )

    	  // returnerar totalt antal talare per språk-id
    	  // val query2 = from(LangDb.langsAndCountries) (
     	  //   (l2c) =>
     	  //     groupBy(l2c.langId)
     	  //   compute(sum(l2c.speakers))
     	  //   orderBy(sum(l2c.speakers))
     	  // )

    	  val count = query.filter(q => q.measures != None).
    	  map(q => (q.key -> q.measures.get)).toMap
    	  count("Swedish") mustEqual 9300000
    	  count.getOrElse("Dutch",None) mustEqual None
        }
     }

    "be able to nuke some of its citizens" in {
      import org.squeryl.PrimitiveTypeMode._

      try{
	inTransaction{
          
          val kaLangs = LangDb.altNames.where(_.name.regex("ka$"))
          kaLangs.size must be greaterThan 0
          
          LangDb.altNames.delete(kaLangs)
	  
          val kaLangs2 = LangDb.altNames.where(_.name.regex("ka$"))//from(LangDb.altNames)(an => where( an.name.regex("ka$")) select(an))
          kaLangs2.size mustEqual 0
          // TRYING A ROLLBACK HERE TO AVOID COMMITTING THE CHANGES
          error("DARN")
	}
      } catch {case _ =>}
    } 



  }
}
