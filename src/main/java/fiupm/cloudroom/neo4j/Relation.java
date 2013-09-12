package fiupm.cloudroom.neo4j;

/**
 *
 * @author fran
 */
public class Relation {

    public static final String OUT = "out";
    public static final String IN = "in";
    public static final String BOTH = "both";
    private String type;
    private String direction;

    public Relation(String type, String direction) {
        this.type = type;
        this.direction = direction;
    }

    public String toJsonCollection() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append(" \"type\" : \"").append(type).append("\"");
        if (direction != null) {
            sb.append(", \"direction\" : \"").append(direction).append("\"");
        }
        sb.append(" }");
        return sb.toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
