package com.awasthi.amitabh.price.calculator.impl;

import com.awasthi.amitabh.domain.Instrument;
import com.awasthi.amitabh.domain.Market;
import com.awasthi.amitabh.domain.State;
import com.awasthi.amitabh.price.TwoWayPrice;
import com.awasthi.amitabh.price.cache.QuoteBook;
import com.awasthi.amitabh.price.cache.impl.QuoteBookImpl;
import com.awasthi.amitabh.price.calculator.Calculator;
import com.awasthi.amitabh.price.impl.MarketUpdateImpl;
import com.awasthi.amitabh.price.impl.TwoWayPriceImpl;
import com.awasthi.amitabh.util.MathUtil;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Author: Amitabh Awasthi
 * Description:-
 */
public class BasicVwapCalculatorTest {

    private final QuoteBook quoteBook = new QuoteBookImpl();
    private final Calculator calculator = new BasicVwapCalculator(quoteBook);

    @Test
    public void test_that_vwap_price_for_single_update_is_same_as_sent_price_if_price_is_firm() {
        final double bidPrice = 0.75685;
        final double offerPrice = 0.75695;
        final double bidAmount = 1000000;
        final double offerAmount = 3000000;

        final TwoWayPrice inputPrice = new TwoWayPriceImpl(Instrument.AUDUSD, State.FIRM, bidPrice, offerPrice, bidAmount, offerAmount);

        final TwoWayPrice outputPrice = calculator.applyMarketUpdate(new MarketUpdateImpl(Market.RTM, inputPrice));

        assertTrue(MathUtil.isQtyEqual(outputPrice.getBidAmount(), bidAmount));
        assertTrue(MathUtil.isPriceEqual(outputPrice.getBidPrice(), bidPrice));
        assertTrue(MathUtil.isQtyEqual(outputPrice.getOfferAmount(), offerAmount));
        assertTrue(MathUtil.isPriceEqual(outputPrice.getOfferPrice(), offerPrice));
    }

    @Test
    public void test_that_vwap_price_for_multiple_updates_is_expected() {
        final double bidPrice1 = 0.75685;
        final double offerPrice1 = 0.75695;
        final double bidAmount1 = 1000000;
        final double offerAmount1 = 3000000;

        final double bidPrice2 = 0.75683;
        final double offerPrice2 = 0.75694;
        final double bidAmount2 = 6000000;
        final double offerAmount2 = 2000000;

        final double bidPrice3 = 0.75684;
        final double offerPrice3 = 0.75697;
        final double bidAmount3 = 4000000;
        final double offerAmount3 = 7000000;

        calculator.applyMarketUpdate(new MarketUpdateImpl(Market.RTM, new TwoWayPriceImpl(Instrument.AUDUSD, State.FIRM, bidPrice1, offerPrice1, bidAmount1, offerAmount1)));
        calculator.applyMarketUpdate(new MarketUpdateImpl(Market.UBS, new TwoWayPriceImpl(Instrument.AUDUSD, State.FIRM, bidPrice2, offerPrice2, bidAmount2, offerAmount2)));
        final TwoWayPrice output = calculator.applyMarketUpdate(new MarketUpdateImpl(Market.CMZ, new TwoWayPriceImpl(Instrument.AUDUSD, State.FIRM, bidPrice3, offerPrice3, bidAmount3, offerAmount3)));

        assertFalse(output.getState().isIndicative());

        assertTrue(MathUtil.isQtyEqual(output.getBidAmount(), bidAmount1 + bidAmount2 + bidAmount3));
        assertTrue(MathUtil.isQtyEqual(output.getOfferAmount(), offerAmount1 + offerAmount2 + offerAmount3));

        final double expectedVwapBid = (bidPrice1 * bidAmount1 + bidPrice2 * bidAmount2 + bidPrice3 * bidAmount3) / (bidAmount1 + bidAmount2 + bidAmount3);
        final double expectedVwapOffer = (offerPrice1 * offerAmount1 + offerPrice2 * offerAmount2 + offerPrice3 * offerAmount3) / (offerAmount1 + offerAmount2 + offerAmount3);

        assertTrue(MathUtil.isPriceEqual(output.getBidPrice(), expectedVwapBid));
        assertTrue(MathUtil.isPriceEqual(output.getOfferPrice(), expectedVwapOffer));

    }

    @Test
    public void test_that_vwap_price_for_invalid_prices_in_cache_is_indicative() {
        final TwoWayPrice inputPrice1 = new TwoWayPriceImpl(Instrument.AUDUSD, State.FIRM, 0.0, 0.0, 0.0, 0.0);
        final TwoWayPrice inputPrice2 = new TwoWayPriceImpl(Instrument.AUDUSD, State.FIRM, 0.0, 0.0, 0.0, 0.0);

        final TwoWayPrice outputPrice1 = calculator.applyMarketUpdate(new MarketUpdateImpl(Market.RTM, inputPrice1));
        final TwoWayPrice outputPrice2 = calculator.applyMarketUpdate(new MarketUpdateImpl(Market.EBSAI, inputPrice2));

        assertTrue(outputPrice1.getState().isIndicative());
        assertTrue(outputPrice2.getState().isIndicative());
    }

    @Test
    public void test_that_vwap_price_is_indicative_if_all_prices_in_cache_are_invalid_or_indicative() {
        final TwoWayPrice inputPrice1 = new TwoWayPriceImpl(Instrument.AUDUSD, State.FIRM, 0.0, 0.0, 0.0, 0.0);
        final TwoWayPrice inputPrice2 = new TwoWayPriceImpl(Instrument.AUDUSD, State.INDICATIVE, 0.75675, 0.75699, 5000000, 4000000);

        final TwoWayPrice outputPrice1 = calculator.applyMarketUpdate(new MarketUpdateImpl(Market.RTM, inputPrice1));
        final TwoWayPrice outputPrice2 = calculator.applyMarketUpdate(new MarketUpdateImpl(Market.EBSAI, inputPrice2));

        assertTrue(outputPrice1.getState().isIndicative());
        assertTrue(outputPrice2.getState().isIndicative());
    }

    @Test
    public void test_that_indicative_update_does_NOT_modify_the_previous_vwap() {
        final double bidPrice = 0.75685;
        final double offerPrice = 0.75695;
        final double bidAmount = 1000000;
        final double offerAmount = 3000000;

        final TwoWayPrice inputPrice1 = new TwoWayPriceImpl(Instrument.AUDUSD, State.FIRM, bidPrice, offerPrice, bidAmount, offerAmount);
        final TwoWayPrice outputPrice1 = calculator.applyMarketUpdate(new MarketUpdateImpl(Market.RTM, inputPrice1));

        final TwoWayPrice inputPrice2 = new TwoWayPriceImpl(Instrument.AUDUSD, State.INDICATIVE, 0.75675, 0.75699, 5000000, 4000000);
        final TwoWayPrice outputPrice2 = calculator.applyMarketUpdate(new MarketUpdateImpl(Market.EBSAI, inputPrice2));

        assertTrue(MathUtil.isQtyEqual(outputPrice2.getBidAmount(), outputPrice1.getBidAmount()));
        assertTrue(MathUtil.isPriceEqual(outputPrice2.getBidPrice(), outputPrice1.getBidPrice()));
        assertTrue(MathUtil.isQtyEqual(outputPrice2.getOfferAmount(), outputPrice1.getOfferAmount()));
        assertTrue(MathUtil.isPriceEqual(outputPrice2.getOfferPrice(), outputPrice1.getOfferPrice()));
    }

}