package com.awasthi.amitabh.price.impl;

import com.awasthi.amitabh.domain.Market;
import com.awasthi.amitabh.price.MarketUpdate;
import com.awasthi.amitabh.price.TwoWayPrice;

/**
 * Improvements I would suggest:
 * 1. MarketUpdate should also contain getInstrument()
 */
public class MarketUpdateImpl implements MarketUpdate {

    private final Market market;
    private final TwoWayPrice twoWayPrice;

    public MarketUpdateImpl(Market market, TwoWayPrice twoWayPrice) {
        this.market = market;
        this.twoWayPrice = twoWayPrice;
    }

    public Market getMarket() {
        return market;
    }

    public TwoWayPrice getTwoWayPrice() {
        return twoWayPrice;
    }

    @Override
    public String toString() {
        return "MarketUpdateImpl{" +
                "market=" + market +
                ", twoWayPrice=" + twoWayPrice +
                '}';
    }
}
