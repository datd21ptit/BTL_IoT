const { format } = require('date-fns'); // Import the format function from date-fns
var dateTime = require('node-datetime');
var mysql = require('mysql');

var db_config = {   
    host: "localhost",
    user: "root",
    password: "123456",
    database: "smarthome",
    port: "3306"
    }

class AppRepository{
    constructor(){
        this.dbConnection = null;
        this.handleMySQLDisconnect();
    }
    handleMySQLDisconnect() {       // Ham khoi tao connection
        this.dbConnection = mysql.createConnection(db_config);
        this.dbConnection.connect(function(err) {
            if (err) {
                console.log('Error when connecting to database:', err);
                setTimeout(handleDisconnect, 2000);
            }
            console.log("Connected to database!");
        });
    
        this.dbConnection.on('error', function(err) {
            console.log('db error', err);
            if(err.code === 'PROTOCOL_CONNECTION_LOST') {
                this.handleMySQLDisconnect(); 
            } else { 
                throw err; 
            }
        });
    }
    
    async getDashboardData(req, res){    // Ham lay du lieu dashboard
        try {
            let limit = req.query.limit;
            let action = await this.getDeviceState();
            // console.log(action);
            let listTemp = await this.getChartData("temp", limit);
            let listHumid = await this.getChartData("humid", limit);
            let listLight = await this.getChartData("light", limit);
            let listWind = await this.getChartData("wind", limit);
            let result = {
                led: action['led'],
                fan: action['fan'],
                relay: action['relay'],
                listWind: listWind,
                listTemp: listTemp,
                listHumid: listHumid,
                listLight: listLight
            };
            res.send(JSON.stringify(result)) ;
        } catch (error) {
            console.log("Error in getLatestData" + error.toString());
        }
    }

    async getChartData(sensor, limit){     // Lay du lieu chart cho dashboard voi loai cam bien sensor
        try {
            let query = "SELECT chart." + sensor +" AS valuee FROM ("
                + "SELECT sensor." + sensor + ", sensor.time FROM sensor "
                + "ORDER BY TIME DESC "
                + "LIMIT " + limit +" "
                + ") AS chart "
                + "ORDER BY chart.time ASC"; // them sap xep vao day
            return await new Promise( (resolve, reject) => {
                this.dbConnection.query(query, (err, result) => {
                    if(err) return reject(err);
                    return resolve(result.map(item => item.valuee));
                })
            });
        } catch (error) {
            console.log("Error in getChartData " + error.toString());
        }
    }

    async getDeviceState(){           // Lay du lieu cam bien trong bang table cua db
        var query = "SELECT * FROM ("
                    +"SELECT *, ROW_NUMBER() OVER (PARTITION BY device ORDER BY time DESC) AS rn "
                    +"FROM smarthome.action "
                    +"WHERE device IN ('led', 'fan', 'relay') "
                    +") AS subquery "
                    +"WHERE rn = 1;";
        return new Promise((resolve, reject) => {
            this.dbConnection.query(query, (err, result) => {
                if(err) return reject(err);
                // console.log(result);
                let ret = {};
                result.map(row => ret[row.device] = row.state );
                return resolve(ret);
            })
        })
    }

    async getSensorTable(req, res){ // Lay du lieu cho trang Table
        const page = parseInt(req.query.page)
        const limit = parseInt(req.query.limit);
        const offset = (page - 1) * limit;
        const { temp, light, humid, time, wind , timeSearch} = req.query;/// @Query("timeSearch") timeSearch: String,
        const sort = req.query.sort;
        
        let whereClauses = [];
        let queryParams = [];

        // Where clauses
        if (temp) whereClauses.push('temp = ?'), queryParams.push(temp);
        if (light) whereClauses.push('light = ?'), queryParams.push(light);
        if (humid) whereClauses.push('humid = ?'), queryParams.push(humid);
        if (wind) whereClauses.push('wind = ?'), queryParams.push(wind);
        if (time) {
            const [month, day, year] = time.split('/');
            const formattedDate = `${year}-${month}-${day}`;
            whereClauses.push('date(time) = ?');
            queryParams.push(formattedDate);
        }
        if(timeSearch){
            whereClauses.push('TIME(time) like ? ');
            queryParams.push('%' + timeSearch +'%');
        }
    
        let sqlQuery = 'SELECT id, temp, light, humid, wind, time FROM sensor';
    
        if (whereClauses.length > 0) {
            sqlQuery += ' WHERE ' + whereClauses.join(' AND ');
        }

        let orderByClauses = [];

        if(Array.isArray(sort)){
            sort.forEach( item =>{
                const tmp = JSON.parse(item);
                const col = tmp['column'];
                const ord = tmp['order'];
                if(col && (ord === 'ASC' || ord === 'DESC')) orderByClauses.push(` ${col} ${ord}`);
            })
        }else if(sort !== undefined && sort !== ""){
            const tmp = JSON.parse(sort);
            const col = tmp['column'];
            const ord = tmp['order'];
            if(col && (ord === 'ASC' || ord === 'DESC')){
                orderByClauses.push(` ${col} ${ord}`);
            }
        }

        if(orderByClauses.length > 0){
            sqlQuery += ' ORDER BY ' + orderByClauses.join(', ');
        }
        // console.log(sort);
        // console.log(orderByClauses);
        sqlQuery += ' LIMIT ? OFFSET ?';       ////// sortHere 
    
        queryParams.push(limit, offset);
        // console.log(limit);
        // console.log(sqlQuery);
        try {
            let ret = await new Promise((resolve, reject) => {
                this.dbConnection.query(sqlQuery, queryParams, (err, result) => {
                    if(err){
                        reject(err);
                    } 
                    resolve(result);
                })
            })

            let countQuery = 'SELECT COUNT(*) AS total FROM sensor';
            if (whereClauses.length > 0) {
                countQuery += ' WHERE ' + whereClauses.join(' AND ');
            }
            let total = await new Promise((resolve, reject) => {
                this.dbConnection.query(countQuery, queryParams.slice(0, -2), (err, countResult) => { // Remove limit and offset from params for count
                    if(err){
                        reject(err);
                    } 
                    const totalRows = countResult[0].total;
                    const totalPages = Math.ceil(totalRows / limit);

                    let js = {
                        page: page,
                        totalPages: totalPages,
                        totalRows: totalRows
                    }
                    resolve(js);
                });
            })
            let count = await this.getCountLess();
            // console.log("count lesst" + count);
            total.count = count;
            const data = []
            ret.forEach(item =>{
                var wind = "no sensor"
                if(item.wind != null){
                    wind = item.wind.toString()
                }
                data.push([item.id.toString(), item.temp.toString(), item.humid.toString(), item.light.toString(), wind , format(new Date(item.time), 'MM-dd HH:mm:ss')])
            })
            total.data = data;

            res.send(JSON.stringify(total));
        } catch (error) {
            console.log("Error in getSensorTable" + error);
            res.status(404).send();
        }
    }

    async getActionTable(req, res){ // Lay du lieu cho trang Table
        const page = parseInt(req.query.page)
        const limit = parseInt(req.query.limit);
        const {device, state, time, timeSearch} = req.query;
        const offset = (page - 1) * limit;
        const sort = req.query.sort;

        let whereClauses = [];
        let queryParams = [];
        if(device){
            whereClauses.push('device = ?');
            queryParams.push(device);
        }
        if (state) {
            whereClauses.push('state = ?');
            queryParams.push(state);
        }
        if (time) {
            const [month, day, year] = time.split('/');
            const formattedDate = `${year}-${month}-${day}`;
            whereClauses.push('date(time) = ?');
            queryParams.push(formattedDate);
        }
        if(timeSearch){
            // SELECT * FROM smarthome.sensor WHERE (time) like '%19%';
            whereClauses.push('TIME(time) LIKE ? ')
            queryParams.push('%' + timeSearch +'%');
        }
    
        let sqlQuery = 'SELECT * FROM action';
    
        if (whereClauses.length > 0) {
            sqlQuery += ' WHERE ' + whereClauses.join(' AND ');
        }
    
        let orderByClauses = [];
        if(Array.isArray(sort)){
            sort.forEach( item =>{
                // console.log(typeof item);
                const tmp = JSON.parse(item);
                // console.log(tmp);
                const col = tmp['column'];
                const ord = tmp['order'];
                if(col && (ord === 'asc' || ord === 'desc')){
                    orderByClauses.push(` ${col} ${ord}`);
                }
            })
        }else if(sort !== undefined){
            const tmp = JSON.parse(sort);
            const col = tmp['column'];
            const ord = tmp['order'];
            if(col && (ord === 'ASC' || ord === 'DESC')){
                orderByClauses.push(` ${col} ${ord}`);
            }
        }
        if(orderByClauses.length > 0){
            sqlQuery += ' ORDER BY ' + orderByClauses.join(', ');
        }

        sqlQuery += ' LIMIT ? OFFSET ?'; 

    
        queryParams.push(limit, offset);
        
        try {
            let ret = await new Promise( (resolve, reject) => {
                this.dbConnection.query(sqlQuery, queryParams, (err, result) => {
                    if(err) reject(err);
                    resolve(result);
                })
            })

            let countQuery = 'SELECT COUNT(*) AS total FROM action';
            if (whereClauses.length > 0) {
                countQuery += ' WHERE ' + whereClauses.join(' AND ');
            }
            let total = await new Promise((resolve, reject) => {
                this.dbConnection.query(countQuery, queryParams.slice(0, -2), (err, countResult) => { // Remove limit and offset from params for count
                    if (err) reject(err);
                    const totalRows = countResult[0].total;
                    const totalPages = Math.ceil(totalRows / limit);

                    let js = {
                        page: page,
                        totalPages: totalPages,
                        totalRows: totalRows
                    }
                    resolve(js);
                });
            })
            let count = await this.getCountTUrnFanOn();
            console.log("count fan " + count);
            total.count = count;
            const data = ret.map( item => [item.id.toString(), item.device, item.state,  format(new Date(item.time), 'MM-dd HH:mm:ss')])
            total.data = data;

            res.send(JSON.stringify(total));
        } catch (error) {
            console.log("Error in getActionTable " + error);
            res.status(404).send();
        }
    }

    async getCountLess(){
        var sql = "SELECT COUNT(*) as count FROM sensor WHERE date(time) = date(now()) and wind <=30"
        let count = await new Promise((resolve, reject) => {
            this.dbConnection.query(sql, function(err, result){
                if(err){
                    reject(err);
                    console.log(err.toString());
                } 
                resolve(result[0].count);
            })
        })
        return parseInt(count);
    }

    async getCountTUrnFanOn(){
        var sql = "SELECT COUNT(*) as count FROM action WHERE date(time) = date(now()) and device = 'fan' and state = 'on'"
        let count = await new Promise((resolve, reject) => {
            this.dbConnection.query(sql, function(err, result){
                if(err){
                    reject(err);
                    console.log(err.toString());
                } 
                resolve(result[0].count);
            })
        })
        return parseInt(count);
    }

    async insertSensorData(temperature, humidity, light, wind) { //Hàm Insert giá trị cảm biến
        var dt = dateTime.create();
        var time_formatted = dt.format('Y-m-d H:M:S');
       
        var sql = "INSERT INTO sensor (temp, humid, light,wind, time) VALUES ('" + temperature + "', '" + humidity +"', '" + light +"', '" + wind +"', '"+ time_formatted + "')";
        try {
            await new Promise((resolve, reject) => {
                this.dbConnection.query(sql, function (err, result) {
                    if (err) reject(err);
                    resolve(result);
                })
            });
        } catch (error) {
            console.log("Error in insertSensorData " + error.toString())
        }
    }

    async insertActionData(device, state) {  //Hàm Insert giá trị action
        var dt = dateTime.create();
        var time_formatted = dt.format('Y-m-d H:M:S');
        var sql = "INSERT INTO action(device, state, time) VALUES ('" + device + "', '" + state + "', '" + time_formatted +  "')";
        try {
            await new Promise((resolve, reject) => {
                this.dbConnection.query(sql, function (err, result) {
                    if (err) reject(err);
                    resolve(result);
                })
            });
        } catch (error) {
            console.log("Error in insertActionData " + error.toString())
        }
    }

    async getFanState(){
        var sql = "SELECT state FROM action WHERE device = 'fan' order by time desc limit 1";
        try {
            let ret = await new Promise((resolve, reject) => {
                this.dbConnection.query(sql, function (err, result) {
                    if (err) reject(err);
                    let ret = (result[0].state);
                    console.log(ret)
                    resolve(ret);
                })
            });
        } catch (error) {
            
        }
    }
}


module.exports = AppRepository;