const server = require('./server/server.js');

async function startup() {
  try {
    connection = await server.initialize();
  } catch (err) {
    console.error(err);
    connection.close();
    process.exit(1); // Non-zero failure code
  }
}

// start node server
startup();