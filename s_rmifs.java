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

    System.out.println("Puerto Local: "+puertolocal+"\n");
    System.out.println("Host: "+host+"\n");
    System.out.println("Puerto: "+puertoaut+"\n");
  }

  public static void main(String [] args)
  throws java.rmi.RemoteException{
    ArrayList<Usuario> usr = new ArrayList<Usuario>();
    Usuario u;

    menuServer(args);
    SolicitudImpl sol = new SolicitudImpl();
    sol.setPuerto(puertoaut);
    sol.setHost(host);

      /*Autenticador a = (Autenticador)
      Naming.lookup("rmi://"+host+":"+puertoaut+"/AutenticadorService");*/

      /*usr = a.getUsuarios();
      Iterator<Usuario> iterador = usr.iterator();

     while(iterador.hasNext()){
      u = iterador.next();
      System.out.println("Encontrado: "+u.getUsuario()+" "+u.getContrasena());
     }*/

     new s_rmifs(puertolocal);

  }
}