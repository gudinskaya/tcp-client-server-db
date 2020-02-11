package org.anngudin.lab4.server;

public class MilitaryOrder {

    private String id, type, subtype, marking, client_id;

    public MilitaryOrder(String id, String type, String subtype, String marking, String client_id) {
        this.id = id;
        this.type = type;
        this.subtype = subtype;
        this.marking = marking;
        this.client_id = client_id;
    }

public String getId() {
    return id;
}
public String getType() {
    return type;
}
public String getSubtype() {
    return subtype;
}
public String getMarking() {
    return marking;
}
public String getClient_id() {
    return client_id;
}

public void setId(String id) {
    this.id = id;
}
public void setType(String type) {
    this.type = type;
}
public void setSubtype(String subtype) {
    this.subtype = subtype;
}
public void setMarking(String marking) {
    this.marking = marking;
}
public void setClient_id(String client_id) {
    this.client_id = client_id;
}

@Override
public String toString() {
    return "Client{" +
            "ClientId='" + id +
            "', type='" + type +
            "', subtype='" + subtype + 
            "', marking='" + marking +
            "', client_id='" + client_id +
            "'}";

}
}
