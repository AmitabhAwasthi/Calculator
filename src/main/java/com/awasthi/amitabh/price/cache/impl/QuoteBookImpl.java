package com.awasthi.amitabh.price.cache.impl;

import com.awasthi.amitabh.domain.Instrument;
import com.awasthi.amitabh.domain.Market;
import com.awasthi.amitabh.price.MarketUpdate;
import com.awasthi.amitabh.price.TwoWayPrice;
import com.awasthi.amitabh.price.cache.QuoteBook;

import java.util.Collection;
import java.util.EnumMap;

/**
 * In current example, this data can be held within the calculator. But it's more flexible to have it as a separate entity
 * Arrival of price update and updating of cache usually would happen on a different thread compared to calculation of
 * vwaps for use in order execution etc
 */
public class QuoteBookImpl implements QuoteBook {
    /*
     * With size of the values known, we can even use a fixed length array and thus avoid creating any intermediate
     * objects during iteration which a collection or call to iterator would.
     * Ignoring for lack of time
     */
    private final EnumMap<Instrument, EnumMap<Market, TwoWayPrice>> priceCache =
            new EnumMap<Instrument, EnumMap<Market, TwoWayPrice>>(Instrument.class);

    public QuoteBookImpl() {
        for (Instrument instrument : Instrument.VALUES) {
            priceCache.put(instrument, new EnumMap<Market, TwoWayPrice>(Market.class));
        }
    }

    public Collection<TwoWayPrice> quoteCacheOnPrice(Instrument instrument, MarketUpdate marketUpdate) {
        final Market market = marketUpdate.getMarket();
        priceCache.get(instrument).put(market, marketUpdate.getTwoWayPrice());

        return priceCache.get(instrument).values();
    }
}
