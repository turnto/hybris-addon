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
    private static final long serialVersionUID = 1L;

    private Checkbox checkbox;

    private Checkbox turnQA;
    private Checkbox turnRating;


    private Selectbox selectQAMode;
    private Selectbox selectRatingMode;

    private StateTurnFlagModel turnQAmodel;

    @WireVariable
    private TurntobackofficeService turntobackofficeService;


    @Override
    public void initialize(Component comp) {
        super.initialize(comp);
        initTurnQA();
        selectRatingMode.setDisabled(true);
    }

    @ViewEvent(componentID = "sendFeedBtn", eventName = Events.ON_CLICK)
    public void sendProducts() throws Exception {
        Messagebox.show(turntobackofficeService.sendCatalogFeed());
    }

    @ViewEvent(componentID = "turnQA", eventName = Events.ON_CHECK)
    public void turnQA() throws InterruptedException {
        boolean turnFlag = turnQA.isChecked();
        String setupType = getSetupType(selectQAMode);

        selectQAMode.setDisabled(!turnFlag);

        turnQAmodel.setFlag(turnFlag);

        if (turnFlag) {
            turnQAmodel.setSetupType(SetupType.valueOf(setupType.toUpperCase()));
        }

        turntobackofficeService.saveStateTurnFlag(turnQAmodel);
        Messagebox.show(turntobackofficeService.getHelloWorld());
    }

    @ViewEvent(componentID = "turnRating", eventName = Events.ON_CHECK)
    public void turnRating() throws InterruptedException {
        selectRatingMode.setDisabled(!turnRating.isChecked());
        Messagebox.show(turntobackofficeService.getHelloWorld());
    }

    @ViewEvent(componentID = "turnOrderReporting", eventName = Events.ON_CHECK)
    public void turnOrderReporting() throws InterruptedException {
        Messagebox.show(turntobackofficeService.getHelloWorld());
    }

    @ViewEvent(componentID = "turnCheckoutChatter", eventName = Events.ON_CHECK)
    public void turnCheckoutChatter() throws InterruptedException {
        Messagebox.show(turntobackofficeService.getHelloWorld());
    }

    @ViewEvent(componentID = "selectQAMode", eventName = Events.ON_SELECT)
    public void selectQAMode() throws InterruptedException {
        boolean turnFlag = turnQA.isChecked();

        String setupType = getSetupType(selectQAMode);

        if (turnFlag) {
            turnQAmodel.setSetupType(SetupType.valueOf(setupType.toUpperCase()));
            turntobackofficeService.saveStateTurnFlag(turnQAmodel);
        }

        Messagebox.show(turntobackofficeService.getHelloWorld());
    }

    @ViewEvent(componentID = "selectRatingMode", eventName = Events.ON_SELECT)
    public void selectRatingMode() throws InterruptedException {
        Messagebox.show(turntobackofficeService.getHelloWorld());
    }

    private void initTurnQA() {
        turnQAmodel = turntobackofficeService.loadByCheckboxId(turnQA.getId());
        turnQA.setChecked(turnQAmodel.getFlag());
        selectQAMode.setDisabled(!turnQA.isChecked());
        ((ListModelList)selectQAMode.getModel()).addSelection(turnQAmodel.getSetupType().getCode().toLowerCase());

    }

    private String getSetupType(Selectbox selectbox){
        int index = selectbox.getSelectedIndex();
        return  (String) selectbox.getModel().getElementAt(index);
    }
}
