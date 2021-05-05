package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import models.ListaEmpleados;

public class ServiceEmpleado {

    String url;

    public ServiceEmpleado() {
        this.url = "https://apiempleadosisma.azurewebsites.net";

    }

    //lo que consumimos de un servicio es un Stream, que es un flujo
    //de datos ,Aunque nosotros visualmente veamos texto,debemos
    //convertir el flujo en texto
    private String leerRespuestaApi(InputStream Stream) throws IOException {
        //necesitamos un lector del flojo
        BufferedReader buffer = new BufferedReader(new InputStreamReader(Stream));
        //tenemos que  leer el flujo y poner un separador de enter
        //entre las lineas
        String linea = "";
        //un Stringgbuffer sirve para leer contenido muy grande
        StringBuffer data = new StringBuffer();
        //el separador de enter de cada linea
        String separador = "";
        //mientras que existan lineas en el xml , dentro del bucle
        while ((linea = buffer.readLine()) != null) {
            //añadimos el contenido de datos a data
            data.append(separador + linea);
            separador = "\n";

        }
        //recuperamos los datos como String
        String response = data.toString();
        return response;

    }

    //metodo para leer el servicio
    private ListaEmpleados getRequestEmpleados(String request) throws MalformedURLException, IOException, JAXBException {

        //las conexiones Http se realizan a traves de objetos url
        URL peticion = new URL(this.url + request);
        HttpURLConnection conexion = (HttpURLConnection) peticion.openConnection();
        //indicamos el tipo de peticion
        conexion.setRequestMethod("GET");
        //tipo de datos a consumir
        conexion.setRequestProperty("Accept", "application/xml");
        //comprobamos si la peticion es correcta
        if (conexion.getResponseCode() == 200) {
            //recuperamos el flujo de la peticion http
            InputStream stream = conexion.getInputStream();
            //convertimos el flujo  en sTRING XML
            String data = this.leerRespuestaApi(stream);
            //deserializamos el contenido en clases
            //convertir un texto xml en clases java de forma automatica
            //este objeto indica el tipo de clase que debe mapear
            JAXBContext context
                    = JAXBContext.newInstance(ListaEmpleados.class);
            //CREAMOS UN DESERIALIZADOR QUE LE PASAREMOS EL STRING
            //Y NOS DEVUELVE LA CLASE Y PROPIEDADES ASIGNADAS
            Unmarshaller serial = context.createUnmarshaller();
            //PARA DESERIALIZAR, EL MARSHALL NECESITA UN STRINGREADER
            StringReader reader = new StringReader(data);
            //AUTOMATICAMENTE EL MARSHALL RECUPERA LA CLASE
            //MEDIANTE EL READER Y LA MAPEA
            ListaEmpleados empleados = (ListaEmpleados) serial.unmarshal(reader);
            return empleados;
        } else {
            return null;

        }

    }

}
