package me.gustavo.ghsecundaryeconomy.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class APISection {

    private final String id;
    private final ItemStack material;
    private final int slot;
    private final int price;
    private final List<String> command;

}
