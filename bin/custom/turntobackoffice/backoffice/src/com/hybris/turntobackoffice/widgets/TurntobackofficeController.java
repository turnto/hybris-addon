/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package com.hybris.turntobackoffice.widgets;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.turntobackoffice.enums.SetupType;
import com.hybris.turntobackoffice.model.StateTurnFlagModel;
import com.hybris.turntobackoffice.model.TurnToGeneralStoreModel;
import com.hybris.turntobackoffice.services.TurntobackofficeService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.*;

import java.util.Date;


public class TurntobackofficeController extends DefaultWidgetController {

    public static final String DEFAULT_VERSION = "4_2";
    public static final String OVERLAY_SETUP_TYPE = "overlay";

    private Checkbox checkboxQA;
    private Checkbox checkboxRating;
    private Checkbox turntoOrderReporting;
    private Checkbox turntoCheckoutChatter;
    private Checkbox buyerComments;
    private Checkbox ccPinboard;
    private Checkbox customerGallery;
    private Checkbox customerGalleryRowWidget;
    private Checkbox ccPinboardTeaser;

    private Checkbox ongoingTransactionsFeed;

    private Selectbox selectboxQA;
    private Selectbox selectboxRating;
    private Textbox siteKey;
    private Textbox authKey;

    private Datebox startDate;
    private Timebox dailyFeedTime;

    private Intbox cachingTime;

    private Selectbox selectboxVersion;

    @WireVariable
    private TurntobackofficeService turntobackofficeService;

    @Override
    public void initialize(Component comp) {
        super.initialize(comp);

        initCheckBoxes();
        initCachingTime(cachingTime);
        initSiteKey(siteKey);
        initAuthKey(authKey);
        initInvalidResponse("isSiteKeyInvalid");
        initVersionModel(selectboxVersion);
        initDailyFeedTime(dailyFeedTime);
        initHiddenWidgets();

    }

    @ViewEvent(componentID = "sendFeedBtn", eventName = Events.ON_CLICK)
    public void sendProducts() throws Exception {
        Messagebox.show(turntobackofficeService.sendCatalogFeed());
    }

    @ViewEvent(componentID = "sendTransactionsFeedBtn", eventName = Events.ON_CLICK)
    public void sendTransactionsFeed() throws Exception {
        Date date = startDate.getValue();
        Messagebox.show(turntobackofficeService.sendTransactionsFeed(date));
    }

    @ViewEvent(componentID = "ongoingTransactionsFeed", eventName = Events.ON_CHECK)
    public void ongoingTransactionsFeed() throws InterruptedException {
        boolean isChecked = ongoingTransactionsFeed.isChecked();

        if (isChecked) {
            Date date = dailyFeedTime.getValue();

            TurnToGeneralStoreModel dailyFeedTimeModel = turntobackofficeService.loadFromTurnToStoreByKey(dailyFeedTime.getId());
            dailyFeedTimeModel.setValue(date.getTime());
            turntobackofficeService.saveToTurnToStore(dailyFeedTimeModel);
        }

        turntobackofficeService.turnCronJob(isChecked);

        StateTurnFlagModel ongoingTransactionsFeedModel = getStateTurnFlagModelById(ongoingTransactionsFeed);
        updateTurntoModuleStatus(ongoingTransactionsFeed, null, ongoingTransactionsFeedModel);
    }

    @ViewEvent(componentID = "checkboxQA", eventName = Events.ON_CHECK)
    public void turnQA() throws InterruptedException {
        StateTurnFlagModel turntoQAModel = getStateTurnFlagModelById(checkboxQA);
        updateTurntoModuleStatus(checkboxQA, selectboxQA, turntoQAModel);
    }


    @ViewEvent(componentID = "checkboxRating", eventName = Events.ON_CHECK)
    public void turnRating() throws InterruptedException {
        StateTurnFlagModel turntoRatingModel = getStateTurnFlagModelById(checkboxRating);
        updateTurntoModuleStatus(checkboxRating, selectboxRating, turntoRatingModel);
    }

    @ViewEvent(componentID = "turntoOrderReporting", eventName = Events.ON_CHECK)
    public void turnOrderReporting() throws InterruptedException {
        StateTurnFlagModel turntoOrderReportingModel = getStateTurnFlagModelById(turntoOrderReporting);
        updateTurntoModuleStatus(turntoOrderReporting, null, turntoOrderReportingModel);
    }

    @ViewEvent(componentID = "turntoCheckoutChatter", eventName = Events.ON_CHECK)
    public void turnCheckoutChatter() throws InterruptedException {
        StateTurnFlagModel turntoCheckoutChatterModel = getStateTurnFlagModelById(turntoCheckoutChatter);
        updateTurntoModuleStatus(turntoCheckoutChatter, null, turntoCheckoutChatterModel);
    }

    @ViewEvent(componentID = "buyerComments", eventName = Events.ON_CHECK)
    public void buyerComments() throws InterruptedException {
        StateTurnFlagModel turntoBuyerCommentsModel = getStateTurnFlagModelById(buyerComments);

        boolean checked = buyerComments.isChecked();

        turnCheckbox(turntoCheckoutChatter, checked);
        turnCheckbox(ccPinboard, checked);
        turnCheckbox(ccPinboardTeaser, checked);

        updateTurntoModuleStatus(buyerComments, null, turntoBuyerCommentsModel);

    }

    @ViewEvent(componentID = "ccPinboard", eventName = Events.ON_CHECK)
    public void ccPinboard() throws InterruptedException {
        StateTurnFlagModel turntoCCPinboardModel = getStateTurnFlagModelById(ccPinboard);
        updateTurntoModuleStatus(ccPinboard, null, turntoCCPinboardModel);
    }

    @ViewEvent(componentID = "ccPinboardTeaser", eventName = Events.ON_CHECK)
    public void ccPinboardTeaser() throws InterruptedException {
        StateTurnFlagModel turntoCCPinboardTeaserModel = getStateTurnFlagModelById(ccPinboardTeaser);
        updateTurntoModuleStatus(ccPinboardTeaser, null, turntoCCPinboardTeaserModel);
    }

    @ViewEvent(componentID = "customerGallery", eventName = Events.ON_CHECK)
    public void customerGallery() throws InterruptedException {
        StateTurnFlagModel turntoCustomerGalleryModel = getStateTurnFlagModelById(customerGallery);
        updateTurntoModuleStatus(customerGallery, null, turntoCustomerGalleryModel);
    }

    @ViewEvent(componentID = "customerGalleryRowWidget", eventName = Events.ON_CHECK)
    public void customerGalleryRowWidget() throws InterruptedException {
        StateTurnFlagModel turntoCustomerGalleryRowWidgetModel = getStateTurnFlagModelById(customerGalleryRowWidget);
        updateTurntoModuleStatus(customerGalleryRowWidget, null, turntoCustomerGalleryRowWidgetModel);
    }

    @ViewEvent(componentID = "selectboxQA", eventName = Events.ON_SELECT)
    public void selectQAMode() throws InterruptedException {
        StateTurnFlagModel turntoQAModel = getStateTurnFlagModelById(checkboxQA);
        updateSetupType(checkboxQA, selectboxQA, turntoQAModel);
    }

    @ViewEvent(componentID = "selectboxRating", eventName = Events.ON_SELECT)
    public void selectRatingMode() throws InterruptedException {
        StateTurnFlagModel turntoRatingModel = getStateTurnFlagModelById(checkboxRating);
        updateSetupType(checkboxRating, selectboxRating, turntoRatingModel);
    }

    @ViewEvent(componentID = "cachingTime", eventName = Events.ON_CHANGE)
    public void setCachingTime() throws InterruptedException {
        TurnToGeneralStoreModel cachingTimeModel = turntobackofficeService.loadFromTurnToStoreByKey(cachingTime.getId());
        cachingTimeModel.setValue(cachingTime.getValue());
        turntobackofficeService.saveToTurnToStore(cachingTimeModel);

        Messagebox.show("Caching time has been changed");
    }

    @ViewEvent(componentID = "siteKey", eventName = Events.ON_CHANGE)
    public void setSiteKey() throws InterruptedException {
        TurnToGeneralStoreModel siteKeyModel = turntobackofficeService.loadFromTurnToStoreByKey(siteKey.getId());

        String oldValue = (String) siteKeyModel.getValue();
        String newVal = siteKey.getValue().trim();

        if (!oldValue.equals(newVal)) {
            siteKeyModel.setValue(newVal);
            turntobackofficeService.saveToTurnToStore(siteKeyModel);

            turntobackofficeService.invalidateCache();

            Messagebox.show("Site Key has been changed");
        }
    }

    @ViewEvent(componentID = "authKey", eventName = Events.ON_CHANGE)
    public void setAuthKey() throws InterruptedException {
        TurnToGeneralStoreModel authKeyModel = turntobackofficeService.loadFromTurnToStoreByKey(authKey.getId());

        String oldValue = (String) authKeyModel.getValue();
        String newVal = authKey.getValue().trim();

        if (!oldValue.equals(newVal)) {
            authKeyModel.setValue(newVal);
            turntobackofficeService.saveToTurnToStore(authKeyModel);
            turntobackofficeService.invalidateCache();

            Messagebox.show("Auth Key has been changed");
        }
    }

    @ViewEvent(componentID = "selectboxVersion", eventName = Events.ON_SELECT)
    public void selectCurentVesion() throws InterruptedException {
        hideWidgets();

        TurnToGeneralStoreModel curentVersionModel = turntobackofficeService.loadFromTurnToStoreByKey(selectboxVersion.getId());
        updateCurrentVersion(selectboxVersion, curentVersionModel);
    }


    private void initCheckBoxes() {
        initCheckBox(checkboxQA, selectboxQA, null);
        initCheckBox(checkboxRating, selectboxRating, null);
        initCheckBox(turntoOrderReporting, null, null);
        initCheckBox(buyerComments, null, null);
        initCheckBox(ccPinboard, null, buyerComments);
        initCheckBox(turntoCheckoutChatter, null, buyerComments);
        initCheckBox(ccPinboardTeaser, null, buyerComments);
        initCheckBox(customerGallery, null, null);
        initCheckBox(customerGalleryRowWidget, null, null);
        initCheckBox(ongoingTransactionsFeed, null, null);
    }

    private void initDailyFeedTime(Timebox dailyFeedTime) {
        long time = new Date().getTime();

        TurnToGeneralStoreModel dailyFeedTimeModel = turntobackofficeService.loadFromTurnToStoreByKey(dailyFeedTime.getId());

        if (dailyFeedTimeModel == null) {
            createTurnToGeneralStoreModel(dailyFeedTime.getId(), time);
        } else {
            time = (Long) dailyFeedTimeModel.getValue();
        }

        dailyFeedTime.setValue(new Date(time));
    }

    private void initCheckBox(Checkbox checkbox, Selectbox selectbox, Checkbox parent) {
        StateTurnFlagModel model = getStateTurnFlagModelById(checkbox);
        checkbox.setChecked(model.getFlag());

        if (selectbox != null) {
            selectbox.setDisabled(!checkbox.isChecked());
            ((ListModelList) selectbox.getModel()).addSelection(model.getSetupType().getCode().toLowerCase().replace("embed", ""));
        }

        if (parent != null) {
            checkbox.setDisabled(!parent.isChecked());
        }

    }

    private void initCachingTime(Intbox cachingTimeBox) {
        Integer cachingTime = 1;

        TurnToGeneralStoreModel cachingTimeModel = turntobackofficeService.loadFromTurnToStoreByKey(cachingTimeBox.getId());

        if (cachingTimeModel == null) {
            createTurnToGeneralStoreModel(cachingTimeBox.getId(), cachingTime);
        } else {
            cachingTime = (Integer) cachingTimeModel.getValue();
        }

        cachingTimeBox.setValue(cachingTime);
    }

    private void initSiteKey(Textbox textbox) {
        String siteKey = "Enter your Site Key";
        TurnToGeneralStoreModel siteKeyModel = turntobackofficeService.loadFromTurnToStoreByKey(textbox.getId());

        if (siteKeyModel == null) {
            createTurnToGeneralStoreModel(textbox.getId(), siteKey);
        } else {
            siteKey = (String) siteKeyModel.getValue();
        }

        textbox.setValue(siteKey);
    }

    private void initInvalidResponse(String key) {
        TurnToGeneralStoreModel storedModel = turntobackofficeService.loadFromTurnToStoreByKey(key);

        if (storedModel == null) {
            createTurnToGeneralStoreModel(key, false);
        }

    }

    private TurnToGeneralStoreModel createTurnToGeneralStoreModel(String key, Object value) {
        TurnToGeneralStoreModel storedModel = new TurnToGeneralStoreModel();
        storedModel.setKey(key);
        storedModel.setValue(value);

        turntobackofficeService.saveToTurnToStore(storedModel);
        return storedModel;
    }

    private void initVersionModel(Selectbox selectVersion) {
        String version = DEFAULT_VERSION;

        TurnToGeneralStoreModel curentVersionModel = turntobackofficeService.loadFromTurnToStoreByKey(selectVersion.getId());

        if (curentVersionModel == null) {
            createTurnToGeneralStoreModel(selectVersion.getId(), version);
        } else {
            version = (String) curentVersionModel.getValue();
        }

        ((ListModelList) selectVersion.getModel()).addSelection(version);

    }

    private void initHiddenWidgets() {
        hideWidgets();
    }

    private void initAuthKey(Textbox authKey) {
        String authKeyValue = "Enter your Auth Key";

        TurnToGeneralStoreModel authKeyModel = turntobackofficeService.loadFromTurnToStoreByKey(authKey.getId());

        if (authKeyModel == null) {
            createTurnToGeneralStoreModel(authKey.getId(), authKeyValue);
        } else {
            authKeyValue = (String) authKeyModel.getValue();
        }

        authKey.setValue(authKeyValue);
    }

    private StateTurnFlagModel getStateTurnFlagModelById(Checkbox checkBox) {
        final String id = checkBox.getId();
        return turntobackofficeService.loadByCheckboxId(id);
    }

    private void updateTurntoModuleStatus(Checkbox checkbox, Selectbox selectbox, StateTurnFlagModel model) throws InterruptedException {
        final boolean isChecked = checkbox.isChecked();
        model.setFlag(isChecked);

        if (selectbox != null) {
            final String setupType = getSetupType(selectboxRating);
            selectbox.setDisabled(!isChecked);
            if (isChecked) model.setSetupType(SetupType.valueOf(setupType.toUpperCase()));
        }

        turntobackofficeService.saveStateTurnFlag(model);
        Messagebox.show(turntobackofficeService.createMessage(isChecked));
    }

    private void updateSetupType(Checkbox checkbox, Selectbox selectbox, StateTurnFlagModel model) throws InterruptedException {
        boolean turnFlag = checkbox.isChecked();

        String setupType = getSetupType(selectbox);

        if (turnFlag) {
            model.setSetupType(SetupType.valueOf(setupType.toUpperCase()));
            turntobackofficeService.saveStateTurnFlag(model);
            Messagebox.show("Setup type has been changed");
        }

    }

    private String getSetupType(Selectbox selectbox) {
        int index = selectbox.getSelectedIndex();
        String type = (String) selectbox.getModel().getElementAt(index);

        if (!OVERLAY_SETUP_TYPE.equals(type)) return type + "Embed";

        return type;
    }

    private void updateCurrentVersion(Selectbox selectVersion, TurnToGeneralStoreModel curentVersionModel) throws InterruptedException {
        String selectedVersion = getSelectedVersion(selectVersion);

        curentVersionModel.setValue(selectedVersion);
        turntobackofficeService.saveToTurnToStore(curentVersionModel);

        Messagebox.show("Current version has been changed");
    }

    private String getSelectedVersion(Selectbox selectboxVersion) {
        int index = selectboxVersion.getSelectedIndex();
        return (String) selectboxVersion.getModel().getElementAt(index);
    }

    private void hideWidgets() {
        String selectedVersion = getSelectedVersion(selectboxVersion);

        boolean showNewWidgets = selectedVersion.equals(DEFAULT_VERSION);

        Component customerGallery = getWidgetInstanceManager().getWidgetslot().getFellow("customer-gallery");
        customerGallery.setVisible(!showNewWidgets);
    }

    private void turnCheckbox(Checkbox checkbox, boolean checked) {
        checkbox.setDisabled(!checked);
        checkbox.setChecked(checked);
        StateTurnFlagModel stateTurnFlagModel = getStateTurnFlagModelById(checkbox);
        stateTurnFlagModel.setFlag(checked);
        turntobackofficeService.saveStateTurnFlag(stateTurnFlagModel);
    }

}
