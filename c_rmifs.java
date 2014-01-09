import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;

import java.io.*;
import java.util.ArrayList;

import Clases.*;
import java.util.Iterator;


public class c_rmifs {

  private static ArrayList<Usuario> leerUsuariosArchivo(String arch)
  throws IOException {
    File archivo = null;
    FileReader fr = null;
    BufferedReader br = null;
    String [] parUserPass;
    String linea;
    ArrayList<Usuario> usrarch = new ArrayList<Usuario>();

    try{
      archivo = new File (arch);
      fr = new FileReader (archivo);
      br = new BufferedReader(fr);

      while((linea=br.readLine())!=null){
        parUserPass = linea.split(":");
        Usuario u = new Usuario(parUserPass[0],parUserPass[1]);
        usrarch.add(u);
      }
      return usrarch;

    }
    catch(Exception e){
      System.out.println("Error leyendo usuarios: " + e);
    }
    finally {
     try{
      if(fr!=null)
        fr.close();
      }
      catch (Exception e2){
        System.out.println("Error cerrando arch usuarios: " + e2);
      }
    }
    return null;
  }

  private static ArrayList<Log> leerComandosArchivo(String arch)
  throws IOException {
    File archivo = null;
    FileReader fr = null;
    BufferedReader br = null;
    String [] parUserPass;
    String linea;
    ArrayList<Log> cmdarch = new ArrayList<Log>();

    try{
      archivo = new File (arch);
      fr = new FileReader (archivo);
      br = new BufferedReader(fr);

      while((linea=br.readLine())!=null){
        parUserPass = linea.split(":");
        Log l = new Log(parUserPass[0],parUserPass[1]);
        cmdarch.add(l);
      }
      return cmdarch;

    }
    catch(Exception e){
      System.out.println("Error leyendo comandos: " + e);
    }
    finally {
     try{
      if(fr!=null)
        fr.close();
      }
      catch (Exception e2){
        System.out.println("Error cerrando arch comandos: " + e2);
      }
    }
    return null;
  }



  public static void main (String[] args)
  throws java.rmi.RemoteException, IOException {

    String server, usuarios, comandos;
    server = "";
    usuarios = "";
    comandos = "";
    int puerto = 0;
    boolean f,m,p,c;
    f= false;
    m= false;
    p = false; 
    c = false;

  for ( int i = 0 ; i < (args.length)-1 ; i++ ) {

    if (args[i].equals("-f") && !f) {
        usuarios = args[i+1];
        f = true;
    } else if (args[i].equals("-m") && !m) {
        server = args[i+1];
        m = true;
    } else if (args[i].equals("-p") && !p) {
        puerto = Integer.parseInt(args[i+1]);
        p = true;
    } else if (args[i].equals("-c") && !c) {
        comandos = args[i+1];
        c = true;
    } else {
        System.out.println("Error de sintaxis: c_rmifs [-f usuarios] -m servidor -p puerto [-c comandos]");
        System.exit(1); 
    }

    i = i+1;
  }

  if ((!p) || (!m)) {
    System.out.println("Error de sintaxis: c_rmifs [-f usuarios] -m servidor -p puerto [-c comandos]");
    System.exit(1); 
  }

  System.out.println("Puerto: "+puerto);
  System.out.println("Servidor: "+server);
  System.out.println("Usuarios: "+usuarios);
  System.out.println("Comandos: "+comandos);

  try{ 
    Usuario u = new Usuario("usuario","123456");
    Solicitud sol = (Solicitud)
    Naming.lookup("rmi://"+server+":"+puerto+"/ArchivosService");

    ArrayList<String> rec = new ArrayList<String>();
    rec = sol.rls(u);
    if (rec!=null){
      Iterator<String> iterador = rec.iterator();
      while(iterador.hasNext()){
        String arch = iterador.next();
        System.out.println("Encontrado: "+arch);
      }
    }
    else{
      System.out.println("No estas autenticado");

    }
  }



   /* try {
      leerComandosArchivo("registros.txt");
    }*/
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
    /*catch(IOException ioe){
      System.out.println();
      System.out.println(
       "java.lang.ArithmeticException");
      System.out.println(ioe);
    }*/
    System.out.println("Borrame");
  }










}