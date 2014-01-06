import Clases.*;
import java.util.ArrayList;

public interface Autenticador
extends java.rmi.Remote{
  public int autenticado(Usuario u)
  throws java.rmi.RemoteException;

  public int guardar(Usuario u)
  throws java.rmi.RemoteException;

  public void setUsuarios(ArrayList<Usuario> usr)
  throws java.rmi.RemoteException;

  public ArrayList<Usuario> getUsuarios()
  throws java.rmi.RemoteException;
}