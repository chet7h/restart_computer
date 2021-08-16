#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <ESP8266WiFiMulti.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>
#include <L298NX2.h>
// ==================================================================================
//                                     khai báo biến
// ==================================================================================

// mã thiết bị. mỗi room sẽ chưa 1 robot. nó là ID tron table [room_info]
#define DEVICE_ID  "1"
#define VERSION  "1.0.0"

const String HOST = "http://192.168.1.5";
const int PORT = 8080;

//WIFI
ESP8266WiFiMulti WiFiMulti;

//MOTOR
// Pin definition
const unsigned int IN1_A = D1;
const unsigned int IN2_A = D2;
const unsigned int IN1_B = D5;
const unsigned int IN2_B = D6;

// Initialize both motors
L298NX2 motors(IN1_A, IN2_A, IN1_B, IN2_B);
// ======================================END=========================================


void setup() {

  Serial.begin(115200);
  // Serial.setDebugOutput(true);

  Serial.println();
  Serial.println();
  Serial.println();

  for (uint8_t t = 4; t > 0; t--) {
    Serial.printf("[SETUP] WAIT %d...\n", t);
    Serial.flush();
    delay(1000);
  }

  WiFi.mode(WIFI_STA);
  WiFiMulti.addAP("Khu c", "123456790");

}

// khởi tạo là stop
int status = 0;

void loop() {
  // wait for WiFi connection
  if ((WiFiMulti.run() == WL_CONNECTED)) {

    WiFiClient client;

    HTTPClient http;

    Serial.print("[HTTP] begin...\n");
    if (http.begin(client, HOST + ":" + PORT + "/robot?deviceId=" + DEVICE_ID)) {  // HTTP


      Serial.print("[HTTP] GET...\n");
      Serial.print(HOST + ":" + PORT + "/robot?deviceId=" + DEVICE_ID);
      // start connection and send HTTP header
      int httpCode = http.GET();

      // httpCode will be negative on error
      if (httpCode > 0) {
        // HTTP header has been send and Server response header has been handled
        Serial.printf("[HTTP] GET... code: %d\n", httpCode);

        // file found at server
        if (httpCode == HTTP_CODE_OK || httpCode == HTTP_CODE_MOVED_PERMANENTLY) {
          status = http.getString().toInt();
          Serial.println(status);
        }
      } else {
        Serial.printf("[HTTP] GET... failed, error: %s\n", http.errorToString(httpCode).c_str());
        //không thể kết nói với server thì set là stop
        status = 0;
      }

      http.end();
    } else {
      Serial.printf("[HTTP} Unable to connect\n");
      //không thể kết nói với server thì set là stop
      status = 0;
    }
  }



  // naviation
  //0: stop | 1: trái | 2: phải | 3: lùi | 4: tiến | 999: update.
  switch (status) {
    case 0:    // stop
      Serial.printf("stop");
      motors.stop();
      break;
    case 1:    // trái
      Serial.printf("trái");
      motors.forwardA();
      motors.backwardB();
      break;
    case 2:    // phải
      Serial.printf("phải");
      motors.forwardB();
      motors.backwardA();
      break;
    case 3:    // lùi
      Serial.printf("lùi");
      motors.backward();
      break;
    case 4:    // tiến
      Serial.printf("tiến");
      motors.forward();
      break;
  }
}
