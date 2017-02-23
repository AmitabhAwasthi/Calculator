package com.awasthi.amitabh.price;

import com.awasthi.amitabh.domain.Instrument;
import com.awasthi.amitabh.domain.State;


public interface TwoWayPrice {

    Instrument getInstrument();

    State getState();

    double getBidPrice();

    double getOfferAmount();

    double getOfferPrice();

    double getBidAmount();
}
