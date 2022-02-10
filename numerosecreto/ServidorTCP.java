/*
 * TAREA PSP03. EJERCICIO 1.
 * El objetivo del ejercicio es crear una aplicación cliente/servidor que se 
 * comunique por el puerto 2000 y realice lo siguiente:
 * El servidor debe generar un número secreto de forma aleatoria entre el 0 
 * al 100. El objetivo de cliente es solicitarle al usuario un número y 
 * enviarlo al servidor hasta que adivine el número secreto. Para ello, el 
 * servidor para cada número que le envía el cliente le indicará si es menor,
 * mayor o es el número secreto del servidor.
 *
 * Esta clase genera la parte correspondiente al servidor
 * RECORDAR  COMENTAR EL PACKAGE SI SE QUIERE COMPILAR FUERA DE NETBEANS.
 */
//package servidortcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author juang <juangmuelas@gmail.com>
 * @since 06/12/2020
 * @version 1
 */

public class ServidorTCP {
    
    /**
     * El hecho de pedir números secretos, lo interpreto como parte de una app
     * segura y por ello, hacemos las comunicaciones mediante TCP.
     * Sigo por ello, la estructura y ejemplo mostrados en el tema para este tipo
     * de conexiones.
     * @param args the command line arguments
     * @param Puerto integer que indica el puerto de enlace
     * @param numeroCorrecto boolean inicializado en false para tantear las 
     * entradas desde el cliente.
     */
    
    static final int Puerto = 2000;   
    boolean numeroCorrecto = false;

    
    public ServidorTCP(){
        
        /**
         * @param numAleatorio objeto de la clase Random para determinar el 
         * numero secreto.
         * Lo mostramos en consola para verificar que recoge el dato.
         */
        int numAleatorio = (int)Math.round(Math.random()*100); 
        System.out.println("Numero secreto: " + numAleatorio);
        
        /**
         * try-cath para tratar la recogida y muestra de datos desde el cliente.
         * la primera parte, hasta recibir la conexión con el cliente sigue la
         * estructura del temario.
         * @throws Exception para mostrar mensaje de error en su caso.
         */
 
        try { 
            // Inicio la escucha del servidor en un determinado puerto
            ServerSocket sSkServidor = new ServerSocket(Puerto);
            //Confirmamos que se recibe le puerto de escucha
            System.out.println("Escucho el puerto " + Puerto );
            // Espero a que se conecte un cliente y creo un nuevo socket para el cliente
            Socket sSkCliente = sSkServidor.accept();     
            //Confirmamos, por confianza, que se recibe.
            System.out.println("Servicio a cliente...");
            /**
             * Flujos abreviados (en temario se crean primero variables 
             * Input/OutputStream) de entrada y salida mediante objetos 
             * DatainputStream y DataOutpuStream
             */
            DataInputStream flujo_entrada = new DataInputStream(sSkCliente.getInputStream());            
            DataOutputStream flujo_salida= new DataOutputStream(sSkCliente.getOutputStream());  
            /**
             * Mientras reciba un dato incorrecto, se trata de forma individual
             * la instrucción, dentro del bucle while
             * @param numCliente recibe el flujo de entrada con el dato a tratar.
             */
            while(numeroCorrecto==false){
                int numCliente=flujo_entrada.readInt();
                /**
                 * Mediante @if tratamos primero la posibilidad correcta
                 * y en el @else los posibles errores.
                 * Utilizamos marcas de colorescapando en ANSI para resaltar.
                 */
                if(numCliente==numAleatorio){
                    numeroCorrecto=true;
                    flujo_salida.writeBoolean(numeroCorrecto); 
                    //Indicar por la salida que es la opción correcta
                    flujo_salida.writeUTF("\033[36m" + numCliente + " es el numero correcto. \n");
                }else{
                    if(numCliente<numAleatorio){
                        numeroCorrecto=false;
                        flujo_salida.writeBoolean(numeroCorrecto);
                        //Indicar por la salida que es menor
                        flujo_salida.writeUTF("\033[33mEl numero "+ numCliente +" es menor que el requerido.\n");
                    }else{
                         numeroCorrecto=false;
                         flujo_salida.writeBoolean(numeroCorrecto);
                         ////Indicar por la salida que es mayor
                        flujo_salida.writeUTF("\033[33mEl numero "+ numCliente +" es mayor que el requerido\n");
                    }               
                }   //Fin if/else                                   
            }//Fin while
            /**
             * Tras tratar los datos, se cierran conexiones y se avisa de ello.
             */
            flujo_entrada.close();
            flujo_salida.close();
            System.out.println("Cerrando conexion.");
            sSkServidor.close();
            sSkCliente.close();    
        } catch( Exception e ) {
            System.out.println( e.getMessage() );
        }
    }//Fin constructor ServidorTCP
      
    public static void main(String[] args) {
        // Iniciamos para poder recibir datos      
        new ServidorTCP();
    }//Fin  main   
} //Fin clase ServidorTCP
