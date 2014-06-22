// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.editor.analysis;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.jfree.chart.JFreeChart;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.ColumnSortListener;
import org.talend.dataprofiler.core.ui.chart.ChartDecorator;
import org.talend.dataprofiler.core.ui.chart.jung.JungGraphGenerator;
import org.talend.dataprofiler.core.ui.editor.preview.HideSeriesChartComposite;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.ChartTypeStatesOperator;
import org.talend.dataprofiler.core.ui.editor.preview.model.ChartWithData;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates;
import org.talend.dataprofiler.core.ui.pref.EditorPreferencePage;
import org.talend.dataprofiler.core.ui.utils.TableUtils;
import org.talend.dataquality.indicators.columnset.ColumnSetMultiValueIndicator;
import org.talend.dataquality.indicators.columnset.ColumnsetPackage;
import org.talend.dataquality.indicators.columnset.CountAvgNullIndicator;
import org.talend.dq.analysis.AnalysisHandler;
import org.talend.dq.indicators.graph.GraphBuilder;
import org.talend.dq.indicators.preview.EIndicatorChartType;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC xzhao class global comment. Detailled comment
 */
public class ColumnCorrelationNominalIntervalResultPage extends AbstractAnalysisResultPage implements PropertyChangeListener {

    protected static Logger log = Logger.getLogger(ColumnCorrelationNominalIntervalResultPage.class);

    // private Composite resultComp;

    private Composite graphicsAndTableComp;

    private ColumnCorrelationNominalAndIntervalMasterPage masterPage;

    private ColumnSetMultiValueIndicator columnSetMultiIndicator;

    private Composite chartComposite;

    private Composite[] previewChartCompsites;

    private String executeData;

    private Section graphicsAndTableSection = null;

    /**
     * DOC zqin ColumnAnalysisResultPage constructor comment.
     * 
     * @param editor
     * @param id
     * @param title
     */
    public ColumnCorrelationNominalIntervalResultPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
        AnalysisEditor analysisEditor = (AnalysisEditor) editor;
        this.masterPage = (ColumnCorrelationNominalAndIntervalMasterPage) analysisEditor.getMasterPage();
        columnSetMultiIndicator = masterPage.getColumnSetMultiValueIndicator();
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
        super.createFormContent(managedForm);

        graphicsAndTableComp = toolkit.createComposite(topComposite);
        graphicsAndTableComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
        graphicsAndTableComp.setLayout(new GridLayout());
        createResultSection(graphicsAndTableComp);
        form.reflow(true);
    }

    @Override
    protected AnalysisHandler getAnalysisHandler() {
        return this.masterPage.getColumnCorrelationAnalysisHandler();
    }

    protected void createResultSection(Composite parent) {
        executeData = getAnalysisHandler().getExecuteData();
        graphicsAndTableSection = this.createSection(form, parent, DefaultMessagesImpl
                .getString("ColumnCorrelationNominalIntervalResultPage.AnalysisResult"), null); //$NON-NLS-1$
        Composite sectionClient = toolkit.createComposite(graphicsAndTableSection);
        sectionClient.setLayout(new GridLayout());
        sectionClient.setLayoutData(new GridData(GridData.FILL_BOTH));
        Composite graphicsComp = toolkit.createComposite(sectionClient);
        graphicsComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        graphicsComp.setLayout(new GridLayout());
        if (executeData == null || executeData.equals(PluginConstant.EMPTY_STRING)) {
            return;
        } else {
            if (!EditorPreferencePage.isHideGraphics()) {
                this.createGraphicsSectionPart(sectionClient);
            }
        }

        Composite simpleSatisticsComp = toolkit.createComposite(sectionClient);
        simpleSatisticsComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        simpleSatisticsComp.setLayout(new GridLayout());
        if (executeData == null || executeData.equals(PluginConstant.EMPTY_STRING)) {
            return;
        } else {
            this.createSimpleStatisticsPart(sectionClient, DefaultMessagesImpl
                    .getString("ColumnCorrelationNominalIntervalResultPage.SimpleStatistics"), columnSetMultiIndicator); //$NON-NLS-1$
        }

        Composite tableComp = toolkit.createComposite(sectionClient);
        tableComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        tableComp.setLayout(new GridLayout());
        if (executeData == null || executeData.equals(PluginConstant.EMPTY_STRING)) {
            return;
        } else {
            this.createTableSectionPart(sectionClient, DefaultMessagesImpl
                    .getString("ColumnCorrelationNominalIntervalResultPage.Data"), columnSetMultiIndicator); //$NON-NLS-1$
        }
        graphicsAndTableSection.setExpanded(true);
        graphicsAndTableSection.setClient(sectionClient);
    }

    protected Section createGraphicsSectionPart(Composite parentComp) {
        Section section = createSection(form, parentComp, DefaultMessagesImpl.getString("ColumnMasterDetailsPage.graphics"), //$NON-NLS-1$
                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.space")); //$NON-NLS-1$ 
        section.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite sectionClient = toolkit.createComposite(section);
        sectionClient.setLayout(new GridLayout());
        sectionClient.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).applyTo(sectionClient);

        chartComposite = toolkit.createComposite(sectionClient);
        chartComposite.setLayout(new GridLayout());

        if (ColumnsetPackage.eINSTANCE.getWeakCorrelationIndicator() == columnSetMultiIndicator.eClass()) {
            GridData gd = new GridData();
            gd.widthHint = 900;
            gd.heightHint = 450;
            chartComposite.setLayoutData(gd);
            GraphBuilder gBuilder = new GraphBuilder();
            gBuilder.setTotalWeight(columnSetMultiIndicator.getCount());
            List<Object[]> listRows = columnSetMultiIndicator.getListRows();
            // MOD msjian TDQ-4781 2012-6-8: make sure exist data
            if (listRows != null && listRows.size() > 0) {
                JungGraphGenerator generator = new JungGraphGenerator(gBuilder, listRows);
                // MOD yyi 2009-09-09 feature 8834
                generator.generate(chartComposite, false, false);
            }
            // TDQ-4781~
        } else {
            createBubbleOrGanttChart(form, chartComposite, columnSetMultiIndicator);
        }

        section.setClient(sectionClient);
        return section;
    }

    private void createBubbleOrGanttChart(final ScrolledForm form, final Composite composite,
            final ColumnSetMultiValueIndicator columnSetMultiValueIndicator) {
        List<Composite> previewChartList = new ArrayList<Composite>();
        List<ModelElement> bubOrGanttColumnList = new ArrayList<ModelElement>();
        if (columnSetMultiValueIndicator instanceof CountAvgNullIndicator) {
            bubOrGanttColumnList = columnSetMultiValueIndicator.getNumericColumns();
        } else {
            bubOrGanttColumnList = columnSetMultiValueIndicator.getDateColumns();
        }
        for (ModelElement column : bubOrGanttColumnList) {
            final MetadataColumn tdColumn = (MetadataColumn) column;

            final ExpandableComposite exComp = toolkit.createExpandableComposite(composite, ExpandableComposite.TREE_NODE
                    | ExpandableComposite.CLIENT_INDENT);
            exComp.setText(DefaultMessagesImpl.getString("ColumnMasterDetailsPage.column", tdColumn.getName())); //$NON-NLS-1$
            exComp.setLayout(new GridLayout());
            exComp.setData(columnSetMultiValueIndicator);
            previewChartList.add(exComp);

            final Composite comp = toolkit.createComposite(exComp);
            comp.setLayout(new GridLayout(2, false));
            comp.setLayoutData(new GridData(GridData.FILL_BOTH));

            if (tdColumn != null) {
                IRunnableWithProgress rwp = new IRunnableWithProgress() {

                    public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                        monitor
                                .beginTask(
                                        DefaultMessagesImpl.getString(
                                                "ColumnCorrelationNominalIntervalResultPage.CreatePreview", tdColumn.getName()), IProgressMonitor.UNKNOWN); //$NON-NLS-1$
                        Display.getDefault().asyncExec(new Runnable() {

                            public void run() {
                                HideSeriesChartComposite hcc = new HideSeriesChartComposite(comp, getAnalysisHandler()
                                        .getAnalysis(), columnSetMultiValueIndicator, tdColumn, true);
                                GridData gd = new GridData();
                                gd.widthHint = 800;
                                gd.heightHint = 450;
                                hcc.setLayoutData(gd);
                            }

                        });

                        monitor.done();
                    }

                };

                try {
                    new ProgressMonitorDialog(getSite().getShell()).run(true, false, rwp);
                } catch (Exception ex) {
                    log.error(ex, ex);
                }
            }

            exComp.addExpansionListener(new ExpansionAdapter() {

                @Override
                public void expansionStateChanged(ExpansionEvent e) {
                    getChartComposite().layout();
                    form.reflow(true);
                }

            });

            exComp.setExpanded(true);
            exComp.setClient(comp);
        }
        if (!previewChartList.isEmpty()) {
            this.previewChartCompsites = previewChartList.toArray(new Composite[previewChartList.size()]);
        }
    }

    private Section createSimpleStatisticsPart(Composite parentComp, String title,
            ColumnSetMultiValueIndicator columnSetMultiValueIndicator) {
        Section section = createSection(form, parentComp, title, DefaultMessagesImpl.getString("ColumnMasterDetailsPage.space")); //$NON-NLS-1$
        section.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite sectionClient = toolkit.createComposite(section);
        sectionClient.setLayout(new GridLayout());
        sectionClient.setLayoutData(new GridData(GridData.FILL_BOTH));

        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).applyTo(sectionClient);

        Composite simpleComposite = toolkit.createComposite(sectionClient);
        simpleComposite.setLayout(new GridLayout(2, true));
        simpleComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

        createSimpleStatistics2(form, simpleComposite, columnSetMultiValueIndicator);

        section.setClient(sectionClient);
        return section;
    }

    // private void createSimpleTable(final ScrolledForm form, final Composite composite,
    // final ColumnSetMultiValueIndicator columnSetMultiValueIndicator) {
    // // final TableViewer tbViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
    //        NumberFormat doubleFormat = new DecimalFormat("0.00"); //$NON-NLS-1$
    // final Table table = new Table(composite, SWT.FULL_SELECTION | SWT.BORDER);
    // GridData gd = new GridData(GridData.FILL_BOTH);
    // gd.heightHint = 200;
    // table.setLayoutData(gd);
    // table.setVisible(true);
    // table.setHeaderVisible(true);
    // table.setLinesVisible(true);
    // String[] titles = {
    //                DefaultMessagesImpl.getString("ColumnCorrelationNominalIntervalResultPage.Label"), DefaultMessagesImpl.getString("ColumnCorrelationNominalIntervalResultPage.Count"), "%" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    // for (String title : titles) {
    // TableColumn column = new TableColumn(table, SWT.NONE);
    // column.setText(title);
    // column.setWidth(100);
    // }
    // String[] label = {
    //                DefaultMessagesImpl.getString("ColumnCorrelationNominalIntervalResultPage.RowCount"), DefaultMessagesImpl.getString("ColumnCorrelationNominalIntervalResultPage.DistinctCount"), DefaultMessagesImpl.getString("ColumnCorrelationNominalIntervalResultPage.UniqueCount"), DefaultMessagesImpl.getString("ColumnCorrelationNominalIntervalResultPage.DuplicateCount") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    //
    // Long countAll = columnSetMultiValueIndicator.getCount();
    // Long distinctCount = columnSetMultiValueIndicator.getDistinctCount();
    // Long uniqueCount = columnSetMultiValueIndicator.getUniqueCount();
    // Long duplicateCount = columnSetMultiValueIndicator.getDuplicateCount();
    // if (countAll != null && distinctCount != null && uniqueCount != null && duplicateCount != null) {
    // long[] count = { countAll, distinctCount, uniqueCount, duplicateCount };
    // double[] percent = new double[4];
    // for (int i = 0; i < count.length; i++) {
    // percent[i] = (double) count[i] / count[0];
    // }
    // for (int itemCount = 0; itemCount < 4; itemCount++) {
    // TableItem item = new TableItem(table, SWT.NONE);
    // if (count[0] == 0) {
    //                    item.setText(new String[] { label[itemCount], String.valueOf(count[itemCount]), "N/A" }); //$NON-NLS-1$
    // continue;
    // }
    //
    // item.setText(new String[] { label[itemCount], String.valueOf(count[itemCount]),
    //                        doubleFormat.format(percent[itemCount] * 100) + "%" }); //$NON-NLS-1$
    // }
    // }
    //
    // for (int i = 0; i < table.getColumnCount(); i++) {
    // table.getColumn(i).pack();
    // }
    // }
    //
    // private void createSimpleStatistics(final ScrolledForm form, final Composite composite,
    // final ColumnSetMultiValueIndicator columnSetMultiValueIndicator) {
    // DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    // dataset.addValue(columnSetMultiValueIndicator.getCount(), DefaultMessagesImpl
    //                .getString("ColumnCorrelationNominalIntervalResultPage.Row_Count"), ""); //$NON-NLS-1$ //$NON-NLS-2$
    // dataset.addValue(columnSetMultiValueIndicator.getDistinctCount(), DefaultMessagesImpl
    //                .getString("ColumnCorrelationNominalIntervalResultPage.Distinct_Count"), ""); //$NON-NLS-1$ //$NON-NLS-2$
    // dataset.addValue(columnSetMultiValueIndicator.getUniqueCount(), DefaultMessagesImpl
    //                .getString("ColumnCorrelationNominalIntervalResultPage.Unique_Count"), ""); //$NON-NLS-1$ //$NON-NLS-2$
    // dataset.addValue(columnSetMultiValueIndicator.getDuplicateCount(), DefaultMessagesImpl
    //                .getString("ColumnCorrelationNominalIntervalResultPage.Duplicate_Count"), ""); //$NON-NLS-1$ //$NON-NLS-2$
    //
    // JFreeChart chart = TopChartFactory.createBarChart(DefaultMessagesImpl
    //                .getString("ColumnCorrelationNominalIntervalResultPage.SimpleSatistics"), dataset, true); //$NON-NLS-1$
    //
    // // MOD mzhao 2009-07-28 Bind the indicator with specific color.
    // if (chart != null) {
    // Plot plot = chart.getPlot();
    // if (plot instanceof CategoryPlot) {
    // ChartDecorator.decorateCategoryPlot(chart);
    // // Row Count
    // ((CategoryPlot) plot).getRenderer()
    // .setSeriesPaint(0, ChartDecorator.IndiBindColor.INDICATOR_ROW_COUNT.getColor());
    // // Distinct Count
    // ((CategoryPlot) plot).getRenderer().setSeriesPaint(1,
    // ChartDecorator.IndiBindColor.INDICATOR_DISTINCT_COUNT.getColor());
    // // Unique Count
    // ((CategoryPlot) plot).getRenderer().setSeriesPaint(2,
    // ChartDecorator.IndiBindColor.INDICATOR_UNIQUE_COUNT.getColor());
    // // Duplicate Count
    // ((CategoryPlot) plot).getRenderer().setSeriesPaint(3,
    // ChartDecorator.IndiBindColor.INDICATOR_DUPLICATE_COUNT.getColor());
    //
    // }
    // }
    //
    // ChartComposite chartComp = new ChartComposite(composite, SWT.NONE, chart);
    // chartComp.setLayoutData(new GridData(GridData.FILL_BOTH));
    // // ChartUtils.createAWTSWTComp(composite, new GridData(GridData.FILL_BOTH), chart);
    // }

    private void createSimpleStatistics2(final ScrolledForm form, final Composite composite,
            final ColumnSetMultiValueIndicator columnSetMultiValueIndicator) {
        List<IndicatorUnit> units = new ArrayList<IndicatorUnit>();
        units.add(new IndicatorUnit(IndicatorEnum.RowCountIndicatorEnum, columnSetMultiValueIndicator.getRowCountIndicator(),
                null));
        units.add(new IndicatorUnit(IndicatorEnum.DistinctCountIndicatorEnum, columnSetMultiValueIndicator
                .getDistinctCountIndicator(), null));
        units.add(new IndicatorUnit(IndicatorEnum.DuplicateCountIndicatorEnum, columnSetMultiValueIndicator
                .getDuplicateCountIndicator(), null));
        units.add(new IndicatorUnit(IndicatorEnum.UniqueIndicatorEnum, columnSetMultiValueIndicator.getUniqueCountIndicator(),
                null));

        EIndicatorChartType simpleStatType = EIndicatorChartType.SIMPLE_STATISTICS;
        IChartTypeStates chartTypeState = ChartTypeStatesOperator.getChartState(simpleStatType, units);
        ChartWithData chartData = new ChartWithData(simpleStatType, chartTypeState.getChart(), chartTypeState.getDataEntity());

        TableViewer tableviewer = chartTypeState.getTableForm(composite);
        tableviewer.setInput(chartData);
        TableUtils.addTooltipOnTableItem(tableviewer.getTable());

        // create chart
        if (!EditorPreferencePage.isHideGraphics()) {
            JFreeChart chart = chartTypeState.getChart();
            ChartDecorator.decorate(chart, null);
            if (chart != null) {
                ChartComposite cc = new ChartComposite(composite, SWT.NONE, chart, true);

                GridData gd = new GridData();
                gd.widthHint = PluginConstant.CHART_STANDARD_WIDHT;
                gd.heightHint = PluginConstant.CHART_STANDARD_HEIGHT;
                cc.setLayoutData(gd);
            }
        }
    }

    private Section createTableSectionPart(Composite parentComp, String title,
            ColumnSetMultiValueIndicator columnSetMultiIndicator) {
        Section columnSetElementSection = this.createSection(form, parentComp, title, null);
        Composite sectionTableComp = toolkit.createComposite(columnSetElementSection);
        sectionTableComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        sectionTableComp.setLayout(new GridLayout());

        TableViewer columnsElementViewer = new TableViewer(sectionTableComp, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
        Table table = columnsElementViewer.getTable();

        List<String> tableColumnNames = columnSetMultiIndicator.getColumnHeaders();
        for (String tableColumnName : tableColumnNames) {
            final TableColumn columnHeader = new TableColumn(table, SWT.NONE);
            columnHeader.setText(tableColumnName);
        }
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        TableSectionViewerProvider provider = new TableSectionViewerProvider();
        List<Object[]> tableRows = columnSetMultiIndicator.getListRows();
        columnsElementViewer.setContentProvider(provider);
        columnsElementViewer.setLabelProvider(provider);
        columnsElementViewer.setInput(tableRows);
        for (int i = 0; i < tableColumnNames.size(); i++) {
            table.getColumn(i).pack();
        }
        columnSetElementSection.setClient(sectionTableComp);
        // ADDED sgandon 15/03/2010 bug 11769 : setup the size of the table to avoid crash and add consistency.
        // MOD msjian 2011-5-30 17479: Excel Odbc connection can not run well on the correlation analysis
        setupTableGridDataLimitedSize(table, tableRows != null ? tableRows.size() : 0);
        // ~
        addColumnSorters(columnsElementViewer, table.getColumns(), this.buildSorter(tableRows));
        columnSetElementSection.setExpanded(false);
        return columnSetElementSection;
    }

    /**
     * 
     * DOC hcheng Comment method "addColumnSorters".For 8267.
     * 
     * @param tableViewer
     * @param tableColumns
     * @param sorters
     */
    protected void addColumnSorters(TableViewer tableViewer, TableColumn[] tableColumns, ViewerSorter[][] sorters) {
        for (int i = 0; i < tableColumns.length; ++i) {
            tableColumns[i].addSelectionListener(new ColumnSortListener(tableColumns, i, tableViewer, sorters));
        }
    }

    private Color bg = new Color(null, 249, 139, 121);

    /**
     * 
     * DOC zhaoxinyi ColumnCorrelationNominalIntervalResultPage class global comment. Detailled comment
     */
    class TableSectionViewerProvider implements IStructuredContentProvider, ITableLabelProvider, ITableColorProvider {

        @SuppressWarnings("unchecked")
        public Object[] getElements(Object inputElement) {
            List<Object> columnDataSet = (List<Object>) inputElement;
            return columnDataSet.toArray();
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

        public Image getImage(Object element) {
            if (element instanceof TdColumn) {
                return ImageLib.getImage(ImageLib.TD_COLUMN);
            }
            return null;
        }

        public void dispose() {
            // TODO Auto-generated method stub

        }

        public Image getColumnImage(Object element, int columnIndex) {
            // TODO Auto-generated method stub
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            for (int i = 0; i < ((Object[]) element).length; i++) {
                if (columnIndex == i) {
                    return String.valueOf(((Object[]) element)[i]);
                }
            }
            return null;
        }

        public void addListener(ILabelProviderListener listener) {
            // TODO Auto-generated method stub

        }

        public boolean isLabelProperty(Object element, String property) {
            // TODO Auto-generated method stub
            return false;
        }

        public void removeListener(ILabelProviderListener listener) {
            // TODO Auto-generated method stub

        }

        public Color getBackground(Object element, int columnIndex) {
            Object[] elements = (Object[]) element;

            for (Object elem : elements) {
                if (elem == null || "".equals(elem)) { //$NON-NLS-1$
                    return bg;
                }
            }
            return null;
        }

        public Color getForeground(Object element, int columnIndex) {
            // TODO Auto-generated method stub
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractFormPage#setDirty(boolean)
     */
    @Override
    public void setDirty(boolean isDirty) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (PluginConstant.ISDIRTY_PROPERTY.equals(evt.getPropertyName())) {
            ((AnalysisEditor) this.getEditor()).firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisResultPage#refresh(org.talend.dataprofiler.core
     * .ui.editor.analysis.AbstractAnalysisMetadataPage)
     */
    @Override
    public void refresh(AbstractAnalysisMetadataPage masterPage) {
        this.masterPage = (ColumnCorrelationNominalAndIntervalMasterPage) masterPage;

        if (summaryComp != null && !summaryComp.isDisposed()) {
            summaryComp.dispose();
        }

        if (graphicsAndTableComp != null && !graphicsAndTableComp.isDisposed()) {
            graphicsAndTableComp.dispose();
        }
        masterPage.refresh();
        createFormContent(getManagedForm());
    }

    public Composite getChartComposite() {
        return chartComposite;
    }

    public Composite[] getPreviewChartCompsites() {
        return previewChartCompsites;
    }

    @Override
    public void dispose() {
        if (bg != null) {
            bg.dispose();
        }
        super.dispose();
    }

    private CorrelationDataSorter[][] buildSorter(List<Object[]> tableRows) {
        CorrelationDataSorter[][] result = null;
        if (tableRows != null && tableRows.size() > 0) {
            Object[] objs = tableRows.get(0);
            int length = objs.length;
            result = new CorrelationDataSorter[length][2];
            for (int i = 0; i < length; ++i) {
                result[i][0] = new CorrelationDataSorter(i + 1);
                result[i][1] = new CorrelationDataSorter(-(i + 1));
            }
        }
        return result;
    }
}