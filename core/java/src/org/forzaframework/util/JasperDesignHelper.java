/*
 * Copyright 2006-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.forzaframework.util;

import net.sf.jasperreports.engine.type.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.beanutils.BeanUtils;
import ar.com.fdvs.dj.domain.DynamicJasperDesign;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.DynamicReportOptions;
import ar.com.fdvs.dj.domain.DJQuery;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.core.CoreException;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.*;

import java.util.Iterator;
import java.lang.reflect.InvocationTargetException;

/**
 * @author cesarreyes
 *         Date: 07-dic-2008
 *         Time: 21:15:14
 */
public class JasperDesignHelper {

	private static final Logger log = LogManager.getLogger(JasperDesignHelper.class);

	public static DynamicJasperDesign getNewDesign(DynamicReport dr) {
		log.info("obtaining DynamicJasperDesign instance");
		DynamicJasperDesign des = new DynamicJasperDesign();
		DynamicReportOptions options = dr.getOptions();
		Page page = options.getPage();

		des.setColumnCount(options.getColumnsPerPage());
		des.setPrintOrder(PrintOrderEnum.VERTICAL);

		OrientationEnum orientation = page.isOrientationPortrait() ? OrientationEnum.PORTRAIT : OrientationEnum.LANDSCAPE;
		des.setOrientation(orientation);

		des.setPageWidth(page.getWidth());
		des.setPageHeight(page.getHeight());

		des.setColumnWidth(options.getColumnWidth());
		des.setColumnSpacing(options.getColumnSpace());
		des.setLeftMargin(options.getLeftMargin());
		des.setRightMargin(options.getRightMargin());
		des.setTopMargin(options.getTopMargin());
		des.setBottomMargin(options.getBottomMargin());

		des.setWhenNoDataType(WhenNoDataTypeEnum.BLANK_PAGE);
		des.setWhenResourceMissingType(WhenResourceMissingTypeEnum.EMPTY);

		des.setTitleNewPage(false);
		des.setSummaryNewPage(false);

		des.setSectionType(SectionTypeEnum.BAND);

//		des.getDetail().setSplitAllowed(dr.isAllowDetailSplit());

		des.setPageHeader(new JRDesignBand());
		des.setPageFooter(new JRDesignBand());
		des.setSummary(new JRDesignBand());

		des.setTitleNewPage(options.isTitleNewPage());

		des.setIgnorePagination(options.isIgnorePagination());

		if (dr.getQuery() != null){
			JRDesignQuery query = getJRDesignQuery(dr);
			des.setQuery(query);
		}

		for (Iterator iterator = dr.getProperties().keySet().iterator(); iterator.hasNext();) {
			String name = (String) iterator.next();
			des.setProperty(name, (String) dr.getProperties().get(name));
		}

		des.setName(dr.getReportName() != null ? dr.getReportName() : "DynamicReport");
		return des;
	}

	protected static JRDesignQuery getJRDesignQuery(DynamicReport dr) {
		JRDesignQuery query = new JRDesignQuery();
		query.setText(dr.getQuery().getText());
		query.setLanguage(dr.getQuery().getLanguage());
		return query;
	}


	public static DynamicJasperDesign downCast(JasperDesign jd, DynamicReport dr) throws CoreException {
		DynamicJasperDesign djd = new DynamicJasperDesign();
		log.info("downcasting JasperDesign");
		try {
			BeanUtils.copyProperties(djd, jd);

			//BeanUtils.copyProperties does not perform deep copy,
			//adding original parameter definitions manually
			if (dr.isTemplateImportParameters()){
				for (Iterator iter = jd.getParametersList().iterator(); iter.hasNext();) {
					JRParameter element = (JRParameter) iter.next();
					try {
						djd.addParameter(element);
					} catch (JRException e) {
						if (log.isDebugEnabled()){
							log.warn(e.getMessage());
						}
					}
				}
			}

			//BeanUtils.copyProperties does not perform deep copy,
			//adding original fields definitions manually
			if (dr.isTemplateImportFields()){
				for (Iterator iter = jd.getFieldsList().iterator(); iter.hasNext();) {
					JRField element = (JRField) iter.next();
					try {
						djd.addField(element);
					} catch (JRException e) {
						if (log.isDebugEnabled()){
							log.warn(e.getMessage());
						}
					}
				}
			}

			//BeanUtils.copyProperties does not perform deep copy,
			//adding original variables definitions manually
			if (dr.isTemplateImportVariables()){
				for (Iterator iter = jd.getVariablesList().iterator(); iter.hasNext();) {
					JRVariable element = (JRVariable) iter.next();
					try {
						if (element instanceof JRDesignVariable){
							djd.addVariable((JRDesignVariable) element);
						}
					} catch (JRException e) {
						if (log.isDebugEnabled()){
							log.warn(e.getMessage());
						}
					}
				}
			}

  			//BeanUtils.copyProperties does not perform deep copy,
			//adding original dataset definitions manually
			if (dr.isTemplateImportDatasets()) {
				// also copy query
				JRQuery query = jd.getQuery();
				if (query instanceof JRDesignQuery) {
					djd.setQuery((JRDesignQuery) query);
					dr.setQuery(new DJQuery(query.getText(), query
							.getLanguage()));
				}

				for (Iterator iter = jd.getDatasetsList().iterator(); iter.hasNext();) {
					JRDesignDataset dataset = (JRDesignDataset) iter.next();
					try {
						djd.addDataset(dataset);
					} catch (JRException e) {
						if (log.isDebugEnabled()) {
							log.warn(e.getMessage());
						}
					}
				}
			}

			//BeanUtils.copyProperties does not perform deep copy,
			//adding original properties definitions manually
			String[] properties = jd.getPropertyNames();
			for (int i = 0; i < properties.length; i++) {
				String propName = properties[i];
				String propValue = jd.getProperty(propName);
				djd.setProperty(propName, propValue);
			}


			//Add all existing styles in the design to the new one
			for (Iterator iterator = jd.getStylesList().iterator(); iterator.hasNext();) {
				JRStyle style = (JRStyle) iterator.next();
				try {
					djd.addStyle(style);
				} catch (JRException e) {
					if (log.isDebugEnabled()){
						log.warn("Duplicated style (style name \""+ style.getName()+"\") when loading design: " + e.getMessage(), e);
					}
				}
			}

		} catch (IllegalAccessException e) {
			throw new CoreException(e.getMessage(),e);
		} catch (InvocationTargetException e) {
			throw new CoreException(e.getMessage(),e);
		}

		return djd;
	}

	/**
	 * Because all the layout calculations are made from the Domain Model of DynamicJasper, when loading
	 * a template file, we have to populate the "ReportOptions" with the settings from the template file (ie: margins, etc)
	 * @param jd
	 * @param dr
	 */
	protected static void populateReportOptionsFromDesign(DynamicJasperDesign jd, DynamicReport dr) {
		DynamicReportOptions options = dr.getOptions();

		options.setBottomMargin(new Integer(jd.getBottomMargin()));
		options.setTopMargin(new Integer(jd.getTopMargin()));
		options.setLeftMargin(new Integer(jd.getLeftMargin()));
		options.setRightMargin(new Integer(jd.getRightMargin()));

		options.setColumnSpace(new Integer(jd.getColumnSpacing()));
		options.setColumnsPerPage(new Integer(jd.getColumnCount()));

		options.setPage(new Page(jd.getPageHeight(),jd.getPageWidth()));

		if (dr.getQuery() != null){
			JRDesignQuery query = JasperDesignHelper.getJRDesignQuery(dr);
			jd.setQuery(query);
		}

	}

}
