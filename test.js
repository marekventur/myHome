// Kamin-Fenster-Dunstabzugshaube-Schaltung
/*
 * if (Sensor.getByName('#kaminTemperaturFühler').getTemperatureCelsius() > 50) {

    // Kamin ist an. Ist ein Fenster offen?
    var offeneFenster = 0;
    
    var fenster = Sensor.filterByType('sensorWindowOpener').filterByRoom('esszimmer, wohnzimmer, kueche');
    
    for (var i=0; i<fenster.size(); i++) 
        if (fenster.get(i).isWindowOpen()) offeneFenster++; 

    if (offeneFenster == 0) {
        // Kein Fenster offen, also darf Dunstabzugshaube nicht angehen
         Aktor.getByName('dunstabzugshaubeNetzspannung').setActivated(false);
    }
    else
    {
        // Mindestens 1 Fenster ist offen, Dunstabzugshaube darf also an sein
        Aktor.getByName('dunstabzugshaubeNetzspannung').setActivated(false);
    }
    
   
    
}*/

Test.test(function(a) {
	println('Hello '+a);
});

/*
// Licht in der Garage soll nur nur Parameter('garageLichtDauerMinuten') Minuten nach letzten Schalterdruck an bleiben. 
if (Aktor.getByName('garageLicht').isActive()) {

    // Licht ist an. Wann wurde das letzte mal der Schalter gedrückt?
    var dauer = System.getTime() - Sensor.getByName('garageLichtSchalter').lastSignal('on', 1).getTime();

    if (dauer >= Parameter.getByName('garageLichtDauerMinuten')*60) 
        // Das Licht ist schon zu lange an. Schalte es aus.
        Aktor.getByName('garageLicht').setActive(false);
}

// Alle 5 Minuten ein Bild des Hofes abspeichern
if (System.getTime() % 15*60 == 0)
    Camera.getByName('hofKamera').shotAndArchive('Hof Fotologbuch');
*/