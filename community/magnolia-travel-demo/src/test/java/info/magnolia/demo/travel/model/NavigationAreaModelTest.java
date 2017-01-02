/**
 * This file Copyright (c) 2015-2017 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This file is dual-licensed under both the Magnolia
 * Network Agreement and the GNU General Public License.
 * You may elect to use one or the other of these licenses.
 *
 * This file is distributed in the hope that it will be
 * useful, but AS-IS and WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, or NONINFRINGEMENT.
 * Redistribution, except as permitted by whichever of the GPL
 * or MNA you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or
 * modify this file under the terms of the GNU General
 * Public License, Version 3, as published by the Free Software
 * Foundation.  You should have received a copy of the GNU
 * General Public License, Version 3 along with this program;
 * if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * 2. For the Magnolia Network Agreement (MNA), this file
 * and the accompanying materials are made available under the
 * terms of the MNA which accompanies this distribution, and
 * is available at http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.demo.travel.model;

import static info.magnolia.test.hamcrest.ExecutionMatcher.throwsNothing;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.template.AreaDefinition;
import info.magnolia.templating.functions.TemplatingFunctions;
import info.magnolia.test.hamcrest.Execution;

import java.util.List;

import javax.jcr.Node;

import org.junit.Test;

public class NavigationAreaModelTest {

    private final NavigationAreaModel navigationAreaModel = new NavigationAreaModel(mock(Node.class), mock(AreaDefinition.class), mock(RenderingModel.class), mock(TemplatingFunctions.class));

    @Test
    public void makeSureReturnedLocalesTakeCountryIntoAccount() {
        // GIVEN
        // WHEN
        // THEN
        assertThat(navigationAreaModel.getLocale("de").getLanguage(), is("de"));
        assertThat(navigationAreaModel.getLocale("de").getCountry(), isEmptyString());
        assertThat(navigationAreaModel.getLocale("de_CH").getLanguage(), is("de"));
        assertThat(navigationAreaModel.getLocale("de_CH").getCountry(), is("CH"));
    }

    @Test
    public void whenNoHomeTemplateIsFoundNoNPEIsThrown() {
        // GIVEN
        // WHEN
        // THEN
        assertThat(new Execution() {
            @Override
            public void evaluate() throws Exception {
                final List<NavigationAreaModel.NavigationItem> result = navigationAreaModel.getRootPages();

                assertThat("We expect an empty list", result, hasSize(0));
            }
        }, throwsNothing());
    }

}