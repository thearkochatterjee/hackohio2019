#include "Stepper.h"
#include <Servo.h>
int LED = 13;
int state;
const int spr = 200;
Stepper mystep(spr, 8, 10, 9, 11);
Servo myservo;

void setup() {
  // sets the pins as outputs:
  pinMode(LED, OUTPUT);
  Serial.begin(9600);
  mystep.setSpeed(80);
  myservo.attach(6);
  myservo.write(0);
}
void loop() {
  //if some date is sent, reads it and saves in state
  if (Serial.available() > 0) {
    state = Serial.read();
  }
  Serial.println(state);
  if (state == 1) {
    digitalWrite(LED, HIGH);
    opendoor();
    state = 0;
  }
  else {
    digitalWrite(LED, LOW);
  }
}

void opendoor() {
  //int sdir = spr / 4;
  mystep.setSpeed(80);
  mystep.step(spr);
  delay(500);
  myservo.write(179);
  mystep.setSpeed(80);
  mystep.step(-spr);
  delay(500);
  myservo.write(1);
}
