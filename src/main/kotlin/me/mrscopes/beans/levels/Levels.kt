package me.mrscopes.beans.levels

import me.mrscopes.beans.utilities.color
import net.kyori.adventure.text.TextComponent

class Levels {
    companion object {
        fun levelDisplay(level: Int): TextComponent {
            var color = ""

            if (level < 10) color = "7"
            else if (level < 20) color = "b"
            else if (level < 30) color = "e"
            else if (level < 40) color = "a"
            else if (level < 50) color = "5"
            else if (level < 60) color = "2"
            else if (level < 70) color = "3"
            else if (level < 80) color = "6"
            else if (level < 90) color = "c"
            else if (level < 100) color = "4"
            else color = "4&l"

            return "&8[&$color$level&8]".color()
        }
    }
}