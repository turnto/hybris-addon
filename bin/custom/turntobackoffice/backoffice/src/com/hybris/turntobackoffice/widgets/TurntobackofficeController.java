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
import com.hybris.cockpitng.util.CockpitComponentsUtils;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.WidgetUtils;
import com.hybris.turntobackoffice.enums.SetupType;
import com.hybris.turntobackoffice.model.StateTurnFlagModel;
import com.hybris.turntobackoffice.model.TurnToGeneralStoreModel;
import com.hybris.turntobackoffice.services.TurntobackofficeService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.*;


public class TurntobackofficeController extends DefaultWidgetController {

    public static final String DEFAULT_VERSION = "4_2";

    private Checkbox checkboxQA;
    private Checkbox checkboxRating;
    private Checkbox turntoOrderReporting;
    private Checkbox turntoCheckoutChatter;
    private Checkbox buyerComments;
    private Checkbox ccPinboard;
    private Checkbox customerGallery;


    private Selectbox selectboxQA;
    private Selectbox selectboxRating;
    private Textbox siteKey;
    private Textbox authKey;

    private Intbox cachingTime;

    private Selectbox selectboxVersion;

    private StateTurnFlagModel turntoQAModel;
    private StateTurnFlagModel turntoRatingModel;
    private StateTurnFlagModel turntoOrderReportingModel;
    private StateTurnFlagModel turntoCheckoutChatterModel;
    private StateTurnFlagModel turntoBuyerCommentsModel;
    private StateTurnFlagModel turntoCCPinboardModel;
    private StateTurnFlagModel turntoCustomerGalleryModel;

    private TurnToGeneralStoreModel cachingTimeModel;
    private TurnToGeneralStoreModel siteKeyModel;
    private TurnToGeneralStoreModel authKeyModel;
    private TurnToGeneralStoreModel invalidResponseModel;
    private TurnToGeneralStoreModel curentVersionModel;

    @WireVariable
    private TurntobackofficeService turntobackofficeService;

    @Override
    public void initialize(Component comp) {
        super.initialize(comp);
        init(checkboxQA, selectboxQA);
        init(checkboxRating, selectboxRating);
        init(turntoOrderReporting, null);
        init(turntoCheckoutChatter, null);
        init(buyerComments, null);
        init(ccPinboard, null);
        init(customerGallery, null);
        init(cachingTime);
        init(siteKey);
        initAuthKey(authKey);
        init(invalidResponseModel, "isSiteKeyInvalid");
        initVersionModel(selectboxVersion);
        initHiddenWidgets();

    }

    @ViewEvent(componentID = "sendFeedBtn", eventName = Events.ON_CLICK)
    public void sendProducts() throws Exception {
        Messagebox.show(turntobackofficeService.sendCatalogFeed());
    }

    @ViewEvent(componentID = "checkboxQA", eventName = Events.ON_CHECK)
    public void turnQA() throws InterruptedException {
        updateTurntoModuleStatus(checkboxQA, selectboxQA, turntoQAModel);
    }

    @ViewEvent(componentID = "checkboxRating", eventName = Events.ON_CHECK)
    public void turnRating() throws InterruptedException {
        updateTurntoModuleStatus(checkboxRating, selectboxRating, turntoRatingModel);
    }

    @ViewEvent(componentID = "turntoOrderReporting", eventName = Events.ON_CHECK)
    public void turnOrderReporting() throws InterruptedException {
        updateTurntoModuleStatus(turntoOrderReporting, null, turntoOrderReportingModel);
    }

    @ViewEvent(componentID = "turntoCheckoutChatter", eventName = Events.ON_CHECK)
    public void turnCheckoutChatter() throws InterruptedException {
        updateTurntoModuleStatus(turntoCheckoutChatter, null, turntoCheckoutChatterModel);
    }

    @ViewEvent(componentID = "buyerComments", eventName = Events.ON_CHECK)
    public void buyerComments() throws InterruptedException {
        updateTurntoModuleStatus(buyerComments, null, turntoBuyerCommentsModel);
    }

    @ViewEvent(componentID = "ccPinboard", eventName = Events.ON_CHECK)
    public void ccPinboard() throws InterruptedException {
        updateTurntoModuleStatus(ccPinboard, null, turntoCCPinboardModel);
    }

    @ViewEvent(componentID = "customerGallery", eventName = Events.ON_CHECK)
    public void customerGallery() throws InterruptedException {
        updateTurntoModuleStatus(customerGallery, null, turntoCustomerGalleryModel);
    }

    @ViewEvent(componentID = "selectboxQA", eventName = Events.ON_SELECT)
    public void selectQAMode() throws InterruptedException {
        updateSetupType(checkboxQA, selectboxQA, turntoQAModel);
    }

    @ViewEvent(componentID = "selectboxRating", eventName = Events.ON_SELECT)
    public void selectRatingMode() throws InterruptedException {
        updateSetupType(checkboxRating, selectboxRating, turntoRatingModel);
    }

    @ViewEvent(componentID = "cachingTime", eventName = Events.ON_CHANGE)
    public void setCachingTime() throws InterruptedException {
        cachingTimeModel.setValue(cachingTime.getValue());
        turntobackofficeService.saveToTurnToStore(cachingTimeModel);
        Messagebox.show("Caching time has been changed");
    }

    @ViewEvent(componentID = "siteKey", eventName = Events.ON_CHANGE)
    public void setSiteKey() throws InterruptedException {
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
        updateCurrentVersion(selectboxVersion, curentVersionModel);
    }

    private void init(Checkbox checkbox, Selectbox selectbox) {
        final String id = checkbox.getId();
        StateTurnFlagModel model = turntobackofficeService.loadByCheckboxId(id);
        setTurntoModel(id, model);
        checkbox.setChecked(model.getFlag());

        if (selectbox != null) {
            selectbox.setDisabled(!checkbox.isChecked());
            ((ListModelList) selectbox.getModel()).addSelection(model.getSetupType().getCode().toLowerCase().replace("embed", ""));
        }

    }

    private void init(Intbox cachingTimeBox) {
        cachingTimeModel = turntobackofficeService.loadFromTurnToStoreByKey(cachingTimeBox.getId());

        Integer cachingTime;

        if (cachingTimeModel == null) {
            cachingTime = 1;
            createCachingMode(cachingTimeBox, cachingTime);
            turntobackofficeService.saveToTurnToStore(cachingTimeModel);
        } else
            cachingTime = (Integer) cachingTimeModel.getValue();

        cachingTimeBox.setValue(cachingTime);
    }

    private void init(Textbox textbox) {
        String siteKey = "Enter your Site Key";

        if (isModelEmpty(textbox))
            turntobackofficeService.saveToTurnToStore(fillSiteKeyModel(textbox, siteKey));
        else
            siteKey = (String) siteKeyModel.getValue();

        textbox.setValue(siteKey);
    }

    private void init(TurnToGeneralStoreModel model, String key) {
        TurnToGeneralStoreModel storedModel = turntobackofficeService.loadFromTurnToStoreByKey(key);
        model = (storedModel == null) ? fillEmptyGeneralStoreModel(model, key) : storedModel;

        invalidResponseModel = model;
    }

    private void initVersionModel(Selectbox selectVersion) {
        String version = DEFAULT_VERSION;

        curentVersionModel = turntobackofficeService.loadFromTurnToStoreByKey(selectVersion.getId());

        if (curentVersionModel == null) {
            curentVersionModel = new TurnToGeneralStoreModel();
            curentVersionModel.setKey(selectVersion.getId());
            curentVersionModel.setValue(version);
            turntobackofficeService.saveToTurnToStore(curentVersionModel);
        } else {
            version = (String) curentVersionModel.getValue();
        }

        ((ListModelList) selectVersion.getModel()).addSelection(version);

    }

    private void initHiddenWidgets() {
        hideWidgets();
    }

    private void initAuthKey(Textbox authKey) {
        String siteKey = "Enter your Auth Key";

        authKeyModel = turntobackofficeService.loadFromTurnToStoreByKey(authKey.getId());

        if (authKeyModel == null) {
            authKeyModel = new TurnToGeneralStoreModel();
            authKeyModel.setKey(authKey.getId());
            authKeyModel.setValue(siteKey);
            turntobackofficeService.saveToTurnToStore(authKeyModel);
        } else {
            siteKey = (String) authKeyModel.getValue();
        }

        authKey.setValue(siteKey);
    }

    private TurnToGeneralStoreModel fillEmptyGeneralStoreModel(TurnToGeneralStoreModel model, String key) {
        if (model == null) {
            model = new TurnToGeneralStoreModel();
            model.setKey(key);
            model.setValue(false);

            turntobackofficeService.saveToTurnToStore(model);
        }
        return model;
    }

    private boolean isModelEmpty(Textbox textbox) {
        siteKeyModel = turntobackofficeService.loadFromTurnToStoreByKey(textbox.getId());
        return siteKeyModel == null;
    }

    private TurnToGeneralStoreModel fillSiteKeyModel(Textbox textbox, String key) {
        siteKeyModel = new TurnToGeneralStoreModel();
        siteKeyModel.setKey(textbox.getId());
        siteKeyModel.setValue(key);

        return siteKeyModel;
    }

    private void createCachingMode(Intbox cachingTimeBox, Integer cachingTime) {
        cachingTimeModel = new TurnToGeneralStoreModel();
        cachingTimeModel.setKey(cachingTimeBox.getId());
        cachingTimeModel.setValue(cachingTime);
    }

    private void setTurntoModel(String id, StateTurnFlagModel model) {
        switch (id) {
            case "checkboxQA":
                turntoQAModel = model;
                break;
            case "checkboxRating":
                turntoRatingModel = model;
                break;
            case "turntoOrderReporting":
                turntoOrderReportingModel = model;
                break;
            case "buyerComments":
                turntoBuyerCommentsModel = model;
                break;
            case "ccPinboard":
                turntoCCPinboardModel = model;
                break;
            case "customerGallery":
                turntoCustomerGalleryModel = model;
                break;
            default:
                turntoCheckoutChatterModel = model;
                break;
        }

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

        if (!"overlay".equals(type)) return type + "Embed";

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

}
