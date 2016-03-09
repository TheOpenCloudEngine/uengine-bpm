package org.uengine.processadmin;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.layout.Layout;
import org.oce.garuda.multitenancy.TenantContext;
import org.uengine.codi.mw3.marketplace.MarketplaceWindow;

/**
 * Created by Misaka on 2016-02-29.
 */
public class ProcessAdminLayout {
    Layout layout;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public ProcessAdminLayout() {
    }

    String jiraTenant;

    public String getJiraTenant() {
        return jiraTenant;
    }

    public void setJiraTenant(String jiraTenant) {
        this.jiraTenant = jiraTenant;
    }

    @ServiceMethod(callByContent = true, target = ServiceMethodContext.TARGET_APPEND)
    public void showProcessAdmin() throws Exception {
        new TenantContext(this.getJiraTenant());

        Layout centerLayout = new Layout();
        centerLayout.setCenter(new ProcessAdminWorkbench());
        centerLayout.setOptions("togglerLength_open:0, spacing_open:0, spacing_closed:0, south__spacing_open:5, west__spacing_open:5, west__size:300");
        centerLayout.setName("east");
        centerLayout.setUseHideBar(false);

        Layout outerLayout = new Layout();
        outerLayout.setOptions("togglerLength_open:0, spacing_open:0, spacing_closed:0, west__spacing_open:1, north__size:52, west__size: 100");
        outerLayout.setCenter(centerLayout);
        outerLayout.setName("center");
        outerLayout.setUseHideBar(false);

        this.setLayout(outerLayout);
    }

    @ServiceMethod(target = ServiceMethodContext.TARGET_APPEND)
    public void showMarketPlace() throws Exception {


        MarketplaceWindow marketplaceWindow = new MarketplaceWindow();
        Layout marketPlaceLayout = new Layout();
        marketPlaceLayout.setOptions("togglerLength_open:0, spacing_open:0, spacing_closed:0, east__size:250");
        marketPlaceLayout.setCenter(marketplaceWindow);
        Layout mainLayout = new Layout();
        mainLayout.setOptions("togglerLength_open:0, spacing_open:0, spacing_closed:0, north__size:52");
        mainLayout.setCenter(marketPlaceLayout);
        this.setLayout(mainLayout);
    }
}
