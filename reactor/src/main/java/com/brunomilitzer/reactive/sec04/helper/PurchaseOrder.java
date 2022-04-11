package com.brunomilitzer.reactive.sec04.helper;

import com.brunomilitzer.reactive.util.Util;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PurchaseOrder {

    private String item;
    private String price;
    private Integer userId;

    public PurchaseOrder(int userId) {
        this.userId = userId;
        this.item = Util.faker().commerce().productName();
        this.price = Util.faker().commerce().price();
    }
}
