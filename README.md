 _____________________________________________________________________________
|                                                                             |
|                VERWIJDER DIT BESTAND NIET, MAAR PAS HET AAN!                |
|_____________________________________________________________________________|

Zoals jullie intussen weten, zullen de coaches doorheen het Vakoverschrijdend
eindproject de code meermaals evalueren. Beschrijf daarom in dit tekstbestand
de indeling van jullie Git-repository:

- Welk deel van de code bevindt zich in welke map(pen)?
- Wat is er naast de code nog meer te vinden in de repository? Waar precies?
- Wordt er gebruikgemaakt van branches? Zo ja, hoe?

Stel het opstellen van dit bestand niet uit tot het einde van een sprint, laat
staan tot het einde van het project!

De precieze indeling en opmaak van dit bestand zijn van gering belang. Zorg er
echter wel voor dat de gevraagde informatie duidelijk naar voor komt, en wijzig
de bestandsnaam (readme.txt) niet.
-------------------------------------------------------------------------------


archive : 

Oude, niet meer gebruikte klassen, afbeeldingen, code, backup



Catan:

Bevat de source files van de GUI van ons project.

	packages:
		- GUI:  Bevat de gui klasses, de verschillende Panelen gezet afhankelijk van hun positie
			- .east
			- .bot
			- .west
			- .top

		- client: bevat de REST client die verbinding maakt met de server	

		- controller: bevat de gebruikte controller

		- demos: bevat mains die bepaalde Panelen testen/tonen
			 bevat ook ons uiteindelijke programmastarter: MakeBoardDemo

		
	Test packages: bevat enkele testen	
		
			
	resources:
		Bevat afbeeldingen voor tegels, dobbelstenen en achtergrond
		properties files om constanten bij te houden

CatanBase:

	packages:

		- adapter: serializer/deserializer voor gson

		- commandos: bevat alle commandos: dingen die moeten gebeuren en makkelijk naar de server kunnen worden gestuurd en ervan worden opgevraagd
			     gegroepeerd per soort
				- .build
				- .cards
				- .trade

		- model: Bevat ons modelklassen
			- .interfaces: bevat alle interfaces
			

		-observer: Bevat onze eigen observer interfaces

		- states: bevat de verschillende staten van onze applicatie die bepalen hoe het spel zich voor een bepaalde speler op dat moment gedraagt 	   

		- utility: Bevat enums om makkelijk verschillende soorten op te vragen
			   Layout klasse die algemeen gebruik van dezelfde borders,fonts en kleuren bijhoudt
		


CatanDB:

bevat alles ivm met de databank

	packages:
		- DAO


Catanserver:

	server: bevat de RESTserver


CatanWebsite:

bevat alles ivm met onze website

	WebPages:

	Packages:
		-Ajax
		-Catan : bevat servlets voor de website
			- .database: bevat klasses voor de databank te initialiseren en te hashen




doc:

Bevat niets nuttig.



Documentatie :

bevat de documentatie met als submappen

	Scans:

		Bevat schetsen van de eerste sessie. ( UML, User interfaces, ...)


	uml:

		Bevat het Visual Paradigm project met het huidige klassendiagram.
		
		submap umlpics: bevat afbeeldingen van de umldiagrammen.


	Use cases:

		Bevat de use cases.

	
Scripts:

Bevat perl scripts om de database aan te maken en af te breken

clear_game1.pl : wist het bord op gameID=1

make_database.pl: Dropt alle tables in db en maakt ze opnieuw aan, steekt ook 8 testusers in de db

fill_database.pl: vult de databank op met dummygegevens, gescraped van stackoverflow

fillGamesForGraph: vult de Games table op voor het testen van de grafieken

