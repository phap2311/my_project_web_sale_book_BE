package com.example.book_storemanagement.model.dto;

import java.time.LocalDate;

public interface CartDTOI1 {
    Long getId();

    String getImage();

    String getAuthor();

    String getName();

    String getQuantity();

    LocalDate getDate_purchase();

    double getTotal_price();
    Long getBillId();
    Long getBooks_id();
}
