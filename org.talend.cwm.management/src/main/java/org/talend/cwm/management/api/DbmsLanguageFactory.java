// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.cwm.management.api;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.softwaredeployment.TdDataProvider;
import org.talend.cwm.softwaredeployment.TdSoftwareSystem;
import orgomg.cwm.foundation.softwaredeployment.DataManager;

/**
 * @author scorreia
 * 
 * Factory for the creation of DbmsLanguage objects.
 */
public final class DbmsLanguageFactory {

    private static Logger log = Logger.getLogger(DbmsLanguageFactory.class);

    private DbmsLanguageFactory() {
        // avoid instantiation
    }

    /**
     * Method "createDbmsLanguage".
     * 
     * @param dataManager a data manager used for initializing the correct language in the created DbmsLanguage
     * @return a new DbmsLanguage even if the data manager did not allow to get the correct language
     */
    public static DbmsLanguage createDbmsLanguage(DataManager dataManager) {
        if (dataManager == null) {
            return new DbmsLanguage();
        }
        TdDataProvider dataprovider = SwitchHelpers.TDDATAPROVIDER_SWITCH.doSwitch(dataManager);
        if (dataprovider == null) {
            return new DbmsLanguage();
        }

        TdSoftwareSystem softwareSystem = SoftwareSystemManager.getInstance().getSoftwareSystem(dataprovider);
        if (softwareSystem == null) {
            return new DbmsLanguage();
        }
        return new DbmsLanguage(softwareSystem.getSubtype());
    }

    /**
     * Method "createDbmsLanguage".
     * 
     * @param connection a connection (must be open)
     * @return the appropriate DbmsLanguage or a default one if something failed with the connection.
     */
    public static DbmsLanguage createDbmsLanguage(Connection connection) {
        assert connection != null;
        String databaseProductName;
        try {
            databaseProductName = connection.getMetaData().getDatabaseProductName();
            return new DbmsLanguage(databaseProductName);
        } catch (SQLException e) {
            log.warn("Exception when retrieving database informations:" + e + ". Creating a default DbmsLanguage.", e);
            return new DbmsLanguage();
        }

    }
}
