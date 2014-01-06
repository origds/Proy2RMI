import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;

import Clases.*;

public class AutenticadorServer {

  public AutenticadorServer (int puerto){
    try{
      LocateRegistry.createRegistry(puerto);
      Autenticador a = new AutenticadorImpl();
      Naming.rebind("rmi://127.0.0.1:"+puerto+"/AutenticadorService", a);
    }
    catch(Exception e){
      System.out.println("Error: " + e);
    }
  }

  private static ArrayList<Usuario> comparaListas(ArrayList<Usuario> nuevo, ArrayList<Usuario> viejo){
    Usuario u;
    Iterator<Usuario> iterador = nuevo.iterator();
    while(iterador.hasNext()){
      u = iterador.next();
      if (viejo.contains(u))
        nuevo.remove(u);
    }
    return nuevo;
  }


  private static int guardarUsuarios(ArrayList<Usuario> usr)
  throws java.rmi.RemoteException {
    Autenticador a = new AutenticadorImpl();
    ArrayList<Usuario> registrados = new ArrayList<Usuario>();
    registrados = a.getUsuarios();
    usr = comparaListas(usr,registrados);
    a.setUsuarios(usr);
    return 0;
  }

  private static void leerUsuariosEnArchivo(String arch){
    File archivo = null;
    FileReader fr = null;
    BufferedReader br = null;
    ArrayList<Usuario> users = new ArrayList<Usuario>();
    String [] parUserPass;
    String linea;
    try{
      archivo = new File (arch);
      fr = new FileReader (archivo);
      br = new BufferedReader(fr);
      while((linea=br.readLine())!=null){
        if(linea.equals("sal"))
          break;
        parUserPass = linea.split(":");
        Usuario u = new Usuario(parUserPass[0],parUserPass[1]);
        users.add(u);
      }
      guardarUsuarios(users);
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

  public static void main(String args[]){
    leerUsuariosEnArchivo(args[1]);
    new AutenticadorServer(21131);
  }
}
