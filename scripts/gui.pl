#!/usr/bin/perl
use warnings;
use strict;
use Tk;
use File::Spec::Functions qw(rel2abs);
use File::Basename;


my $path =  dirname(rel2abs($0));


my $mw = MainWindow->new();
my $btn = $mw->Button(-text => 'Reset Database',
                      -command => sub{system("$path/make_database.pl")})->pack;
 
MainLoop;
