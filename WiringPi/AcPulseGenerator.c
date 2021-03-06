/**********************************************************
pulse sequence generator for AC remote RH151
usage:
    AcPulseGenerator [ON/OFF] [Temperature(16-31)]
to send pulse(use ir-ctl):
    sudo ./AcPulseGenerator 0 25 && ir-ctl -d /dev/lirc0 --send=./ac.pulse
**********************************************************/

#include<stdio.h>
#include<stdlib.h>
#include<time.h>

#define byte unsigned char
#define T 425
#define PIN_NO 3

// Default byte sequence
byte sequence[] = { 35,203,38,1,0,0,24,0,54,64,0,0,0,0,0,0,0,0 };

void sendLeaderPulse();
void sendEndPulse();
byte sendBytePulse(byte data);
byte sendOnOff(int isOn);
byte sendTemperature(byte p0);

FILE* temp;

int main(int argc, char* argv[]) {


    /*
        get input arguments and save them.
        argument 1:
            default: 1(ON) 
            ON/OFF > isOn.data
        
        argument 2: 
            default: 25
            Temperature > temperature.data
    */
    
    int isOn = 1;
    if (argc > 1) {
        int temp = atoi(argv[1]);
        if (temp != 0 && temp != 1) {
            fprintf(stderr, "ON/OFF should be 0 or 1\n");
        }
        else {
            isOn = atoi(argv[1]);
        }
    }
    temp = fopen("/home/pi/isOn.data", "w");
    fprintf(temp, "%d", isOn);
    fclose(temp);

    int temperature = 25;
    if (argc > 2) {
        int temp = atoi(argv[2]);
        if (16 > temp || temp > 31) {
            fprintf(stderr, "Temparature should be between 16 and 31\n");
        }
        else {
            temperature = atoi(argv[2]);
        }
    }
    temp = fopen("/home/pi/temperature.data", "w");
    fprintf(temp, "%d", temperature);
    fclose(temp);

    // parity byte the sum of every byte of sequence
    temp = fopen("/home/pi/ac.pulse", "w");

    byte parityByte = 0;

    for (int j = 0; j < 2; j++) {
        sendLeaderPulse();
        for (int i = 0; i < 18; i++) {
            // ON/OFF byte
            if (i == 5) {
                parityByte += sendOnOff(isOn);
            }
            // temperature byte
            else if (i == 7) {
                parityByte += sendTemperature(temperature);
            }
            // parity byte
            else if (i == 17) {
                sendBytePulse(parityByte);
            }
            // default byte
            else {
                parityByte += sendBytePulse(sequence[i]);
            }
        }
        sendEndPulse();
    }
    fclose(temp);

}

// send pulse indicating start of transmission
void sendLeaderPulse() {
    fprintf(temp, "pulse %d\n", T * 8);
    fprintf(temp, "space %d\n", T * 4);
}

// send pulse indicating end of transmission
void sendEndPulse() {
    fprintf(temp, "pulse %d\n", T * 1);
    fprintf(temp, "space %d\n", T * 30);
}

// transfer byte to pulse and transmit
byte sendBytePulse(byte data) {
    for (int i = 0; i < 8; i++) {
        if ((data >> i) & 1) {
            fprintf(temp, "pulse %d\n", T * 1);
            fprintf(temp, "space %d\n", T * 3);
        }
        else {
            fprintf(temp, "pulse %d\n", T * 1);
            fprintf(temp, "space %d\n", T * 1);
        }
    }
    return data;
}

byte sendOnOff(int isOn) {
    byte data;
    if (isOn) {
        data = 32;
    }
    else {
        data = 0;
    }
    sendBytePulse(data);
    return data;
}

byte sendTemperature(byte p0) {
    byte data = p0 - 16;
    sendBytePulse(data);
    return data;
}

