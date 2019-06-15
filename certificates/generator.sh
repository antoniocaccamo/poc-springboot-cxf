#!/usr/bin/env bash

password=Passw0rd!

server_alias=cxf-prime-server

nclient=2
client_alias_prefix=cxf-prime-client-

echo removing previous..

rm -fr client
rm -fr server

echo creating server keystore

mkdir server

keytool -genkeypair -alias $server_alias -keyalg RSA -keysize 2048 -validity 3650 \
        -dname "CN=$server_alias, OU=SecOps, O=antoniocaccamo.me, ST=Italy, C=IT" \
        -keystore server/$server_alias.keystore -keypass $password -storepass $password

keytool -exportcert -alias $server_alias -rfc -file server/$server_alias.cer\
        -keystore server/$server_alias.keystore -keypass $password -storepass $password



mkdir client

for idx in $(seq 1 $nclient);
do
    echo creating client keystore : $idx

    keytool -genkeypair -alias $client_alias_prefix$idx -keyalg RSA -keysize 2048 -validity 3650 \
        -dname "CN=$client_alias_prefix$idx, OU=SecOps, O=antoniocaccamo.me, ST=Italy, C=IT" \
        -keystore client/$client_alias_prefix$idx.keystore -keypass $password -storepass $password

    keytool -exportcert -alias $client_alias_prefix$idx  -rfc -file client/$client_alias_prefix$idx.cer \
        -keystore client/$client_alias_prefix$idx.keystore -keypass $password -storepass $password

    echo importing server public cert in my keystore

    keytool -importcert -trustcacerts -noprompt -alias $server_alias -file server/$server_alias.cer \
        -keystore client/$client_alias_prefix$idx.keystore -keypass $password -storepass $password

    echo importing my public cert into server keystore

    keytool -importcert -trustcacerts -noprompt -alias $client_alias_prefix$idx  -file client/$client_alias_prefix$idx.cer \
        -keystore server/$server_alias.keystore -keypass $password -storepass $password

done

# echo ">>> ----------------------------------------------------------------- <<<"
#
# echo server/$server_alias.keystore list
#
# keytool -list -v -keystore server/$server_alias.keystore -keypass $password -storepass $password
#
# for idx in $(seq 1 $nclient);
# do
#     echo client/$client_alias_prefix$idx.keystore list
#     keytool -list -v -keystore client/$client_alias_prefix$idx.keystore -keypass $password -storepass $password
# done
# echo ">>> ----------------------------------------------------------------- <<<"