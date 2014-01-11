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
  
  /* Funciones para manejar el menu */

  private static Usuario leerUsuariosArchivo(String arch)
  throws IOException {
    File archivo = null;
    FileReader fr = null;
    BufferedReader br = null;
    String [] parUserPass;
    String linea;
    Usuario usrarch = null;

    if (arch.equals(""))
      return null;

    try{
      archivo = new File (arch);
      fr = new FileReader (archivo);
      br = new BufferedReader(fr);

      while((linea=br.readLine())!=null){
        parUserPass = linea.split(":");
        if (parUserPass.length==2){
          usrarch = new Usuario(parUserPass[0],parUserPass[1]);
          return usrarch;
        }
        else{
          System.out.println("Warning: Usuario no valido. Error de sintaxis");
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

   private static Log leerComandosConsola()
  throws IOException {

    BufferedReader br = null;
    String [] parCmdArg;
    String linea;
    Log cmd = null;

    try{
      br = new BufferedReader(new InputStreamReader(System.in));

      while(true){
        System.out.println("\nIngrese un comando: ");
        linea = br.readLine();

        if (linea.length()!=0){
          parCmdArg = linea.split(" ");
          if(parCmdArg.length==2)
            cmd = new Log(parCmdArg[0],parCmdArg[1]);
          else if (parCmdArg.length==1){
            cmd = new Log(parCmdArg[0],"");
            if(parCmdArg[0].equals("sal")){
              cmd = new Log(parCmdArg[0],"");
              return cmd;
            }
          } else
            System.out.println("\nDebe introducir un comando valido\n");
          return cmd; 
        }
        else
          System.out.println("\nDebe introducir un comando valido\n"); 
      }
    }
    catch(Exception e){
      System.out.println("Error leyendo comandos: " + e);
    }
    return null;
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

  private static void lls() {
    File carpeta = new File("./");
    File [] listaArchivos = carpeta.listFiles();

    for (int i = 0; i < listaArchivos.length; i++){
      if (listaArchivos[i].isFile())
        System.out.println(listaArchivos[i].getName());
    } 
  }

  private static void manual() {

    System.out.println("\nLista de Comandos:\n");
    System.out.println("rls - Muestra la lista de archivos disponibles en servidor centralizado.\n");
    System.out.println("Sintaxis: rls\n");  
    System.out.println("lls - Muestra la lista de archivos locales del directorio actual\n");
    System.out.println("Sintaxis: lls\n");
    System.out.println("sub - Sube un archivo disponible localmente al servidor remoto\n");
    System.out.println("Sintaxis: sub nombrearchivo\n");
    System.out.println("baj - Baja un archivo disponible en el servidor remoto\n");
    System.out.println("Sintaxis: baj nombrearchivo\n");
    System.out.println("bor - Borra el archivo en el servidor remoto\n");
    System.out.println("Sintaxis: bor nombrearchivo\n");
    System.out.println("info - Muestra la lista de comandos disponible con una breve descripcion\n");
    System.out.println("Sintaxis: info\n");
    System.out.println("sal - Termina la ejecucion del programa\n");
    System.out.println("Sintaxis: sal\n");

  }

  private static void pedirCmdConsola(Usuario uconectado) {
    Log cmd;

    try {
      
      cmd = leerComandosConsola();
      procesarComandos(cmd, uconectado);
      while(!cmd.getUsuario().equals("sal")){
        cmd = leerComandosConsola();
        procesarComandos(cmd, uconectado);
      }
      System.out.println("\nHa salido correctamente. Hasta luego");

    }
    catch(Exception e){
      System.out.println("Error leyendo comandos: " + e);
    }
  }


  private static void procesarComandos(Log comando, Usuario user){
    String cmdactual, argumento;
    boolean subio, borro, bajo;
    subio = false;
    borro = false;
    bajo = false;
    cmdactual = null;
    argumento = null;

    try {

      Solicitud sol = (Solicitud)
      Naming.lookup("rmi://"+server+":"+puerto+"/ArchivosService");
      
        cmdactual = comando.getUsuario();
        argumento = comando.getRegistro();

        if(cmdactual.equals("rls")) {
          printRls(sol.rls(user));
        
        } else if (cmdactual.equals("lls")) {  
          lls();

        } else if (cmdactual.equals("sub")) {
          subio = sol.sub(user, archivoToBytes(argumento), argumento);

          if (!subio)
            System.out.println("Error: el archivo " + argumento + " no pudo subirse");

        } else if (cmdactual.equals("baj")) {
          bajo = bytesToArchivo(sol.baj(user, argumento), argumento);

          if (!bajo)
            System.out.println("Error: el archivo " + argumento + " no pudo bajarse");

        } else if (cmdactual.equals("bor")) {
          borro = sol.bor(user, argumento);

          if (!borro)
            System.out.println("Error: el archivo " + argumento + " no fue borrado");

        } else if (cmdactual.equals("info")) {
          manual();

        } else if (cmdactual.equals("sal")) {
          sol.sal(user);

        } else {
          System.out.println("\nDebe introducir un comando valido\n");

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

  }

  /* Funciones auxiliares para procesar comandos */

  private static Boolean existeArchivo(String nombreArchivo){
    File carpeta = new File("./");
    File [] listaArchivos = carpeta.listFiles();

    for (int i = 0; i < listaArchivos.length; i++){
      if (listaArchivos[i].isFile() && 
          nombreArchivo.equals(listaArchivos[i].getName())){
          return true;
      }
    }
    return false;
  }

  private static byte[] archivoToBytes(String nombreArchivo) {
    
    try{    
      File file = new File(nombreArchivo);
      byte buffer[] = new byte[(int)file.length()];
 
      BufferedInputStream input = new BufferedInputStream(new FileInputStream(file.getName()));
 
      input.read(buffer,0,buffer.length);
      input.close();

      return buffer;
    } 
    catch(Exception e){
      System.out.println("Error bajando archivo: "+e.getMessage());
      e.printStackTrace();
    }
    return null;
  }

  private static Boolean bytesToArchivo(byte[] archivo, String nombreArchivo)
  throws java.rmi.RemoteException {

    try{
      if (existeArchivo(nombreArchivo)) {
        return false;
      }

      File file = new File(nombreArchivo);
      BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file.getName()));
        
      output.write(archivo,0,archivo.length);
      output.flush();
      output.close();

      return true;
    }
    catch(Exception e){
      System.out.println("Error bajando archivo: "+e.getMessage());
      e.printStackTrace();
    }
    return false;
  }

  private static void printRls(ArrayList<String> l) {
    Iterator<String> iterador = l.iterator();
    String archivo;
    System.out.println("\nArchivos Remotos:\n");
      while(iterador.hasNext()){
        archivo  = iterador.next();
        System.out.println(archivo);
      }
  }

  private static void menuCliente (String[] param) {

    boolean f,m,p,c;
    server = "";
    usuarios = "";
    comandos = "";
    f= false;
    m= false;
    p = false; 
    c = false;

    for ( int i = 0 ; i < (param.length)-1 ; i++ ) {

      if (param[i].equals("-f") && !f) {
          usuarios = param[i+1];
          f = true;
      } else if (param[i].equals("-m") && !m) {
          server = param[i+1];
          m = true;
      } else if (param[i].equals("-p") && !p) {
          puerto = Integer.parseInt(param[i+1]);
          p = true;
      } else if (param[i].equals("-c") && !c) {
          comandos = param[i+1];
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

  }

  /*Funcion Main*/

  public static void main (String[] args)
  throws java.rmi.RemoteException, IOException {

    menuCliente(args);

    try{
      Solicitud sol = (Solicitud)
      Naming.lookup("rmi://"+server+":"+puerto+"/ArchivosService");
      ArrayList<Log> archcmd = new ArrayList<Log>();
      ArrayList<Usuario> archuser = new ArrayList<Usuario>();
      Usuario uconectado = null;
      Log comando = null;
      Boolean sal = false;

      //Manejo de Archivo de Usuario
      uconectado = leerUsuariosArchivo(usuarios);
      if(uconectado==null)
        uconectado = login();

      // Verificacion de autenticacion de usuario
      if (sol.registrado(uconectado)) {

        //Manejo de Archivo de Comandos
        if (!comandos.equals("")) {
          archcmd = leerComandosArchivo(comandos);
          if (archcmd!=null){
            Iterator<Log> iterador = archcmd.iterator();
            while(iterador.hasNext()){
              comando = iterador.next();
              if (!comando.getUsuario().equals("sal")) {
                procesarComandos(comando, uconectado);
              }
              else {
                sal = true;
                procesarComandos(comando, uconectado);
                break;
              }
            }
            if (!sal) 
              pedirCmdConsola(uconectado);
            else
              System.out.println("Ha salido correctamente. Hasta luego");
          } else {
            System.out.println("No se pudo leer el archivo de comandos");
            System.exit(1);
          }
        }
        else{
          pedirCmdConsola(uconectado);
        }
      } else {
        System.out.println("");
        System.out.println("Usuario y constrasena invalidas");
        System.out.println("");

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