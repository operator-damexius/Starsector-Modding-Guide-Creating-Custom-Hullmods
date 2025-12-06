# Starsector Modding Guide: Creating Custom Hullmods

This reference guide explains how to script, register, and implement custom Hullmods (ship upgrades) in Starsector.

---

## 1. The Java Script (`data/hullmods/`)

Every hullmod starts with a Java class that extends `BaseHullMod`. This script defines what the upgrade actually *does* to the ship's stats.

### A. Basic Structure
Create a new file in `data/hullmods/` (e.g., `MyCustomMod.java`).

```java
package data.hullmods; // Must match your folder structure!

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class MyCustomMod extends BaseHullMod {

    // 1. Define your bonuses here for easy editing
    public static final float ARMOR_BONUS = 150f;
    public static final float RANGE_BONUS = 20f;

    // 2. Apply the stats
    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        
        // Modify Armor (Flat bonus)
        stats.getArmorBonus().modifyFlat(id, ARMOR_BONUS);
        
        // Modify Weapon Range (Percentage bonus)
        stats.getBallisticWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
        stats.getEnergyWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
    }

    // 3. Update the Tooltip Description
    // This fills in the %s symbols in your CSV description
    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return "" + (int) ARMOR_BONUS;
        if (index == 1) return "" + (int) RANGE_BONUS + "%";
        return null;
    }
}

2. Stat Modification Cheat Sheet
Copy these lines into applyEffectsBeforeShipCreation to apply specific bonuses.

Speed & Mobility

// Max Speed (+20%)
stats.getMaxSpeed().modifyPercent(id, 20f);

// Acceleration (+50%)
stats.getAcceleration().modifyPercent(id, 50f);

// Turn Rate (+30%)
stats.getMaxTurnRate().modifyPercent(id, 30f);

// Zero Flux Boost (Speed boost when not firing)
stats.getZeroFluxSpeedBoost().modifyFlat(id, 50f);

Weapons & Damage

// Range (+200 units flat)
stats.getBallisticWeaponRangeBonus().modifyFlat(id, 200f);

// Fire Rate (+15%)
stats.getBallisticRoFMult().modifyMult(id, 1.15f);

// Flux Cost (-10% cost to fire)
stats.getBallisticWeaponFluxCostMod().modifyMult(id, 0.9f);

// Recoil (-50%)
stats.getRecoilPerShotMult().modifyMult(id, 0.5f);

Defense & Flux

// Flux Capacity (+1000 flat)
stats.getFluxCapacity().modifyFlat(id, 1000f);

// Flux Dissipation (+10%)
stats.getFluxDissipation().modifyPercent(id, 10f);

// Shield Damage Taken (-20% damage taken)
stats.getShieldDamageTakenMult().modifyMult(id, 0.8f);

// Armor (+25%)
stats.getArmorBonus().modifyPercent(id, 25f);

Campaign Stats

// Burn Level (+1 speed on map)
stats.getMaxBurnLevel().modifyFlat(id, 1f);

// Sensor Range (+20%)
stats.getSensorStrength().modifyPercent(id, 20f);

// Maintenance Cost (-50%)
stats.getSuppliesPerMonth().modifyMult(id, 0.5f);

3. Registration (hull_mods.csv)
Once your script is written, you must register it in data/hullmods/hull_mods.csv. This tells the game the mod exists, what it costs, and what text to display.

Columns:

id: Unique ID (e.g., solvaris_grid).

name: Display name (e.g., "Solvaris Grid").

tier: Rarity (0 = Built-in/Hidden, 1 = Common, 2 = Rare, 3 = Legendary).

cost_frigate, cost_dest...: Ordnance Point (OP) cost.

desc: Tooltip text. Use %s for numbers defined in Java.

script: Path to your Java file (e.g., data.hullmods.MyCustomMod).

sprite: Path to icon image.

Example Entry:

id,name,tier,cost_frigate,cost_dest,cost_cruiser,cost_capital,desc,script,sprite
solvaris_grid,Solvaris Grid,0,0,0,0,0,A specialized network uplink. Increases armor by %s and range by %s.,data.hullmods.SolvarisGrid,graphics/icons/hullsys/high_energy_focus.png

4. Adding to Ships (Built-In)
To make a hullmod permanent and unremovable on a specific ship (like a Boss ship or Faction flagship), add it to the .ship file (JSON).

File: data/hulls/your_ship_id.ship

{
  "hullName": "Vortex",
  "hullId": "srss_vortex",
  ...
  "builtInMods": [
    "solvaris_grid",
    "solvaris_order_core"
  ],
  ...
}
