/**
 * This file is part of the QoCIM middleware.
 *
 * Copyright (C) 2014 IRIT, Télécom SudParis
 *
 * The QoCIM software is free software: you can redistribute it and/or modify
 * It under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The QoCIM software platform is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License
 * for more details: http://www.gnu.org/licenses
 *
 * Initial developer(s): Pierrick MARIE
 * Contributor(s):
 */
package qocim.capsule.parameters.tests;

import java.util.ArrayList;
import java.util.List;

import qocim.datamodel.QoCMetaData;
import qocim.datamodel.precisionindicator.PercentPrecisionFactory;
import qocim.datamodel.precisionindicator.PercentPrecisionQoCMetricDefinition;
import qocim.datamodel.precisionindicator.PerthousandPrecisionFactory;
import qocim.datamodel.precisionindicator.PerthousandPrecisionQoCMetricDefinition;
import qocim.routingfilters.IQoCIMRoutingFilterGenerator;
import qocim.routingfilters.impl.JavaScriptBeginRoutingFilterGenerator;
import qocim.routingfilters.impl.JavaScriptEndRoutingFilterGenerator;
import qocim.routingfilters.impl.JavaScriptQoCIMRoutingFilterGenerator;
import qocim.routingfilters.impl.QoCIMRoutingFilter;

/**
 * Method used to generate a subscription routing filter.
 *
 * The structure of the resulting filter is:
 *
 * <pre>
 * if(QoC meta-data DOES NOT contains the freshness indicator OR QoC meta-data DOES NOT contains the precision indicator (evaluated in
 * percent) OR QoC meta-data DOES NOT contains the precision indicator (evaluated in
 * per thousand)) return FALSE
 * </pre>
 *
 * So, the QoC meta-data received by this application have to contain at least
 * the freshness OR the precision indicator.evaluated in percent or in per
 * thousand.
 *
 * @author Pierrick MARIE
 */
public class SubscriptionFilter {

	/*
	 * To create a routing filter the <i>id</i> of the QoC metric value does not
	 * matter.
	 */
	private static final String DEFAULT_METRICVALUE_ID = "0";

	public String precisionsFilter() {

		/*
		 * The routing filter created by the class.
		 */
		QoCIMRoutingFilter routingFilter;
		/*
		 * The precision QàC meta-data used to add a constraint into the filter.
		 */
		QoCMetaData percentPrecision, perthousandPrecision;
		/*
		 * The filter generator used to transform QoC meta-data into
		 * constraints.
		 */
		IQoCIMRoutingFilterGenerator qocimFilterGenerator;
		/*
		 * The list of QoC meta-data handled by <i>qocimFilterGenerator</i>.
		 */
		List<QoCMetaData> constraints;

		constraints = new ArrayList<QoCMetaData>();
		qocimFilterGenerator = new JavaScriptQoCIMRoutingFilterGenerator();
		routingFilter = new QoCIMRoutingFilter();

		/*
		 * Initialization of the precision (evaluated in percent) QoC meta-data
		 * and production of the resulting constraint.
		 */
		percentPrecision = new QoCMetaData(PercentPrecisionFactory.instance().newQoCIndicator(DEFAULT_METRICVALUE_ID,
				PercentPrecisionQoCMetricDefinition.MIN_VALUE_DEFAULTVALUE));
		constraints.add(percentPrecision);

		/*
		 * Initialization of the precision (evaluated in per-thousand) QoC
		 * meta-data and production of the resulting constraint.
		 */
		perthousandPrecision = new QoCMetaData(PerthousandPrecisionFactory.instance().newQoCIndicator(
				DEFAULT_METRICVALUE_ID, PerthousandPrecisionQoCMetricDefinition.MIN_VALUE_DEFAULTVALUE));
		constraints.add(perthousandPrecision);

		/*
		 * Creation of the routing filter.
		 */
		qocimFilterGenerator.addQoCCriterionConstraints(constraints);
		routingFilter.addFilterGenerator(new JavaScriptBeginRoutingFilterGenerator());
		routingFilter.addFilterGenerator(qocimFilterGenerator);
		routingFilter.addFilterGenerator(new JavaScriptEndRoutingFilterGenerator());

		return routingFilter.toString();
	}
}
