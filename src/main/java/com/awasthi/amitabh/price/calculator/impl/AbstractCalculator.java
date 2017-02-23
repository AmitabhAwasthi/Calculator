package com.awasthi.amitabh.price.calculator.impl;

import com.awasthi.amitabh.domain.Instrument;
import com.awasthi.amitabh.price.MarketUpdate;
import com.awasthi.amitabh.price.TwoWayPrice;
import com.awasthi.amitabh.price.cache.QuoteBook;
import com.awasthi.amitabh.price.calculator.Calculator;

import java.util.Collection;

/**
 * Author: Amitabh Awasthi
 * Description:-
 */
public abstract class AbstractCalculator implements Calculator {

    private final QuoteBook quoteBook;

    public AbstractCalculator(QuoteBook quoteBook) {
        this.quoteBook = quoteBook;
    }

    public TwoWayPrice applyMarketUpdate(MarketUpdate twoWayMarketPrice) {
        final Instrument instrument = twoWayMarketPrice.getTwoWayPrice().getInstrument();
        final Collection<TwoWayPrice> currentQuoteBook = quoteBook.quoteCacheOnPrice(instrument, twoWayMarketPrice);
        return calculatePriceStrategy(instrument, currentQuoteBook);
    }

    protected abstract TwoWayPrice calculatePriceStrategy(Instrument instrument, Collection<TwoWayPrice> currentQuoteBook);
}
