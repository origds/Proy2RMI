package Clases;

public class Log {
  private String usuario;
  private String registro;

  public Log(){
  }  

  public Log(String usuario, String registro){
    this.usuario = usuario;
    this.registro = registro;
  }

  public String getLog(){
    return registro +" "+usuario;
  }

  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }

  public void setRegistro(String registro) {
    this.registro =registro;
  }

  public String getUsuario(){
    return usuario;
  }

  public String getRegistro(){
    return registro;
  }  
}
