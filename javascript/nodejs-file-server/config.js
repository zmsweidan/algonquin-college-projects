module.exports = {
  port: process.env.HTTP_PORT || 3000 ,
  db: {
    user: "",
    password: "",
    connectString: "localhost/orcl",
    poolMin: 10,
    poolMax: 10,
    poolIncrement: 0
  }
};