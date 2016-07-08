package com.hybris.turntobackoffice.services;


import com.hybris.turntobackoffice.model.HistoricalOrderFeed;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TransactionFeedService {

    private Logger _logger = LoggerFactory.getLogger(getClass());

    private static final String ORDER_FILE_NAME = "turnto_orders_feed";
    private static final String FILE_TYPE = ".tsv";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    @Resource(name = "flexibleSearchService")
    private FlexibleSearchService flexibleSearchService;

    @Resource(name = "baseStoreService")
    private BaseStoreService baseStoreService;

    @Resource(name = "orderConverter")
    private Converter<OrderModel, OrderData> orderConverter;


    public File generateTransactionFeedFile(Date startDate) {
        List<HistoricalOrderFeed> historicalOrderFeeds = createHistoricalOrders(startDate);
        return writeOrdersToFile(historicalOrderFeeds);
    }

    public File generateDailyTransactionFeedFile(Date startDate) {
        List<HistoricalOrderFeed> historicalOrderFeeds = createForLastDayHistoricalOrders(startDate);
        return writeOrdersToFile(historicalOrderFeeds);
    }

    private File writeOrdersToFile(List<HistoricalOrderFeed> historicalOrderFeeds) {
        File tsv = null;
        FileWriter fw = null;
        PrintWriter pw = null;

        try {
            tsv = File.createTempFile(ORDER_FILE_NAME, FILE_TYPE);
            fw = new FileWriter(tsv);
            pw = new PrintWriter(fw);
            pw.println("ORDERID\tORDERDATE\tEMAIL\tITEMTITLE\tITEMURL\tITEMLINEID\tZIP\tFIRSTNAME\tLASTNAME\tSKU\tPRICE");
            int lineId = 0;

            for (HistoricalOrderFeed order : historicalOrderFeeds) {

                pw.print(order.getOrderId() + "\t");
                pw.print(order.getOrderDate() + "\t");
                pw.print(order.getEmail() + "\t");
                pw.print(order.getItemTitle() + "\t");
                pw.print(order.getItemURL() + "\t");
                pw.print(++lineId + "\t");
                pw.print(order.getZip() + "\t");
                pw.print(order.getFirstname() + "\t");
                pw.print(order.getLastname() + "\t");
                pw.print(order.getSku() + "\t");
                pw.print(order.getPrice() + "\t");
                pw.println();
            }

        } catch (IOException e) {
            _logger.error("Error with writing products to file, cause " + e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(pw);
            IOUtils.closeQuietly(fw);
        }

        return tsv;
    }

    private List<HistoricalOrderFeed> createHistoricalOrders(Date startDate) {
        DateTime start = new DateTime(startDate.getTime()).withTime(0, 0, 0, 0);
        DateTime end = start.plusDays(1);

        List<OrderData> orderDataList = getOrdersBetweenDates(start.toDate(), end.toDate());

        List<HistoricalOrderFeed> historicalOrderFeeds = getHistoricalOrderFeeds(orderDataList);

        return historicalOrderFeeds;
    }

    private List<HistoricalOrderFeed> getHistoricalOrderFeeds(List<OrderData> orderDataList) {
        List<HistoricalOrderFeed> historicalOrderFeeds = new ArrayList<>();

        for (OrderData orderData : orderDataList) {
            List<OrderEntryData> products = orderData.getEntries();

            for (OrderEntryData entryData : products) {
                HistoricalOrderFeed orderFeed = new HistoricalOrderFeed();
                populateOrderData(orderFeed, orderData);
                populateProductData(orderFeed, entryData);
                historicalOrderFeeds.add(orderFeed);
            }
        }

        return historicalOrderFeeds;

    }

    private List<HistoricalOrderFeed> createForLastDayHistoricalOrders(Date startDate) {
        DateTime end = new DateTime(startDate.getTime());
        DateTime start = end.minusDays(1);

        List<OrderData> orderDataList = getOrdersBetweenDates(start.toDate(), end.toDate());

        List<HistoricalOrderFeed> historicalOrderFeeds = getHistoricalOrderFeeds(orderDataList);

        return historicalOrderFeeds;
    }

    private List<OrderData> getOrdersBetweenDates(Date startDate, Date endDate) {

        String query = "SELECT {pk} FROM {" + OrderModel._TYPECODE + "} " + " WHERE {versionID} IS NULL " +
                "AND {store} = ?store " +
                "AND {" + OrderModel.CREATIONTIME + "} >= ?startDate " +
                "AND {" + OrderModel.CREATIONTIME + "} < ?endDate";

        final BaseStoreModel currentBaseStore = baseStoreService.getBaseStoreForUid(Config.getParameter("hybris.store.uid"));

        HashMap queryParams = new HashMap();

        queryParams.put("store", currentBaseStore);
        queryParams.put("startDate", startDate);
        queryParams.put("endDate", endDate);

        SearchResult<OrderModel> result = flexibleSearchService.search(query, queryParams);

        return Converters.convertAll(result.getResult(), orderConverter);
    }

    private void populateOrderData(HistoricalOrderFeed orderFeed, OrderData orderData) {
        orderFeed.setOrderId(orderData.getCode());
        orderFeed.setOrderDate(DATE_FORMAT.format(orderData.getCreated()));
        orderFeed.setFirstname(parseFirstName(orderData.getUser().getName()));
        orderFeed.setLastname(parseLastName(orderData.getUser().getName()));
        orderFeed.setEmail(orderData.getUser().getUid());
        orderFeed.setZip(orderData.getDeliveryAddress().getPostalCode());

    }

    private void populateProductData(HistoricalOrderFeed orderFeed, OrderEntryData entryData) {
        orderFeed.setItemTitle(entryData.getProduct().getName());
        orderFeed.setSku(entryData.getProduct().getCode());
        orderFeed.setItemURL(Config.getParameter("hybris.main.path") + Config.getParameter("hybris.store.path") + entryData.getProduct().getUrl());
        orderFeed.setPrice(entryData.getBasePrice().getValue().toString());

    }

    private String parseLastName(String name) {
        String[] names = name.split(" ");
        if (names.length > 1) {
            return names[1];
        }

        return "";
    }

    private String parseFirstName(String name) {
        String[] names = name.split(" ");
        if (names.length > 0) {
            return names[0];
        }

        return "";
    }

}
