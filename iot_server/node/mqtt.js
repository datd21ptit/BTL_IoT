var mqtt = require('mqtt');
var mqtt_sensor_topic = "sensor"
var mqtt_action_topic = "action"
var mqtt_initial_topic = "mytopic"
const options = { 
    clientId: 'ESP32', 
    port: 1883, 
    keepalive: 60,
    username: 'admin', 
    password: '123456' };
// const client = 

let resPromiseResolver;

class MqttClient{
    constructor(appRepository){
        this.appRepository = appRepository;
        this.client = mqtt.connect('tcp://localhost', options);

        this.client.on('connect', function () {
            console.log("connected to mqtt")
    
            this.subscribe(mqtt_sensor_topic)
    
            this.subscribe(mqtt_initial_topic)
    
            this.subscribe(mqtt_action_topic)
        })

        this.client.on('message', async function (topic, message, packet ) {
            try {
                
            
            var msg_str = message.toString();
            var state = await appRepository.getDeviceState();
            var isOn = state.fan
            if(topic == mqtt_sensor_topic) {
                var jsonObject = JSON.parse(msg_str);
                var wind = jsonObject["wind"];
                var intWind = parseInt(wind)
                console.log("wind "+ wind + " ison: " +  isOn);

                if(intWind <= 30 && isOn == "off"){
                    let dat = {
                        device: "fan",
                        state: "on",
                    }
                    this.publish(mqtt_action_topic, JSON.stringify(dat), (err) => {
                        if (err) {
                            console.error(`Failed to publish message to topic ${topic}:`, err);
                        }
                    });
                    await appRepository.insertActionData("fan", "on")
                }else if(intWind > 30 && isOn == "on"){
                    let dat = {
                        device: "fan",
                        state: "off",
                    }
                    this.publish(mqtt_action_topic, JSON.stringify(dat), (err) => {
                        if (err) {
                            console.error(`Failed to publish message to topic ${topic}:`, err);
                        }
                    });
                    await appRepository.insertActionData("fan", "off")
                }


                appRepository.insertSensorData(jsonObject["temp"], jsonObject["humid"], jsonObject["light"], jsonObject["wind"]);
            }else if(topic == mqtt_initial_topic){
                if(msg_str == "esp_setup"){
                    let latest = await appRepository.getDeviceState();
                    let ret = {
                        fan: latest.fan,
                        led: latest.led,
                        relay: latest.relay
                    }
                    console.log("initital " + JSON.stringify(ret));
                    try {
                        this.publish(mqtt_initial_topic, JSON.stringify(ret));
                    } catch (error) {
                        console.log(error);
                    }
                }
            }else if (topic === mqtt_action_topic) {
                const msg = message.toString();
                if (msg === "True") {
                    // Store the response object to be used in the /action route
                    if (resPromiseResolver) {
                        resPromiseResolver();
                    }
                }
            }
        } catch (error) {
            console.log(error)
        }
        });
    }


    async checkWind(wind){
        
    }
    publish(topic, message){
        this.client.publish(topic, message, (err) => {
            if (err) console.log(err);
        })
    }
    subscribe(topic) {
        this.client.subscribe(topic, (err) => {
            if (err) {
                console.error(`Failed to subscribe to topic ${topic}:`, err);
            } else {
                console.log(`Subscribed to topic ${topic}`);
            }
        });
    }

    publishAction(device, state, res) {
        let dat = {
            device: device,
            state: state,
        }
        this.client.publish(mqtt_action_topic, JSON.stringify(dat), (err) => {
            if (err) {
                console.error(`Failed to publish message to topic ${topic}:`, err);
            }
        });
        new Promise((resolve) => {
            resPromiseResolver = resolve;
        }).then(() => {
            this.appRepository.insertActionData(device, state);
            return res.status(200).send();
        });
    }
}
module.exports = MqttClient;