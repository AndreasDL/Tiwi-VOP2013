#!/usr/bin/perl
use strict;
use warnings;
use DBI;
use LWP::Simple;
use Digest::SHA qw(sha512);
use Encode qw(encode_utf8);

#binmode STDOUT, ":utf8";
my $sql = "insert into users(name,pass,salt,mail,enabled) values (?,?,?,?,1)";
my $pages=100;

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

my $sth;
my $count = 0;
for my $i (1...$pages){
    my $page = "http://stackoverflow.com/users?page=$i&tab=reputation&filter=week";
    print "pagina: $page\n";
    my $content = get($page) or die 'Unable to get page';
    $content =~ s/^.*?<div id="user-browser">(.*)<div class="pager fr" >.*$/$1/s;
    $content =~ s/^.*?<table.*?>(.*?)<\/table>.*$/$1/s;
    $content =~ s/^.*?<div class="user-details">(.*)/$1/s;
    my @users = split(/<div class="user-details">/,$content);
    for my $user(@users){
    if ($user =~ /<a.*?>(.*?)<\/a>/s){
	my $name = encode_utf8($1);
    
	if ($user =~ /<span class="reputation-score".*?>(.*?)<\/span>/s){
	    my $reputation = encode_utf8($1);
	    my $salt = int(rand(10000000));
	    my $pass = unpack("H*", sha512("$name$reputation$salt"));
	    my $mail = "$name\@stackoverflow.com";
	    
	    $sth = $db->prepare($sql);
	    $sth->execute($name,$pass,$salt,$mail);
	    $count++;
	}
    }
}
}
#print $content,"\n";
print "Aantal users: $count\n";

#Disconnect
END {
    $db->disconnect if defined($db);
}
