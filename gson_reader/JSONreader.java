import java.net.*;
import java.io.*;
import com.google.gson.*;


public class JSONreader{
  public static void main(String[] args){

    String urlName = "http://challenges.hackajob.co/swapi/api/films/?format=json"; //initiliaze empty string


    try{
      StringBuffer result = new StringBuffer();
      URL url = new URL(urlName);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      int responseCode = conn.getResponseCode();
      if(responseCode != 200) System.out.println("Something went wrong: " +
      responseCode);

      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line;

      while((line = rd.readLine()) != null){
        result.append(line);
      }
      rd.close();


      JsonObject newObject = (JsonObject) new JsonParser().parse(result.toString());
      JsonArray resultObject = (JsonArray) newObject.get("results");
      for(int i=0;i<resultObject.size();++i){
        JsonObject temp = (JsonObject) resultObject.get(i);
        if(temp.get("title").toString().contains("Revenge of the Sith")) System.out.println(temp);
      }
      // System.out.println(resultObject.get(0));


      // System.out.println(jsonObject.getAsJsonArray("results").get(0).getAsJsonObject().get("title"));


    } catch (Exception e){ System.out.println(e);}


  }
}
