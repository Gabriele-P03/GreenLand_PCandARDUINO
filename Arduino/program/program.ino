#include <SoftwareSerial.h>
#include <DHT.h>
#include <EEPROM.h>

#define DHTTYPE DHT11   //DHT Temperature and Humidity type
#define DHTPIN 8        //DHT Temperature and Humidity sensor on 8th pin 

#define RXBLT 2         //HC-05 module TX receive data from broadcast and TRANSMIT to Arduino
#define TXBLT 3         //HC-05 module RECEIVE data from Arduino and transmit on broadcast

#define FAN1 12         //First Fan's relay pin, common with the resistence. INTO
#define FAN2 13         //Second Fan's relay. OUTO

DHT dht(DHTPIN, DHTTYPE);
SoftwareSerial blue(RXBLT, TXBLT);

String text = "";

//This matrix contains the recommended 
//min and max values of the surveys
int rcmdValues[3][2]; 
float t, h;

void setup(){
  Serial.begin(9600);
  blue.begin(9600);
  dht.begin();
  pinMode(FAN1, OUTPUT);
  pinMode(FAN2, OUTPUT);
  digitalWrite(FAN1, LOW);
  digitalWrite(FAN2, LOW);
  loadRcmdValues();
}

void loop(){

  temp_hum();
  if (isnan(t) || isnan(h)) {
    return;
  }
  Serial.println(t);
  Serial.println(h);
  Serial.println("\n");
  blt();
  chkT();
}

/**
 * Confront temperature with recommended temperatures, 
 * in order to cold down, warm up or stay that way
 */
void chkT(){
  
  if(t > 21.20){               //Cold down
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
  blue.write((int)t);
  blue.write((int)h);

  
   // A SoftwareSerial#available() returns an integer equals 
   // to the amount of bytes which can be read
  if(blue.available() > 0){
    setNewSeed();
  }
}

/*
 * Wait 2000 ms and detect temperature and humidity
 */
void temp_hum(){
  delay(2000);
  t = dht.readTemperature();
  h = dht.readHumidity();
}


/*
 * Called by blt() when the recommended values of the seed
 * has to be updated. This happens when user presses on 
 * update seed button in the Greenland App - @see project
 * 
 * 
 * This function saves new recommended values inside
 * the EEPROM of the AtMega328P.
 * These values are loaded inside global variables when
 * the system is switched on.
 * So, when you have updated values, restart the system
 */
void setNewSeed(){
    for(int i = 0, address = 0; i < 3; i++){
      for(int j = 0; j < 2; j++){
          int x = blue.read();
          EEPROM.write(address++, x >> 8);
          EEPROM.write(address++, x >> 0xFF);
          Serial.println(x);  //REMEMBER TO ELIMINATE THIS----->DEBUG
      }
    }
}


/**
 * Called on swithcing on of the system.
 * If available, it reads from the EEPROM the recommended values
 * of the surveys
 */
void loadRcmdValues(){
  int len = EEPROM.length();
  if(len > 22){
    for(int i = 0, address = 0; i < 3; i++){
      for(int j = 0; j < 2; j++){
           rcmdValues[i][j] = int((unsigned char)EEPROM.read(address++) << 8 |
                                  (unsigned char)EEPROM.read(address++) & 0xFF);
           Serial.println(rcmdValues[i][j]);  //REMEMBER TO ELIMINATE THIS----->DEBUG
      }
    }
  }
}
