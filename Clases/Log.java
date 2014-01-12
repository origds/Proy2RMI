package Clases;

public class Log {
  private String accion;
  private String registro;

  public Log(){
  }  

  public Log(String accion, String registro){
    this.accion = accion;
    this.registro = registro;
  }

  public String getLog(){
    return registro +" "+accion;
  }

  public void setAccion(String accion) {
    this.accion = accion;
  }

  public void setRegistro(String registro) {
    this.registro =registro;
  }

  public String getAccion(){
    return accion;
  }

  public String getRegistro(){
    return registro;
  }  
}
