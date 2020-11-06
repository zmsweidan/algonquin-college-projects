const http = require('http');
const express = require('express');
const serverConfig = require('../config.js');
const bodyParser= require('body-parser')
const multer = require('multer');
const fs = require('fs-extra'); 
const oracledb = require('oracledb');

var httpServer;
var connection;
var all_files;
const app = express();
httpServer = http.createServer(app);
app.use(bodyParser.urlencoded({extended: true}))

/**
 * Start server
 */
async function initialize() {

  // Connect to database 
  try {
    connection = await oracledb.getConnection(  {
      user          : serverConfig.db.user,
      password      : serverConfig.db.password,
      connectString : serverConfig.db.connectString
    });

    console.log('Database connected');
    oracledb.autoCommit = true;
    getAllFilesFromDb();

    return new Promise((resolve, reject) => {

      // GENERATE index.html
      app.get('/', (req, res) => {
        res.sendFile(__dirname + '/index.html');
      });
  
  
      // SET MULTER STORAGE
      var storage = multer.diskStorage({
        destination: function (req, file, cb) {
          cb(null, 'uploads')
        },
        filename: function (req, file, cb) {
          cb(null, file.fieldname + '-' + Date.now())
        }
      })
      var upload = multer({ storage: storage })
  
  
      // HTTP POST SINGLE FILE
      app.post('/uploadfile', upload.single('myFile'), (req, res, next) => {
        const file = req.file; 
        if (!file) {
          const error = new Error('Please upload a file')
          error.httpStatusCode = 400
          return next(error)
        }
        res.send(file);
        insertFileToDb(file);
      })
  
  
      // POST MULTIPLE FILES
      app.post('/uploadmultiple', upload.array('myFiles', 12), (req, res, next) => {
        const files = req.files
        if (!files) {
          const error = new Error('Please choose files')
          error.httpStatusCode = 400
          return next(error)
        }
        res.send(files);
        insertFilesToDb(files);     
      })


      // GET ALL FILES
      app.get('/files', (req, res) => {
        getAllFilesFromDb();
        res.send(all_files);
      });
  
      // LISTEN TO PORT
      httpServer.listen(serverConfig.port)
        .on('listening', () => {
          console.log(`Web server listening on localhost:${serverConfig.port}`);
          resolve();
        })
        .on('error', err => {
          reject(err);
        });
        
    });

  } catch(err) {
    console.error('Error in processing:\n', err);
  } finally {
    console.log('Server started');
    return connection;
  }
}


/** get all files from database */
async function getAllFilesFromDb() {
  var query = `SELECT * FROM cst8276_files`
  try{
    // set all blobs to be returns as buffers
    oracledb.fetchAsBuffer = [ oracledb.BLOB ];
    result = await connection.execute(query, [], { outFormat: oracledb.OBJECT }, 
      function (err, result) {
        if (err) throw err;
        // convert blob to base64
        result.rows.forEach( row => {
          row["FILE_DATA"] = row["FILE_DATA"].toString('base64');
        });
        all_files = (result.rows);
        return result.rows;
      })
  } catch(err) {
    console.error("Retrieval Error: " + err.message);
  }
}


/**
 * insert single file to database
 * @param {*} file file object
 */
async function insertFileToDb(file) {
  var fileObj = fs.readFileSync(file.path);
  var encodedObj = fileObj.toString('base64');    
  var buffer = Buffer.from(encodedObj, 'base64');
  var values = {
    fn: file.originalname,
    ft: file.mimetype,
    fe: 'base64',
    fd:  buffer,
    fs: buffer.length
  };
  console.log(values);
  var query = `INSERT INTO cst8276_files VALUES(null, :fn, :ft, :fe, :fd, :fs, null)`
  try{
    await connection.execute(query, values);
  } catch(err) {
    console.error("Insertion Error: " + err.message);
  }
}


/**
 * inserts multiple files to database
 * @param {*} files file object
 */
async function insertFilesToDb(files) {
  files.forEach(file => {
    insertFileToDb(file);
  });
}

module.exports.initialize = initialize;
