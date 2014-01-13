/**
* SolicitudImpl.java
* Archivo donde se implementan las funciones de la interfaz Solicitud
* @autor Oriana Gomez e Ivan Travecedo
* @version 1.0
**/

import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;

import Clases.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class SolicitudImpl
extends
java.rmi.server.UnicastRemoteObject
implements Solicitud {

  public static ArrayList<Log> logCmd = new ArrayList<Log>(); // 0 en registrarnEnLog
  public static ArrayList<Log> logProp = new ArrayList<Log>(); // 1 en registrarnEnLog

  public static int puerto = 0;
  public static String host = "";

  public static final long serialVersionUID = 2L;

  /**
  * SolicitudImpl. Constructor de la clase SolicitudImpl
  **/
  public SolicitudImpl() 
  throws java.rmi.RemoteException {
    super();
  }

  /**
  * setPuerto. Funcion que asigna un valor a la variable que representa el puerto donde
  * correra el servidor de autenticacion
  * @param p: valor que se asignara a la variable puerto
  **/
  public static void setPuerto(int p){
    puerto = p;
  }

  /**
  * setHost. Funcion que asigna un valor a la variable que representa el IP donde correra 
  * el servidor de autenticacion
  * @param h: valor que se asignara a la variable host
  **/
  public static void setHost(String h){
    host = h;
  }

  /**
  * esPropietario. Funcion que verifica si un archivo subido al servidor pertenece al usuario dado
  * @param u: Usuario usado para ver si es propietario del archivo
  * @param nombreArchivo: Nombre del archivo para ver si el usuario es propietario del mismo
  * @return devuelve si el usuario es propietario de ese archivo o no
  **/
  private Boolean esPropietario(Usuario u, String nombreArchivo){
    Log log = new Log();

    Iterator<Log> iterador = logProp.iterator();
    while(iterador.hasNext()){
      log = iterador.next();
      if(log.getAccion().equals(u.getUsuario()) && 
         log.getRegistro().equals(nombreArchivo))
        return true;
    }
    return false;
  }

  /**
  * existeArchivo. Funcion que verifica si un archivo ya existe en el servidor de archivos
  * @param nombreArchivo: Nombre del archivo del que se desea saber si existe
  * @return devuelve si el archivo dado existe o no en el servidor de archivos
  **/
  private Boolean existeArchivo(String nombreArchivo){
    File carpeta = new File("./");
    File [] listaArchivos = carpeta.listFiles();

    for (int i = 0; i < listaArchivos.length; i++){
      if (listaArchivos[i].isFile() && 
          nombreArchivo.equals(listaArchivos[i].getName())){
          return true;
      }
    }
    return false;
  }

  /**
  * registrarEnLog. Funcion que registra en el ArrayList logCmd o logProp cada log o propietario
  * nuevo generado en el servidor de archivos. 
  * @param u: Usuario que realizo la accion a registrar en el log
  * @param accion: accion realizada por el usuario
  * @param n: indica en que ArrayList se guardara el log (si es 0 se guarda en el log de comandos
  * si es 1 se guarda en la lista de propietarios)
  **/
  private void registrarEnLog (Usuario u, String accion, int n){
    Log nuevo = new Log(u.getUsuario(),accion);
    if(n==0){
      if (logCmd.size()==20)
        logCmd.remove(0);
      logCmd.add(nuevo);
    }
    else {
      logProp.add(nuevo);
    }
  }

  /**
  * printLog. Funcion que permite imprimir el ArrayList de Log de Comandos con formato 
  **/
  public void printLog() {
    Iterator<Log> iterador = logCmd.iterator();
    Log log;
    int i = 1;

    System.out.println("\nLog:\n");
      while(iterador.hasNext()){
        log = iterador.next();
        System.out.println(i+") Usuario: "+log.getAccion()+" Comando: "+log.getRegistro());
        i++;
      }
  }

  /**
  * rls. Funcion que retorna todos los archivos contenidos en la carpeta local del servidor remoto
  * @param u: usuario que ejecuta el comando rls en el programa
  * @return devuelve la lista de nombres de archivos contenidos en la carpeta local del serv remoto
  **/
  public ArrayList<String> rls(Usuario u)
  throws java.rmi.RemoteException {
    ArrayList<String> archivos = new ArrayList<String>();
    File carpeta = new File("./");
    File [] listaArchivos = carpeta.listFiles();

    for (int i = 0; i < listaArchivos.length; i++){
      if (listaArchivos[i].isFile())
        archivos.add(listaArchivos[i].getName());
    }

    registrarEnLog(u,"rls",0);

    return archivos;
      
  }

  /**
  * sub. Funcion que sube un archivo de la carpeta local del cliente al servidor de archivos remoto
  * @param u: usuario que desea subir el archivo
  * @param archivo: arreglo de bytes que representan al archivo a subir
  * @param nombreArchivo: nombre del archivo que va a subirse al servidor
  * @return devuelve si el archivo pudo subirse o no
  **/
  public Boolean sub(Usuario u, byte[] archivo, String nombreArchivo)
  throws java.rmi.RemoteException {

    try{
      if (existeArchivo(nombreArchivo) || archivo==null) {
        registrarEnLog(u,"sub",0);
        return false;
      }

      File arch = new File(nombreArchivo);
      BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(arch.getName()));
        
      out.write(archivo,0,archivo.length);
      out.flush();
      out.close();

      registrarEnLog(u,"sub",0);
      registrarEnLog(u,nombreArchivo,1);

      return true;
    }
    catch(Exception e){
      System.out.println("Error subiendo archivo: "+e.getMessage());
      e.printStackTrace();
    }
    return false;
  }

  /**
  * baj. Funcion que baja un archivo del servidor de archivos remoto a la carpeta local del cliente
  * @param u: usuario que desea bajar el archivo
  * @param nombreArchivo: nombre del archivo que va a bajarse del servidor
  * @return devuelve si el archivo pudo bajarseo no
  **/
  public byte[] baj(Usuario u , String nombreArchivo)
  throws java.rmi.RemoteException {
    try{
      if(existeArchivo(nombreArchivo)){
        File archivo = new File(nombreArchivo);
        byte buffer[] = new byte[(int)archivo.length()];
   
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(archivo.getName()));
   
        in.read(buffer,0,buffer.length);
        in.close();

        registrarEnLog(u,"baj",0);

        return buffer;
      }
      registrarEnLog(u,"baj",0);
      return null;
    } 
    catch(Exception e){
      registrarEnLog(u,"baj",0);
      System.out.println("Error bajando archivo: "+e.getMessage());
      e.printStackTrace();
    }
    return null;
  }

  /**
  * bor. Funcion que borra un archivo que esta en la carpeta local del servidor de archivos
  * siempre que el usuario a borrarlo sea su propietario
  * @param u: usuario que desea borrar el archivo
  * @param nombreArchivo: nombre del archivo que va a ser borrado
  * @return devuelve si el archivo pudo borrarse o no
  **/
  public Boolean bor(Usuario u, String nombreArchivo)
  throws java.rmi.RemoteException {
    if (existeArchivo(nombreArchivo) && esPropietario(u,nombreArchivo)){
      File archivo = new File(nombreArchivo);
      registrarEnLog(u,"bor",0);
      return archivo.delete();
    }
    registrarEnLog(u,"bor",0);
    return false;
  }

  /**
  * sal. Funcion que permite al usuario salir del sistema y lo registra en log
  * @param u: usuario que desea salir
  **/
  public void sal(Usuario u)
  throws java.rmi.RemoteException {
    registrarEnLog(u,"sal",0);
  }

  /**
  * regustrado. Funcion que permite al servidor de archivos saber si un usuario esta autenticado
  * @param u: usuario a autenticar
  * @return devuelve si el usuario esta autenticado o no
  **/
  public Boolean registrado(Usuario u)
  throws java.rmi.RemoteException {

    try {
      Autenticador auten = (Autenticador)
      Naming.lookup("rmi://"+host+":"+puerto+"/AutenticadorService");
      return auten.autenticado(u); 
    }
    catch (NotBoundException nbe) {
      System.out.println();
      System.out.println(
       "NotBoundException");
      System.out.println(nbe);
    }
    catch (MalformedURLException murle) {
      System.out.println();
      System.out.println(
        "MalformedURLException");
      System.out.println(murle);
    }
    return null;
  }
}