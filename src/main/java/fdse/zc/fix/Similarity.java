package fdse.zc.fix;

public class Similarity {
  public static boolean sameClassName(String source, String target) {
    source = source.replaceAll("\\.(java|class)", "");
    target = target.replaceAll("\\.(java|class)", "");
    String[] sourceArray = source.split("\\.");
    String[] targetArray = target.split("\\.");
    String sourceClassName = sourceArray[sourceArray.length - 1];
    String targetClassName = targetArray[targetArray.length - 1];
    System.out.println(sourceClassName);
    System.out.println(targetClassName);
    return true;
  }

  public static int identifierSimilarity(String source, String target) {
    source = source.replaceAll("\\.(java|class)", "");
    target = target.replaceAll("\\.(java|class)", "");
    int[][] dp = new int[source.length() + 1][target.length() + 1];
    for(int i = 1;i <= source.length();i++)
      dp[i][0] = i;
    for(int j = 1;j <= target.length();j++)
      dp[0][j] = j;
    for(int i = 1;i <= source.length();i++) {
      for(int j = 1;j <= target.length();j++) {
        if(source.charAt(i - 1) == target.charAt(j - 1))
          dp[i][j] = dp[i - 1][j - 1];
        else {
          dp[i][j] = Math.min(dp[i - 1][j] + 1,
          Math.min(dp[i][j - 1] + 1, dp[i - 1][j - 1] + 1));
        }
      }
    }
    return dp[source.length()][target.length()];
  }

}