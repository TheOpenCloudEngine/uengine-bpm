package org.uengine.social;

import org.metaworks.widget.ModalWindow;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.model.InstanceTooltip;

/**
 * Created by jangjinyoung on 15. 9. 8..
 */
@Component
@Order(100)
public class SocialBPMInstanceTooltip extends InstanceTooltip{
    public SocialBPMInstanceTooltip() throws Exception {
        super();
    }

    @Override
    public ModalWindow monitor() throws Exception {

        ModalWindow modalWindow = new ModalWindow();

        InstanceMonitorPanel instanceMonitorPanel = createInstanceMonitorPanel();

        modalWindow.setPanel(instanceMonitorPanel);
        modalWindow.setWidth(0);
        modalWindow.setHeight(0);

        modalWindow.setTitle("Process Monitor");

        return modalWindow;
    }

    protected InstanceMonitorPanel createInstanceMonitorPanel() throws Exception {
        InstanceMonitorPanel instanceMonitorPanel = new InstanceMonitorPanel();
        instanceMonitorPanel.load(this.getInstanceId(), processManager);
        return instanceMonitorPanel;
    }
}
