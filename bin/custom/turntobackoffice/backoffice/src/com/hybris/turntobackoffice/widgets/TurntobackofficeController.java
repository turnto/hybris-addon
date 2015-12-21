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


public class TurntobackofficeController extends DefaultWidgetController {

    private Checkbox checkboxQA;
    private Checkbox checkboxRating;
    private Checkbox turntoOrderReporting;
    private Checkbox turntoCheckoutChatter;
    private Selectbox selectboxQA;
    private Selectbox selectboxRating;
    private Textbox siteKey;
    private Intbox cachingTime;

    private StateTurnFlagModel turntoQAModel;
    private StateTurnFlagModel turntoRatingModel;
    private StateTurnFlagModel turntoOrderReportingModel;
    private StateTurnFlagModel turntoCheckoutChatterModel;
    private TurnToGeneralStoreModel cachingTimeModel;
    private TurnToGeneralStoreModel siteKeyModel;

    @WireVariable
    private TurntobackofficeService turntobackofficeService;

    @Override
    public void initialize(Component comp) {
        super.initialize(comp);
        init(checkboxQA, selectboxQA);
        init(checkboxRating, selectboxRating);
        init(turntoOrderReporting, null);
        init(turntoCheckoutChatter, null);
        init(cachingTime);
        init(siteKey);
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

    @ViewEvent(componentID = "selectboxQA", eventName = Events.ON_SELECT)
    public void selectQAMode() throws InterruptedException {
        updateSetupType(checkboxQA, selectboxQA, turntoQAModel);
    }

    @ViewEvent(componentID = "selectboxRating", eventName = Events.ON_SELECT)
    public void selectRatingMode() throws InterruptedException {
        updateSetupType(checkboxRating, selectboxRating, turntoRatingModel);
    }

    @ViewEvent(componentID = "cachingTime", eventName = Events.ON_BLUR)
    public void setCachingTime() throws InterruptedException {
        cachingTimeModel.setValue(cachingTime.getValue());
        turntobackofficeService.saveToTurnToStore(cachingTimeModel);
        Messagebox.show("Caching time has been changed");
    }

    @ViewEvent(componentID = "siteKey", eventName = Events.ON_BLUR)
    public void setSiteKey() throws InterruptedException {
        siteKeyModel.setValue(siteKey.getValue());
        turntobackofficeService.saveToTurnToStore(siteKeyModel);
        Messagebox.show("Site Key has been changed");
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
        cachingTimeModel = turntobackofficeService.loadFromTurntoToStoreByKey(cachingTimeBox.getId());

        Integer cachingTime;

        if (cachingTimeModel == null) {
            cachingTime = 1;
            createCachingMode(cachingTimeBox, cachingTime);
            turntobackofficeService.saveToTurnToStore(cachingTimeModel);
        } else {
            cachingTime = (Integer) cachingTimeModel.getValue();
        }

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

    private boolean isModelEmpty(Textbox textbox) {
        siteKeyModel = turntobackofficeService.loadFromTurntoToStoreByKey(textbox.getId());
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
}
