/**
* a_rmifs.java
* Archivo donde se implementa la clase para el funcionamiento del servidor de autenticacion
* @autor Oriana Gomez e Ivan Travecedo
* @version 1.0
**/

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import java.io.*;

import Clases.*;

public class a_rmifs {

  /**
  * a_rmifs. Funcion que permite correr el servidor de autenticacion
  * @param puerto: numero de puerto al que se le asociara el servicio de autenticacion
  **/
  public a_rmifs (int puerto){
    try{
      LocateRegistry.createRegistry(puerto);
      Autenticador a = new AutenticadorImpl();
      Naming.rebind("rmi://127.0.0.1:"+puerto+"/AutenticadorService", a);
    }
    catch(Exception e){
      System.out.println("Error: " + e);
    }
  }

  /**
  * leerUsuariosEnArchivo. Funcion que lee de un archivo los usuarios a registrar en 
  * el servidor de autenticacion
  * @param arch: nombre del archivo donde se encuentra el login y contrasena de los 
  * usuarios a registrar
  **/
  private static void leerUsuariosEnArchivo(String arch)
  throws java.rmi.RemoteException {
    File archivo = null;
    FileReader fr = null;
    BufferedReader br = null;
    String [] parUserPass;
    String linea;

    Autenticador a = new AutenticadorImpl();

    try{
      archivo = new File (arch);
      fr = new FileReader (archivo);
      br = new BufferedReader(fr);
      while((linea=br.readLine())!=null){
        parUserPass = linea.split(":");
        Usuario u = new Usuario(parUserPass[0],parUserPass[1]);
        a.guardar(u);
      }
    }
    catch(Exception e){
      System.out.println("Error leyendo archivo: " + e);
    }
    finally {
     try{
      if(fr!=null)
        fr.close();
      }
      catch (Exception e2){
        System.out.println("Error cerrando archivo: " + e2);
      }
    }
  }

  /**
  * main. Funcion que maneja las acciones del servidor de autenticacion
  * @param args: lista de argumentos obtenidas por consola al correr el programa
  **/
  public static void main(String args[])
  throws java.rmi.RemoteException {
    
    int puerto = 0;
    boolean f,p;
    f = false;
    p = false;

   for ( int i = 0 ; i < (args.length)-1 ; i++ ) {

    if (args[i].equals("-f") && !f) {
        leerUsuariosEnArchivo(args[i+1]);
        f = true;
    } else if (args[i].equals("-p") && !p) {
        puerto = Integer.parseInt(args[i+1]);
        p = true;
    } else {
        System.out.println("Error de sintaxis: a_rmifs -f usuarios -p puerto");
        System.exit(1); 
    }

      i = i+1;
    }

    if ((!f) || (!p)) {
      System.out.println("Error de sintaxis: a_rmifs -f usuarios -p puerto");
      System.exit(1); 
    }
    
    System.out.println("Puerto: "+puerto+"\n");
    new a_rmifs(puerto);
  }
}
