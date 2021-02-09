#include <SoftwareSerial.h>
#include <time.h>
#include <stdlib.h>

#define RES 12;
#define FAN 13;
#define RHT 7;
#define TX 1;
#define RX 0;


void setup() {

  pinMode(RHT, INPUT);
  pinMode(FAN, OUTPUT);
  pinMode(RES, OUTPUT);
  
  Serial.begin(9600);
  srand(time(NULL));
}



void loop() {
   
}



void fan(){
  bool on = false;
  if(!on){
    digitalWrite(FAN, HIGH);
    on = true;
  }else{
    digitalWrite(FAN, LOW);
    on = false;
  }
}


void res(){
  bool on = false;
  if(!on){
    digitalWrite(RES, HIGH);
    on = true;
  }else{
    digitalWrite(RES, LOW);
    on = false;
  }
}
