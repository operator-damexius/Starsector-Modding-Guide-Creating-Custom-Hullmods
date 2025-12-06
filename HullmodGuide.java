package data.scripts;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

/**
 * ============================================================================
 * STARSECTOR MODDING GUIDE: CREATING CUSTOM HULLMODS
 * ============================================================================
 * This reference class explains how to script, register, and implement 
 * custom Hullmods (ship upgrades).
 */
public class HullmodGuide {

    /*
     * --------------------------------------------------------------------
     * 1. THE JAVA SCRIPT STRUCTURE (data/hullmods/MyCustomMod.java)
     * --------------------------------------------------------------------
     * Every hullmod starts with a Java class that extends BaseHullMod.
     */
    
    // Example Class Structure:
    public static class MyCustomMod extends BaseHullMod {

        // A. Define your bonuses here for easy editing
        public static final float ARMOR_BONUS = 150f;
        public static final float RANGE_BONUS = 20f;

        // B. Apply the stats
        @Override
        public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
            
            // Modify Armor (Flat bonus)
            stats.getArmorBonus().modifyFlat(id, ARMOR_BONUS);
            
            // Modify Weapon Range (Percentage bonus)
            stats.getBallisticWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
            stats.getEnergyWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
        }

        // C. Update the Tooltip Description
        // This fills in the %s symbols in your CSV description
        @Override
        public String getDescriptionParam(int index, HullSize hullSize) {
            if (index == 0) return "" + (int) ARMOR_BONUS;
            if (index == 1) return "" + (int) RANGE_BONUS + "%";
            return null;
        }
    }

    /*
     * --------------------------------------------------------------------
     * 2. COMMON STAT MODIFICATIONS CHEAT SHEET
     * --------------------------------------------------------------------
     * Copy these lines into applyEffectsBeforeShipCreation to use them.
     */
    
    public void cheatSheet(MutableShipStatsAPI stats, String id) {
        
        // --- SPEED & MOBILITY ---
        stats.getMaxSpeed().modifyPercent(id, 20f);             // Max Speed (+20%)
        stats.getAcceleration().modifyPercent(id, 50f);         // Acceleration (+50%)
        stats.getMaxTurnRate().modifyPercent(id, 30f);          // Turn Rate (+30%)
        stats.getZeroFluxSpeedBoost().modifyFlat(id, 50f);      // Zero Flux Boost (+50 Su)

        // --- WEAPONS & DAMAGE ---
        stats.getBallisticWeaponRangeBonus().modifyFlat(id, 200f); // Range (+200 units flat)
        stats.getBallisticRoFMult().modifyMult(id, 1.15f);      // Fire Rate (+15%)
        stats.getBallisticWeaponFluxCostMod().modifyMult(id, 0.9f); // Flux Cost (-10%)
        stats.getRecoilPerShotMult().modifyMult(id, 0.5f);      // Recoil (-50%)

        // --- DEFENSE & FLUX ---
        stats.getFluxCapacity().modifyFlat(id, 1000f);          // Flux Capacity (+1000 flat)
        stats.getFluxDissipation().modifyPercent(id, 10f);      // Flux Dissipation (+10%)
        stats.getShieldDamageTakenMult().modifyMult(id, 0.8f);  // Shield Damage Taken (-20%)
        stats.getArmorBonus().modifyPercent(id, 25f);           // Armor (+25%)

        // --- CAMPAIGN STATS ---
        stats.getMaxBurnLevel().modifyFlat(id, 1f);             // Burn Level (+1 speed on map)
        stats.getSensorStrength().modifyPercent(id, 20f);       // Sensor Range (+20%)
        stats.getSuppliesPerMonth().modifyMult(id, 0.5f);       // Maintenance Cost (-50%)
    }

    /*
     * --------------------------------------------------------------------
     * 3. REGISTRATION (hull_mods.csv)
     * --------------------------------------------------------------------
     * You must register the script in `data/hullmods/hull_mods.csv`.
     * * COLUMNS:
     * - id:            Unique ID (e.g., solvaris_grid)
     * - name:          Display name (e.g., "Solvaris Grid")
     * - tier:          Rarity (0=Hidden, 1=Common, 2=Rare, 3=Legendary)
     * - cost_frigate:  Ordnance Point (OP) cost
     * - desc:          Tooltip text. Use %s for numbers defined in Java.
     * - script:        Path to Java file (e.g., data.hullmods.SolvarisGrid)
     * - sprite:        Path to icon image
     * * EXAMPLE ENTRY:
     * solvaris_grid,Solvaris Grid,0,0,0,0,0,"Increases armor by %s.",data.hullmods.SolvarisGrid,graphics/icons/icon.png
     */

    /*
     * --------------------------------------------------------------------
     * 4. ADDING TO SHIPS (Built-In)
     * --------------------------------------------------------------------
     * To make a hullmod permanent on a ship, add its ID to the .ship JSON file.
     * * FILE: data/hulls/srss_vortex.ship
     * * {
     * "hullName": "Vortex",
     * "hullId": "srss_vortex",
     * ...
     * "builtInMods": [
     * "solvaris_grid",
     * "solvaris_order_core"
     * ],
     * ...
     * }
     */
}