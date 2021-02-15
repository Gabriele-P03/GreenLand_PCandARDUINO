#include <EEPROM.h>

//This matrix contains the recommended 
//min and max values of the surveys
int rcmdValues[3][2]; 

/*
 * Called by blt() when the recommended values of the seed
 * has to be updated. This happens when user presses on 
 * update seed button in the Greenland App - @see project
 * 
 * This function saves new recommended values inside
 * the EEPROM of the AtMega328P.
 * These values are loaded inside global variables when
 * the system is switched on.
 * So, when you have updated values, restart the system
 * 
 * Via bluetooth the phone send also the current hour
 */
void setNewSeed(Stream &blue){
    Serial.println("Settings new seed: ");
    for(int i = 0, address = 0; i < 3; i++){
      for(int j = 0; j < 2; j++){
          int x = blue.read();
          EEPROM.write(address++, x >> 8);
          EEPROM.write(address++, x >> 0xFF);
          Serial.println(x);
      }
   }
}


/**
 * Called on swithcing on of the system.
 * If available, it reads from the EEPROM the recommended values
 * of the surveys
 */
void loadRcmdValues(Stream &blue){
    for(int i = 0, address = 0; i < 3; i++){
      for(int j = 0; j < 2; j++){
           rcmdValues[i][j] = int( (unsigned char)EEPROM.read(address++) << 8 | (unsigned char)EEPROM.read(address++) & 0xFF);
           Serial.println(rcmdValues[i][j]);  //REMEMBER TO ELIMINATE THIS----->DEBUG
     }
   }
}
