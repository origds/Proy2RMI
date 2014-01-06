import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;

import Clases.*;

import java.util.ArrayList;
import java.util.Iterator;


public class s_rmifs{

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

  public static void main(String [] args){
    ArrayList<Usuario> usr = new ArrayList<Usuario>();
    Usuario u;
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