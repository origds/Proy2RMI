import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import Clases.*;

import java.util.ArrayList;
import java.io.*;

public class s_rmifs{
  static int puertolocal, puertoaut;
  static String host;

  /* Funcion para correr el servidor */
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

  /* Funciones para manejar el menu */
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

  /*Funcion Main*/
  
  public static void main(String [] args)
  throws java.rmi.RemoteException{

    menuServer(args);

    System.out.println("Puerto Local: "+puertolocal+"\n");
    System.out.println("Host: "+host+"\n");
    System.out.println("Puerto: "+puertoaut+"\n");

    SolicitudImpl sol = new SolicitudImpl();
    sol.setPuerto(puertoaut);
    sol.setHost(host);

    new s_rmifs(puertolocal);

    pedirCmdConsola();

  }
}