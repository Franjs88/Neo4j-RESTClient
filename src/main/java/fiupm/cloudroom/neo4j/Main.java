package fiupm.cloudroom.neo4j;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.net.URI;
import java.net.URISyntaxException;
import javax.ws.rs.core.MediaType;

/**
 * Hello world!
 *
 */
public class Main {

    private final static String SERVER_ROOT_URI = "http://localhost:7474/";
    private final static String nodeEntryPointUri = "http://localhost:7474/db/data/" + "node";

    private static URI createNode() {
        WebResource resource = Client.create()
                .resource(nodeEntryPointUri);
        // POST {} to the node entry point URI
        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .entity("{}")
                .post(ClientResponse.class);

        final URI location = response.getLocation();

        System.out.println(String.format("POST to [%s], status code [%d], location header [%s]",
                nodeEntryPointUri, response.getStatus(), location.toString()));

        response.close();
        return location;
    }

    private static void addProperty(URI nodeUri, String propertyName, String propertyValue) {
        String propertyUri = nodeUri.toString() + "/properties/" + propertyName;
        // http://localhost:7474/db/data/node/{node_id}/properties/{property_name}

        WebResource resource = Client.create()
                .resource(propertyUri);
        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .entity("\"" + propertyValue + "\"")
                .put(ClientResponse.class);

        System.out.println(String.format("PUT to [%s], status code [%d]",
                propertyUri, response.getStatus()));
        response.close();
    }

    private static URI addRelationship(URI startNode, URI endNode,
            String relationshipType, String jsonAttributes)
            throws URISyntaxException {
        URI fromUri = new URI(startNode.toString() + "/relationships");
        String relationshipJSON = generateJsonRelationship(endNode, relationshipType, jsonAttributes);

        WebResource resource = Client.create().resource(fromUri);
        //POST JSON to the relationship URI
        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .entity(relationshipJSON)
                .post(ClientResponse.class);

        final URI location = response.getLocation();
        System.out.println(String.format("POST to [%s], status code [%d], location header [%s]",
                fromUri, response.getStatus(), location.toString()));

        response.close();
        return location;
    }

    private static String generateJsonRelationship(URI endNode,
            String relationshipType, String... jsonAttributes) {
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"to\" : \"");
        sb.append(endNode.toString());
        sb.append("\", ");

        sb.append("\"type\" : \"");
        sb.append(relationshipType);
        if (jsonAttributes == null || jsonAttributes.length < 1) {
            sb.append("\"");
        } else {
            sb.append("\", \"data\" : ");
            for (int i = 0; i < jsonAttributes.length; i++) {
                sb.append(jsonAttributes[i]);
                if (i < jsonAttributes.length - 1) { // Miss off the final comma
                    sb.append(", ");
                }
            }
        }

        sb.append(" }");
        return sb.toString();
    }

    private static void addMetadataToProperty(URI relationshipUri, String name,
            String value) throws URISyntaxException {
        URI propertyUri = new URI(relationshipUri.toString() + "/properties");
        String entity = toJsonNameValuePairCollection(name, value);
        WebResource resource = Client.create().resource(propertyUri);
        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .entity(entity)
                .put(ClientResponse.class);

        System.out.println(String.format("PUT [%s] to [%s], status code [%d]",
                entity, propertyUri, response.getStatus()));
        response.close();
    }

    private static String toJsonNameValuePairCollection(String name,
            String value) {
        return String.format("{ \"%s\" : \"%s\" }", name, value);
    }
    
    

    public static void main(String[] args) throws URISyntaxException {
        WebResource resource = Client.create().resource(SERVER_ROOT_URI);
        ClientResponse response = resource.get(ClientResponse.class);

        System.out.println(String.format("GET on [%s], status code [%d]",
                SERVER_ROOT_URI, response.getStatus()));
        response.close();

        //Creamos nodos y añadimos sus propiedades
        URI firstNode = createNode();
        addProperty(firstNode, "nombre", "Joe Strummer");
        URI secondNode = createNode();
        addProperty(secondNode, "grupo", "The Clash");

        //Creamos relaciones entre nodos
        URI relationshipUri = addRelationship(firstNode, secondNode, "singer",
                "{ \"from\" : \"1976\", \"until\" : \"1986\" }");


        //Añadimos propiedades a la relacion creada
        addMetadataToProperty(relationshipUri, "stars", "5");
        
        
    }
}
