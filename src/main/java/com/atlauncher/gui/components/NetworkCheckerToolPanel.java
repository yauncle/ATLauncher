/*
 * ATLauncher - https://github.com/ATLauncher/ATLauncher
 * Copyright (C) 2013-2019 ATLauncher
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.atlauncher.gui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

import com.atlauncher.App;
import com.atlauncher.FileSystem;
import com.atlauncher.LogManager;
import com.atlauncher.builders.HTMLBuilder;
import com.atlauncher.data.Constants;
import com.atlauncher.evnt.listener.SettingsListener;
import com.atlauncher.evnt.manager.SettingsManager;
import com.atlauncher.gui.dialogs.ProgressDialog;
import com.atlauncher.managers.DialogManager;
import com.atlauncher.network.Analytics;
import com.atlauncher.network.Download;
import com.atlauncher.utils.Utils;

import org.mini2Dx.gettext.GetText;

public class NetworkCheckerToolPanel extends AbstractToolPanel implements ActionListener, SettingsListener {
    private static final long serialVersionUID = 4811953376698111667L;

    private final JLabel TITLE_LABEL = new JLabel(GetText.tr("Network Checker"));

    private final JLabel INFO_LABEL = new JLabel(new HTMLBuilder().center().split(60)
            .text(GetText
                    .tr("This tool does various tests on your network and determines any issues that may pop up with "
                            + "connecting to our file servers and to other servers."))
            .build());

    public NetworkCheckerToolPanel() {
        TITLE_LABEL.setFont(BOLD_FONT);
        TOP_PANEL.add(TITLE_LABEL);
        MIDDLE_PANEL.add(INFO_LABEL);
        BOTTOM_PANEL.add(LAUNCH_BUTTON);
        LAUNCH_BUTTON.addActionListener(this);
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        SettingsManager.addListener(this);
        this.checkLaunchButtonEnabled();
    }

    private void checkLaunchButtonEnabled() {
        LAUNCH_BUTTON.setEnabled(App.settings.enableLogs());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Analytics.sendEvent("NetworkChecker", "Run", "Tool");

        int ret = DialogManager.yesNoDialog().setTitle(GetText.tr("Network Checker"))
                .setContent(new HTMLBuilder().center().split(75).text(GetText.tr(
                        "Please note that the data from this tool is sent to ATLauncher so we can diagnose possible issues in your setup. This test may take up to 10 minutes or longer to complete and you will be unable to do anything while it's running. Please also keep in mind that this test will use some of your bandwidth, it will use approximately 20MB.<br/><br/>Do you wish to continue?"))
                        .build())
                .setType(DialogManager.INFO).show();

        if (ret == 0) {
            final ProgressDialog dialog = new ProgressDialog(GetText.tr("Network Checker"), 5,
                    GetText.tr("Network Checker Running. Please Wait!"), "Network Checker Tool Cancelled!");
            dialog.addThread(new Thread(() -> {
                StringBuilder results = new StringBuilder();

                // Ping Test
                results.append("Ping results to " + Constants.DOWNLOAD_HOST + " was "
                        + Utils.pingAddress(Constants.DOWNLOAD_HOST) + "\n\n----------------\n\n");
                dialog.doneTask();

                results.append(
                        "Tracert to " + Constants.DOWNLOAD_HOST + " was " + Utils.traceRoute(Constants.DOWNLOAD_HOST));
                dialog.doneTask();

                // Response Code Test
                try {
                    results.append(String.format("Response code to %s was %d\n\n----------------\n\n",
                            Constants.DOWNLOAD_SERVER,
                            Download.build()
                                    .setUrl(String.format("%s/launcher/json/hashes.json", Constants.DOWNLOAD_SERVER))
                                    .getResponseCode()));
                } catch (Exception e1) {
                    results.append(String.format("Exception thrown when connecting to %s\n\n----------------\n\n",
                            Constants.DOWNLOAD_SERVER));
                    results.append(e1.toString());
                }
                dialog.doneTask();

                // Ping Pong Test
                results.append(String.format("Response to ping on %s was %s\n\n----------------\n\n",
                        Constants.DOWNLOAD_SERVER,
                        Download.build().setUrl(String.format("%s/ping", Constants.DOWNLOAD_SERVER)).asString()));
                dialog.doneTask();

                // Speed Test
                File file = FileSystem.TEMP.resolve("20MB.test").toFile();
                if (file.exists()) {
                    Utils.delete(file);
                }
                long started = System.currentTimeMillis();
                try {
                    Download.build().setUrl(String.format("%s/20MB.test", Constants.DOWNLOAD_SERVER))
                            .downloadTo(file.toPath()).downloadFile();
                } catch (Exception e2) {
                    results.append(
                            String.format("Exception thrown when downloading 20MB.test from %s\n\n----------------\n\n",
                                    Constants.DOWNLOAD_SERVER));
                    results.append(e2.toString());
                }

                long timeTaken = System.currentTimeMillis() - started;
                float bps = file.length() / (timeTaken / 1000);
                float kbps = bps / 1024;
                float mbps = kbps / 1024;
                String speed = (mbps < 1
                        ? (kbps < 1 ? String.format("%.2f B/s", bps) : String.format("%.2f " + "KB/s", kbps))
                        : String.format("%.2f MB/s", mbps));
                results.append(String.format(
                        "Download speed to %s was %s, " + ""
                                + "taking %.2f seconds to download 20MB\n\n----------------\n\n",
                        Constants.DOWNLOAD_SERVER, speed, (timeTaken / 1000.0)));
                dialog.doneTask();

                String result = Utils.uploadPaste(Constants.LAUNCHER_NAME + " Network Test Log", results.toString());
                if (result.contains(Constants.PASTE_CHECK_URL)) {
                    LogManager.info("Network Test has finished running, you can view the results at " + result);
                } else {
                    LogManager.error("Network Test failed to submit to " + Constants.LAUNCHER_NAME + "!");
                    dialog.setReturnValue(false);
                }

                dialog.doneTask();
                dialog.setReturnValue(true);
                dialog.close();
            }));
            dialog.start();
            if (dialog.getReturnValue() == null || !(Boolean) dialog.getReturnValue()) {
                LogManager.error("Network Test failed to run!");
            } else {
                LogManager.info("Network Test ran and submitted to " + Constants.LAUNCHER_NAME + "!");

                DialogManager.okDialog().setTitle(GetText.tr("Network Checker"))
                        .setContent(new HTMLBuilder().center().text(GetText.tr(
                        "The network checker tool has completed and the data sent off to ATLauncher.<br/><br/>Thanks for your input to help understand and fix network related issues."))
                        .build())
                        .setType(DialogManager.INFO).show();
            }
        }
    }

    @Override
    public void onSettingsSaved() {
        this.checkLaunchButtonEnabled();
    }
}
