package se.stts.langdb

import org.specs._
import org.squeryl._
import org.squeryl.adapters.H2Adapter

class ArtikelKodTest extends Specification {

  System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
  System.setErr(new java.io.PrintStream(System.err, true, "UTF-8"));

  
  "A LangDb" should {

    "ska instansieras utan fel" in {
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


    // "skapa klasser" in {
    //   case class Lang_ArtikelKodTest(iso: String, 
    //   				     enName: String, 
    //   				     cBlock: Option[String]) 
    //        extends AutoId
    //   case class AltName_ArtikelKodTest(name: String,
    //   					langId: Int = 0) 
    //        extends AutoId
    // }


    "sökexempel: lista alla språknamn (enName)" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
     	val session = Session.currentSession

	val names = from(LangDb.langs)(l => select(l.enName))
	//names.foreach(println)
	// -> Swedish
	// -> English
	// -> Finnish
	// -> ...
	names.toSet must contain("Swedish")
	names.toSet must contain("English")
	names.toSet must contain("Finnish")
      }
    }


    "sökexempel: foreach syntax" in {
      val numbers = List(1,2,3)
      //numbers.foreach(number => print(number))
      true mustEqual true
    }

    "sökexempel: summera antalet språk" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
     	val session = Session.currentSession

	val nLangs = from(LangDb.langs)(l => 
	  compute(count)
        ).single.measures

	//println(nLangs)
	// -> 22
	nLangs mustEqual 22
      }
    }

    "sökexempel: lista altNames för ett givet språks enName (1: 'genväg')" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
     	val session = Session.currentSession

	val lang = from(LangDb.langs)(
	  l => 
    	    where(l.enName === "Finnish")
	    select(l)).single
	// -> finska
	// -> suomi
	lang.aNames.map(_.name).toSet mustEqual Set("finska","suomi")
      }
    }

    "sökexempel: lista altNames för ett givet språks enName (2: id join)" in {
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

    "sökexempel: lista altNames för ett givet språks enName (3: left join)" in {
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

    "sökexempel: lista par av enName - altName" in {
      import org.squeryl.PrimitiveTypeMode._

//       SELECT LANG.ENNAME, ALT_NAMES.NAME
//         FROM LANG 
//         LEFT JOIN ALT_NAMES ON (ALT_NAMES.LANGID = LANG.ID)

      inTransaction {
     	val session = Session.currentSession

	val names = join(LangDb.langs, LangDb.altNames.leftOuter)(
	  (l, a) => 
	    select(l.enName, a.map(_.name))
    	  on(l.id === a.map(_.langId)))

	// -> Swedish, Some(svenska)
	// -> English, None
	// -> Finnish, Some(finska)
	// -> Finnish, Some(suomi)
	// -> ...

	//val map = names.filter( n => n._2 != None).map( n => (n._1, n._2.get)).toMap
	val map = names.map(n => (n._1, n._2)).toMap
	map("Swedish") mustEqual Some("svenska")
	map("English") mustEqual None
      }
    }

    "sökexempel: beräkna totalt antal talare per språk" in {
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

    "sökexempel: sökning i ett sökresultat [avsnitt: Queryklassen/Kodruta 2]" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
     	val session = Session.currentSession

	// Metod för att plocka ut indoeuropeiska språk
	def indoLangs(langs: Queryable[Lang]): Query[Lang] = {
	  join(langs, LangDb.families,
	       LangDb.langsAndFamilies.leftOuter)(
	    (l, f, laf) => where(f.name === "Indo-European")
	    select(l)
	    on(l.id === laf.map(_.langId), 
	       f.id === laf.map(_.familyId)))
	}
	
	// Alla indoeuropeiska språk
	val langs = indoLangs(LangDb.langs)
	// Antal talare indoeuropeiska språk
	val nSpeakers = from(langs, LangDb.langsAndCountries)(
	    (l, lac) => where(l.id === lac.langId)
	    compute(sum(lac.speakers))
	  ).single.measures.get
	//println(nSpeakers)
	nSpeakers mustEqual 101860000
      }
    }

    "sökexempel: regexp och delete [avsnitt: Transaktioner]" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
     	val session = Session.currentSession

	val alts = 
	  LangDb.altNames.where(
	    _.name.regex("ka$") )
	LangDb.altNames.delete(alts)
	true mustEqual true
      }
    }


  }
}
