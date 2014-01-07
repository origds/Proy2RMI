import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;

import java.io.*;
import java.util.ArrayList;

import Clases.*;


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
   /* try {
      leerComandosArchivo("registros.txt");




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
    catch (java.lang.ArithmeticException ae) {
      System.out.println();
      System.out.println(
       "java.lang.ArithmeticException");
      System.out.println(ae);
    }
    catch(IOException ioe){
      System.out.println();
      System.out.println(
       "java.lang.ArithmeticException");
      System.out.println(ioe);
    }*/
    System.out.println("Borrame");
  }










}