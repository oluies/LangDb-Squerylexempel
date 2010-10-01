Denna fil finns här:

[http://github.com/NikolajLindberg/LangDb-Squerylexempel](http://github.com/NikolajLindberg/LangDb-Squerylexempel)

# LangDb.scala 

Kodexempel till squerylartikeln i Datormagazin nummer 11,
2010. LangDb.scala är ett litet exempel som illustrerar hur man
använder [Squeryl](http://squeryl.org).

## Instruktioner för att testa exempeldatabasen


* Se till att du har en modern [JVM](http://java.com) installerad
 
* Installera [SBT](http://code.google.com/p/simple-build-tool/). Se
  "Mer om SBT" nedan.

* Installera [git](http://git-scm.com/) (om du vill)

    * Checka ut testprojektet: `git clone
          git://github.com/NikolajLindberg/LangDb-Squerylexempel.git`

* _Eller_ ladda ner koden här XXX, och packa upp den
 
* _Eller_ hämta koden från [Datormagazins
  filarkiv](http://www.datormagazin.se/filer/), och packa upp den
* Ställ dig i katalogen `LangDb-Squerylexempel`
* Starta `sbt`
* Skriv `update` (nu hämtar sbt de externa bibliotek som krävs)
* Skriv `test` (exempelprojektet kompileras och testerna körs)

Om du lyckas med stegen ovan, kan du med fördel börja titta på
kodexemplen.

## Kodexempel

Själva exempeldatabasen finns i
`LangDb-Squerylexempel/src/main/scala/se/stts/langdb/LangDb.scala`. Här
kan du se alla detaljer som inte fanns plats för i artikeln.

## Dokumentation 

Koden är rikligt dokumenterad, och finns genererad med hjälp av
`scaladoc`. Öppna filen XXX i din html-läsare och njut.

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

SBT, "Simple Build Tool", är en ett lättanvänt kompileringsverktyg
skrivet i och för Scala. Det är enkelt att använda, men annars det är
inget "simpelt" över SBT. (`Ant` kan slänga sig i väggen.)

Engelska installationsanvisningar finns
[här](http://code.google.com/p/simple-build-tool/wiki/Setup). Det
behövs knappast någon översättning. Tricket är att skapa en exekverbar
batch/skalfil som kör SBT.

På Linux: 

* Lägg SBT-jarfilen i `~/bin` tillsammans med skriptet `sbt`, som skall
   ha följande innehåll:

  `java -Xmx512M -jar `dirname $0`/sbt-launch.jar "$@"`
* Gör skriptet exekverbart: `chmod u+x ~/bin/sbt`

På Windows:

* Skapa en batchfil, `sbt.bat`, och se till att den finns i din
  sökväg. Lägg sbt-jarfilen i samma katalog som batchfilen.
* Plutta in följande i filen:

  `set SCRIPT_DIR=%~dp0`

  `java -Xmx512M -jar "%SCRIPT_DIR%sbt-launch.jar" %*`


### Exempelkommandon

SBT är ett fint verktyg, som kan göra en massa saker. Här följer några
grundläggande kommandon.

* `update` --- läser och kompilerar projektfilen, och laddar ner de
  externa bibliotek som eventuellt behövs.
* `compile` --- kompilerar de Scala- och javafiler som ligger under
  biblioteket `src`. Kompilerar endast de filer som påverkas av de
  senaste ändringarna.  
* `~ compile` --- samma som ovan, men körs automatiskt så fort någon källkodsfil
  ändrats.
* `test` --- kompilerar källkoden och kör alla tester
* `~ test` --- samma som ovan, men körs automatiskt så fort någon
  källkodsfil ändrats
* `test-quick` --- kör endast de tester som misslyckades i förra
  körningen
* `doc` --- genererar scaladoc
* `clean` --- rensar bort kompilerad kod

### Konfiguration

Det finns två konfigurationsfiler att hålla redan på. Den ena är
`project/build.properties` och den andra är projektdefinitionen. I
detta exempel är det `project/build/SquerylDemoProject.scala`.

## Bra länkar

Missa inte samlingen med
[länkar](http://github.com/NikolajLindberg/LangDb-Squerylexempel/wiki/L%C3%A4nkar)
på wikin.

