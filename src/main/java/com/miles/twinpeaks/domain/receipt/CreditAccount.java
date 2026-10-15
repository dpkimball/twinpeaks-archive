package com.miles.twinpeaks.domain.receipt;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "credit_account", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CreditAccount {
    /*
              Table "public.credit_account"
       Column        |         Type          | Modifiers
---------------------+-----------------------+-----------
 credit_account_id   | integer               | not null
 user_id             | integer               | not null
 credit_account_name | character varying(32) | not null
Indexes:
    "credit_account_pkey" PRIMARY KEY, btree (credit_account_id)
    "credit_account_credit_account_name_key" UNIQUE CONSTRAINT, btree (credit_account_name)
Foreign-key constraints:
    "account_role_user_id_fkey" FOREIGN KEY (user_id) REFERENCES account(user_id)
Referenced by:
    TABLE "mb_receipt" CONSTRAINT "mb_receipt_credit_account_id_fkey" FOREIGN KEY (credit_account_id) REFERENCES credit_account(credit_account_id)


     */

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(nullable = false)
    private long user_id;

    @Column(nullable = false)
    private String account;

    public CreditAccount() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "CreditAccount {" +
                "id=" + id +
                ", account=" + account +
                '}';
    }
}
