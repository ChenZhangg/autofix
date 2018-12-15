package fdse.zc.fix;

public interface ErrorRegexp {
  String package_doesnt_exist = "/home/travis/build/([^/]+/){2}(.+)\\.java:(\\[)?(\\d+).+error: package";
}