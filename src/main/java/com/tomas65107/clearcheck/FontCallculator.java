package com.tomas65107.clearcheck;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Map;

public class FontCallculator {

    private static final Map<Character, Integer> CHAR_WIDTHS = Map.ofEntries(
            Map.entry('!', 1),
            Map.entry('"', 3),
            Map.entry('#', 5),
            Map.entry('$', 5),
            Map.entry('%', 5),
            Map.entry('&', 5),
            Map.entry('\'', 1),
            Map.entry('(', 3),
            Map.entry(')', 3),
            Map.entry('*', 3),
            Map.entry('+', 5),
            Map.entry(',', 1),
            Map.entry('-', 5),
            Map.entry('.', 1),
            Map.entry('/', 5),

            Map.entry('0', 5),
            Map.entry('1', 5),
            Map.entry('2', 5),
            Map.entry('3', 5),
            Map.entry('4', 5),
            Map.entry('5', 5),
            Map.entry('6', 5),
            Map.entry('7', 5),
            Map.entry('8', 5),
            Map.entry('9', 5),

            Map.entry(':', 1),
            Map.entry(';', 1),
            Map.entry('<', 4),
            Map.entry('=', 5),
            Map.entry('>', 4),
            Map.entry('?', 5),
            Map.entry('@', 6),

            Map.entry('A', 5),
            Map.entry('B', 5),
            Map.entry('C', 5),
            Map.entry('D', 5),
            Map.entry('E', 5),
            Map.entry('F', 5),
            Map.entry('G', 5),
            Map.entry('H', 5),
            Map.entry('I', 3),
            Map.entry('J', 5),
            Map.entry('K', 5),
            Map.entry('L', 5),
            Map.entry('M', 5),
            Map.entry('N', 5),
            Map.entry('O', 5),
            Map.entry('P', 5),
            Map.entry('Q', 5),
            Map.entry('R', 5),
            Map.entry('S', 5),
            Map.entry('T', 5),
            Map.entry('U', 5),
            Map.entry('V', 5),
            Map.entry('W', 5),
            Map.entry('X', 5),
            Map.entry('Y', 5),
            Map.entry('Z', 5),

            Map.entry('[', 3),
            Map.entry('\\', 5),
            Map.entry(']', 3),
            Map.entry('^', 5),
            Map.entry('_', 5),
            Map.entry('`', 2),

            Map.entry('a', 5),
            Map.entry('b', 5),
            Map.entry('c', 5),
            Map.entry('d', 5),
            Map.entry('e', 5),
            Map.entry('f', 4),
            Map.entry('g', 5),
            Map.entry('h', 5),
            Map.entry('i', 1),
            Map.entry('j', 5),
            Map.entry('k', 4),
            Map.entry('l', 2),
            Map.entry('m', 5),
            Map.entry('n', 5),
            Map.entry('o', 5),
            Map.entry('p', 5),
            Map.entry('q', 5),
            Map.entry('r', 5),
            Map.entry('s', 5),
            Map.entry('t', 3),
            Map.entry('u', 5),
            Map.entry('v', 5),
            Map.entry('w', 5),
            Map.entry('x', 5),
            Map.entry('y', 5),
            Map.entry('z', 5),

            Map.entry('{', 3),
            Map.entry('|', 1),
            Map.entry('}', 3),
            Map.entry('~', 6)
    );

    private static int getCharWidth(char character, boolean bold) {
        if (character == ' ') {
            return 4;
        }

        int width = CHAR_WIDTHS.getOrDefault(character, 3);

        if (bold) {
            width++;
        }

        return width + 1;
    }

    public static int getStringWidth(String text) {
        int width = 0;

        for (char character : text.toCharArray()) {
            width += getCharWidth(character, false);
        }

        return width;
    }

    public static Component fitLine(Component component) {
        final int TARGET_WIDTH = 190;
        final int ELLIPSIS_WIDTH = getStringWidth("...");

        // Calculate actual width while respecting formatting.
        final int[] width = {0};

        component.getVisualOrderText().accept((index, style, codePoint) -> {
            String character = new String(Character.toChars(codePoint));

            int charWidth = character.length() == 1
                    ? getCharWidth(character.charAt(0), style.isBold())
                    : 6;

            width[0] += charWidth;
            return true;
        });

        // Short enough: pad with spaces.
        if (width[0] <= TARGET_WIDTH) {
            MutableComponent result = component.copy();

            int remaining = TARGET_WIDTH - width[0];
            int spaceWidth = getCharWidth(' ', false);

            if (spaceWidth > 0) {
                result.append(Component.literal(
                        " ".repeat(remaining / spaceWidth)
                ));
            }

            return result;
        }

        // Too long: truncate and add "..."
        int maxWidth = TARGET_WIDTH - ELLIPSIS_WIDTH;

        MutableComponent result = Component.empty();
        final int[] currentWidth = {0};

        component.getVisualOrderText().accept((index, style, codePoint) -> {
            String character = new String(Character.toChars(codePoint));

            int charWidth = character.length() == 1
                    ? getCharWidth(character.charAt(0), style.isBold())
                    : 6;

            if (currentWidth[0] + charWidth > maxWidth) {
                return false;
            }

            result.append(
                    Component.literal(character)
                            .withStyle(style)
            );

            currentWidth[0] += charWidth;
            return true;
        });

        result.append(Component.literal("..."));

        return result;
    }
}
