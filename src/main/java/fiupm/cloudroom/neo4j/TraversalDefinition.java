package fiupm.cloudroom.neo4j;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fran
 */
public class TraversalDefinition {

    public static final String DEPTH_FIRST = "depth first";
    public static final String NODE = "node";
    public static final String ALL = "all";
    private String uniqueness;
    private int maxDepth;
    private String returnFilter;
    private String order;
    private List<Relation> relationships;

    public TraversalDefinition() {
        this.relationships = new ArrayList<Relation>();
        this.order = DEPTH_FIRST;
        this.returnFilter = ALL;
        this.maxDepth = 1;
        this.uniqueness = NODE;
    }

    public String getUniqueness() {
        return uniqueness;
    }

    public void setUniqueness(String uniqueness) {
        this.uniqueness = uniqueness;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public String getReturnFilter() {
        return returnFilter;
    }

    public void setReturnFilter(String returnFilter) {
        this.returnFilter = returnFilter;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public List<Relation> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<Relation> relationships) {
        this.relationships = relationships;
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append(" \"order\" : \"").append(order).append("\"");
        sb.append(", ");
        sb.append(" \"uniqueness\" : \"").append(uniqueness).append("\"");
        sb.append(", ");
        if (relationships.size() > 0) {
            sb.append("\"relationships\" : [");
            for (int i = 0; i < relationships.size(); i++) {
                sb.append(relationships.get(i).toJsonCollection() );
                if (i < relationships.size() -1) {
                    //Miss off the final comma
                    sb.append( ", ");
                }
                
            }
            sb.append("], ");
        }
        sb.append( "\"return filter\" : { ");
        sb.append( "\"language\" : \"builtin\", ");
        sb.append( "\"name\" : \"");
        sb.append(returnFilter);
        sb.append("\" }, ");
        sb.append( "\"max depth\" : ");
        sb.append(maxDepth);
        sb.append( " }");
        return sb.toString();
    }
}
