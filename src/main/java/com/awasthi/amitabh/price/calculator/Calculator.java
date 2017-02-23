package com.awasthi.amitabh.price.calculator;

import com.awasthi.amitabh.price.MarketUpdate;
import com.awasthi.amitabh.price.TwoWayPrice;

/**
 * Description:-
 */
public interface Calculator {
    TwoWayPrice applyMarketUpdate(final MarketUpdate twoWayMarketPrice);
}
