/**
* s_rmifs.java
* Archivo donde se implementa la clase para el funcionamiento del servidor de archivos
* @autor Oriana Gomez e Ivan Travecedo
* @version 1.0
**/

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import Clases.*;

import java.io.*;

public class s_rmifs{

  static int puertolocal, puertoaut;
  static String host;

  /**
  * s_rmifs. Funcion que permite correr el servidor de archivos
  * @param puerto: numero de puerto al que se le asociara el servicio de archivos
  **/
  public s_rmifs (int puerto){
    try{
      LocateRegistry.createRegistry(puerto);
      Solicitud s = new SolicitudImpl();
      Naming.rebind("rmi://127.0.0.1:"+puerto+"/ArchivosService", s);
    }
    catch(Exception e){
      System.out.println("Error: " + e);
    }
  }

  /**
  * procesarComandosConsola. Funcion que evalua cada comando ingresado por consola para ejecutarlo
  **/
  private static Log procesarComandosConsola()
  throws IOException {

    BufferedReader br = null;
    String linea;
    SolicitudImpl sol = new SolicitudImpl();

    try{
      br = new BufferedReader(new InputStreamReader(System.in));

      while(true){
        System.out.print("\nIngrese un comando: ");
        linea = br.readLine();
  
        if (linea.equals("log")) {
          sol.printLog();
        } else if (linea.equals("sal")) {
          System.out.println("\nHa salido correctamente. Hasta luego");
          System.exit(0);
        } else
          System.out.println("\nDebe introducir un comando valido\n"); 
     }
    }
    catch(Exception e){
      System.out.println("Error leyendo comandos: " + e);
    }
    return null;
  }

  /**
  * pedirCmdConsola. Funcion que pide infinitamente comandos al usuario por consola hasta que ejecute sal
  **/
  private static void pedirCmdConsola() {
    Log cmd;

    try {
      
      cmd = procesarComandosConsola();
      while(!cmd.getAccion().equals("sal")){
        cmd = procesarComandosConsola();
      }
      System.out.println("\nHa salido correctamente. Hasta luego");

    }
    catch(Exception e){
      System.out.println("Error leyendo comandos: " + e);
    }
  }

  /**
  * menuServer. Funcion que maneja el menu presentado al usuario del servidor del archivos
  * @param param: arreglo que toma los valores ingresados por consola como argumento al correr el programa
  **/
  public static void menuServer(String [] param) { 
    boolean l,h,r;
    l = false;
    h = false;
    r = false;
    puertolocal = 0; // puerto local donde corre servidor de archivos
    puertoaut = 0; // puerto donde corre servidor de autenticacion
    host = "";

    for ( int i = 0 ; i < (param.length)-1 ; i++ ) {

      if (param[i].equals("-l") && !l) {
          puertolocal = Integer.parseInt(param[i+1]);
          l = true;
      } else if (param[i].equals("-h") && !h) {
          host = param[i+1];
          h = true;
      } else if (param[i].equals("-r") && !r) {
          puertoaut = Integer.parseInt(param[i+1]);
          r = true;
      } else {
          System.out.println("Error de sintaxis: s_rmifs -l puertolocal -h host -r puerto");
          System.exit(1); 
      }

      i = i+1;
    }

    if ((!l) || (!h) || (!r))  {
      System.out.println("Error de sintaxis: s_rmifs -l puertolocal -h host -r puerto");
      System.exit(1); 
    }

  }

  /**
  * main. Funcion que maneja las acciones del servidor de archivos
  * @param args: lista de argumentos obtenidas por consola al correr el programa
  **/
  public static void main(String [] args)
  throws java.rmi.RemoteException{

    menuServer(args);

    System.out.println("Puerto Local: "+puertolocal+"\n");
    System.out.println("Host: "+host+"\n");
    System.out.println("Puerto: "+puertoaut+"\n");

    SolicitudImpl.setPuerto(puertoaut);
    SolicitudImpl.setHost(host);


    new s_rmifs(puertolocal);

    pedirCmdConsola();

  }
}