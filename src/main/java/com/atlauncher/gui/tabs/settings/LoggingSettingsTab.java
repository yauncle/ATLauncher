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
package com.atlauncher.gui.tabs.settings;

import java.awt.GridBagConstraints;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import com.atlauncher.App;
import com.atlauncher.gui.components.JLabelWithHover;
import com.atlauncher.utils.Utils;

import org.mini2Dx.gettext.GetText;

@SuppressWarnings("serial")
public class LoggingSettingsTab extends AbstractSettingsTab {
    private JLabelWithHover forgeLoggingLevelLabel;
    private JComboBox<String> forgeLoggingLevel;

    private JLabelWithHover enableLeaderboardsLabel;
    private JCheckBox enableLeaderboards;

    private JLabelWithHover enableLoggingLabel;
    private JCheckBox enableLogs;

    private JLabelWithHover enableAnalyticsLabel;
    private JCheckBox enableAnalytics;

    private JLabelWithHover enableOpenEyeReportingLabel;
    private JCheckBox enableOpenEyeReporting;

    public LoggingSettingsTab() {
        // Forge Logging Level
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = LABEL_INSETS;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        forgeLoggingLevelLabel = new JLabelWithHover(GetText.tr("Forge Logging Level") + ":", HELP_ICON, "<html>"
                + GetText.tr("This determines the type of logging that Forge should report back to you.") + "</html>");
        add(forgeLoggingLevelLabel, gbc);

        gbc.gridx++;
        gbc.insets = FIELD_INSETS;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        forgeLoggingLevel = new JComboBox<>();
        forgeLoggingLevel.addItem("SEVERE");
        forgeLoggingLevel.addItem("WARNING");
        forgeLoggingLevel.addItem("INFO");
        forgeLoggingLevel.addItem("CONFIG");
        forgeLoggingLevel.addItem("FINE");
        forgeLoggingLevel.addItem("FINER");
        forgeLoggingLevel.addItem("FINEST");
        forgeLoggingLevel.setSelectedItem(App.settings.getForgeLoggingLevel());
        add(forgeLoggingLevel, gbc);

        // Enable Leaderboards

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = LABEL_INSETS;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        enableLeaderboardsLabel = new JLabelWithHover(GetText.tr("Enable Leaderboards") + "?", HELP_ICON,
                GetText.tr("If you want to participate in the Leaderboards."));
        add(enableLeaderboardsLabel, gbc);

        gbc.gridx++;
        gbc.insets = FIELD_INSETS;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        enableLeaderboards = new JCheckBox();
        if (App.settings.enableLeaderboards()) {
            enableLeaderboards.setSelected(true);
        }
        if (!App.settings.enableLogs()) {
            enableLeaderboards.setEnabled(false);
        }
        add(enableLeaderboards, gbc);

        // Enable Logging

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = LABEL_INSETS;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        enableLoggingLabel = new JLabelWithHover(GetText.tr("Enable Logging") + "?", HELP_ICON, "<html>" + GetText.tr(
                "The Launcher sends back anonymous usage and error logs<br/>to our servers in order to make the Launcher and Packs<br/>better. If you don't want this to happen then simply<br/>disable this option.")
                + "</html>");
        add(enableLoggingLabel, gbc);

        gbc.gridx++;
        gbc.insets = FIELD_INSETS;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        enableLogs = new JCheckBox();
        enableLogs.addActionListener(e -> {
            if (!enableLogs.isSelected()) {
                enableOpenEyeReporting.setSelected(false);
                enableOpenEyeReporting.setEnabled(false);
                enableLeaderboards.setSelected(false);
                enableLeaderboards.setEnabled(false);
            } else {
                enableOpenEyeReporting.setSelected(true);
                enableOpenEyeReporting.setEnabled(true);
                enableLeaderboards.setSelected(true);
                enableLeaderboards.setEnabled(true);
            }
        });
        if (App.settings.enableLogs()) {
            enableLogs.setSelected(true);
        }
        add(enableLogs, gbc);

        // Enable Analytics

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = LABEL_INSETS;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        enableAnalyticsLabel = new JLabelWithHover(GetText.tr("Enable Anonymous Analytics") + "?", HELP_ICON,
                "<html>" + GetText.tr(
                        "The Launcher sends back anonymous analytics to Google Analytics<br/>in order to track what people do and don't use in the launcher.<br/>This helps determine what new features we implement in the future.<br/>All analytics are anonymous and contain no user/instance information in it at all.<br/>If you don't want to send anonymous analytics, you can disable this option.")
                        + "</html>");
        add(enableAnalyticsLabel, gbc);

        gbc.gridx++;
        gbc.insets = FIELD_INSETS;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        enableAnalytics = new JCheckBox();
        if (App.settings.enableAnalytics()) {
            enableAnalytics.setSelected(true);
        }
        add(enableAnalytics, gbc);

        // Enable OpenEye Reporting

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = LABEL_INSETS;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        enableOpenEyeReportingLabel = new JLabelWithHover(GetText.tr("Enable OpenEye Reporting") + "?", HELP_ICON,
                "<html>" + Utils.splitMultilinedString(GetText.tr(
                        "OpenEye is a mod/project created by the OpenMods team which aims to help gather statistics and crash logs from Minecraft in order to help users and modders discover and fix issues with mods. With the OpenEye mod installed (each ModPack chooses if they wish to install it or not, it's not installed by default to all packs by the Launcher) everytime Minecraft crashes the OpenEye report is sent to OpenEye for analysis and if a note from the modder has been added on the cause/fix it will be displayed to you. For more information please see http://openeye.openblocks.info"),
                        80, "<br/>") + "</html>");
        add(enableOpenEyeReportingLabel, gbc);

        gbc.gridx++;
        gbc.insets = FIELD_INSETS;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        enableOpenEyeReporting = new JCheckBox();
        if (!App.settings.enableLogs()) {
            enableOpenEyeReporting.setEnabled(false);
        }
        if (App.settings.enableOpenEyeReporting()) {
            enableOpenEyeReporting.setSelected(true);
        }
        add(enableOpenEyeReporting, gbc);
    }

    public void save() {
        App.settings.setForgeLoggingLevel((String) forgeLoggingLevel.getSelectedItem());
        App.settings.setEnableLeaderboards(enableLeaderboards.isSelected());
        App.settings.setEnableLogs(enableLogs.isSelected());
        App.settings.setEnableAnalytics(enableAnalytics.isSelected());
        App.settings.setEnableOpenEyeReporting(enableOpenEyeReporting.isSelected());
    }

    @Override
    public String getTitle() {
        return GetText.tr("Logging");
    }
}
