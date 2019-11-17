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
import javax.swing.JTextField;

import com.atlauncher.App;
import com.atlauncher.evnt.listener.RelocalizationListener;
import com.atlauncher.evnt.manager.RelocalizationManager;
import com.atlauncher.gui.components.JLabelWithHover;
import com.atlauncher.managers.DialogManager;
import com.atlauncher.utils.Utils;

import org.mini2Dx.gettext.GetText;

@SuppressWarnings("serial")
public class ToolsSettingsTab extends AbstractSettingsTab implements RelocalizationListener {
    private JLabelWithHover enableServerCheckerLabel;
    private JCheckBox enableServerChecker;

    private JLabelWithHover serverCheckerWaitLabel;
    private JTextField serverCheckerWait;

    public ToolsSettingsTab() {
        RelocalizationManager.addListener(this);
        // Enable Server Checker
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = LABEL_INSETS;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        enableServerCheckerLabel = new JLabelWithHover(GetText.tr("Enable Server Checker") + "?", HELP_ICON, GetText
                .tr("This setting enables or disables the checking of added servers in the Server Checker Tool."));
        add(enableServerCheckerLabel, gbc);

        gbc.gridx++;
        gbc.insets = FIELD_INSETS;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        enableServerChecker = new JCheckBox();
        if (App.settings.enableServerChecker()) {
            enableServerChecker.setSelected(true);
        }
        enableServerChecker.addActionListener(e -> {
            if (!enableServerChecker.isSelected()) {
                serverCheckerWait.setEnabled(false);
            } else {
                serverCheckerWait.setEnabled(true);
            }
        });
        add(enableServerChecker, gbc);

        // Server Checker Wait Settings
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = LABEL_INSETS;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        serverCheckerWaitLabel = new JLabelWithHover(GetText.tr("Time Between Checks") + ":", HELP_ICON,
                "<html>" + Utils.splitMultilinedString(GetText.tr(
                        "This option controls how long the launcher should wait between checking servers in the server checker. This value is in minutes and should be between 1 and 30, with the default being 5."),
                        75, "<br/>") + "</html>");
        add(serverCheckerWaitLabel, gbc);

        gbc.gridx++;
        gbc.insets = FIELD_INSETS;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        serverCheckerWait = new JTextField(4);
        serverCheckerWait.setText(App.settings.getServerCheckerWait() + "");
        if (!App.settings.enableServerChecker()) {
            serverCheckerWait.setEnabled(false);
        }
        add(serverCheckerWait, gbc);
    }

    public boolean isValidServerCheckerWait() {
        if (Integer.parseInt(serverCheckerWait.getText().replaceAll("[^0-9]", "")) < 1
                || Integer.parseInt(serverCheckerWait.getText().replaceAll("[^0-9]", "")) > 30) {
            DialogManager.okDialog().setTitle(GetText.tr("Help"))
                    .setContent(GetText.tr(
                            "The server checker wait time you specified is invalid. Please check it and try again."))
                    .setType(DialogManager.ERROR).show();
            return false;
        }
        return true;
    }

    public boolean needToRestartServerChecker() {
        return ((enableServerChecker.isSelected() != App.settings.enableServerChecker()) || (App.settings
                .getServerCheckerWait() != Integer.parseInt(serverCheckerWait.getText().replaceAll("[^0-9]", ""))));
    }

    public void save() {
        App.settings.setEnableServerChecker(enableServerChecker.isSelected());
        App.settings.setServerCheckerWait(Integer.parseInt(serverCheckerWait.getText().replaceAll("[^0-9]", "")));
    }

    @Override
    public String getTitle() {
        return GetText.tr("Tools");
    }

    @Override
    public void onRelocalization() {
        this.enableServerCheckerLabel.setText(GetText.tr("Enable Server Checker") + "?");
        this.enableServerCheckerLabel.setToolTipText(GetText
                .tr("This setting enables or disables the checking of added servers in the Server Checker Tool."));

        this.serverCheckerWaitLabel.setText(GetText.tr("Time Between Checks") + ":");
        this.serverCheckerWaitLabel.setToolTipText("<html>" + Utils.splitMultilinedString(GetText.tr(
                "This option controls how long the launcher should wait between checking servers in the server checker. This value is in minutes and should be between 1 and 30, with the default being 5."),
                75, "<br/>") + "</html>");
    }
}
