import Clases.*;
import java.io.*;
import java.util.ArrayList;

public interface Solicitud
extends java.rmi.Remote {

  public ArrayList<String> rls(Usuario u)
  throws java.rmi.RemoteException;

  public Boolean sub(Usuario u, byte[] archivo, String nombreArchivo)
  throws java.rmi.RemoteException;

  public byte[] baj(Usuario u , String nombreArchivo)
  throws java.rmi.RemoteException;

  public Boolean bor(Usuario u, String nombreArchivo)
  throws java.rmi.RemoteException;

  public Boolean sal(Usuario u)
  throws java.rmi.RemoteException;

  public void registrar(ArrayList<Usuario> usr, Usuario u)
  throws java.rmi.RemoteException;

  public Boolean registrado(Usuario u)
  throws java.rmi.RemoteException;
}