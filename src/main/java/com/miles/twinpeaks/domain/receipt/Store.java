package com.miles.twinpeaks.domain.receipt;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Date;

/*
 * a simple domain entity doubling as a DTO
 */
@Entity
@Table(name = "Store", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Store {
    /*
                                             Table "public.store"
     Column      |         Type          | Collation | Nullable |                    Default
    ------------------+-----------------------+-----------+----------+-----------------------------------------------
     store_id      | integer               |           | not null | nextval('store_store_id_seq'::regclass)
     store_number  | character varying(50) |           | not null |
     store_address | character varying(50) |           | not null |
     store_city    | character varying(50) |           | not null |
     store_state   | character varying(50) |           | not null |
     store_zip     | integer               |           | not null |
     store_phone   | character varying(50) |           | not null |
     Indexes:
        "store_pkey" PRIMARY KEY, btree (store_id)
        "store_store_number_key" UNIQUE CONSTRAINT, btree (store_number)

     */

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String zip;

    @Column(nullable = false)
    private String phone;


    public Store() {
    }

    public long getId() {
        return this.id;
    }

    // for tests ONLY
    public void setId(long id) {
        this.id = id;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Store {" +
                "id=" + id +
                ", number=" + number +
                ", city=" + city +
                ", state=" + state +
                ", phone='" + phone + '\'' +
                '}';
    }
}
