package fdse.zc.util;

public class ExtractDiffInfo {
  private static ExtractDiffInfo extractDiffInfo;

  private ExtractDiffInfo() { }

  public static ExtractDiffInfo getInstance() {
    if(extractDiffInfo == null) {
      extractDiffInfo = new ExtractDiffInfo();
    }
    return extractDiffInfo;
  }

}