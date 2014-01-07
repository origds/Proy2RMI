import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;

import Clases.*;

import java.util.ArrayList;
import java.util.Iterator;

public class s_rmifs{
  static int puertolocal, puertoaut;
  static String host;

  public s_rmifs (int puertolocal){
    try{
      LocateRegistry.createRegistry(puertolocal);
      Solicitud s = new SolicitudImpl();
      Naming.rebind("rmi://127.0.0.1:"+puertolocal+"/ArchivosService", s);
    }
    catch(Exception e){
      System.out.println("Error: " + e);
    }
  }  

  public static void menuServer(String [] param) { 
    puertolocal = 0;
    puertoaut = 0;

    if (param.length == 6) {
    
      if (param[0].equals("-l")) {
        puertolocal = Integer.parseInt(param[1]);
        if (param[2].equals("-h")) {
          host = param[3];
          if (param[4].equals("-r")) {
            puertoaut = Integer.parseInt(param[5]);    
          } else {
            System.out.println("Error de sintaxis: s_rmifs -l puertolocal -h host -r puerto");
            System.exit(0);
          }
        } else if (param[2].equals("-r")) {
          puertoaut = Integer.parseInt(param[3]);
          if (param[4].equals("-h")) {
            host = param[5];    
          } else {
            System.out.println("Error de sintaxis: s_rmifs -l puertolocal -h host -r puerto");
            System.exit(0);
          }
        } else {
          System.out.println("Error de sintaxis: s_rmifs -l puertolocal -h host -r puerto");
          System.exit(0);
        }
      } else if (param[0].equals("-h")) {
        host = param[1];
        if (param[2].equals("-l")) {
          puertolocal = Integer.parseInt(param[3]);
          if (param[4].equals("-r")) {
            puertoaut = Integer.parseInt(param[5]);    
          } else {
            System.out.println("Error de sintaxis: s_rmifs -l puertolocal -h host -r puerto");
            System.exit(0);
          }
        } else if (param[2].equals("-r")) {
          puertoaut = Integer.parseInt(param[3]);
          if (param[4].equals("-l")) {
            puertolocal = Integer.parseInt(param[5]);    
          } else {
            System.out.println("Error de sintaxis: s_rmifs -l puertolocal -h host -r puerto");
            System.exit(0);
          }
        } else {
          System.out.println("Error de sintaxis: s_rmifs -l puertolocal -h host -r puerto");
          System.exit(0);
        }
      } else if (param[0].equals("-r")) {
        puertoaut = Integer.parseInt(param[1]);
        if (param[2].equals("-l")) {
          puertolocal = Integer.parseInt(param[3]);
          if (param[4].equals("-h")) {
            host = param[5];    
          } else {
            System.out.println("Error de sintaxis: s_rmifs -l puertolocal -h host -r puerto");
            System.exit(0);
          }
        } else if (param[2].equals("-h")) {
          host = param[3];
          if (param[4].equals("-l")) {
            puertolocal = Integer.parseInt(param[5]);
          } else {
            System.out.println("Error de sintaxis: s_rmifs -l puertolocal -h host -r puerto");
            System.exit(0);
          }
        } else {
          System.out.println("Error de sintaxis: s_rmifs -l puertolocal -h host -r puerto");
          System.exit(0);
        }
      }
    
    } else {
    
        System.out.println("Error de sintaxis: s_rmifs -l puertolocal -h host -r puerto");
        System.exit(0);
    
    }

    System.out.println("Puerto Local: "+puertolocal+"\n");
    System.out.println("Host: "+host+"\n");
    System.out.println("Puerto: "+puertoaut+"\n");
  }

  public static void main(String [] args){
    ArrayList<Usuario> usr = new ArrayList<Usuario>();
    Usuario u;

    menuServer(args);

    try{
      Autenticador a = (Autenticador)
      Naming.lookup("rmi://127.0.0.1:21131/AutenticadorService");

      usr = a.getUsuarios();
      Iterator<Usuario> iterador = usr.iterator();

     while(iterador.hasNext()){
      u = iterador.next();
      System.out.println("Encontrado: "+u.getUsuario()+" "+u.getContrasena());
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
    catch (
      java.lang.ArithmeticException
      ae) {
      System.out.println();
      System.out.println(
       "java.lang.ArithmeticException");
      System.out.println(ae);
    }
  }
}