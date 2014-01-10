import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;

import java.io.*;
import java.util.ArrayList;

import Clases.*;
import java.util.Iterator;

public class c_rmifs {

  static String server, usuarios, comandos;
  static int puerto = 0;
  
  private static ArrayList<Usuario> leerUsuariosArchivo(String arch)
  throws IOException {
    File archivo = null;
    FileReader fr = null;
    BufferedReader br = null;
    String [] parUserPass;
    String linea;
    ArrayList<Usuario> usrarch = new ArrayList<Usuario>();

    if (arch.equals(""))
      return null;

    try{
      archivo = new File (arch);
      fr = new FileReader (archivo);
      br = new BufferedReader(fr);

      while((linea=br.readLine())!=null){
        parUserPass = linea.split(":");
        if (parUserPass.length==2){
          Usuario u = new Usuario(parUserPass[0],parUserPass[1]);
          usrarch.add(u);
        }
        else{
          System.out.println("Warning: Usuario no agregado. Error de sintaxis");
        }
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
    String [] parCmdArg;
    String linea;
    ArrayList<Log> cmdarch = new ArrayList<Log>();
    Log logCmd;

    if (arch.equals(""))
      return null;

    try{
      archivo = new File (arch);
      fr = new FileReader (archivo);
      br = new BufferedReader(fr);
      while((linea=br.readLine())!=null){
        parCmdArg = linea.split(" ");
        if(parCmdArg.length==2)
          logCmd = new Log(parCmdArg[0],parCmdArg[1]);
        else
          logCmd = new Log(parCmdArg[0],"");
        cmdarch.add(logCmd);
        if(parCmdArg[0].equals("sal"))
          break;
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

  private static void registrarUsuarios(ArrayList<Usuario> usuarios, Usuario actual)
  throws java.rmi.RemoteException {
    try{
      Solicitud sol = (Solicitud)
      Naming.lookup("rmi://"+server+":"+puerto+"/ArchivosService");

      sol.registrar(usuarios,actual);
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
  }

  private static Usuario login() throws IOException {
    BufferedReader br = new BufferedReader(new
                            InputStreamReader(System.in));
    String user = "";
    String psw = "";
    Usuario actual;

    while(true){
      System.out.println("Ingrese nombre de usuario: ");
      user = br.readLine();
      System.out.println("Ingrese contrasena: ");
      psw = br.readLine();

      if (user.length()!=0 && psw.length()!=0){
        actual = new Usuario(user, psw);
        return actual;
      }
      else{
       System.out.println("Debe introducir un usuario y contrasena validas"); 
      }
    }
  }

  public static void main (String[] args)
  throws java.rmi.RemoteException, IOException {

    boolean f,m,p,c;
    server = "";
    usuarios = "";
    comandos = "";
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
    Solicitud sol = (Solicitud)
    Naming.lookup("rmi://"+server+":"+puerto+"/ArchivosService");
    Usuario uconectado = login();
    ArrayList<Log> archcmd = new ArrayList<Log>();
    ArrayList<Usuario> archuser = new ArrayList<Usuario>();

    // Verificamos que el usuario este conectado

    if(sol.registrado(uconectado)){
      //Manejo de Archivo de Usuario
      archuser = leerUsuariosArchivo(usuarios);
      if(archuser!=null){
        registrarUsuarios(archuser,uconectado);
      }

      //Manejo de Archivo de Comandos

    
      archcmd = leerComandosArchivo(comandos);
      if (archcmd!=null){
        Iterator<Log> iterador = archcmd.iterator();
        while(iterador.hasNext()){
          Log casa = iterador.next();
          System.out.println("comando: "+casa.getUsuario()+ " argumento: "+casa.getRegistro());
        }
      }


    }
    else{


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
    /*catch(IOException ioe){
      System.out.println();
      System.out.println(
       "java.lang.ArithmeticException");
      System.out.println(ioe);
    }*/
  }










}