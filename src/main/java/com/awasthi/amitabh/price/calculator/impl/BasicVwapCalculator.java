package com.awasthi.amitabh.price.calculator.impl;

import com.awasthi.amitabh.domain.Instrument;
import com.awasthi.amitabh.domain.State;
import com.awasthi.amitabh.price.TwoWayPrice;
import com.awasthi.amitabh.price.cache.QuoteBook;
import com.awasthi.amitabh.price.impl.TwoWayPriceImpl;
import com.awasthi.amitabh.util.MathUtil;

import java.util.Collection;

import static com.awasthi.amitabh.util.MathUtil.*;

public class BasicVwapCalculator extends AbstractCalculator {

    public BasicVwapCalculator(QuoteBook quoteBook) {
        super(quoteBook);
    }

    @Override
    protected TwoWayPrice calculatePriceStrategy(Instrument instrument, Collection<TwoWayPrice> currentQuoteBook) {
        double totalBaseBidQty = 0.0d;
        double totalTermsBidQty = 0.0d;
        double totalBaseOfferQty = 0.0d;
        double totalTermsOfferQty = 0.0d;

        for (TwoWayPrice twoWayPrice : currentQuoteBook) {
            if (!twoWayPrice.getState().isIndicative()) {

                /*
                 * once either of the price/qty is undefined, we won't include it in the calculation.
                 * Such a situation pretty much implies a bug in the system or pricing source or improper staling
                 */

                final double bidPrice = twoWayPrice.getBidPrice();
                final double offerPrice = twoWayPrice.getOfferPrice();
                final double bidAmount = twoWayPrice.getBidAmount();
                final double offerAmount = twoWayPrice.getOfferAmount();

                if (isValid(bidPrice, bidAmount)) {
                    totalBaseBidQty += twoWayPrice.getBidAmount();
                    totalTermsBidQty += twoWayPrice.getBidAmount() * twoWayPrice.getBidPrice();
                }

                if (isValid(offerPrice, offerAmount)) {
                    totalBaseOfferQty += twoWayPrice.getOfferAmount();
                    totalTermsOfferQty += twoWayPrice.getOfferAmount() * twoWayPrice.getOfferPrice();
                }

            } // no need to include indicative prices
        }

        final double bidVwap;
        final double offerVwap;

        if (isQtyUndefined(totalBaseBidQty)) {
            bidVwap = 0.0d;
        } else {
            bidVwap = totalTermsBidQty / totalBaseBidQty;
        }

        if (isQtyUndefined(totalBaseOfferQty)) {
            offerVwap = 0.0d;
        } else {
            offerVwap = totalTermsOfferQty / totalBaseOfferQty;
        }

        if (isPriceUndefined(bidVwap) && isPriceUndefined(offerVwap)) {
            /*
             * Just return an unusable price i.e. there is no vwap available
             * Ideally, such cases are better handled and price feeds are staled in such circumstances
             */
            return new TwoWayPriceImpl(instrument, State.INDICATIVE, 0.0, 0.0, 0.0, 0.0);
        } else {
            return new TwoWayPriceImpl(instrument, State.FIRM, bidVwap, offerVwap, totalBaseBidQty, totalBaseOfferQty);
        }

    }

    private boolean isValid(double price, double amount) {
        return !((isPriceUndefined(price) || isQtyUndefined(amount)));
    }
}
