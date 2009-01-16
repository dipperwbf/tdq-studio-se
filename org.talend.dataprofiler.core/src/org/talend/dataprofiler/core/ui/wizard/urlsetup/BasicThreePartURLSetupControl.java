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
package org.talend.dataprofiler.core.ui.wizard.urlsetup;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Driver;

import net.sourceforge.sqlexplorer.util.MyURLClassLoader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.cwm.dburl.SupportDBUrlStore;
import org.talend.cwm.dburl.SupportDBUrlType;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dq.analysis.parameters.DBConnectionParameter;

/**
 * 
 */
public class BasicThreePartURLSetupControl extends URLSetupControl {

    private Text urlText;

    public static Driver driver;

    public BasicThreePartURLSetupControl(Composite parent, SupportDBUrlType dbType) {
        super(parent, dbType);
    }

    protected void createPart(Composite parent, String dbLiteral, final DBConnectionParameter connectionParam) {
        if (dbLiteral.trim().equals("Generic JDBC")) {
            GridLayout layout = new GridLayout();
            layout.numColumns = 3;
            parent.setLayout(layout);
            Label labelJar = new Label(parent, SWT.NONE);
            labelJar.setText("Driver jar");
            final Text jarText = new Text(parent, SWT.BORDER | SWT.SINGLE);
            jarText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            jarText.setEditable(false);
            jarText.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    connectionParam.setDriverPath(jarText.getText());
                }

            });
            final Button selectJar = new Button(parent, SWT.PUSH);
            final StringBuilder filenameAll = new StringBuilder();
            selectJar.setText("...");
            selectJar.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent event) {
                    FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell());
                    String filename = dialog.open();
                    if (filename != null) {
                        filenameAll.append(filename + ";");
                        // filenameAll.deleteCharAt(0);
                        jarText.setText(filenameAll.toString());
                    } else {
                        jarText.setText("");
                    }
                }
            });
            Label labelDriver = new Label(parent, SWT.NONE);
            labelDriver.setText("Driver Class Name");
            // int comboUrlCount = 0;
            // String[] dbDriverName = new String[SupportDBUrlType.values().length - 1];
            // for (SupportDBUrlType dbType : SupportDBUrlType.values()) {
            // if (!dbType.getDbDriver().trim().equals("")) {
            // dbDriverName[comboUrlCount++] = dbType.getDbDriver();
            // }
            // }
            final Combo comboDriver = new Combo(parent, SWT.READ_ONLY);
            comboDriver.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            // comboDriver.setItems(dbDriverName);
            comboDriver.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    connectionParam.setDriverClassName(comboDriver.getText());
                }

            });

            Button listDriverBtn = new Button(parent, SWT.PUSH);
            listDriverBtn.setText("List Drivers");
            listDriverBtn.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    comboDriver.removeAll();
                    for (String stringToFile : jarText.getText().trim().split(";")) {
                        File file = new File(stringToFile);
                        if (file != null) {
                            try {
                                MyURLClassLoader cl = new MyURLClassLoader(file.toURL());
                                Class[] classes = cl.getAssignableClasses(Driver.class);
                                for (int i = 0; i < classes.length; ++i) {
                                    comboDriver.add(classes[i].getName());
                                }
                            } catch (MalformedURLException ex) {
                                ex.printStackTrace();

                            } catch (IOException ex) {
                                ex.printStackTrace();

                            }
                        }
                    }
                    if (comboDriver.getItemCount() > 0) {
                        comboDriver.setText(comboDriver.getItem(0));
                    }
                }
            });

            Label labelUrl = new Label(parent, SWT.NONE);
            labelUrl.setText("Url");
            final Text urlText = new Text(parent, SWT.BORDER | SWT.SINGLE);
            urlText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            urlText.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    connectionParam.setJdbcUrl(urlText.getText());
                }

            });

        } else if (dbLiteral.trim().equals("SQLite3")) {
            GridLayout layout = new GridLayout();
            layout.numColumns = 3;
            parent.setLayout(layout);

            Label labelfile = new Label(parent, SWT.NONE);
            final Text fileText = new Text(parent, SWT.BORDER | SWT.SINGLE);
            final Button selectFile = new Button(parent, SWT.PUSH);
            Label labelUrl = new Label(parent, SWT.NONE);
            final Text urlText = new Text(parent, SWT.BORDER | SWT.SINGLE);

            labelfile.setText("File");
            fileText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            fileText.setEditable(false);
            fileText.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    connectionParam.setFilePath(fileText.getText());
                }

            });

            selectFile.setText("Browser...");

            labelUrl.setText("Url");
            urlText.setEditable(false);
            urlText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            setConnectionURL(SupportDBUrlStore.getInstance().getDBUrl(dbType.getDBKey(), "", "", fileText.getText(), "", ""));
            urlText.setText(getConnectionURL());
            urlText.setEditable(false);

            urlText.addKeyListener(new KeyAdapter() {

                public void keyReleased(KeyEvent e) {
                    setConnectionURL(urlText.getText());
                }

            });

            urlText.addFocusListener(new FocusAdapter() {

                public void focusGained(FocusEvent e) {
                    urlText.setEditable(true);
                }

                public void focusLost(FocusEvent e) {
                    urlText.setEditable(false);
                }
            });

            selectFile.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent event) {
                    FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell());
                    String filename = dialog.open();
                    if (filename != null) {
                        fileText.setText(filename);
                    } else {
                        fileText.setText("");
                    }
                    String url = SupportDBUrlStore.getInstance().getDBUrl(dbType.getDBKey(), "", "", fileText.getText(), "", "");
                    String validURL = getValidURL(url);
                    setConnectionURL(validURL);
                    urlText.setText(getConnectionURL());
                }

                private String getValidURL(String url) {
                    if (url.indexOf("\\") != 0) {
                        url = url.replace("\\", "\\\\");
                    }
                    return url;
                }
            });

        } else {
            GridLayout layout = new GridLayout();
            layout.numColumns = 2;
            parent.setLayout(layout);

            boolean compositeEnable = !(dbType.getHostName() == null);
            Label label = new Label(parent, SWT.NONE);
            label.setText(DefaultMessagesImpl.getString("BasicThreePartURLSetupControl.Hostname")); //$NON-NLS-1$
            final Text hostNameText = new Text(parent, SWT.BORDER | SWT.SINGLE);
            hostNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            if (compositeEnable) {
                hostNameText.setText(dbType.getHostName());
            }
            label.setEnabled(compositeEnable);
            hostNameText.setEnabled(compositeEnable);

            compositeEnable = !(dbType.getPort() == null);
            label = new Label(parent, SWT.NONE);
            label.setText(DefaultMessagesImpl.getString("BasicThreePartURLSetupControl.Port")); //$NON-NLS-1$
            final Text portText = new Text(parent, SWT.BORDER | SWT.SINGLE);
            portText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            if (compositeEnable) {
                portText.setText(dbType.getPort());
            }
            label.setEnabled(compositeEnable);
            portText.setEnabled(compositeEnable);

            compositeEnable = !(dbType.getDBName() == null);
            label = new Label(parent, SWT.NONE);
            if (dbType == SupportDBUrlType.ORACLEWITHSIDDEFAULTURL) {
                label.setText(DefaultMessagesImpl.getString("BasicThreePartURLSetupControl.SID")); //$NON-NLS-1$
            } else if (dbType == SupportDBUrlType.ORACLEWITHSERVICENAMEDEFAULTURL) {
                label.setText(DefaultMessagesImpl.getString("BasicThreePartURLSetupControl.serviceName")); //$NON-NLS-1$
            } else {
                label.setText("DBname"); //$NON-NLS-1$
            }
            final Text databaseNameText = new Text(parent, SWT.BORDER | SWT.SINGLE);
            databaseNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            if (compositeEnable) {
                databaseNameText.setText(dbType.getDBName());
            }
            label.setEnabled(compositeEnable);
            databaseNameText.setEnabled(compositeEnable);

            compositeEnable = !(dbType.getParamSeprator() == null);
            label = new Label(parent, SWT.NONE);
            label.setText("Additional JDBC Parameters"); //$NON-NLS-1$
            final Text parameterText = new Text(parent, SWT.BORDER | SWT.SINGLE);
            parameterText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            if (dbType.getParamSeprator() != null) {
                parameterText.setText(org.talend.dq.PluginConstant.DEFAULT_PARAMETERS);
            } else {
                parameterText.setText(PluginConstant.EMPTY_STRING);
            }
            parameterText.setEnabled(compositeEnable);
            label.setEnabled(compositeEnable);
            parameterText.setEnabled(compositeEnable);

            compositeEnable = !(dbType.getDataSource() == null);
            label = new Label(parent, SWT.NONE);
            label.setText(DefaultMessagesImpl.getString("BasicThreePartURLSetupControl.dataSource")); //$NON-NLS-1$
            final Text dataSourceText = new Text(parent, SWT.BORDER | SWT.SINGLE);
            dataSourceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            if (compositeEnable) {
                dataSourceText.setText(dbType.getDataSource());
            }
            label.setEnabled(compositeEnable);
            dataSourceText.setEnabled(compositeEnable);

            label = new Label(parent, SWT.NONE);
            label.setText(DefaultMessagesImpl.getString("BasicThreePartURLSetupControl.url")); //$NON-NLS-1$
            urlText = new Text(parent, SWT.BORDER | SWT.SINGLE);
            urlText.setEditable(false);
            urlText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            urlText.addFocusListener(new FocusAdapter() {

                public void focusGained(FocusEvent e) {
                    urlText.setEditable(true);
                }

                public void focusLost(FocusEvent e) {
                    urlText.setEditable(false);
                }
            });
            urlText.setText(getConnectionURL());
            urlText.addKeyListener(new KeyAdapter() {

                public void keyReleased(KeyEvent e) {
                    setConnectionURL(urlText.getText());
                }

            });

            dataSourceText.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent event) {
                    setConnectionURL(SupportDBUrlStore.getInstance().getDBUrl(dbType.getDBKey(), hostNameText.getText(),
                            portText.getText(), databaseNameText.getText(), dataSourceText.getText(), parameterText.getText()));
                    urlText.setText(getConnectionURL());
                }
            });

            hostNameText.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent event) {
                    setConnectionURL(SupportDBUrlStore.getInstance().getDBUrl(dbType.getDBKey(), hostNameText.getText(),
                            portText.getText(), databaseNameText.getText(), dataSourceText.getText(), parameterText.getText()));
                    urlText.setText(getConnectionURL());
                }
            });

            portText.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent event) {

                    setConnectionURL(SupportDBUrlStore.getInstance().getDBUrl(dbType.getDBKey(), hostNameText.getText(),
                            portText.getText(), databaseNameText.getText(), dataSourceText.getText(), parameterText.getText()));
                    urlText.setText(getConnectionURL());
                }
            });
            portText.addKeyListener(new KeyAdapter() {

                public void keyReleased(KeyEvent e) {
                    Long portValue = null;
                    try {
                        portValue = new Long(portText.getText());
                    } catch (NumberFormatException e1) {
                        // JUMP
                    }
                    if (portValue == null || portValue <= 0) {
                        portText.setText(PluginConstant.EMPTY_STRING); //$NON-NLS-1$
                    }
                }
            });

            databaseNameText.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent event) {
                    setConnectionURL(SupportDBUrlStore.getInstance().getDBUrl(dbType.getDBKey(), hostNameText.getText(),
                            portText.getText(), databaseNameText.getText(), dataSourceText.getText(), parameterText.getText()));
                    urlText.setText(getConnectionURL());
                    SupportDBUrlStore.getInstance().changeAllDBNmae(databaseNameText.getText());
                }
            });

            parameterText.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    setConnectionURL(SupportDBUrlStore.getInstance().getDBUrl(dbType.getDBKey(), hostNameText.getText(),
                            portText.getText(), databaseNameText.getText(), dataSourceText.getText(), parameterText.getText()));
                    urlText.setText(getConnectionURL());
                }

            });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.urlsetup.URLSetupControl#setConnectionURL(java.lang.String)
     */
    @Override
    public void setConnectionURL(String connectionURL) {
        super.setConnectionURL(connectionURL);
        if (urlText != null) {
            this.urlText.setText(connectionURL);
        }
    }
}
