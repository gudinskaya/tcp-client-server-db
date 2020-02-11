package lab4.cqrs.datatypes;

public class MilitaryClient {

    private String TIN, id, country;

    public MilitaryClient(String id, String TIN, String country) {
        this.id = id;
        this.TIN = TIN;
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public String getTIN() {
        return TIN;
    }

    public String getCountry() {
        return country;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setTIN(String TIN) {
        this.TIN = TIN;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Client{" +
                "ClientId='" + id +
                "', TIN='" + TIN +
                "', country='" + country + 
                "'}";
    }
}