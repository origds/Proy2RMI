# Archivo MAKEFILE
# Realizado por Oriana Gomez e Ivan Travecedo.
# Ultima modificacion: 12/01/14
# Para compilar ejecutar el comando 'make'.
# Para limpiar ejecutar el comando 'make clean'.

JCC = javac
RM = rm

default: Autenticador.class AutenticadorImpl.class Solicitud.class SolicitudImpl.class a_rmifs.class c_rmifs.class s_rmifs.class

Autenticador.class: Autenticador.java
	$(JCC) Autenticador.java

AutenticadorImpl.class: AutenticadorImpl.java
	$(JCC) AutenticadorImpl.java

Solicitud.class: Solicitud.java
	$(JCC) Solicitud.java

SolicitudImpl.class: SolicitudImpl.java
	$(JCC) SolicitudImpl.java

a_rmifs.class: a_rmifs.java
	$(JCC) a_rmifs.java

c_rmifs.class: c_rmifs.java
	$(JCC) c_rmifs.java

s_rmifs.class: s_rmifs.java
	$(JCC) s_rmifs.java

clean:
	$(RM) *.class
	$(RM) ./Clases/*.class