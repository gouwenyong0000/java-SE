package pres.testimage;

import java.io.Serializable;

public class PersonImage  implements Serializable {

    String name;
    String photo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
