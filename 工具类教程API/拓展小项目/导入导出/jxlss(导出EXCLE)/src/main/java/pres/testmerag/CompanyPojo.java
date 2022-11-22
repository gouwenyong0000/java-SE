package pres.testmerag;

import java.io.Serializable;
import java.util.List;

public class CompanyPojo implements Serializable {
    String company;
    List<PersonPojo> employees;
    Integer rows;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public List<PersonPojo> getEmployees() {
        return employees;
    }

    public void setEmployees(List<PersonPojo> employees) {
        this.employees = employees;
    }

    public CompanyPojo() {
    }

    public CompanyPojo(String company, List<PersonPojo> employees) {
        this.company = company;
        this.employees = employees;
        this.rows = employees.size() * 3;

    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "CompanyPojo{" +
                "company='" + company + '\'' +
                ", employees=" + employees +
                ", rows=" + rows +
                '}';
    }
}
