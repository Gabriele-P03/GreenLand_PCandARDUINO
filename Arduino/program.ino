/**
 * Comment written in italian ===> it will delete later. It's just a note about new features
 * RISCALDAMENTO E RAFFREDDAMENTO 
 */

#include "Seed.h"
#include <SoftwareSerial.h>
#include <DHT.h>

#define DHTTYPE DHT11   //DHT Temperature and Humidity Sensor type
#define DHTPIN 8        //DHT Temperature and Humidity Sensor on 8th pin 

#define RXBLT 2         //HC-05 module TX receive data from broadcast and TRANSMIT to Arduino
#define TXBLT 3         //HC-05 module RECEIVE data from Arduino and transmit on broadcast

#define FAN1 12         //First Fan's relay pin, common with the resistence. INTO
#define FAN2 13         //Second Fan's relay. OUTO

#define PHRES A5        //AnalogInput, it reads the Voltage passed by the photoresistor from
#define PHRESIN 7     //USED DURING DEVELOPING TO AFFORD VOLTAGE TO THE PHOTORESISTOR

DHT dht(DHTPIN, DHTTYPE);
SoftwareSerial blue(RXBLT, TXBLT);

String text = "";
float t, h, l;

void setup(){
  Serial.begin(9600);
  blue.begin(9600);
  dht.begin();
  pinMode(PHRES, INPUT);
  pinMode(FAN1, OUTPUT);
  pinMode(FAN2, OUTPUT);
  digitalWrite(FAN1, LOW);
  digitalWrite(FAN2, LOW);

  pinMode(PHRESIN, OUTPUT);
  digitalWrite(PHRESIN, HIGH);
  loadRcmdValues(blue);
}

void loop(){

  temp_hum();
  if (isnan(t) || isnan(h)) {
    return;
  }
  Serial.println(t);
  Serial.println(h);
  Serial.println(l);
  Serial.println();
  blt();
  chkT();
}


/**
 * Confront temperature with recommended temperatures, 
 * in order to cold down, warm up or stay that way
 */
void chkT(){
  if(t > rcmdValues[0][1]){               //Cold down
    digitalWrite(FAN1, HIGH);
    digitalWrite(FAN2, HIGH);
  }
  else if(t == 21.20){         //Stay that way
    digitalWrite(FAN1, LOW);
    digitalWrite(FAN2, LOW);
  }
  else{                        //Warm up
    digitalWrite(FAN1, HIGH);
    digitalWrite(FAN2, LOW);
  }
}


/*
 * Bluetooth control function
 */
void blt(){ 
  // A SoftwareSerial#available() returns an integer equals 
  // to the amount of bytes which can be read 
  if(blue.available() > 1){
      setNewSeed(blue);
  }

  /**
   * Integer is represented in Arduino with 2 bytes (not the 4 usually). The application
   * is written in Java, that uses the 4 bytes. So it cast value before as integer
   * and then as unsigned char 'cause it is represented with 4 bytes in Arduino
   */
  blue.write((unsigned char)(int)t);
  blue.write((unsigned char)(int)h);  
  blue.write((unsigned char)(int)l); 
}


/*
 * Wait 2000 ms and detect temperature and humidity
 */
void temp_hum(){
  delay(2000);
  t = dht.readTemperature();
  h = dht.readHumidity();
  l = analogRead(PHRES)*0.09;
}
