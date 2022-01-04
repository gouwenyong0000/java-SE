package pres.testmerag;

import java.io.Serializable;
import java.util.List;

public class PersonPojo  implements Serializable {

    String name;
    String nativePlace;
    List<String> address;
    String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "PersonPojo{" +
                "name='" + name + '\'' +
                ", nativePlace='" + nativePlace + '\'' +
                ", address=" + address +
                ", phone='" + phone + '\'' +
                '}';
    }
}
