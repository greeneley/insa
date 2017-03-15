// mon premier programme
// et aussi mon petit manuel de fonctions

const int MON_PIN_13 = 13;
boolean   boolVrai = true;
boolean   boolFaux = false;

void setup()
{
 // on doit faire toutes les initialisations de variables
 Serial.begin(9600);     // On initialise le moniteur d'informations
 pinMode(MON_PIN_13, OUTPUT);    // Le pin 13 envoie du courant, est initialisée
 digitalWrite(MON_PIN_13, HIGH); // Le pin 13 est alimenté par l'arduino
 
 if(MON_PIN_13 == 12)
 {
   Serial.println("On est donc dans la condition");
 }
 else
 {
   Serial.println("Ah non ca ne marche pas");
 }
 
 int i;
 for(i=0; i<20; i++)
 {
   Serial.println(i);
 }
 
}

void loop()
{
  // le corps de notre programme qui est cense boucler
  Serial.println("HAHA"); // On affiche HAHA sur la sortie
}
