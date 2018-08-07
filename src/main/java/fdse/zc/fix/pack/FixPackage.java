package fdse.zc.fix.pack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.builder.AstBuilder;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.json.JSONArray;
import org.json.JSONObject;

public class FixPackage {
  public static void main(String[] args) throws Exception {
    Map<String, String> map = FixPackage.dependencyList("junit");
    //map.forEach((String k, String v) -> System.out.println(v));
    //updateGradle();
    updateMaven();
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

  public static void updateGradle() throws Exception{
    String path = "/Users/zhangchen/projects/autofix/build.gradle";
    String string = IOUtils.toString(new FileInputStream(path), "UTF-8");
    AstBuilder astBuilder = new AstBuilder();
    List<ASTNode> nodes = astBuilder.buildFromString(string);
    FindDependenciesVisitor visitor = new FindDependenciesVisitor();
    for(ASTNode node : nodes){
      node.visit(visitor);
      //System.out.println(node.getClass());
    }
    System.out.println(visitor.getDependenceLineNum());
    //System.out.println(string);
    String dependency = "\tcompile group: \"com.liferay\", name:\"com.liferay.bookmarks.api\", version:\"1.0.0\"";
    List<String> gradleFileContents = Files.readAllLines(Paths.get(path));
    gradleFileContents.add(visitor.getDependenceLineNum() - 1, "\n" + dependency + "\n");
    /*
    StringBuilder builder = new StringBuilder( gradleFileContents.get( visitor.getDependenceLineNum() - 1 ) );
    builder.insert( visitor.getColumnNum() - 2, "\n" + dependency + "\n" );
    String dep = builder.toString();

    gradleFileContents.remove( visitor.getDependenceLineNum() - 1 );
    gradleFileContents.add( visitor.getDependenceLineNum() - 1, dep );
    */
    gradleFileContents.forEach(System.out::println);
  }

  public static void updateMaven() throws Exception{
    String path = "/Users/zhangchen/projects/projectanalysis/flyway/pom.xml";
    MavenXpp3Reader reader = new MavenXpp3Reader();
    Model model = reader.read(new FileReader(path));
    System.out.println(model.toString());
    Dependency dependency = new Dependency();
    dependency.setGroupId("zc");
    dependency.setArtifactId("zc");
    dependency.setVersion("1.0");
    System.out.println(dependency);

    model.addDependency(dependency);
    MavenXpp3Writer writer = new MavenXpp3Writer();
    writer.write(System.out, model);
  }
}