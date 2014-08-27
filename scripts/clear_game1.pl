#!/usr/bin/perl
use warnings;
use strict;
use DBI;

#team02_testing for test server, team02_stable for stable server
my $user = "team02_stable";

my $query = "delete from boards where id = 1";

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

#Excecute the Querie
my $sth;

print "Drop Game1\n";
$sth = $db->prepare($query);
$sth->execute();

#Disconnect
END {
    $db->disconnect if defined($db);
}
