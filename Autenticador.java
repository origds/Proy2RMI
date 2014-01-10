import Clases.*;
import java.util.ArrayList;

public interface Autenticador
extends java.rmi.Remote{
  public Boolean autenticado(Usuario u)
  throws java.rmi.RemoteException;

  public Boolean guardar(Usuario u)
  throws java.rmi.RemoteException;

  public void setUsuarios(ArrayList<Usuario> usr)
  throws java.rmi.RemoteException;

  public ArrayList<Usuario> getUsuarios()
  throws java.rmi.RemoteException;
}