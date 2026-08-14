package com.tomas65107.clearcheck;

import com.google.common.base.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.text.BreakIterator;
import java.util.*;
import java.util.List;

public final class TextCutter {

    public static List<Component> cutTextComponent(Component c, boolean useNewLinesInText) {
        return cutTextComponent(c, 0, 200, useNewLinesInText);
    }

    public static List<Component> cutTextComponent(Component c,
                                                   int indent,
                                                   int maxwidthperline,
                                                   boolean useNewLinesInText) {
        //Logic lifted from create mod! thanks
        if (c == null) return List.of(Component.empty());
        String s = c.getString();

        // Split input into lines if useNewLinesInText is true
        List<String> preLines;
        if (useNewLinesInText) {
            preLines = Arrays.asList(s.split("\n", -1)); // keep empty lines
        } else {
            preLines = Collections.singletonList(s);
        }

        Font font = Minecraft.getInstance().font;
        List<String> lines = new LinkedList<>();

        for (String line : preLines) {
            // Split words using BreakIterator
            List<String> words = new LinkedList<>();
            BreakIterator iterator = BreakIterator.getLineInstance(Minecraft.getInstance().getLocale());
            iterator.setText(line);
            int start = iterator.first();
            for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
                String word = line.substring(start, end);
                words.add(word);
            }

            // Apply hard wrap (your existing logic)
            StringBuilder currentLine = new StringBuilder();
            int width = 0;
            for (String word : words) {
                int newWidth = font.width(word.replaceAll("_", "_"));
                if (width + newWidth > maxwidthperline) {
                    if (width > 0) {
                        lines.add(currentLine.toString());
                        currentLine = new StringBuilder();
                        width = 0;
                    } else {
                        lines.add(word);
                        continue;
                    }
                }
                currentLine.append(word);
                width += newWidth;
            }
            if (width > 0) {
                lines.add(currentLine.toString());
            }
        }

        // Format
        MutableComponent lineStart = Component.literal(Strings.repeat(" ", indent));
        List<Component> formattedLines = new ArrayList<>(lines.size());
        for (String l : lines) {
            formattedLines.add(lineStart.copy().append(l));
        }

        return formattedLines;
    }


}
