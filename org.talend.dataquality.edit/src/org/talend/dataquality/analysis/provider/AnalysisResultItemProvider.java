/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.dataquality.analysis.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisPackage;
import org.talend.dataquality.analysis.AnalysisResult;

import org.talend.dataquality.domain.DomainFactory;

import orgomg.cwm.objectmodel.core.CorePackage;

import orgomg.cwmx.analysis.informationreporting.provider.ReportGroupItemProvider;

/**
 * This is the item provider adapter for a {@link org.talend.dataquality.analysis.AnalysisResult} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class AnalysisResultItemProvider
    extends ReportGroupItemProvider
    implements	
        IEditingDomainItemProvider,	
        IStructuredItemContentProvider,	
        ITreeItemContentProvider,	
        IItemLabelProvider,	
        IItemPropertySource {
    /**
     * This constructs an instance from a factory and a notifier.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AnalysisResultItemProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    /**
     * This returns the property descriptors for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
        if (itemPropertyDescriptors == null) {
            super.getPropertyDescriptors(object);

            addIndicatorsPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Indicators feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addIndicatorsPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_AnalysisResult_indicators_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_AnalysisResult_indicators_feature", "_UI_AnalysisResult_type"),
                 AnalysisPackage.Literals.ANALYSIS_RESULT__INDICATORS,
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
     * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
     * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
        if (childrenFeatures == null) {
            super.getChildrenFeatures(object);
            childrenFeatures.add(AnalysisPackage.Literals.ANALYSIS_RESULT__RESULT_METADATA);
            childrenFeatures.add(AnalysisPackage.Literals.ANALYSIS_RESULT__INDICATOR_VALUES);
        }
        return childrenFeatures;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EStructuralFeature getChildFeature(Object object, Object child) {
        // Check the type of the specified child object and return the proper feature to use for
        // adding (see {@link AddCommand}) it as a child.

        return super.getChildFeature(object, child);
    }

    /**
     * This returns AnalysisResult.gif.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/AnalysisResult"));
    }

    /**
     * This returns the label text for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((AnalysisResult)object).getName();
        return label == null || label.length() == 0 ?
            getString("_UI_AnalysisResult_type") :
            getString("_UI_AnalysisResult_type") + " " + label;
    }

    /**
     * This handles model notifications by calling {@link #updateChildren} to update any cached
     * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void notifyChanged(Notification notification) {
        updateChildren(notification);

        switch (notification.getFeatureID(AnalysisResult.class)) {
            case AnalysisPackage.ANALYSIS_RESULT__RESULT_METADATA:
            case AnalysisPackage.ANALYSIS_RESULT__INDICATOR_VALUES:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
                return;
        }
        super.notifyChanged(notification);
    }

    /**
     * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
     * that can be created under this object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
        super.collectNewChildDescriptors(newChildDescriptors, object);

        newChildDescriptors.add
            (createChildParameter
                (AnalysisPackage.Literals.ANALYSIS_RESULT__RESULT_METADATA,
                 AnalysisFactory.eINSTANCE.createExecutionInformations()));

        newChildDescriptors.add
            (createChildParameter
                (AnalysisPackage.Literals.ANALYSIS_RESULT__INDICATOR_VALUES,
                 DomainFactory.eINSTANCE.createLiteralValue()));

        newChildDescriptors.add
            (createChildParameter
                (AnalysisPackage.Literals.ANALYSIS_RESULT__INDICATOR_VALUES,
                 DomainFactory.eINSTANCE.createNumericValue()));

        newChildDescriptors.add
            (createChildParameter
                (AnalysisPackage.Literals.ANALYSIS_RESULT__INDICATOR_VALUES,
                 DomainFactory.eINSTANCE.createTextValue()));

        newChildDescriptors.add
            (createChildParameter
                (AnalysisPackage.Literals.ANALYSIS_RESULT__INDICATOR_VALUES,
                 DomainFactory.eINSTANCE.createIntegerValue()));

        newChildDescriptors.add
            (createChildParameter
                (AnalysisPackage.Literals.ANALYSIS_RESULT__INDICATOR_VALUES,
                 DomainFactory.eINSTANCE.createRealNumberValue()));

        newChildDescriptors.add
            (createChildParameter
                (AnalysisPackage.Literals.ANALYSIS_RESULT__INDICATOR_VALUES,
                 DomainFactory.eINSTANCE.createDateValue()));
    }

    /**
     * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
        Object childFeature = feature;
        Object childObject = child;

        boolean qualify =
            childFeature == CorePackage.Literals.NAMESPACE__OWNED_ELEMENT ||
            childFeature == CorePackage.Literals.CLASSIFIER__FEATURE;

        if (qualify) {
            return getString
                ("_UI_CreateChild_text2",
                 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
        }
        return super.getCreateChildText(owner, feature, child, selection);
    }

    /**
     * Return the resource locator for this item provider's resources.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ResourceLocator getResourceLocator() {
        return DataqualityEditPlugin.INSTANCE;
    }

}
