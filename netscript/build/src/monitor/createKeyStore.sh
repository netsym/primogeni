rm -f selfsignedcert.cer keystore.jks truststore.jks
keytool -genkeypair -alias certificatekey -keyalg RSA -validity 1024 -keystore keystore.jks -storepass foobar -keypass foobar -dname "CN=PrimoGeni, OU=FIU, O=EDU, L=Miami, S=FL, C=US"
#keytool -export -alias certificatekey -keystore keystore.jks -rfc -file selfsignedcert.cer -storepass foobar -keypass foobar
#keytool -import -alias certificatekey -file selfsignedcert.cer -keystore truststore.jks -storepass foobar -keypass foobar -noprompt
#rm selfsignedcert.cer
