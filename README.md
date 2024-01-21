# Projekt F70 - Wetter API

## Einführung

Dieses Projekt enthält eine einfache Implementierung einer Wetter API und stellt die daraus stammenden Daten in einer GUI dar.
Als API wurde die von openweathermap.org angebotene Schnittstelle verwendet. 
Nachfolgend wird das Projekt und die Schnittstelle kurz beschrieben.

## Open Weather API

Für die Implmentierung der Wetterdaten wurde eine Schnittstelle von openweathermap.org verwendet.
Der Anbieter bietet diverse Preismodelle mit unterschiedlichen Funktionen. Zusätzlich werden zwei gratis Modelle angeboten.
Das erste ist die Version 2.5, diese bietet grundlegende Wetterdaten wie Temepratur, Luftfeuchtigkeit, Windgeschindigkeit, etc.
Allerdings stößt man mit dieser sehr schnell an die Grenzen des Möglichen.
Das zweite Modell ist die Version 3.0, diese ist bis zu 1000 Aufrufen am Tag kostenlos, danach kosten jeder weitere Aufruf € 0,0014.
Mit dieser Version haben wir die Möglichkeit auch Wettervorhersagen und andere Parameter wie zum Beispiel Wetterwarnungen abzurufen.

Die retournierten Werte eines API Aufrufes können entweder im JSON oder XML Format erfolgen. Wir haben uns für dieses Projekt für das 
JSON Format entschieden.

Für jeden Aufruf, egal ob Version 2.5 oder 3.0 wird ein OpenWeather Account benötigt. Über diesen erhält man einen API Key (API Schlüssel) der für alle Abfragen
benötigt wird.

Zusätzlich zum API Key werden auch die Längen- und Breitengrade des abzufragenden Orts benötigt. Ein Standard Aufruf wird wie folgt durchgeführt:

>https://api.openweathermap.org/data/3.0/onecall?lat={lat}&lon={lon}&exclude={part}&appid={API key}

Die Parameter "{lat}" und "{long}" stehen dabei für die oben erwähnten Längen- und Breitengrade und der Parameter "{API key}" steht den ebenfalls Oben 
erwähnten API Schlüssel.
Je nach Anforderung können noch weitere Parameter zu jeder Abfrage mitgegeben werden. Für unseren Anwendungsfall reicht jedoch ein Standardaufruf.

Ein Beispielaufruf ist in der Datei "example.json" zu finden.


## Projektüberblick

In unsererm Projekt haben wir die oben erwähnte Version 3.0 verwendet. 
Die JSON Daten werden mittels Aufruf der API URL abgefragt, die Parameter für Längen- und Breitengrad sind dabei für einige österreichische Städte bereits vordefinert. 
Zusätzlich wird dem Benutzer die Möglichkeit geobten, selbst Koordinaten einzugeben und von diesen die Werte abzufragenden. 

Der oben erwähnte API Key ist dabei bereits fix hinterlegt. Sollte das Programm also öffentlich zugänglich gemacht werden, muss dieser selbstverständlich geändert werden.
Eine der größten Herausforderungen war dabei die aus dem JSON Aufruf resultierenden Daten in geeignete Formate umzuwandeln.

Für die Darstellung der retournierten Werte haben wir uns für ein einfaches Grid Pane entschieden. Es werden einige Standard Wetterdaten abgefragt, umgewandelt und ausgegeben.
Sollte das Programm einige Zeit unberührt gelaufen sein, kann der User mit einem "Aktualisieren" Knopf einen neuerlichen Aufruf durchführen um die aktuellsten Daten zu erhalten.
Der Zeitpunkt der letzten Aktualisieren wird dabei im rechten unteren Fensterrand angezeigt.

Mit einem weitern Knopf "Zeige Graph" kann der Benutzer die Temepraturvorhersage für die nächsten 7 Tage in einer grafischen Darstellung anzeigen. Die Grafik zeigt dabei sowohl den 
höchsten, als auch den niedrigsten Temperaturwert des jeweiligen Tages an.



