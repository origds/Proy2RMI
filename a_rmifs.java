import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;

import Clases.*;

public class a_rmifs {

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

  public static void main(String args[])
  throws java.rmi.RemoteException {
    
    int puerto = 0;

    if (args.length == 4) {
    
      if (args[0].equals("-f")) {
        leerUsuariosEnArchivo(args[1]);
        if (args[2].equals("-p")) {
          puerto = Integer.parseInt(args[3]);
        } else {
          System.out.println("Error de sintaxis: a_rmifs -f usuarios -p puerto");
          System.exit(0);
        }
      } else if (args[0].equals("-p")) {
        puerto = Integer.parseInt(args[1]);
        if (args[2].equals("-f"))
          leerUsuariosEnArchivo(args[3]);
        else {
          System.out.println("Error de sintaxis: a_rmifs -f usuarios -p puerto");
          System.exit(0);
        }
      }
    
    } else {
    
        System.out.println("Error de sintaxis: a_rmifs -f usuarios -p puerto");
        System.exit(0);
    
    }
    
    System.out.println("Puerto: "+puerto+"\n");
    new a_rmifs(puerto);
  }
}
