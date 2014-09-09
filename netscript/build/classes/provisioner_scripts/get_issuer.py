import sys
import pwd
import getopt
import os
import re
import M2Crypto
from M2Crypto import X509

#By default look in this folder
HOME            = os.environ["HOME"]
# Path to my certificate
CERTIFICATE     = HOME + "/.ssl/encrypted.pem"
OUTFILE         = HOME + "/.ssl/out.txt" 

if "Usage" not in dir():
    def Usage():
        print "usage: " + sys.argv[ 0 ] + " [option...]"
        print """Options:
    -c certificate, --certificate=file    SSL certificate from Emulab"""

try:
    opts, REQARGS = getopt.getopt( sys.argv[ 1: ], "c:o:",
                                   [ "credentials="
                                     ] )
except getopt.GetoptError, err:
    print >> sys.stderr, str( err )
    Usage()
    sys.exit( 1 )

for opt, arg in opts:
    if opt in ( "-c", "--certificate" ):
        CERTIFICATE = arg
        print "cert=%s"%CERTIFICATE
    elif opt in ( "-o", "--outfile" ):
        OUTFILE = arg
        print "outfile=%s"%OUTFILE

print "certificate=%s outfile=%s"%(CERTIFICATE, OUTFILE)

cert = X509.load_cert( CERTIFICATE )
issuer = cert.get_issuer().CN
print "issuer=%s"%(issuer)

f = open(OUTFILE, "w")
f.write(issuer)
f.close



