package fdse.zc.fix.pack;

import java.io.InputStream;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

public class FixPackage {
  public static void main(String[] args) throws Exception {
    Map<String, String> map = FixPackage.dependencyList("junit");
    //map.forEach((String k, String v) -> System.out.println(v));

  }

  public static Map<String, String> dependencyList(String className) throws Exception{
    URIBuilder ub = new URIBuilder("http://search.maven.org/solrsearch/select");
    ub.addParameter("q", "c:\"" + className + "\"");
    ub.addParameter("rows", "20");
    ub.addParameter("wt", "json");
    ub.addParameter("start", "0");

    String path = ub.toString();

    URL url = new URL(path);
    InputStream inputStream = url.openStream();
    String jsonString = IOUtils.toString(inputStream, "UTF-8");

    JSONObject json = new JSONObject(jsonString);
    int numFound = json.getJSONObject("response").getInt("numFound");
    int pageCount = (int)Math.ceil(numFound / 20.0);

    Map<String, Long> timestampMap = new LinkedHashMap<>();
    Map<String, String> idMap = new LinkedHashMap<>();
    for(int pageNum = 0; pageNum < pageCount; pageNum++){
      ub.setParameter("start", Integer.toString(pageNum * 20));
      path = ub.toString();
      url = new URL(path);
      inputStream = url.openStream();
      jsonString = IOUtils.toString(inputStream, "UTF-8");
      json = new JSONObject(jsonString);
      JSONArray docs = json.getJSONObject("response").getJSONArray("docs");

      for(int i = 0; i < docs.length(); i++){
        String groupID = docs.getJSONObject(i).getString("g");
        String artifactID = docs.getJSONObject(i).getString("a");
        String key = groupID + ":" + artifactID;
        String id = docs.getJSONObject(i).getString("id");
        long timestamp = docs.getJSONObject(i).getLong("timestamp");
        Long maxTimestamp = timestampMap.get(key);
        if(maxTimestamp == null || maxTimestamp < timestamp){
          timestampMap.put(key, timestamp);
          idMap.put(key, id);
        }
      }
    }  
    return idMap;
  }

  
}