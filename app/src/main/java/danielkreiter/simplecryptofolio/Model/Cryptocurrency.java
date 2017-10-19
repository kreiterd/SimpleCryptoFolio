package danielkreiter.simplecryptofolio.Model;

public class Cryptocurrency {

    private long id;
    private String name;

    public Cryptocurrency() {

    }

    public Cryptocurrency(String name) {
        this.name = name;
    }


    public long getId() {
        return id;
    }

    public void setId(long mId) {
        this.id = mId;
    }

    public String getName() {
        return name;
    }

    public void setName(String mName) {
        this.name = mName;
    }

    @Override
    public String toString() {
        return this.getName();
    }


}
