Denna fil finns här:

[http://github.com/NikolajLindberg/LangDb-Squerylexempel](http://github.com/NikolajLindberg/LangDb-Squerylexempel)

# LangDb.scala 

Kodexempel till squerylartikeln i Datormagazin nummer 11
2010. LangDb.scala är ett litet exempel som illustrerar hur man
använder [Squeryl](http://squeryl.org).

Instruktioner för att testa exempeldatabasen finns här:

[http://github.com/NikolajLindberg/LangDb-Squerylexempel/wiki](http://github.com/NikolajLindberg/LangDb-Squerylexempel/wiki)

## Snabb sammanfattning, följ stegen nedan:


* Se till att du har en modern [JVM](http://java.com) installerad
 
* Installera [SBT](http://code.google.com/p/simple-build-tool/). Se
  "Mer om SBT" nedan.

* Installera [git](http://git-scm.com/) (om du vill)

    * Checka ut testprojektet: `git clone
          git://github.com/NikolajLindberg/LangDb-Squerylexempel.git`

* _Eller_ ladda ner koden här XXX, och packa upp den
 
* _Eller_ hämta koden från [Datormagazins
  filarkiv](http://www.datormagazin.se/filer/), och packa upp den
* Ställ dig i katalogen LangDb-Squerylexempel
* Starta `sbt`
* Skriv `update` (nu hämtar sbt de externa bibliotek som krävs)
* Skriv `test` (exempelprojektet kompileras och testerna körs)

Om du lyckas med stegen ovan, kan du ge med fördel börja titta på
kodexemplen.

## Kodexempel

Själva exempeldatabasen finns i
`LangDb-Squerylexempel/src/main/scala/se/stts/langdb/LangDb.scala`. Här
kan du se alla detaljer som inte fanns plats för i artikeln.

## Koddokumentation 

Koden är rikligt dokumenterad, och finns genererad med hjälp av
`scaladoc`.

## Exempel på inläsning av data

Det finns en särskilld fil som sköter inläsningen av lite data,
`src/test/scala/se/stts/langdb/MediumDataSample.scala` (notera att den
ligger i "test").

## Testexempel

Till koden finns en uppsättning tester, som körs med hjälp av
[Specs](http://code.google.com/p/specs/). Specs är ett bibliotek för
enhetstestning (besläktat med exempelvis JUnit).

Testkoden är intressant, dels för att den visar hur Specs används,
dels för att den visar hur testdatabasen används.


## Mer om SBT

SBT är en ett lättanvänt kompileringsverktyg, "Simple Build Tool",
skrivet i och för Scala. Det är enkelt att använda, men annars det är
inget "simpelt" över SBT.

Engelska installationsanvisningar finns
[här](http://code.google.com/p/simple-build-tool/wiki/Setup). Det
behövs knappast någon översättning. Ticket är att skapa en exekverbar
batch/skalfil som kör SBT.

På Linux: 

* Lägg SBT-jarfilen i ~/bin tillsammans med skriptet `sbt`, som skall
   ha följande innehåll:
  `java -Xmx512M -jar `dirname $0`/sbt-launch.jar "$@"`
* Gör skriptet exekverbart: `chmod u+x ~/bin/sbt`

På Windows:

* Skapa en batchfil, `sbt.bat`, och se till att den finns i Windows
  sökväg. Lägg sbt-jarfilen i samma katalog som batchfilen.
* Plutta in följande i filen:

  `set SCRIPT_DIR=%~dp0

  java -Xmx512M -jar "%SCRIPT_DIR%sbt-launch.jar" %*`


## Bra länkar

Missa inte samlingen med
[länkar](http://github.com/NikolajLindberg/LangDb-Squerylexempel/wiki/L%C3%A4nkar)
på wikin.
