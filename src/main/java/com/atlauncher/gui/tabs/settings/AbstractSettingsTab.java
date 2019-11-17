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
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.atlauncher.App;
import com.atlauncher.gui.CustomLineBorder;
import com.atlauncher.gui.tabs.Tab;
import com.atlauncher.utils.Utils;

@SuppressWarnings("serial")
public abstract class AbstractSettingsTab extends JPanel implements Tab {
    final Insets LABEL_INSETS = new Insets(5, 0, 5, 10);
    final Insets FIELD_INSETS = new Insets(5, 0, 5, 0);
    final Insets LABEL_INSETS_SMALL = new Insets(0, 0, 0, 10);
    final Insets FIELD_INSETS_SMALL = new Insets(0, 0, 0, 0);

    final ImageIcon HELP_ICON = Utils.getIconImage("/assets/image/Help.png");
    final ImageIcon ERROR_ICON = Utils.getIconImage("/assets/image/Error.png");
    final ImageIcon WARNING_ICON = Utils.getIconImage("/assets/image/Warning.png");

    final Border RESTART_BORDER = BorderFactory.createEmptyBorder(0, 0, 0, 5);
    final Border HOVER_BORDER = new CustomLineBorder(5, App.THEME.getHoverBorderColor(), 2);

    final GridBagConstraints gbc;

    public AbstractSettingsTab() {
        setLayout(new GridBagLayout());
        this.gbc = new GridBagConstraints();
    }
}
