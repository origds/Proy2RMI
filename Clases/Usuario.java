/**
* Usuario.java
* Archivo donde se define la clase Usuario
* @autor Oriana Gomez e Ivan Travecedo
* @version 1.0
**/

package Clases;

import java.io.*;

public class Usuario implements Serializable {
  private String usuario;
  private String contrasena;

  public static final long serialVersionUID = 3L;

  /**
  * Usuario. Constructor que inicializa los atributos para una instancia de usuario
  * @param Usuario: valor que se asignara a la variable 
  **/
  public Usuario(String usuario, String contrasena){
    this.usuario = usuario;
    this.contrasena = contrasena;
  }

  /**
  * getUsuario. Funcion que devuelve el valor del atributo usuario
  * @return nombre del usuario
  **/
  public String getUsuario(){
    return usuario;
  }

  /**
  * getContasena. Funcion que devuelve el valor del atributo contrasena
  * @return contrasena del usuario
  **/
  public String getContrasena(){
    return contrasena;
  }  

  /**
  * setUsuario. Funcion que asigna un valor al atributo usuario
  * @param nombre de usuario que se asignara al atributo
  **/
  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }

  /**
  * setContrasena. Funcion que asigna un valor al atributo contrasena
  * @param valor de contrasena que se asignara al atributo
  **/
  public void setContrasena(String contrasena) {
    this.contrasena = contrasena;
  }
}
