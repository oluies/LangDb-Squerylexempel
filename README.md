# Kodexempel till squerylartikeln i Datormagazin 11/2010

Här finns information för att installera och köra koden som hör till
artikeln om databas-API:et Squeryl, som presenteras i Datormagazin
11/2010.

Instruktionerna i denna fil finns att läsa här:
[http://github.com/NikolajLindberg/LangDb-Squerylexempel](http://github.com/NikolajLindberg/LangDb-Squerylexempel)


# LangDb.scala 

LangDb.scala är ett litet exempel som illustrerar hur man
använder [Squeryl](http://squeryl.org).


# Instruktioner för att testa exempeldatabasen


* Se till att du har en modern [JVM](http://java.com) installerad
 
* Installera [SBT](http://code.google.com/p/simple-build-tool/). Se "Mer om SBT" nedan.

* Ladda ner projektet:
  * Installera [git](http://git-scm.com/) (om du vill)

      * Checka ut testprojektet: 
          ```git clone git://github.com/NikolajLindberg/LangDb-Squerylexempel.git```

  * _Eller_ ladda ner koden här XXX, och packa upp den
 
  * _Eller_ hämta koden från [Datormagazins filarkiv](http://www.datormagazin.se/filer/), och packa upp den

* Ställ dig i katalogen `LangDb-Squerylexempel`
* Starta `sbt`
* Skriv `update` (nu hämtar SBT alla externa bibliotek som krävs,
  inklusive Squeryl)
* Skriv `test` (exempelprojektet kompileras och testerna körs)

Om du lyckas med stegen ovan, kan du med fördel börja titta på
kodexemplen.


# Kodexempel

Själva exempeldatabasen finns i
`LangDb-Squerylexempel/src/main/scala/se/stts/langdb/LangDb.scala`. Här
kan du se alla detaljer som inte fanns plats för i artikeln.


# Testexempel

Till koden finns en uppsättning tester, som körs med hjälp av
[Specs](http://code.google.com/p/specs/). Specs är ett bibliotek för
enhetstestning (besläktat med exempelvis JUnit). 

Testkoden är intressant, dels för att den visar hur Specs används,
dels för att den visar hur testdatabasen används. Här finns exempel på
hur man kopplar upp sig till en databas och anropar den via Squeryl.

Källkoden till testerna ligger här:
`LangDb-Squerylexempel/src/test/scala/se/stts/langdb/`


# Dokumentation 

Koden är dokumenterad, och går bra att läsa direkt i källkoden. Med
hjälp av `scaladoc` kan man generera en HTML-version, om så
önskas. Starta SBT och skriv
`doc`.

Öppna filen
`LangDb-Squerylexempel/target/scala_2.8.0/doc/main/api/index.html` in
en webbläsare.


# Exempel på inläsning av data

Det finns en särskild fil som sköter inläsningen av lite data,
`LangDb-Squerylexempel/src/test/scala/se/stts/langdb/MediumDataSample.scala`
(notera att den ligger i test-katalogen).


# H2-databasen och databasfilerna

Exempeldatabasen körs i databasmotorn
[H2](http://www.h2database.com/html/main.html). Den laddas automatiskt
ner av SBT.

När `test` körs i SBT, skapas två databasfiler i tmp-katalogen:

* MediumDataSample.h2.db
* ArtikelKodTest.h2.db

Om du manullet installerar H2, får du tillgång till ett grafiskt
gränssnitt som körs via en webbläsare. Du kan koppla upp dig mot
databasfilerna ovan via detta gränssnitt, och söka i dem med vanlig
SQL.


# Mer om SBT

SBT, "Simple Build Tool", är en ett lättanvänt kompileringsverktyg
skrivet i och för Scala.

Engelska installationsanvisningar finns
[här](http://code.google.com/p/simple-build-tool/wiki/Setup). Installationen
går ut på att skapa en exekverbar batch/skalfil som kör SBT.

Börja med att ladda ner `sbt-launch-0.7.4.jar`, och gör sedan så här:

## På Linux: 

* Lägg SBT-jarfilen i `~/bin` tillsammans med skriptet `sbt`, som skall
   ha följande innehåll:

    ```java -Xmx512M -jar `dirname $0`/sbt-launch-0.7.4.jar "$@"```

* Gör skriptet exekverbart: `chmod u+x ~/bin/sbt`

## På Windows:

* Skapa en batchfil, `sbt.bat`, och se till att den finns i din
  sökväg. Lägg SBT-jarfilen i samma katalog som batchfilen.
* Skriv in följande i filen:

    ```set SCRIPT_DIR=%~dp0```

    ```java -Xmx512M -jar "%SCRIPT_DIR%sbt-launch-0.7.4.jar" %*```


## Exempelkommandon

SBT är ett avancerat verktyg, som kan göra en massa saker. Här följer
några grundläggande kommandon.

* `update` --- läser och kompilerar projektfilen, och laddar ner de
  externa bibliotek som eventuellt behövs.
* `compile` --- kompilerar de scala- och javafiler som ligger under
  biblioteket `src`. Kompilerar endast de filer som påverkas av de
  senaste ändringarna.  
* `~ compile` --- samma som ovan, men körs automatiskt så fort någon källkodsfil
  ändrats.
* `test` --- kompilerar källkoden och kör alla tester
* `~ test` --- samma som ovan, men körs automatiskt så fort någon
  källkodsfil ändrats
* `test-quick` --- kör endast de tester som misslyckades i förra
  körningen

* `doc` --- genererar scaladoc. `doc-all` genererar scaladoc även för
  testklasser.
* `clean` --- rensar bort kompilerad kod


## Konfiguration

Det finns två konfigurationsfiler att hålla redan på. Den ena är
`LangDb-Squerylexempel/project/build.properties` och den andra är
projektdefinitionen. I detta exempel finns projekdefinitionen i
`LangDb-Squerylexempel/project/build/SquerylDemoProject.scala`.


## Programversioner

För detta exempelprojekt används följande programversioner (se även
konfigurationsfilerna ovan):

* [[Squeryl|http://squeryl.org]] 0.9.4-RC2
* Databas [[H2|http://www.h2database.com]] 1.2.141
* [[Scala|http://scala-lang.org]] 2.8 final
* [[Java|http://java.com]] 1.6.0
* SBT 0.7.4


# Länkar

En samling länkar, relevanta för exempelprojektet.

* Den viktigaste platsen för Scala:
  [scala-lang.org](http://scala-lang.org)
  * En av flera av Scalas diskussionsgrupper heter [Scala
    user](http://scala-programming-language.1934581.n4.nabble.com/Scala-User-f1934582.html)
* [Planet Scala](http://www.planetscala.com/) samlar scalabloggar
* Squeryl bor här: [squeryl.org](http://squeryl.org)
    * Squeryls diskussionsgrupp:
      [groups.google.com/group/squeryl](http://groups.google.com/group/squeryl?pli=1)
* [Daily Scala](http://daily-scala.blogspot.com/) bjuder på kodexempel
  med förklaringar
* [Specs](http://code.google.com/p/specs/) är ett scalabibliotek för
  programtestning
* Versionshantering med [Git](http://git-scm.com/)
* Databasen [H2](http://http://h2database.com), som används i
  exempelprojektet


Diverse verktyg

* Scalaplugin för Emacs:
  [Ensime](http://github.com/aemoncannon/ensime)
* Scalaplugin för NetBeans:
  [ErlyBird](http://sourceforge.net/projects/erlybird/files/)
* Scalaplugin för Eclipse: [scala-ide.org](http://www.scala-ide.org/)
* Scalaplugin för
  [IntelliJ IDEA](http://confluence.jetbrains.net/display/SCA/Getting+Started+with+IntelliJ+IDEA+Scala+Plugin)
* [SBT](http://code.google.com/p/simple-build-tool/) är populärt bland
   scalaprogrammerare, eftersom det, trots namnet, är så avancerat ---
   men ändå lättanvänt


# Läsning


## Scalaböcker

* Beginning Scala (David Pollak)
* Programming in Scala (Odersky, Spoon och Venners)
* [Programming Scala](http://programming-scala.labs.oreilly.com/)
  (Wampler, Payne) OBS! Fritt tillgänglig nätversion


## Text om relationsdatabaser

Fritt tillgängligt kapitel ur boken Java Database Best Practices:
Chapter 2: [Relational Data
Architecture](http://oreilly.com/catalog/javadtabp/chapter/index.html). (En
bra översikt och förklaring av relationsdatabaser.)


