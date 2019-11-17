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

import com.atlauncher.App;
import com.atlauncher.gui.components.JLabelWithHover;

import org.mini2Dx.gettext.GetText;

@SuppressWarnings("serial")
public class BackupsSettingsTab extends AbstractSettingsTab {
    private JLabelWithHover enableModsBackupsLabel;
    private JCheckBox enableModsBackups;

    public BackupsSettingsTab() {
        // Enable mods backups
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = LABEL_INSETS;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        enableModsBackupsLabel = new JLabelWithHover(GetText.tr("Enable Mods Backups") + "?", HELP_ICON,
                GetText.tr("If we should backup mods when creating a backup for an instance."));
        add(enableModsBackupsLabel, gbc);

        gbc.gridx++;
        gbc.insets = FIELD_INSETS;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        enableModsBackups = new JCheckBox();
        enableModsBackups.setSelected(App.settings.enableModsBackups());
        add(enableModsBackups, gbc);
    }

    public void save() {
        App.settings.setEnableModsBackups(enableModsBackups.isSelected());
    }

    @Override
    public String getTitle() {
        return GetText.tr("Backups");
    }
}
