/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.dataquality.indicators.sql.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.talend.dataquality.indicators.sql.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class IndicatorSqlFactoryImpl extends EFactoryImpl implements IndicatorSqlFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static IndicatorSqlFactory init() {
        try {
            IndicatorSqlFactory theIndicatorSqlFactory = (IndicatorSqlFactory)EPackage.Registry.INSTANCE.getEFactory("http://dataquality.indicators.sql"); 
            if (theIndicatorSqlFactory != null) {
                return theIndicatorSqlFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new IndicatorSqlFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IndicatorSqlFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case IndicatorSqlPackage.SQL_INDICATOR: return createSqlIndicator();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SqlIndicator createSqlIndicator() {
        SqlIndicatorImpl sqlIndicator = new SqlIndicatorImpl();
        return sqlIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IndicatorSqlPackage getIndicatorSqlPackage() {
        return (IndicatorSqlPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static IndicatorSqlPackage getPackage() {
        return IndicatorSqlPackage.eINSTANCE;
    }

} //IndicatorSqlFactoryImpl
