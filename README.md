[http://github.com/NikolajLindberg/LangDb-Squerylexempel](http://github.com/NikolajLindberg/LangDb-Squerylexempel)

# LangDb.scala 

Kodexempel till squerylartikeln i Datormagazin nummer 11
2010. LangDb.scala är ett litet exempel som illustrerar hur man
använder [Squeryl](http://squeryl.org).

Instruktioner för att testa exempeldatabasen finns här:

[http://github.com/NikolajLindberg/LangDb-Squerylexempel/wiki](http://github.com/NikolajLindberg/LangDb-Squerylexempel/wiki)

## Snabb sammanfattning, följ stegen nedan:


* Se till att du har en modern [JVM](http://java.com) installerad 
* Installera [SBT](http://code.google.com/p/simple-build-tool/)
* Installera [git](http://git-scm.com/) (om du vill)
    * Checka ut testprojektet:
          git clone git://github.com/NikolajLindberg/LangDb-Squerylexempel.git
* _Eller_ ladda ner koden här XXX, och packa upp den 
* _Eller_ hämta koden från [Datormagazins filarkiv](http://www.datormagazin.se/filer/), och packa upp den
* Ställ dig i katalogen LangDb-Squerylexempel
* Starta `sbt`
* Skriv `update` (nu hämtar sbt de externa bibliotek som krävs)
* Skriv `test` (exempelprojektet kompileras och testerna körs)

Om du lyckts med stegen ovan, kan du ge med fördel börja titta på
kodexemplen.

## Kodexempel

Själva exempeldatabasen finns i
`LangDb-Squerylexempel/src/main/scala/se/stts/langdb/LangDb.scala`. Här
kan du se alla detaljer som inte fanns plats för i artikeln.

## Kodokumentation 

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


## Bra länkar

Missa inte samlingen med [länkar](http://github.com/NikolajLindberg/LangDb-Squerylexempel/wiki/L%C3%A4nkar) på wikin.
