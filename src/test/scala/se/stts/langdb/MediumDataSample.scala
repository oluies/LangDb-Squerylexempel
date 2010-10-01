package se.stts.langdb

import org.squeryl._
import org.squeryl.adapters.H2Adapter

object MediumDataSample {

  /**
   * De språk som ska stoppas in i testdatabasen
   */
  val langs = Seq(
    Lang("swe", "Swedish", Some("LATIN")),
    Lang("fin", "Finnish", Some("LATIN")),
    Lang("eng", "English", Some("LATIN")),
    Lang("spa", "Spanish", Some("LATIN")),
    Lang("cat", "Catalan", Some("LATIN")),
    Lang("fra", "French", Some("LATIN")),
    Lang("rus", "Russian", Some("CYRILLIC")),
    Lang("bul", "Bulgarian", Some("CYRILLIC")),
    Lang("ara", "Arabic", Some("ARABIC")),
    Lang("zho", "Chinese", Some("CHINESE")),
    Lang("afr", "Afrikaans", Some("LATIN")),
    Lang("deu", "German", Some("LATIN")),
    Lang("zul", "Zulu", Some("LATIN")),
    Lang("xho", "Xhosa", Some("LATIN")),
    Lang("ell", "Greek", Some("GREEK")),
    Lang("nld", "Dutch", Some("LATIN")),
    Lang("sma", "Southern Sami", Some("LATIN")),
    Lang("sme", "Northern Sami", Some("LATIN")),
    Lang("smj", "Lule Sami", Some("LATIN")),
    Lang("nob", "Norwegian Bokmål", Some("LATIN")),
    Lang("nno", "Norwegian Nynorsk", Some("LATIN")),
    Lang("fiu", "Kväni", Some("LATIN")))

  /**
   * Koppling mellan språk och 'altName':
   * Språkkod (ISO) -> Lista på alternativa namn
   */
  private val altNames = Map(
    ("swe" -> Seq("svenska")),
    ("fin" -> Seq("finska", "suomi")),
    ("spa" -> Seq("español")),
    ("cat" -> Seq("català")),
    ("fra" -> Seq("français", "franska")),
    ("rus" -> Seq("русский")),
    ("bul" -> Seq("български")),
    ("ara" -> Seq("العربية")),
    ("zho" -> Seq("中文")),
    ("afr" -> Seq("Afrikaans")),
    ("zul" -> Seq("Zulu")),
    ("xho" -> Seq("Xhosa")),
    ("deu" -> Seq("Deutsch")),
    ("ell" -> Seq("Ελληνικά")),
    ("nld" -> Seq("Nederlands")),
    ("nob" -> Seq("bokmål")),
    ("nno" -> Seq("nynorsk")),
    ("sma" -> Seq("sydsamiska")),
    ("sme" -> Seq("nordsamiska", "davvisámegiella")),
    ("smj" -> Seq("lulesamiska", "julevsábme")),
    ("fiu" -> Seq("kvääni", "kvänska", "kvensk")))

  /**
   * Koppling mellan språk och språkfamilj:
   * Språkkod (ISO) -> Namn på språkfamiljer
   * 
   */
  private val langFamilies = Map(
    ("swe" -> Seq("Indo-European", "Germanic", "North Germanic")),
    ("fin" -> Seq("Uralic", "Finno-Ugric", "Finnic")),
    ("spa" -> Seq("Indo-European", "Italic", "Romance", "Italo-Western", "Gallo-Iberian", "Ibero-Romance", "West Iberian")),
    ("cat" -> Seq("Indo-European", "Romance")),
    ("eng" -> Seq("Indo-European", "Germanic", "West Germanic", "Anglo-Frisian", "English")),
    ("fra" -> Seq("Indo-European", "Italic", "Romance")),
    ("rus" -> Seq("Indo-European", "Slavic", "East Slavic")),
    ("bul" -> Seq("Indo-European", "Slavic", "South Slavic")),
    ("ara" -> Seq("Afro-Asiatic", "Semitic")),
    ("zho" -> Seq("Sino-Tibetan", "Chinese")),
    ("afr" -> Seq("Indo-European", "Germanic", "West Germanic")),
    ("zul" -> Seq("Niger-Congo", "Benue-Congo", "Bantu")),
    ("xho" -> Seq("Niger-Congo", "Benue-Congo", "Bantu")),
    ("deu" -> Seq("Indo-European", "Germanic", "West Germanic")),
    ("ell" -> Seq("Indo-European", "Greek")),
    ("nld" -> Seq("Indo-European", "Germanic", "West Germanic")),
    ("sma" -> Seq("Uralic", "Finno-Ugric", "Sami")),
    ("sme" -> Seq("Uralic", "Finno-Ugric", "Sami")),
    ("smj" -> Seq("Uralic", "Finno-Ugric", "Sami")),
    ("nob" -> Seq("Indo-European", "Germanic", "North Germanic")),
    ("nno" -> Seq("Indo-European", "Germanic", "North Germanic")),
    ("fiu" -> Seq("Uralic", "Finno-Ugric", "Finnic")))

  /**
   * Koppling mellan namn på språkfamiljer och deras ISO-koder.
   */
  private val family2iso = langFamilies.valuesIterator.toSet.flatten.map(familyName => 
    familyName match {
      case "Afro-Asiatic" => Some(familyName -> "afa")
      case "Indo-European" => Some(familyName -> "ine")
      case "Sino-Tibetan" => Some(familyName -> "sit")
      case "Uralic" => Some(familyName -> "urj")
      case "Niger-Congo" => Some(familyName -> "nic")
      case "Germanic" => Some(familyName -> "gem")
      case "Finno-Ugric" => Some(familyName -> "fiu")
      case "Romance" => Some(familyName -> "roa")
      case "Slavic" => Some(familyName -> "sla")
      case "Semitic" => Some(familyName -> "sem")
      case "Sami" => Some(familyName -> "smi")
      case _ => None
    }
  ).flatten.toMap

  /**
   * De länder som ska stoppas in i testdatabasen
   */
  val countries = Seq(
    Country("SE", "Sweden", Some("Europe")),
    Country("FI", "Finland", Some("Europe")),
    Country("RU", "Russia", Some("Europe")),
    Country("EG", "Egypt", Some("Africa")),
    Country("SA", "Saudi Arabia", Some("Asia")),
    Country("CN", "China", Some("Asia")),
    Country("HK", "Hong Kong", Some("Asia")),
    Country("SG", "Singapore", Some("Asia")),
    Country("AR", "Argentina", Some("South America")),
    Country("ES", "Spain", Some("Europe")),
    Country("FR", "France", Some("Europe")),
    Country("DE", "Germany", Some("Europe")),
    Country("CA", "Canada", Some("North America")),
    Country("US", "United States", Some("North America")),
    Country("GB", "Great Britain", Some("Europe")),
    Country("IE", "Ireland", Some("Europe")),
    Country("NZ", "Zealand", Some("Pacific")),
    Country("AU", "Australia", Some("Pacific")),
    Country("BG", "Bulgaria", Some("Europe")),
    Country("ZA", "South Africa", Some("Africa")),
    Country("GR", "Greece", Some("Europe")),
    Country("BE", "Belgium", Some("Europe")),
    Country("NL", "Netherlands", Some("Europe")),
    Country("NO", "Norway", Some("Europe"))
  )


  /**
   * Koppling mellan språk och länder:
   * Språkkod (ISO) -> Landskod (ISO)
   */
  private val langsAndCountries = Map(
    (("swe", "SE") -> LangAndCountry(Some(true), Some(9000000))),
    (("swe", "FI") -> LangAndCountry(Some(true), Some(300000))),
    (("fin", "FI") -> LangAndCountry(Some(true), Some(61000000))),
    (("ara", "SE") -> LangAndCountry(Some(false), None)),

    (("spa", "AR") -> LangAndCountry(Some(true), Some(33000000))),
    (("spa", "ES") -> LangAndCountry(Some(true), Some(28000000))),
    (("cat", "ES") -> LangAndCountry(Some(true), Some(9000000))),

    (("deu", "DE") -> LangAndCountry(Some(true), None)),
    (("deu", "BE") -> LangAndCountry(Some(true), None)),
    (("nld", "BE") -> LangAndCountry(Some(true), None)),
    (("nld", "NL") -> LangAndCountry(Some(true), None)),

    (("fra", "FR") -> LangAndCountry(Some(true), None)),
    (("fra", "CA") -> LangAndCountry(Some(true), None)),
    (("fra", "BE") -> LangAndCountry(Some(true), None)),

    (("rus", "RU") -> LangAndCountry(Some(true), None)),
    (("bul", "BG") -> LangAndCountry(Some(true), None)),

    (("ara", "EG") -> LangAndCountry(Some(true), None)),
    (("ara", "SA") -> LangAndCountry(Some(true), None)),

    (("zho", "CN") -> LangAndCountry(Some(true), None)),
    (("zho", "HK") -> LangAndCountry(Some(true), None)),
    (("zho", "SG") -> LangAndCountry(Some(true), None)),

    (("eng", "ZA") -> LangAndCountry(Some(true), None)),
    (("afr", "ZA") -> LangAndCountry(Some(true), Some(6000000))),
    (("zul", "ZA") -> LangAndCountry(Some(true), Some(96000000))),
    (("xho", "ZA") -> LangAndCountry(Some(true), Some(6900000))),

    (("ell", "GR") -> LangAndCountry(Some(true), Some(12000000))),

    (("sma", "SE") -> LangAndCountry(Some(true), Some(300))),
    (("sme", "SE") -> LangAndCountry(Some(true), Some(10000))),
    (("smj", "SE") -> LangAndCountry(Some(true), Some(1000))),

    (("sma", "NO") -> LangAndCountry(Some(true), Some(300))),
    (("sme", "NO") -> LangAndCountry(Some(true), Some(10000))),
    (("smj", "NO") -> LangAndCountry(Some(true), Some(1000))),

    (("nob", "NO") -> LangAndCountry(Some(true), Some(4080000))),
    (("nno", "NO") -> LangAndCountry(Some(true), Some(480000))),
    (("fiu", "NO") -> LangAndCountry(Some(true), Some(6500)))
  )


  /**
   * Skapar en testdatabas
   * @param dbPath Filen där databasen ska skapas
   */
  def createDb(dbPath: java.io.File): Unit = {
    
    /* Ladda drivrutiner för H2:s databasmotor */
    Class.forName("org.h2.Driver")
    
    /* Här skapas en Squeryl-session (motsvarar en JDBC connection) */
    def session = Session.create(java.sql.DriverManager.getConnection("jdbc:h2:"+ dbPath +";AUTO_SERVER=TRUE"), new H2Adapter)
    SessionFactory.concreteFactory = Some(()=> session)

    import org.squeryl.PrimitiveTypeMode._
    using(session) {
      transaction{
	
	// DELETE EXISTING DATABASE, IF IT EXISTS
        LangDb.drop

	// CREATE A NEW, EMPTY DATABASE
        LangDb.create
	
	// LANG INSERTIONS
	langs.foreach(LangDb.langs.insert(_))

	// LANG-ALT NAME ASSOCIATIONS
	for ( (isoLangOfLanguage, altNamesForLang) <- altNames ) {
	  for ( altName <- altNamesForLang ) {
	    val lang = LangDb.langs.where(l => l.iso === isoLangOfLanguage).single
	    lang.aNames.associate(AltName(altName))
	  }
	}

	// FAMILY NAME INSERTIONS
	for (familyName <- langFamilies.valuesIterator.toSet.flatten) {
	    val familyIso = family2iso.get(familyName)
	    LangDb.families.insert(Family(familyName, familyIso))
	}

	// LANG-FAMILY ASSOCIATIONS
	for ( (isoLang, families) <- langFamilies ) {
 	  val lang = LangDb.langs.where(l => l.iso === isoLang).single
	  for (familyName <- families) {
	    val family = LangDb.families.where(f => f.name === familyName).single
 	    lang.families.associate(family)
          }
	}

	// COUNTRY INSERTIONS
	countries.foreach(LangDb.countries.insert(_))

	// LANG-COUNTRY ASSOCIATIONS
	for ( ((isoLang, isoCountry), l2c0) <- langsAndCountries ) {
	  val lang = LangDb.langs.where(l => l.iso === isoLang).single
	  val country = LangDb.countries.where(c => c.iso === isoCountry).single

	  // TODO: associate => assign!
 	  val l2c = lang.countries.associate(country)

 	  update(LangDb.langsAndCountries)(item =>
 	    where(item.langId === l2c.langId and
 	  	  item.countryId === l2c.countryId)
             set(item.speakers := l2c0.speakers,
    	        item.official := l2c0.official))
	}
      }
    }
  }
}
