package julia.dhbw;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.core.MediaType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;

/*In diesen Zeilen werden die benötigten Java-Pakete importiert, um die verschiedenen Klassen
 und Schnittstellen zu verwenden, die für die Erstellung eines RESTful Web Service in Jakarta EE
 erforderlich sind. Außerdem werden Klassen für die Verbindung zur Datenbank und die Verarbeitung von JSON importiert.
 */

@Path("/masterquiz")
/*Dies ist eine Annotation für die Ressourcenklasse (ExampleResource),
 die den Pfad angibt, unter dem diese Ressource erreichbar ist.*/
public class ExampleResource {

    @GET
    //Dies ist eine Annotation für die Methode hello(), die angibt, dass diese
    // Methode auf eine HTTP GET-Anforderung reagiert.
    @Produces(MediaType.APPLICATION_JSON)
    //Diese Annotation gibt an, dass die Methode daten() JSON-Daten produzieret.
    public String daten() {
        //Dies ist die Methode, die auf die GET-Anforderung reagiert. Sie gibt einen
        // String zurück, der normalerweise als HTTP-Antwort gesendet wird
/*Datenbankverbindung
        String url, String user, und String pass sind Variablen, in denen die Verbindungsinformationen
        zur MySQL-Datenbank gespeichert sind.*/
        String url = "jdbc:mysql://localhost:3306/quiz";
        String user = "root";
        String pass = "";
        try {
            Connection con = DriverManager.getConnection(url, user, pass);
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM  frage f JOIN antwort a ON f.id = a.frageId";
            //definiert eine SQL-Abfrage, die Daten aus den Tabellen "frage" und "antwort" abruft und miteinander verknüpft
            ResultSet rs = stmt.executeQuery(sql);

            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

            while (rs.next()) {
                String frage = rs.getString("f.wert");
                String antwort = rs.getString("a.wert");
                boolean istRichtig = rs.getBoolean("a.richtig");

                jsonArrayBuilder.add(Json.createObjectBuilder()
                        .add("frage", frage)
                        .add("antwort", antwort)
                        .add("istRichtig", istRichtig));
            }
//Eine Schleife durchläuft die Ergebnisse der SQL-Abfrage und extrahiert Daten wie Fragen, Antworten und ob die Antwort richtig ist.
//Die extrahierten Daten werden in ein JSON-Array (JsonArray) aufgebaut und formatiert.
            JsonArray jsonArray = jsonArrayBuilder.build();

            return jsonArray.toString();
//Die Methode gibt das JSON-Array als String zurück, der als HTTP-Antwortkörper gesendet wird.
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }//Im Fehlerfall (z.B., wenn keine Verbindung zur Datenbank hergestellt werden kann),
        // wird eine Fehlermeldung zurückgegeben und der Fehler wird auf der Konsole ausgegeben.
    }
}