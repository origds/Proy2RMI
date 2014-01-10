import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
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
  private static ArrayList<Usuario> online = new ArrayList<Usuario>(); // usuarios conectados

  public static int puerto = 0;
  public static String host = "";

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

  private static Boolean estaConectado(Usuario u)
  throws java.rmi.RemoteException {
    try{
      Autenticador auten = (Autenticador)
      Naming.lookup("rmi://"+host+":"+puerto+"/AutenticadorService");
      if(online.contains(u)){
        return true;
      }
      else{
        if(auten.autenticado(u)){
          online.add(u);
          return true;
        }
        else{
          return false;
        }
      }
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

  public void registrar(ArrayList<Usuario> usr, Usuario u)
  throws java.rmi.RemoteException {
    try{
      Autenticador auten = (Autenticador)
      Naming.lookup("rmi://"+host+":"+puerto+"/AutenticadorService");      
      if(estaConectado(u))
        auten.setUsuarios(usr);
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
  }

  private Boolean esPropietario(Usuario u, String nombreArchivo){
    Log log = new Log();

    Iterator<Log> iterador = logProp.iterator();
    while(iterador.hasNext()){
      log = iterador.next();
      if(log.getUsuario().equals(u.getUsuario()) && 
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

  public ArrayList<String> rls(Usuario u)
  throws java.rmi.RemoteException {
    ArrayList<String> archivos = new ArrayList<String>();
    File carpeta = new File("./");
    File [] listaArchivos = carpeta.listFiles();

    System.out.println("Puerto auten: "+ puerto+ " host auten: "+host);

      if (estaConectado(u)){
        for (int i = 0; i < listaArchivos.length; i++){
          if (listaArchivos[i].isFile())
            archivos.add(listaArchivos[i].getName());
        }

        registrarEnLog(u,"rls",0);

        return archivos;
      }
    return null;
  }

  public Boolean sub(Usuario u, byte[] archivo, String nombreArchivo)
  throws java.rmi.RemoteException {

    try{
      if (existeArchivo(nombreArchivo))
        return false;
      if (estaConectado(u)){

        File file = new File(nombreArchivo);
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file.getName()));
          
        output.write(archivo,0,archivo.length);
        output.flush();
        output.close();

        registrarEnLog(u,"sub",0);
        registrarEnLog(u,nombreArchivo,1);

        return true;
      }
      else{
        return false;
      }
    }
    catch(Exception e){
      System.out.println("Error bajando archivo: "+e.getMessage());
      e.printStackTrace();
    }
    return false;
  }

  public byte[] baj(Usuario u , String nombreArchivo)
  throws java.rmi.RemoteException {
    try{
      if (estaConectado(u)){      
        File file = new File(nombreArchivo);
        byte buffer[] = new byte[(int)file.length()];
   
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file.getName()));
   
        input.read(buffer,0,buffer.length);
        input.close();

        registrarEnLog(u,"baj",0);

        return buffer;
      }
      else{
        return null;
      }
    } 
    catch(Exception e){
      System.out.println("Error bajando archivo: "+e.getMessage());
      e.printStackTrace();
    }
    return null;
  }

  public Boolean bor(Usuario u, String nombreArchivo)
  throws java.rmi.RemoteException {
    if(estaConectado(u)){
      if (existeArchivo(nombreArchivo) && esPropietario(u,nombreArchivo)){
        File archivo = new File(nombreArchivo);
        registrarEnLog(u,"bor",0);
        return archivo.delete();
      }
      return false;
    }
    return false;
  }

  public Boolean sal(Usuario u)
  throws java.rmi.RemoteException {
    registrarEnLog(u,"sal",0);
    return online.remove(u);
  }

  public Boolean registrado(Usuario u)
  throws java.rmi.RemoteException {
    return estaConectado(u);
  }
}