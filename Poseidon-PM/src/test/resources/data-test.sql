CREATE TABLE IF NOT EXISTS BidList (
  BidListId tinyint(4) NOT NULL AUTO_INCREMENT,
  account VARCHAR(30) NOT NULL,
  type VARCHAR(30) NOT NULL,
  bidQuantity DOUBLE,
  askQuantity DOUBLE,
  bid DOUBLE ,
  ask DOUBLE,
  benchmark VARCHAR(125),
  bidListDate TIMESTAMP,
  commentary VARCHAR(125),
  security VARCHAR(125),
  status VARCHAR(10),
  trader VARCHAR(125),
  book VARCHAR(125),
  creationName VARCHAR(125),
  creationDate TIMESTAMP ,
  revisionName VARCHAR(125),
  revisionDate TIMESTAMP ,
  dealName VARCHAR(125),
  dealType VARCHAR(125),
  sourceListId VARCHAR(125),
  side VARCHAR(125),

  PRIMARY KEY (BidListId)
);

CREATE TABLE  IF NOT EXISTS Trade (
  TradeId tinyint(4) NOT NULL AUTO_INCREMENT,
  account VARCHAR(30) NOT NULL,
  type VARCHAR(30) NOT NULL,
  buyQuantity DOUBLE,
  sellQuantity DOUBLE,
  buyPrice DOUBLE ,
  sellPrice DOUBLE,
  tradeDate TIMESTAMP,
  security VARCHAR(125),
  status VARCHAR(10),
  trader VARCHAR(125),
  benchmark VARCHAR(125),
  book VARCHAR(125),
  creationName VARCHAR(125),
  creationDate TIMESTAMP ,
  revisionName VARCHAR(125),
  revisionDate TIMESTAMP ,
  dealName VARCHAR(125),
  dealType VARCHAR(125),
  sourceListId VARCHAR(125),
  side VARCHAR(125),

  PRIMARY KEY (TradeId)
);

CREATE TABLE  IF NOT EXISTS CurvePoint (
  Id tinyint(4) NOT NULL AUTO_INCREMENT,
  CurveId tinyint,
  asOfDate TIMESTAMP,
  term DOUBLE ,
  value DOUBLE ,
  creationDate TIMESTAMP ,

  PRIMARY KEY (Id)
);

CREATE TABLE  IF NOT EXISTS Rating (
  Id tinyint(4) NOT NULL AUTO_INCREMENT,
  moodysRating VARCHAR(125),
  sandRating VARCHAR(125),
  fitchRating VARCHAR(125),
  orderNumber tinyint,

  PRIMARY KEY (Id)
);

CREATE TABLE  IF NOT EXISTS RuleName (
  Id tinyint(4) NOT NULL AUTO_INCREMENT,
  name VARCHAR(125),
  description VARCHAR(125),
  json VARCHAR(125),
  template VARCHAR(512),
  sqlStr VARCHAR(125),
  sqlPart VARCHAR(125),

  PRIMARY KEY (Id)
);

CREATE TABLE IF NOT EXISTS Users (
  Id tinyint(4) NOT NULL AUTO_INCREMENT,
  username VARCHAR(125) UNIQUE,
  password VARCHAR(125),
  fullname VARCHAR(125) UNIQUE,
  role VARCHAR(125),

  PRIMARY KEY (Id)
);

insert into Users(username, password, fullname, role) values("admintest", "$2a$10$YVkCtGPOjWWGEkN8nF9LJ.FiG78FuC5a79a9uoJ613qmMuit7ddGG", "Administrator",  "ADMIN");
insert into Users(username, password, fullname, role) values("usertest", "$2a$10$3A5TonAD.rpsreOKJfT6EOzWb9buRiq6lVz77TwG4jvqpmcp1RWlO", "User", "USER");