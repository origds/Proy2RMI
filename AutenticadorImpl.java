/**
* AutenticadorImpl.java
* Archivo donde se implementan las funciones de la interfaz Autenticador
* @autor Oriana Gomez e Ivan Travecedo
* @version 1.0
**/

import Clases.*;
import java.util.ArrayList;
import java.util.Iterator;

public class AutenticadorImpl
extends
java.rmi.server.UnicastRemoteObject
implements Autenticador {

  public static ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

  public static final long serialVersionUID = 1L;

  /**
  * AutenticadorImpl. Constructor de la clase AutenticadorImpl
  **/
  public AutenticadorImpl() 
  throws java.rmi.RemoteException {
    super();
  }

  /**
  * Autenticado. Funcion que verifica si un usuario se encuentra registrado para usar
  * el servidor de archivos
  * @param Usuario. instancia de la clase usuario que se verificara
  * @return devuelve si el usuario fue autenticado o no 
  **/
  public Boolean autenticado (Usuario usuario)
  throws java.rmi.RemoteException {
    Usuario u;
    Iterator<Usuario> iterador = usuarios.iterator();
    System.out.println("Buscando: "+usuario.getUsuario()+" "+usuario.getContrasena());
    while(iterador.hasNext()){
      u = iterador.next();
      System.out.println("Encontrado: "+u.getUsuario()+" "+u.getContrasena());
      if(u.getUsuario().equals(usuario.getUsuario()) && 
         u.getContrasena().equals(usuario.getContrasena()))
        return true;
    }
    return false;
  }

  /**
  * Guardar. Funcion que agrega los usuarios a la lista de registrados del servidor de autenticacion.
  * @param Usuario. instancia de la clase usuario que se agregara
  * @return devuelve si el usuario fue agregado o no 
  **/
  public Boolean guardar(Usuario usuario)
  throws java.rmi.RemoteException {
    if(!usuarios.contains(usuario)){
      usuarios.add(usuario);
      return true;
    }
    return false;
  }

  /**
  * setUsuarios. Funcion que guarda cada uno de los usuarios del arraylist al servidor de autenticacion
  * @param Usr: ArrayList de usuarios que van a ser registrados
  **/
  public void setUsuarios(ArrayList<Usuario> usr)
  throws java.rmi.RemoteException {
    Usuario u;
    Iterator<Usuario> iterador = usr.iterator();
    while(iterador.hasNext()){
      u = iterador.next();
      guardar(u);
    }
  }

  /**
  * getUsuarios. Funcion que devuelve la lista de usuarios registrados en el servidor de autenticacion
  * @return devuelve la lista de usuarios registrados en el servidor de autenticacion 
  **/
  public ArrayList<Usuario> getUsuarios()
  throws java.rmi.RemoteException {
    return usuarios;
  }
}