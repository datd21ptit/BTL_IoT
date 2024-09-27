# Smart Home System2

Link git: https://github.com/datd21ptit/BTL_IoT/tree/main

Smart home system is IoT and Application Subject Project which has DHT11 and light sensor to monitor temperature, humidity and brightness of house. System contains three part:
- - Hardware: Esp8266 to read sensor data, publish it and listen to controlling signal.
- - Backend: NodeJs is chosen because of easy and modulization of implementation. Backend's role is to listen sensor data from hardward and write it to MySql database, response to HTTP request of Frontend
- - Frontend: An Android application is used for monitoring sensor, controlling device, search data from database. 

# Description
1. iot_server folder is Backend implementation.
2. [esp8266](https://github.com/datd21ptit/iot_smarthome_server/tree/master/smartHomeEsp8266) contains implementation of ESP8266
3. [smarthome]() is implementation of Frontend - Android application.


# API Docs
- API Docs in ```localhost:3001/api-docs```
# Installation
## Mosquitto
1. Download and install mosquitto in [Mosquitto Eclipse](https://mosquitto.org/download/)
2. Open cmd or terminal and run ```cd your_directory/mosquitto```
3. Create a file to save username and password in ```your_directory/mosquitto``` in format username:password and run ```mosquitto_passwd your_directory/mosquitto/passFile```
4. Create ```broker.conf``` to config mosquitto:
```
listener 1883
password_file passFile
allow_anonymous true
```
5. Run ```mosquitto -v -c broker.conf``` to run mosquitto with configuration in broker.conf
## Clone project
```git clone https://github.com/datd21ptit/BTL_IoT```
## Install Database
1. Open MySql Workbench
2. Create new schema ```smarthome```
3. Import table from folder ```./iot_server/mysql```
## Install NodeJs
1. Open folder node in Visual Studio or any else IDE
2. Run ```npm install``` to install all dependencies
3. Open ```repository.js``` and ```mqtt.js``` to modify these fields
```
'./reposistory.js'
var db_config = {
    host: "localhost",      //replace with your host
    user: "root",           //replace with your username
    password: "123456",     //replace with your password
    database: "smarthome",
    port: "3306"
    }

'./mqtt.js'
const options = { 
    clientId: 'ESP32', 
    port: 1883, 
    keepalive: 60,          
    username: 'admin',      //replce with your mqtt username
    password: '123456'      //replace with your mqtt password
    };
```
4. Run ```node smarthome.js``` to run server
## Implement ESP8266
1. Open ```./esp8266/esp8266.ino``` in Arduino IDE
2. Plug ESP8266 in by a data transfering cable
3. Select a COM port which you pluged in and select board ```Adafruit Feather HUZZAH ESP8266```
4. Install library
- DHT library: ```DHT sensor library by Adafruit``` in library manager
- ArduinoJson library: ```ArduinoJson by Benoit Blanchon```
- ESP8266WiFi library
- PubSubClient library
5. Modify this field: ```const char* mqtt_broker = "192.168.186.108"``` to the ip address of your localhost.
5. Compile and upload code


## Android application
### Prerequistic
- Android studio
- Implementation of [Backend](./iot_server).
### Install
1. Open folder [smarthome](./smarthome/) in Android studio
2. Install Virtual Device or connect physical one
3. Run application


