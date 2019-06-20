# Spring Boot CXF

- cxf-codegen-plugin wsdl2java
- spring boot cxf server
- spring boot cxf client
- ssl config on both
- WSS4J X.509 Token Profile ( timestamp, signature and encryption )
- WS Policy

## Steps
1. execute [keystores generator script](certificates/generator.sh)
2. install [cxf-prime-common](cxf-prime-common) into local maven repo
3. launch [cxf-prime-server-jaxws](cxf-prime-server-jaxws) 
4. launch [cxf-prime-client](cxf-prime-client) 
