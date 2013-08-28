// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.record.linkage.ui.section.definition;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.dataquality.record.linkage.ui.composite.MatchRuleTableComposite;
import org.talend.dataquality.record.linkage.ui.composite.definition.MatchRuleTableDefinitionComposite;
import org.talend.dataquality.record.linkage.ui.section.MatchingKeySection;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.rules.MatchKeyDefinition;
import org.talend.dataquality.rules.MatchRule;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.RulesFactory;


/**
 * created by zshen on Aug 21, 2013
 * Detailled comment
 *
 */
public class MatchKeyDefinitionSection extends MatchingKeySection {


    Logger log = Logger.getLogger(MatchKeyDefinitionSection.class);

    private MatchRuleDefinition matchRuleDef = null;
    /**
     * DOC zshen MatchKeyDefinitionSection constructor comment.
     *
     * @param form
     * @param parent
     * @param style
     * @param toolkit
     * @param analysis
     */
    public MatchKeyDefinitionSection(ScrolledForm form, Composite parent, FormToolkit toolkit) {
        super(form, parent, Section.TWISTIE | Section.TITLE_BAR | Section.EXPANDED, toolkit, null);
        super.setIsNeedSubChart(false);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataquality.record.linkage.ui.section.MatchingKeySection#getSectionName()
     */
    @Override
    protected String getSectionName() {
        return MatchAnalysisConstant.MATCHING_KEY_DEFINITION_SECTION_NAME;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataquality.record.linkage.ui.section.MatchingKeySection#getMatchRuleDefinition()
     */
    @Override
    protected MatchRuleDefinition getMatchRuleDefinition() {
        return this.matchRuleDef;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.talend.dataquality.record.linkage.ui.section.MatchingKeySection#createSubChart(org.eclipse.swt.widgets.Composite
     * )
     */
    @Override
    protected void createSubChart(Composite sectionClient) {
        // don't need the chart so do nothing at here
    }



    /*
     * (non-Javadoc)
     *
     * @see
     * org.talend.dataquality.record.linkage.ui.section.MatchingKeySection#createMatchKeyFromCurrentMatchRule(java.lang
     * .String)
     */
    @Override
    public void createMatchKeyFromCurrentMatchRule(String column) {
        MatchRuleTableComposite matchRuleTableComp = getCurrentMatchRuleTableComposite();
        matchRuleTableComp.addKeyDefinition(column, matchRuleDef);
    }

    /**
     * Sets the matchRuleDef.
     *
     * @param matchRuleDef the matchRuleDef to set
     */
    public void setMatchRuleDef(MatchRuleDefinition matchRuleDef) {
        this.matchRuleDef = RulesFactory.eINSTANCE.createMatchRuleDefinition();
        this.matchRuleDef.getMatchRules().addAll(matchRuleDef.getMatchRules());
        this.matchRuleDef.setMatchGroupQualityThreshold(matchRuleDef.getMatchGroupQualityThreshold());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.talend.dataquality.record.linkage.ui.section.MatchingKeySection#removeMatchKeyFromCurrentMatchRule(java.lang
     * .String)
     */
    @Override
    public void removeMatchKeyFromCurrentMatchRule(MatchKeyDefinition columnkey) {
        MatchRuleTableComposite matchRuleTableComp = getCurrentMatchRuleTableComposite();
        matchRuleTableComp.removeKeyDefinition(columnkey, matchRuleDef);
    }

    /**
     * Getter for matchRules.
     *
     * @return the matchRules
     */
    public List<MatchRule> getMatchRules() {
        List<MatchRule> matchRuleKeys = new ArrayList<MatchRule>();
        matchRuleKeys.addAll(EcoreUtil.copyAll(this.matchRuleDef.getMatchRules()));
        return matchRuleKeys;
    }

    public double getGroupQualityThreshold() {
        return this.matchRuleDef.getMatchGroupQualityThreshold();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.talend.dataquality.record.linkage.ui.section.MatchingKeySection#createTableComposite(org.eclipse.swt.widgets
     * .Composite, org.talend.dataquality.rules.MatchRule)
     */
    @Override
    protected MatchRuleTableComposite createTableComposite(Composite parent, MatchRule matchRule) {
        return new MatchRuleTableDefinitionComposite(parent, SWT.NO_FOCUS, matchRule);
    }


}
