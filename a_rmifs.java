import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import java.io.*;

import Clases.*;

public class a_rmifs {

  /* Funcion para correr el servidor */

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

  /* Funcion para registrar los usuarios en el servidor */
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

  /* Funcion Main */
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
