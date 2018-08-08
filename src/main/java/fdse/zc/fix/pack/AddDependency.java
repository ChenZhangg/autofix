package fdse.zc.fix.pack;

import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.builder.AstBuilder;

public class AddDependency{
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