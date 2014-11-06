/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.dataquality.indicators.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.RangeRestriction;
import org.talend.dataquality.domain.sql.SqlPredicate;
import org.talend.dataquality.helpers.DomainHelper;
import org.talend.dataquality.indicators.BinFrequencyIndicator;
import org.talend.dataquality.indicators.IndicatorsPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Bin Frequency Indicator</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * </p>
 * 
 * @generated
 */
public class BinFrequencyIndicatorImpl extends FrequencyIndicatorImpl implements BinFrequencyIndicator {

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected BinFrequencyIndicatorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorsPackage.Literals.BIN_FREQUENCY_INDICATOR;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#handle(java.lang.Object)
     */
    @Override
    public boolean handle(Object data) {

        // if the bin parameter is set,look range name as frequency Map key.
        if (parameters != null) {
            Domain bins = parameters.getBins();
            if (bins != null) {
                String binFreqKey = getFormatName(data);
                // except null if the bin parameteris set.
                if (binFreqKey == null) {
                    return true;
                }
                return super.handle(binFreqKey);
            }
        }
        return super.handle(data);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#specialNames(java.lang.Object)
     */
    @Override
    protected List<String> specialNames(Object name) {
        List<String> ls = new ArrayList<String>();
        String specialName = getFormatName(name);
        if (specialName != null) {
            ls.add(specialName);
        } else {
            ls.add(name.toString());
        }
        return ls;
    }

    /*
     * (non-Javadoc)
     * 
     * if the bin parameter is set,look range name as a new name.
     */
    @Override
    protected String getFormatName(Object name) {
        if (name == null) {
            return null;
        }
        if (parameters != null) {
            Domain bins = parameters.getBins();
            if (bins != null) {
                EList<RangeRestriction> ranges = bins.getRanges();
                for (RangeRestriction range : ranges) {
                    double minRealValue = DomainHelper.getRealValue(range.getLowerValue());
                    double maxRealValue = DomainHelper.getRealValue(range.getUpperValue());
                    double inputValue = Double.valueOf(name.toString());
                    if (minRealValue <= inputValue && inputValue < maxRealValue) {
                        String rangeName = range.getName();
                        if (rangeName == null) {
                            rangeName = analyzedElement.getName() + PluginConstant.SPACE_STRING
                                    + SqlPredicate.GREATER_EQUAL.getLiteral() + PluginConstant.SPACE_STRING + minRealValue
                                    + " AND " + analyzedElement.getName() + PluginConstant.SPACE_STRING
                                    + SqlPredicate.LESS.getLiteral() + PluginConstant.SPACE_STRING + maxRealValue;
                            range.setName(rangeName);
                        }
                        return rangeName;
                    }
                }
                // if the data(name) is not in these ranges,return null and ignor it.
                return null;
            }
        }
        return name.toString();
    }

} // BinFrequencyIndicatorImpl
