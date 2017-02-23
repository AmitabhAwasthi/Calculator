package com.awasthi.amitabh.price.impl;

import com.awasthi.amitabh.domain.Instrument;
import com.awasthi.amitabh.domain.State;
import com.awasthi.amitabh.price.TwoWayPrice;

public class TwoWayPriceImpl implements TwoWayPrice {

    private final Instrument instrument;
    private final State state;
    private final double bidPrice;
    private final double offerPrice;
    private final double bidAmount;
    private final double offerAmount;

    public TwoWayPriceImpl(Instrument instrument,
                           State state,
                           double bidPrice,
                           double offerPrice,
                           double bidAmount,
                           double offerAmount) {

        this.instrument = instrument;
        this.state = state;
        this.bidPrice = bidPrice;
        this.offerPrice = offerPrice;
        this.bidAmount = bidAmount;
        this.offerAmount = offerAmount;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public State getState() {
        return state;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public double getOfferPrice() {
        return offerPrice;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public double getOfferAmount() {
        return offerAmount;
    }

    @Override
    public String toString() {
        return "TwoWayPriceImpl{" +
                "instrument=" + instrument +
                ", state=" + state +
                ", bidPrice=" + bidPrice +
                ", offerPrice=" + offerPrice +
                ", bidAmount=" + bidAmount +
                ", offerAmount=" + offerAmount +
                '}';
    }
}
