package fdse.zc.gumtree.java;

public class Mapping {
    private JavaTree first;
    private JavaTree second;
    public Mapping(JavaTree first, JavaTree second){
        this.first = first;
        this.second = second;
    }

    public JavaTree getFirst() {
        return first;
    }

    public JavaTree getSecond() {
        return second;
    }

    public String toString() {
        return "(" + getFirst().toString() + "\n" + getSecond().toString() + ")\n";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mapping pair = (Mapping) o;

        if (!first.equals(pair.first)) return false;
        return second.equals(pair.second);

    }

    public int hashCode() {
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        return result;
    }
}
