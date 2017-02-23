package com.awasthi.amitabh.price;

import com.awasthi.amitabh.domain.Market;


public interface MarketUpdate {
    Market getMarket();

    TwoWayPrice getTwoWayPrice();
}