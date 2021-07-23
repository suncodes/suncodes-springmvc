package pojo;

public class LifeBO {
    private String city;
    private String person;
    private String target;

    public LifeBO() {
    }

    public LifeBO(String city, String person, String target) {
        this.city = city;
        this.person = person;
        this.target = target;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "LifeBO{" +
                "city='" + city + '\'' +
                ", person='" + person + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}
