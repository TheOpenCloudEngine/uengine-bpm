package org.uengine.social.test;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.metaworks.test.TestMetaworksRemoteService;
import org.oce.garuda.multitenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.uengine.social.SocialBPMTenantLifecycle;

/**
 * Created by jjy on 2016. 4. 22..
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:target/uengine-social-bpm-portal-4.0.1-SNAPSHOT/WEB-INF/customContext.xml"})
@Transactional
public class SubscriptionTest{

    @Autowired
    private ApplicationContext applicationContext;


    @Before
    public void setup(){
        //Set application context for testing
        new TestMetaworksRemoteService(
            applicationContext
        );
    }

    /***
     * Scenarios
     *
     * (1) - when a new user comes whom company never been subscribed before, default process definitions in a app jar should be installed for the company for evaluation purpose.
     */
    @Test
    @Transactional
    public void testDefaultProcessInstallation(){

        new TenantContext("6");
        SocialBPMTenantLifecycle tenantLifecycle = new SocialBPMTenantLifecycle();
        tenantLifecycle.onNewTenantSubscribe("6");
    }
}
