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

  private void registrarEnLog (Usuario u, String accion, int n){
    Log nuevo = new Log(u.getUsuario(),accion);
    if(n==0){
      if (logCmd.size()==20)
        logCmd.remove(0);
      logCmd.add(nuevo);
    }
    else{
      logProp.add(nuevo);
    }
  }

  public void registrar(ArrayList<Usuario> usr)
  throws java.rmi.RemoteException {
    try{
      Autenticador auten = (Autenticador)
      Naming.lookup("rmi://"+host+":"+puerto+"/AutenticadorService");
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

    try{
      Autenticador auten = (Autenticador)
      Naming.lookup("rmi://"+host+":"+puerto+"/AutenticadorService");

      if (auten.autenticado(u)){
        for (int i = 0; i < listaArchivos.length; i++){
          if (listaArchivos[i].isFile())
            archivos.add(listaArchivos[i].getName());
        }

        registrarEnLog(u,"rls",0);

        return archivos;
      }
    }
    catch (MalformedURLException murle) {
      System.out.println();
      System.out.println(
        "MalformedURLException");
      System.out.println(murle);
    }
    catch (RemoteException re) {
      System.out.println();
      System.out.println(
        "RemoteException");
      System.out.println(re);
    }
    catch (NotBoundException nbe) {
      System.out.println();
      System.out.println(
       "NotBoundException");
      System.out.println(nbe);
    }
    return null;    
  }

  public Boolean sub(Usuario u, byte[] archivo, String nombreArchivo)
  throws java.rmi.RemoteException {

    if (existeArchivo(nombreArchivo))
      return false;

    try{
      File file = new File(nombreArchivo);
      BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file.getName()));
        
      output.write(archivo,0,archivo.length);
      output.flush();
      output.close();

      registrarEnLog(u,"sub",0);
      registrarEnLog(u,nombreArchivo,1);

      return true;

    } catch(Exception e) {
      System.err.println("Error subiendo archivo: "+ e.getMessage());
      e.printStackTrace();
    }
    return false;
  }

  public byte[] baj(Usuario u , String nombreArchivo)
  throws java.rmi.RemoteException {
    try{
      File file = new File(nombreArchivo);
      byte buffer[] = new byte[(int)file.length()];
 
      BufferedInputStream input = new BufferedInputStream(new FileInputStream(file.getName()));
 
      input.read(buffer,0,buffer.length);
      input.close();

      registrarEnLog(u,"baj",0);

      return buffer;

    } catch(Exception e){
      System.out.println("Error bajando archivo: "+e.getMessage());
      e.printStackTrace();

      return null;
    }


  }

  public Boolean bor(Usuario u, String nombreArchivo)
  throws java.rmi.RemoteException {
    if (existeArchivo(nombreArchivo) && esPropietario(u,nombreArchivo)){
      File archivo = new File(nombreArchivo);
      registrarEnLog(u,"bor",0);
      return archivo.delete();
    }
    return false;
  }

  public void sal(Usuario u)
  throws java.rmi.RemoteException {
    registrarEnLog(u,"sal",0);
  }

}