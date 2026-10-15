package com.miles.twinpeaks.domain.usda.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * a simple domain entity doubling as a DTO
 */
@Entity
@Table(name = "usda_food", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class USDAFood {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(nullable = false)
    private long db_offset;

    @Column(nullable = false)
    private long ndbno;

    @Column()
    private String name;

    @Column()
    private int barcodetype;

    @Column()
    private String barcodevalue;

    @Column()
    private String upd;

    public USDAFood() {
    }

    public long getUsda_food_id() {
        return id;
    }

    public void setUsda_food_id(long usda_food_id) {
        this.id = usda_food_id;
    }

    public long getDb_offset() {
        return db_offset;
    }

    public void setDb_offset(long db_offset) {
        this.db_offset = db_offset;
    }

    public long getNdbno() {
        return ndbno;
    }

    public void setNdbno(long ndbno) {
        this.ndbno = ndbno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBarcodetype() {
        return barcodetype;
    }

    public void setBarcodetype(int barcodetype) {
        this.barcodetype = barcodetype;
    }

    public String getBarcodevalue() {
        return barcodevalue;
    }

    public void setBarcodevalue(String barcodevalue) {
        this.barcodevalue = barcodevalue;
    }

    public String getUpd() {
        return upd;
    }

    public void setUpd(String upd) {
        this.upd = upd;
    }

    @Override
    public String toString() {
        return "USDAFood {" +
                "id=" + id +
                ", name=" + name +
                ", ndbno=" + ndbno +
                ", barcode=" + barcodevalue +
                '}';
    }
}
