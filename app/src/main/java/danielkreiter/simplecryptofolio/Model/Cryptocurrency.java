package danielkreiter.simplecryptofolio.Model;

public class Cryptocurrency {

    private long mId;

    private String mName;

    public Cryptocurrency() {

    }

    public Cryptocurrency(String name) {
        this.mName = name;
    }


    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    @Override
    public String toString() {
        return this.getName();
    }


}
