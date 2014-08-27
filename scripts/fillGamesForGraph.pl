#!/usr/bin/perl
use strict;
use warnings;
use DBI;
use LWP::Simple;
use Digest::SHA qw(sha512);
use Encode qw(encode_utf8);

#binmode STDOUT, ":utf8";
my $sql = "insert into games (status,name,host,maxplayers,datum) values (2,?,?,?,?)";
my $aantal=5000;

#Database settings
my $dbuser = "team02_stable";
my $platform = "Oracle";
my $sid = "III";
my $host = "db.vop.tiwi.be";
my $port = "1521";
my $pw = "VMVQLSN8";

#Connect to Database
my $connstr = "dbi:$platform:host=$host;port=$port;sid=$sid";
print "Verbinding maken met databank...\n";
my $db = DBI->connect($connstr, $dbuser, $pw ) || die( $DBI::errstr . "\n" );
print "Verbinding gemaakt!\n\n";


#inserting data
print "inserting data";

for (1...$aantal){
	#print "insert $_\n";

	my $sth;

	my $name = "testgame";
	my $host = "2";
	my $maxplayers = "4";
	my $datum = "20130000" + (int(rand(12))+1) * 100 + (int(rand(30))+1);

	$sth = $db->prepare($sql);
	$sth->execute($name,$host,$maxplayers,$datum);
}
