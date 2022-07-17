const mysql = require("mysql");
const pool = mysql.createPool({
    connectionLimit: 10,
    host: "mysql",
    user: "root",
    password: "suchan",
    databases: "myapp",
});
exports.pool = pool;