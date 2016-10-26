/**
 * This file Copyright (c) 2015-2016 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This program and the accompanying materials are made
 * available under the terms of the Magnolia Network Agreement
 * which accompanies this distribution, and is available at
 * http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.demo.travel.multisite.setup;

import static info.magnolia.test.hamcrest.NodeMatchers.*;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import info.magnolia.context.MgnlContext;
import info.magnolia.module.InstallContext;
import info.magnolia.module.ModuleVersionHandler;
import info.magnolia.module.ModuleVersionHandlerTestCase;
import info.magnolia.module.model.Version;
import info.magnolia.repository.RepositoryConstants;

import java.util.Arrays;
import java.util.List;

import javax.jcr.Session;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link TravelDemoMultiSiteModuleVersionHandler}.
 */
public class TravelDemoMultiSiteModuleVersionHandlerTest extends ModuleVersionHandlerTestCase {

    private Session session;

    @Override
    protected String getModuleDescriptorPath() {
        return "/META-INF/magnolia/travel-demo-multisite.xml";
    }

    @Override
    protected ModuleVersionHandler newModuleVersionHandlerForTests() {
        return new TravelDemoMultiSiteModuleVersionHandler();
    }

    @Override
    protected List<String> getModuleDescriptorPathsForTests() {
        return Arrays.asList("/META-INF/magnolia/core.xml");
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        setupConfigNode("/modules/multisite/config/sites/sportstation/templates");
        session = MgnlContext.getJCRSession(RepositoryConstants.CONFIG);
    }

    @Test
    public void cleanInstallCreatesMappingsInSite() throws Exception {
        // GIVEN
        // Looks weird (clean install?) and it is indeed – well travel-demo does the migration itself
        setupConfigNode("/modules/multisite/config/sites/travel");

        // WHEN
        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(null);

        // THEN
        assertTrue(session.nodeExists("/modules/multisite/config/sites/travel/domains"));
        assertTrue(session.nodeExists("/modules/multisite/config/sites/travel/mappings"));
    }

    @Test
    public void updateFrom07DoesNotFailInstallation() throws Exception {
        // GIVEN
        setupConfigNode("/modules/multisite/config/sites/sportstation");
        setupConfigNode("/modules/multisite/config/sites/travel/domains");
        setupConfigNode("/modules/multisite/config/sites/travel/mappings");

        // WHEN
        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("0.7"));

        // THEN
        // We expect no exception
    }

    @Test
    public void updateFrom06CreatesMappingAndDomainConfigWhenItDoesNotExist() throws Exception {
        // GIVEN
        setupConfigNode("/modules/multisite/config/sites/sportstation");
        setupConfigNode("/modules/multisite/config/sites/travel");

        // WHEN
        executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("0.6"));

        // THEN
        assertTrue(session.nodeExists("/modules/multisite/config/sites/travel/domains"));
        assertTrue(session.nodeExists("/modules/multisite/config/sites/travel/mappings"));
    }

    @Test
    public void upgradeFrom014RemovesSportstationThemeFromJCR() throws Exception {
        // GIVEN
        setupConfigNode("/modules/site/config/themes/sportstation-theme");

        // WHEN
        final InstallContext ctx = executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("0.14"));

        // THEN
        assertThat(session.getRootNode(), not(hasNode("modules/site/config/themes/sportstation-theme")));
        this.assertNoMessages(ctx);
    }

    @Test
    public void upgradeFrom014RenamesMultisiteTemplatePrototypeId() throws Exception {
        // GIVEN
        setupConfigProperty("/modules/multisite/config/sites/sportstation/templates", "prototypeId", "whatevah");

        // WHEN
        final InstallContext ctx = executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("0.14"));

        // THEN
        assertThat(session.getNode("/modules/multisite/config/sites/sportstation/templates"), hasProperty("prototypeId", "travel-demo-multisite:pages/prototype"));
        this.assertNoMessages(ctx);
    }

}