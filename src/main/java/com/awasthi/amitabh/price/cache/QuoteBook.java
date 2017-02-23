package com.awasthi.amitabh.price.cache;

import com.awasthi.amitabh.domain.Instrument;
import com.awasthi.amitabh.price.MarketUpdate;
import com.awasthi.amitabh.price.TwoWayPrice;

import java.util.Collection;

/**
 * Description:- This can be further broken into a QuoteBook (to only query the current quotebook) and a MutableQuoteBook (to apply incoming updates) depending on whether we want to
 * lazily calculate computed price or on each tick
 */
public interface QuoteBook {

    Collection<TwoWayPrice> quoteCacheOnPrice(Instrument instrument, MarketUpdate marketUpdate);
}
