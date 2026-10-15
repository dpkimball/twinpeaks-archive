package com.miles.twinpeaks.domain.receipt;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.miles.twinpeaks.domain.usda.api.USDAFood;

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
@Table(name = "receipt_item", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ReceiptItem {
    /*
    --------------------+-----------------------+-----------+----------+-------------------------------------------------------------
    mb_receipt_item_id | integer               |           | not null | nextval('mb_receipt_item_mb_receipt_item_id_seq'::regclass)
    mb_category_id     | integer               |           |          |
    mb_receipt_id      | integer               |           | not null |
    label              | character varying(50) |           |          |
    description        | character varying(50) |           |          |
    price              | numeric               |           | not null |
    taxable            | boolean               |           | not null |
    deposit            | numeric               |           |          |
    expiration         | date                  |           |          |
    usda_food_id       | integer               |           |          |
    Indexes:
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_fk")
    private ReceiptItemCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_fk")
    @JsonBackReference(value = "receipt-items")
    private Receipt receipt;

    public ReceiptItemCategory getCategory() {
        return this.category;
    }

    private boolean sameAsFormer(ReceiptItemCategory newCategory) {
        return category == null ? newCategory == null : category.equals(newCategory);
    }
    private boolean sameAsFormer(Receipt newReceipt) {
        return receipt == null ? newReceipt == null : receipt.equals(newReceipt);
    }

    public void setCategory(ReceiptItemCategory category) {
        //prevent endless loop
        if (sameAsFormer(category))
            return;
        //set new owner
        ReceiptItemCategory oldCategory = this.category;
        this.category = category;
        //remove from the old owner
        if (oldCategory != null)
            oldCategory.removeReceiptItem(this);
        //set myself to new owner
        if (category != null)
            category.addReceiptItem(this);
    }

    public void setReceipt(Receipt receipt) {
        //prevent endless loop
        if (sameAsFormer(receipt))
            return;
        //set new owner
        Receipt oldReceipt = this.receipt;
        this.receipt = receipt;
        //remove from the old owner
        if (oldReceipt != null)
            oldReceipt.removeReceiptItem(this);
        //set myself to new owner
        if (receipt != null)
            receipt.addReceiptItem(this);
    }

    @Column()
    private String label;

    @Column()
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Boolean taxable;

    @Column()
    private BigDecimal deposit;

    @Column()
    private Date expiration;

    @Column()
    private int barcodetype;

    @Column()
    private String barcodevalue;

    @OneToOne
    @JoinColumn(name = "usda_food_fk")
    private USDAFood usda_food;

    public ReceiptItem() {
    }

    public ReceiptItem(ReceiptItemCategory category) {
        //this.category = category;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getTaxable() {
        return taxable;
    }

    public void setTaxable(Boolean taxable) {
        this.taxable = taxable;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public USDAFood getUsda_food() {
        return usda_food;
    }

    public void setUsda_food(USDAFood usda_food) {
        this.usda_food = usda_food;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Receipt_Item {" +
                "id=" + id +
                //", category=" + category +
                ", label=" + label +
                ", price=" + price +
                ", usda_food='" + usda_food + '\'' +
                '}';
    }
}
