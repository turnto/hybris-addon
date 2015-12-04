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
import com.hybris.turntobackoffice.services.TurntobackofficeService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Selectbox;


public class TurntobackofficeController extends DefaultWidgetController {

    private Checkbox checkboxQA;
    private Checkbox checkboxRating;
    private Selectbox selectboxQA;
    private Selectbox selectboxRating;
    private StateTurnFlagModel turntoQAModel;
    private StateTurnFlagModel turntoRatingModel;

    @WireVariable
    private TurntobackofficeService turntobackofficeService;

    @Override
    public void initialize(Component comp) {
        super.initialize(comp);
        init(checkboxQA, selectboxQA);
        init(checkboxRating, selectboxRating);
        selectboxRating.setDisabled(true);
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
        Messagebox.show(turntobackofficeService.createMessage(true));
    }

    @ViewEvent(componentID = "turntoCheckoutChatter", eventName = Events.ON_CHECK)
    public void turnCheckoutChatter() throws InterruptedException {
        Messagebox.show(turntobackofficeService.createMessage(true));
    }

    @ViewEvent(componentID = "selectboxQA", eventName = Events.ON_SELECT)
    public void selectQAMode() throws InterruptedException {
        boolean turnFlag = checkboxQA.isChecked();

        String setupType = getSetupType(selectboxQA);

        if (turnFlag) {
            turntoQAModel.setSetupType(SetupType.valueOf(setupType.toUpperCase()));
            turntobackofficeService.saveStateTurnFlag(turntoQAModel);
        }
    }

    @ViewEvent(componentID = "selectboxRating", eventName = Events.ON_SELECT)
    public void selectRatingMode() throws InterruptedException {
        Messagebox.show(turntobackofficeService.createMessage(true));
    }

    private void init(Checkbox checkbox, Selectbox selectbox) {
        final String id = checkbox.getId();
        StateTurnFlagModel model = turntobackofficeService.loadByCheckboxId(id);
        setTurntoModel(id, model);
        checkbox.setChecked(model.getFlag());
        selectbox.setDisabled(!checkbox.isChecked());
        ((ListModelList) selectbox.getModel()).add(model.getSetupType().getCode().toLowerCase());
    }

    private void setTurntoModel(String id, StateTurnFlagModel model) {
        if (id.equals("checkboxQA")) turntoQAModel = model;
        else turntoRatingModel = model;
    }

    private void updateTurntoModuleStatus(Checkbox checkbox, Selectbox selectbox, StateTurnFlagModel model) throws InterruptedException {
        final boolean isChecked = checkbox.isChecked();
        final String setupType = getSetupType(selectboxRating);

        selectbox.setDisabled(!isChecked);
        model.setFlag(isChecked);

        if (isChecked) model.setSetupType(SetupType.valueOf(setupType.toUpperCase()));

        turntobackofficeService.saveStateTurnFlag(model);
        Messagebox.show(turntobackofficeService.createMessage(isChecked));
    }

    private String getSetupType(Selectbox selectbox) {
        int index = selectbox.getSelectedIndex();
        return (String) selectbox.getModel().getElementAt(index);
    }
}
