package com.miles.twinpeaks.domain.receipt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.miles.twinpeaks.domain.security.ApplicationUser;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;

/*
heartleaf=> \d mb_receipt
                                                  Table "public.mb_receipt"
       Column       |            Type             | Collation | Nullable |                      Default
--------------------+-----------------------------+-----------+----------+---------------------------------------------------
 mb_receipt_id      | integer                     |           | not null | nextval('mb_receipt_mb_receipt_id_seq'::regclass)
 user_id            | integer                     |           | not null |
 mb_store_id        | integer                     |           | not null |
 purchase_total     | numeric                     |           | not null |
 subpurchase_total  | numeric                     |           | not null |
 tax_total          | numeric                     |           | not null |
 total_items        | integer                     |           | not null |
 trace_no           | integer                     |           | not null |
 credit_account_id  | integer                     |           |          |
 receipt_created_on | timestamp without time zone |           | not null | CURRENT_DATE
 purchase_date      | timestamp without time zone |           |          |
Indexes:
    "mb_receipt_pkey" PRIMARY KEY, btree (mb_receipt_id)
Foreign-key constraints:
    "account_mb_receipt_user_id_fkey" FOREIGN KEY (user_id) REFERENCES account(user_id)
    "mb_receipt_credit_account_id_fkey" FOREIGN KEY (credit_account_id) REFERENCES credit_account(credit_account_id)
    "mb_receipt_mb_receipt_store_id_fkey" FOREIGN KEY (mb_store_id) REFERENCES mb_store(mb_store_id)

 */
@Entity
@Table(name = "Receipt", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "store_fk")
    private Store store;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_fk")
    @JsonIgnoreProperties
    @JsonProperty(value = "user")
    private ApplicationUser user;

    @Column(nullable = false)
    private BigDecimal subpurchase_total;

    @Column(nullable = false)
    private BigDecimal tax_total;

    @Column(nullable = false)
    private BigDecimal purchase_total;

    @Column(nullable = false)
    private int total_items;

    @Column(nullable = false)
    private int trace_no;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "credit_account_fk")
    private CreditAccount credit_account;

    @CreationTimestamp
    @Column(name = "create_date")
    private Date create_date;

    @UpdateTimestamp
    @Column(name = "update_date")
    private Date update_date;

    @Column(nullable = false)
    private Date purchase_date;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receipt", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "receipt-items")
    private Set<ReceiptItem> items = new HashSet<>(0);

    /**
     * Returns a collection with owned receipt items. The
     * returned collection is a defensive copy.
     *
     * @return a collection with owned twitter accounts
     */
    public Collection<ReceiptItem> getReceiptItems() {
        //defensive copy, nobody will be able to change
        //the list from the outside
        return new ArrayList<>(items);
    }
    public Receipt() {
    }

    public Receipt(Store store, Date purchase_date, BigDecimal purchase_total) {
        this.store = store;
        this.purchase_date = purchase_date;
        this.purchase_total = purchase_total;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Add new receipt item to the category. The method keeps
     * relationships consistency:
     * * this category is set as the receipt_item category
     */
    public void addReceiptItem(ReceiptItem receiptItem) {
        //prevent endless loop
        if (items.contains(receiptItem))
            return;
        //add new receipt item
        items.add(receiptItem);
        //set myself into the receipt item
        receiptItem.setReceipt(this);
    }

    /**
     * Removes the category from the receipt_item. The method keeps
     * relationships consistency:
     * * the category will no longer reference this receipt_item as its category
     */
    public void removeReceiptItem(ReceiptItem receiptItem) {
        //prevent endless loop
        if (!this.items.contains(receiptItem))
            return;
        //remove the category
        this.items.remove(receiptItem);
        //remove myself from the receipt item
        receiptItem.setReceipt(null);
    }

    public Store getStore() {
        return store;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public BigDecimal getSubpurchase_total() {
        return subpurchase_total;
    }

    public void setSubpurchase_total(BigDecimal subpurchase_total) {
        this.subpurchase_total = subpurchase_total;
    }

    public BigDecimal getTax_total() {
        return tax_total;
    }

    public void setTax_total(BigDecimal tax_total) {
        this.tax_total = tax_total;
    }

    public BigDecimal getPurchase_total() {
        return purchase_total;
    }

    public void setPurchase_total(BigDecimal purchase_total) {
        this.purchase_total = purchase_total;
    }

    public int getTotal_items() {
        return total_items;
    }

    public void setTotal_items(int total_items) {
        this.total_items = total_items;
    }

    public int getTrace_no() {
        return trace_no;
    }

    public void setTrace_no(int trace_no) {
        this.trace_no = trace_no;
    }

    public Date getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(Date purchase_date) {
        this.purchase_date = purchase_date;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public CreditAccount getCredit_account() {
        return credit_account;
    }

    public void setCredit_account(CreditAccount credit_account) {
        this.credit_account = credit_account;
    }

    @Override
    public String toString() {
        return "Receipt {" +
                "id=" + id +
                ", store=" + store +
                ", user_id=" + user.getId() +
                ", purchase_total=" + purchase_total +
                ", purchase_date='" + purchase_date + '\'' +
                '}';
    }
}
