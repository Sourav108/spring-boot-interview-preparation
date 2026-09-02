package com.spring.interview.modern.model;

/**
 * Modern Java 21 Sealed Interface hierarchy with Records and Pattern Matching for switch.
 */
public sealed interface ModernUser permits ModernUser.Admin, ModernUser.Customer, ModernUser.Guest {

    String id();
    String username();

    record Admin(String id, String username, String role, int permissionsLevel) implements ModernUser {}
    record Customer(String id, String username, double accountBalance) implements ModernUser {}
    record Guest(String id, String username, long sessionExpiryEpoch) implements ModernUser {}

    static String formatUserSummary(ModernUser user) {
        // Exhaustive switch pattern matching with record deconstruction
        return switch (user) {
            case Admin(var id, var name, var role, var lvl) ->
                String.format("Admin '%s' (ID: %s, Role: %s, Level: %d)", name, id, role, lvl);
            case Customer(var id, var name, var bal) ->
                String.format("Customer '%s' (ID: %s, Balance: $%.2f)", name, id, bal);
            case Guest(var id, var name, var exp) ->
                String.format("Guest '%s' (ID: %s, Expiry: %d)", name, id, exp);
        };
    }
}
