package com.github.weisj.darkmode;

import com.intellij.ide.actions.QuickChangeLookAndFeel;
import com.intellij.ide.ui.LafManager;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.SystemInfo;

import javax.swing.*;

/**
 * Automatically changes the IDEA theme based on windows settings.
 */
public class AutoDarkMode implements Disposable {
    private static final Logger LOGGER = Logger.getInstance(AutoDarkMode.class);

    private final LafManager lafManager;
    private final AutoDarkModeOptions options;
    private final ThemeMonitor monitor;

    public AutoDarkMode(final LafManager lafManager) {
        options = ServiceManager.getService(AutoDarkModeOptions.class);
        this.lafManager = lafManager;
        if (!SystemInfo.isWin10OrNewer) {
            LOGGER.error("Plugin only supports Windows 10 or newer");
            monitor = null;
            return;
        }
        if (!DarkModeNative.loadLibrary()) {
            LOGGER.error("Could not load library.");
            monitor = null;
            return;
        }
        monitor = new ThemeMonitor(this::onThemeChange);
        monitor.setRunning(true);
    }

    private void onThemeChange(final boolean isDark, final boolean isHighContrast) {
        UIManager.LookAndFeelInfo current = lafManager.getCurrentLookAndFeel();
        UIManager.LookAndFeelInfo target = getTargetLaf(isDark, isHighContrast);
        if (!target.equals(current)) {
            updateLaf(target);
        }
    }

    private UIManager.LookAndFeelInfo getTargetLaf(final boolean dark, final boolean highContrast) {
        return highContrast && options.getCheckHighContrast() ? options.getHighContrastTheme()
                                                              : dark ? options.getDarkTheme()
                                                                     : options.getLightTheme();
    }

    private void updateLaf(final UIManager.LookAndFeelInfo targetLaf) {
        QuickChangeLookAndFeel.switchLafAndUpdateUI(lafManager, targetLaf, true);
    }

    @Override
    public void dispose() {
        if (monitor != null) monitor.setRunning(false);
    }
}