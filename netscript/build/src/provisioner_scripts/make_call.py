#
# GENIPUBLIC-COPYRIGHT
# Copyright (c) 2008-2010 University of Utah and the Flux Group.
# All rights reserved.
# 
# Permission to use, copy, modify and distribute this software is hereby
# granted provided that (1) source code retains these copyright, permission,
# and disclaimer notices, and (2) redistributions including binaries
# reproduce the notices in supporting documentation.
#
# THE UNIVERSITY OF UTAH ALLOWS FREE USE OF THIS SOFTWARE IN ITS "AS IS"
# CONDITION.  THE UNIVERSITY OF UTAH DISCLAIMS ANY LIABILITY OF ANY KIND
# FOR ANY DAMAGES WHATSOEVER RESULTING FROM THE USE OF THIS SOFTWARE.

import sys
import pwd
import getopt
import time
import os
import re
import xmlrpclib

ACCEPTSLICENAME=1
OtherUser  = None
Expiration = (60 * 30)

from urlparse import urlsplit, urlunsplit
from urllib import splitport
import xmlrpclib
import M2Crypto
from M2Crypto import X509
import socket
import time;

if "Usage" not in dir():
    def Usage():
        print "usage: " + sys.argv[ 0 ] + " [option...]"
        print """Options:
    -c file, --credentials=file         read self-credentials from file
                                            [default: query from SA]
    -u authority, --authority           authority to be called 
    -d, --debug                         be verbose about XML methods invoked
    -f file, --certificate=file         read SSL certificate from file
                                            [default: ~/.ssl/encrypted.pem]
    -h, --help                          show options and usage"""
        if "ACCEPTSLICENAME" in globals():
            print """    -n name, --slicename=name           specify human-readable name of slice
                                            [default: mytestslice]"""
            pass
        print """    -m uri, --cm=uri           specify uri of component manager
                                            [default: local]"""
        print """    -p file, --passphrase=file          read passphrase from file
                                            [default: ~/.ssl/password]
    -r file, --read-commands=file       specify additional configuration file
    -s file, --slicecredentials=file    read slice credentials from file
                                            [default: query from SA]
    -a file, --admincredentials=file    read admin credentials from file"""

try:
    opts, REQARGS = getopt.getopt( sys.argv[ 1: ], "c:df:hn:p:r:s:m:a:u:o:w:i:t:e:v:l:b:f:g:j:k:q:x:y:z:",
                                   [ "credentials=", "debug", "certificate=",
                                     "help", "passphrase=", "read-commands=",
                                     "slicecredentials=","admincredentials",
                                     "slicename=", "cm=", "delete", "authority",
                                     "command", "passphrase", "sliceurn"] )
except getopt.GetoptError, err:
    print >> sys.stderr, str( err )
    Usage()
    sys.exit( 1 )

args = REQARGS

HOME            = os.environ["HOME"]
# Path to my certificate
CERTIFICATE     = ""
PASSPHRASEFILE  = ""
passphrase      = ""

CONFIGFILE      = ".protogeni-config.py"
GLOBALCONF      = HOME + "/" + CONFIGFILE
LOCALCONF       = ""
EXTRACONF       = ""
SLICENAME       = ""
REQARGS         = ""
CMURI           = ""
DELETE          = ""
CREDENTIAL      = ""
TYPE            = ""

if os.path.exists( GLOBALCONF ):
    execfile( GLOBALCONF )
if os.path.exists( LOCALCONF ):
    execfile( LOCALCONF )
if EXTRACONF and os.path.exists( EXTRACONF ):
    execfile( EXTRACONF )
SERVER_PATH     = { "default" : ":443/protogeni/xmlrpc/" }

params = {}
selfcredentialfile = None
slicecredentialfile = None
admincredentialfile = None

REGISTER = 0
REDEEM = 0
STARTSLICE = 0
STATUS = 0
DELETE = 0
OUTPUTFILE= ""

def Fatal(message):
    print >> sys.stderr, message
    sys.exit(1)

def PassPhraseCB(v, prompt1='Enter passphrase:', prompt2='Verify passphrase:'):
    return PASSPHRASE 

def geni_am_response_handler(method, method_args):
    """Handles the GENI AM responses, which are different from the
    ProtoGENI responses. ProtoGENI always returns a dict with three
    keys (code, value, and output. GENI AM operations return the
    value, or an XML RPC Fault if there was a problem.
    """
    return apply(method, method_args)

#
# Call the rpc server.
#
def do_method(module, method, params, URI=None, quiet=False, version=None,
              response_handler=None):
    if not os.path.exists(CERTIFICATE):
        return Fatal("error: missing emulab certificate: %s\n" % CERTIFICATE)
    
    from M2Crypto.m2xmlrpclib import SSL_Transport
    from M2Crypto import SSL

    if URI == None and CMURI and (module == "cm" or module == "cmv2"):
        URI = CMURI
        pass

    if URI == None:
        if module in XMLRPC_SERVER:
            addr = XMLRPC_SERVER[ module ]
        else:
            addr = XMLRPC_SERVER[ "default" ]

        if module in SERVER_PATH:
            path = SERVER_PATH[ module ]
        else:
            path = SERVER_PATH[ "default" ]

        URI = "https://" + addr + path + module
    elif module:
        URI = URI + "/" + module
        pass

    if version:
        URI = URI + "/" + version
        pass

    scheme, netloc, path, query, fragment = urlsplit(URI)
    if not scheme:
        URI = "https://" + URI
        pass
    
    scheme, netloc, path, query, fragment = urlsplit(URI)
    if scheme == "https":
        host,port = splitport(netloc)
        if not port:
            netloc = netloc + ":443"
            URI = urlunsplit((scheme, netloc, path, query, fragment));
            pass
        pass
    
    ctx = SSL.Context("sslv23")
    ctx.load_cert(CERTIFICATE, CERTIFICATE, PassPhraseCB)
    ctx.set_verify(SSL.verify_none, 16)
    ctx.set_allow_unknown_ca(0)
    
    # Get a handle on the server,
    server = xmlrpclib.ServerProxy(URI, SSL_Transport(ctx), verbose=0)
        
    # Get a pointer to the function we want to invoke.
    meth      = getattr(server, method)
    meth_args = [ params ]

    if response_handler:
        # If a response handler was passed, use it and return the result.
        # This is the case when running the GENI AM.
        return response_handler(meth, params)

    #
    # Make the call. 
    #
    while True:
        try:
            response = apply(meth, meth_args)
            break
        except xmlrpclib.Fault, e:
            if not quiet: print >> sys.stderr, e.faultString
            if e.faultCode == 503:
                print >> sys.stderr, "Will try again in a moment. Be patient!"
                time.sleep(5.0)
                continue
                pass
            return (-1, None)
        except xmlrpclib.ProtocolError, e:
            if not quiet: print >> sys.stderr, e.errmsg
            return (-1, None)
        except M2Crypto.SSL.Checker.WrongHost, e:
            if not quiet:
                print >> sys.stderr, "Warning: certificate host name mismatch."
                print >> sys.stderr, "Please consult:"
                print >> sys.stderr, "    http://www.protogeni.net/trac/protogeni/wiki/HostNameMismatch"            
                print >> sys.stderr, "for recommended solutions."
                print >> sys.stderr, e
                pass
            return (-1, None)
        pass

    #
    # Parse the Response, which is a Dictionary. See EmulabResponse in the
    # emulabclient.py module. The XML standard converts classes to a plain
    # Dictionary, hence the code below. 
    # 
    if response[ "code" ] and len(response["output"]):
        if not quiet: print >> sys.stderr, response["output"] + ":",
        pass

    rval = response["code"]

    #
    # If the code indicates failure, look for a "value". Use that as the
    # return value instead of the code. 
    # 
    if rval:
        if response["value"]:
            rval = response["value"]
            pass
        pass
    return (rval, response)

for opt, arg in opts:
    if opt in ( "-c", "--credentials" ):
        selfcredentialfile = arg
    elif opt in ( "-u", "--authority" ):
        AUTHORITY = arg
        print "authority=%s"%(AUTHORITY)
    elif opt in ( "-t", "--type" ):
        TYPE = arg
        params["type"] = TYPE
        print "[p] type=%s"%(TYPE)
    elif opt in ( "-e", "--credential" ):
        #f = open(arg, "r")
        #CREDENTIAL = f.read() 
        rval,response = do_method("sa", "GetCredential", {}) 
        CREDENTIAL = response["value"]
        params["credential"] = CREDENTIAL 
        print "[p] credential=%s"%(CREDENTIAL)
        print "rval=%s"%(rval)
    elif opt in ( "-o", "--command" ):
        COMMAND = arg
        print "command=%s"%(COMMAND)
    elif opt in ( "-w", "--passphrase" ):
        PASSPHRASE = str(arg)
        print "passphrase=%s"%(PASSPHRASE)
    elif opt in ( "-i", "--outputfile" ):
        OUTPUTFILE = str(arg)
        print "OUTPUTFILE=%s-"%(OUTPUTFILE)
    elif opt in ( "-v", "--validtime" ):
        print "valid=%s"%(arg)
        VALID = str(arg)
        params["expiration"] = VALID 
        print "[p] VALID=%s"%(VALID)
    elif opt in ( "-l", "--sliceurn" ):
		SLURN = str(arg)
		params["urn"] = SLURN 
		print "[p] SLURN=%s"%(SLURN)    
    elif opt in ( "-b", "--impotent" ):
        #IMPOTENT = str(arg)
        #params["impotent"] = IMPOTENT
        #print "[p] IMPOTENT=%s"%(IMPOTENT)
        DELETE = 1
        print "DELETE=%s"%(DELETE)
    elif opt in ( "-q", "--register" ):
        REGISTER=1
        print "REGISTER=%s"%(REGISTER)
    elif opt in ( "-x", "--register" ):
        REDEEM=1
        print "REDEEM=%s"%(REDEEM)
    elif opt in ( "-y", "--register" ):
        STARTSLICE=1
        print "STARTSLICE=%s"%(STARTSLICE)
    elif opt in ( "-z", "--register" ):
        STATUS=1
        print "STATUS=%s"%(STATUS)
    elif opt in ( "-g", "--rspec" ):
        rspecfile = open(arg)
        RSPEC = rspecfile.read()
        rspecfile.close()
        #params["rspec"] = RSPEC
        print "[p] RSPEC=%s"%(RSPEC)
    elif opt in ( "-j", "--credentials" ):
        credentials = open(arg)
        CREDENTIALS = credentials.read()
        credentials.close()
        params["credentials"] = (CREDENTIALS,) 
        print "[p] CREDENTIALS=%s"%(CREDENTIALS)
    elif opt in ( "-k", "--slice_urn" ):
        SLICEURN = str(arg)
        params["slice_urn"] = SLICEURN 
        print "[p] SLICEURN=%s"%(SLICEURN)
    elif opt in ( "-d", "--debug" ):
        debug = 1
    elif opt in ( "-d", "--debug" ):
        debug = 1
    elif opt in ( "-d", "--debug" ):
        debug = 1
    elif opt in ( "--delete" ):
        DELETE = 1
    elif opt in ( "-f", "--certificate" ):
        CERTIFICATE = arg
        print "cert=%s"%(CERTIFICATE)
    elif opt in ( "-h", "--help" ):
        Usage()
        sys.exit( 0 )
    elif opt in ( "-n", "--slicename" ):
        SLICENAME = arg
        params["hrn"] = SLICENAME
        print "[p] slicename=%s"%(SLICENAME)
    elif opt in ( "-m", "--cm" ):
        CMURI = arg
        if CMURI[-2:] == "cm":
            CMURI = CMURI[:-3]
        elif CMURI[-4:] == "cmv2":
            CMURI = CMURI[:-5]
            pass
        pass
    elif opt in ( "-p", "--passphrase" ):
        PASSPHRASEFILE = arg
        print "password=%s"%(PASSPHRASEFILE) 
    elif opt in ( "-r", "--read-commands" ):
        EXTRACONF = arg
    elif opt in ( "-s", "--slicecredentials" ):
        slicecredentialfile = arg
        print "credential=%s"%(slicecredentialfile)
    elif opt in ( "-a", "--admincredentials" ):
        admincredentialfile = arg

#erase output file before calling do_method
os.system("rm out.txt")

debug           = 0
impotent        = 0
HOME            = os.environ["HOME"]

CONFIGFILE      = ".protogeni-config.py"
GLOBALCONF      = HOME + "/" + CONFIGFILE

if "PROTOGENI_CERTIFICATE" in os.environ:
    CERTIFICATE = os.environ[ "PROTOGENI_CERTIFICATE" ]
if "PROTOGENI_PASSPHRASE" in os.environ:
    PASSPHRASEFILE = os.environ[ "PROTOGENI_PASSPHRASE" ]

cert = X509.load_cert( CERTIFICATE )

# XMLRPC server: use www.emulab.net for the clearinghouse, and
# the issuer of the certificate we'll identify with for everything else
XMLRPC_SERVER   = { "ch" : "www.emulab.net",
                    "default" : cert.get_issuer().CN }
SERVER_PATH     = { "default" : ":443/protogeni/xmlrpc/" }

if os.path.exists( GLOBALCONF ):
    execfile( GLOBALCONF )
if os.path.exists( LOCALCONF ):
    execfile( LOCALCONF )
if EXTRACONF and os.path.exists( EXTRACONF ):
    execfile( EXTRACONF )

if "sa" in XMLRPC_SERVER:
    HOSTNAME = XMLRPC_SERVER[ "sa" ]
else:
    HOSTNAME = XMLRPC_SERVER[ "default" ]
DOMAIN   = HOSTNAME[HOSTNAME.find('.')+1:]
SLICEURN = "urn:publicid:IDN+" + DOMAIN + "+slice+" + SLICENAME

#make the call to do_method
#print "here 1 au=%s cmd=%s urn=%s"%(AUTHORITY, COMMAND, params['urn'])

if REGISTER==1:
   rval,response = do_method(AUTHORITY, COMMAND, params)   
   params2 = {}
   params2["slice_urn"]   = params["urn"] 
   params2["credentials"] = (response["value"],)
   params2["rspec"]       = RSPEC   
   params2["impotent"]    = 0
   
   print "1 slice_urn=%s---"%(params2["slice_urn"])
   print "1 rspec=%s---"%(params2["rspec"])
   print "1 impotent=%s---"%(params2["impotent"])
   print "1 CREDENTIALS=%s---"%(params2["credentials"])

   rval,response = do_method("cm", "GetTicket", params2, version="2.0")
elif DELETE==1:
   params1 = {}
   params1["credential"] = CREDENTIAL 
   params1["type"]       = "Slice"
   params1["hrn"]        = SLICENAME
   #Look up slice
   rval,response = do_method("sa", "Resolve", params1)
   print "--------looking up slice in DELETE %s"%(rval)
   myslice = response["value"]
   
   #Get slice credential
   params2 = {}
   params2["credential"] = CREDENTIAL 
   params2["type"]       = "Slice"   
   params2["urn"]        = SLURN
   rval,response = do_method("sa", "GetCredential", params2)
   print "---------getting slice credential in DELETE %s"%(rval)
   slicecred = response["value"]
   
   #Let's delete the slice now
   params3 = {}
   params3["credentials"] = (slicecred,)
   params3["slice_urn"]   = SLURN
   rval,response = do_method("cm", "DeleteSlice", params3, version="2.0")
   print "---------deleting slice in DELETE rval=%s slurn=%s"%(rval, SLURN)
   
elif REDEEM==1:
   params1 = {}
   params1["credential"] = CREDENTIAL 
   params1["type"]       = "Slice"
   params1["hrn"]        = SLICENAME
   rval,response = do_method("sa", "Resolve", params1)
   myslice = response["value"]
   my_resolved_slice = myslice  
 
   params2 = {}
   params2["credential"] = CREDENTIAL 
   params2["type"]       = "Slice"   
   params2["urn"]        = SLURN
   rval,response = do_method("sa", "GetCredential", params2)
   slicecred = response["value"] 

   params3 = {}
   params3["credentials"] = (slicecred,)
   params3["urn"]         = myslice["urn"] 
   #print "HERE 3 crdentials=%s \n urn=%s"%(params3["credentials"], params3["urn"])
   rval,response = do_method("cm", "Resolve", params3, version="2.0")
   myslice = response["value"]

   params4 = {}
   params4["credentials"] = (slicecred,)
   params4["urn"]         = myslice["ticket_urn"]
   rval,response = do_method("cm", "Resolve", params4, version="2.0")
   ticket = response["value"]
   redeemcred = slicecred;
 
   params5 = {}
   params5["slice_urn"] = SLURN
   params5["credentials"] = (slicecred,)
   #print "HERE 5, slice_urn=%s credentials=%s"%(params5["slice_urn"], params5["credentials"])
   rval,response = do_method("cm", "GetSliver", params5, version="2.0")

   params = {}
   params["credential"] = CREDENTIAL 
   rval,response = do_method("sa", "GetKeys", params)
   if rval:
      Fatal("Could not get my keys")
      pass
   mykeys = response["value"]

   params6 = {}
   params6["credentials"] = (redeemcred,)
   params6["ticket"]      = ticket
   params6["slice_urn"]   = SLURN
   params6["keys"]        = mykeys
   #print "HERE LAST: crecentials=%s"%(params6["credentials"])
   rval,response = do_method("cm", "RedeemTicket", params6, version="2.0")
   if rval:
      Fatal("Could not redeem the ticket")
      pass
   #(sliver, manifest) = response["value"]
   #print "Created the sliver"
   #print str(manifest)

elif STARTSLICE==1:
   #Start slice

   params2 = {}
   params2["credential"] = CREDENTIAL
   params2["type"]       = "Slice"
   params2["hrn"]        = SLICENAME
   print "HERE AT LAST, cre=%s--- type=%s--- urn=%s---"%(params2["credential"], params2["type"], params2["hrn"])
   rval,response = do_method("sa", "Resolve", params2)
   myslice = response["value"]

   params = {}
   params["credential"] = CREDENTIAL
   params["type"]       = "Slice"
   params["urn"]        = myslice["urn"]    
   rval,response = do_method("sa", "GetCredential", params) 
   slicecred = response["value"]

   params = {}
   params["credentials"] = (slicecred,)
   params["slice_urn"]   = SLURN
   rval,response = do_method("cm", "GetSliver", params, version="2.0")
   slivercred = response["value"]  
  
   params = {}
   params["credentials"] = (slivercred,)
   params["slice_urn"]   = SLURN
   rval,response = do_method("cm", "StartSliver", params, version="2.0")

elif STATUS==1:
   #Poll for sliver status
   params2 = {}
   params2["credential"] = CREDENTIAL
   params2["type"]       = "Slice"
   params2["hrn"]        = SLICENAME
   print "HERE AT LAST, cre=%s--- type=%s--- urn=%s---"%(params2["credential"], params2["type"], params2["hrn"])
   rval,response = do_method("sa", "Resolve", params2)
   myslice = response["value"]   

   params = {}
   params["credential"] = CREDENTIAL
   params["type"]       = "Slice"
   params["urn"]        = myslice["urn"]
   rval,response = do_method("sa", "GetCredential", params)
   slicecred = response["value"]

   params = {}
   params["credentials"] = (slicecred,)
   params["slice_urn"]   = SLURN
   rval,response = do_method("cm", "GetSliver", params, version="2.0")
   slivercred = response["value"]

   params = {}
   params["credentials"] = (slivercred,)
   params["slice_urn"]   = SLURN
   rval,response = do_method("cm", "SliverStatus", params, version="2.0")
 
else:
   print "calling 2..."
   rval,response = do_method(AUTHORITY, COMMAND, params)

print "here 2"

#print output to a file
#OUTPUTFILE="/primoGENI/newscripts/out.txt"
print "out=%s"%(str(OUTPUTFILE))
f = open(OUTPUTFILE, 'w')
f.write(str(rval) + "END\n")
f.write(str(response["value"]))

#print "rval:%s\n"%(rval)
#print "output:%s"%(response)

