package Clases;

import java.io.*;

public class Usuario implements Serializable {
  private String usuario;
  private String contrasena;

  public Usuario(String usuario, String contrasena){
    this.usuario = usuario;
    this.contrasena = contrasena;
  }

  public String getUsuario(){
    return usuario;
  }

  public String getContrasena(){
    return contrasena;
  }  

  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }

  public void setContrasena(String contrasena) {
    this.contrasena = contrasena;
  }
}
