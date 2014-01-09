import Clases.*;
import java.util.ArrayList;
import java.util.Iterator;

public class AutenticadorImpl
extends
java.rmi.server.UnicastRemoteObject
implements Autenticador {

  public static ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

  public AutenticadorImpl() 
  throws java.rmi.RemoteException {
    super();
  }

  public int autenticado (Usuario usuario)
  throws java.rmi.RemoteException {
    Usuario u;
    Iterator<Usuario> iterador = usuarios.iterator();
    System.out.println("Buscando: "+usuario.getUsuario()+" "+usuario.getContrasena());
    while(iterador.hasNext()){
      u = iterador.next();
      System.out.println("Encontrado: "+u.getUsuario()+" "+u.getContrasena());
      if(u.getUsuario().equals(usuario.getUsuario()) && 
         u.getContrasena().equals(usuario.getContrasena()))
        return 0;
    }
    return 1;
  }

  public int guardar(Usuario usuario)
  throws java.rmi.RemoteException {
    if(!usuarios.contains(usuario)){
      usuarios.add(usuario);
      return 0;
    }
    return 1;
  }

  public void setUsuarios(ArrayList<Usuario> usr)
  throws java.rmi.RemoteException {
    Usuario u;
    Iterator<Usuario> iterador = usr.iterator();
    while(iterador.hasNext()){
      u = iterador.next();
      guardar(u);
    }
  }

  public ArrayList<Usuario> getUsuarios()
  throws java.rmi.RemoteException {
    return usuarios;
  }
}