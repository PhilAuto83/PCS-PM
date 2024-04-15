CREATE TABLE IF NOT EXISTS  BidList (
  BidListId TINYINT(4) NOT NULL AUTO_INCREMENT PRIMARY KEY,
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
  side VARCHAR(125)
);

CREATE TABLE IF NOT EXISTS  Trade (
  TradeId TINYINT(4) NOT NULL AUTO_INCREMENT,
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

CREATE TABLE IF NOT EXISTS  CurvePoint (
  Id TINYINT(4) NOT NULL AUTO_INCREMENT,
  CurveId tinyint,
  asOfDate TIMESTAMP,
  term DOUBLE ,
  value DOUBLE ,
  creationDate TIMESTAMP ,

  PRIMARY KEY (Id)
);

CREATE TABLE IF NOT EXISTS Rating (
  Id TINYINT(4) NOT NULL AUTO_INCREMENT,
  moodysRating VARCHAR(125),
  sandRating VARCHAR(125),
  fitchRating VARCHAR(125),
  orderNumber tinyint,

  PRIMARY KEY (Id),
  FOREIGN KEY (Id) REFERENCES Trade (TradeId)
      ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS RuleName (
  Id TINYINT(4) NOT NULL AUTO_INCREMENT,
  name VARCHAR(125),
  description VARCHAR(125),
  json VARCHAR(125),
  template VARCHAR(512),
  sqlStr VARCHAR(125),
  sqlPart VARCHAR(125),

  PRIMARY KEY (Id)
);

CREATE TABLE IF NOT EXISTS Users (
  Id TINYINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(125) UNIQUE,
  password VARCHAR(125),
  fullname VARCHAR(125) UNIQUE,
  role VARCHAR(125),

  PRIMARY KEY (Id)
);

-- user insertions
insert into Users(username, password, fullname, role) values("admintest", "$2a$10$YVkCtGPOjWWGEkN8nF9LJ.FiG78FuC5a79a9uoJ613qmMuit7ddGG", "Administrator",  "ADMIN");
insert into Users(username, password, fullname, role) values("usertest", "$2a$10$3A5TonAD.rpsreOKJfT6EOzWb9buRiq6lVz77TwG4jvqpmcp1RWlO", "User", "USER");
-- trade insertions
insert into Trade(account, type, buy_quantity) values("123456","type test", 10.00);
insert into Trade(account, type, buy_quantity) values("456789","type test2", 20.00);
insert into Trade(account, type, buy_quantity) values("888888","type test3", 30.00);
insert into Trade(account, type, buy_quantity) values("741852","type test4", 40.00);
insert into Trade(account, type, buy_quantity) values("112233","type to be deleted", 1000.00);
-- bid list insertions
insert into BidList(account, type, bid_quantity) values("123456","type test", 10.00);
insert into BidList(account, type, bid_quantity) values("456789","type test2", 20.00);
insert into BidList(account, type, bid_quantity) values("888888","type test3", 30.00);
insert into BidList(account, type, bid_quantity) values("888888","type test5", 100.00);
insert into BidList(account, type, bid_quantity) values("888889","type test6", 1000.00);
-- rating insertion
insert into Rating(moodys_rating, sandprating, fitch_rating, order_number) values("AAA+", "AAA", "AAA-",1);
-- rule name insertions
insert into RuleName(name, description, json, template, sql_str, sql_part) values ("Rule 1", "this is rule 1", "json 1", "template 1", "select * from rulename;", "sql rule 1");
insert into RuleName(name, description, json, template, sql_str, sql_part) values ("Rule 25", "this is rule 25", "json 25", "template 25", "select * from rulename;", "sql rule 25");
-- curve point insertions
insert into CurvePoint(term, value) values(150.005, 120.56);
insert into CurvePoint(term, value) values(110.005, 100.56);

