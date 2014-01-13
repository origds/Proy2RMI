/**
* Log.java
* Archivo donde se define la clase Log
* @autor Oriana Gomez e Ivan Travecedo
* @version 1.0
**/

package Clases;

public class Log {
  private String accion;
  private String registro;

  /**
  * Log. Constructor vacio de la clase
  **/
  public Log(){
  }  

  /**
  * Log. Constructor de la clase, usando parametros
  * @param Accion: accion que se registra en el log
  * @param Registro: argumento que acompana la accion en el log
  **/
  public Log(String accion, String registro){
    this.accion = accion;
    this.registro = registro;
  }

  /**
  * getLog. Funcion que retorna una instancia de la clase log
  * @return devuelve una instancia de la clase log, separada en accion y registro
  **/
  public String getLog(){
    return registro +" "+accion;
  }

  /**
  * setAccion. Asigna un valor al atributo accion de la instacia de log
  * @param Accion: valor que sera asignado al atributo accion
  **/
  public void setAccion(String accion) {
    this.accion = accion;
  }

  /**
  * setRegistro. Asigna un valor al atributo registro de la instacia de log
  * @param Registro: valor que sera asignado al atributo registro
  **/
  public void setRegistro(String registro) {
    this.registro =registro;
  }

  /**
  * getAccion. Funcion que retorna el atributo accion de la instancia de log
  * @return devuelve el valor del atributo accion.
  **/
  public String getAccion(){
    return accion;
  }

  /**
  * getRegistro. Funcion que retorna el atributo registro de la instancia de log
  * @return devuelve el valor del atributo registro.
  **/
  public String getRegistro(){
    return registro;
  }
}
