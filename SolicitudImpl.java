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

  public SolicitudImpl() 
  throws java.rmi.RemoteException {
    super();
  }

  public static void setPuerto(int p){
    puerto = p;
  }

  public static void setHost(String h){
    host = h;
  }

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

  public void sal(Usuario u)
  throws java.rmi.RemoteException {
    registrarEnLog(u,"sal",0);
  }

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