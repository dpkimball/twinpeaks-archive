package com.miles.twinpeaks.domain.receipt;

import com.fasterxml.jackson.annotation.*;
import com.miles.twinpeaks.domain.receipt.ReceiptItem;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/*
 * a simple domain entity doubling as a DTO
 */
@Entity
@Table(name = "receipt_item_category", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class , property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ReceiptItemCategory {
    /*
                                             Table "public.mb_category"
     Column     |         Type          | Collation | Nullable |                       Default
----------------+-----------------------+-----------+----------+-----------------------------------------------------
 mb_category_id | integer               |           | not null | nextval('mb_category_mb_category_id_seq'::regclass)
 category_name  | character varying(50) |           | not null |
Indexes:
    "mb_category_pkey" PRIMARY KEY, btree (mb_category_id)
    "mb_category_category_name_key" UNIQUE CONSTRAINT, btree (category_name)
Referenced by:
    TABLE "mb_receipt_item" CONSTRAINT "mb_category_id_fkey" FOREIGN KEY (mb_category_id) REFERENCES mb_category(mb_category_id)


     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(nullable = false, unique = true)
    private String category;

    public ReceiptItemCategory() {
    }

    public ReceiptItemCategory(String category) {
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    @JsonBackReference(value = "receipt-items")
    private Set<ReceiptItem> receiptItems = new HashSet<>(0);

    /**
     * Returns a collection with owned receipt items. The
     * returned collection is a defensive copy.
     *
     * @return a collection with owned twitter accounts
     */
    public Collection<ReceiptItem> getReceiptItems() {
        //defensive copy, nobody will be able to change
        //the list from the outside
        return new ArrayList<>(receiptItems);
    }

    /**
     * Add new receipt item to the category. The method keeps
     * relationships consistency:
     * * this category is set as the receipt_item category
     */
    public void addReceiptItem(ReceiptItem receiptItem) {
        //prevent endless loop
        if (receiptItems.contains(receiptItem))
            return;
        //add new receipt item
        receiptItems.add(receiptItem);
        //set myself into the receipt item
        receiptItem.setCategory(this);
    }

    /**
     * Removes the category from the receipt_item. The method keeps
     * relationships consistency:
     * * the category will no longer reference this receipt_item as its category
     */
    public void removeReceiptItem(ReceiptItem receiptItem) {
        //prevent endless loop
        if (!receiptItems.contains(receiptItem))
            return;
        //remove the category
        receiptItems.remove(receiptItem);
        //remove myself from the receipt item
        receiptItem.setCategory(null);
    }

    @Override
    public String toString() {
        return "Receipt Item Category {" +
                "id=" + id +
                ", category=" + category +
                '}';
    }
}
