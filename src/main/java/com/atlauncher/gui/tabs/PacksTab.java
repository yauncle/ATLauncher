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
package com.atlauncher.gui.tabs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.atlauncher.App;
import com.atlauncher.data.Pack;
import com.atlauncher.evnt.listener.RelocalizationListener;
import com.atlauncher.evnt.manager.RelocalizationManager;
import com.atlauncher.evnt.manager.TabChangeManager;
import com.atlauncher.gui.card.NilCard;
import com.atlauncher.gui.card.PackCard;
import com.atlauncher.gui.dialogs.AddCursePackDialog;
import com.atlauncher.gui.dialogs.AddPackDialog;
import com.atlauncher.gui.panels.LoadingPanel;
import com.atlauncher.network.Analytics;

import org.mini2Dx.gettext.GetText;

@SuppressWarnings("serial")
public final class PacksTab extends JPanel implements Tab, RelocalizationListener {
    private final JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final JPanel contentPanel = new JPanel(new GridBagLayout());
    private final JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private final JButton addButton = new JButton(GetText.tr("Add Pack"));
    private final JButton addCurseButton = new JButton(GetText.tr("Add Curse Pack"));
    private final JButton clearButton = new JButton(GetText.tr("Clear"));
    private final JButton expandAllButton = new JButton(GetText.tr("Expand All"));
    private final JButton collapseAllButton = new JButton(GetText.tr("Collapse All"));
    private final JTextField searchField = new JTextField(16);
    private final JButton searchButton = new JButton(GetText.tr("Search"));
    private final JCheckBox serversBox = new JCheckBox(GetText.tr("Can Create Server"));
    private final JCheckBox privateBox = new JCheckBox(GetText.tr("Private Packs Only"));
    private final JCheckBox searchDescBox = new JCheckBox(GetText.tr("Search Description"));
    private NilCard nilCard;
    private boolean isVanilla;
    private boolean isFeatured;
    private boolean loaded = false;

    private List<PackCard> cards = new LinkedList<>();

    public PacksTab(boolean isFeatured, boolean isVanilla) {
        super(new BorderLayout());
        this.isFeatured = isFeatured;
        this.isVanilla = isVanilla;
        this.topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.contentPanel.setLayout(new GridBagLayout());

        final JScrollPane scrollPane = new JScrollPane(this.contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        this.add(scrollPane, BorderLayout.CENTER);

        if (!this.isFeatured && !this.isVanilla) {
            this.add(this.topPanel, BorderLayout.NORTH);
            this.add(this.bottomPanel, BorderLayout.SOUTH);
        }

        RelocalizationManager.addListener(this);

        this.setupTopPanel();

        addLoadingCard();

        refresh();

        TabChangeManager.addListener(() -> {
            searchField.setText("");
            serversBox.setSelected(false);
            privateBox.setSelected(false);
            searchDescBox.setSelected(false);
        });

        this.collapseAllButton.addActionListener(e -> {
            for (Component comp : contentPanel.getComponents()) {
                if (comp instanceof PackCard) {
                    ((PackCard) comp).setCollapsed(true);
                }
            }
        });
        this.expandAllButton.addActionListener(e -> {
            for (Component comp : contentPanel.getComponents()) {
                if (comp instanceof PackCard) {
                    ((PackCard) comp).setCollapsed(false);
                }
            }
        });
        this.addButton.addActionListener(e -> {
            new AddPackDialog();
            reload();
        });
        this.addCurseButton.addActionListener(e -> {
            new AddCursePackDialog();
        });
        this.clearButton.addActionListener(e -> {
            searchField.setText("");
            searchDescBox.setSelected(false);
            serversBox.setSelected(false);
            privateBox.setSelected(false);
            reload();
        });

        this.searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    Analytics.sendEvent(searchField.getText(), "Search", "Pack");
                    reload();
                }
            }
        });

        this.searchButton.addActionListener(e -> {
            Analytics.sendEvent(searchField.getText(), "Search", "Pack");
            reload();
        });

        this.privateBox.addItemListener(e -> reload());
        this.serversBox.addItemListener(e -> reload());
        this.searchDescBox.addItemListener(e -> reload());
    }

    private void addLoadingCard() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        this.contentPanel.add(new LoadingPanel(), gbc);
    }

    private void setupTopPanel() {
        this.topPanel.add(this.addButton);
        this.topPanel.add(this.addCurseButton);
        this.topPanel.add(this.clearButton);
        this.topPanel.add(this.searchField);
        this.topPanel.add(this.searchButton);
        this.topPanel.add(this.serversBox);
        this.topPanel.add(this.privateBox);
        this.topPanel.add(this.searchDescBox);

        this.bottomPanel.add(this.expandAllButton);
        this.bottomPanel.add(this.collapseAllButton);
    }

    private void loadPacks(boolean force) {
        if (!force && loaded) {
            return;
        }

        List<Pack> packs = App.settings.sortPacksAlphabetically()
                ? App.settings.getPacksSortedAlphabetically(this.isFeatured, this.isVanilla)
                : App.settings.getPacksSortedPositionally(this.isFeatured, this.isVanilla);

        for (Pack pack : packs) {
            if (pack.canInstall()) {
                PackCard card = new PackCard(pack);
                this.cards.add(card);
            }
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        loaded = true;
    }

    private void load(boolean keep) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        Pack pack;
        boolean show;
        int count = 0;
        for (PackCard card : this.cards) {
            show = true;
            pack = card.getPack();
            if (keep) {
                if (!this.searchField.getText().isEmpty()) {
                    if (!Pattern.compile(Pattern.quote(this.searchField.getText()), Pattern.CASE_INSENSITIVE)
                            .matcher(pack.getName()).find()) {
                        show = false;
                    }
                }

                if (this.searchDescBox.isSelected()) {
                    if (Pattern.compile(Pattern.quote(this.searchField.getText()), Pattern.CASE_INSENSITIVE)
                            .matcher(pack.getDescription()).find()) {
                        show = true;
                    }
                }

                if (this.serversBox.isSelected()) {
                    if (!pack.canCreateServer()) {
                        show = false;
                    }
                }

                if (privateBox.isSelected()) {
                    if (!pack.isPrivate()) {
                        show = false;
                    }
                }

                if (show) {
                    this.contentPanel.add(card, gbc);
                    gbc.gridy++;
                    count++;
                }
            }
        }

        if (count == 0) {
            nilCard = new NilCard(GetText.tr("There are no packs to display.\n\nPlease check back another time."));
            this.contentPanel.add(nilCard, gbc);
        }
    }

    public void reload() {
        this.contentPanel.removeAll();
        load(true);
        revalidate();
        repaint();
    }

    public void refresh() {
        this.cards.clear();
        loadPacks(true);
        this.contentPanel.removeAll();
        load(true);
        revalidate();
        repaint();
    }

    @Override
    public String getTitle() {
        return (this.isFeatured ? GetText.tr("Featured Packs")
                : (this.isVanilla ? GetText.tr("Vanilla Packs") : GetText.tr("Packs")));
    }

    @Override
    public void onRelocalization() {
        addButton.setText(GetText.tr("Add Pack"));
        addCurseButton.setText(GetText.tr("Add Curse Pack"));
        clearButton.setText(GetText.tr("Clear"));
        expandAllButton.setText(GetText.tr("Expand All"));
        collapseAllButton.setText(GetText.tr("Collapse All"));
        serversBox.setText(GetText.tr("Can Create Server"));
        privateBox.setText(GetText.tr("Private Packs Only"));
        searchDescBox.setText(GetText.tr("Search Description"));

        if (nilCard != null) {
            nilCard.setMessage(GetText.tr("There are no packs to display.\n\nPlease check back another time."));
        }
    }
}
