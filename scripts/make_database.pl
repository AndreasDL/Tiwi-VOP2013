#!/usr/bin/perl
use warnings;
use strict;
use DBI;

#team02_testing for test server, team02_stable for stable server
my $user = "team02_stable";

my $dropUsers = "DROP TABLE Users";
my $createUsers = "CREATE TABLE Users(
  id INTEGER NOT NULL,
  name VARCHAR(50) NOT NULL,
  pass VARCHAR(150),
  salt VARCHAR(150),
  mail VARCHAR(100),
  game INTEGER,
  color INTEGER,
  enabled INTEGER DEFAULT 0 NOT NULL,
  administrator INTEGER DEFAULT 0 NOT NULL,
  blocked INTEGER DEFAULT 0 NOT NULL,
  facebookuser INTEGER DEFAULT 0 NOT NULL,
  facebookid INTEGER DEFAULT 0 NOT NULL,
  gamepass VARCHAR(150),
  PRIMARY KEY (id)
)";
my $dropGames = "DROP TABLE Games";
my $createGames = "CREATE TABLE Games(
  id INTEGER NOT NULL,
  status INTEGER NOT NULL,
  name VARCHAR(20) NOT NULL,
  host INTEGER NOT NULL,
  maxplayers INTEGER NOT NULL,
  datum VARCHAR(50) NOT NULL,
  PRIMARY KEY (id)
)";
my $dropBoards= "DROP TABLE Boards";
my $createBoards="CREATE TABLE Boards(
  id INTEGER NOT NULL,
  nr INTEGER NOT NULL,
  commands VARCHAR2(4000),
  PRIMARY KEY(id,nr)
)";


my $dropGameSeq = "DROP SEQUENCE game_seq";
my $createGameSeq = "CREATE SEQUENCE game_seq 
start with 4 
increment by 1";
my $dropGameTrigger = "DROP TRIGGER game_trigger";
my $createGameTrigger = "CREATE TRIGGER game_trigger
before insert on Games
for each row
begin
select game_seq.nextval into :new.id from dual;
end;
";
my $dropUserSeq = "DROP SEQUENCE user_seq";
my $createUserSeq = "CREATE SEQUENCE user_seq 
start with 1 
increment by 1";
my $dropUserTrigger = "DROP TRIGGER user_trigger";
my $createUserTrigger = "CREATE TRIGGER user_trigger
before insert on Users
for each row
begin
select user_seq.nextval into :new.id from dual;
end;
";
#pass = team02
my $createTestuser = "
insert into users (name,pass,salt,mail,enabled,administrator) values ('team02','2d349d273ee5ce2dadf095c2c2aa107cf945f5fce73344c6e78afe46a298f28c2549ccd45c8acf94e26448b49afcd090d0615a0ccdbdf26b4598fc41299d7f35','hoi','test\@test.com',1,1)
";

#gameid=1
my $createJens = "
insert into users (name,enabled,administrator,game,color,gamepass)
values ('Jens',1,0,1,-65536,'pass')
";
my $createSam = "
insert into users (name,enabled,administrator,game,color,gamepass)
values ('Sam',1,0,1,-16776961,'pass')
";

#gameid=2
my $createJens2 = "
insert into users (name,enabled,administrator,game,color,gamepass)
values ('Jens2',1,0,2,-65536,'pass')
";
my $createSam2 = "
insert into users (name,enabled,administrator,game,color,gamepass)
values ('Sam2',1,0,2,-16776961,'pass')
";
my $createTom2 = "
insert into users (name,enabled,administrator,game,color,gamepass)
values ('Tom2',1,0,2,-1,'pass')
";

#gameid=3
my $createJens3 = "
insert into users (name,enabled,administrator,game,color,gamepass)
values ('Jens3',1,0,3,-65536,'pass')
";
my $createSam3 = "
insert into users (name,enabled,administrator,game,color,gamepass)
values ('Sam3',1,0,3,-16776961,'pass')
";


#Database settings
my $platform = "Oracle";
my $sid = "III";
my $host = "db.vop.tiwi.be";
my $port = "1521";
my $pw = "VMVQLSN8";

#Connect to Database
my $connstr = "dbi:$platform:host=$host;port=$port;sid=$sid";
print "Verbinding maken met databank...\n";
my $db = DBI->connect($connstr, $user, $pw ) || die( $DBI::errstr . "\n" );
print "Verbinding gemaakt!\n\n";


#Excecute the Queries
my $sth;

#Drop Current DB
#Drop GameTrigger BEFORE Games!!!
print "Drop UserTrigger\n";
$sth = $db->prepare($dropUserTrigger);
$sth->execute();
print "Drop GameTrigger\n";
$sth = $db->prepare($dropGameTrigger);
$sth->execute();
print "Drop Users\n";
$sth = $db->prepare($dropUsers);
$sth->execute();
print "Drop Games\n";
$sth = $db->prepare($dropGames);
$sth->execute();
print "Drop Boards\n";
$sth = $db->prepare($dropBoards);
$sth->execute();
print "Drop GameSequence\n";
$sth = $db->prepare($dropGameSeq);
$sth->execute();
print "Drop UserSequence\n";
$sth = $db->prepare($dropUserSeq);
$sth->execute();

#Create new DB
print "Create Users\n";
$sth = $db->prepare($createUsers);
$sth->execute();
print "Create Games\n";
$sth = $db->prepare($createGames);
$sth->execute();
print "Create Board\n";
$sth = $db->prepare($createBoards);
$sth->execute();
print "Create GameSequence\n";
$sth = $db->prepare($createGameSeq);
$sth->execute();
print "Create GameTrigger\n";
$sth = $db->prepare($createGameTrigger);
$sth->execute();
print "Create UserSequence\n";
$sth = $db->prepare($createUserSeq);
$sth->execute();
print "Create UserTrigger\n";
$sth = $db->prepare($createUserTrigger);
$sth->execute();

#Insert testuser
print "Create Testuser\n";
$sth = $db->prepare($createTestuser);
$sth->execute();
print "Create Jens\n";
$sth = $db->prepare($createJens);
$sth->execute();
print "Create Sam\n";
$sth = $db->prepare($createSam);
$sth->execute();
print "Create Jens2\n";
$sth = $db->prepare($createJens2);
$sth->execute();
print "Create Sam2\n";
$sth = $db->prepare($createSam2);
$sth->execute();
print "Create Tom2\n";
$sth = $db->prepare($createTom2);
$sth->execute();
print "Create Jens3\n";
$sth = $db->prepare($createJens3);
$sth->execute();
print "Create Sam3\n";
$sth = $db->prepare($createSam3);
$sth->execute();


#Disconnect
END {
    $db->disconnect if defined($db);
}