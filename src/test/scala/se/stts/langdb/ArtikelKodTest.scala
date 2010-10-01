package se.stts.langdb

import org.specs._
import org.squeryl._
import org.squeryl.adapters.H2Adapter

/**
 * Tester för artikelns kodexempel
 * 
 * @author Nikolaj Lindberg, Hanna Lindgren
 */
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


    "befolka databasen" in {
      import org.squeryl.PrimitiveTypeMode._

      try {
	inTransaction {
     	  val session = Session.currentSession
	  
	  val swe0 = Lang("xxx", "Swedish",
			  Some("LATIN"))
	  val swe = LangDb.langs.insert(swe0)
	  swe.aNames.associate(
	    AltName("svenska"))

	  val fam0 = Family("Indo-EuropeanX", 
			    Some("xxx"))
	  val fam = LangDb.families.
	  insert(fam0)
	  swe.families.associate(fam)

	  error("must generate an error to avoid commit")
	}
      }
      catch { case e => "" }
      true mustEqual true
    }


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

	val lang = from(LangDb.langs)(l => 
	  where(l.enName === "Finnish")
        select(l)).single
	//lang.aNames.foreach(println(_))
	// -> finska
	// -> suomi
	lang.aNames.map(_.name).toSet mustEqual Set("finska","suomi")
      }
    }

    "sökexempel: lista altNames för ett givet språks enName (2: id join)" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
     	val session = Session.currentSession

	val altNames = from(LangDb.langs, 
			    LangDb.altNames)(
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

      inTransaction {
     	val session = Session.currentSession

	val names = join(LangDb.langs, 
			 LangDb.altNames.leftOuter)(
	  (l, a) => 
	    select(l.enName, a.map(_.name))
	  on(l.id === a.map(_.langId)))

	// -> Swedish, Some(svenska)
	// -> English, None
	// -> Finnish, Some(finska)
	// -> Finnish, Some(suomi)
	// -> ...

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
	  (l,lc) =>
	    groupBy(l.enName)
	  compute(sum(lc.speakers))
	  orderBy(sum(lc.speakers))
	  on(l.id === lc.langId)
	)
	// -> Swedish, Some(9300000)
	// -> Dutch, None
	// -> ...
	val map = query.map( n => (n.key, n.measures)).toMap
	map("Swedish") mustEqual Some(9300000)
	map("Dutch") mustEqual None
	//println(map)
      }
    }

    "update: ändra alt name för ett språk (partial update)" in {
      import org.squeryl.PrimitiveTypeMode._
      inTransaction {
     	val session = Session.currentSession
	
	// fejkar ett stavfel
	val n1 = update(LangDb.altNames)(a =>
	  where(a.name === "svenska")
          set(a.name := "svneska"))

	// rättar ett stavfel
	val n2 = update(LangDb.altNames)(a =>
	  where(a.name === "svneska")
          set(a.name := "svenska"))

	val n3 = LangDb.altNames.where(a => a.name === "svenska").size

	n1 mustEqual 1
	n2 mustEqual 1
	n3 mustEqual 1
      }
    }

    "sök+update: ändra antal talare för ett språk, i ett visst land (partial update)" in {
      import org.squeryl.PrimitiveTypeMode._
      inTransaction {
     	val session = Session.currentSession
	
	val lc1 = from(LangDb.langs, 
		       LangDb.countries, 
		       LangDb.langsAndCountries)(
	  (l,c,lc) =>
	    where(l.enName === "Dutch" and 
		  c.enName === "Netherlands" and
		  l.id === lc.langId and
		  c.id === lc.countryId)
	  select(lc)).single

	val n1 = update(LangDb.langsAndCountries)(
	  (lc) =>
	    where(lc.id === lc1.id)
          set(lc.speakers := Some(15000000L)))

	val nSpeakers = from(LangDb.langs, 
		       LangDb.countries, 
		       LangDb.langsAndCountries)(
	  (l,c,lc) =>
	    where(l.enName === "Dutch" and 
		  c.enName === "Netherlands" and
		  l.id === lc.langId and
		  c.id === lc.countryId)
	  select(lc.speakers)).single

	nSpeakers mustEqual Some(15000000L)

	val n2 = update(LangDb.langsAndCountries)(
	  (lc) =>
	    where(lc.id === lc1.id)
          set(lc.speakers := None))

	n1 mustEqual 1
	n2 mustEqual 1

	val nSpeakers2 = from(LangDb.langs, 
		       LangDb.countries, 
		       LangDb.langsAndCountries)(
	  (l,c,lc) =>
	    where(l.enName === "Dutch" and 
		  c.enName === "Netherlands" and
		  l.id === lc.langId and
		  c.id === lc.countryId)
	  select(lc.speakers)).single

	  nSpeakers2 mustEqual None
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
	
	// Antal talare av indoeuropeiska språk
	val nSpeakers = from(langs, LangDb.langsAndCountries)(
	  (l, lac) => where(l.id === lac.langId)
	  compute(sum(lac.speakers))
	).single.measures.get
	// -> 101.860.000
	nSpeakers mustEqual 101860000
      }
    }

    "sökexempel: regexp och delete [avsnitt: Transaktioner]" in {
      import org.squeryl.PrimitiveTypeMode._

      inTransaction {
     	val session = Session.currentSession

	val alts = LangDb.altNames.where(
	  _.name.regex("ka$") )
	LangDb.altNames.delete(alts)

	val alts2 = from(LangDb.altNames)(a =>
	  where(a.name regex "ka$")
          compute(count)).single
	alts2.measures mustEqual 0
      }
    }


  }
}
